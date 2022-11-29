import { defineConfig } from "vitest/config";
import Vue from "@vitejs/plugin-vue";
import Icons from "unplugin-icons/vite";
import IconsResolver from "unplugin-icons/resolver";
import Components from "unplugin-vue-components/vite";
import Markdown from "vite-plugin-md";
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
			dirs: ['src/components'],
		}),
		Icons()
	],
	resolve: {
		// die UI-bibliothek und der client haben vue als Dependency. Einmal reicht,
		// sonst gibt es Probleme, die evtl durch andere Build-Methoden korrigiert
		// werden könn. Diese Methode funktioniert aber.
		dedupe: ["vue"],
		alias: {
			// Importe können durch ein vorangestelltes `~` absolut gefunden werden
			"~": resolve(__dirname, "src")
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
