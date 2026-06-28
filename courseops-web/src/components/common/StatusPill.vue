<script setup lang="ts">
import { materialStatusLabels } from '../../domain/constants'
import type { MaterialStatus } from '../../domain/types'

defineProps<{ status: MaterialStatus }>()
</script>

<template>
  <span class="status-pill" :class="status">{{ materialStatusLabels[status] }}</span>
</template>
