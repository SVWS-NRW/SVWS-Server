<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<div class="overflow-auto grid-cols-2 grid gap-4 size-full pr-4">
		<slot />
	</div>
	<Teleport to="#source" v-if="source.length > 0">
		<div class="text-2xl">Source</div>
		{{ source }}
	</Teleport>
	<Teleport to="#docs" v-if="$slots.docs">
		<slot name="docs" />
	</Teleport>
	<Teleport to="#events">
		<div class="text-2xl">Events</div>
		<div v-for="l, i of logList" :key="i">{{ l }}</div>
	</Teleport>
	<template v-if="$slots.controls">
		<Teleport to="#controls" defer>
			<div class="text-2xl">Controls/Story</div>
			<slot name="controls" />
		</Teleport>
	</template>
</template>

<script setup lang="ts">

	import { onBeforeMount, ref, useId } from 'vue';
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
		id: '',
		size: '',
		icon: '',
		source: '',
		layout: undefined,
		autoPropsDisabled: false,
		group: '',
		responsiveDisabled: false,
	});

	const uid = useId();

	onBeforeMount(() => storyManager.setStory({id: uid}));

	document.addEventListener("log", (event) => {
		const ce = event as CustomEvent<string>;
		logList.value.push(ce.detail);
	});

	const logList = ref<string[]>([]);

</script>