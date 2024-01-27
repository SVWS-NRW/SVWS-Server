import { defineConfig } from "vitest/config";
import Vue from "@vitejs/plugin-vue";
import Icons from "unplugin-icons/vite";
import IconsResolver from "unplugin-icons/resolver";
import Components from "unplugin-vue-components/vite";
import Markdown from 'unplugin-vue-markdown/vite'
import { resolve } from "path";
import { comlink } from 'vite-plugin-comlink'

export default defineConfig({
	test: {},
	server: { port: 3000 },
	plugins: [
		comlink(),
		Vue({
			include: [/\.vue$/, /\.md$/]
		}),
		Markdown({}),
		Components({
			resolvers: [IconsResolver()],
			dirs: [
				'src/components',
				resolve(__dirname, '../ui/src/components'),
				resolve(__dirname, '../components/src/components'),
			],
			extensions: ['vue', 'md'],
			include: [/\.vue$/, /\.vue\?vue/, /\.md$/],
		}),
		Icons(),
	],
	worker: {
		plugins: () => [ comlink() ]
	},
	resolve: {
		alias: {
			// Importe können durch ein vorangestelltes `~` absolut gefunden werden
			"~": resolve(__dirname, "src"),
			"@comp": resolve(__dirname, '../components/src/index.ts'),
			"@ui": resolve(__dirname, '../ui/src/index.ts'),
			"@core": resolve(__dirname, '../core/src/index.ts')
		}
	},
	build: {
		outDir: "build/output",
		emptyOutDir: true,
		sourcemap: true,
		minify: true,
		commonjsOptions: {},
		rollupOptions: {
			output: {
				entryFileNames: `assets/[name].js`,
				chunkFileNames: `assets/[name].js`,
				assetFileNames: `assets/[name].[ext]`
			}
		}
	}
});
