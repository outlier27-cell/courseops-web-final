<script setup lang="ts">
import { computed, ref } from 'vue'
import { useCourseOpsStore } from '../../stores/useCourseOpsStore'
import LogFilterBar from './LogFilterBar.vue'
import TimelineItem from './TimelineItem.vue'

const store = useCourseOpsStore()
const searchText = ref('')
const actionType = ref('all')
const roleFilter = ref('all')

const visibleLogs = computed(() => store.logs.filter((log) => {
  const needle = searchText.value.trim().toLowerCase()
  const matchesSearch = !needle || `${log.actor}${log.targetName}${log.source}`.toLowerCase().includes(needle)
  const matchesType = actionType.value === 'all' || log.action === actionType.value
  const matchesRole = roleFilter.value === 'all' || log.actorRole === roleFilter.value
  return matchesSearch && matchesType && matchesRole
}))

const groupedLogs = computed(() => {
  const groups = [
    { label: 'Today', description: '今天发生的上传、审核、反馈和通知事件。', logs: visibleLogs.value.slice(0, 5) },
    { label: 'Yesterday', description: '昨天仍在影响当前项目节奏的关键动态。', logs: visibleLogs.value.slice(5, 10) },
    { label: 'Earlier', description: '更早的审计记录，用于追踪材料状态变化。', logs: visibleLogs.value.slice(10) },
  ]
  return groups.filter((group) => group.logs.length)
})

const pageTitle = computed(() => {
  if (store.currentRole === 'admin') return '审计日志'
  if (store.currentRole === 'teacher') return '课程动态'
  return '动态记录'
})

const pageDescription = computed(() => {
  if (store.currentRole === 'admin') return '可检索、可展开、可追踪的课程项目操作审计记录。'
  if (store.currentRole === 'teacher') return '查看学生提交、材料审核、反馈处理和项目风险变化。'
  return '查看与你相关的提交、反馈、截止提醒和状态变化。'
})
</script>

<template>
  <section class="activity-timeline motion-rise">
    <header class="timeline-hero">
      <div class="audit-orbit" aria-hidden="true">
        <span></span>
        <span></span>
        <span></span>
      </div>
      <div>
        <span class="eyebrow">{{ store.currentRole === 'admin' ? 'Audit Timeline' : 'Activity Timeline' }}</span>
        <h1>{{ pageTitle }}</h1>
        <p>{{ pageDescription }}</p>
      </div>
      <div class="timeline-hero-stats">
        <article><strong>{{ visibleLogs.length }}</strong><span>当前记录</span></article>
        <article><strong>{{ store.logs.filter((log) => log.actorRole === 'teacher').length }}</strong><span>教师操作</span></article>
        <article><strong>{{ store.logs.filter((log) => log.action.includes('上传')).length }}</strong><span>上传事件</span></article>
      </div>
    </header>

    <LogFilterBar v-model:search-text="searchText" v-model:action-type="actionType" v-model:role-filter="roleFilter" :role="store.currentRole" />

    <section v-for="group in groupedLogs" :key="group.label" class="audit-timeline-shell">
      <aside>
        <strong>{{ group.label }}</strong>
        <span>{{ group.description }}</span>
      </aside>
      <div class="timeline-list">
        <TimelineItem v-for="log in group.logs" :key="log.id" :log="log" :role="store.currentRole" />
      </div>
    </section>
  </section>
</template>
