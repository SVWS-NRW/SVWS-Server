import { defineConfig } from "vitest/config";
import { resolve } from 'path'

export default defineConfig({
  test: {
    globals: true,
    root: "src",
    reporters: ["default", "junit"],
    outputFile: "../../../build/coverage/svws-core-ts/junit.xml",
    coverage: {
      reportsDirectory: "../../../build/coverage/svws-core-ts",
    },
  },
  resolve: {
	alias: [{ find: "~", replacement: resolve(__dirname, '../../../src/ts-lib/ts/src') }],
  },
});