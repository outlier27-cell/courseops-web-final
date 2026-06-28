import {
  mockAnalytics,
  mockCourses,
  mockFeedback,
  mockHistories,
  mockLogs,
  mockMaterials,
  mockMembers,
  mockNotifications,
  mockProjects,
  mockSubmissions,
  mockUsers,
} from '../data/mockCourseOps'
import type {
  AnalyticsSnapshot,
  Course,
  CourseProject,
  MaterialHistory,
  MaterialItem,
  MaterialStatus,
  NotificationItem,
  OperationLog,
  Submission,
  TeacherFeedback,
  TeamMember,
  UploadResult,
  User,
} from '../domain/types'

type ApiResult<T> = {
  code: number
  message: string
  data: T
}

export type ApiRuntimeState = {
  baseUrl: string
  usingMock: boolean
  fallbackCount: number
  lastError: string
}

export type LoginResponse = {
  token: string
  user: User
}

const API_BASE = import.meta.env.VITE_COURSEOPS_API_BASE ?? 'http://127.0.0.1:8080/api'
const USE_MOCK_FALLBACK = import.meta.env.VITE_COURSEOPS_MOCK_FALLBACK !== 'false'
const AUTH_TOKEN_KEY = 'courseops-token'
const apiRuntimeState: ApiRuntimeState = {
  baseUrl: API_BASE,
  usingMock: false,
  fallbackCount: 0,
  lastError: '',
}
const wait = (ms = 160) => new Promise((resolve) => window.setTimeout(resolve, ms))
const clone = <T>(value: T): T => structuredClone(value)

export function getApiToken() {
  return window.localStorage.getItem(AUTH_TOKEN_KEY) ?? ''
}

export function setApiToken(token: string) {
  window.localStorage.setItem(AUTH_TOKEN_KEY, token)
}

export function clearApiToken() {
  window.localStorage.removeItem(AUTH_TOKEN_KEY)
}

export function getApiRuntimeState(): ApiRuntimeState {
  return { ...apiRuntimeState }
}

function markApiHealthy() {
  apiRuntimeState.usingMock = false
  apiRuntimeState.lastError = ''
}

function markApiFallback(error: unknown) {
  apiRuntimeState.usingMock = true
  apiRuntimeState.fallbackCount += 1
  apiRuntimeState.lastError = error instanceof Error ? error.message : '后端连接失败'
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const token = getApiToken()
  const response = await fetch(`${API_BASE}${path}`, {
    ...init,
    headers: {
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(init?.body instanceof FormData ? {} : { 'Content-Type': 'application/json' }),
      ...(init?.headers ?? {}),
    },
  })
  if (!response.ok) throw new Error(`HTTP ${response.status}`)
  const result = await response.json() as ApiResult<T>
  if (result.code !== 0) throw new Error(result.message)
  markApiHealthy()
  return result.data
}

async function withFallback<T>(apiCall: () => Promise<T>, fallback: () => T | Promise<T>) {
  try {
    return await apiCall()
  } catch (error) {
    if (error instanceof Error && /^HTTP (401|403)$/.test(error.message)) throw error
    if (!USE_MOCK_FALLBACK) throw error
    markApiFallback(error)
    await wait()
    return fallback()
  }
}

export async function login(userId: string, password: string): Promise<LoginResponse> {
  try {
    return await request<LoginResponse>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ userId, password }),
    })
  } catch (error) {
    if (!USE_MOCK_FALLBACK || (error instanceof Error && /^HTTP (401|403)$/.test(error.message))) throw error
    const user = mockUsers.find((item) => item.id === userId)
    if (!user || password !== '123456') throw error
    markApiFallback(error)
    await wait()
    return {
      token: `mock-token-${user.id}`,
      user: clone(user),
    }
  }
}

export async function getCurrentUser() {
  return request<User | null>('/auth/me')
}

export async function getUsers() {
  return withFallback<User[]>(() => request<User[]>('/auth/users'), () => clone(mockUsers))
}

export async function getCourses() {
  return withFallback<Course[]>(() => request<Course[]>('/courses'), () => clone(mockCourses))
}

export async function getProjects() {
  return withFallback<CourseProject[]>(() => request<CourseProject[]>('/projects'), () => clone(mockProjects))
}

export async function getMaterialTasks() {
  return withFallback<MaterialItem[]>(() => request<MaterialItem[]>('/materials'), () => clone(mockMaterials))
}

export async function getTeamMembers() {
  return withFallback<TeamMember[]>(() => request<TeamMember[]>('/projects/p-web-final/members').then(async (first) => {
    const second = await request<TeamMember[]>('/projects/p-db-er/members')
    const third = await request<TeamMember[]>('/projects/p-writing/members')
    return [...first, ...second, ...third]
  }), () => clone(mockMembers))
}

export async function getSubmissions() {
  return withFallback<Submission[]>(() => request<Submission[]>('/submissions'), () => clone(mockSubmissions))
}

export async function getTeacherFeedback() {
  return withFallback<TeacherFeedback[]>(() => request<TeacherFeedback[]>('/feedback'), () => clone(mockFeedback))
}

export async function getMaterialHistories() {
  return withFallback<MaterialHistory[]>(async () => {
    const materials = await getMaterialTasks()
    const groups = await Promise.all(materials.map((material) => request<MaterialHistory[]>(`/materials/${material.id}/histories`)))
    return groups.flat()
  }, () => clone(mockHistories))
}

export async function getNotifications() {
  return withFallback<NotificationItem[]>(() => request<NotificationItem[]>('/notifications'), () => clone(mockNotifications))
}

export async function getAnalytics() {
  return withFallback<AnalyticsSnapshot>(() => request<AnalyticsSnapshot>('/analytics'), () => clone(mockAnalytics))
}

export async function getOperationLogs() {
  return withFallback<OperationLog[]>(() => request<OperationLog[]>('/logs'), () => clone(mockLogs))
}

export async function updateTaskStatus(id: string, status: MaterialStatus) {
  return withFallback(() => request<{ id: string; status: MaterialStatus }>(`/materials/${id}/status`, {
    method: 'PATCH',
    body: JSON.stringify({ status, actorId: '张老师' }),
  }), () => ({ id, status }))
}

export async function uploadMaterial(id: string, file: File, submittedBy: string): Promise<UploadResult> {
  const formData = new FormData()
  formData.append('file', file)
  return withFallback(() => request<UploadResult>(`/materials/${id}/upload?submittedBy=${encodeURIComponent(submittedBy)}`, {
    method: 'POST',
    body: formData,
  }), async () => {
    await wait(280)
    return {
      materialId: id,
      fileName: file.name,
      fileSize: file.size,
      fileType: file.type || file.name.split('.').pop() || 'unknown',
      submittedAt: new Date().toLocaleString('zh-CN', { hour12: false }),
      submittedBy,
      status: 'submitted',
      message: '文件已进入本地演示提交队列，接入后端后将替换为真实上传。',
    }
  })
}

export async function appendOperationLog(log: OperationLog) {
  return withFallback(() => request('/logs', {
    method: 'POST',
    body: JSON.stringify(log),
  }), () => clone(log))
}
