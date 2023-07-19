<script setup lang="ts">
	import { logEvent } from 'histoire/client';

	const propsVariants = {
		Primary: { type: "primary" },
		Secondary: { type: "secondary" },
		Danger: { type: "danger" },
		Transparent: { type: "transparent" },
		Disabled: { type: "primary", disabled: true },
		Icon: { type: "icon" },
		Trash: { type: "trash" },
	} as const;

	function onClick(event: Event) {
		logEvent('Click', event);
	}
</script>

<template>
	<Story title="Button" icon="ri:cursor-line" :layout="{type: 'grid', width: '45%'}">
		<Variant v-for="(props, title) of propsVariants"
			:key="title"
			:title="title">
			<svws-ui-button v-bind="props" @click="onClick">
				<template v-if="props.type !== 'icon'">
					Button
				</template>
				<i-ri-settings3-line v-else />
			</svws-ui-button>
		</Variant>
	</Story>
</template>
