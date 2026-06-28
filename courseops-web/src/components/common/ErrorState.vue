<script setup lang="ts">
import { RefreshCw, TriangleAlert } from 'lucide-vue-next'

defineProps<{ message: string }>()
defineEmits<{ retry: [] }>()
</script>

<template>
  <section class="error-state">
    <div class="error-orb"><TriangleAlert :size="24" /></div>
    <h3>数据加载失败</h3>
    <p>{{ message }}</p>
    <button @click="$emit('retry')">
      <RefreshCw :size="15" />
      重试
    </button>
  </section>
</template>
