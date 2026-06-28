<script setup lang="ts">
import DeadlineCalendarCube from './DeadlineCalendarCube.vue'
import FloatingCourseCards from './FloatingCourseCards.vue'
import ThreeDDocumentStack from './ThreeDDocumentStack.vue'
import WorkflowPreviewBoard from './WorkflowPreviewBoard.vue'
</script>

<template>
  <aside class="floating-visual">
    <div class="product-mock-shell">
      <div class="mock-topbar">
        <span></span>
        <span></span>
        <span></span>
        <strong>Material Workflow</strong>
      </div>
      <WorkflowPreviewBoard />
    </div>
    <div class="progress-orb"><strong>68%</strong><span>材料完成率</span></div>
    <DeadlineCalendarCube />
    <ThreeDDocumentStack />
    <FloatingCourseCards />
  </aside>
</template>
