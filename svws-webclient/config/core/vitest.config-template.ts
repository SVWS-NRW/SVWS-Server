import { defineConfig } from "vitest/config";
import { resolve } from 'path'

export default defineConfig({
  test: {
    globals: true,
    root: "../../../src",
    reporters: ["default", "junit"],
    outputFile: "../build/coverage/@modulename@/junit.xml",
    coverage: {
      provider: "v8",
      reportsDirectory: "../build/coverage/@modulename@",
    },
	exclude: ["@vitest_excludes@"]
  },
  resolve: {
	alias: [{ find: "~", replacement: resolve(__dirname, '../../../src/core/ts/src') }],
  },
});
