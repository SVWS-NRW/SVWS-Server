import { defineConfig } from "vitest/config";
import BetterDefine from '@vue-macros/better-define'
import Vue from "@vitejs/plugin-vue";
import Icons from "unplugin-icons/vite";
import IconsResolver from "unplugin-icons/resolver";
import Components from "unplugin-vue-components/vite";
import Markdown from "vite-plugin-md";
import { resolve } from "path";
import { ComponentResolver } from 'unplugin-vue-components/types';

const dev = process.env.NODE_ENV === 'development';
const svwsUi = dev ? resolve(__dirname, "../../ui-components/ts/src/index.ts") : '@svws-nrw/svws-ui';
const svwsComponents = [ 'src/components' ];
const svwsResolvers: ComponentResolver[] = [ IconsResolver() ];
if (dev) {
	svwsComponents.push(resolve(__dirname, '../../ui-components/ts/src/components'));
} else {
	svwsResolvers.push({
		type: 'component', resolve: (name: string) => { if (name.startsWith('SvwsUi') ) { return {	name,	from: svwsUi } } }
	});
}

export default defineConfig({
	test: {},
	server: { port: 3000 },
	plugins: [
		BetterDefine.vite(),
		Vue({
			include: [/\.vue$/, /\.md$/]
		}),
		Markdown(),
		Components({
			resolvers: svwsResolvers,
			dirs: svwsComponents,
			extensions: ['vue', 'md'],
			include: [/\.vue$/, /\.vue\?vue/, /\.md$/],
		}),
		Icons(),
	],
	resolve: {
		alias: {
			// Importe können durch ein vorangestelltes `~` absolut gefunden werden
			"~": resolve(__dirname, "src"),
			'@svws-nrw/svws-ui': svwsUi
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
