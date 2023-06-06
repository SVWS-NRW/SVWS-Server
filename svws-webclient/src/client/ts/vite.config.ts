import { defineConfig } from "vitest/config";
import Vue from "@vitejs/plugin-vue";
import Icons from "unplugin-icons/vite";
import IconsResolver from "unplugin-icons/resolver";
import Components from "unplugin-vue-components/vite";
import Markdown from "vite-plugin-vue-markdown";
import { resolve } from "path";

export default defineConfig({
	test: {},
	server: { port: 3000 },
	plugins: [
		Vue({
			include: [/\.vue$/, /\.md$/]
		}),
		Markdown(),
		Components({
			resolvers: [IconsResolver()],
			dirs: [
				'src/components',
				resolve(__dirname, '../../ui/ts/src/components'),
				resolve(__dirname, '../../components/ts/src/components'),
			],
			extensions: ['vue', 'md'],
			include: [/\.vue$/, /\.vue\?vue/, /\.md$/],
		}),
		Icons(),
	],
	resolve: {
		alias: {
			// Importe können durch ein vorangestelltes `~` absolut gefunden werden
			"~": resolve(__dirname, "src"),
			"@comp": resolve(__dirname, '../../components/ts/src/index.ts'),
			"@ui": resolve(__dirname, '../../ui/ts/src/index.ts'),
			"@core": resolve(__dirname, '../../core/ts/src/index.ts')
		}
	},
	build: {
		outDir: "../../../build/output",
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
