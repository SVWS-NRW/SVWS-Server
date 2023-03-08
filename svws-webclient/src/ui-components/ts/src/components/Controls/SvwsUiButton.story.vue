<script setup lang="ts">
	import { hstEvent } from 'histoire/client';

	const propsVariants = {
		Primary: { type: "primary" },
		Secondary: { type: "secondary" },
		Danger: { type: "danger" },
		Transparent: { type: "transparent" },
		'Dropdown Action': { type: "primary", dropdownAction: true },
		Disabled: { type: "primary", disabled: true },
		Trash: { type: "trash" },
	} as const;

	function onClick(event: Event) {
		hstEvent('Click', event);
	}
</script>

<template>
	<Story title="SVWS UI/Controls/Button">
		<Variant v-for="(props, title) of propsVariants"
			:key="title"
			:title="title">
			<svws-ui-button v-bind="props" @click="onClick">Button</svws-ui-button>
		</Variant>
	</Story>
</template>
