/// <reference types="histoire" />

import { defineConfig, searchForWorkspaceRoot } from "vite";
import { resolve } from "path";
import Components from "unplugin-vue-components/vite";
import Vue from "@vitejs/plugin-vue";
import { HstVue } from '@histoire/plugin-vue'

export default defineConfig({
	server: { fs: { allow: [searchForWorkspaceRoot(process.cwd())] } },
	test: {
		environment: "happy-dom",
		reporters: ["default", "junit", "verbose"],
		outputFile: { junit: "build/testresults/junit.xml" },
		include: [ "src/**/*.test.ts" ],
	},
	histoire: {
		setupFile: './histoire.setup.ts',
		plugins: [ HstVue() ],
		theme: {
			title: 'SVWS UI',
			colors: {
				primary: {
					'50': '#f1f7fe',
					'100': '#e3eefb',
					'200': '#c0dcf7',
					'300': '#88c0f1',
					'400': '#4ba1e7',
					'500': '#2285d5',
					'600': '#1467b5',
					'700': '#115393',
					'800': '#12477a',
					'900': '#153d65',
				},
			},
			darkClass: 'dark',
			hideColorSchemeSwitch: false,
			defaultColorScheme: 'light',
			storeColorScheme: true,
			logo: {
				square: './src/assets/img/favicon.svg',
				light: './src/assets/img/histoire-svws.svg',
				dark: './src/assets/img/histoire-svws-dark.svg',
			},
		},
		backgroundPresets: [
			{ label: 'Transparent', color: 'transparent' },
			{ label: 'White', color: '#fff' },
			{ label: 'Light', color: '#f2f4f5' },
			{ label: 'Black', color: '#2c2c2c' },
		],
		tree: {
			groups: [
				{ id: 'top-top', title: '' },
				{ id: 'top', title: '' },
				{ title: 'Components', include: file => !file.title.includes('Serialize') },
				{ id: 'app', title: 'App' },
				{ id: 'design-system', title: 'Info' },
			],
		},
	},
	plugins: [
		Vue(),
		Components({ globs: ["src/**/!(*story.vue)*.vue", "src/**/*Props.ts"] }),
	],
	build: {
		lib: {
			entry: resolve(__dirname, "src/index.ts"),
			name: "SvwsUI",
		},
		rollupOptions: {
			external: ["vue"],
			output: { globals: { vue: "Vue" } },
		},
	},
});
