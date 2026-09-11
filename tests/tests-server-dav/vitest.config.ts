import { resolve } from 'node:path';
import { defineConfig } from "vitest/config";

export default defineConfig({
	test: {
		globals: true,
		reporters: ["default", "junit"],
		outputFile: "./build/test-results/api-test-results.xml",
		silent: false,
		testTimeout: 20_000,
	},
	resolve: {
		alias: {
			"@testUtils": resolve(import.meta.dirname, "../utils"),
			"@testDav": resolve(import.meta.dirname, "./tests"),
			"@core": resolve(import.meta.dirname, '../../svws-webclient/core/src'),
		},
	},
});
