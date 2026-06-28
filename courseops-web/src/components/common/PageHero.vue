<script setup lang="ts">
defineProps<{
  eyebrow?: string
  title: string
  description: string
}>()
</script>

<template>
  <header class="page-hero motion-rise">
    <span v-if="eyebrow" class="eyebrow">{{ eyebrow }}</span>
    <h1>{{ title }}</h1>
    <p>{{ description }}</p>
    <div class="page-hero-actions">
      <slot name="actions" />
    </div>
  </header>
</template>
