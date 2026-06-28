<script setup lang="ts">
import { onMounted } from 'vue'
import { useRoute, useRouter, RouterView } from 'vue-router'
import { getApiToken } from '../../services/courseopsApi'
import { useCourseOpsStore } from '../../stores/useCourseOpsStore'
import ErrorState from '../common/ErrorState.vue'
import SkeletonCard from '../common/SkeletonCard.vue'
import VisualShaderBackground from '../visuals/VisualShaderBackground.vue'
import SideNav from './SideNav.vue'
import TopNav from './TopNav.vue'

const store = useCourseOpsStore()
const router = useRouter()
const route = useRoute()

onMounted(() => {
  if (!getApiToken() && !route.query.preview) {
    void router.replace('/login')
    return
  }
  if (!store.users.length || !store.projects.length || !store.materials.length) void store.loadInitialData()
})
</script>

<template>
  <main class="app-shell">
    <VisualShaderBackground variant="aurora" />
    <SideNav />
    <section class="app-workspace">
      <TopNav />
      <section class="app-content">
        <section class="runtime-status-bar" v-if="store.apiStatus.usingMock || store.errorMessage">
          <strong>当前状态</strong>
          <span v-if="store.errorMessage">{{ store.errorMessage }}</span>
          <span v-else>后端未连通，页面正在使用本地演示数据。</span>
          <em>{{ store.apiStatus.baseUrl }}</em>
        </section>
        <ErrorState v-if="store.errorMessage && !store.users.length" :message="store.errorMessage" @retry="store.loadInitialData" />
        <section v-else-if="store.isLoading && !store.users.length" class="loading-panel" aria-live="polite">
          <div class="loading-copy">
            <span>CourseOps is preparing</span>
            <h2>正在加载课程工作台</h2>
            <p>正在连接后端或切换到本地演示数据，请稍等几秒。</p>
          </div>
          <div class="loading-grid">
            <SkeletonCard v-for="index in 6" :key="index" />
          </div>
        </section>
        <RouterView v-else />
      </section>
    </section>
  </main>
</template>
