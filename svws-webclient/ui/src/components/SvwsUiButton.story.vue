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
	<Story title="Button" id="svws-ui-button" icon="ri:cursor-line" :layout="{type: 'grid', width: '45%'}">
		<Variant v-for="(props, title) of propsVariants"
			:key="title"
			:id="title"
			:title="title">
			<div class="flex gap-2 p-1">
				<svws-ui-button v-bind="props" @click="onClick">
					<template v-if="props.type !== 'icon'">
						Button
					</template>
					<span class="icon i-ri-settings-3-line" v-else />
				</svws-ui-button>
				<div v-if="title === 'Primary' || title === 'Secondary'" class="page--statistik">
					<svws-ui-button v-bind="props" @click="onClick">
						Button
					</svws-ui-button>
				</div>
			</div>
		</Variant>
	</Story>
</template>
