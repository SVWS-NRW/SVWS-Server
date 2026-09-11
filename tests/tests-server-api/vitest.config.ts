import { resolve } from 'node:path';
import { defineConfig } from "vitest/config";

export default defineConfig({
	test: {
		globals: true,
		testTimeout: 20000,
		reporters: ["default", "junit"],
		outputFile: "./build/test-results/api-test-results.xml",
		globalSetup: ["globalSetup.ts"],
		silent: false,
		isolate: false,
		fileParallelism: false,
	},
	resolve: {
		alias: {
			"@testUtils": resolve(import.meta.dirname, "../utils"),
			"@testApi": resolve(import.meta.dirname, "./tests"),
			"@core": resolve(import.meta.dirname, '../../svws-webclient/core/src'),
		},
	},
});
