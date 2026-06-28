<script setup lang="ts">
defineProps<{ value: number }>()
</script>

<template>
  <div class="progress-bar" aria-label="progress">
    <i :style="{ width: `${Math.max(0, Math.min(100, value))}%` }"></i>
  </div>
</template>
