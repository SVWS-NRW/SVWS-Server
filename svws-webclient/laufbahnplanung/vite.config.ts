import { defineConfig } from "vitest/config";
import Vue from "@vitejs/plugin-vue";
import Components from "unplugin-vue-components/vite";
import Markdown from 'unplugin-vue-markdown/vite'
import { resolve } from "path";

export default defineConfig({
	test: {},
	server: { port: 3000 },
	base: '',
	plugins: [
		Vue({ include: [/\.vue$/, /\.md$/] }),
		Markdown({}),
		Components({
			dirs: [
				'src/components',
				resolve(__dirname, '../ui/src/components'),
				resolve(__dirname, '../components/src/components'),
			],
			extensions: ['vue', 'md'],
			include: [/\.vue$/, /\.vue\?vue/, /\.md$/],
		}),
	],
	resolve: {
		alias: {
			// Importe können durch ein vorangestelltes `~` absolut gefunden werden
			"~": resolve(__dirname, "src"),
			"@json": resolve(__dirname, "../../svws-asd/src/main/resources/de/svws_nrw/asd/types"),
		},
	},
	build: {
		outDir: "build/output",
		emptyOutDir: true,
		sourcemap: true,
		minify: true,
		commonjsOptions: {},
		rollupOptions: {
			output: {
				entryFileNames: 'assets/[name].js',
				chunkFileNames: 'assets/[name].js',
				assetFileNames: 'assets/[name].[ext]',
			},
		},
	},
});
