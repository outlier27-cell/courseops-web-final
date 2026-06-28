<script setup lang="ts">
import EmptyMotionState from '../visuals/EmptyMotionState.vue'

defineProps<{ title: string; description: string; action?: string }>()
defineEmits<{ action: [] }>()
</script>

<template>
  <EmptyMotionState :title="title" :description="description" :action="action" @action="$emit('action')" />
</template>
