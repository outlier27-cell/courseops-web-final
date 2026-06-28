<script setup lang="ts">
import { riskLabels } from '../../domain/constants'
import type { RiskLevel } from '../../domain/types'

defineProps<{ risk: RiskLevel }>()
</script>

<template>
  <span class="risk-badge" :class="risk">{{ riskLabels[risk] }}</span>
</template>
