<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{ title: string; description: string; eyebrow?: string }>()
const titleLines = computed(() => props.title.split('\n'))
</script>

<template>
  <section class="product-showcase-hero motion-rise">
    <div class="product-showcase-copy">
      <span v-if="eyebrow" class="eyebrow">{{ eyebrow }}</span>
      <h1>
        <span v-for="line in titleLines" :key="line">{{ line }}</span>
      </h1>
      <p>{{ description }}</p>
      <slot name="actions" />
    </div>
    <slot name="visual" />
  </section>
</template>
