<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCourseOpsStore } from '../../stores/useCourseOpsStore'
import AnimatedNumber from '../common/AnimatedNumber.vue'
import ProgressBar from '../common/ProgressBar.vue'
import RiskBadge from '../common/RiskBadge.vue'
import StatusPill from '../common/StatusPill.vue'
import MotionReveal from '../visuals/MotionReveal.vue'

const store = useCourseOpsStore()
const router = useRouter()

const heroTitle = computed(() => {
  if (store.currentRole === 'student') return `${store.currentUser?.name ?? '同学'}，今天有 ${store.studentTodos.length} 项材料需要处理`
  if (store.currentRole === 'teacher') return `${store.currentUser?.name ?? '老师'}，有 ${store.teacherPendingReviews.length} 份材料待审核`
  return '全校课程项目运营总览'
})

const heroDescription = computed(() => {
  if (store.currentRole === 'student') return '优先处理被退回、临期和待提交材料，系统会把最紧急的事项推到前面。'
  if (store.currentRole === 'teacher') return '集中查看学生提交、反馈处理和课程项目风险，减少等待和遗漏。'
  return '查看全校项目健康度、高风险课程和异常操作日志，快速定位运营风险。'
})

const focusMaterials = computed(() => {
  if (store.currentRole === 'teacher') return store.teacherPendingReviews
  if (store.currentRole === 'admin') return store.materials.filter((item) => item.status === 'revision_required')
  return store.studentTodos
})

const focusLabel = computed(() => (store.currentRole === 'teacher' ? '待审核材料' : '待处理材料'))

const priorityHeading = computed(() => {
  if (store.currentRole === 'teacher') return '待审核材料'
  if (store.currentRole === 'admin') return '高风险课程项目'
  return '今日待办'
})

const suggestionTitle = computed(() => {
  if (store.currentRole === 'student') return '说明书正文需要优先修改'
  if (store.currentRole === 'teacher') return '有材料待审核'
  return '数据库原理存在高风险项目'
})

const suggestionCopy = computed(() => {
  if (store.currentRole === 'student') return '先处理教师反馈，再重新上传最新版。'
  if (store.currentRole === 'teacher') return '尽快处理已提交材料，避免学生等待。'
  return '先关注高风险课程，再查看对应材料缺口。'
})
</script>

<template>
  <MotionReveal>
    <section class="workspace-home cockpit-view">
      <header class="cockpit-hero" data-reveal>
        <div>
          <span class="eyebrow">Intelligent Cockpit</span>
          <h1>{{ heroTitle }}</h1>
          <p>{{ heroDescription }}</p>
        </div>
        <button class="primary-action" @click="router.push('/app/tasks')">进入材料流转</button>
      </header>

      <section class="role-metrics" data-reveal>
        <article><AnimatedNumber :value="focusMaterials.length" /><span>{{ focusLabel }}</span></article>
        <article><AnimatedNumber :value="store.adminRiskProjects.length" /><span>风险项目</span></article>
        <article><AnimatedNumber :value="store.unreadNotifications.length" /><span>未读通知</span></article>
        <article><AnimatedNumber :value="store.analytics?.materialCompletionRate ?? 0" suffix="%" /><span>材料完成率</span></article>
      </section>

      <section class="workspace-layout" data-reveal>
        <div class="workspace-main">
          <div class="section-heading-row">
            <span class="eyebrow">Priority Stack</span>
            <h2>{{ priorityHeading }}</h2>
          </div>
          <div class="todo-grid priority-stack">
            <button
              v-for="material in focusMaterials.slice(0, 6)"
              :key="material.id"
              class="todo-card interactive-lift"
              @click="router.push(`/app/tasks?materialId=${material.id}`)"
            >
              <span class="priority-index">P{{ focusMaterials.findIndex((item) => item.id === material.id) + 1 }}</span>
              <strong>{{ material.title }}</strong>
              <span>{{ material.assignee }} · {{ material.deadline }} · {{ material.riskHint }}</span>
              <StatusPill :status="material.status" />
            </button>
          </div>

          <div class="section-heading-row compact">
            <span class="eyebrow">Project Pulse</span>
            <h2>我的课程项目</h2>
          </div>
          <div class="workspace-projects">
            <button
              v-for="project in store.projects"
              :key="project.id"
              class="workspace-project-card interactive-lift"
              @click="router.push(`/app/projects/${project.id}`)"
            >
              <div>
                <strong>{{ project.title }}</strong>
                <span>{{ project.phase }} · {{ project.deadline }}</span>
              </div>
              <ProgressBar :value="project.progress" />
              <RiskBadge :risk="project.riskLevel" />
            </button>
          </div>
        </div>

        <aside class="workspace-aside">
          <article class="smart-suggestion interactive-lift">
            <span>智能提醒</span>
            <h3>{{ suggestionTitle }}</h3>
            <p>{{ suggestionCopy }}</p>
            <button @click="router.push(store.currentRole === 'admin' ? '/app/analytics' : '/app/tasks')">立即处理</button>
          </article>
        </aside>
      </section>
    </section>
  </MotionReveal>
</template>
