<script setup lang="ts">
defineProps<{ value: number; label?: string }>()
</script>

<template>
  <div class="progress-ring" :style="{ '--value': value }">
    <strong>{{ value }}%</strong>
    <span>{{ label ?? '完成度' }}</span>
  </div>
</template>
