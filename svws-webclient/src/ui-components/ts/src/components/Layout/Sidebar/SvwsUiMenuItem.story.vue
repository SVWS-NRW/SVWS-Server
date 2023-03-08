<script setup lang="ts">
	import { hstEvent } from 'histoire/client';

	const propsVariants = {
		Default: { icon: "team", active: false, collapsed: false, subline: "" },
		Active: { icon: "team", active: true, collapsed: false, subline: "" },
		Collapsed: { icon: "team", active: false, collapsed: true, subline: "" },
		Subline: { icon: "team", active: false, collapsed: false, subline: "Subline" },
	};

	function onClick(event: Event) {
		hstEvent("click", event);
	}
</script>

<template>
	<Story title="SVWS UI/Layout/Sidebar/Menu Item">
		<Variant v-for="(props, title) of propsVariants"
			:key="title"
			:title="title">
			<svws-ui-menu-item v-bind="props" @click="onClick">Menu Item</svws-ui-menu-item>
		</Variant>
	</Story>
</template>