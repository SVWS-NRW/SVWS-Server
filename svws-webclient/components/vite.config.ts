import { defineConfig } from "vite";
import { resolve } from "path";
import Components from "unplugin-vue-components/vite";
import Vue from "@vitejs/plugin-vue";
import Markdown from 'unplugin-vue-markdown/vite'
import { HstVue } from '@histoire/plugin-vue'

export default defineConfig({
	histoire: {
		setupFile: './src/histoire.setup.ts',
		sandboxDarkClass: 'theme-dark',
		plugins: [
			HstVue(),
		],
	},
	plugins: [
		Vue({
			include: [/\.vue$/, /\.md$/]
		}),
		Markdown({}),
		Components({
			dirs: [
				'src/components',
				resolve(__dirname, '../ui/src/components'),
			],
			extensions: ['vue', 'md'],
			include: [/\.vue$/, /\.vue\?vue/, /\.md$/],
		}),
	],
	resolve: {
		alias: {
			"@core": resolve(__dirname, '../core/src/index.ts'),
			// darf wegen konkurrierendem CSS so nicht verwendet werden, absolute Pfade verwenden!
			// "@ui": resolve(__dirname, '../ui/src/index.ts')
		}
	},
	build: {
		lib: {
			entry: resolve(__dirname, "src/index.ts"),
			name: "SvwsComponents"
		},
		rollupOptions: {
			external: ["vue"],
			output: {
				globals: {
					vue: "Vue"
				}
			}
		}
	}
});
