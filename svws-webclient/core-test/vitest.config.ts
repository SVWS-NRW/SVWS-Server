import { defineConfig } from "vitest/config";
import { resolve } from 'path'

export default defineConfig({
	test: {
		globals: true,
		reporters: ["default", "junit"],
		outputFile: "build/coverage/junit.xml",
		setupFiles: "setup.ts",
		coverage: {
			provider: "v8",
			reportsDirectory: "build/coverage",
		},
		include: [
			"src/**/*.test.ts",
		]
	},
	resolve: {
		alias: {
			"~": resolve(__dirname, "src"),
			"@core": resolve(__dirname, '../core/src/index.ts'),
		},
	},
});
