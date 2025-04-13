import { defineConfig } from "vitest/config";
import { resolve } from 'node:path'

export default defineConfig({
	test: {
		globals: true,
		reporters: ["default", "junit"],
		outputFile: "build/test-results/api-test-results.xml",
		globalSetup: ["globalSetup.ts"],
		silent: false,

		coverage: {
			provider: "v8",
			reportsDirectory: "build/test-results",
		},
	},
	resolve: {
		alias: {
			"~": resolve(__dirname, "src"),
			"@core": resolve(__dirname, '../../svws-webclient/core/src/index.ts'),
		},
	},
});
