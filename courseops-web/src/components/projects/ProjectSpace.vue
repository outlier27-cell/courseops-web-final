<script setup lang="ts">
import { computed, onMounted, ref, watchEffect } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCourseOpsStore } from '../../stores/useCourseOpsStore'
import ProgressBar from '../common/ProgressBar.vue'
import ProgressRing from '../common/ProgressRing.vue'
import RiskBadge from '../common/RiskBadge.vue'
import StatusPill from '../common/StatusPill.vue'
import TeamAvatarStack from './TeamAvatarStack.vue'

const route = useRoute()
const router = useRouter()
const store = useCourseOpsStore()
const tab = ref('overview')
const tabs = ['overview', 'materials', 'members', 'submissions', 'feedback', 'activity']
const labels: Record<string, string> = {
  overview: '概览',
  materials: '材料',
  members: '成员',
  submissions: '提交',
  feedback: '反馈',
  activity: '动态',
}

watchEffect(() => {
  const id = String(route.params.id || '')
  if (id) store.selectProject(id)
})

const project = computed(() => store.selectedProject)
const course = computed(() => store.courses.find((item) => item.id === project.value?.courseId))
const projectHistories = computed(() => store.histories.filter((item) => store.selectedProjectMaterials.some((material) => material.id === item.materialId)))

onMounted(() => {
  if (!store.users.length || !store.projects.length) void store.loadInitialData()
})
</script>

<template>
  <section v-if="project" class="project-space motion-rise">
    <header class="project-space-hero">
      <div>
        <span class="eyebrow">{{ course?.name ?? 'Project Space' }}</span>
        <h1>{{ project.title }}</h1>
        <p>{{ project.description }}</p>
        <div class="project-hero-meta">
          <RiskBadge :risk="project.riskLevel" />
          <span>{{ project.phase }}</span>
          <span>{{ project.deadline }} 截止</span>
        </div>
      </div>
      <div class="project-hero-actions">
        <button class="secondary" @click="router.push('/app/projects')">返回项目中心</button>
        <button class="primary" @click="router.push(`/app/tasks?materialId=${store.selectedProjectMaterials[0]?.id ?? ''}`)">进入材料流转</button>
      </div>
    </header>

    <section class="project-command-strip">
      <article class="project-health-core">
        <div class="health-orb-shell">
          <span></span>
          <ProgressRing :value="project.healthScore" label="健康度" />
        </div>
        <strong>项目健康核心</strong>
      </article>
      <article>
        <span>风险原因</span>
        <strong>{{ project.riskReason }}</strong>
      </article>
      <article>
        <span>最近反馈</span>
        <strong>{{ project.recentFeedback }}</strong>
      </article>
      <article>
        <span>团队</span>
        <TeamAvatarStack :members="store.selectedProjectMembers" />
      </article>
    </section>

    <div class="segmented-control project-tabs">
      <button v-for="item in tabs" :key="item" :class="{ active: tab === item }" @click="tab = item">{{ labels[item] }}</button>
    </div>

    <section class="project-space-layout">
      <article class="project-workspace-panel tab-stage" :key="tab">
        <template v-if="tab === 'overview'">
          <h2>项目运行概览</h2>
          <div class="overview-grid">
            <section>
              <span>我的完成度</span>
              <ProgressBar :value="project.progress" />
              <strong>{{ project.progress }}%</strong>
            </section>
            <section>
              <span>团队完成度</span>
              <ProgressBar :value="project.teamProgress" />
              <strong>{{ project.teamProgress }}%</strong>
            </section>
          </div>
          <div class="project-material-preview spacious">
            <strong>关键材料</strong>
            <button v-for="material in store.selectedProjectMaterials" :key="material.id" @click="router.push(`/app/tasks?materialId=${material.id}`)">
              <span>{{ material.title }}</span>
              <StatusPill :status="material.status" />
            </button>
          </div>
        </template>

        <template v-if="tab === 'materials'">
          <h2>材料清单</h2>
          <div class="material-checklist refined">
            <button v-for="material in store.selectedProjectMaterials" :key="material.id" @click="router.push(`/app/tasks?materialId=${material.id}`)">
              <div>
                <strong>{{ material.title }}</strong>
                <span>{{ material.required ? '必交' : '选交' }} · {{ material.format }} · {{ material.deadline }} · {{ material.latestFile || '暂无文件' }}</span>
              </div>
              <StatusPill :status="material.status" />
            </button>
          </div>
        </template>

        <template v-if="tab === 'members'">
          <h2>团队成员</h2>
          <div class="member-grid refined">
            <section v-for="member in store.selectedProjectMembers" :key="member.id">
              <strong>{{ member.name }}</strong>
              <span>{{ member.responsibility }}</span>
              <ProgressBar :value="member.progress" />
              <em>{{ member.hasOverdueRisk ? '存在逾期风险' : '节奏正常' }}</em>
            </section>
          </div>
        </template>

        <template v-if="tab === 'submissions'">
          <h2>提交记录</h2>
          <div class="submission-records refined">
            <section v-for="submission in store.selectedProjectSubmissions" :key="submission.id">
              <strong>{{ submission.fileName }}</strong>
              <span>{{ submission.submittedBy }} · {{ submission.submittedAt }}</span>
              <p>{{ submission.feedback }}</p>
            </section>
          </div>
        </template>

        <template v-if="tab === 'feedback'">
          <h2>教师反馈</h2>
          <div class="feedback-list refined">
            <section v-for="item in store.selectedProjectFeedback" :key="item.id">
              <strong>{{ item.teacherName }}</strong>
              <p>{{ item.content }}</p>
              <button @click="store.resolveFeedback(item.id)">标记已处理</button>
            </section>
          </div>
        </template>

        <template v-if="tab === 'activity'">
          <h2>项目动态</h2>
          <div class="feedback-list refined">
            <section v-for="history in projectHistories" :key="history.id">
              <strong>{{ history.action }}</strong>
              <span>{{ history.actor }} · {{ history.createdAt }}</span>
            </section>
          </div>
        </template>
      </article>

      <aside class="project-activity-rail">
        <h3>最近动态</h3>
        <article v-for="history in projectHistories.slice(0, 4)" :key="history.id">
          <strong>{{ history.action }}</strong>
          <span>{{ history.actor }} · {{ history.createdAt }}</span>
        </article>
      </aside>
    </section>
  </section>
  <section v-else class="empty-state">
    <div class="empty-orb"></div>
    <h3>项目空间加载中</h3>
    <p>正在读取课程项目、材料、成员和反馈数据。</p>
  </section>
</template>
