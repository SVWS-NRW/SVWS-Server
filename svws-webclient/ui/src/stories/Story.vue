<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<slot />
	<Teleport to="#source" v-if="source.length > 0">
		<div class="text-2xl">Source</div>
		{{ source }}
	</Teleport>
	<Teleport to="#docs" v-if="$slots.docs">
		<slot name="docs" />
	</Teleport>
	<Teleport to="#events">
		<div class="text-2xl flex items-center align-middle gap-12">
			<span>Events</span>
			<SvwsUiButton type="trash" @click="storyManager.events = []" title="Entferne alle Events aus dem Log" />
		</div>
		<div v-for="l, i of storyManager.events" :key="i">{{ l }}</div>
	</Teleport>
	<template v-if="$slots.controls">
		<Teleport to="#controls" defer>
			<div class="text-2xl">Controls/Story</div>
			<slot name="controls" />
		</Teleport>
	</template>
</template>

<script setup lang="ts">

	import { onBeforeMount, useId } from 'vue';
	import storyManager from './StoryManager';


	const props = withDefaults(defineProps<{
		title?: string;
		id?: string;
		layout?: {type?: 'grid'|'iframe'|'single'; width?: string, iframe?: boolean};
		icon?: string;
		source?: string;
		autoPropsDisabled?: boolean;
		group?: string;
		responsiveDisabled?: boolean;
	}>(), {
		title: '',
		id: () => useId(),
		size: '',
		icon: '',
		source: '',
		layout: undefined,
		autoPropsDisabled: false,
		group: '',
		responsiveDisabled: false,
	});

	onBeforeMount(() => storyManager.setStoryByID(props.id));

	document.addEventListener("log", (event) => {
		const ce = event as CustomEvent<string>;
		storyManager.events.push(ce.detail);
	});

</script>