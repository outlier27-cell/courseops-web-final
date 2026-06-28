<script setup lang="ts">
import { onMounted, ref } from 'vue'

const root = ref<HTMLElement | null>(null)

onMounted(() => {
  if (!root.value || window.matchMedia('(prefers-reduced-motion: reduce)').matches) return
  const items = Array.from(root.value.querySelectorAll<HTMLElement>('[data-reveal]'))
  items.forEach((item, index) => {
    item.style.opacity = '1'
    item.style.filter = 'none'
    item.animate(
      [
        { opacity: 1, transform: 'translateY(12px) scale(0.997)' },
        { opacity: 1, transform: 'translateY(0) scale(1)' },
      ],
      {
        duration: 560,
        delay: index * 80,
        easing: 'cubic-bezier(.16, 1, .3, 1)',
        fill: 'none',
      },
    )
  })
})
</script>

<template>
  <div ref="root" class="motion-reveal">
    <slot />
  </div>
</template>
