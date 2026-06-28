<script setup lang="ts">
import { BarChart3, ClipboardCheck, Database, FileText, LayoutDashboard } from 'lucide-vue-next'
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const items = [
  { path: '/app', label: '工作台', hint: '今日优先级', icon: LayoutDashboard, exact: true },
  { path: '/app/projects', label: '项目', hint: '课程空间', icon: FileText },
  { path: '/app/tasks', label: '材料', hint: '流转轨道', icon: ClipboardCheck },
  { path: '/app/analytics', label: '洞察', hint: '运营建议', icon: BarChart3 },
  { path: '/app/logs', label: '审计', hint: '动态留痕', icon: Database },
]

const currentPath = computed(() => route.path)
</script>

<template>
  <aside class="side-nav">
    <button class="brand" @click="router.push('/login')">
      <span class="brand-mark">C</span>
      <span>
        <strong>CourseOps</strong>
        <small>Course operations system</small>
      </span>
    </button>
    <nav>
      <button
        v-for="item in items"
        :key="item.path"
        :class="{ active: item.exact ? currentPath === item.path : currentPath.startsWith(item.path) }"
        @click="router.push(item.path)"
      >
        <component :is="item.icon" :size="18" />
        <span>
          <strong>{{ item.label }}</strong>
          <small>{{ item.hint }}</small>
        </span>
      </button>
    </nav>
  </aside>
</template>
