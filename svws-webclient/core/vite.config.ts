import { defineConfig } from "vite";
import { resolve } from "node:path";

export default defineConfig({
	plugins: [],
	build: {
		lib: {
			entry: resolve(__dirname, "src/index.ts"),
			formats: ['es'],
			name: "SvwsCore",
		},
	},
});
