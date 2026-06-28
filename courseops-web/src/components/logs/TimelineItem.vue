<script setup lang="ts">
import { Activity, CheckCircle2, FileUp, MessageSquareText, ShieldCheck } from 'lucide-vue-next'
import { computed, ref } from 'vue'
import type { Component } from 'vue'
import { materialStatusLabels } from '../../domain/constants'
import type { MaterialStatus, OperationLog, UserRole } from '../../domain/types'

const props = defineProps<{ log: OperationLog; role: UserRole }>()
const expanded = ref(false)

const icon = computed<Component>(() => {
  if (props.log.action.includes('上传')) return FileUp
  if (props.log.action.includes('通过')) return CheckCircle2
  if (props.log.action.includes('反馈')) return MessageSquareText
  if (props.log.action.includes('审计') || props.log.action.includes('风险')) return ShieldCheck
  return Activity
})

const summary = computed(() => {
  const before = props.log.before ? `从“${formatValue(props.log.before)}”` : ''
  const after = props.log.after ? `更新为“${formatValue(props.log.after)}”` : ''
  return `${props.log.actor} 在 ${props.log.targetName} ${before}${after}`.trim()
})

function formatValue(value: string) {
  if (value in materialStatusLabels) return materialStatusLabels[value as MaterialStatus]
  return value
}
</script>

<template>
  <article class="timeline-item" :class="log.actorRole">
    <button class="timeline-main" @click="expanded = !expanded">
      <i><component :is="icon" :size="18" /></i>
      <span class="timeline-time">{{ log.createdAt }}</span>
      <strong>{{ log.action }}</strong>
      <p>{{ summary }}</p>
      <em>{{ expanded ? '收起' : '展开' }}</em>
    </button>
    <div v-if="expanded" class="timeline-detail">
      <span>来源模块：{{ log.source }}</span>
      <span>操作对象：{{ log.targetType }}</span>
      <span>操作角色：{{ log.actorRole }}</span>
      <span v-if="role === 'admin'">审计 ID：{{ log.id }}</span>
    </div>
  </article>
</template>
