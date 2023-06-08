import { defineConfig } from "vitest/config";
import { resolve } from 'path'

export default defineConfig({
  test: {
    globals: true,
    root: "../../../src",
    reporters: ["default", "junit"],
    outputFile: "../build/coverage/core/junit.xml",
    coverage: {
      provider: "v8",
      reportsDirectory: "../build/coverage/core",
    },
  },
  resolve: {
	alias: [{ find: "~", replacement: resolve(__dirname, '../../../src/core/ts/src') }],
  },
});