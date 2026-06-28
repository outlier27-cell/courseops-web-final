<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue'

defineProps<{ title: string; description: string; action?: string }>()
defineEmits<{ action: [] }>()

const lottieRef = ref<HTMLDivElement | null>(null)
let animation: { destroy: () => void } | undefined

onMounted(async () => {
  if (!lottieRef.value || window.matchMedia('(prefers-reduced-motion: reduce)').matches) return
  try {
    const lottie = await import('lottie-web')
    animation = lottie.default.loadAnimation({
      container: lottieRef.value,
      renderer: 'svg',
      loop: true,
      autoplay: true,
      animationData: {
        v: '5.7.4',
        fr: 30,
        ip: 0,
        op: 90,
        w: 120,
        h: 120,
        nm: 'CourseOps Empty Orb',
        ddd: 0,
        assets: [],
        layers: [
          {
            ddd: 0,
            ind: 1,
            ty: 4,
            nm: 'orb',
            sr: 1,
            ks: {
              o: { a: 0, k: 100 },
              r: { a: 1, k: [{ t: 0, s: [0] }, { t: 90, s: [360] }] },
              p: { a: 0, k: [60, 60, 0] },
              a: { a: 0, k: [0, 0, 0] },
              s: { a: 1, k: [{ t: 0, s: [86, 86, 100] }, { t: 45, s: [104, 104, 100] }, { t: 90, s: [86, 86, 100] }] },
            },
            shapes: [
              {
                ty: 'el',
                p: { a: 0, k: [0, 0] },
                s: { a: 0, k: [74, 74] },
              },
              {
                ty: 'fl',
                c: { a: 0, k: [0.031, 0.498, 0.439, 1] },
                o: { a: 0, k: 28 },
              },
            ],
            ip: 0,
            op: 90,
            st: 0,
            bm: 0,
          },
        ],
      },
    })
  } catch {
    // CSS fallback remains visible.
  }
})

onBeforeUnmount(() => animation?.destroy())
</script>

<template>
  <section class="empty-motion-state">
    <div ref="lottieRef" class="empty-motion-orb"></div>
    <h3>{{ title }}</h3>
    <p>{{ description }}</p>
    <button v-if="action" @click="$emit('action')">{{ action }}</button>
  </section>
</template>
