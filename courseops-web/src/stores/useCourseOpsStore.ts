import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { materialStatusOrder } from '../domain/constants'
import type {
  AnalyticsSnapshot,
  Course,
  CourseProject,
  MaterialHistory,
  MaterialItem,
  MaterialStatus,
  NotificationItem,
  OperationLog,
  SearchResult,
  Submission,
  TeacherFeedback,
  TeamMember,
  User,
  UserRole,
} from '../domain/types'
import {
  appendOperationLog,
  clearApiToken,
  getAnalytics,
  getApiRuntimeState,
  getCourses,
  getMaterialHistories,
  getMaterialTasks,
  getNotifications,
  getOperationLogs,
  getProjects,
  getSubmissions,
  getTeacherFeedback,
  getTeamMembers,
  getUsers,
  getCurrentUser,
  updateTaskStatus,
  uploadMaterial,
} from '../services/courseopsApi'

const STORAGE_KEY = 'uibe-courseops-platform-state-v2'

type PersistedState = {
  projects?: CourseProject[]
  materials?: MaterialItem[]
  submissions?: Submission[]
  logs?: OperationLog[]
  histories?: MaterialHistory[]
  notifications?: NotificationItem[]
  feedback?: TeacherFeedback[]
  currentUserId?: string
  selectedProjectId?: string
  selectedMaterialId?: string
}

export const useCourseOpsStore = defineStore('courseops', () => {
  const users = ref<User[]>([])
  const courses = ref<Course[]>([])
  const projects = ref<CourseProject[]>([])
  const materials = ref<MaterialItem[]>([])
  const members = ref<TeamMember[]>([])
  const submissions = ref<Submission[]>([])
  const feedback = ref<TeacherFeedback[]>([])
  const histories = ref<MaterialHistory[]>([])
  const logs = ref<OperationLog[]>([])
  const notifications = ref<NotificationItem[]>([])
  const analytics = ref<AnalyticsSnapshot | null>(null)

  const currentUserId = ref('u-student-001')
  const selectedProjectId = ref('p-web-final')
  const selectedMaterialId = ref('m-report')
  const searchQuery = ref('')
  const isLoading = ref(false)
  const errorMessage = ref('')
  const apiStatus = ref(getApiRuntimeState())

  const currentUser = computed(() => users.value.find((user) => user.id === currentUserId.value) ?? users.value[0])
  const currentRole = computed<UserRole>(() => currentUser.value?.role ?? 'student')
  const selectedProject = computed(() => projects.value.find((project) => project.id === selectedProjectId.value) ?? projects.value[0])
  const selectedMaterial = computed(() => materials.value.find((material) => material.id === selectedMaterialId.value) ?? materials.value[0])
  const selectedProjectMaterials = computed(() => materials.value.filter((material) => material.projectId === selectedProjectId.value))
  const selectedProjectMembers = computed(() => members.value.filter((member) => member.projectId === selectedProjectId.value))
  const selectedProjectFeedback = computed(() => feedback.value.filter((item) => item.projectId === selectedProjectId.value))
  const selectedProjectSubmissions = computed(() => submissions.value.filter((submission) => selectedProjectMaterials.value.some((material) => material.id === submission.materialId)))
  const selectedMaterialHistory = computed(() => histories.value.filter((history) => history.materialId === selectedMaterialId.value))
  const selectedMaterialSubmissions = computed(() => submissions.value.filter((submission) => submission.materialId === selectedMaterialId.value))
  const selectedMaterialFeedback = computed(() => feedback.value.filter((item) => item.materialId === selectedMaterialId.value))
  const unreadNotifications = computed(() => notifications.value.filter((item) => item.userId === currentUserId.value && !item.read))
  const currentUserNotifications = computed(() => notifications.value.filter((item) => item.userId === currentUserId.value))
  const dueSoonMaterials = computed(() => materials.value.filter((material) => ['not_started', 'preparing', 'revision_required'].includes(material.status)).slice(0, 5))
  const materialColumns = computed(() => materialStatusOrder.map((status) => ({
    status,
    materials: materials.value.filter((material) => material.status === status),
  })))

  const studentTodos = computed(() => materials.value.filter((material) => ['preparing', 'revision_required', 'not_started'].includes(material.status)))
  const teacherPendingReviews = computed(() => materials.value.filter((material) => material.status === 'submitted'))
  const adminRiskProjects = computed(() => projects.value.filter((project) => project.riskLevel !== 'low'))

  const searchResults = computed<SearchResult[]>(() => {
    const query = searchQuery.value.trim().toLowerCase()
    if (!query) return []
    const includes = (value?: string) => Boolean(value?.toLowerCase().includes(query))
    const results: SearchResult[] = []

    courses.value.forEach((course) => {
      if ([course.name, course.teacher, course.assistant].some(includes)) {
        results.push({
          id: course.id,
          type: 'course',
          title: course.name,
          description: `${course.teacher} / ${course.semester}`,
          targetRoute: '/app/projects',
        })
      }
    })

    projects.value.forEach((project) => {
      if ([project.title, project.description, project.owner, ...project.members].some(includes)) {
        results.push({
          id: project.id,
          type: 'project',
          title: project.title,
          description: `${project.phase} / ${project.deadline}`,
          targetRoute: `/app/projects/${project.id}`,
        })
      }
    })

    materials.value.forEach((material) => {
      if ([material.title, material.description, material.assignee, material.latestFile].some(includes)) {
        results.push({
          id: material.id,
          type: 'material',
          title: material.title,
          description: `${material.assignee} / ${material.format} / ${material.deadline}`,
          targetRoute: `/app/tasks?materialId=${material.id}`,
        })
      }
    })

    submissions.value.forEach((submission) => {
      if ([submission.fileName, submission.submittedBy, submission.feedback].some(includes)) {
        results.push({
          id: submission.id,
          type: 'submission',
          title: submission.fileName,
          description: `${submission.submittedBy} / ${submission.submittedAt}`,
          targetRoute: `/app/tasks?materialId=${submission.materialId}`,
        })
      }
    })

    logs.value.forEach((log) => {
      if ([log.actor, log.action, log.targetName, log.source].some(includes)) {
        results.push({
          id: log.id,
          type: 'log',
          title: `${log.action} / ${log.targetName}`,
          description: `${log.actor} / ${log.createdAt}`,
          targetRoute: '/app/logs',
        })
      }
    })

    return results.slice(0, 24)
  })

  async function loadInitialData() {
    isLoading.value = true
    errorMessage.value = ''
    try {
      let current = null as Awaited<ReturnType<typeof getCurrentUser>>
      try {
        current = await getCurrentUser()
      } catch {
        current = null
      }
      const [userData, courseData, projectData, materialData, memberData, submissionData, feedbackData, historyData, logData, notificationData, analyticsData] = await Promise.all([
        getUsers(),
        getCourses(),
        getProjects(),
        getMaterialTasks(),
        getTeamMembers(),
        getSubmissions(),
        getTeacherFeedback(),
        getMaterialHistories(),
        getOperationLogs(),
        getNotifications(),
        getAnalytics(),
      ])
      users.value = userData
      if (current?.id) currentUserId.value = current.id
      courses.value = courseData
      projects.value = projectData
      materials.value = materialData
      members.value = memberData
      submissions.value = submissionData
      feedback.value = feedbackData
      histories.value = historyData
      logs.value = logData
      notifications.value = notificationData
      analytics.value = analyticsData
      restoreLocalState()
    } catch (error) {
      if (error instanceof Error && /^HTTP (401|403)$/.test(error.message)) {
        clearApiToken()
        window.localStorage.removeItem(STORAGE_KEY)
        window.location.assign('/login')
        return
      }
      errorMessage.value = '数据加载失败，请刷新页面重试。'
    } finally {
      apiStatus.value = getApiRuntimeState()
      isLoading.value = false
    }
  }

  function persistLocalState() {
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify({
      projects: projects.value,
      materials: materials.value,
      submissions: submissions.value,
      logs: logs.value,
      histories: histories.value,
      notifications: notifications.value,
      feedback: feedback.value,
      currentUserId: currentUserId.value,
      selectedProjectId: selectedProjectId.value,
      selectedMaterialId: selectedMaterialId.value,
    }))
  }

  function restoreLocalState() {
    try {
      const saved = window.localStorage.getItem(STORAGE_KEY)
      if (!saved) return
      const parsed = JSON.parse(saved) as PersistedState
      if (Array.isArray(parsed.projects) && parsed.projects.length) projects.value = parsed.projects
      if (Array.isArray(parsed.materials) && parsed.materials.length) materials.value = parsed.materials
      if (Array.isArray(parsed.submissions) && parsed.submissions.length) submissions.value = parsed.submissions
      if (Array.isArray(parsed.logs) && parsed.logs.length) logs.value = parsed.logs
      if (Array.isArray(parsed.histories) && parsed.histories.length) histories.value = parsed.histories
      if (Array.isArray(parsed.notifications) && parsed.notifications.length) notifications.value = parsed.notifications
      if (Array.isArray(parsed.feedback) && parsed.feedback.length) feedback.value = parsed.feedback
      if (parsed.currentUserId) currentUserId.value = parsed.currentUserId
      if (parsed.selectedProjectId) selectedProjectId.value = parsed.selectedProjectId
      if (parsed.selectedMaterialId) selectedMaterialId.value = parsed.selectedMaterialId
    } catch {
      window.localStorage.removeItem(STORAGE_KEY)
    }
  }

  function resetDemoState() {
    window.localStorage.removeItem(STORAGE_KEY)
    currentUserId.value = 'u-student-001'
    selectedProjectId.value = 'p-web-final'
    selectedMaterialId.value = 'm-report'
    searchQuery.value = ''
    users.value = []
    courses.value = []
    projects.value = []
    materials.value = []
    members.value = []
    submissions.value = []
    feedback.value = []
    histories.value = []
    logs.value = []
    notifications.value = []
    analytics.value = null
  }

  function refreshApiStatus() {
    apiStatus.value = getApiRuntimeState()
  }

  function clearError() {
    errorMessage.value = ''
  }

  function switchRole(role: UserRole) {
    const user = users.value.find((item) => item.role === role)
    if (user) currentUserId.value = user.id
    persistLocalState()
  }

  function replaceCurrentUser(userId: string) {
    currentUserId.value = userId
    persistLocalState()
  }

  function selectProject(id: string) {
    selectedProjectId.value = id
    persistLocalState()
  }

  function selectMaterial(id: string) {
    selectedMaterialId.value = id
    const material = materials.value.find((item) => item.id === id)
    if (material) selectedProjectId.value = material.projectId
    persistLocalState()
  }

  function createLog(payload: Omit<OperationLog, 'id' | 'actor' | 'actorRole' | 'createdAt'>) {
    const log: OperationLog = {
      id: `mongo-${Date.now()}`,
      actor: currentUser.value?.name ?? '系统',
      actorRole: currentRole.value,
      createdAt: new Date().toLocaleString('zh-CN', { hour12: false }),
      ...payload,
    }
    logs.value.unshift(log)
    void appendOperationLog(log).finally(refreshApiStatus)
    return log
  }

  function createHistory(payload: Omit<MaterialHistory, 'id' | 'actor' | 'createdAt'>) {
    histories.value.unshift({
      id: `mh-${Date.now()}`,
      actor: currentUser.value?.name ?? '系统',
      createdAt: new Date().toLocaleString('zh-CN', { hour12: false }),
      ...payload,
    })
  }

  function createNotification(payload: Omit<NotificationItem, 'id' | 'createdAt' | 'read'>) {
    notifications.value.unshift({
      id: `n-${Date.now()}`,
      read: false,
      createdAt: new Date().toLocaleString('zh-CN', { hour12: false }),
      ...payload,
    })
  }

  async function changeMaterialStatus(materialId: string, status: MaterialStatus) {
    clearError()
    const material = materials.value.find((item) => item.id === materialId)
    if (!material) return
    const before = material.status
    try {
      const result = await updateTaskStatus(materialId, status)
      material.status = result.status
      material.progress = status === 'approved' ? 100 : Math.max(material.progress, 72)
      createHistory({ materialId, action: '修改材料状态', fromStatus: before, toStatus: status })
      createLog({
        action: status === 'approved' ? '标记已通过' : status === 'revision_required' ? '标记需修改' : '更新材料状态',
        targetType: 'material',
        targetName: material.title,
        before,
        after: status,
        source: '材料流转中心',
      })
      createNotification({
        userId: currentRole.value === 'teacher' ? 'u-student-001' : currentUserId.value,
        title: `${material.title} 状态已更新`,
        description: `状态从 ${before} 变为 ${status}`,
        type: status === 'approved' ? 'submission' : 'feedback',
        relatedTargetId: materialId,
      })
      persistLocalState()
    } catch {
      errorMessage.value = '状态更新失败，请稍后重试。'
    } finally {
      refreshApiStatus()
    }
  }

  async function submitMaterialFile(materialId: string, file: File | null) {
    clearError()
    const material = materials.value.find((item) => item.id === materialId)
    if (!material) return
    if (!file) {
      errorMessage.value = '请选择要上传的文件。'
      return
    }
    if (file.size > 50 * 1024 * 1024) {
      errorMessage.value = '文件过大，请上传 50MB 以内的文件。'
      return
    }
    const extension = file.name.split('.').pop()?.toUpperCase()
    if (extension !== material.format) {
      errorMessage.value = `该材料要求 ${material.format} 格式，请重新选择文件。`
      return
    }
    try {
      const before = material.status
      const previousFile = material.latestFile
      const result = await uploadMaterial(materialId, file, currentUser.value?.name ?? '当前用户')
      material.status = result.status
      material.latestFile = result.fileName
      material.progress = Math.max(material.progress, 88)
      submissions.value.unshift({
        id: `s-${Date.now()}`,
        materialId,
        fileName: result.fileName,
        fileUrl: '#',
        fileSize: result.fileSize,
        submittedBy: result.submittedBy,
        submittedAt: result.submittedAt,
        status: result.status,
        feedback: result.message,
      })
      createHistory({
        materialId,
        action: '上传文件',
        fromStatus: before,
        toStatus: result.status,
        fileName: result.fileName,
      })
      createLog({
        action: '上传文件',
        targetType: 'submission',
        targetName: material.title,
        before: previousFile || '本地文件未提交',
        after: result.fileName,
        source: '材料流转中心',
      })
      createNotification({
        userId: currentUserId.value,
        title: `${material.title} 已上传`,
        description: `${result.fileName} 已进入提交记录。`,
        type: 'submission',
        relatedTargetId: materialId,
      })
      persistLocalState()
    } catch {
      errorMessage.value = '上传失败，请检查文件后重试。'
    } finally {
      refreshApiStatus()
    }
  }

  function resolveFeedback(feedbackId: string) {
    const item = feedback.value.find((entry) => entry.id === feedbackId)
    if (!item) return
    item.status = 'resolved'
    createLog({
      action: '学生处理反馈',
      targetType: 'feedback',
      targetName: item.content,
      before: 'unresolved',
      after: 'resolved',
      source: '项目空间',
    })
    persistLocalState()
  }

  function markNotificationRead(id: string) {
    const notification = notifications.value.find((item) => item.id === id)
    if (!notification) return
    notification.read = true
    createLog({
      action: '标记通知已读',
      targetType: 'notification',
      targetName: notification.title,
      before: '未读',
      after: '已读',
      source: '通知中心',
    })
    persistLocalState()
  }

  return {
    users,
    courses,
    projects,
    materials,
    members,
    submissions,
    feedback,
    histories,
    logs,
    notifications,
    analytics,
    currentUserId,
    selectedProjectId,
    selectedMaterialId,
    searchQuery,
    isLoading,
    errorMessage,
    apiStatus,
    currentUser,
    currentRole,
    selectedProject,
    selectedMaterial,
    selectedProjectMaterials,
    selectedProjectMembers,
    selectedProjectFeedback,
    selectedProjectSubmissions,
    selectedMaterialHistory,
    selectedMaterialSubmissions,
    selectedMaterialFeedback,
    unreadNotifications,
    currentUserNotifications,
    dueSoonMaterials,
    materialColumns,
    studentTodos,
    teacherPendingReviews,
    adminRiskProjects,
    searchResults,
    loadInitialData,
    persistLocalState,
    restoreLocalState,
    resetDemoState,
    refreshApiStatus,
    clearError,
    switchRole,
    replaceCurrentUser,
    selectProject,
    selectMaterial,
    changeMaterialStatus,
    submitMaterialFile,
    resolveFeedback,
    markNotificationRead,
  }
})
