import { resolve } from 'node:path';
import { defineConfig } from "vitest/config";

export default defineConfig({
	test: {
		globals: true,
		testTimeout: 20000,
		setupFiles: ['./setup-tls.ts', './setup-coretypes.ts'],
		outputFile: "./build/test-results/api-test-results.xml",
		reporters: ["default", "junit"],
		silent: false,
	},
	resolve: {
		alias: {
			"@testUtils": resolve(import.meta.dirname, "../utils"),
			"@testWenom": resolve(import.meta.dirname, "./tests"),
			"@core": resolve(import.meta.dirname, '../../svws-webclient/core/src'),
			"@wenom": resolve(import.meta.dirname, '../../svws-webclient/enmserver/src'),
		},
	},
});
