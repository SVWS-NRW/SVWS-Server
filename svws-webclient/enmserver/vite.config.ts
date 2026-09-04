import { defineConfig, loadEnv } from "vite";
import Vue from "@vitejs/plugin-vue";
import Components from "unplugin-vue-components/vite";
import Markdown from 'unplugin-vue-markdown/vite';
import { resolve } from "node:path";
import tailwindcss from '@tailwindcss/vite';
import basicSsl from '@vitejs/plugin-basic-ssl';

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd());
	const phpApiUrl = env.VITE_PHP_API_URL;
	const isProxyEnabled = (typeof phpApiUrl === 'string') && (phpApiUrl.trim() !== "");

	return {
		server: {
			port: 3003,
			https: {},
			cors: false,
			...(isProxyEnabled ? {
				proxy: {
					'^/(api|oauth)': {
						target: phpApiUrl,
						changeOrigin: true,
						secure: false,
					},
				},
			} : {}),
		},
		base: "./", // relateiven Base-Pfad setzen, damit man den Client auch in Unterverzeichnissen hosten kann
		plugins: [
			Vue({ include: [/\.vue$/, /\.md$/] }),
			basicSsl(),
			tailwindcss(),
			Markdown({}),
			Components({
				globs: ["src/**/*.{vue,md}", "src/**/*Props.ts", "../ui/src/**/*.{vue,md}", "../ui/src/**/*Props.ts", '!../ui/src/**/*.story.*'],
				types: [],
			}),
		],
		resolve: {
			alias: {
				// Importe können durch ein vorangestelltes `~` absolut gefunden werden
				"@wenom": resolve(import.meta.dirname, './src'),
				"@ui": resolve(import.meta.dirname, '../ui/src/'),
				"@core": resolve(import.meta.dirname, '../core/src'),
				"@json": resolve(import.meta.dirname, "../../svws-asd/src/main/resources/de/svws_nrw/asd/types"),
				"@images": resolve(import.meta.dirname, "images"),
				"@icons": resolve(import.meta.dirname, "../../node_modules/remixicon/icons"),
				"@version": resolve(import.meta.dirname, "./version.ts"),
				"@githash": resolve(import.meta.dirname, "./githash.ts"),
			},
		},
		build: {
			outDir: "build/output/public",
			emptyOutDir: true,
		},
	};
});
