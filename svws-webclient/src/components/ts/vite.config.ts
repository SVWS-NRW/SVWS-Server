import { defineConfig } from "vite";
import { resolve } from "path";
import Icons from "unplugin-icons/vite";
import IconsResolver from "unplugin-icons/resolver";
import Components from "unplugin-vue-components/vite";
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
		Components({
			globs: ["src/components/**/!(*story.vue)*.vue"],
			dirs: ['src/components'],
			resolvers: [IconsResolver()]
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
