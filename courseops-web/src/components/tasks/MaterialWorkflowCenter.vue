<script setup lang="ts">
import { computed, onMounted, ref, watchEffect } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { materialStatusLabels } from '../../domain/constants'
import type { MaterialStatus } from '../../domain/types'
import { useCourseOpsStore } from '../../stores/useCourseOpsStore'
import SuccessMotion from '../visuals/SuccessMotion.vue'
import WorkflowOrbit from '../visuals/WorkflowOrbit.vue'
import MaterialDetailDrawer from './MaterialDetailDrawer.vue'
import MaterialTaskCard from './MaterialTaskCard.vue'

const statusGuidance: Record<MaterialStatus, { title: string; tip: string }> = {
  not_started: { title: '先把材料骨架搭起来', tip: '补齐封面、目录和核心截图，再推进下一版。' },
  preparing: { title: '整理完成后即可提交', tip: '统一命名和格式后直接进入上传。' },
  submitted: { title: '等待教师确认', tip: '保持版本可追溯，反馈会直接进入轨道。' },
  revision_required: { title: '优先处理高风险修改', tip: '按反馈逐条回收问题，修完再重新上传。' },
  approved: { title: '材料可归档', tip: '可继续补充展示材料，或者进入项目收尾。' },
}

const route = useRoute()
const router = useRouter()
const store = useCourseOpsStore()
const successMessage = ref('')
let successTimer = 0

watchEffect(() => {
  const id = String(route.query.materialId || '')
  if (id) store.selectMaterial(id)
})

const selectedSubmissions = computed(() => store.selectedMaterialSubmissions)
const selectedHistories = computed(() => store.selectedMaterialHistory)
const selectedFeedback = computed(() => store.selectedMaterialFeedback)

onMounted(() => {
  if (!store.users.length || !store.materials.length) void store.loadInitialData()
})

function selectMaterial(id: string) {
  store.selectMaterial(id)
  void router.replace({ path: '/app/tasks', query: { materialId: id } })
}

function showSuccess(message: string) {
  successMessage.value = message
  window.clearTimeout(successTimer)
  successTimer = window.setTimeout(() => {
    successMessage.value = ''
  }, 1800)
}

async function updateStatus(status: MaterialStatus) {
  await store.changeMaterialStatus(store.selectedMaterial.id, status)
  if (!store.errorMessage) showSuccess(status === 'approved' ? '材料已通过，审计记录已更新' : '材料已标记为需修改')
}

async function uploadMaterialFile(file: File | null) {
  await store.submitMaterialFile(store.selectedMaterial.id, file)
  if (!store.errorMessage) showSuccess('上传成功，材料已进入提交轨道')
}
</script>

<template>
  <section v-if="store.selectedMaterial" class="material-workflow motion-rise">
    <SuccessMotion :active="!!successMessage" :label="successMessage" />

    <header class="workflow-command">
      <WorkflowOrbit />
      <div>
        <span class="eyebrow">Material Workflow</span>
        <h1>材料流转控制台</h1>
        <p>从待准备到已通过，上传、审核、修改和日志留痕在这里形成一条完整闭环。</p>
      </div>
      <div class="workflow-command-stats">
        <article v-for="column in store.materialColumns" :key="column.status">
          <strong>{{ column.materials.length }}</strong>
          <span>{{ materialStatusLabels[column.status] }}</span>
        </article>
      </div>
    </header>

    <section class="material-flow-rail" aria-label="材料流转轨道">
      <div class="rail-copy">
        <span class="eyebrow">Material Flow Rail</span>
        <h2>文件沿业务轨道移动，而不是被放进五个静态盒子。</h2>
        <p>选中任何材料，轨道、状态站点和右侧控制面板会同步表达它当前所处的步骤。</p>
      </div>
      <div class="flow-rail-line"></div>
      <div class="flow-rail-glow"></div>
      <article
        v-for="(column, index) in store.materialColumns"
        :key="column.status"
        class="flow-station"
        :class="{ active: column.materials.some((material) => material.id === store.selectedMaterialId) }"
        :style="{ '--station-index': index }"
      >
        <b>{{ index + 1 }}</b>
        <span>{{ materialStatusLabels[column.status] }}</span>
        <strong>{{ column.materials.length }}</strong>
      </article>
    </section>

    <div class="workflow-layout">
      <div class="workflow-board">
        <section v-for="column in store.materialColumns" :key="column.status" class="workflow-column">
          <h3>{{ materialStatusLabels[column.status] }} <span>{{ column.materials.length }}</span></h3>
          <MaterialTaskCard
            v-for="material in column.materials"
            :key="material.id"
            :material="material"
            :project-name="store.projects.find((project) => project.id === material.projectId)?.title ?? '课程项目'"
            :active="store.selectedMaterialId === material.id"
            @select="selectMaterial"
          />
          <div class="workflow-column-note" :class="`note-${column.status}`">
            <strong>{{ statusGuidance[column.status].title }}</strong>
            <p>{{ statusGuidance[column.status].tip }}</p>
          </div>
        </section>
      </div>

      <MaterialDetailDrawer
        :material="store.selectedMaterial"
        :submissions="selectedSubmissions"
        :histories="selectedHistories"
        :feedback="selectedFeedback"
        :error-message="store.errorMessage"
        @update-status="updateStatus"
        @upload="uploadMaterialFile"
      />
    </div>
  </section>

  <section v-else class="empty-state">
    <div class="empty-orb"></div>
    <h3>材料流转加载中</h3>
    <p>正在读取材料、提交记录、教师反馈和状态历史。</p>
  </section>
</template>
