<script setup lang="ts">
import { nextTick, onMounted, onUnmounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useCourseOpsStore } from '../../stores/useCourseOpsStore'
import AnimatedNumber from '../common/AnimatedNumber.vue'
import InsightChartCard from './InsightChartCard.vue'

const store = useCourseOpsStore()
const router = useRouter()
const trendRef = ref<HTMLDivElement | null>(null)
const courseRef = ref<HTMLDivElement | null>(null)
const riskRef = ref<HTMLDivElement | null>(null)
const gapRef = ref<HTMLDivElement | null>(null)
let echartsModule: typeof import('echarts') | null = null
type EChartsInstance = import('echarts').ECharts
let charts: EChartsInstance[] = []

const palette = {
  green: '#087f70',
  blue: '#2f6fed',
  amber: '#b7791f',
  red: '#c2413b',
  grid: 'rgba(20, 23, 22, 0.08)',
  text: '#5d6563',
}

function disposeCharts() {
  charts.forEach((chart) => chart.dispose())
  charts = []
}

async function loadEcharts() {
  echartsModule ??= await import('echarts')
  return echartsModule
}

async function renderCharts() {
  if (!store.analytics) return
  await nextTick()
  const echarts = await loadEcharts()
  disposeCharts()
  const tooltip = {
    trigger: 'axis',
    backgroundColor: 'rgba(255,255,255,0.92)',
    borderColor: 'rgba(20,23,22,0.08)',
    borderWidth: 1,
    padding: 12,
    textStyle: { color: '#141716', fontFamily: 'Inter, Segoe UI, sans-serif' },
    extraCssText: 'box-shadow: 0 18px 50px rgba(20,23,22,.12); border-radius: 14px; backdrop-filter: blur(18px);',
  }
  const axis = {
    axisLine: { lineStyle: { color: palette.grid } },
    axisTick: { show: false },
    axisLabel: { color: palette.text },
    splitLine: { lineStyle: { color: palette.grid } },
  }
  const grid = { left: 34, right: 18, top: 24, bottom: 36 }

  if (trendRef.value) {
    const chart = echarts.init(trendRef.value)
    chart.setOption({
      tooltip,
      grid,
      xAxis: { type: 'category', data: store.analytics.submissionTrend.map((item) => item.day), ...axis },
      yAxis: { type: 'value', ...axis },
      series: [{
        type: 'line',
        smooth: true,
        symbolSize: 8,
        data: store.analytics.submissionTrend.map((item) => item.count),
        itemStyle: { color: palette.green },
        lineStyle: { width: 3, color: palette.green },
        areaStyle: { color: 'rgba(8,127,112,.12)' },
      }],
    })
    charts.push(chart)
  }

  if (courseRef.value) {
    const chart = echarts.init(courseRef.value)
    chart.setOption({
      tooltip,
      grid,
      xAxis: { type: 'category', data: store.analytics.courseCompletion.map((item) => item.course), ...axis },
      yAxis: { type: 'value', ...axis },
      series: [{
        type: 'bar',
        barWidth: 34,
        data: store.analytics.courseCompletion.map((item) => item.value),
        itemStyle: { color: palette.blue, borderRadius: [12, 12, 4, 4] },
      }],
    })
    charts.push(chart)
  }

  if (riskRef.value) {
    const chart = echarts.init(riskRef.value)
    chart.setOption({
      tooltip: { ...tooltip, trigger: 'item' },
      color: [palette.green, palette.amber, palette.red],
      series: [{
        type: 'pie',
        radius: ['58%', '78%'],
        padAngle: 3,
        itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
        label: { color: palette.text },
        data: store.analytics.riskDistribution.map((item) => ({ name: item.level, value: item.count })),
      }],
    })
    charts.push(chart)
  }

  if (gapRef.value) {
    const chart = echarts.init(gapRef.value)
    chart.setOption({
      tooltip,
      grid,
      xAxis: { type: 'category', data: store.analytics.materialGap.map((item) => item.material), ...axis },
      yAxis: { type: 'value', ...axis },
      series: [{
        type: 'bar',
        barWidth: 34,
        data: store.analytics.materialGap.map((item) => item.count),
        itemStyle: { color: palette.amber, borderRadius: [12, 12, 4, 4] },
      }],
    })
    charts.push(chart)
  }
}

function resizeCharts() {
  charts.forEach((chart) => chart.resize())
}

onMounted(async () => {
  if (!store.analytics) await store.loadInitialData()
  void renderCharts()
  window.addEventListener('resize', resizeCharts)
})
onUnmounted(() => {
  window.removeEventListener('resize', resizeCharts)
  disposeCharts()
})
watch(() => store.analytics, () => {
  void renderCharts()
}, { flush: 'post' })
</script>

<template>
  <section v-if="store.analytics" class="operational-insights motion-rise">
    <header class="analytics-hero">
      <div>
        <span class="eyebrow">Operational Insights</span>
        <h1>本周 3 个课程项目存在材料缺口</h1>
        <p>{{ store.currentRole === 'admin' ? '全校课程项目运行态势、风险分布与审计动态集中呈现。' : '围绕你的课程项目生成可执行的材料提交建议。' }}</p>
      </div>
      <aside class="insight-summary">
        <strong>推荐优先处理</strong>
        <p>数据库原理完成度偏低，ER 图和源码压缩包缺口最高，建议先发提醒再进入材料流转。</p>
        <button @click="router.push('/app/tasks')">进入材料流转</button>
      </aside>
    </header>

    <section class="mega-proof-board" aria-label="运营证明数字">
      <article class="proof-hero-metric">
        <span>Material completion</span>
        <strong><AnimatedNumber :value="store.analytics.materialCompletionRate" suffix="%" /></strong>
        <p>材料完成率是本周最核心的运营信号。</p>
      </article>
      <article>
        <span>Workflow proof</span>
        <strong>5</strong>
        <p>五个材料状态构成统一流转路径。</p>
      </article>
      <article>
        <span>Traceability</span>
        <strong>127</strong>
        <p>所有提交和反馈都沉淀为可审计动态。</p>
      </article>
      <article>
        <span>Decision focus</span>
        <strong>{{ store.analytics.dueSoonCount + store.analytics.revisionRequiredCount }}</strong>
        <p>临期和需修改材料合并为今日优先事项。</p>
      </article>
    </section>

    <section class="insight-grid">
      <InsightChartCard title="近 7 天提交趋势" insight="周末提交明显增加，建议周五前发提醒。" action="发送提醒" tone="green">
        <div ref="trendRef" class="chart"></div>
      </InsightChartCard>
      <InsightChartCard title="不同课程材料完成度" insight="数据库原理完成度偏低，ER 图缺口最多。" action="查看数据库项目" tone="blue" @action="router.push('/app/projects/p-db-er')">
        <div ref="courseRef" class="chart"></div>
      </InsightChartCard>
      <InsightChartCard title="风险等级分布" insight="高风险项目集中在 Web 应用开发和数据库原理。" action="查看高风险项目" tone="red" @action="router.push('/app/projects')">
        <div class="risk-chart-wrap">
          <div ref="riskRef" class="chart"></div>
          <div class="risk-center-number">
            <strong>{{ store.analytics.riskDistribution.reduce((sum, item) => sum + item.count, 0) }}</strong>
            <span>风险项</span>
          </div>
        </div>
      </InsightChartCard>
      <InsightChartCard title="材料类型缺口分布" insight="数据库设计图和源码压缩包缺口最高。" action="进入材料流转" tone="amber" @action="router.push('/app/tasks')">
        <div ref="gapRef" class="chart"></div>
      </InsightChartCard>
    </section>
  </section>

  <section v-else class="operational-insights motion-rise analytics-loading-stage">
    <header class="analytics-hero">
      <div>
        <span class="eyebrow">Operational Insights</span>
        <h1>正在生成运营证明数据</h1>
        <p>系统正在汇总材料完成率、临期风险、反馈缺口和审计动态。</p>
      </div>
      <aside class="insight-summary">
        <strong>Loading signal</strong>
        <p>数据面板即将完成渲染。</p>
      </aside>
    </header>
  </section>
</template>
