<script setup lang="ts">
defineProps<{ active?: boolean; label?: string }>()
</script>

<template>
  <div v-if="active" class="success-motion" role="status">
    <i></i>
    <span>{{ label ?? '操作已完成' }}</span>
  </div>
</template>
