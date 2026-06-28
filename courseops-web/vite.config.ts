import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  base: process.env.GITHUB_PAGES === 'true' ? '/courseops-web-final/' : '/',
  plugins: [vue()],
  build: {
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules/vue') || id.includes('node_modules/vue-router') || id.includes('node_modules/pinia')) return 'vue'
          if (id.includes('node_modules/@arco-design')) return 'arco'
          if (id.includes('node_modules/echarts')) return 'echarts'
          if (id.includes('node_modules')) return 'vendor'
        },
      },
    },
  },
})
