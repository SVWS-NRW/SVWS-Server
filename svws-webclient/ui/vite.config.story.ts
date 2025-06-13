import { defineConfig } from "vite";
import { resolve } from "node:path";
import Components from "unplugin-vue-components/vite";
import Vue from "@vitejs/plugin-vue";
import Markdown from 'unplugin-vue-markdown/vite'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
	resolve: {
		alias: {
			"~": resolve('./src'),
			"@icons": resolve(__dirname, "../../node_modules/remixicon/icons"),
			"@json": resolve(__dirname, "../../svws-asd/src/main/resources/de/svws_nrw/asd/types"),
		},
	},
	plugins: [
		Vue({ include: [/\.vue$/, /\.md$/] }),
		tailwindcss(),
		Markdown({wrapperClasses: 'prose'}),
		Components({ globs: ["src/**/*.{vue,md}", "src/**/*Props.ts", '!src/**/*.story.*'], types: [] }),
	],
	build: {
		outDir: './.histoire/dist',
	},
});
