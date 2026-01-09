import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default defineConfig({
  plugins: [vue()],
  define: {
    global: 'window'
  },
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
      'global': resolve(__dirname, 'node_modules/global')
    }
  },
  server: {
    port: 3000,
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/api')
      },
      '/ws': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        ws: true
      }
    }
  },
  build: {
    target: 'es2015',
    minify: 'terser',
    sourcemap: false,
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true,
        pure_funcs: ['console.log']
      }
    },
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules')) {
            if (id.includes('vue')) {
              return 'vue-vendor'
            }
            if (id.includes('echarts')) {
              return 'echarts-vendor'
            }
            if (id.includes('axios')) {
              return 'axios-vendor'
            }
            return 'vendor'
          }
        },
        chunkFileNames: 'js/[name]-[hash].js',
        entryFileNames: 'js/[name]-[hash].js',
        assetFileNames: (assetInfo) => {
          const info = assetInfo.name.split('.')
          const ext = info[info.length - 1]
          if (/\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/i.test(assetInfo.name)) {
            return `media/[name]-[hash].[ext]`
          }
          if (/\.(png|jpe?g|gif|svg|ico|webp)(\?.*)?$/i.test(assetInfo.name)) {
            return `images/[name]-[hash].[ext]`
          }
          if (/\.(woff2?|eot|ttf|otf)(\?.*)?$/i.test(assetInfo.name)) {
            return `fonts/[name]-[hash].[ext]`
          }
          return `${ext}/[name]-[hash].[ext]`
        }
      }
    },
    chunkSizeWarningLimit: 1000,
    assetsInlineLimit: 4096,
    reportCompressedSize: false,
    cssCodeSplit: true
  },
  optimizeDeps: {
    include: ['vue', 'vue-router', 'pinia', 'axios', 'echarts'],
    exclude: []
  },
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: ['./src/test/setup.js'],
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html', 'lcov'],
      exclude: [
        'node_modules/',
        'src/test/',
        '**/*.d.ts',
        '**/*.config.js',
        '**/mockData',
        'dist/'
      ]
    },
    include: ['src/**/*.{test,spec}.{js,jsx,ts,tsx}'],
    exclude: ['node_modules/', 'dist/']
  }
})
