import type { AppRouteName, MaterialStatus, RiskLevel, UserRole } from './types'

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

export const routeLabels: Record<AppRouteName, string> = {
  landing: '首页',
  workspace: '我的工作台',
  projects: '课程项目',
  'project-space': '项目空间',
  tasks: '材料流转',
  analytics: '运营洞察',
  logs: '动态记录',
}
