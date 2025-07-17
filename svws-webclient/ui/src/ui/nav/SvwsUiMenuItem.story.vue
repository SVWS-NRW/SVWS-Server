<template>
	<Story title="Sidebar/Menu Item" id="svws-ui-menu-item" :layout="{ type: 'single', iframe: false }" group="app">
		<Variant v-for="(props, title) of propsVariants"
			:key="title"
			:id="title"
			:title="title">
			<svws-ui-menu-item v-bind="props" @click="onClick">Menu Item</svws-ui-menu-item>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import { logEvent } from '../../stories/helper';

	const propsVariants = {
		Default: { icon: "team", active: false, collapsed: false, subline: "" },
		Active: { icon: "team", active: true, collapsed: false, subline: "" },
		Collapsed: { icon: "team", active: false, collapsed: true, subline: "" },
		Subline: { icon: "team", active: false, collapsed: false, subline: "Subline" },
	};

	function onClick(event: Event) {
		logEvent("click", event);
	}
</script>
