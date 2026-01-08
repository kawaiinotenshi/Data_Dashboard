import js from '@eslint/js'
import vue from 'eslint-plugin-vue'

export default [
  {
    ignores: ['node_modules/**', 'dist/**', '.vite/**', 'coverage/**', 'public/**']
  },
  js.configs.recommended,
  ...vue.configs['flat/recommended'],
  {
    files: ['**/*.{js,mjs,cjs,vue}'],
    languageOptions: {
      ecmaVersion: 2021,
      sourceType: 'module',
      globals: {
        window: 'readonly',
        document: 'readonly',
        console: 'readonly',
        fetch: 'readonly',
        ResizeObserver: 'readonly',
        setInterval: 'readonly',
        clearInterval: 'readonly',
        setTimeout: 'readonly',
        clearTimeout: 'readonly',
        process: 'readonly',
        define: 'readonly',
        exports: 'readonly',
        require: 'readonly',
        __dirname: 'readonly',
        module: 'readonly',
        localStorage: 'readonly',
        sessionStorage: 'readonly',
        alert: 'readonly',
        confirm: 'readonly',
        prompt: 'readonly',
        URL: 'readonly'
      }
    },
    rules: {
      'vue/multi-word-component-names': 'off',
      'no-console': 'off',
      'no-debugger': 'off'
    }
  }
]
