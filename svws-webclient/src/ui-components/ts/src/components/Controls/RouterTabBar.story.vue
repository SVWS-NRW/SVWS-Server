<script lang="ts" setup>
	import { ref, h } from "vue";
	import RouterTabBar from "./RouterTabBar.vue";

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
		{ path: "/link1", name: "link1", component: { render: () => h("h1", "link1") }, meta: { text: "Link1" } },
		{ path: "/link2", name: "link2", component: { render: () => h("h1", "link2") }, meta: { text: "Link2" } },
		{ path: "/link3", name: "link3", component: { render: () => h("h1", "link3") }, meta: { text: "Link3" } }
	];

	const hidden = ref([false, false, false, true]);
	const selectedRoute = ref(routes[0]);
</script>

<template>
	<Story title="SVWS UI/Controls/Router Tab Bar">
		<RouterTabBar :routes="routes" :hidden="hidden" v-model="selectedRoute">
			<component :is="selectedRoute.component" />
		</RouterTabBar>
	</Story>
</template>
