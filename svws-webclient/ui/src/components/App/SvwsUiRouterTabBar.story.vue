<script lang="ts" setup>
	import { ref, h } from "vue";

	const routes = [
		{ path: "/", name: "home", component: { render: () => h("h1", "Home") }, meta: { text: "Home" } },
		{ path: "/about", name: "about", component: { render: () => h("h1", "About") }, meta: { text: "About" } },
		{
			path: "/settings",
			name: "settings",
			component: { render: () => h("h1", "Settings") },
			meta: { text: "Settings" }
		},
		{ path: "/hidden", name: "hidden", component: { render: () => h("h1", "Hidden") }, meta: { text: "Hidden" } },
		{ path: "/link1", name: "link1", component: { render: () => h("h1", "link1") }, meta: { text: "Link mit einem sehr langen Titel" } },
		{ path: "/link2", name: "link2", component: { render: () => h("h1", "link2") }, meta: { text: "Link mit einem sehr langen Titel 2" } },
		{ path: "/link3", name: "link3", component: { render: () => h("h1", "link3") }, meta: { text: "Link mit einem sehr langen Titel 3" } }
	];

	const routesVertical = [
		{ path: "/", name: "home", component: { render: () => h("h1", "Home") }, meta: { text: "Home" } },
		{ path: "/about", name: "about", component: { render: () => h("h1", "About") }, meta: { text: "About" } },
		{
			path: "/settings",
			name: "settings",
			component: { render: () => h("h1", "Settings") },
			meta: { text: "Settings" }
		},
		{ path: "/hidden", name: "hidden", component: { render: () => h("h1", "Hidden") }, meta: { text: "Hidden" } },
		{ path: "/link1", name: "link1", component: { render: () => h("h1", "link1") }, meta: { text: "Link1" } },
		{ path: "/link2", name: "link2", component: { render: () => h("h1", "link2") }, meta: { text: "Link2" } },
		{ path: "/link3", name: "link3", component: { render: () => h("h1", "link3") }, meta: { text: "Link3" } }
	];

	const hidden = ref([false, false, false, true]);
	const selectedRoute = ref(routes[0]);
</script>

<template>
	<Story title="Router Tab Bar" id="svws-ui-router-tab-bar" group="app" icon="ri:route-line" :layout="{type: 'grid', width: '90%'}">
		<Variant title="Default" id="Default">
			<svws-ui-header><span>Headline</span><br><span class="opacity-50">Subtitle</span></svws-ui-header>
			<svws-ui-router-tab-bar :routes="routes" :hidden="hidden" v-model="selectedRoute">
			<!--<component :is="selectedRoute.component" />-->
			</svws-ui-router-tab-bar>
			<div class="h-32" />
		</Variant>

		<Variant title="<svws-ui-router-vertical-tab-bar>" id="<svws-ui-router-vertical-tab-bar>">
			<svws-ui-router-vertical-tab-bar :routes="routesVertical" :hidden="hidden" v-model="selectedRoute">
				<!--<component :is="selectedRoute.component" />-->
			</svws-ui-router-vertical-tab-bar>
		</Variant>
	</Story>
</template>
