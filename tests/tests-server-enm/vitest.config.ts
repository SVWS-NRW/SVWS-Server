import { defineConfig } from "vitest/config";
import { resolve } from 'node:path';

export default defineConfig({
	test: {
		globals: true,
		testTimeout: 20000,
		outputFile: "./build/test-results/api-test-results.xml",
		reporters: ["default", "junit"],
		silent: false,
	},
	resolve: {
		alias: {
			"~": resolve(__dirname, "src"),
			"@core": resolve(__dirname, '../../svws-webclient/core/src/index.ts'),
		},
	},
});
