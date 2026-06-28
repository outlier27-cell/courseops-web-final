<script setup lang="ts">
import { ArrowRight, Clock3, Sparkles } from 'lucide-vue-next'
import { computed, onMounted, onUnmounted, ref, watch } from 'vue'
import type { SearchResult } from '../../domain/types'

const props = defineProps<{ open: boolean; query: string; results: SearchResult[] }>()
const emit = defineEmits<{ close: []; openResult: [route: string] }>()

const activeIndex = ref(0)
const canCloseFromBackdrop = ref(false)

const typeLabels: Record<string, string> = {
  course: '课程',
  project: '项目',
  material: '材料',
  submission: '提交记录',
  log: '日志',
}

const quickActions = [
  { title: '打开材料流转轨道', description: '继续处理上传、审核和状态变更', route: '/app/tasks' },
  { title: '查看高风险项目', description: '定位临期项目和材料缺口', route: '/app/projects' },
  { title: '进入运营洞察', description: '查看趋势、风险和建议动作', route: '/app/analytics' },
]

const recentItems = [
  { title: 'Web 应用开发 · 期末项目', description: '项目空间 / 材料清单 / 反馈', route: '/app/projects/p-web-final' },
  { title: '系统说明书初稿', description: '材料详情 / 上传记录 / 状态历史', route: '/app/tasks?materialId=m-report' },
]

const grouped = computed(() => {
  const groups: Record<string, SearchResult[]> = {}
  props.results.forEach((result) => {
    groups[result.type] ??= []
    groups[result.type].push(result)
  })
  return groups
})

function resultIndex(result: SearchResult) {
  return props.results.findIndex((item) => item.id === result.id)
}

function openActiveResult() {
  const target = props.results[activeIndex.value]
  if (target) emit('openResult', target.targetRoute)
}

function breadcrumb(route: string) {
  if (route.includes('/tasks')) return '工作台 / 材料流转'
  if (route.includes('/projects/')) return '项目中心 / 项目空间'
  if (route.includes('/projects')) return '项目中心'
  if (route.includes('/analytics')) return '运营洞察'
  if (route.includes('/logs')) return '审计日志'
  return 'CourseOps'
}

function onKeydown(event: KeyboardEvent) {
  if (!props.open) return
  if (event.key === 'Escape') emit('close')
  if (event.key === 'ArrowDown') {
    event.preventDefault()
    activeIndex.value = Math.min(props.results.length - 1, activeIndex.value + 1)
  }
  if (event.key === 'ArrowUp') {
    event.preventDefault()
    activeIndex.value = Math.max(0, activeIndex.value - 1)
  }
  if (event.key === 'Enter') {
    event.preventDefault()
    openActiveResult()
  }
}

watch(
  () => [props.query, props.open],
  () => {
    activeIndex.value = 0
    canCloseFromBackdrop.value = false
    if (props.open) {
      window.setTimeout(() => {
        canCloseFromBackdrop.value = true
      }, 180)
    }
  },
)

function closeFromBackdrop() {
  if (canCloseFromBackdrop.value) emit('close')
}

onMounted(() => window.addEventListener('keydown', onKeydown))
onUnmounted(() => window.removeEventListener('keydown', onKeydown))
</script>

<template>
  <Teleport to="body">
    <div v-if="open" class="search-overlay" @click.self="closeFromBackdrop">
      <section class="search-command-palette" role="dialog" aria-label="全局搜索">
        <header>
          <span>Command Center</span>
          <strong>{{ query || '搜索课程、项目、材料、负责人、文件名' }}</strong>
          <small>Esc 关闭 · ↑↓ 选择 · Enter 打开</small>
        </header>

        <div v-if="results.length" class="search-groups">
          <section v-for="(items, type) in grouped" :key="type">
            <h3>{{ typeLabels[String(type)] ?? type }}</h3>
            <button
              v-for="item in items"
              :key="item.id"
              :class="{ active: resultIndex(item) === activeIndex }"
              @mouseenter="activeIndex = resultIndex(item)"
              @click="$emit('openResult', item.targetRoute)"
            >
              <span>{{ typeLabels[item.type] ?? item.type }}</span>
              <strong>{{ item.title }}</strong>
              <small>{{ breadcrumb(item.targetRoute) }} · {{ item.description }}</small>
              <ArrowRight :size="15" />
            </button>
          </section>
        </div>

        <div v-else-if="!query" class="command-suggestions">
          <section>
            <h3><Clock3 :size="15" /> 最近访问</h3>
            <button v-for="item in recentItems" :key="item.route" @click="$emit('openResult', item.route)">
              <strong>{{ item.title }}</strong>
              <small>{{ item.description }}</small>
              <span>{{ breadcrumb(item.route) }}</span>
            </button>
          </section>
          <section>
            <h3><Sparkles :size="15" /> 推荐操作</h3>
            <button v-for="action in quickActions" :key="action.route" @click="$emit('openResult', action.route)">
              <strong>{{ action.title }}</strong>
              <small>{{ action.description }}</small>
              <span>{{ breadcrumb(action.route) }}</span>
            </button>
          </section>
        </div>

        <div v-else class="empty-state compact">
          <div class="empty-orb"></div>
          <h3>没有找到结果</h3>
          <p>试试搜索课程名、材料名、负责人、团队成员或最新文件名。</p>
        </div>
      </section>
    </div>
  </Teleport>
</template>
