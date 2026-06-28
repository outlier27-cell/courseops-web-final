<script setup lang="ts">
import { onBeforeUnmount, onMounted, ref } from 'vue'

const canvasRef = ref<HTMLCanvasElement | null>(null)
const ready = ref(false)
let cleanup: (() => void) | undefined

function shouldFallback() {
  return window.matchMedia('(prefers-reduced-motion: reduce)').matches || navigator.hardwareConcurrency <= 4
}

onMounted(async () => {
  if (!canvasRef.value || shouldFallback()) return

  try {
    const THREE = await import('three')
    const canvas = canvasRef.value
    const renderer = new THREE.WebGLRenderer({
      canvas,
      alpha: true,
      antialias: true,
      powerPreference: 'high-performance',
    })
    const scene = new THREE.Scene()
    const camera = new THREE.PerspectiveCamera(42, 1, 0.1, 100)
    camera.position.set(0, 0, 8.5)

    const group = new THREE.Group()
    scene.add(group)

    const sandMaterial = new THREE.MeshPhysicalMaterial({
      color: 0xe8f7f2,
      roughness: 0.36,
      metalness: 0.02,
      transmission: 0.36,
      thickness: 1.1,
      transparent: true,
      opacity: 0.72,
      side: THREE.DoubleSide,
    })
    const tealMaterial = new THREE.MeshStandardMaterial({ color: 0x087f70, roughness: 0.44, metalness: 0.06, transparent: true, opacity: 0.42 })
    const blueMaterial = new THREE.MeshStandardMaterial({ color: 0x2f6fed, roughness: 0.52, metalness: 0.04, transparent: true, opacity: 0.30 })
    const amberMaterial = new THREE.MeshStandardMaterial({ color: 0xd5a248, roughness: 0.58, metalness: 0.02, transparent: true, opacity: 0.24 })

    const ribbonGeometry = new THREE.TorusKnotGeometry(1.9, 0.035, 210, 12, 2, 5)
    const ribbon = new THREE.Mesh(ribbonGeometry, sandMaterial)
    ribbon.position.set(2.8, -1.4, -2.1)
    ribbon.rotation.set(0.9, -0.45, 0.12)
    group.add(ribbon)

    const planeGeometry = new THREE.PlaneGeometry(2.9, 1.08, 22, 8)
    const materials = [tealMaterial, blueMaterial, amberMaterial, sandMaterial]
    for (let index = 0; index < 9; index += 1) {
      const card = new THREE.Mesh(planeGeometry, materials[index % materials.length])
      const angle = (index / 9) * Math.PI * 2
      card.position.set(Math.cos(angle) * 4.1, Math.sin(angle * 1.2) * 1.45, -1.8 - (index % 3) * 0.58)
      card.rotation.set(0.12 + index * 0.025, -0.32 + angle * 0.12, Math.sin(angle) * 0.14)
      group.add(card)
    }

    const particleGeometry = new THREE.BufferGeometry()
    const count = 180
    const positions = new Float32Array(count * 3)
    for (let index = 0; index < count; index += 1) {
      positions[index * 3] = (Math.random() - 0.5) * 13
      positions[index * 3 + 1] = (Math.random() - 0.5) * 7
      positions[index * 3 + 2] = -1 - Math.random() * 6
    }
    particleGeometry.setAttribute('position', new THREE.BufferAttribute(positions, 3))
    const particles = new THREE.Points(
      particleGeometry,
      new THREE.PointsMaterial({
        color: 0x087f70,
        size: 0.018,
        transparent: true,
        opacity: 0.34,
        depthWrite: false,
      }),
    )
    scene.add(particles)

    scene.add(new THREE.AmbientLight(0xffffff, 2.4))
    const keyLight = new THREE.DirectionalLight(0xffffff, 2.6)
    keyLight.position.set(5, 4, 6)
    scene.add(keyLight)

    const resize = () => {
      const rect = canvas.getBoundingClientRect()
      renderer.setPixelRatio(Math.min(window.devicePixelRatio, 1.35))
      renderer.setSize(rect.width, rect.height, false)
      camera.aspect = rect.width / rect.height
      camera.updateProjectionMatrix()
    }

    let raf = 0
    let time = 0
    const tick = () => {
      time += 0.006
      group.rotation.y = Math.sin(time * 0.72) * 0.12
      group.rotation.x = Math.cos(time * 0.54) * 0.045
      ribbon.rotation.z += 0.0022
      particles.rotation.z -= 0.0008
      particles.rotation.y = Math.sin(time) * 0.035
      renderer.render(scene, camera)
      ready.value = true
      raf = requestAnimationFrame(tick)
    }

    resize()
    tick()
    window.addEventListener('resize', resize)
    cleanup = () => {
      window.removeEventListener('resize', resize)
      cancelAnimationFrame(raf)
      renderer.dispose()
      ribbonGeometry.dispose()
      planeGeometry.dispose()
      particleGeometry.dispose()
      materials.forEach((material) => material.dispose())
    }
  } catch {
    ready.value = false
  }
})

onBeforeUnmount(() => cleanup?.())
</script>

<template>
  <canvas ref="canvasRef" class="global-depth-scene" :class="{ ready }"></canvas>
</template>
