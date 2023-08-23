import { defineConfig } from "vitest/config";
import { resolve } from 'path'

export default defineConfig({
	test: {
		globals: true,
		reporters: ["default", "junit"],
		outputFile: "../../../build/coverage/core/junit.xml",
		coverage: {
			provider: "v8",
			reportsDirectory: "../../../build/coverage/core",
			all: true,
		},
		include: [
			"src/**/*.test.ts",
		]
	},
	resolve: {
		alias: {
			"~": resolve(__dirname, "src"),
			"@transpiled": resolve(__dirname, '../../../build/tests/ts/index.ts'),
		},
	},
});
