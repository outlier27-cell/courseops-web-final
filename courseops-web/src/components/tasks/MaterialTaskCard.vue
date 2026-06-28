<script setup lang="ts">
import { CalendarClock, FileUp, MessageSquareText, UserRound } from 'lucide-vue-next'
import type { MaterialItem } from '../../domain/types'
import FileFormatBadge from '../common/FileFormatBadge.vue'
import ProgressBar from '../common/ProgressBar.vue'
import StatusPill from '../common/StatusPill.vue'

defineProps<{ material: MaterialItem; projectName: string; active?: boolean }>()
defineEmits<{ select: [id: string] }>()
</script>

<template>
  <button class="material-task-card interactive-lift" :class="{ active }" @click="$emit('select', material.id)">
    <div class="task-card-top">
      <strong>{{ material.title }}</strong>
      <StatusPill :status="material.status" />
    </div>
    <p>{{ projectName }}</p>
    <div class="task-meta-grid">
      <span><UserRound :size="13" />{{ material.assignee }}</span>
      <span><CalendarClock :size="13" />{{ material.deadline }}</span>
    </div>
    <em>{{ material.riskHint }}</em>
    <ProgressBar :value="material.progress" />
    <small><FileUp :size="14" /> {{ material.latestFile || '暂无文件' }}</small>
    <footer>
      <FileFormatBadge :format="material.format" />
      <span><MessageSquareText :size="13" /> {{ material.feedbackCount }} 条反馈</span>
    </footer>
  </button>
</template>
