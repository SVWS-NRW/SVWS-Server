import { defineConfig } from "vitest/config";
import { resolve } from 'node:path'

export default defineConfig({
	test: {
		globals: true,
		testTimeout: 20000,
		reporters: ["default", "junit"],
		outputFile: "build/test-results/api-test-results.xml",
		globalSetup: ["globalSetup.ts"],
		silent: false,
		isolate: false,
		fileParallelism: false,

		coverage: {
			provider: "v8",
			reportsDirectory: "build/test-results",
		},
	},
	resolve: {
		alias: {
			"~": resolve(__dirname, "src"),
		},
	},
});
