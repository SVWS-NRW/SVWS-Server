import { defineConfig } from "vite";
import Vue from "@vitejs/plugin-vue";
import Components from "unplugin-vue-components/vite";
import Markdown from 'unplugin-vue-markdown/vite'
import { resolve } from "node:path";
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
	test: {},
	base: '/admin/',
	server: { port: 3001 },
	plugins: [
		Vue({ include: [/\.vue$/, /\.md$/] }),
		tailwindcss(),
		Markdown({}),
		Components({
			globs: ["src/**/*.{vue,md}", "src/**/*Props.ts", "../ui/src/**/*.vue", "../ui/src/**/*Props.ts", '!../ui/src/**/*.story.*'],
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
			"@images": resolve(__dirname, "images"),
			"@icons": resolve(__dirname, "../../node_modules/remixicon/icons"),
		},
	},
	build: {
		outDir: "build/output",
		emptyOutDir: true,
		sourcemap: true,
		minify: false,
		rollupOptions: {
			output: {
				entryFileNames: 'assets/[name].js',
				chunkFileNames: 'assets/[name].js',
				assetFileNames: 'assets/[name].[ext]',
			},
		},
	},
});
