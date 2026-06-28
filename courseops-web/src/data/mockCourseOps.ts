import type {
  AnalyticsSnapshot,
  Course,
  CourseProject,
  MaterialHistory,
  MaterialItem,
  NotificationItem,
  OperationLog,
  Submission,
  TeacherFeedback,
  TeamMember,
  User,
} from '../domain/types'

export const mockUsers: User[] = [
  { id: 'u-student-001', name: '张同学', role: 'student', department: '信息学院', avatar: 'Z' },
  { id: 'u-teacher-001', name: '张老师', role: 'teacher', department: '信息学院', avatar: 'T' },
  { id: 'u-admin-001', name: '教务管理员', role: 'admin', department: '教务处', avatar: 'A' },
]

export const mockCourses: Course[] = [
  { id: 'c-web', name: 'Web 应用开发', teacher: '张老师', assistant: '陈助教', semester: '2026 春季学期', studentsCount: 86 },
  { id: 'c-db', name: '数据库原理', teacher: '李老师', assistant: '周助教', semester: '2026 春季学期', studentsCount: 72 },
  { id: 'c-writing', name: '学术写作', teacher: '王老师', assistant: '刘助教', semester: '2026 春季学期', studentsCount: 54 },
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
    riskReason: '说明书正文处于需修改状态，源码压缩包尚未提交。',
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
    riskReason: 'ER 图未提交，距离截止不足 24 小时。',
    recentFeedback: 'ER 图缺少关系说明，请今天补齐。',
  },
  {
    id: 'p-writing',
    courseId: 'c-writing',
    title: '课程论文阶段稿',
    description: '完成二稿修改、引用检查和最终提交记录。',
    owner: '李同学',
    members: ['李同学', '张同学'],
    deadline: '2026-06-28',
    phase: '教师反馈处理',
    progress: 83,
    teamProgress: 79,
    riskLevel: 'low',
    healthScore: 88,
    riskReason: '当前无明显风险。',
    recentFeedback: '引用格式已基本通过，等待最终确认。',
  },
]

export const mockMembers: TeamMember[] = [
  { id: 'tm-1', projectId: 'p-web-final', name: '张同学', role: 'leader', avatar: 'Z', responsibility: '前端与说明书正文', progress: 74, hasOverdueRisk: false },
  { id: 'tm-2', projectId: 'p-web-final', name: '李同学', role: 'member', avatar: 'L', responsibility: '系统截图与测试记录', progress: 90, hasOverdueRisk: false },
  { id: 'tm-3', projectId: 'p-web-final', name: '王同学', role: 'member', avatar: 'W', responsibility: '数据库设计图', progress: 42, hasOverdueRisk: true },
  { id: 'tm-4', projectId: 'p-db-er', name: '张同学', role: 'leader', avatar: 'Z', responsibility: 'ER 图与实验报告', progress: 46, hasOverdueRisk: true },
  { id: 'tm-5', projectId: 'p-writing', name: '李同学', role: 'leader', avatar: 'L', responsibility: '论文正文', progress: 86, hasOverdueRisk: false },
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
    riskHint: '教师反馈未处理',
  },
  {
    id: 'm-screenshots',
    projectId: 'p-web-final',
    title: '系统截图',
    description: '登录页、工作台、项目中心、材料看板、数据洞察截图。',
    required: true,
    format: 'ZIP',
    deadline: '2026-06-25',
    status: 'submitted',
    progress: 90,
    assignee: '李同学',
    feedbackCount: 0,
    latestFile: 'courseops-screenshots.zip',
    riskHint: '等待教师确认',
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
    riskHint: '尚未上传文件',
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
    riskHint: '距离截止不足 24 小时',
  },
  {
    id: 'm-paper',
    projectId: 'p-writing',
    title: '论文二稿',
    description: '根据教师反馈修改正文、引用和摘要。',
    required: true,
    format: 'DOCX',
    deadline: '2026-06-28',
    status: 'approved',
    progress: 100,
    assignee: '李同学',
    feedbackCount: 0,
    latestFile: 'academic-writing-v3.docx',
    riskHint: '已通过',
  },
]

export const mockSubmissions: Submission[] = [
  {
    id: 's-report-v2',
    materialId: 'm-report',
    fileName: 'courseops-manual-v2.docx',
    fileUrl: '#',
    fileSize: 2_430_000,
    submittedBy: '张同学',
    submittedAt: '2026-06-23 10:12',
    status: 'revision_required',
    feedback: '请补充项目空间与材料流转说明。',
  },
  {
    id: 's-screenshots',
    materialId: 'm-screenshots',
    fileName: 'courseops-screenshots.zip',
    fileUrl: '#',
    fileSize: 8_210_000,
    submittedBy: '李同学',
    submittedAt: '2026-06-23 09:48',
    status: 'submitted',
    feedback: '等待教师确认。',
  },
]

export const mockFeedback: TeacherFeedback[] = [
  { id: 'fb-1', materialId: 'm-report', projectId: 'p-web-final', teacherName: '张老师', content: '说明书需要补充项目空间与材料流转的操作截图。', status: 'unresolved', createdAt: '2026-06-23 10:20' },
  { id: 'fb-2', materialId: 'm-source', projectId: 'p-web-final', teacherName: '张老师', content: '源码压缩包需要包含运行说明和数据库脚本。', status: 'unresolved', createdAt: '2026-06-23 09:36' },
  { id: 'fb-3', materialId: 'm-paper', projectId: 'p-writing', teacherName: '王老师', content: '引用格式已通过，可以准备最终提交。', status: 'resolved', createdAt: '2026-06-22 16:12' },
]

export const mockHistories: MaterialHistory[] = [
  { id: 'mh-1', materialId: 'm-report', actor: '张同学', action: '上传文件', fromStatus: 'preparing', toStatus: 'submitted', fileName: 'courseops-manual-v2.docx', createdAt: '2026-06-23 10:12' },
  { id: 'mh-2', materialId: 'm-report', actor: '张老师', action: '标记需修改', fromStatus: 'submitted', toStatus: 'revision_required', createdAt: '2026-06-23 10:20' },
  { id: 'mh-3', materialId: 'm-screenshots', actor: '李同学', action: '上传文件', fromStatus: 'preparing', toStatus: 'submitted', fileName: 'courseops-screenshots.zip', createdAt: '2026-06-23 09:48' },
]

export const mockLogs: OperationLog[] = [
  { id: 'mongo-8301', actor: '张老师', actorRole: 'teacher', action: '提交反馈', targetType: 'feedback', targetName: '说明书正文', before: '已提交', after: '需修改', createdAt: '2026-06-23 10:20', source: '项目空间' },
  { id: 'mongo-8297', actor: '张同学', actorRole: 'student', action: '上传文件', targetType: 'submission', targetName: '说明书正文', before: '待提交', after: 'courseops-manual-v2.docx', createdAt: '2026-06-23 10:12', source: '材料流转中心' },
  { id: 'mongo-8288', actor: '系统', actorRole: 'admin', action: '标记项目风险', targetType: 'project', targetName: '数据库实验报告', before: '临近截止', after: '高风险', createdAt: '2026-06-23 09:55', source: '风险引擎' },
]

export const mockNotifications: NotificationItem[] = [
  { id: 'n-1', userId: 'u-student-001', title: '说明书正文需要修改', description: '张老师反馈：请补充项目空间与材料流转说明。', type: 'feedback', relatedTargetId: 'm-report', read: false, createdAt: '2026-06-23 10:20' },
  { id: 'n-2', userId: 'u-student-001', title: '数据库 ER 图即将截止', description: '距离截止不足 24 小时，建议今天完成上传。', type: 'deadline', relatedTargetId: 'm-er', read: false, createdAt: '2026-06-23 09:30' },
  { id: 'n-3', userId: 'u-teacher-001', title: '有 2 份材料待审核', description: 'Web 期末项目新增材料提交，请及时检查。', type: 'submission', relatedTargetId: 'p-web-final', read: false, createdAt: '2026-06-23 10:30' },
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
