import { defineConfig } from "vite";
import Vue from "@vitejs/plugin-vue";
import Components from "unplugin-vue-components/vite";
import Markdown from 'unplugin-vue-markdown/vite'
import { resolve } from "path";

export default defineConfig({
	server: {
		port: 3003,
		cors: false,
	},
	plugins: [
		Vue({ include: [/\.vue$/, /\.md$/] }),
		Markdown({}),
		Components({
			globs: ["src/**/*.vue", "src/**/*Props.ts", "../ui/src/**/*.vue", "../ui/src/**/*Props.ts",'!../ui/src/**/*.story.*'],
			types: [],
		}),
	],
	resolve: {
		alias: {
			// Importe können durch ein vorangestelltes `~` absolut gefunden werden
			"~": resolve(__dirname, "src"),
			"@ui": resolve(__dirname, '../ui/src/'),
			"@core": resolve(__dirname, '../core/src/'),
			"@json": resolve(__dirname, "../../svws-asd/src/main/resources/de/svws_nrw/asd/types"),
		},
	},
	build: {
		outDir: "build/output/public",
		emptyOutDir: true,
	},
});
