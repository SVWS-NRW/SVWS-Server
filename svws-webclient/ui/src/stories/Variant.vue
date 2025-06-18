<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<template v-if="((storyManager.gridView === 'single') && active) || (storyManager.gridView === 'grid')">
		<div class="">
			<div @click="active && storyManager.setVariantById('')" class="border-l border-t border-r pr-3 pl-3 rounded-t-md text-lg w-fit"
				:class="active ? 'bg-ui-brand text-ui-onbrand cursor-pointer' : 'bg-ui-brand-secondary text-ui-50'">
				{{ title }}
			</div>
			<div @click="switchActive" class="border rounded-r-lg rounded-b-lg border-ui-50 p-4" :class="color?.label">
				<slot />
			</div>
		</div>
		<template v-if="active && $slots.controls">
			<Teleport to="#controls" defer>
				<div class="text-2xl">Controls/Variant</div>
				<slot name="controls" />
			</Teleport>
		</template>
		<Teleport to="#source" v-if="($slots.source || (source !== undefined)) && active" defer>
			<div class="text-2xl">Source</div>
			<slot name="source">{{ source }}</slot>
		</Teleport>
	</template>
</template>

<script setup lang="ts">

	import { computed, onBeforeMount, useId, useSlots } from 'vue';
	import storyManager from './StoryManager';

	const props = withDefaults(defineProps<{
		title?: string;
		id?: string;
		layout?: {type?: 'grid'|'iframe'; width?: string};
		icon?: string;
		source?: string;
		responsiveDisabled?: boolean;
	}>(), {
		title: '',
		id: () => useId(),
		size: '',
		icon: '',
		source: undefined,
		layout: undefined,
		responsiveDisabled: false,
	});

	const slots = useSlots();

	onBeforeMount(() => storyManager.registerVariant(props, slots));

	const active = computed(() => storyManager.variant.id === props.id);

	const color = computed(() => storyManager.color);

	function switchActive() {
		if (!active.value)
			storyManager.setVariantById(props.id);
	}

</script>