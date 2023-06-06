import { defineConfig } from "vite";
import { resolve } from "path";
import Icons from "unplugin-icons/vite";
import IconsResolver from "unplugin-icons/resolver";
import Components from "unplugin-vue-components/vite";
import Vue from "@vitejs/plugin-vue";
import Markdown from "vite-plugin-vue-markdown";
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
		Markdown(),
		Components({
			resolvers: [IconsResolver()],
			dirs: [
				'src/components',
				resolve(__dirname, '../../ui/ts/src/components'),
			],
			extensions: ['vue', 'md'],
			include: [/\.vue$/, /\.vue\?vue/, /\.md$/],
		}),
		Icons(),
	],
	resolve: {
		alias: {
			"@core": resolve(__dirname, '../../core/ts/src/index.ts')
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
