import { resolve } from "node:path";
import { defineConfig } from "vitest/config";

export default defineConfig({
	plugins: [],
	build: {
		lib: {
			entry: resolve(import.meta.dirname, "src/index.ts"),
			formats: ['es'],
			name: "SvwsCore",
		},
	},
	test: {
		globals: true,
		reporters: ["default", "junit"],
		outputFile: "build/coverage/junit.xml",
		setupFiles: "test/setup.ts",
		coverage: {
			provider: "v8",
			reportsDirectory: "build/coverage",
		},
		include: [
			"test/**/*.test.ts",
		],
	},
	resolve: {
		alias: {
			"@json": resolve(import.meta.dirname, "../../svws-asd/src/main/resources/de/svws_nrw/asd/types"),
			"@core": resolve(import.meta.dirname, './src'),
		},
	},
});
