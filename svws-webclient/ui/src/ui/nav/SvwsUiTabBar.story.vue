<template>
	<Story title="Tab Bar" id="svws-ui-tab-bar" group="app" icon="ri:route-line" :layout="{type: 'grid', width: '90%'}">
		<Variant title="Default" id="Default">
			<svws-ui-header><span class="svws-headline">Headline</span><br><span class="svws-subline">Subtitle</span></svws-ui-header>
			<svws-ui-tab-bar :tab-manager>
				Route: {{ selectedRoute.text }}
			</svws-ui-tab-bar>
			<div class="h-32" />
		</Variant>

		<Variant title="<svws-ui-tab-bar-vertical>" id="<svws-ui-tab-bar-vertical>">
			<svws-ui-tab-bar-vertical :tab-manager="tabManagerVertical">
				Route: {{ selectedRouteVertical.text }}
			</svws-ui-tab-bar-vertical>
		</Variant>
	</Story>
</template>

<script lang="ts" setup>

	import { ref } from "vue";
	import type { TabData } from "./TabData";
	import { TabManager } from "./TabManager";

	const tabs = [
		{ name: "home", text: "Home" },
		{ name: "about", text: "About" },
		{ name: "settings", text: "Settings" },
		{ name: "hidden", text: "Hidden", hide: true },
		{ name: "link1", text: "Link mit einem sehr langen Titel" },
		{ name: "link2", text: "Link mit einem sehr langen Titel 2" },
		{ name: "link3", text: "Link mit einem sehr langen Titel 3" },
	];
	async function setTab(tab : TabData) {
		selectedRoute.value = tab;
	}
	const tabManager = () => new TabManager(tabs, tabs[0], setTab);
	const selectedRoute = ref(tabManager().tab);

	const tabsVertical = [
		{ name: "home", text: "Home" },
		{ name: "about", text: "About" },
		{ name: "settings", text: "Settings" },
		{ name: "hidden", text: "Hidden", hide: true },
		{ name: "link1", text: "Link1" },
		{ name: "link2", text: "Link2" },
		{ name: "link3", text: "Link3" },
	];
	async function setTabVertical(tab : TabData) {
		selectedRouteVertical.value = tab;
	}
	const tabManagerVertical = () => new TabManager(tabsVertical, tabsVertical[0], setTabVertical);
	const selectedRouteVertical = ref(tabManagerVertical().tab);

</script>
