import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    // 去掉 Vite 自动加的 crossorigin 属性（导致浏览器 CORS 预检卡顿）
    {
      name: 'remove-crossorigin',
      transformIndexHtml(html) {
        return html.replace(/ crossorigin/g, '')
      }
    }
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  // ========== 开发服务器配置 ==========
  server: {
    port: 5173,                     // 前端开发服务器端口
    proxy: {
      // 将以 /api 开头的请求代理到后端 Spring Boot
      '/api': {
        target: 'http://localhost:8080',  // 后端地址
        changeOrigin: true,               // 修改请求来源
        // secure: false,                 // HTTPS 时使用
      }
    }
  }
})
