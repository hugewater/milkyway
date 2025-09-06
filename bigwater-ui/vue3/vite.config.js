import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  optimizeDeps: {
    include: ['@antv/g6']
  },
  server: {
    port: 5175,
    proxy: {
      '/bw-api': {
        target: 'http://localhost:9999',
        changeOrigin: true,
        // if your API is under /bw-api already, no rewrite needed.
        // If your Quarkus endpoints are at /, use:
        // rewrite: (path) => path.replace(/^\/bw-api/, '')
      }
    }
  }
})
