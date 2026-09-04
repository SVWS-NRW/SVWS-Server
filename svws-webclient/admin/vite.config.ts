/// <reference types="vitest/config" />
import { defineConfig, loadEnv } from "vite";
import Vue from "@vitejs/plugin-vue";
import Components from "unplugin-vue-components/vite";
import Markdown from 'unplugin-vue-markdown/vite';
import { resolve } from "node:path";
import tailwindcss from '@tailwindcss/vite';
import basicSsl from '@vitejs/plugin-basic-ssl';

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd());
	const serverApiUrl = env.VITE_SERVER_API_URL;
	const isProxyEnabled = (typeof serverApiUrl === 'string') && (serverApiUrl.trim() !== "");

	return {
		publicDir: "images",
		test: {},
		base: '/admin/',
		server: {
			port: 3001,
			https: {},
			cors: false,
			...(isProxyEnabled ? {
				proxy: {
					'^/(?!(admin|assets|fonts|img|images|@[^/]+|node_modules|src)(/|$))': {
						target: serverApiUrl,
						changeOrigin: true,
						secure: false,
						bypass: (req) => {
							const url = req.url ?? "";
							const urlPath = url.split('?')[0] ?? "";
							if (urlPath === '/' || urlPath === '/index.html') {
								return url;
							}
							const pathSegments = urlPath.split('/').filter(Boolean);
							if ((pathSegments.length === 1) && pathSegments[0]?.includes('.')) {
								return url;
							}
						},
					},
				},
			} : {}),
		},
		plugins: [
			Vue({ include: [/\.vue$/, /\.md$/] }),
			basicSsl(),
			tailwindcss(),
			Markdown({}),
			Components({
				globs: ["src/**/*.{vue,md}", "src/**/*Props.ts", "../ui/src/**/*.vue", "../ui/src/**/*Props.ts", '!../ui/src/**/*.story.*'],
				types: [],
			}),
		],
		resolve: {
			alias: {
				"@admin": resolve(import.meta.dirname, "./src"),
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
	};
});
