<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { clearApiToken, login, setApiToken } from '../../services/courseopsApi'
import { useCourseOpsStore } from '../../stores/useCourseOpsStore'

const route = useRoute()
const router = useRouter()
const store = useCourseOpsStore()

const userId = ref('u-student-001')
const password = ref('123456')
const errorMessage = ref('')
const isSubmitting = ref(false)

const redirectTo = computed(() => String(route.query.redirect || '/app'))

async function submit() {
  isSubmitting.value = true
  errorMessage.value = ''
  try {
    const result = await login(userId.value, password.value)
    setApiToken(result.token)
    store.replaceCurrentUser(result.user.id)
    await store.loadInitialData()
    await router.replace(redirectTo.value)
  } catch (error) {
    clearApiToken()
    errorMessage.value = error instanceof Error ? error.message : '登录失败'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <main class="login-page">
    <section class="login-story-panel" aria-label="CourseOps 功能说明">
      <p class="eyebrow">CourseOps Delivery Console</p>
      <h2>把期末项目的提交、反馈和验收放进同一条工作流。</h2>
      <p>
        学生提交材料，教师审核反馈，系统记录日志和项目风险。登录后可以进入项目、材料、分析和审计页面完成完整演示。
      </p>
      <div class="login-proof-grid">
        <article><strong>3</strong><span>演示角色</span></article>
        <article><strong>8+</strong><span>核心接口</span></article>
        <article><strong>10</strong><span>说明书截图</span></article>
      </div>
    </section>

    <section class="login-panel">
      <p class="eyebrow">CourseOps Login</p>
      <h1>进入课程工作台</h1>
      <p class="login-copy">使用真实后端登录，进入项目、材料、分析和日志页面。</p>

      <form class="login-form" @submit.prevent="submit">
        <label>
          <span>账号</span>
          <input v-model="userId" autocomplete="username" placeholder="u-student-001" />
        </label>
        <label>
          <span>密码</span>
          <input v-model="password" type="password" autocomplete="current-password" placeholder="123456" />
        </label>
        <p v-if="errorMessage" class="login-error">{{ errorMessage }}</p>
        <button class="primary" type="submit" :disabled="isSubmitting">
          {{ isSubmitting ? '登录中...' : '登录' }}
        </button>
      </form>

      <div class="login-hints">
        <span>学生：u-student-001 / 123456</span>
        <span>教师：u-teacher-001 / 123456</span>
        <span>管理员：u-admin-001 / 123456</span>
      </div>
    </section>
  </main>
</template>
