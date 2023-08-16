import { defineConfig } from "vite";
import { resolve } from "path";
import Icons from "unplugin-icons/vite";
import Vue from "@vitejs/plugin-vue";
import { HstVue } from '@histoire/plugin-vue'

export default defineConfig({
	histoire: {
		setupFile: './src/histoire.setup.ts',
		plugins: [
			HstVue(),
		],
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
				}
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
			{
				label: 'Transparent',
				color: 'transparent'
			},
			{
				label: 'White',
				color: '#fff'
			},
			{
				label: 'Light',
				color: '#f2f4f5'
			},
			{
				label: 'Black',
				color: '#2c2c2c'
			},
		],
		tree: {
			groups: [
				{
					id: 'top-top',
					title: '',
				},
				{
					id: 'top',
					title: '',
				},
				{
					title: 'Components',
					include: file => !file.title.includes('Serialize'),
				},
				{
					id: 'app',
					title: 'App',
				},
				{
					id: 'design-system',
					title: 'Info',
				},
			],
		},
	},
	plugins: [
		Vue({
			reactivityTransform: true
		}),
		Icons(),
	],
	resolve: {
		// die UI-bibliothek und der client haben vue als Dependency. Einmal reicht,
		// sonst gibt es Probleme, die evtl durch andere Build-Methoden korrigiert
		// werden könn. Diese Methode funktioniert aber.
		dedupe: ["vue"],
	},
	build: {
		lib: {
			entry: resolve(__dirname, "src/index.ts"),
			name: "SvwsUI"
		},
		rollupOptions: {
			external: ["vue"],
			output: {
				globals: {
					vue: "Vue"
				}
			}
		}
	}
});
