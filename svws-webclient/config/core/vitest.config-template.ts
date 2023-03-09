import { defineConfig } from "vitest/config";
import { resolve } from 'path'

export default defineConfig({
  test: {
    globals: true,
    root: "src",
    reporters: ["default", "junit"],
    outputFile: "@coverage_out@/junit.xml",
    coverage: {
      reportsDirectory: "@coverage_out@",
    },
  },
  resolve: {
	alias: [{ find: "~", replacement: resolve(__dirname, '../../../src/core/ts/src') }],
  },
});