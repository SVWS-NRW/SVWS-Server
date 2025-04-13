import { defineConfig } from "vitest/config";
import { resolve } from 'node:path'

export default defineConfig({
	test: {
		globals: true,
		reporters: ["default", "junit"],
		outputFile: "build/coverage/junit.xml",
		setupFiles: "src/test/setup.ts",
		coverage: {
			provider: "v8",
			reportsDirectory: "build/coverage",
		},
		include: [
			"src/**/*.test.ts",
		],
	},
	resolve: {
		alias: {
			"@json": resolve(__dirname, "../../svws-asd/src/main/resources/de/svws_nrw/asd/types"),
		},
	},
});
