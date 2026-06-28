<script setup lang="ts">
import type { MaterialHistory } from '../../domain/types'

defineProps<{ histories: MaterialHistory[] }>()
</script>

<template>
  <section class="material-timeline">
    <h3>状态历史</h3>
    <article v-for="history in histories" :key="history.id">
      <strong>{{ history.action }}</strong>
      <span>{{ history.actor }} · {{ history.createdAt }}</span>
      <p v-if="history.fileName">文件：{{ history.fileName }}</p>
      <p v-else-if="history.fromStatus && history.toStatus">{{ history.fromStatus }} → {{ history.toStatus }}</p>
    </article>
  </section>
</template>
