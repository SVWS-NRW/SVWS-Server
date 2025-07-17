<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<template v-if="((storyManager.gridView === 'single') && active) || (storyManager.gridView === 'grid')">
		<div class="size-full flex flex-col">
			<div @click="active && storyManager.setVariantById('')" class="border-l border-t border-r pr-3 pl-3 rounded-t-md text-lg w-fit"
				:class="active ? 'bg-ui-brand text-ui-onbrand cursor-pointer' : 'bg-ui-brand-secondary text-ui-50'">
				{{ title }}
			</div>
			<div @click="switchActive" class="border rounded-r-lg rounded-b-lg border-ui-50 size-full" ref="dragger">
				<div class="border-r border-b rounded-lg relative border-ui-50 p-4 overflow-auto" :class="color?.label" :style>
					<slot />
					<div class="absolute bottom-0 right-0 pb-4 pr-4 size-6 cursor-nwse-resize bg-uistatic-100" @mousedown.prevent="dragStart"><span class="icon-sm i-ri-arrow-right-down-line" /></div>
				</div>
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

	import { computed, onBeforeMount, onUnmounted, ref, useSlots } from 'vue';
	import storyManager from './StoryManager';
	import type { PaneSplitterConfig } from './../ui/composables/usePaneSplitter';
	import { usePaneSplitter } from './../ui/composables/usePaneSplitter';

	const props = withDefaults(defineProps<{
		title: string;
		id: string;
		layout?: {type?: 'grid'|'iframe'; width?: string};
		icon?: string;
		source?: string;
		responsiveDisabled?: boolean;
	}>(), {
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

	const dragger = ref<HTMLElement|null>(null);

	const configV = <PaneSplitterConfig>({ minSplit: 0, maxSplit: 100, defaultSplit: 100, snap: 99, mode: 'vertical', dragger });
	const configH = <PaneSplitterConfig>({ minSplit: 0, maxSplit: 100, defaultSplit: 100, snap: 99, mode: 'horizontal', dragger });

	const { removeDragListeners, dragStart: dragStart1, thisStyle: leftStyle } = usePaneSplitter(configV);
	const { dragStart: dragStart2, thisStyle: upperStyle } = usePaneSplitter(configH);

	onUnmounted(removeDragListeners);

	function dragStart(e: MouseEvent) {
		dragStart1(e);
		dragStart2(e);
	}

	const style = computed(() => `${leftStyle.value} ${upperStyle.value}`);

</script>