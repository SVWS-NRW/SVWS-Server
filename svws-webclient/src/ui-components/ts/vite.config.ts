import { defineConfig } from "vite";
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
		Vue({
			reactivityTransform: true
		}),
		Components({
			globs: ["src/components/**/!(*story.vue)*.vue"],
			dirs: ['src/components'],
			resolvers: [IconsResolver()]
		}),
		Icons(),
	],
	resolve: {
		// die UI-bibliothek und der client haben vue als Dependency. Einmal reicht,
		// sonst gibt es Probleme, die evtl durch andere Build-Methoden korrigiert
		// werden könn. Diese Methode funktioniert aber.
		dedupe: ["vue"],
	},
	build: {
		lib: {
			entry: resolve(__dirname, "src/index.ts"),
			name: "SvwsUI"
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
