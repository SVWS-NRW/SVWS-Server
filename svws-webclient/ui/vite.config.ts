/// <reference types="vitest/config" />
import { defineConfig, searchForWorkspaceRoot } from "vite";
import { resolve } from "node:path";
import Components from "unplugin-vue-components/vite";
import Vue from "@vitejs/plugin-vue";
import Markdown from 'unplugin-vue-markdown/vite';
import tailwindcss from '@tailwindcss/vite';

export default defineConfig({
	server: { fs: { allow: [searchForWorkspaceRoot(process.cwd())] } },
	test: {
		environment: "happy-dom",
		reporters: ["default", "junit", "verbose"],
		outputFile: { junit: "build/testresults/junit.xml" },
		include: ["test/**/*.test.ts"],
	},
	resolve: {
		alias: {
			"@ui": resolve(import.meta.dirname, './src'),
			"@icons": resolve(__dirname, "../../node_modules/remixicon/icons"),
			"@core": resolve(import.meta.dirname, '../core/src'),
			"@json": resolve(__dirname, "../../svws-asd/src/main/resources/de/svws_nrw/asd/types"),
		},
	},
	plugins: [
		Vue({ include: [/\.vue$/, /\.md$/] }),
		tailwindcss(),
		Markdown({}),
		Components({ globs: ["src/**/*.{vue,md}", "src/**/*Props.ts", '!src/**/*.story.*'], types: [] }),
	],
	build: {
		lib: {
			entry: resolve(__dirname, "src/index.ts"),
			formats: ['es'],
			name: "SvwsUI",
		},
		rollupOptions: {
			external: ["vue"],
			output: { globals: { vue: "Vue" } },
		},
	},
	define: { __VUE_OPTIONS_API__: false },
});
