export type UserRole = 'student' | 'teacher' | 'admin'
export type RiskLevel = 'low' | 'medium' | 'high'
export type MaterialStatus = 'not_started' | 'preparing' | 'submitted' | 'revision_required' | 'approved'
export type MaterialFormat = 'PDF' | 'DOCX' | 'ZIP' | 'MP4' | 'PPTX' | 'PNG'
export type AppRouteName = 'landing' | 'workspace' | 'projects' | 'project-space' | 'tasks' | 'analytics' | 'logs'

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
  assistant: string
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
  riskReason: string
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
  format: MaterialFormat
  deadline: string
  status: MaterialStatus
  progress: number
  assignee: string
  feedbackCount: number
  latestFile: string
  riskHint: string
}

export interface Submission {
  id: string
  materialId: string
  fileName: string
  fileUrl: string
  fileSize: number
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
  targetType: 'project' | 'material' | 'submission' | 'feedback' | 'notification' | 'system'
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

export interface SearchResult {
  id: string
  type: 'course' | 'project' | 'material' | 'submission' | 'log'
  title: string
  description: string
  targetRoute: string
}
