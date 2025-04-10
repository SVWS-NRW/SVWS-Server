import {defineConfig} from "vitest/config";
import {resolve} from 'node:path'

export default defineConfig({
	test: {
		globals: true,
		reporters: ["default", "junit"],
		outputFile: "build/test-results/api-test-results.xml",
		silent: false,
		coverage: {
			provider: "v8",
			reportsDirectory: "build/test-results",
		},
		testTimeout: 20_000,
	},
	resolve: {
		alias: {
			"~": resolve(__dirname, "src"),
		},
	},
});
