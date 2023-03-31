import { defineConfig } from "vite";
import BetterDefine from '@vue-macros/better-define'
import { resolve } from "path";
import Icons from "unplugin-icons/vite";
import IconsResolver from "unplugin-icons/resolver";
import Components from "unplugin-vue-components/vite";
import Vue from "@vitejs/plugin-vue";
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
		BetterDefine.vite(),
		Vue(),
		Components({
			resolvers: [IconsResolver()],
			dirs: [ 'src/components', ],
			globs: ["src/components/**/!(*story.vue)*.vue"],
			extensions: ['vue', 'md'],
			include: [/\.vue$/, /\.vue\?vue/, /\.md$/],
		}),
		Icons(),
	],
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
