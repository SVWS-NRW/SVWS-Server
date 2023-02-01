import { defineConfig } from "vitest/config";
import Vue from "@vitejs/plugin-vue";
import Icons from "unplugin-icons/vite";
import IconsResolver from "unplugin-icons/resolver";
import Components from "unplugin-vue-components/vite";
import Markdown from "vite-plugin-md";
import { resolve } from "path";
import { ComponentResolver } from 'unplugin-vue-components/types';

export default defineConfig({
	test: {},
	server: { port: 3000 },
	plugins: [
		Vue({
			include: [/\.vue$/, /\.md$/]
		}),
		Markdown(),
		Components({
			resolvers: [IconsResolver(), SvwsUiResolver()],
			dirs: ['src/components'],
			extensions: ['vue', 'md'],
			include: [/\.vue$/, /\.vue\?vue/, /\.md$/],
		    types: [
				{
					"from": "@svws-nrw/svws-ui",
					names: SVWSComponentNames()
				}
			]
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

function SvwsUiResolver (): ComponentResolver {
	return {
		type: 'component',
		resolve: (name: string) => {
			// name = pascalCase(name)
			if (name.startsWith('SvwsUi') ) {
				return {
					name,
					from: "@svws-nrw/svws-ui"
				}
			}
		}
	}
}

function SVWSComponentNames() {
	return [
		"SvwsUiButton",
		"SvwsUiCheckbox",
		"SvwsUiDropdown",
		"SvwsUiDropdownItem",
		"SvwsUiDropdownWithAction",
		"SvwsUiMultiSelect",
		"SvwsUiProgressBar",
		"SvwsUiRadioGroup",
		"SvwsUiRadioOption",
		"SvwsUiSelectInput",
		"SvwsUiTabBar",
		"SvwsUiTabButton",
		"SvwsUiTabPanel",
		"SvwsUiTextareaInput",
		"SvwsUiTextInput",
		"SvwsUiToggle",
		"SvwsUiAvatar",
		"SvwsUiContentCard",
		"SvwsUiHeader",
		"SvwsUiIcon",
		"SvwsUiModal",
		"SvwsUiOverlay",
		"SvwsUiAppLayout",
		"SvwsUiSidebarMenu",
		"SvwsUiSidebarMenuHeader",
		"SvwsUiSidebarMenuItem",
		"SvwsUiSecondaryMenu",
		"SvwsUiBadge",
		"SvwsUiTooltip",
		"SvwsUiPopover",
		"SvwsUiTable",
		"SvwsUiRouterTabBar",
		"SvwsUiRouterTabBarButton",
		"SvwsUiRouterVerticalTabBar",
		"SvwsUiDragData",
		"SvwsUiDropData"
	]
}