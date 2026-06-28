<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCourseOpsStore } from '../../stores/useCourseOpsStore'
import ProgressBar from '../common/ProgressBar.vue'
import ProgressRing from '../common/ProgressRing.vue'
import RiskBadge from '../common/RiskBadge.vue'
import SegmentedControl from '../common/SegmentedControl.vue'
import StatusPill from '../common/StatusPill.vue'
import TeamAvatarStack from './TeamAvatarStack.vue'

const store = useCourseOpsStore()
const router = useRouter()
const filter = ref('all')
const options = [
  { label: '全部项目', value: 'all' },
  { label: '我的项目', value: 'mine' },
  { label: '临近截止', value: 'due' },
  { label: '材料缺口', value: 'gap' },
  { label: '高完成度', value: 'done' },
]

const visibleProjects = computed(() => {
  if (filter.value === 'due') return store.projects.filter((project) => project.riskLevel !== 'low')
  if (filter.value === 'done') return store.projects.filter((project) => project.progress >= 85)
  if (filter.value === 'gap') return store.projects.filter((project) => store.materials.some((material) => material.projectId === project.id && material.status !== 'approved'))
  return store.projects
})

function courseName(courseId: string) {
  return store.courses.find((course) => course.id === courseId)?.name ?? '课程项目'
}

function projectMaterials(projectId: string) {
  return store.materials.filter((material) => material.projectId === projectId).slice(0, 3)
}
</script>

<template>
  <section class="project-center motion-rise">
    <header class="project-plaza-hero">
      <div>
        <span class="eyebrow">Course Project Plaza</span>
        <h1>课程项目中心</h1>
        <p>集中查看课程、团队、材料要求、截止风险和教师反馈，按项目快速定位问题。</p>
      </div>
      <div class="plaza-snapshot">
        <article><strong>{{ store.projects.length }}</strong><span>活跃项目</span></article>
        <article><strong>{{ store.projects.filter((project) => project.riskLevel !== 'low').length }}</strong><span>风险项目</span></article>
        <article><strong>{{ store.materials.filter((material) => material.status !== 'approved').length }}</strong><span>待处理材料</span></article>
      </div>
    </header>

    <div class="project-toolbar">
      <SegmentedControl v-model="filter" :options="options" />
      <span>按风险、材料缺口和项目进度聚焦当前最需要处理的课程项目。</span>
    </div>

    <div class="project-card-grid">
      <article
        v-for="project in visibleProjects"
        :key="project.id"
        class="course-project-card project-board-card tilt-project-card interactive-lift"
        :class="[`theme-${project.courseId}`, { 'risk-breathing': project.riskLevel === 'high' }]"
      >
        <header>
          <div>
            <span class="course-chip">{{ courseName(project.courseId) }}</span>
            <h3>{{ project.title }}</h3>
          </div>
          <RiskBadge :risk="project.riskLevel" />
        </header>
        <p>{{ project.description }}</p>

        <div class="project-board-main">
          <ProgressRing :value="project.healthScore" label="健康度" />
          <div class="project-board-facts">
            <span>负责人：{{ project.owner }}</span>
            <span>阶段：{{ project.phase }}</span>
            <span>截止：{{ project.deadline }}</span>
            <span>团队完成度：{{ project.teamProgress }}%</span>
          </div>
        </div>

        <div class="project-material-preview">
          <strong>材料清单预览</strong>
          <button v-for="material in projectMaterials(project.id)" :key="material.id" @click="router.push(`/app/tasks?materialId=${material.id}`)">
            <span>{{ material.title }}</span>
            <StatusPill :status="material.status" />
          </button>
        </div>

        <div class="project-feedback-line">
          <span>最近反馈</span>
          <p>{{ project.recentFeedback }}</p>
        </div>

        <footer>
          <TeamAvatarStack :members="store.members.filter((member) => member.projectId === project.id)" />
          <ProgressBar :value="project.progress" />
          <button @click="router.push(`/app/projects/${project.id}`)">进入项目空间</button>
        </footer>
      </article>
    </div>
  </section>
</template>
