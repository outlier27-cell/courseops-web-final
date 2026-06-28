<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'

const stageRef = ref<HTMLElement | null>(null)
const progress = ref(0)
const pointerX = ref(0)
const pointerY = ref(0)

const chapters = [
  {
    id: '01',
    kicker: 'Project ignition',
    title: '课程项目先被点亮',
    text: '课程、团队、阶段、截止时间和风险聚合成一个运营核心，用户第一眼看到的是完整系统，而不是散落表格。',
  },
  {
    id: '02',
    kicker: 'Material orbit',
    title: '材料沿轨道流转',
    text: '待准备、待提交、已提交、需修改、已通过形成连续路径，材料像能量包一样穿过每个业务站点。',
  },
  {
    id: '03',
    kicker: 'Feedback loop',
    title: '教师反馈形成闭环',
    text: '反馈、修订、重新提交和审核状态绑定在同一条链路上，界面表达的是协同推进，而不是静态记录。',
  },
  {
    id: '04',
    kicker: 'Audit field',
    title: '所有动作留下审计光轨',
    text: '上传、审核、提醒、通过都进入日志场，演示、验收和后续追踪都有清晰证据。',
  },
]

const activeIndex = computed(() => Math.min(chapters.length - 1, Math.max(0, Math.round(progress.value * (chapters.length - 1)))))
const stageStyle = computed(() => ({
  '--stage-progress': progress.value.toFixed(3),
  '--pointer-x': pointerX.value.toFixed(3),
  '--pointer-y': pointerY.value.toFixed(3),
}))

function updateProgress() {
  if (!stageRef.value) return
  const rect = stageRef.value.getBoundingClientRect()
  const travel = Math.max(1, rect.height - window.innerHeight)
  progress.value = Math.min(1, Math.max(0, -rect.top / travel))
}

function handlePointer(event: PointerEvent) {
  if (!stageRef.value) return
  const rect = stageRef.value.getBoundingClientRect()
  pointerX.value = (event.clientX - rect.left) / rect.width - 0.5
  pointerY.value = (event.clientY - rect.top) / rect.height - 0.5
}

onMounted(() => {
  updateProgress()
  window.addEventListener('scroll', updateProgress, { passive: true })
  window.addEventListener('resize', updateProgress)
})

onBeforeUnmount(() => {
  window.removeEventListener('scroll', updateProgress)
  window.removeEventListener('resize', updateProgress)
})
</script>

<template>
  <section
    ref="stageRef"
    class="immersive-course-stage"
    :style="stageStyle"
    aria-label="CourseOps 沉浸式产品叙事"
    @pointermove="handlePointer"
  >
    <div class="immersive-stage-sticky">
      <div class="stage-atmosphere" aria-hidden="true">
        <span class="energy-ring ring-a"></span>
        <span class="energy-ring ring-b"></span>
        <span class="energy-ring ring-c"></span>
      </div>

      <div class="stage-copy">
        <span class="eyebrow">Product Motion System</span>
        <h2>让 CourseOps 像一个会运转的课程项目引擎。</h2>
        <p>不再把能力拆成一块块说明卡，而是让项目、材料、反馈和审计在同一个空间里逐步出现，像一部可以滚动观看的产品短片。</p>
      </div>

      <div class="stage-device" aria-hidden="true">
        <div class="engine-core">
          <span class="core-ring"></span>
          <strong>CourseOps</strong>
          <em>Live workflow</em>
        </div>
        <div class="orbit-path path-a"></div>
        <div class="orbit-path path-b"></div>
        <div class="orbit-path path-c"></div>
        <div class="floating-module module-a"><span>课程项目</span><strong>25</strong></div>
        <div class="floating-module module-b"><span>材料完成率</span><strong>68%</strong></div>
        <div class="floating-module module-c"><span>教师反馈</span><strong>4</strong></div>
        <div class="floating-module module-d"><span>审计日志</span><strong>Live</strong></div>
      </div>

      <div class="stage-chapters">
        <article
          v-for="(chapter, index) in chapters"
          :key="chapter.id"
          :class="{ active: index === activeIndex }"
        >
          <span>{{ chapter.id }}</span>
          <small>{{ chapter.kicker }}</small>
          <h3>{{ chapter.title }}</h3>
          <p>{{ chapter.text }}</p>
        </article>
      </div>
    </div>
  </section>
</template>
