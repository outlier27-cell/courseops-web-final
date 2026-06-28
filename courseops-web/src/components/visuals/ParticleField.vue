<script setup lang="ts">
const particles = Array.from({ length: 38 }, (_, index) => ({
  id: index,
  x: `${(index * 29) % 100}%`,
  y: `${(index * 47) % 100}%`,
  delay: `${(index % 9) * 0.42}s`,
  size: `${3 + (index % 4)}px`,
}))
</script>

<template>
  <div class="particle-field" aria-hidden="true">
    <i
      v-for="particle in particles"
      :key="particle.id"
      :style="{ left: particle.x, top: particle.y, animationDelay: particle.delay, width: particle.size, height: particle.size }"
    ></i>
  </div>
</template>
