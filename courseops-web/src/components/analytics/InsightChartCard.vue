<script setup lang="ts">
defineProps<{ title: string; insight: string; action: string; tone?: 'green' | 'blue' | 'amber' | 'red' }>()
defineEmits<{ action: [] }>()
</script>

<template>
  <article class="insight-chart-card" :class="tone ?? 'green'">
    <header>
      <span>Insight</span>
      <h3>{{ title }}</h3>
    </header>
    <div class="chart-slot"><slot /></div>
    <footer>
      <p>{{ insight }}</p>
      <button @click="$emit('action')">{{ action }}</button>
    </footer>
  </article>
</template>
