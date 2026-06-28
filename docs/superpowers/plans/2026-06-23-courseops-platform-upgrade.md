# CourseOps Platform Upgrade Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Upgrade UIBE CourseOps from a course-project demo dashboard into a polished, role-aware, platform-grade course project and material workflow product for students, teachers, and administrators.

**Architecture:** Refactor the current single-file Vue app into a modular Vue 3 platform with explicit domain models, Vue Router routes, a Pinia singleton store, centralized mock/API services, reusable product components, and Apple-inspired premium SaaS visual language. The first implementation remains local-first and mock-backed, but every data access path is routed through a service layer so real backend APIs can replace mock data without rewriting pages.

**Tech Stack:** Vue 3, TypeScript, Vite, Vue Router, Pinia, ECharts, Lucide Icons, Arco Design Vue where useful, CSS design tokens, localStorage persistence, browser-based verification.

---

## 0. Product Decision

The old plan is superseded. The new product direction is:

```text
UIBE CourseOps =
Apple-inspired premium SaaS landing experience
+ role-aware course project workspace
+ material submission workflow
+ teacher feedback loop
+ operational insights
+ audit timeline
```

This is no longer only a course assignment presentation UI. It should feel like a product the university could put in front of real students and teachers.

## 0.1 Final Correction Layer

These corrections are mandatory. If any earlier task in this document conflicts with this section, follow this section.

The product must satisfy three outcomes at the same time:

- Real workflow: users can complete `查看课程项目 -> 查看材料要求 -> 上传材料 -> 状态变化 -> 教师反馈 -> 修改提交 -> 日志记录`.
- Premium decoration: pages must include product-grade Hero sections, pseudo-3D visuals, floating panels, micro-interactions, refined icons, progressive reveal, and clear visual focus modules.
- Unified beauty: all pages share one spacing system, typography hierarchy, color system, card language, motion language, and interaction style.

Failure condition:

- If the result is only `left sidebar + big title + cards + charts`, the implementation fails even if it builds.

Mandatory technical corrections:

- Use real Vue Router. Route-like page state is not enough for the final product.
- Use a true singleton store. Prefer Pinia. Do not create a new store state per component call.
- Global search must be functional through a glass Command Palette / Search Overlay.
- Upload must validate file presence, format, and max file size of 50MB before changing state.
- User actions must append operation logs, material history, and relevant notifications.
- Material Workflow is the strongest demo page and must have real state transitions.
- App inner page titles must be useful and controlled. Landing can use 72px-96px titles; app pages should stay around 40px-56px.

Mandatory user chains:

- Student chain: Landing -> Workspace -> Today Todo -> Material Workflow -> Upload File -> Submitted column -> Project Space submission record -> Activity Timeline log -> Notification update.
- Teacher chain: Switch teacher role -> Workspace pending reviews -> Material Detail -> Mark revision required or approved -> Log appended -> Student sees status and feedback.
- Admin chain: Switch admin role -> Operational Insights -> Audit Logs -> Expand log -> See audit ID -> Search/filter by project, material, or actor.

## 1. Current Code Audit

Current frontend files:

- `courseops-web/src/App.vue`: contains all data, page state, interactions, ECharts setup, login, dashboard, projects, kanban, analytics, and logs.
- `courseops-web/src/style.css`: contains all visual system and page styling.
- `courseops-web/src/main.ts`: mounts Vue app and registers Arco Vue.
- `courseops-web/package.json`: has `dev`, `build`, `preview`; no `lint` or standalone `typecheck` script.

Current strengths to keep:

- Vue 3 + TypeScript + Vite already works.
- ECharts is installed and build passes.
- Local CRUD-like interactions exist: create project, create task, update material, change task status, append logs.
- Visual base already moved toward Apple-inspired light UI.

Current limitations to fix:

- One huge `App.vue` makes the product hard to maintain.
- Data is hardcoded inside UI code.
- Current Kanban is task-based, not material-submission-flow-based.
- Current dashboard is still dashboard-demo-shaped.
- No public landing page.
- No explicit roles: student, teacher, admin.
- No API abstraction.
- No clear project-space model.
- Operation logs expose technical IDs too prominently.

## 2. Target Route Map

Implement route-like structure first. If Vue Router is wired during implementation, these map directly to routes. If not, keep them as typed page keys so the UI behaves the same.

```text
/
  Public Landing Page

/app
  My Workspace

/app/projects
  Course Project Center

/app/projects/:id
  Project Space

/app/tasks
  Material Workflow Center

/app/analytics
  Operational Insights

/app/logs
  Activity Timeline / Audit Logs
```

## 3. Target File Structure

Create focused files. Do not keep expanding `App.vue`.

```text
courseops-web/src/
  App.vue
  main.ts
  style.css
  domain/
    types.ts
    constants.ts
  router/
    index.ts
  data/
    mockCourseOps.ts
  services/
    courseopsApi.ts
  stores/
    useCourseOpsStore.ts
  components/
    shell/
      AppShell.vue
      TopNav.vue
      SideNav.vue
      RoleSwitcher.vue
      NotificationPopover.vue
      SearchCommandPalette.vue
    common/
      GlassCard.vue
      PageHero.vue
      ProductShowcaseHero.vue
      StatusPill.vue
      RiskBadge.vue
      FileFormatBadge.vue
      ProgressBar.vue
      ProgressRing.vue
      SegmentedControl.vue
      EmptyState.vue
      SkeletonCard.vue
      ErrorState.vue
      AnimatedNumber.vue
      VisualGlowBackground.vue
    landing/
      PublicLanding.vue
      FloatingVisual.vue
      ThreeDDocumentStack.vue
      FloatingCourseCards.vue
      WorkflowPreviewBoard.vue
      DeadlineCalendarCube.vue
    dashboard/
      WorkspaceHome.vue
      TodoCard.vue
      SmartSuggestionCard.vue
      WeeklyOverview.vue
    projects/
      ProjectCenter.vue
      CourseProjectCard.vue
      ProjectSpace.vue
      MaterialChecklist.vue
      SubmissionRecordList.vue
      FeedbackList.vue
      TeamAvatarStack.vue
    tasks/
      MaterialWorkflowCenter.vue
      MaterialTaskCard.vue
      MaterialDetailDrawer.vue
      MaterialTimeline.vue
    analytics/
      OperationalInsights.vue
      InsightChartCard.vue
    logs/
      ActivityTimeline.vue
      TimelineItem.vue
      LogFilterBar.vue
  styles/
    tokens.css
    base.css
    motion.css
```

Responsibility boundaries:

- `domain/types.ts`: all product entities and status unions.
- `data/mockCourseOps.ts`: mock users, courses, projects, materials, submissions, logs, analytics.
- `services/courseopsApi.ts`: async service functions. UI never imports mock data directly.
- `stores/useCourseOpsStore.ts`: Pinia singleton store and single source of reactive state and mutations.
- `router/index.ts`: real Vue Router route definitions for `/`, `/app`, `/app/projects`, `/app/projects/:id`, `/app/tasks`, `/app/analytics`, and `/app/logs`.
- `components/shell/SearchCommandPalette.vue`: functional global search overlay with grouped results and keyboard Esc close.
- `components/shell/NotificationPopover.vue`: notification center with unread state, mark-read action, and navigation targets.
- `components/common/*`: reusable product UI primitives.
- `components/landing/*`: public marketing/product homepage only.
- `components/dashboard/*`: role-aware workspace page.
- `components/projects/*`: project center and project space.
- `components/tasks/*`: material workflow Kanban and detail drawer.
- `components/analytics/*`: operational insight cards and charts.
- `components/logs/*`: timeline and searchable audit/activity log.

## 4. Domain Model

Use these exact TypeScript types as the platform baseline.

```ts
export type UserRole = 'student' | 'teacher' | 'admin'
export type RiskLevel = 'low' | 'medium' | 'high'
export type MaterialStatus =
  | 'not_started'
  | 'preparing'
  | 'submitted'
  | 'revision_required'
  | 'approved'

export interface User {
  id: string
  name: string
  role: UserRole
  department: string
  avatar: string
}

export interface Course {
  id: string
  name: string
  teacher: string
  semester: string
  studentsCount: number
}

export interface CourseProject {
  id: string
  courseId: string
  title: string
  description: string
  owner: string
  members: string[]
  deadline: string
  phase: string
  progress: number
  teamProgress: number
  riskLevel: RiskLevel
  healthScore: number
  recentFeedback: string
}

export interface TeamMember {
  id: string
  projectId: string
  name: string
  role: 'leader' | 'member'
  avatar: string
  responsibility: string
  progress: number
  hasOverdueRisk: boolean
}

export interface MaterialItem {
  id: string
  projectId: string
  title: string
  description: string
  required: boolean
  format: 'PDF' | 'DOCX' | 'ZIP' | 'MP4' | 'PPTX' | 'PNG'
  deadline: string
  status: MaterialStatus
  progress: number
  assignee: string
  feedbackCount: number
  latestFile: string
}

export interface Submission {
  id: string
  materialId: string
  fileName: string
  fileUrl: string
  submittedBy: string
  submittedAt: string
  status: MaterialStatus
  feedback: string
}

export interface TeacherFeedback {
  id: string
  materialId: string
  projectId: string
  teacherName: string
  content: string
  status: 'unresolved' | 'resolved'
  createdAt: string
}

export interface MaterialHistory {
  id: string
  materialId: string
  actor: string
  action: string
  fromStatus?: MaterialStatus
  toStatus?: MaterialStatus
  fileName?: string
  createdAt: string
}

export interface OperationLog {
  id: string
  actor: string
  actorRole: UserRole
  action: string
  targetType: 'project' | 'material' | 'submission' | 'feedback' | 'system'
  targetName: string
  before: string
  after: string
  createdAt: string
  source: string
}

export interface NotificationItem {
  id: string
  userId: string
  title: string
  description: string
  type: 'deadline' | 'feedback' | 'submission' | 'risk' | 'system'
  relatedTargetId?: string
  read: boolean
  createdAt: string
}

export interface UploadResult {
  materialId: string
  fileName: string
  fileSize: number
  fileType: string
  submittedAt: string
  submittedBy: string
  status: MaterialStatus
  message: string
}

export interface AnalyticsSnapshot {
  materialCompletionRate: number
  dueSoonCount: number
  revisionRequiredCount: number
  pendingFeedbackCount: number
  submissionTrend: Array<{ day: string; count: number }>
  courseCompletion: Array<{ course: string; value: number }>
  riskDistribution: Array<{ level: RiskLevel; count: number }>
  materialGap: Array<{ material: string; count: number }>
  healthRadar: number[]
}
```

## 5. Design System Direction

Use Apple-inspired premium SaaS styling, not Apple copy.

Core CSS tokens:

```css
:root {
  --color-bg-page: #f5f5f7;
  --color-bg-soft: #fbfbfd;
  --color-bg-surface: rgba(255, 255, 255, 0.74);
  --color-bg-elevated: rgba(255, 255, 255, 0.88);
  --color-text-primary: #1d1d1f;
  --color-text-secondary: #6e6e73;
  --color-text-tertiary: #86868b;
  --color-accent: #007a6c;
  --color-accent-soft: rgba(0, 122, 108, 0.1);
  --color-warning: #b87500;
  --color-danger: #d92d20;
  --color-success: #12805c;
  --radius-sm: 10px;
  --radius-md: 16px;
  --radius-lg: 24px;
  --radius-xl: 32px;
  --radius-2xl: 40px;
  --shadow-soft: 0 20px 60px rgba(0, 0, 0, 0.06);
  --shadow-hover: 0 28px 80px rgba(0, 0, 0, 0.1);
  --shadow-inner: inset 0 1px 0 rgba(255, 255, 255, 0.75);
  --blur-glass: blur(24px);
}
```

Visual rules:

- Landing can use 64px-88px display titles.
- App pages use 40px-56px page titles only when meaningful.
- No generic admin cards.
- Every metric must connect to an action or insight.
- Use pseudo-3D with CSS transforms. Do not add Three.js unless explicitly approved later.
- Use light glass panels selectively; do not make every element glass.
- Use calm green, amber, red, and blue only as semantic accents.

## 6. Implementation Tasks

### Task 1: Add Domain Types and Constants

**Files:**
- Create: `courseops-web/src/domain/types.ts`
- Create: `courseops-web/src/domain/constants.ts`
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Create domain types**

Create `courseops-web/src/domain/types.ts` with the full TypeScript model from section 4.

- [ ] **Step 2: Create status labels and route constants**

Create `courseops-web/src/domain/constants.ts`:

```ts
import type { MaterialStatus, RiskLevel, UserRole } from './types'

export const roleLabels: Record<UserRole, string> = {
  student: '学生视角',
  teacher: '教师视角',
  admin: '管理员视角',
}

export const materialStatusLabels: Record<MaterialStatus, string> = {
  not_started: '待准备',
  preparing: '待提交',
  submitted: '已提交',
  revision_required: '需修改',
  approved: '已通过',
}

export const materialStatusOrder: MaterialStatus[] = [
  'not_started',
  'preparing',
  'submitted',
  'revision_required',
  'approved',
]

export const riskLabels: Record<RiskLevel, string> = {
  low: '正常',
  medium: '临近截止',
  high: '高风险',
}

export const appPages = [
  'landing',
  'workspace',
  'projects',
  'project-space',
  'tasks',
  'analytics',
  'logs',
] as const

export type AppPage = typeof appPages[number]
```

- [ ] **Step 3: Verify TypeScript compiles**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 1A: Install and Wire Vue Router and Pinia

**Files:**
- Modify: `courseops-web/package.json`
- Modify: `courseops-web/src/main.ts`
- Create: `courseops-web/src/router/index.ts`
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Confirm dependencies**

Run:

```bash
cd courseops-web
npm.cmd install vue-router pinia
```

Expected: `vue-router` and `pinia` are present in `dependencies`.

- [ ] **Step 2: Create real routes**

Create `courseops-web/src/router/index.ts`:

```ts
import { createRouter, createWebHistory } from 'vue-router'
import PublicLanding from '../components/landing/PublicLanding.vue'
import WorkspaceHome from '../components/dashboard/WorkspaceHome.vue'
import ProjectCenter from '../components/projects/ProjectCenter.vue'
import ProjectSpace from '../components/projects/ProjectSpace.vue'
import MaterialWorkflowCenter from '../components/tasks/MaterialWorkflowCenter.vue'
import OperationalInsights from '../components/analytics/OperationalInsights.vue'
import ActivityTimeline from '../components/logs/ActivityTimeline.vue'

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'landing', component: PublicLanding },
    { path: '/app', name: 'workspace', component: WorkspaceHome },
    { path: '/app/projects', name: 'projects', component: ProjectCenter },
    { path: '/app/projects/:id', name: 'project-space', component: ProjectSpace, props: true },
    { path: '/app/tasks', name: 'tasks', component: MaterialWorkflowCenter },
    { path: '/app/analytics', name: 'analytics', component: OperationalInsights },
    { path: '/app/logs', name: 'logs', component: ActivityTimeline },
  ],
})
```

- [ ] **Step 3: Register router and Pinia**

Modify `courseops-web/src/main.ts`:

```ts
import { createPinia } from 'pinia'
import { createApp } from 'vue'
import ArcoVue from '@arco-design/web-vue'
import '@arco-design/web-vue/dist/arco.css'
import './styles/tokens.css'
import './styles/base.css'
import './styles/motion.css'
import './style.css'
import App from './App.vue'
import { router } from './router'

createApp(App)
  .use(createPinia())
  .use(router)
  .use(ArcoVue)
  .mount('#app')
```

- [ ] **Step 4: Make App.vue a router host**

After all route components exist, `App.vue` must be reduced to:

```vue
<template>
  <RouterView />
</template>
```

If shell wrapping is needed for `/app/*` routes, use a layout component or route-level wrapper. Do not put route branching logic back into `App.vue`.

- [ ] **Step 5: Verify URL behavior**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds. Manual browser checks must confirm:

- `/` opens Landing.
- `/app` opens Workspace after refresh.
- `/app/projects` opens Project Center after refresh.
- `/app/projects/p-web-final` opens Project Space after refresh.
- `/app/tasks?materialId=m-report` opens Material Workflow with that material selected.

### Task 2: Centralize Mock Data

**Files:**
- Create: `courseops-web/src/data/mockCourseOps.ts`
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Move all demo data out of App.vue**

Create `courseops-web/src/data/mockCourseOps.ts`:

```ts
import type {
  AnalyticsSnapshot,
  Course,
  CourseProject,
  MaterialItem,
  OperationLog,
  Submission,
  User,
} from '../domain/types'

export const mockUsers: User[] = [
  { id: 'u-student-001', name: '张同学', role: 'student', department: '信息学院', avatar: 'Z' },
  { id: 'u-teacher-001', name: '张老师', role: 'teacher', department: '信息学院', avatar: 'T' },
  { id: 'u-admin-001', name: '管理员', role: 'admin', department: '教务处', avatar: 'A' },
]

export const mockCourses: Course[] = [
  { id: 'c-web', name: 'Web 应用开发', teacher: '张老师', semester: '2026 春季学期', studentsCount: 86 },
  { id: 'c-db', name: '数据库原理', teacher: '李老师', semester: '2026 春季学期', studentsCount: 72 },
  { id: 'c-writing', name: '学术写作', teacher: '王老师', semester: '2026 春季学期', studentsCount: 54 },
]

export const mockProjects: CourseProject[] = [
  {
    id: 'p-web-final',
    courseId: 'c-web',
    title: 'Web 期末项目',
    description: '完成一个具备项目管理、材料流转和操作审计能力的课程项目平台。',
    owner: '张同学',
    members: ['张同学', '李同学', '王同学'],
    deadline: '2026-06-25',
    phase: '材料完善',
    progress: 72,
    teamProgress: 68,
    riskLevel: 'medium',
    healthScore: 76,
    recentFeedback: '说明书正文需要补充数据库设计说明。',
  },
  {
    id: 'p-db-er',
    courseId: 'c-db',
    title: '数据库实验报告',
    description: '提交 ER 图、表结构说明和实验报告 PDF。',
    owner: '张同学',
    members: ['张同学'],
    deadline: '2026-06-24',
    phase: '待补交',
    progress: 46,
    teamProgress: 46,
    riskLevel: 'high',
    healthScore: 58,
    recentFeedback: 'ER 图缺少关系说明，请今天补齐。',
  },
]

export const mockMaterials: MaterialItem[] = [
  {
    id: 'm-report',
    projectId: 'p-web-final',
    title: '说明书正文',
    description: '包含系统简介、功能说明、截图、数据库设计和技术架构。',
    required: true,
    format: 'DOCX',
    deadline: '2026-06-25',
    status: 'revision_required',
    progress: 70,
    assignee: '张同学',
    feedbackCount: 2,
    latestFile: 'courseops-manual-v2.docx',
  },
  {
    id: 'm-screenshots',
    projectId: 'p-web-final',
    title: '系统截图',
    description: '登录页、工作台、项目中心、材料看板、数据洞察截图。',
    required: true,
    format: 'PNG',
    deadline: '2026-06-25',
    status: 'submitted',
    progress: 90,
    assignee: '李同学',
    feedbackCount: 0,
    latestFile: 'courseops-screenshots.zip',
  },
  {
    id: 'm-source',
    projectId: 'p-web-final',
    title: '源代码压缩包',
    description: '包含前端、后端、数据库脚本和运行说明。',
    required: true,
    format: 'ZIP',
    deadline: '2026-06-26',
    status: 'preparing',
    progress: 52,
    assignee: '张同学',
    feedbackCount: 1,
    latestFile: '',
  },
  {
    id: 'm-er',
    projectId: 'p-db-er',
    title: '数据库 ER 图',
    description: '展示实体、关系、主键、外键和约束。',
    required: true,
    format: 'PDF',
    deadline: '2026-06-24',
    status: 'not_started',
    progress: 20,
    assignee: '张同学',
    feedbackCount: 1,
    latestFile: '',
  },
]

export const mockSubmissions: Submission[] = [
  {
    id: 's-report-v2',
    materialId: 'm-report',
    fileName: 'courseops-manual-v2.docx',
    fileUrl: '#',
    submittedBy: '张同学',
    submittedAt: '2026-06-23 10:12',
    status: 'revision_required',
    feedback: '请补充项目空间与材料流转说明。',
  },
]

export const mockLogs: OperationLog[] = [
  {
    id: 'mongo-8301',
    actor: '张同学',
    actorRole: 'student',
    action: '更新材料状态',
    targetType: 'material',
    targetName: '说明书正文',
    before: '待提交',
    after: '需修改',
    createdAt: '2026-06-23 10:16',
    source: '材料流转中心',
  },
  {
    id: 'mongo-8297',
    actor: '张老师',
    actorRole: 'teacher',
    action: '提交反馈',
    targetType: 'feedback',
    targetName: '数据库 ER 图',
    before: '未反馈',
    after: '需要补充关系说明',
    createdAt: '2026-06-23 10:08',
    source: '项目空间',
  },
]

export const mockAnalytics: AnalyticsSnapshot = {
  materialCompletionRate: 68,
  dueSoonCount: 3,
  revisionRequiredCount: 2,
  pendingFeedbackCount: 4,
  submissionTrend: [
    { day: '周一', count: 8 },
    { day: '周二', count: 12 },
    { day: '周三', count: 10 },
    { day: '周四', count: 16 },
    { day: '周五', count: 22 },
    { day: '周六', count: 31 },
    { day: '周日', count: 28 },
  ],
  courseCompletion: [
    { course: 'Web 应用开发', value: 72 },
    { course: '数据库原理', value: 46 },
    { course: '学术写作', value: 83 },
  ],
  riskDistribution: [
    { level: 'low', count: 6 },
    { level: 'medium', count: 4 },
    { level: 'high', count: 2 },
  ],
  materialGap: [
    { material: '数据库设计图', count: 8 },
    { material: '说明书正文', count: 5 },
    { material: '源码压缩包', count: 3 },
  ],
  healthRadar: [72, 68, 58, 82, 76],
}
```

- [ ] **Step 2: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 3: Add API Service Layer

**Files:**
- Create: `courseops-web/src/services/courseopsApi.ts`

- [ ] **Step 1: Create async API wrappers**

Create `courseops-web/src/services/courseopsApi.ts`:

```ts
import {
  mockAnalytics,
  mockCourses,
  mockLogs,
  mockMaterials,
  mockProjects,
  mockSubmissions,
  mockUsers,
} from '../data/mockCourseOps'
import type { MaterialStatus, OperationLog } from '../domain/types'

const wait = (ms = 180) => new Promise((resolve) => window.setTimeout(resolve, ms))

export async function getUsers() {
  await wait()
  return structuredClone(mockUsers)
}

export async function getCourses() {
  await wait()
  return structuredClone(mockCourses)
}

export async function getProjects() {
  await wait()
  return structuredClone(mockProjects)
}

export async function getProjectDetail(id: string) {
  await wait()
  return structuredClone(mockProjects.find((project) => project.id === id) ?? mockProjects[0])
}

export async function getMaterialTasks() {
  await wait()
  return structuredClone(mockMaterials)
}

export async function getSubmissions() {
  await wait()
  return structuredClone(mockSubmissions)
}

export async function updateTaskStatus(id: string, status: MaterialStatus) {
  await wait()
  return { id, status }
}

export async function uploadMaterial(id: string, file: File) {
  // TODO: replace local mock upload with real backend multipart upload API.
  await wait(240)
  return {
    materialId: id,
    fileName: file.name,
    fileSize: file.size,
    fileType: file.type || file.name.split('.').pop() || 'unknown',
    submittedAt: new Date().toLocaleString('zh-CN', { hour12: false }),
    submittedBy: '当前用户',
    status: 'submitted' as MaterialStatus,
    message: '文件已进入本地模拟提交队列，接入后端后将替换为真实上传。',
  }
}

export async function getAnalytics() {
  await wait()
  return structuredClone(mockAnalytics)
}

export async function getOperationLogs() {
  await wait()
  return structuredClone(mockLogs)
}

export async function appendOperationLog(log: OperationLog) {
  await wait(120)
  return structuredClone(log)
}
```

- [ ] **Step 2: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 4: Create Pinia Singleton Store

**Files:**
- Create: `courseops-web/src/stores/useCourseOpsStore.ts`
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Create state store**

Create `courseops-web/src/stores/useCourseOpsStore.ts`:

```ts
import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import { materialStatusOrder } from '../domain/constants'
import type {
  AnalyticsSnapshot,
  Course,
  CourseProject,
  MaterialItem,
  MaterialHistory,
  MaterialStatus,
  NotificationItem,
  OperationLog,
  Submission,
  TeacherFeedback,
  TeamMember,
  User,
  UserRole,
} from '../domain/types'
import {
  appendOperationLog,
  getAnalytics,
  getCourses,
  getMaterialTasks,
  getOperationLogs,
  getProjects,
  getSubmissions,
  getUsers,
  updateTaskStatus,
  uploadMaterial,
} from '../services/courseopsApi'

const STORAGE_KEY = 'uibe-courseops-platform-state-v1'

export const useCourseOpsStore = defineStore('courseops', () => {
  const users = ref<User[]>([])
  const courses = ref<Course[]>([])
  const projects = ref<CourseProject[]>([])
  const materials = ref<MaterialItem[]>([])
  const submissions = ref<Submission[]>([])
  const members = ref<TeamMember[]>([])
  const feedback = ref<TeacherFeedback[]>([])
  const histories = ref<MaterialHistory[]>([])
  const notifications = ref<NotificationItem[]>([])
  const logs = ref<OperationLog[]>([])
  const analytics = ref<AnalyticsSnapshot | null>(null)
  const currentUserId = ref('u-student-001')
  const selectedProjectId = ref('p-web-final')
  const selectedMaterialId = ref('m-report')
  const isLoading = ref(false)
  const errorMessage = ref('')

  const currentUser = computed(() => users.value.find((user) => user.id === currentUserId.value) ?? users.value[0])
  const currentRole = computed<UserRole>(() => currentUser.value?.role ?? 'student')
  const selectedProject = computed(() => projects.value.find((project) => project.id === selectedProjectId.value) ?? projects.value[0])
  const selectedMaterial = computed(() => materials.value.find((material) => material.id === selectedMaterialId.value) ?? materials.value[0])
  const selectedProjectMaterials = computed(() => materials.value.filter((material) => material.projectId === selectedProjectId.value))
  const selectedProjectMembers = computed(() => members.value.filter((member) => member.projectId === selectedProjectId.value))
  const selectedProjectFeedback = computed(() => feedback.value.filter((item) => item.projectId === selectedProjectId.value))
  const selectedMaterialHistory = computed(() => histories.value.filter((history) => history.materialId === selectedMaterialId.value))
  const unreadNotifications = computed(() => notifications.value.filter((item) => item.userId === currentUserId.value && !item.read))
  const dueSoonMaterials = computed(() => materials.value.filter((material) => ['not_started', 'preparing', 'revision_required'].includes(material.status)).slice(0, 4))
  const materialColumns = computed(() => materialStatusOrder.map((status) => ({
    status,
    materials: materials.value.filter((material) => material.status === status),
  })))

  async function loadInitialData() {
    isLoading.value = true
    errorMessage.value = ''
    try {
      const [userData, courseData, projectData, materialData, submissionData, logData, analyticsData] = await Promise.all([
        getUsers(),
        getCourses(),
        getProjects(),
        getMaterialTasks(),
        getSubmissions(),
        getOperationLogs(),
        getAnalytics(),
      ])
      users.value = userData
      courses.value = courseData
      projects.value = projectData
      materials.value = materialData
      submissions.value = submissionData
      logs.value = logData
      analytics.value = analyticsData
      restoreLocalState()
    } catch {
      errorMessage.value = '数据加载失败，请刷新页面重试。'
    } finally {
      isLoading.value = false
    }
  }

  function persistLocalState() {
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify({
      currentUserId: currentUserId.value,
      selectedProjectId: selectedProjectId.value,
      selectedMaterialId: selectedMaterialId.value,
      projects: projects.value,
      materials: materials.value,
      submissions: submissions.value,
      logs: logs.value,
    }))
  }

  function restoreLocalState() {
    const saved = window.localStorage.getItem(STORAGE_KEY)
    if (!saved) return
    const parsed = JSON.parse(saved)
    if (parsed.currentUserId) currentUserId.value = parsed.currentUserId
    if (parsed.selectedProjectId) selectedProjectId.value = parsed.selectedProjectId
    if (parsed.selectedMaterialId) selectedMaterialId.value = parsed.selectedMaterialId
    if (Array.isArray(parsed.projects)) projects.value = parsed.projects
    if (Array.isArray(parsed.materials)) materials.value = parsed.materials
    if (Array.isArray(parsed.submissions)) submissions.value = parsed.submissions
    if (Array.isArray(parsed.logs)) logs.value = parsed.logs
  }

  async function switchRole(role: UserRole) {
    const user = users.value.find((item) => item.role === role)
    if (user) currentUserId.value = user.id
    persistLocalState()
  }

  function createLog(payload: Omit<OperationLog, 'id' | 'actor' | 'actorRole' | 'createdAt'>) {
    const log: OperationLog = {
      id: `mongo-${Date.now()}`,
      actor: currentUser.value.name,
      actorRole: currentRole.value,
      createdAt: new Date().toLocaleString('zh-CN', { hour12: false }),
      ...payload,
    }
    logs.value.unshift(log)
    return log
  }

  function createHistory(payload: Omit<MaterialHistory, 'id' | 'actor' | 'createdAt'>) {
    histories.value.unshift({
      id: `mh-${Date.now()}`,
      actor: currentUser.value.name,
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
    const material = materials.value.find((item) => item.id === materialId)
    if (!material) return
    const before = material.status
    const result = await updateTaskStatus(materialId, status)
    material.status = result.status
    material.progress = status === 'approved' ? 100 : Math.max(material.progress, 72)
    createHistory({
      materialId,
      action: '修改材料状态',
      fromStatus: before,
      toStatus: status,
    })
    const log = createLog({
      action: '更新材料状态',
      targetType: 'material',
      targetName: material.title,
      before,
      after: status,
      source: '材料流转中心',
    })
    await appendOperationLog(log)
    createNotification({
      userId: currentUserId.value,
      title: `${material.title} 状态已更新`,
      description: `状态从 ${before} 变为 ${status}`,
      type: status === 'approved' ? 'submission' : 'feedback',
      relatedTargetId: materialId,
    })
    persistLocalState()
  }

  async function submitMaterialFile(materialId: string, file: File) {
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
    const result = await uploadMaterial(materialId, file)
    material.status = result.status
    material.latestFile = result.fileName
    material.progress = Math.max(material.progress, 88)
    submissions.value.unshift({
      id: `s-${Date.now()}`,
      materialId,
      fileName: result.fileName,
      fileUrl: '#',
      submittedBy: currentUser.value.name,
      submittedAt: new Date().toLocaleString('zh-CN', { hour12: false }),
      status: result.status,
      feedback: result.message,
    })
    createHistory({
      materialId,
      action: '上传文件',
      fromStatus: 'preparing',
      toStatus: result.status,
      fileName: result.fileName,
    })
    const log = createLog({
      action: '上传文件',
      targetType: 'submission',
      targetName: material.title,
      before: '本地文件未提交',
      after: result.fileName,
      source: '材料流转中心',
    })
    await appendOperationLog(log)
    createNotification({
      userId: currentUserId.value,
      title: `${material.title} 已上传`,
      description: `${result.fileName} 已进入提交记录。`,
      type: 'submission',
      relatedTargetId: materialId,
    })
    persistLocalState()
  }

  function markNotificationRead(id: string) {
    const notification = notifications.value.find((item) => item.id === id)
    if (!notification) return
    notification.read = true
    createLog({
      action: '标记通知已读',
      targetType: 'system',
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
    submissions,
    members,
    feedback,
    histories,
    notifications,
    logs,
    analytics,
    currentUserId,
    selectedProjectId,
    selectedMaterialId,
    isLoading,
    errorMessage,
    currentUser,
    currentRole,
    selectedProject,
    selectedMaterial,
    selectedProjectMaterials,
    selectedProjectMembers,
    selectedProjectFeedback,
    selectedMaterialHistory,
    unreadNotifications,
    dueSoonMaterials,
    materialColumns,
    loadInitialData,
    switchRole,
    changeMaterialStatus,
    submitMaterialFile,
    markNotificationRead,
    persistLocalState,
  }
})
```

- [ ] **Step 2: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 5: Split Design Tokens

**Files:**
- Create: `courseops-web/src/styles/tokens.css`
- Create: `courseops-web/src/styles/base.css`
- Create: `courseops-web/src/styles/motion.css`
- Modify: `courseops-web/src/style.css`
- Modify: `courseops-web/src/main.ts`

- [ ] **Step 1: Create tokens**

Create `courseops-web/src/styles/tokens.css` with the CSS token block from section 5.

- [ ] **Step 2: Create base layout styles**

Create `courseops-web/src/styles/base.css`:

```css
* {
  box-sizing: border-box;
}

html {
  background: var(--color-bg-page);
}

body {
  margin: 0;
  min-width: 320px;
  min-height: 100vh;
  color: var(--color-text-primary);
  background:
    radial-gradient(circle at 12% 8%, rgba(0, 122, 108, 0.14), transparent 30%),
    radial-gradient(circle at 88% 18%, rgba(52, 120, 246, 0.1), transparent 32%),
    var(--color-bg-page);
  font-family: -apple-system, BlinkMacSystemFont, "SF Pro Display", "Inter", "Segoe UI", "Microsoft YaHei UI", sans-serif;
  -webkit-font-smoothing: antialiased;
  text-rendering: optimizeLegibility;
}

button,
input,
select {
  font: inherit;
}

button {
  cursor: pointer;
}

#app {
  min-height: 100vh;
}
```

- [ ] **Step 3: Create motion styles**

Create `courseops-web/src/styles/motion.css`:

```css
.motion-rise {
  animation: motion-rise 420ms ease both;
}

.interactive-lift {
  transition: transform 220ms ease, box-shadow 220ms ease, border-color 220ms ease, background 220ms ease;
}

.interactive-lift:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-hover);
}

@keyframes motion-rise {
  from {
    opacity: 0;
    transform: translateY(14px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
```

- [ ] **Step 4: Import split styles**

Modify `courseops-web/src/main.ts` to import:

```ts
import './styles/tokens.css'
import './styles/base.css'
import './styles/motion.css'
import './style.css'
```

- [ ] **Step 5: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 6: Build Common Product Components

**Files:**
- Create: `courseops-web/src/components/common/GlassCard.vue`
- Create: `courseops-web/src/components/common/PageHero.vue`
- Create: `courseops-web/src/components/common/StatusPill.vue`
- Create: `courseops-web/src/components/common/RiskBadge.vue`
- Create: `courseops-web/src/components/common/ProgressBar.vue`
- Create: `courseops-web/src/components/common/ProgressRing.vue`
- Create: `courseops-web/src/components/common/SegmentedControl.vue`
- Create: `courseops-web/src/components/common/EmptyState.vue`
- Create: `courseops-web/src/components/common/SkeletonCard.vue`

- [ ] **Step 1: Create reusable card**

Create `GlassCard.vue`:

```vue
<template>
  <section class="glass-card interactive-lift">
    <slot />
  </section>
</template>
```

- [ ] **Step 2: Create page hero**

Create `PageHero.vue`:

```vue
<script setup lang="ts">
defineProps<{
  eyebrow?: string
  title: string
  description: string
}>()
</script>

<template>
  <header class="page-hero motion-rise">
    <span v-if="eyebrow" class="eyebrow">{{ eyebrow }}</span>
    <h1>{{ title }}</h1>
    <p>{{ description }}</p>
    <div class="page-hero-actions">
      <slot name="actions" />
    </div>
  </header>
</template>
```

- [ ] **Step 3: Create status and risk components**

Create `StatusPill.vue`:

```vue
<script setup lang="ts">
import { materialStatusLabels } from '../../domain/constants'
import type { MaterialStatus } from '../../domain/types'

defineProps<{ status: MaterialStatus }>()
</script>

<template>
  <span class="status-pill" :class="status">{{ materialStatusLabels[status] }}</span>
</template>
```

Create `RiskBadge.vue`:

```vue
<script setup lang="ts">
import { riskLabels } from '../../domain/constants'
import type { RiskLevel } from '../../domain/types'

defineProps<{ risk: RiskLevel }>()
</script>

<template>
  <span class="risk-badge" :class="risk">{{ riskLabels[risk] }}</span>
</template>
```

- [ ] **Step 4: Create progress components**

Create `ProgressBar.vue`:

```vue
<script setup lang="ts">
defineProps<{ value: number }>()
</script>

<template>
  <div class="progress-bar" aria-label="progress">
    <i :style="{ width: `${Math.max(0, Math.min(100, value))}%` }"></i>
  </div>
</template>
```

Create `ProgressRing.vue`:

```vue
<script setup lang="ts">
defineProps<{ value: number; label?: string }>()
</script>

<template>
  <div class="progress-ring" :style="{ '--value': value }">
    <strong>{{ value }}%</strong>
    <span>{{ label ?? '完成度' }}</span>
  </div>
</template>
```

- [ ] **Step 5: Create segmented control**

Create `SegmentedControl.vue`:

```vue
<script setup lang="ts">
defineProps<{
  options: Array<{ label: string; value: string }>
  modelValue: string
}>()

defineEmits<{ 'update:modelValue': [value: string] }>()
</script>

<template>
  <div class="segmented-control">
    <button
      v-for="option in options"
      :key="option.value"
      :class="{ active: option.value === modelValue }"
      @click="$emit('update:modelValue', option.value)"
    >
      {{ option.label }}
    </button>
  </div>
</template>
```

- [ ] **Step 6: Create empty and skeleton states**

Create `EmptyState.vue`:

```vue
<script setup lang="ts">
defineProps<{ title: string; description: string; action?: string }>()
defineEmits<{ action: [] }>()
</script>

<template>
  <section class="empty-state">
    <div class="empty-orb"></div>
    <h3>{{ title }}</h3>
    <p>{{ description }}</p>
    <button v-if="action" @click="$emit('action')">{{ action }}</button>
  </section>
</template>
```

Create `SkeletonCard.vue`:

```vue
<template>
  <section class="skeleton-card">
    <i></i>
    <b></b>
    <span></span>
  </section>
</template>
```

- [ ] **Step 7: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 7: Build Public Landing Page

**Files:**
- Create: `courseops-web/src/components/landing/PublicLanding.vue`
- Create: `courseops-web/src/components/landing/FloatingVisual.vue`
- Create: `courseops-web/src/components/landing/ThreeDDocumentStack.vue`
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Create pseudo-3D visual components**

Create `ThreeDDocumentStack.vue`:

```vue
<template>
  <div class="document-stack" aria-label="course material stack">
    <div class="doc-card doc-card-a">说明书正文 <strong>70%</strong></div>
    <div class="doc-card doc-card-b">数据库 ER 图 <strong>待提交</strong></div>
    <div class="doc-card doc-card-c">源码压缩包 <strong>准备中</strong></div>
  </div>
</template>
```

Create `FloatingVisual.vue`:

```vue
<script setup lang="ts">
import ThreeDDocumentStack from './ThreeDDocumentStack.vue'
</script>

<template>
  <aside class="floating-visual">
    <div class="progress-orb"><strong>68%</strong><span>材料完成率</span></div>
    <ThreeDDocumentStack />
    <div class="floating-chip chip-a">3 个临期项目</div>
    <div class="floating-chip chip-b">4 条教师反馈</div>
  </aside>
</template>
```

- [ ] **Step 2: Create public landing page**

Create `PublicLanding.vue`:

```vue
<script setup lang="ts">
import FloatingVisual from './FloatingVisual.vue'

defineEmits<{
  enterApp: []
  viewProjects: []
}>()
</script>

<template>
  <main class="public-landing">
    <nav class="landing-nav">
      <strong>UIBE CourseOps</strong>
      <button @click="$emit('enterApp')">进入我的工作台</button>
    </nav>

    <section class="landing-hero">
      <div class="landing-copy motion-rise">
        <span class="eyebrow">Course Project Operating System</span>
        <h1>面向全校课程项目的智能运营平台。</h1>
        <p>把课程项目、材料要求、截止风险、教师反馈和操作记录组织成一条真实可用的协作流。</p>
        <div class="landing-actions">
          <button class="primary" @click="$emit('enterApp')">进入我的工作台</button>
          <button class="secondary" @click="$emit('viewProjects')">查看课程项目</button>
        </div>
      </div>
      <FloatingVisual />
    </section>

    <section class="capability-grid">
      <article><h3>课程项目统一管理</h3><p>集中查看课程、项目、团队、阶段和截止时间。</p></article>
      <article><h3>期末材料智能清单</h3><p>按材料格式、状态、反馈和提交记录组织任务。</p></article>
      <article><h3>截止风险提醒</h3><p>把临期、缺口、需修改事项转化为明确行动。</p></article>
      <article><h3>教师 / 学生协作流</h3><p>学生提交，教师检查，系统记录全过程。</p></article>
    </section>

    <section class="flow-section">
      <h2>从项目发布到材料通过，一条流程闭环。</h2>
      <div class="flow-steps">
        <span>创建课程项目</span>
        <span>发布材料要求</span>
        <span>学生提交材料</span>
        <span>助教检查状态</span>
        <span>生成风险提醒</span>
      </div>
    </section>

    <section class="landing-cta">
      <h2>开始管理我的课程项目</h2>
      <p>进入工作台后，你会直接看到今天最重要的材料、反馈和截止风险。</p>
      <button class="primary" @click="$emit('enterApp')">开始使用</button>
    </section>
  </main>
</template>
```

- [ ] **Step 3: Wire landing into App**

Modify `App.vue` so the initial page is `landing`, and clicking landing buttons changes the page to `workspace` or `projects`.

- [ ] **Step 4: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 8: Build Product App Shell

**Files:**
- Create: `courseops-web/src/components/shell/AppShell.vue`
- Create: `courseops-web/src/components/shell/TopNav.vue`
- Create: `courseops-web/src/components/shell/SideNav.vue`
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Create side navigation**

Create `SideNav.vue`:

```vue
<script setup lang="ts">
import { BarChart3, ClipboardCheck, Database, FileText, LayoutDashboard } from 'lucide-vue-next'
import type { AppPage } from '../../domain/constants'

defineProps<{ currentPage: AppPage }>()
defineEmits<{ navigate: [page: AppPage] }>()

const items: Array<{ page: AppPage; label: string; icon: unknown }> = [
  { page: 'workspace', label: '我的工作台', icon: LayoutDashboard },
  { page: 'projects', label: '课程项目', icon: FileText },
  { page: 'tasks', label: '材料流转', icon: ClipboardCheck },
  { page: 'analytics', label: '运营洞察', icon: BarChart3 },
  { page: 'logs', label: '动态记录', icon: Database },
]
</script>

<template>
  <aside class="side-nav">
    <button class="brand" @click="$emit('navigate', 'landing')">UIBE CourseOps</button>
    <nav>
      <button
        v-for="item in items"
        :key="item.page"
        :class="{ active: currentPage === item.page }"
        @click="$emit('navigate', item.page)"
      >
        <component :is="item.icon" :size="18" />
        {{ item.label }}
      </button>
    </nav>
  </aside>
</template>
```

- [ ] **Step 2: Create top navigation**

Create `TopNav.vue`:

```vue
<script setup lang="ts">
import { Bell, Search, UserRound } from 'lucide-vue-next'
import { roleLabels } from '../../domain/constants'
import type { User, UserRole } from '../../domain/types'

defineProps<{
  user: User
  searchText: string
}>()

defineEmits<{
  'update:searchText': [value: string]
  switchRole: [role: UserRole]
}>()
</script>

<template>
  <header class="top-nav">
    <label class="global-search">
      <Search :size="17" />
      <input
        :value="searchText"
        placeholder="搜索课程、项目、材料、负责人..."
        @input="$emit('update:searchText', ($event.target as HTMLInputElement).value)"
      />
    </label>
    <div class="top-nav-actions">
      <span>2026 春季学期</span>
      <button><Bell :size="16" /> 通知</button>
      <select :value="user.role" @change="$emit('switchRole', ($event.target as HTMLSelectElement).value as UserRole)">
        <option v-for="(label, role) in roleLabels" :key="role" :value="role">{{ label }}</option>
      </select>
      <span class="user-chip"><UserRound :size="16" /> {{ user.name }}</span>
    </div>
  </header>
</template>
```

- [ ] **Step 3: Create shell**

Create `AppShell.vue`:

```vue
<script setup lang="ts">
import type { AppPage } from '../../domain/constants'
import type { User, UserRole } from '../../domain/types'
import SideNav from './SideNav.vue'
import TopNav from './TopNav.vue'

defineProps<{
  currentPage: AppPage
  user: User
  searchText: string
}>()

defineEmits<{
  navigate: [page: AppPage]
  'update:searchText': [value: string]
  switchRole: [role: UserRole]
}>()
</script>

<template>
  <main class="app-shell">
    <SideNav :current-page="currentPage" @navigate="$emit('navigate', $event)" />
    <section class="app-workspace">
      <TopNav
        :user="user"
        :search-text="searchText"
        @update:search-text="$emit('update:searchText', $event)"
        @switch-role="$emit('switchRole', $event)"
      />
      <section class="app-content">
        <slot />
      </section>
    </section>
  </main>
</template>
```

- [ ] **Step 4: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 8A: Add Functional Search Command Palette

**Files:**
- Create: `courseops-web/src/components/shell/SearchCommandPalette.vue`
- Modify: `courseops-web/src/components/shell/TopNav.vue`
- Modify: `courseops-web/src/stores/useCourseOpsStore.ts`

- [ ] **Step 1: Add grouped search results to store**

Add a computed `searchResults` in `useCourseOpsStore.ts` that searches:

- course name
- project title and description
- material title and latest file
- teacher / owner / assignee
- team member name and responsibility
- operation log actor, target, and source

Result shape:

```ts
export interface SearchResult {
  id: string
  type: 'course' | 'project' | 'material' | 'submission' | 'log'
  title: string
  description: string
  targetRoute: string
}
```

- [ ] **Step 2: Create glass command palette**

Create `SearchCommandPalette.vue`:

```vue
<script setup lang="ts">
import { computed, onMounted, onUnmounted } from 'vue'

const props = defineProps<{
  open: boolean
  query: string
  results: Array<{ id: string; type: string; title: string; description: string; targetRoute: string }>
}>()

const emit = defineEmits<{
  close: []
  openResult: [route: string]
}>()

const grouped = computed(() => {
  const groups: Record<string, typeof props.results> = {}
  props.results.forEach((result) => {
    groups[result.type] ??= []
    groups[result.type].push(result)
  })
  return groups
})

function onKeydown(event: KeyboardEvent) {
  if (event.key === 'Escape') emit('close')
}

onMounted(() => window.addEventListener('keydown', onKeydown))
onUnmounted(() => window.removeEventListener('keydown', onKeydown))
</script>

<template>
  <Teleport to="body">
    <div v-if="open" class="search-overlay" @click.self="$emit('close')">
      <section class="search-command-palette">
        <header>
          <span>全局搜索</span>
          <strong>{{ query || '搜索课程、项目、材料、提交记录、日志' }}</strong>
        </header>
        <div v-if="results.length" class="search-groups">
          <section v-for="(items, type) in grouped" :key="type">
            <h3>{{ type }}</h3>
            <button v-for="item in items" :key="item.id" @click="$emit('openResult', item.targetRoute)">
              <strong>{{ item.title }}</strong>
              <span>{{ item.description }}</span>
            </button>
          </section>
        </div>
        <div v-else class="empty-state compact">
          <h3>没有找到结果</h3>
          <p>换一个课程、材料、负责人或文件名试试。</p>
        </div>
      </section>
    </div>
  </Teleport>
</template>
```

- [ ] **Step 3: Wire into TopNav**

TopNav input must open the palette while typing. Clicking a result must route through `router.push(targetRoute)`.

- [ ] **Step 4: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 8B: Add Notification Center

**Files:**
- Create: `courseops-web/src/components/shell/NotificationPopover.vue`
- Modify: `courseops-web/src/components/shell/TopNav.vue`
- Modify: `courseops-web/src/stores/useCourseOpsStore.ts`

- [ ] **Step 1: Create notification popover**

Create `NotificationPopover.vue`:

```vue
<script setup lang="ts">
import type { NotificationItem } from '../../domain/types'

defineProps<{ open: boolean; notifications: NotificationItem[] }>()
defineEmits<{
  close: []
  openNotification: [notification: NotificationItem]
  markRead: [id: string]
}>()
</script>

<template>
  <Teleport to="body">
    <div v-if="open" class="notification-layer" @click.self="$emit('close')">
      <section class="notification-popover">
        <header>
          <strong>通知中心</strong>
          <span>{{ notifications.filter((item) => !item.read).length }} 条未读</span>
        </header>
        <button v-for="item in notifications" :key="item.id" :class="{ unread: !item.read }" @click="$emit('openNotification', item)">
          <strong>{{ item.title }}</strong>
          <span>{{ item.description }}</span>
          <small>{{ item.createdAt }}</small>
          <em @click.stop="$emit('markRead', item.id)">标记已读</em>
        </button>
        <div v-if="!notifications.length" class="empty-state compact">
          <h3>暂无通知</h3>
          <p>材料提交、教师反馈和风险提醒会出现在这里。</p>
        </div>
      </section>
    </div>
  </Teleport>
</template>
```

- [ ] **Step 2: Generate notifications from actions**

Store actions must create notifications for:

- upload material
- teacher feedback
- status changed to revision required
- status changed to approved
- project risk marked
- notification marked read

- [ ] **Step 3: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 9: Rebuild Workspace Home

**Files:**
- Create: `courseops-web/src/components/dashboard/WorkspaceHome.vue`
- Create: `courseops-web/src/components/dashboard/TodoCard.vue`
- Create: `courseops-web/src/components/dashboard/SmartSuggestionCard.vue`
- Create: `courseops-web/src/components/dashboard/WeeklyOverview.vue`
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Create to-do card**

Create `TodoCard.vue`:

```vue
<script setup lang="ts">
import { CalendarClock } from 'lucide-vue-next'
import type { MaterialItem } from '../../domain/types'
import StatusPill from '../common/StatusPill.vue'

defineProps<{ material: MaterialItem }>()
defineEmits<{ open: [id: string] }>()
</script>

<template>
  <button class="todo-card interactive-lift" @click="$emit('open', material.id)">
    <div>
      <strong>{{ material.title }}</strong>
      <span>{{ material.description }}</span>
    </div>
    <StatusPill :status="material.status" />
    <small><CalendarClock :size="14" /> {{ material.deadline }}</small>
  </button>
</template>
```

- [ ] **Step 2: Create smart suggestion**

Create `SmartSuggestionCard.vue`:

```vue
<script setup lang="ts">
defineProps<{ title: string; description: string; action: string }>()
defineEmits<{ action: [] }>()
</script>

<template>
  <article class="smart-suggestion interactive-lift">
    <span>智能提醒</span>
    <h3>{{ title }}</h3>
    <p>{{ description }}</p>
    <button @click="$emit('action')">{{ action }}</button>
  </article>
</template>
```

- [ ] **Step 3: Create weekly overview**

Create `WeeklyOverview.vue`:

```vue
<script setup lang="ts">
defineProps<{
  pending: number
  dueSoon: number
  completed: number
  feedback: number
}>()
</script>

<template>
  <aside class="weekly-overview">
    <h3>本周概览</h3>
    <div><span>待提交材料</span><strong>{{ pending }}</strong></div>
    <div><span>临期项目</span><strong>{{ dueSoon }}</strong></div>
    <div><span>已完成项目</span><strong>{{ completed }}</strong></div>
    <div><span>教师反馈</span><strong>{{ feedback }}</strong></div>
  </aside>
</template>
```

- [ ] **Step 4: Create workspace page**

Create `WorkspaceHome.vue`:

```vue
<script setup lang="ts">
import type { CourseProject, MaterialItem, User } from '../../domain/types'
import PageHero from '../common/PageHero.vue'
import ProgressBar from '../common/ProgressBar.vue'
import RiskBadge from '../common/RiskBadge.vue'
import SmartSuggestionCard from './SmartSuggestionCard.vue'
import TodoCard from './TodoCard.vue'
import WeeklyOverview from './WeeklyOverview.vue'

defineProps<{
  user: User
  materials: MaterialItem[]
  projects: CourseProject[]
}>()

defineEmits<{
  openMaterial: [id: string]
  openProject: [id: string]
  navigateTasks: []
}>()
</script>

<template>
  <section class="workspace-home motion-rise">
    <PageHero
      eyebrow="My Course Workspace"
      :title="`${user.name}，今天有 ${materials.length} 项课程材料需要处理`"
      description="系统已按截止时间、教师反馈和材料状态为你整理好今天最重要的事项。"
    >
      <template #actions>
        <button class="primary-action" @click="$emit('navigateTasks')">查看材料流转</button>
      </template>
    </PageHero>

    <div class="workspace-layout">
      <section class="workspace-main">
        <h2>今日待办</h2>
        <div class="todo-grid">
          <TodoCard v-for="material in materials" :key="material.id" :material="material" @open="$emit('openMaterial', $event)" />
        </div>

        <h2>我的课程项目</h2>
        <div class="workspace-projects">
          <button v-for="project in projects" :key="project.id" class="workspace-project-card" @click="$emit('openProject', project.id)">
            <div>
              <strong>{{ project.title }}</strong>
              <span>{{ project.phase }} · {{ project.deadline }}</span>
            </div>
            <ProgressBar :value="project.progress" />
            <RiskBadge :risk="project.riskLevel" />
          </button>
        </div>
      </section>

      <aside class="workspace-aside">
        <WeeklyOverview
          :pending="materials.filter((item) => item.status === 'preparing').length"
          :due-soon="projects.filter((item) => item.riskLevel !== 'low').length"
          :completed="materials.filter((item) => item.status === 'approved').length"
          :feedback="materials.reduce((sum, item) => sum + item.feedbackCount, 0)"
        />
        <SmartSuggestionCard
          title="Web 期末项目将在 2 天后截止"
          description="说明书正文仍处于需修改状态，建议先处理教师反馈。"
          action="处理反馈"
          @action="$emit('navigateTasks')"
        />
      </aside>
    </div>
  </section>
</template>
```

- [ ] **Step 4A: Add role-specific workspace variants**

Workspace must change by role, not only by user name.

Student variant must show:

- today materials to submit
- returned materials
- due-soon course projects
- recent teacher feedback
- quick upload entry

Teacher variant must show:

- pending review materials
- missing student/team submissions
- projects that need reminders
- course project risk
- quick feedback entry

Admin variant must show:

- school-wide project health
- high-risk courses
- weekly submission trend
- abnormal operation logs
- audit entry

Acceptance:

- Switching role changes title, cards, visible buttons, and insight copy.

- [ ] **Step 5: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 10: Rebuild Course Project Center and Project Space

**Files:**
- Create: `courseops-web/src/components/projects/ProjectCenter.vue`
- Create: `courseops-web/src/components/projects/CourseProjectCard.vue`
- Create: `courseops-web/src/components/projects/ProjectSpace.vue`
- Create: `courseops-web/src/components/projects/MaterialChecklist.vue`
- Create: `courseops-web/src/components/projects/SubmissionRecordList.vue`
- Create: `courseops-web/src/components/projects/FeedbackList.vue`
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Create project card**

Create `CourseProjectCard.vue`:

```vue
<script setup lang="ts">
import type { Course, CourseProject } from '../../domain/types'
import ProgressBar from '../common/ProgressBar.vue'
import RiskBadge from '../common/RiskBadge.vue'

defineProps<{ project: CourseProject; course?: Course }>()
defineEmits<{ open: [id: string] }>()
</script>

<template>
  <article class="course-project-card interactive-lift">
    <span>{{ course?.name ?? '课程项目' }}</span>
    <h3>{{ project.title }}</h3>
    <p>{{ project.description }}</p>
    <div class="project-meta">
      <span>教师：{{ course?.teacher ?? '-' }}</span>
      <span>{{ project.members.length }} 位成员</span>
      <span>{{ project.deadline }} 截止</span>
    </div>
    <ProgressBar :value="project.progress" />
    <div class="project-card-foot">
      <RiskBadge :risk="project.riskLevel" />
      <button @click="$emit('open', project.id)">进入项目空间</button>
    </div>
  </article>
</template>
```

- [ ] **Step 2: Create project center**

Create `ProjectCenter.vue`:

```vue
<script setup lang="ts">
import { computed, ref } from 'vue'
import type { Course, CourseProject } from '../../domain/types'
import PageHero from '../common/PageHero.vue'
import SegmentedControl from '../common/SegmentedControl.vue'
import CourseProjectCard from './CourseProjectCard.vue'

const props = defineProps<{ projects: CourseProject[]; courses: Course[] }>()
defineEmits<{ openProject: [id: string] }>()

const filter = ref('mine')
const filterOptions = [
  { label: '我的项目', value: 'mine' },
  { label: '全部课程', value: 'all' },
  { label: '临近截止', value: 'due' },
  { label: '有材料缺口', value: 'gap' },
  { label: '已完成', value: 'done' },
]

const visibleProjects = computed(() => {
  if (filter.value === 'due') return props.projects.filter((project) => project.riskLevel !== 'low')
  if (filter.value === 'done') return props.projects.filter((project) => project.progress >= 90)
  return props.projects
})

function courseOf(project: CourseProject) {
  return props.courses.find((course) => course.id === project.courseId)
}
</script>

<template>
  <section class="project-center motion-rise">
    <PageHero
      eyebrow="Course Project Hub"
      title="课程项目中心"
      description="集中查看课程项目、团队进度、材料要求与截止风险。"
    />
    <SegmentedControl v-model="filter" :options="filterOptions" />
    <div class="project-card-grid">
      <CourseProjectCard
        v-for="project in visibleProjects"
        :key="project.id"
        :project="project"
        :course="courseOf(project)"
        @open="$emit('openProject', $event)"
      />
    </div>
  </section>
</template>
```

- [ ] **Step 3: Create material checklist and project space**

Create `MaterialChecklist.vue`:

```vue
<script setup lang="ts">
import type { MaterialItem } from '../../domain/types'
import StatusPill from '../common/StatusPill.vue'

defineProps<{ materials: MaterialItem[] }>()
defineEmits<{ openMaterial: [id: string] }>()
</script>

<template>
  <section class="material-checklist">
    <h3>材料清单</h3>
    <button v-for="item in materials" :key="item.id" @click="$emit('openMaterial', item.id)">
      <div>
        <strong>{{ item.title }}</strong>
        <span>{{ item.required ? '必交' : '选交' }} · {{ item.format }} · {{ item.deadline }}</span>
      </div>
      <StatusPill :status="item.status" />
    </button>
  </section>
</template>
```

Create `SubmissionRecordList.vue`:

```vue
<script setup lang="ts">
import type { Submission } from '../../domain/types'
defineProps<{ submissions: Submission[] }>()
</script>

<template>
  <section class="submission-records">
    <h3>提交记录</h3>
    <article v-for="item in submissions" :key="item.id">
      <strong>{{ item.fileName }}</strong>
      <span>{{ item.submittedBy }} · {{ item.submittedAt }}</span>
      <p>{{ item.feedback }}</p>
    </article>
  </section>
</template>
```

Create `FeedbackList.vue`:

```vue
<script setup lang="ts">
import type { MaterialItem } from '../../domain/types'
defineProps<{ materials: MaterialItem[] }>()
</script>

<template>
  <section class="feedback-list">
    <h3>教师反馈</h3>
    <article v-for="item in materials.filter((material) => material.feedbackCount > 0)" :key="item.id">
      <strong>{{ item.title }}</strong>
      <p>{{ item.feedbackCount }} 条反馈待处理。</p>
    </article>
  </section>
</template>
```

Create `ProjectSpace.vue`:

```vue
<script setup lang="ts">
import type { Course, CourseProject, MaterialItem, Submission } from '../../domain/types'
import PageHero from '../common/PageHero.vue'
import ProgressRing from '../common/ProgressRing.vue'
import FeedbackList from './FeedbackList.vue'
import MaterialChecklist from './MaterialChecklist.vue'
import SubmissionRecordList from './SubmissionRecordList.vue'

defineProps<{
  project: CourseProject
  course?: Course
  materials: MaterialItem[]
  submissions: Submission[]
}>()

defineEmits<{ openMaterial: [id: string]; back: [] }>()
</script>

<template>
  <section class="project-space motion-rise">
    <PageHero
      :eyebrow="course?.name ?? 'Project Space'"
      :title="project.title"
      :description="project.description"
    >
      <template #actions>
        <button @click="$emit('back')">返回项目中心</button>
      </template>
    </PageHero>
    <div class="project-space-grid">
      <aside class="project-summary-card">
        <ProgressRing :value="project.progress" label="我的完成度" />
        <p>{{ project.recentFeedback }}</p>
      </aside>
      <MaterialChecklist :materials="materials" @open-material="$emit('openMaterial', $event)" />
      <SubmissionRecordList :submissions="submissions" />
      <FeedbackList :materials="materials" />
    </div>
  </section>
</template>
```

- [ ] **Step 3A: Upgrade Project Space into tabbed functional workspace**

Project Space must expose these sections through tabs or segmented control:

- `概览`: course, phase, deadline, health score, risk reason, recent feedback.
- `材料`: material name, format, required/optional, deadline, status, latest file, feedback count, action button.
- `成员`: member name, responsibility, progress, overdue risk.
- `提交`: file name, submitter, submit time, status, feedback summary.
- `反馈`: feedback object, feedback content, unresolved/resolved, action button.
- `动态`: material history and operation logs related to the project.

Acceptance:

- Clicking a material action enters `/app/tasks?materialId=<id>`.
- Team members use `TeamAvatarStack`.
- Recent dynamics use `MaterialHistory`, not static text.

- [ ] **Step 4: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 11: Rebuild Material Workflow Center

**Files:**
- Create: `courseops-web/src/components/tasks/MaterialWorkflowCenter.vue`
- Create: `courseops-web/src/components/tasks/MaterialTaskCard.vue`
- Create: `courseops-web/src/components/tasks/MaterialDetailDrawer.vue`
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Create material task card**

Create `MaterialTaskCard.vue`:

```vue
<script setup lang="ts">
import { FileUp } from 'lucide-vue-next'
import type { MaterialItem } from '../../domain/types'
import ProgressBar from '../common/ProgressBar.vue'
import StatusPill from '../common/StatusPill.vue'

defineProps<{ material: MaterialItem; projectName: string }>()
defineEmits<{ select: [id: string] }>()
</script>

<template>
  <button class="material-task-card interactive-lift" @click="$emit('select', material.id)">
    <div class="task-card-top">
      <strong>{{ material.title }}</strong>
      <StatusPill :status="material.status" />
    </div>
    <p>{{ projectName }}</p>
    <span>{{ material.assignee }} · {{ material.deadline }} · {{ material.format }}</span>
    <ProgressBar :value="material.progress" />
    <small><FileUp :size="14" /> {{ material.latestFile || '暂无文件' }} · {{ material.feedbackCount }} 条反馈</small>
  </button>
</template>
```

- [ ] **Step 2: Create detail drawer**

Create `MaterialDetailDrawer.vue`:

```vue
<script setup lang="ts">
import type { MaterialItem, MaterialStatus, Submission } from '../../domain/types'
import StatusPill from '../common/StatusPill.vue'

defineProps<{
  material: MaterialItem
  submissions: Submission[]
}>()

defineEmits<{
  updateStatus: [status: MaterialStatus]
  upload: [file: File]
}>()

function emitFile(event: Event, emit: (event: 'upload', file: File) => void) {
  const input = event.target as HTMLInputElement
  const file = input.files?.[0]
  if (file) emit('upload', file)
}
</script>

<template>
  <aside class="material-detail-drawer">
    <div class="drawer-head">
      <span>材料详情</span>
      <h2>{{ material.title }}</h2>
      <StatusPill :status="material.status" />
    </div>
    <section>
      <h3>材料要求</h3>
      <p>{{ material.description }}</p>
      <p>{{ material.required ? '必交材料' : '选交材料' }} · {{ material.format }} · {{ material.deadline }} 截止</p>
    </section>
    <section>
      <h3>上传记录</h3>
      <article v-for="submission in submissions" :key="submission.id">
        <strong>{{ submission.fileName }}</strong>
        <span>{{ submission.submittedAt }}</span>
        <p>{{ submission.feedback }}</p>
      </article>
    </section>
    <section>
      <h3>操作</h3>
      <label class="upload-button">
        上传文件
        <input type="file" @change="emitFile($event, $emit)" />
      </label>
      <button @click="$emit('updateStatus', 'submitted')">提交审核</button>
      <button @click="$emit('updateStatus', 'revision_required')">标记需修改</button>
      <button @click="$emit('updateStatus', 'approved')">标记已通过</button>
    </section>
  </aside>
</template>
```

- [ ] **Step 3: Create workflow page**

Create `MaterialWorkflowCenter.vue`:

```vue
<script setup lang="ts">
import { computed } from 'vue'
import { materialStatusLabels } from '../../domain/constants'
import type { CourseProject, MaterialItem, MaterialStatus, Submission } from '../../domain/types'
import PageHero from '../common/PageHero.vue'
import MaterialDetailDrawer from './MaterialDetailDrawer.vue'
import MaterialTaskCard from './MaterialTaskCard.vue'

const props = defineProps<{
  columns: Array<{ status: MaterialStatus; materials: MaterialItem[] }>
  projects: CourseProject[]
  selectedMaterial: MaterialItem
  submissions: Submission[]
}>()

defineEmits<{
  selectMaterial: [id: string]
  updateStatus: [id: string, status: MaterialStatus]
  upload: [id: string, file: File]
}>()

const selectedSubmissions = computed(() => props.submissions.filter((item) => item.materialId === props.selectedMaterial.id))

function projectName(projectId: string) {
  return props.projects.find((project) => project.id === projectId)?.title ?? '课程项目'
}
</script>

<template>
  <section class="material-workflow motion-rise">
    <PageHero
      eyebrow="Material Workflow"
      title="课程材料流转中心"
      description="按真实提交状态管理材料准备、上传、审核、修改和通过。"
    />
    <div class="workflow-layout">
      <div class="workflow-board">
        <section v-for="column in columns" :key="column.status" class="workflow-column">
          <h3>{{ materialStatusLabels[column.status] }} <span>{{ column.materials.length }}</span></h3>
          <MaterialTaskCard
            v-for="material in column.materials"
            :key="material.id"
            :material="material"
            :project-name="projectName(material.projectId)"
            @select="$emit('selectMaterial', $event)"
          />
        </section>
      </div>
      <MaterialDetailDrawer
        :material="selectedMaterial"
        :submissions="selectedSubmissions"
        @update-status="$emit('updateStatus', selectedMaterial.id, $event)"
        @upload="$emit('upload', selectedMaterial.id, $event)"
      />
    </div>
  </section>
</template>
```

- [ ] **Step 3A: Enforce real material workflow interactions**

Material Workflow is the strongest demo page. It must satisfy:

- Columns are exactly `待准备`, `待提交`, `已提交`, `需修改`, `已通过`.
- Clicking a card changes the right detail drawer.
- Upload validates file existence, extension, and 50MB max size.
- Successful upload updates `latestFile`.
- Successful upload sets status to `submitted`.
- Successful upload sets progress to at least `88`.
- Successful upload appends a `Submission`.
- Successful upload appends a `MaterialHistory`.
- Successful upload appends an `OperationLog` with action `上传文件`.
- Successful upload creates a `NotificationItem`.
- The Kanban card automatically moves to `已提交`.
- `标记需修改` sets status to `revision_required`, appends history, appends log, creates notification.
- `标记已通过` sets status to `approved`, progress to `100`, appends history, appends log, creates notification.

Forbidden outcomes:

- Button exists but no state changes.
- Upload succeeds without a submission record.
- Status changes without logs.
- Kanban columns do not update after status changes.

- [ ] **Step 3B: Add material status timeline**

Create and render `MaterialTimeline.vue` inside the drawer:

```vue
<script setup lang="ts">
import type { MaterialHistory } from '../../domain/types'
defineProps<{ histories: MaterialHistory[] }>()
</script>

<template>
  <section class="material-timeline">
    <h3>状态历史</h3>
    <article v-for="history in histories" :key="history.id">
      <strong>{{ history.action }}</strong>
      <span>{{ history.actor }} · {{ history.createdAt }}</span>
      <p v-if="history.fileName">文件：{{ history.fileName }}</p>
      <p v-else-if="history.fromStatus && history.toStatus">{{ history.fromStatus }} -> {{ history.toStatus }}</p>
    </article>
  </section>
</template>
```

- [ ] **Step 4: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 12: Rebuild Operational Insights

**Files:**
- Create: `courseops-web/src/components/analytics/OperationalInsights.vue`
- Create: `courseops-web/src/components/analytics/InsightChartCard.vue`
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Create chart card shell**

Create `InsightChartCard.vue`:

```vue
<script setup lang="ts">
defineProps<{ title: string; insight: string }>()
</script>

<template>
  <article class="insight-chart-card">
    <h3>{{ title }}</h3>
    <div class="chart-slot">
      <slot />
    </div>
    <p>{{ insight }}</p>
  </article>
</template>
```

- [ ] **Step 2: Create analytics page**

Create `OperationalInsights.vue`:

```vue
<script setup lang="ts">
import { onMounted, ref } from 'vue'
import * as echarts from 'echarts'
import type { AnalyticsSnapshot, UserRole } from '../../domain/types'
import PageHero from '../common/PageHero.vue'
import InsightChartCard from './InsightChartCard.vue'

const props = defineProps<{ analytics: AnalyticsSnapshot; role: UserRole }>()

const trendRef = ref<HTMLDivElement | null>(null)
const courseRef = ref<HTMLDivElement | null>(null)
const riskRef = ref<HTMLDivElement | null>(null)
const gapRef = ref<HTMLDivElement | null>(null)

function renderChart(el: HTMLDivElement | null, option: echarts.EChartsOption) {
  if (!el) return
  const chart = echarts.init(el)
  chart.setOption(option)
}

onMounted(() => {
  const tooltip = {
    backgroundColor: 'rgba(255,255,255,0.9)',
    borderColor: 'rgba(0,0,0,0.06)',
    textStyle: { color: '#1d1d1f' },
  }
  renderChart(trendRef.value, {
    tooltip,
    grid: { left: 24, right: 18, top: 18, bottom: 28 },
    xAxis: { type: 'category', data: props.analytics.submissionTrend.map((item) => item.day) },
    yAxis: { type: 'value' },
    series: [{ type: 'line', smooth: true, data: props.analytics.submissionTrend.map((item) => item.count), itemStyle: { color: '#007a6c' } }],
  })
  renderChart(courseRef.value, {
    tooltip,
    grid: { left: 36, right: 18, top: 18, bottom: 42 },
    xAxis: { type: 'category', data: props.analytics.courseCompletion.map((item) => item.course) },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: props.analytics.courseCompletion.map((item) => item.value), itemStyle: { color: '#3478f6', borderRadius: [8, 8, 0, 0] } }],
  })
  renderChart(riskRef.value, {
    tooltip,
    series: [{ type: 'pie', radius: ['58%', '78%'], data: props.analytics.riskDistribution.map((item) => ({ name: item.level, value: item.count })) }],
  })
  renderChart(gapRef.value, {
    tooltip,
    grid: { left: 36, right: 18, top: 18, bottom: 42 },
    xAxis: { type: 'category', data: props.analytics.materialGap.map((item) => item.material) },
    yAxis: { type: 'value' },
    series: [{ type: 'bar', data: props.analytics.materialGap.map((item) => item.count), itemStyle: { color: '#b87500', borderRadius: [8, 8, 0, 0] } }],
  })
})
</script>

<template>
  <section class="operational-insights motion-rise">
    <PageHero
      eyebrow="Operational Insights"
      title="本周 3 个课程项目存在材料缺口"
      :description="role === 'admin' ? '全校课程项目运行态势、风险分布与审计动态。' : '围绕你的课程项目生成可执行的材料提交建议。'"
    />
    <section class="insight-metrics">
      <article><span>材料完成率</span><strong>{{ analytics.materialCompletionRate }}%</strong></article>
      <article><span>临期风险数</span><strong>{{ analytics.dueSoonCount }}</strong></article>
      <article><span>需修改材料</span><strong>{{ analytics.revisionRequiredCount }}</strong></article>
      <article><span>教师反馈待处理</span><strong>{{ analytics.pendingFeedbackCount }}</strong></article>
    </section>
    <section class="insight-grid">
      <InsightChartCard title="近 7 天提交趋势" insight="周六、周日提交量明显增加，建议提前发送提醒。"><div ref="trendRef" class="chart"></div></InsightChartCard>
      <InsightChartCard title="不同课程材料完成度" insight="数据库原理完成度偏低，应优先检查 ER 图材料。"><div ref="courseRef" class="chart"></div></InsightChartCard>
      <InsightChartCard title="风险等级分布" insight="高风险项目数量可控，但临期风险需要今天处理。"><div ref="riskRef" class="chart"></div></InsightChartCard>
      <InsightChartCard title="材料类型缺口分布" insight="数据库设计图缺口最多，应优先补齐。"><div ref="gapRef" class="chart"></div></InsightChartCard>
    </section>
  </section>
</template>
```

- [ ] **Step 2A: Add decision actions to every insight chart**

Each chart card must include:

- chart title
- chart body
- automatic insight
- suggested action button

Required action examples:

- `近 7 天提交趋势`: insight `周末提交明显增加，建议教师在周五前发布提醒。`, action `发送提醒`.
- `课程完成度`: insight `数据库原理完成度偏低，ER 图缺口最多。`, action `查看数据库项目`.
- `风险分布`: insight `高风险项目集中在 Web 应用开发和数据库原理。`, action `查看高风险项目`.
- `材料缺口`: insight `数据库设计图和源码压缩包缺口最高。`, action `进入材料流转`.

- [ ] **Step 2B: Enforce ECharts lifecycle quality**

Chart implementation must:

- keep chart instances in an array or map
- call `dispose()` before re-render or on unmount
- listen to window resize and call `resize()`
- remove resize listener on unmount
- use calm colors from design tokens
- use white glass tooltip style
- avoid default saturated ECharts palette
- keep enough chart padding and avoid cramped axes

- [ ] **Step 3: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 13: Rebuild Activity Timeline and Audit Logs

**Files:**
- Create: `courseops-web/src/components/logs/ActivityTimeline.vue`
- Create: `courseops-web/src/components/logs/TimelineItem.vue`
- Create: `courseops-web/src/components/logs/LogFilterBar.vue`
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Create filter bar**

Create `LogFilterBar.vue`:

```vue
<script setup lang="ts">
defineProps<{ searchText: string; actionType: string }>()
defineEmits<{
  'update:searchText': [value: string]
  'update:actionType': [value: string]
}>()
</script>

<template>
  <div class="log-filter-bar">
    <input
      :value="searchText"
      placeholder="搜索操作人、对象、课程项目..."
      @input="$emit('update:searchText', ($event.target as HTMLInputElement).value)"
    />
    <select :value="actionType" @change="$emit('update:actionType', ($event.target as HTMLSelectElement).value)">
      <option value="all">全部操作</option>
      <option value="更新材料状态">更新材料状态</option>
      <option value="提交反馈">提交反馈</option>
      <option value="上传文件">上传文件</option>
    </select>
  </div>
</template>
```

- [ ] **Step 2: Create timeline item**

Create `TimelineItem.vue`:

```vue
<script setup lang="ts">
import { ref } from 'vue'
import type { OperationLog, UserRole } from '../../domain/types'

defineProps<{ log: OperationLog; role: UserRole }>()

const expanded = ref(false)
</script>

<template>
  <article class="timeline-item">
    <button class="timeline-main" @click="expanded = !expanded">
      <span>{{ log.createdAt }}</span>
      <strong>{{ log.action }}</strong>
      <p>{{ log.actor }} 将 {{ log.targetName }} 从“{{ log.before }}”更新为“{{ log.after }}”</p>
    </button>
    <div v-if="expanded" class="timeline-detail">
      <span>来源模块：{{ log.source }}</span>
      <span>操作对象：{{ log.targetType }}</span>
      <span v-if="role === 'admin'">审计 ID：{{ log.id }}</span>
    </div>
  </article>
</template>
```

- [ ] **Step 3: Create timeline page**

Create `ActivityTimeline.vue`:

```vue
<script setup lang="ts">
import { computed, ref } from 'vue'
import type { OperationLog, UserRole } from '../../domain/types'
import PageHero from '../common/PageHero.vue'
import LogFilterBar from './LogFilterBar.vue'
import TimelineItem from './TimelineItem.vue'

const props = defineProps<{ logs: OperationLog[]; role: UserRole }>()

const searchText = ref('')
const actionType = ref('all')

const visibleLogs = computed(() => props.logs.filter((log) => {
  const matchesSearch = !searchText.value || `${log.actor}${log.targetName}${log.source}`.includes(searchText.value)
  const matchesType = actionType.value === 'all' || log.action === actionType.value
  return matchesSearch && matchesType
}))
</script>

<template>
  <section class="activity-timeline motion-rise">
    <PageHero
      :eyebrow="role === 'admin' ? 'Audit Timeline' : 'Activity Timeline'"
      :title="role === 'admin' ? '审计日志' : '动态记录'"
      :description="role === 'admin' ? '可检索的课程项目操作审计记录。' : '查看与你相关的提交、反馈和状态变化。'"
    />
    <LogFilterBar v-model:search-text="searchText" v-model:action-type="actionType" />
    <section class="timeline-list">
      <TimelineItem v-for="log in visibleLogs" :key="log.id" :log="log" :role="role" />
    </section>
  </section>
</template>
```

- [ ] **Step 3A: Add role-specific timeline behavior**

Timeline title and visible detail must change by role:

- Student role title: `动态记录`.
- Student timeline shows uploads, teacher feedback, material status changes, project due risks.
- Teacher role title: `课程动态`.
- Teacher timeline shows student submissions, reviewed materials, risk projects.
- Admin role title: `审计日志`.
- Admin timeline shows all operations, actor, target, before/after status, source module, audit ID.

Rules:

- Mongo audit ID appears only when admin expands a timeline item.
- Main timeline must not show `mongo-8301` style technical fields.
- Every timeline item has icon and semantic color.
- Search filters actor, project, material, and source.
- Action type filter supports upload, status update, feedback, risk, notification read.
- Role filter is visible to admin.

- [ ] **Step 4: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 14: Recompose App.vue

**Files:**
- Modify: `courseops-web/src/App.vue`

- [ ] **Step 1: Replace single-file app body with composition root**

`App.vue` should import the store, shell, and pages. It should only coordinate navigation and pass props.

Target structure:

```vue
<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { AppPage } from './domain/constants'
import { useCourseOpsStore } from './stores/useCourseOpsStore'
import AppShell from './components/shell/AppShell.vue'
import PublicLanding from './components/landing/PublicLanding.vue'
import WorkspaceHome from './components/dashboard/WorkspaceHome.vue'
import ProjectCenter from './components/projects/ProjectCenter.vue'
import ProjectSpace from './components/projects/ProjectSpace.vue'
import MaterialWorkflowCenter from './components/tasks/MaterialWorkflowCenter.vue'
import OperationalInsights from './components/analytics/OperationalInsights.vue'
import ActivityTimeline from './components/logs/ActivityTimeline.vue'

const page = ref<AppPage>('landing')
const searchText = ref('')
const store = useCourseOpsStore()

const selectedCourse = computed(() => store.courses.value.find((course) => course.id === store.selectedProject.value?.courseId))
const selectedProjectMaterials = computed(() => store.materials.value.filter((material) => material.projectId === store.selectedProjectId.value))
const selectedProjectSubmissions = computed(() => store.submissions.value.filter((submission) => selectedProjectMaterials.value.some((material) => material.id === submission.materialId)))

function openProject(id: string) {
  store.selectedProjectId.value = id
  page.value = 'project-space'
  store.persistLocalState()
}

function openMaterial(id: string) {
  store.selectedMaterialId.value = id
  page.value = 'tasks'
  store.persistLocalState()
}

onMounted(store.loadInitialData)
</script>

<template>
  <PublicLanding
    v-if="page === 'landing'"
    @enter-app="page = 'workspace'"
    @view-projects="page = 'projects'"
  />

  <AppShell
    v-else-if="store.currentUser.value"
    :current-page="page"
    :user="store.currentUser.value"
    v-model:search-text="searchText"
    @navigate="page = $event"
    @switch-role="store.switchRole"
  >
    <WorkspaceHome
      v-if="page === 'workspace'"
      :user="store.currentUser.value"
      :materials="store.dueSoonMaterials.value"
      :projects="store.projects.value"
      @open-material="openMaterial"
      @open-project="openProject"
      @navigate-tasks="page = 'tasks'"
    />
    <ProjectCenter
      v-else-if="page === 'projects'"
      :projects="store.projects.value"
      :courses="store.courses.value"
      @open-project="openProject"
    />
    <ProjectSpace
      v-else-if="page === 'project-space'"
      :project="store.selectedProject.value"
      :course="selectedCourse"
      :materials="selectedProjectMaterials"
      :submissions="selectedProjectSubmissions"
      @open-material="openMaterial"
      @back="page = 'projects'"
    />
    <MaterialWorkflowCenter
      v-else-if="page === 'tasks'"
      :columns="store.materialColumns.value"
      :projects="store.projects.value"
      :selected-material="store.selectedMaterial.value"
      :submissions="store.submissions.value"
      @select-material="store.selectedMaterialId.value = $event"
      @update-status="store.changeMaterialStatus"
      @upload="store.submitMaterialFile"
    />
    <OperationalInsights
      v-else-if="page === 'analytics' && store.analytics.value"
      :analytics="store.analytics.value"
      :role="store.currentRole.value"
    />
    <ActivityTimeline
      v-else-if="page === 'logs'"
      :logs="store.logs.value"
      :role="store.currentRole.value"
    />
  </AppShell>
</template>
```

- [ ] **Step 2: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds. If `.value` usage in template causes mismatch, unwrap refs in script-level computed helpers before passing.

### Task 15: Complete Platform CSS

**Files:**
- Modify: `courseops-web/src/style.css`

- [ ] **Step 1: Add component styles**

Add styles for:

- `.public-landing`
- `.landing-hero`
- `.floating-visual`
- `.document-stack`
- `.app-shell`
- `.side-nav`
- `.top-nav`
- `.page-hero`
- `.glass-card`
- `.workspace-home`
- `.todo-card`
- `.weekly-overview`
- `.project-center`
- `.course-project-card`
- `.project-space`
- `.material-workflow`
- `.workflow-board`
- `.workflow-column`
- `.material-task-card`
- `.material-detail-drawer`
- `.operational-insights`
- `.insight-chart-card`
- `.activity-timeline`
- `.timeline-item`
- `.empty-state`
- `.skeleton-card`

Use the tokens from `styles/tokens.css`. Avoid raw colors except chart-specific colors.

- [ ] **Step 2: Add responsive rules**

Add breakpoints:

```css
@media (max-width: 1366px) {
  .app-content {
    max-width: 1280px;
  }
}

@media (max-width: 960px) {
  .app-shell {
    grid-template-columns: 1fr;
  }

  .side-nav {
    position: sticky;
    top: 0;
    z-index: 20;
    width: 100%;
    flex-direction: row;
    overflow-x: auto;
  }

  .workflow-layout,
  .project-space-grid,
  .workspace-layout {
    grid-template-columns: 1fr;
  }

  .workflow-board {
    overflow-x: auto;
  }
}
```

- [ ] **Step 3: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: build succeeds.

### Task 16: Browser Verification

**Files:**
- No code changes unless verification finds defects.

- [ ] **Step 1: Start dev server**

Run:

```bash
cd courseops-web
npm.cmd run dev -- --host 127.0.0.1 --port 5173
```

Expected: local app opens at `http://127.0.0.1:5173/`.

- [ ] **Step 2: Verify landing**

Manual checks:

- Hero appears at `/`.
- Primary button enters workspace.
- Secondary button enters project center.
- Pseudo-3D material stack is visible.
- No ordinary admin dashboard appears on public homepage.

- [ ] **Step 3: Verify role-aware shell**

Manual checks:

- Sidebar is visually light.
- Top search exists.
- Role switch changes visible user identity.
- Student view does not feel like admin dashboard.

- [ ] **Step 4: Verify workspace**

Manual checks:

- Page title tells the user what to do today.
- Today to-do cards are material actions.
- Weekly overview has pending material, due soon, completed, feedback.
- Smart suggestion points to a concrete action.

- [ ] **Step 5: Verify project center**

Manual checks:

- Project grid appears.
- Segmented controls filter projects.
- Entering project space shows overview, material checklist, submissions, feedback.

- [ ] **Step 6: Verify material workflow**

Manual checks:

- Columns are `待准备`, `待提交`, `已提交`, `需修改`, `已通过`.
- Cards are materials, not generic tasks.
- Selecting card updates the detail drawer.
- Status button updates state and appends a log.
- Upload input records file name in local simulated state.

- [ ] **Step 7: Verify analytics**

Manual checks:

- Top hero contains a real operational conclusion.
- Metric cards link to actionable concepts.
- Every chart has an insight sentence.
- ECharts colors are calm and consistent.

- [ ] **Step 8: Verify logs**

Manual checks:

- Student role shows “动态记录”.
- Admin role shows “审计日志”.
- Mongo ID appears only in expanded/admin detail.
- Search and action filter update visible records.

### Task 16A: Add Advanced Product Decoration Layer

**Files:**
- Create: `courseops-web/src/components/common/AnimatedNumber.vue`
- Create: `courseops-web/src/components/common/VisualGlowBackground.vue`
- Create: `courseops-web/src/components/common/ProductShowcaseHero.vue`
- Create: `courseops-web/src/components/landing/FloatingCourseCards.vue`
- Create: `courseops-web/src/components/landing/WorkflowPreviewBoard.vue`
- Create: `courseops-web/src/components/landing/DeadlineCalendarCube.vue`
- Modify: page components that use these visuals.

- [ ] **Step 1: Add animated numbers**

Create `AnimatedNumber.vue`:

```vue
<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'

const props = defineProps<{ value: number; suffix?: string }>()
const display = ref(0)

function animate() {
  const start = display.value
  const end = props.value
  const started = performance.now()
  const duration = 680
  function frame(now: number) {
    const progress = Math.min(1, (now - started) / duration)
    display.value = Math.round(start + (end - start) * progress)
    if (progress < 1) requestAnimationFrame(frame)
  }
  requestAnimationFrame(frame)
}

onMounted(animate)
watch(() => props.value, animate)
</script>

<template>
  <strong class="animated-number">{{ display }}{{ suffix ?? '' }}</strong>
</template>
```

- [ ] **Step 2: Add visual glow background**

Create `VisualGlowBackground.vue`:

```vue
<template>
  <div class="visual-glow-background" aria-hidden="true">
    <i class="glow-a"></i>
    <i class="glow-b"></i>
    <i class="glow-c"></i>
  </div>
</template>
```

- [ ] **Step 3: Add product showcase hero shell**

Create `ProductShowcaseHero.vue`:

```vue
<script setup lang="ts">
defineProps<{ title: string; description: string; eyebrow?: string }>()
</script>

<template>
  <section class="product-showcase-hero">
    <div>
      <span v-if="eyebrow" class="eyebrow">{{ eyebrow }}</span>
      <h1>{{ title }}</h1>
      <p>{{ description }}</p>
      <slot name="actions" />
    </div>
    <slot name="visual" />
  </section>
</template>
```

- [ ] **Step 4: Add landing visuals**

Create landing decorative components with real information:

- `FloatingCourseCards`: shows project title, deadline, risk, progress.
- `WorkflowPreviewBoard`: shows material states across `待准备`, `待提交`, `已提交`, `需修改`, `已通过`.
- `DeadlineCalendarCube`: shows nearest deadline and days remaining.

Acceptance:

- Landing uses at least two of these plus `ThreeDDocumentStack`.
- Workspace uses `AnimatedNumber` and `VisualGlowBackground`.
- Project Center uses `TeamAvatarStack` and `ProgressRing`.
- Material Workflow uses `MaterialTimeline` and drawer slide-in.
- Analytics uses `AnimatedNumber` and refined chart cards.

Forbidden:

- Decoration that conveys no product information.
- Decoration that makes the interface slower or visually noisy.

### Task 16B: Add Loading, Empty, and Error States Everywhere

**Files:**
- Modify page components.
- Modify common state components.

- [ ] **Step 1: Add loading skeleton coverage**

Every main page must render skeletons while `store.isLoading` is true:

- Landing data metrics skeleton.
- Workspace todo skeleton.
- Project card skeleton.
- Material card skeleton.
- Chart skeleton.
- Timeline skeleton.

- [ ] **Step 2: Add empty states**

Every main list must have an empty state:

- no course projects
- no todo materials
- no submissions
- no logs
- no search results
- no notifications

- [ ] **Step 3: Add error states**

Every risky action must show product-grade error state:

- data load failed
- upload failed
- file format error
- file too large
- status update failed

Acceptance:

- No page becomes blank on loading, empty, or error states.
- Empty states include icon/orb, explanation, and action button where useful.

### Task 17: Quality Commands

**Files:**
- Modify `package.json` only if adding scripts.

- [ ] **Step 1: Check available scripts**

Run:

```bash
cd courseops-web
npm.cmd run
```

Expected: shows `dev`, `build`, `preview`. There is currently no `lint` script.

- [ ] **Step 2: Build**

Run:

```bash
cd courseops-web
npm.cmd run build
```

Expected: TypeScript and Vite build pass.

- [ ] **Step 3: Optional typecheck script**

If desired, add to `package.json`:

```json
"typecheck": "vue-tsc -b"
```

Then run:

```bash
cd courseops-web
npm.cmd run typecheck
```

Expected: typecheck passes.

## 7. Acceptance Criteria

Product criteria:

- Public landing page feels like a premium SaaS product, not an admin login screen.
- Landing includes product storytelling, pseudo-3D visuals, workflow preview, data metrics, and CTA.
- Student default experience is “what should I do today,” not “look at system metrics.”
- Teacher/admin roles change actual cards, actions, and insights, not only the displayed name.
- Course projects have a real project-space concept.
- Material workflow uses real submission states and automatically moves cards between columns.
- Upload changes latest file, status, progress, submissions, history, logs, and notifications.
- Analytics page provides decisions and recommendation buttons, not just charts.
- Activity timeline hides technical audit details from ordinary students and reveals audit IDs only for admin-expanded details.
- Notification center is functional and supports mark-read behavior.
- Global search opens a glass command palette, groups results, and navigates to project/material targets.

Technical criteria:

- No business data lives directly in page templates.
- Mock data is centralized in `data/mockCourseOps.ts`.
- UI reads through `services/courseopsApi.ts` and the Pinia `stores/useCourseOpsStore.ts`.
- Vue Router powers `/`, `/app`, `/app/projects`, `/app/projects/:id`, `/app/tasks`, `/app/analytics`, and `/app/logs`.
- Browser address changes on navigation, and refresh preserves the current page.
- `App.vue` is only a router host or minimal composition root, not a giant feature file.
- Store is singleton and does not create separate state per component.
- `npm.cmd run build` passes.
- Local app runs at `http://127.0.0.1:5173/`.

Visual criteria:

- Apple-inspired language is used as a visual reference only.
- No copied Apple assets, logos, or layouts.
- No cyberpunk, dashboard big-screen, rainbow gradient, or cheap template look.
- 1366px, 1440px, 1920px, and tablet widths remain usable.
- Hover, loading, empty, and error states are visually designed.
- App inner page titles are not oversized; the first screen shows title, key task, and primary action together.
- Decoration is functional: pseudo-3D and glow elements must communicate project, material, deadline, workflow, or insight information.
- Final UI cannot degrade into `sidebar + big title + cards + charts`.

## 8. Known Scope Boundaries

This plan creates a local-first platform frontend. It does not require a real backend in the first pass.

Simulated now:

- File upload stores file name and status locally.
- API service returns centralized mock data.
- MongoDB logs are represented by `OperationLog` objects.
- Role switch changes UI perspective.

Backend-ready later:

- Replace `services/courseopsApi.ts` functions with real HTTP calls.
- Connect `uploadMaterial()` to actual object storage or backend upload endpoint.
- Persist `OperationLog` to MongoDB collection `operation_logs`.
- Persist users, courses, projects, materials, and submissions to MySQL.

## 9. Self-Review

Spec coverage:

- Final correction layer: Section 0.1.
- Real Vue Router: Task 1A.
- Pinia singleton store: Task 4.
- Landing page: Task 7.
- Functional search command palette: Task 8A.
- Notification center: Task 8B.
- App shell and role switch: Task 8.
- Workspace dashboard replacement: Task 9.
- Course project center and project space: Task 10.
- Material workflow Kanban, upload validation, status history: Task 11.
- Operational insights: Task 12.
- Activity timeline / audit logs: Task 13.
- Advanced decoration layer: Task 16A.
- Loading, empty, and error states: Task 16B.
- Design tokens and visual polish: Task 5 and Task 15.
- Data model and API layer: Tasks 1-4.
- Verification and quality commands: Tasks 16-17.

Placeholder scan:

- The plan avoids unfinished implementation markers. API upload is intentionally implemented as a local simulated upload with a real return object.

Type consistency:

- `MaterialStatus`, `RiskLevel`, `UserRole`, and page names are defined before use.
- Components use the domain types from `domain/types.ts`.
- Service functions referenced by the store are defined in `services/courseopsApi.ts`.

## 10. Execution Recommendation

Use subagent-driven development if available:

- Worker 1: Tasks 1-4, domain model, Router, Pinia singleton store, API, upload validation primitives.
- Worker 2: Tasks 5-8B, design system, landing, app shell, search command palette, notification center.
- Worker 3: Tasks 9-11, role-aware workspace, project center, project space, material workflow, upload/history/log loop.
- Worker 4: Tasks 12-13, operational insights, ECharts lifecycle, activity timeline, role-aware audit details.
- Main agent: Task 14-17, integration, advanced decoration layer, loading/empty/error states, CSS polish, browser verification.

If working inline, execute in strict order from Task 1 to Task 17 and run `npm.cmd run build` after every task. Do not skip Task 1A, Task 8A, Task 8B, Task 16A, or Task 16B; those tasks are mandatory for the final product-grade version.
