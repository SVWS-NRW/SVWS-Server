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
		rollupOptions: {
			plugins: [typescript({
				tsconfig: resolve(__dirname, 'tsconfig.build.json')
			}
			)]
		}
	},
});
