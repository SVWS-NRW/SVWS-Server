<script setup lang="ts">
	import { ref } from 'vue';

	const propsVariants = {
		Primary: { type: "primary" },
		Secondary: { type: "secondary" },
		Danger: { type: "danger" },
		Transparent: { type: "transparent" },
		Disabled: { type: "primary", disabled: true },
		Icon: { type: "icon" },
	} as const;

	type Item = {text: string; key: number};
	const items = [
		{text: "Erste Option", key: 1}, {text: "Zweite Option", key: 2}, {text: "Dritte Option", key: 3}
	]

	function onClick(item: Item) {
		selectedOption.value = item;
	}

	const selectedOption = ref();
</script>

<template>
	<Story title="ButtonMenu" id="svws-ui-button-menu" icon="ri:cursor-line" :layout="{type: 'grid', width: '45%'}">
		<Variant v-for="(props, title) of propsVariants" :key="title" :id="title" :title="title">
			<svws-ui-button-menu v-bind="props" @selected="onClick" :items="items" :item-text="i=>i.text">
				<template v-if="props.type !== 'icon'">
					Button
				</template>
				<i-ri-settings3-line v-else />
			</svws-ui-button-menu>
			<br>{{ selectedOption }}
		</Variant>
	</Story>
</template>
