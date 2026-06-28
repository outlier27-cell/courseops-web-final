<script setup lang="ts">
import type { MaterialFormat } from '../../domain/types'

defineProps<{ format: MaterialFormat }>()
</script>

<template>
  <span class="file-format-badge">{{ format }}</span>
</template>
