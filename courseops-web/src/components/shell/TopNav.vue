<script setup lang="ts">
import { Bell, DatabaseZap, LogOut, RefreshCw, Search, UserRound } from 'lucide-vue-next'
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import type { UserRole } from '../../domain/types'
import { clearApiToken } from '../../services/courseopsApi'
import { useCourseOpsStore } from '../../stores/useCourseOpsStore'
import SuccessMotion from '../visuals/SuccessMotion.vue'
import NotificationPopover from './NotificationPopover.vue'
import RoleSwitcher from './RoleSwitcher.vue'
import SearchCommandPalette from './SearchCommandPalette.vue'

const store = useCourseOpsStore()
const router = useRouter()
const searchOpen = ref(false)
const notificationOpen = ref(false)
const searchShell = ref<HTMLDivElement | null>(null)
const searchInput = ref<HTMLInputElement | null>(null)
const successMessage = ref('')
let successTimer = 0

const apiLabel = computed(() => (store.apiStatus.usingMock ? '演示数据' : '实时后端'))

function showSuccess(message: string) {
  successMessage.value = message
  window.clearTimeout(successTimer)
  successTimer = window.setTimeout(() => {
    successMessage.value = ''
  }, 1600)
}

function openSearch() {
  searchOpen.value = true
  requestAnimationFrame(() => searchInput.value?.focus())
}

function openResult(path: string) {
  searchOpen.value = false
  void router.push(path)
}

function openNotification(targetId?: string) {
  notificationOpen.value = false
  if (!targetId) return
  if (targetId.startsWith('p-')) void router.push(`/app/projects/${targetId}`)
  else void router.push(`/app/tasks?materialId=${targetId}`)
}

function switchRole(role: UserRole) {
  store.switchRole(role)
  showSuccess(role === 'student' ? '已切换到学生视角' : role === 'teacher' ? '已切换到教师视角' : '已切换到管理员视角')
}

function markNotificationRead(id: string) {
  store.markNotificationRead(id)
  showSuccess('通知已标记为已读')
}

function resetDemoState() {
  store.resetDemoState()
  showSuccess('演示数据已重置')
  void store.loadInitialData()
}

async function logout() {
  clearApiToken()
  store.resetDemoState()
  await router.replace('/login')
}

function onGlobalKeydown(event: KeyboardEvent) {
  if ((event.metaKey || event.ctrlKey) && event.key.toLowerCase() === 'k') {
    event.preventDefault()
    openSearch()
  }
}

onMounted(() => {
  window.addEventListener('keydown', onGlobalKeydown)
  searchShell.value?.addEventListener('pointerdown', openSearch, { capture: true })
  searchShell.value?.addEventListener('focusin', openSearch, { capture: true })
})
onUnmounted(() => {
  window.removeEventListener('keydown', onGlobalKeydown)
  searchShell.value?.removeEventListener('pointerdown', openSearch, { capture: true })
  searchShell.value?.removeEventListener('focusin', openSearch, { capture: true })
})
</script>

<template>
  <header class="top-nav">
    <SuccessMotion :active="!!successMessage" :label="successMessage" />
    <div ref="searchShell" class="global-search" role="button" tabindex="0" aria-label="全局搜索" @click="openSearch" @keydown.enter.prevent="openSearch">
      <Search :size="17" />
      <input
        ref="searchInput"
        v-model="store.searchQuery"
        placeholder="搜索课程、项目、材料、负责人、文件名..."
        @click.stop="openSearch"
        @focus="searchOpen = true"
        @input="searchOpen = true"
      />
      <kbd>Ctrl K</kbd>
    </div>
    <div class="top-nav-actions">
      <span class="api-chip" :class="{ muted: store.apiStatus.usingMock }" :title="store.apiStatus.lastError || store.apiStatus.baseUrl">
        <DatabaseZap :size="15" />
        {{ apiLabel }}
      </span>
      <span class="semester-chip">2026 春季学期</span>
      <button class="notification-button" @click="notificationOpen = true">
        <Bell :size="16" />
        通知
        <b v-if="store.unreadNotifications.length">{{ store.unreadNotifications.length }}</b>
      </button>
      <RoleSwitcher v-if="store.currentUser" :role="store.currentRole" @switch-role="switchRole" />
      <span v-if="store.currentUser" class="user-chip"><UserRound :size="16" /> {{ store.currentUser.name }}</span>
      <button class="utility-button" type="button" @click="resetDemoState">
        <RefreshCw :size="15" />
        重置演示
      </button>
      <button class="utility-button danger" type="button" @click="logout">
        <LogOut :size="15" />
        退出
      </button>
    </div>
    <SearchCommandPalette :open="searchOpen" :query="store.searchQuery" :results="store.searchResults" @close="searchOpen = false" @open-result="openResult" />
    <NotificationPopover
      :open="notificationOpen"
      :notifications="store.currentUserNotifications"
      @close="notificationOpen = false"
      @mark-read="markNotificationRead"
      @open-notification="(item) => openNotification(item.relatedTargetId)"
    />
  </header>
</template>
