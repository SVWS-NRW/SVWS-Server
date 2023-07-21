import { defineConfig } from "vitest/config";
import { resolve } from 'path'

export default defineConfig({
  test: {
    globals: true,
    root: "../../../src",
    reporters: ["default", "junit"],
    outputFile: "../build/coverage/api-test/junit.xml",
    coverage: {
      provider: "v8",
      reportsDirectory: "../build/coverage/api-test",
    },
	exclude: ["core-test","client-test","browser-test"]
  },
  resolve: {
	alias: [{ find: "~", replacement: resolve(__dirname, '../../../src/core/ts/src') }],
  },
});
