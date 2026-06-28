<script setup lang="ts">
import { CheckCircle2, FileArchive, RefreshCcw, UploadCloud } from 'lucide-vue-next'
import type { MaterialHistory, MaterialItem, MaterialStatus, Submission, TeacherFeedback } from '../../domain/types'
import FileFormatBadge from '../common/FileFormatBadge.vue'
import StatusPill from '../common/StatusPill.vue'
import MaterialTimeline from './MaterialTimeline.vue'

defineProps<{
  material: MaterialItem
  submissions: Submission[]
  histories: MaterialHistory[]
  feedback: TeacherFeedback[]
  errorMessage: string
}>()

defineEmits<{ updateStatus: [status: MaterialStatus]; upload: [file: File | null] }>()

function emitFile(event: Event, emit: (event: 'upload', file: File | null) => void) {
  const input = event.target as HTMLInputElement
  emit('upload', input.files?.[0] ?? null)
  input.value = ''
}
</script>

<template>
  <aside class="material-detail-drawer">
    <div class="drawer-head">
      <span>材料详情</span>
      <h2>{{ material.title }}</h2>
      <div class="drawer-head-meta">
        <StatusPill :status="material.status" />
        <FileFormatBadge :format="material.format" />
      </div>
    </div>

    <p v-if="errorMessage" class="inline-error">{{ errorMessage }}</p>

    <label class="upload-dropzone">
      <UploadCloud :size="24" />
      <strong>上传最新材料文件</strong>
      <span>支持 {{ material.format }}，单文件不超过 50MB。成功后会写入提交记录、状态历史、操作日志和通知。</span>
      <input type="file" @change="emitFile($event, $emit)" />
    </label>

    <section class="drawer-section">
      <h3>材料要求</h3>
      <p>{{ material.description }}</p>
      <div class="requirement-grid">
        <span>{{ material.required ? '必交材料' : '选交材料' }}</span>
        <span>{{ material.deadline }} 截止</span>
        <span>最新文件：{{ material.latestFile || '暂无文件' }}</span>
      </div>
    </section>

    <section class="drawer-section">
      <h3>上传记录</h3>
      <article v-for="submission in submissions" :key="submission.id" class="submission-chip">
        <FileArchive :size="16" />
        <span>
          <strong>{{ submission.fileName }}</strong>
          <small>{{ submission.submittedBy }} · {{ submission.submittedAt }}</small>
          <em v-if="submission.feedback">{{ submission.feedback }}</em>
        </span>
      </article>
      <p v-if="!submissions.length" class="muted-copy">暂无提交记录。</p>
    </section>

    <section class="drawer-section">
      <h3>教师反馈</h3>
      <article v-for="item in feedback" :key="item.id" class="feedback-bubble">
        <strong>{{ item.teacherName }}</strong>
        <p>{{ item.content }}</p>
      </article>
      <p v-if="!feedback.length" class="muted-copy">暂无教师反馈。</p>
    </section>

    <MaterialTimeline :histories="histories" />

    <section class="drawer-actions">
      <button @click="$emit('updateStatus', 'revision_required')">
        <RefreshCcw :size="16" />
        标记需修改
      </button>
      <button class="approve-action" @click="$emit('updateStatus', 'approved')">
        <CheckCircle2 :size="16" />
        标记已通过
      </button>
    </section>
  </aside>
</template>
