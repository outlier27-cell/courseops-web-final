<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import GlobalDepthScene from './GlobalDepthScene.vue'

defineProps<{ variant?: 'sand' | 'aurora' | 'orbit' }>()

const pointerX = ref(0)
const pointerY = ref(0)
const scrollRatio = ref(0)

const backgroundStyle = computed(() => ({
  '--pointer-x': pointerX.value.toFixed(3),
  '--pointer-y': pointerY.value.toFixed(3),
  '--scroll-ratio': scrollRatio.value.toFixed(3),
}))

function handlePointer(event: PointerEvent) {
  pointerX.value = event.clientX / window.innerWidth - 0.5
  pointerY.value = event.clientY / window.innerHeight - 0.5
}

function handleScroll() {
  const max = Math.max(1, document.documentElement.scrollHeight - window.innerHeight)
  scrollRatio.value = Math.min(1, Math.max(0, window.scrollY / max))
}

onMounted(() => {
  handleScroll()
  window.addEventListener('pointermove', handlePointer, { passive: true })
  window.addEventListener('scroll', handleScroll, { passive: true })
  window.addEventListener('resize', handleScroll)
})

onBeforeUnmount(() => {
  window.removeEventListener('pointermove', handlePointer)
  window.removeEventListener('scroll', handleScroll)
  window.removeEventListener('resize', handleScroll)
})
</script>

<template>
  <div class="visual-shader-background" :class="variant ?? 'aurora'" :style="backgroundStyle" aria-hidden="true">
    <GlobalDepthScene />
    <i class="shader-current current-a"></i>
    <i class="shader-current current-b"></i>
    <i class="shader-current current-c"></i>
    <i class="shader-blob shader-a"></i>
    <i class="shader-blob shader-b"></i>
    <i class="shader-blob shader-c"></i>
    <canvas class="shader-noise"></canvas>
  </div>
</template>
