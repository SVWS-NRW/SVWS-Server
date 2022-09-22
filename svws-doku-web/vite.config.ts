import { defineConfig } from 'vite'
import Vue from "@vitejs/plugin-vue";
import { resolve } from "path";

export default defineConfig({
	plugins: [
		Vue({
      include: [/\.vue$/, /\.md$/],
    }),
	],
	resolve: {
		// die UI-bibliothek und der client haben vue als Dependency. Einmal reicht,
		// sonst gibt es Probleme, die evtl durch andere Build-Methoden korrigiert
		// werden könn. Diese Methode funktioniert aber.
		dedupe: ["vue"],
		alias: {
			// Importe können durch ein vorangestelltes `~` absolut gefunden werden
			"~": resolve(__dirname, "./src"),
			"@svws-nrw/svws-ui": resolve(__dirname, "../svws-ui-components"),
			"@svws-nrw/svws-api-ts": resolve(__dirname, "../svws-ts-lib/src/main/ts")
		}
	},
	build: {
		outDir: "build/output",
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
