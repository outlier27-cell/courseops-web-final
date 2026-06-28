<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue'

const canvasRef = ref<HTMLCanvasElement | null>(null)
const fallback = ref(false)
let cleanup: (() => void) | undefined

function shouldFallback() {
  return window.matchMedia('(prefers-reduced-motion: reduce)').matches || window.innerWidth < 900 || navigator.hardwareConcurrency <= 4
}

onMounted(async () => {
  if (!canvasRef.value || shouldFallback()) {
    fallback.value = true
    return
  }

  try {
    const THREE = await import('three')
    const canvas = canvasRef.value
    const renderer = new THREE.WebGLRenderer({ canvas, alpha: true, antialias: true, powerPreference: 'high-performance' })
    const scene = new THREE.Scene()
    const camera = new THREE.PerspectiveCamera(38, 1, 0.1, 100)
    camera.position.set(0, 0, 7)

    const group = new THREE.Group()
    scene.add(group)

    const material = new THREE.MeshPhysicalMaterial({
      color: 0xdff4ef,
      metalness: 0.04,
      roughness: 0.28,
      transmission: 0.42,
      thickness: 0.6,
      transparent: true,
      opacity: 0.88,
    })

    const accent = new THREE.MeshStandardMaterial({ color: 0x087f70, roughness: 0.38, metalness: 0.08 })
    const blue = new THREE.MeshStandardMaterial({ color: 0x2f6fed, roughness: 0.44, metalness: 0.05 })

    const planet = new THREE.Mesh(new THREE.IcosahedronGeometry(1.32, 4), material)
    group.add(planet)

    for (let index = 0; index < 7; index += 1) {
      const card = new THREE.Mesh(new THREE.BoxGeometry(1.25, 0.76, 0.045), index % 2 ? accent : blue)
      const angle = (index / 7) * Math.PI * 2
      card.position.set(Math.cos(angle) * 2.25, Math.sin(angle) * 0.95, Math.sin(angle) * 1.1)
      card.rotation.set(0.22, angle, -0.08)
      group.add(card)
    }

    const light = new THREE.DirectionalLight(0xffffff, 2.2)
    light.position.set(4, 5, 6)
    scene.add(light)
    scene.add(new THREE.AmbientLight(0xffffff, 1.8))

    const resize = () => {
      const rect = canvas.getBoundingClientRect()
      renderer.setSize(rect.width, rect.height, false)
      renderer.setPixelRatio(Math.min(window.devicePixelRatio, 1.6))
      camera.aspect = rect.width / rect.height
      camera.updateProjectionMatrix()
    }

    let frame = 0
    let raf = 0
    const tick = () => {
      frame += 0.01
      group.rotation.y += 0.004
      group.rotation.x = Math.sin(frame) * 0.06
      renderer.render(scene, camera)
      raf = requestAnimationFrame(tick)
    }

    resize()
    tick()
    window.addEventListener('resize', resize)
    cleanup = () => {
      window.removeEventListener('resize', resize)
      cancelAnimationFrame(raf)
      renderer.dispose()
      scene.traverse((object) => {
        const disposable = object as unknown as { geometry?: { dispose?: () => void } }
        disposable.geometry?.dispose?.()
      })
    }
  } catch {
    fallback.value = true
  }
})

onBeforeUnmount(() => cleanup?.())
</script>

<template>
  <div class="three-hero-scene" :class="{ fallback }">
    <canvas ref="canvasRef"></canvas>
    <div class="three-hero-fallback">
      <span></span>
      <span></span>
      <span></span>
    </div>
  </div>
</template>
