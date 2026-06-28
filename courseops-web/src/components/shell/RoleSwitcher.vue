<script setup lang="ts">
import { roleLabels } from '../../domain/constants'
import type { UserRole } from '../../domain/types'

defineProps<{ role: UserRole }>()
defineEmits<{ switchRole: [role: UserRole] }>()
</script>

<template>
  <select class="role-switcher" :value="role" @change="$emit('switchRole', ($event.target as HTMLSelectElement).value as UserRole)">
    <option v-for="(label, value) in roleLabels" :key="value" :value="value">{{ label }}</option>
  </select>
</template>
