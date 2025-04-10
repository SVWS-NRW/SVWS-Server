import {defineConfig} from "vitest/config";
import {resolve} from 'node:path'

export default defineConfig({
	test: {
		globals: true,
		testTimeout: 20000,
		reporters: ["default", "junit"],
		silent: false,
		coverage: {
			provider: "v8",
		},
	},
	resolve: {
		alias: {
			"~": resolve(__dirname, "src"),
			"@core": resolve(__dirname, '../../svws-webclient/core/src/index.ts'),
		},
	},
});
