<script setup lang="ts">
const steps = ['待准备', '待提交', '已提交', '需修改', '已通过']
</script>

<template>
  <div class="workflow-orbit" aria-hidden="true">
    <div class="orbit-track"></div>
    <span v-for="(step, index) in steps" :key="step" :style="{ '--i': index }">
      <b>{{ step }}</b>
    </span>
    <i class="orbit-file orbit-file-a"></i>
    <i class="orbit-file orbit-file-b"></i>
  </div>
</template>
