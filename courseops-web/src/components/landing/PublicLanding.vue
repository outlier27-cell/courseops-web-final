<script setup lang="ts">
import { useRouter } from 'vue-router'
import AnimatedNumber from '../common/AnimatedNumber.vue'
import ProductShowcaseHero from '../common/ProductShowcaseHero.vue'
import MotionReveal from '../visuals/MotionReveal.vue'
import ThreeDHeroScene from '../visuals/ThreeDHeroScene.vue'
import VisualShaderBackground from '../visuals/VisualShaderBackground.vue'
import FloatingVisual from './FloatingVisual.vue'
import ImmersiveCourseStage from './ImmersiveCourseStage.vue'

const router = useRouter()
</script>

<template>
  <main class="public-landing">
    <VisualShaderBackground variant="sand" />
    <nav class="landing-nav">
      <strong>UIBE CourseOps</strong>
      <div>
        <a href="#proof">产品证明</a>
        <a href="#capabilities">产品能力</a>
        <a href="#workflow">使用流程</a>
        <button @click="router.push('/login')">进入工作台</button>
      </div>
    </nav>

    <MotionReveal>
      <ProductShowcaseHero
        eyebrow="Course Project Operating System"
        title="让课程项目像产品一样运转。"
        description="从项目创建、材料流转、教师反馈到审计日志，CourseOps 把期末项目变成一条可展示、可协作、可追踪的工作流。"
      >
        <template #actions>
          <div class="landing-actions" data-reveal>
            <button class="primary" @click="router.push('/login')">进入工作台</button>
            <button class="secondary" @click="router.push('/login')">查看课程项目</button>
          </div>
        </template>
        <template #visual>
          <div class="hero-visual-stage" data-reveal>
            <ThreeDHeroScene />
            <div class="orbit-label orbit-label-project">课程项目</div>
            <div class="orbit-label orbit-label-material">材料文件</div>
            <div class="orbit-label orbit-label-feedback">教师反馈</div>
            <div class="orbit-label orbit-label-audit">审计日志</div>
            <FloatingVisual />
          </div>
        </template>
      </ProductShowcaseHero>
    </MotionReveal>

    <section class="landing-scene-corridor" aria-label="CourseOps 产品叙事走廊">
      <section id="proof" class="product-proof-band" aria-label="CourseOps 产品证明">
        <article class="proof-claim">
          <span>One workflow</span>
          <h2>课程项目、材料提交、教师反馈、审计日志，一条链路完成。</h2>
        </article>
        <article>
          <strong>5 Stages</strong>
          <p>材料从准备、待提交、已提交、需修改到已通过，状态变化可追踪。</p>
        </article>
        <article>
          <strong>Every action</strong>
          <p>上传、反馈、审核、提醒都会进入动态记录，演示也有完整证据链。</p>
        </article>
      </section>

      <section id="capabilities" class="capability-cinema">
        <div class="cinema-copy">
          <span class="eyebrow">3 Reasons to use CourseOps</span>
          <h2>不是管理更多表格，而是让每个课程项目自动进入轨道。</h2>
        </div>
        <div class="cinema-reasons">
          <article>
            <span>01</span>
            <h3>Material Workflow</h3>
            <p>材料要求、文件上传、状态变化和历史记录合并成一条可演示的流转链。</p>
          </article>
          <article>
            <span>02</span>
            <h3>Feedback Loop</h3>
            <p>教师反馈直接回到材料详情里，学生处理后状态和动态同步更新。</p>
          </article>
          <article>
            <span>03</span>
            <h3>Audit Timeline</h3>
            <p>每次上传、审核、提醒都会留下审计证据，讲解时更有说服力。</p>
          </article>
        </div>
      </section>

      <section id="workflow" class="flow-section cinematic-flow">
        <div class="flow-narrative-copy">
          <span class="eyebrow">Workflow Story</span>
          <h2>从发布项目到日志留痕，形成完整业务闭环。</h2>
          <p>CourseOps 把期末项目最容易失控的材料要求、提交流转、教师反馈和审计日志串成一条可演示的产品链路。</p>
        </div>
        <div class="flow-steps">
          <span>发布项目</span>
          <span>设置材料要求</span>
          <span>学生上传</span>
          <span>教师反馈</span>
          <span>修改提交</span>
          <span>材料通过</span>
          <span>日志留痕</span>
        </div>
      </section>
    </section>

    <ImmersiveCourseStage />

    <section class="scroll-story-section compact-story">
      <div class="story-sticky-copy">
        <span class="eyebrow">Product Proof Narrative</span>
        <h2>不是后台页面堆叠，而是一条能被演示的课程项目运营故事。</h2>
        <p>每一次滚动都对应一个真实业务阶段：项目创建、材料提交、教师反馈、审计留痕和运营洞察。</p>
      </div>
      <div class="story-board">
        <article><span>01</span><h3>项目创建</h3><p>课程、团队、阶段和风险一屏建立。</p></article>
        <article><span>02</span><h3>材料流转</h3><p>文件在五个状态之间形成轨道。</p></article>
        <article><span>03</span><h3>教师反馈</h3><p>反馈绑定材料，状态自动更新。</p></article>
        <article><span>04</span><h3>审计洞察</h3><p>所有操作进入日志和数据洞察。</p></article>
      </div>
    </section>

    <section class="landing-metrics proof-metrics-strip">
      <article><AnimatedNumber :value="68" suffix="%" /><span>材料完成率</span></article>
      <article><AnimatedNumber :value="3" /><span>临期项目</span></article>
      <article><AnimatedNumber :value="4" /><span>待反馈材料</span></article>
      <article><AnimatedNumber :value="127" /><span>本周提交</span></article>
    </section>

    <section class="landing-cta">
      <div>
        <span class="eyebrow">Start Operating</span>
        <h2>先处理最重要的材料、反馈和风险。</h2>
        <p>进入工作台后，系统会按你的角色自动整理今日待办、风险项目、教师反馈和审计动态。</p>
      </div>
      <button class="primary" @click="router.push('/login')">开始使用</button>
    </section>
  </main>
</template>
