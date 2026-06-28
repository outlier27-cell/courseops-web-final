import { createRouter, createWebHashHistory, createWebHistory } from 'vue-router'
import { getApiToken } from '../services/courseopsApi'

const PublicLanding = () => import('../components/landing/PublicLanding.vue')
const LoginPage = () => import('../components/landing/LoginPage.vue')
const AppShell = () => import('../components/shell/AppShell.vue')
const WorkspaceHome = () => import('../components/dashboard/WorkspaceHome.vue')
const ProjectCenter = () => import('../components/projects/ProjectCenter.vue')
const ProjectSpace = () => import('../components/projects/ProjectSpace.vue')
const MaterialWorkflowCenter = () => import('../components/tasks/MaterialWorkflowCenter.vue')
const OperationalInsights = () => import('../components/analytics/OperationalInsights.vue')
const ActivityTimeline = () => import('../components/logs/ActivityTimeline.vue')

export const router = createRouter({
  history: import.meta.env.BASE_URL === '/courseops-web-final/' ? createWebHashHistory(import.meta.env.BASE_URL) : createWebHistory(),
  routes: [
    { path: '/', name: 'landing', component: PublicLanding },
    { path: '/login', name: 'login', component: LoginPage },
    {
      path: '/app',
      component: AppShell,
      meta: { requiresAuth: true },
      children: [
        { path: '', name: 'workspace', component: WorkspaceHome },
        { path: 'projects', name: 'projects', component: ProjectCenter },
        { path: 'projects/:id', name: 'project-space', component: ProjectSpace },
        { path: 'tasks', name: 'tasks', component: MaterialWorkflowCenter },
        { path: 'analytics', name: 'analytics', component: OperationalInsights },
        { path: 'logs', name: 'logs', component: ActivityTimeline },
      ],
    },
  ],
})

router.beforeEach((to) => {
  const token = getApiToken()
  if (to.query.preview) return true
  if (to.meta.requiresAuth && !token) return { name: 'login', query: { redirect: to.fullPath } }
  if (to.name === 'login' && token) return { path: '/app' }
  return true
})
