import { defineConfig } from "vite";
import { resolve } from "node:path";
import Components from "unplugin-vue-components/vite";
import Vue from "@vitejs/plugin-vue";
import Markdown from 'unplugin-vue-markdown/vite'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
	resolve: {
		alias: {
			"@icons": resolve(__dirname, "../../node_modules/remixicon/icons"),
			"@json": resolve(__dirname, "../../svws-asd/src/main/resources/de/svws_nrw/asd/types"),
		},
	},
	plugins: [
		Vue({ include: [/\.vue$/, /\.md$/] }),
		tailwindcss(),
		// Die MD Wrapper-Class sollte in Tailwind `prose` und `min-w-full` verwenden, da sonst die Breite nicht passt.
		Markdown({ wrapperClasses: 'prose min-w-full' }),
		Components({ globs: ["src/**/*.{vue,md}", "src/**/*Props.ts", '!src/**/*.story.*'], types: [] }),
	],
	build: {
		outDir: './.histoire/dist',
	},
	define: {
		__VUE_OPTIONS_API__: false,
	},
});
