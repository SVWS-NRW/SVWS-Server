import { resolve } from 'node:path';
import { defineConfig } from "vitest/config";

export default defineConfig({
	test: {
		globals: true,
		pool: "threads",
		reporters: ["default", "junit"],
		outputFile: "../../../build/coverage/junit.xml",
		coverage: {
			provider: "v8",
			reportsDirectory: "../../../build/coverage",
		},
		include: [
			"src/**/*.test.ts",
		],
	},
	resolve: {
		alias: {
			"@transpile": resolve(import.meta.dirname, "./src"),
			"@core": resolve(import.meta.dirname, '../../../../svws-webclient/core/src'),
		},
	},
});
