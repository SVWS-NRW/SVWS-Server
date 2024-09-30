<script lang="ts" setup>

	import { ref } from "vue";
	import type { AuswahlChildData } from "../../types";

	const routes = [
		{ name: "home", text: "Home" },
		{ name: "about", text: "About" },
		{ name: "settings", text: "Settings" },
		{ name: "hidden", text: "Hidden" },
		{ name: "link1", text: "Link mit einem sehr langen Titel" },
		{ name: "link2", text: "Link mit einem sehr langen Titel 2" },
		{ name: "link3", text: "Link mit einem sehr langen Titel 3" }
	];

	const routesVertical = [
		{ name: "home", text: "Home" },
		{ name: "about", text: "About" },
		{ name: "settings", text: "Settings" },
		{ name: "hidden", text: "Hidden" },
		{ name: "link1", text: "Link1" },
		{ name: "link2", text: "Link2" },
		{ name: "link3", text: "Link3" }
	];

	const hidden = ref([false, false, false, true]);
	const selectedRoute = ref(routes[0]);
	async function setTab(tab : AuswahlChildData) {
		selectedRoute.value = tab;
	}

</script>

<template>
	<Story title="Router Tab Bar" id="svws-ui-router-tab-bar" group="app" icon="ri:route-line" :layout="{type: 'grid', width: '90%'}">
		<Variant title="Default" id="Default">
			<svws-ui-header><span>Headline</span><br><span class="opacity-50">Subtitle</span></svws-ui-header>
			<svws-ui-router-tab-bar :tabs="routes" :hidden="hidden" :tab="selectedRoute" :set-tab="setTab">
				Route: {{ selectedRoute.text }}
			</svws-ui-router-tab-bar>
			<div class="h-32" />
		</Variant>

		<Variant title="<svws-ui-router-vertical-tab-bar>" id="<svws-ui-router-vertical-tab-bar>">
			<svws-ui-router-vertical-tab-bar :tabs="routesVertical" :hidden="hidden" :tab="selectedRoute" :set-tab="setTab">
				Route: {{ selectedRoute.text }}
			</svws-ui-router-vertical-tab-bar>
		</Variant>
	</Story>
</template>
