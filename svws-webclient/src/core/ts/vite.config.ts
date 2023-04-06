import { defineConfig } from "vite";
import { resolve } from "path";
import typescript from '@rollup/plugin-typescript';


export default defineConfig({
	plugins: [],
	build: {
		lib: {
			entry: resolve(__dirname, "src/index.ts"),
			name: "SvwsCore"
		},
		// muss wegen der impliziten `type`-Imports verwendet werden, esbuild funktioniert so nicht
		rollupOptions: {
			plugins: [typescript()]
		}
	},
});
