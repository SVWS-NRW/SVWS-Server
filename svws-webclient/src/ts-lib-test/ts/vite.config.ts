/// <reference types="vitest" />

import { defineConfig } from 'vite'
import { resolve } from 'path'

export default defineConfig({
  test: {},
  resolve: {
	alias: [{ find: "~", replacement: resolve(__dirname, '../../../src/ts-lib/ts/src') }],
  },
  build: { emptyOutDir: true }
})