import { defineConfig } from "vite";
import { resolve } from "path";
import Icons from "unplugin-icons/vite";
import IconsResolver from "unplugin-icons/resolver";
import Components from "unplugin-vue-components/vite";
import AutoImport from "unplugin-auto-import/vite";
import Vue from "@vitejs/plugin-vue";

export default defineConfig({
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
		AutoImport({
			dts: true,
			eslintrc: {
				enabled: true,
			},
			include: [
				/\.[tj]sx?$/, // .ts, .tsx, .js, .jsx
				/\.vue$/, /\.vue\?vue/, // .vue
			],
			imports: [
				"vue"
			],
			dirs: [
				'./src/composables'
			]
		})
	],
	resolve: {
		// die UI-bibliothek und der client haben vue als Dependency. Einmal reicht,
		// sonst gibt es Probleme, die evtl durch andere Build-Methoden korrigiert
		// werden könn. Diese Methode funktioniert aber.
		dedupe: ["vue"],
		alias: {
			// Importe können durch ein vorangestelltes `~` absolut gefunden werden
			"~": resolve(__dirname, "./src/components")
		}
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
