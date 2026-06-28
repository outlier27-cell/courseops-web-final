<script setup lang="ts">
import type { TeamMember } from '../../domain/types'

defineProps<{ members: TeamMember[] }>()
</script>

<template>
  <div class="team-avatar-stack">
    <span v-for="member in members.slice(0, 4)" :key="member.id" :title="member.name">{{ member.avatar }}</span>
  </div>
</template>
