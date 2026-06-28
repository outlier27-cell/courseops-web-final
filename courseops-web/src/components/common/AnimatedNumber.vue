<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'

const props = defineProps<{ value: number; suffix?: string }>()
const display = ref(0)

function animate() {
  const start = display.value
  const end = props.value
  const started = performance.now()
  const duration = 680
  function frame(now: number) {
    const progress = Math.min(1, (now - started) / duration)
    display.value = Math.round(start + (end - start) * progress)
    if (progress < 1) requestAnimationFrame(frame)
  }
  requestAnimationFrame(frame)
}

onMounted(animate)
watch(() => props.value, animate)
</script>

<template>
  <strong class="animated-number">{{ display }}{{ suffix ?? '' }}</strong>
</template>
