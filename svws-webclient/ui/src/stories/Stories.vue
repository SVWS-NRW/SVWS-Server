<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<div class="flex isolate overflow-auto h-screen">
		<div class="relative top-0 left-0 z-20 border-r border-ui-25" style="width: 15%;">
			<div class="flex flex-col h-full bg-ui-75">
				<div class="px-4 h-16 flex items-center gap-2 flex-none">
					<div class="py-3 sm:py-4 flex-1 h-full flex items-center pr-2 cursor-pointer" @click="router.push({path: '/'})">
						<img src="/src/assets/img/histoire-svws-dark.svg" width="50%" class="hidden dark:block">
						<img src="/src/assets/img/histoire-svws.svg" width="50%" class="block dark:hidden">
					</div>
					<div class="ml-auto flex-none flex" />
				</div>
				<div class="overflow-y-auto flex-1 pl-2">
					<ul>
						<li v-for="[key, items] of groups" :key="key" class="">
							<div class="text-2xl font-bold capitalize text-ui-100 pl-2 sticky top-0 bg-ui-50">{{ key }}</div>
							<ul>
								<li v-for="item of items.sort(cmp)" :key="item.path" class="text-xl pl-10 hover:bg-ui-brand-secondary cursor-pointer mr-2"
									:class="{'bg-ui-selected': clicked.path === item.path}" @click="routeTo(item)">
									{{ item.path.split('/').pop() }}
								</li>
							</ul>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="relative bottom-0 right-0" style="width: 85%;">
			<div class="h-full flex isolate" ref="dragger1">
				<div class="flex flex-col relative top-0 left-0 z-20 border-r border-ui-25 h-full" :style="leftStyle1">
					<div class="flex-none flex justify-around h-10 mx-8 items-center gap-4">
						<div class="text-ui-caution font-bold">
							Zur alten Histoire-Version der Doku geht es hier: <a href="https://eloquent-baklava-d6aa9d.netlify.app/">Link</a>
						</div>
						<div class="w-60"> <SvwsUiButton type="icon" @click="isDark = !isDark"><span class="icon" :class="!isDark ? 'i-ri-sun-line':'i-ri-moon-line'" /> </SvwsUiButton> </div>
						<div class="w-60"><SvwsUiButton type="transparent" @click="gridView = (gridView === 'single') ? 'grid':'single'">{{ gridView === 'single' ? 'Single':'Grid' }} aktiviert</SvwsUiButton></div>
						<div class="w-60">
							<ui-select label="Hintergrundfarbe" v-model="color" :manager="colorSelectManager" searchable headless />
						</div>
					</div>
					<div class="pr-8 size-full flex gap-4 relative overflow-hidden">
						<template v-if="(gridView === 'single') && (storyManager.story !== undefined) && (storyManager.story.mapVariants.size > 1)">
							<div class="relative top-0 left-0 h-full w-96 border-r border-ui-25 bg-ui-75 overflow-auto pt-4 pl-2 border-t">
								<svws-ui-table clickable :clicked="storyManager.variant" :items="storyManager.story.mapVariants.values()" scroll-into-view scroll :columns="columnsVariant" @update:clicked="storyManager.setVariantById($event.id)" />
							</div>
						</template>
						<div class="bottom-0 right-0 overflow-auto size-full pr-4 pl-4" :class="{'grid-cols-2 grid gap-4': (gridView === 'grid') && (storyManager.story.mapVariants.size > 1) }">
							<RouterView />
						</div>
					</div>
					<!-- <div class="dragger absolute z-100 hover:bg-ui-brand-hover transition-colors duration-150 delay-150 top-0 bottom-0 cursor-ew-resize w-4" @mousedown.prevent="dragStart2" /> -->
				</div>
				<div class="relative bottom-0 right-0 flex isolate overflow-auto flex-col portrait h-full" :style="rightStyle1" ref="dragger2">
					<div class="dragger absolute z-100 top-0 bottom-0 cursor-ew-resize w-1.5" @mousedown.prevent="dragStart1" />
					<div class="relative top-0 left-0 z-20" :style="upperStyle1">
						<div class="flex flex-col h-full">
							<div class="flex overflow-hidden relative h-10 flex-none border-b border-ui-25">
								<div @click="visible = 'controls'" class="px-4 h-full inline-flex items-center relative cursor-pointer"
									:class="visible === 'controls' ? 'bg-ui-brand text-ui-onbrand':'text-ui-brand hover:text-ui-onbrand-hover hover:bg-ui-brand-hover bg-ui-brand-secondary'">
									Controls
								</div>
								<div @click="visible = 'docs'" class="px-4 h-full inline-flex items-center hover:bg-ui-brand-hover relative text-ui-10 cursor-pointer"
									:class="visible === 'docs' ? 'bg-ui-brand text-ui-onbrand':'text-ui-brand hover:text-ui-onbrand-hover hover:bg-ui-brand-hover bg-ui-brand-secondary'">
									Docs
								</div>
								<div @click="visible = 'events'" class="px-4 h-full inline-flex items-center hover:bg-ui-brand-hover relative text-ui-10 cursor-pointer"
									:class="visible === 'events' ? 'bg-ui-brand text-ui-onbrand':'text-ui-brand hover:text-ui-onbrand-hover hover:bg-ui-brand-hover bg-ui-brand-secondary'">
									Events <SvwsUiBadge v-if="eventCounter > 0" type="primary" class="ml-3"> {{ eventCounter > 0 ? eventCounter : '' }}</SvwsUiBadge>
								</div>
							</div>
							<div data-test-id="story-controls" class="flex flex-col divide-y divide-gray-100 dark:divide-gray-750 h-full overflow-auto p-2">
								<div class="h-9 flex-none px-2 flex items-center relative" />
								<div class="flex-none">
									<div id="controls" class="size-full" :class="visible === 'controls' ? '':'hidden'" />
									<div id="docs" class="size-full" :class="visible === 'docs' ? '':'hidden'" />
									<div id="events" class="size-full" :class="visible === 'events' ? '':'hidden'" />
								</div>
							</div>
						</div>
						<div class="dragger absolute z-100 left-0 right-0 cursor-ns-resize h-1.5" @mousedown.prevent="dragStart2" />
					</div>
					<div class="relative bottom-0 right-0 border-t border-ui-25" :style="lowerStyle1">
						<div class="bg-ui-100 overflow-hidden flex flex-col h-full">
							<div class="size-full overflow-auto">
								<div class="p-4 size-full whitespace-pre block font-mono" id="source" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, onUnmounted, reactive, ref, watchEffect } from 'vue';
	import { useDark } from "@vueuse/core";
	import type { RouteRecord } from 'vue-router';
	import type { ColorPreset } from './StoryManager';
	import storyManager from './StoryManager';
	import router from './router';
	import type { PaneSplitterConfig} from './../ui/composables/usePaneSplitter';
	import { usePaneSplitter } from './../ui/composables/usePaneSplitter';
	import { SelectManagerSingle } from './../ui/controls/select/selectManager/SelectManagerSingle';

	const isDark = useDark({ selector: 'html' });

	const groups = new Map<string, RouteRecord[]>([['default', []]]);
	for (const route of router.getRoutes()) {
		const split = route.path.split('/');
		split.shift();
		const key = split.shift();
		if (key === undefined)
			continue;
		if ((split.length === 0))
			groups.get('default')?.push(route);
		else
			if (groups.has(key))
				groups.get(key)?.push(route);
			else
				groups.set(key, [route]);
	}

	function cmp(a: RouteRecord, b: RouteRecord) {
		if (a.path < b.path)
			return -1;
		else if (a.path > b.path)
			return 1;
		return 0;
	}
	const clicked = ref<RouteRecord>(router.getRoutes().find(r => r.path === router.currentRoute.value.path) || router.getRoutes()[0]);
	const columnsVariant = [ {key: 'title', label: 'Variant'} ];

	async function routeTo(option: RouteRecord) {
		clicked.value = option;
		await router.push({ path: option.path });
		// storyManager.setStoryByID('default');
	}

	const visible = ref<'events'|'docs'|'controls'>('controls');

	watchEffect(() => {
		if ((visible.value === 'events') && (storyManager.events.length > 0))
			initialEventCounter.value = storyManager.events.length;
	});

	const initialEventCounter = ref(storyManager.events.length);

	const eventCounter = computed(() => {
		if (visible.value !== 'events')
			return storyManager.events.length - initialEventCounter.value;
		else
			return 0;
	});

	const color = computed({
		get: () => storyManager.color,
		set: (value) => storyManager.color = value,
	});

	const gridView = computed({
		get: () => storyManager.gridView,
		set: (value) => storyManager.gridView = value,
	});

	const	backgroundPresets: ColorPreset[] = [
		{label:'Transparent',color:'transparent',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui',color:'var(--background-color-ui)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-0',color:'var(--background-color-ui-0)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-10',color:'var(--background-color-ui-10)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-25',color:'var(--background-color-ui-25)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-50',color:'var(--background-color-ui-50)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-75',color:'var(--background-color-ui-75)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-100',color:'var(--background-color-ui-100)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-hover',color:'var(--background-color-ui-hover)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-ui-brand',color:'var(--background-color-ui-brand)',contrastColor:'var(--text-color-ui-onbrand)'},
		{label:'bg-ui-brand-hover',color:'var(--background-color-ui-brand-hover)',contrastColor:'var(--text-color-ui-onbrand)'},
		{label:'bg-ui-brand-secondary',color:'var(--background-color-ui-brand-secondary)',contrastColor:'var(--text-color-ui-onbrand)'},
		{label:'bg-ui-statistic',color:'var(--background-color-ui-statistic)',contrastColor:'var(--text-color-ui-onstatistic)'},
		{label:'bg-ui-statistic-hover',color:'var(--background-color-ui-statistic-hover)',contrastColor:'var(--text-color-ui-onstatistic)'},
		{label:'bg-ui-statistic-secondary',color:'var(--background-color-ui-statistic-secondary)',contrastColor:'var(--text-color-ui-onstatistic)'},
		{label:'bg-ui-selected',color:'var(--background-color-ui-selected)',contrastColor:'var(--text-color-ui-onselected)'},
		{label:'bg-ui-selected-hover',color:'var(--background-color-ui-selected-hover)',contrastColor:'var(--text-color-ui-onselected)'},
		{label:'bg-ui-danger',color:'var(--background-color-ui-danger)',contrastColor:'var(--text-color-ui-ondanger)'},
		{label:'bg-ui-danger-hover',color:'var(--background-color-ui-danger-hover)',contrastColor:'var(--text-color-ui-ondanger)'},
		{label:'bg-ui-danger-secondary',color:'var(--background-color-ui-danger-secondary)',contrastColor:'var(--text-color-ui-ondanger)'},
		{label:'bg-ui-success',color:'var(--background-color-ui-success)',contrastColor:'var(--text-color-ui-onsuccess)'},
		{label:'bg-ui-success-hover',color:'var(--background-color-ui-success-hover)',contrastColor:'var(--text-color-ui-onsuccess)'},
		{label:'bg-ui-success-secondary',color:'var(--background-color-ui-success-secondary)',contrastColor:'var(--text-color-ui-onsuccess)'},
		{label:'bg-ui-warning',color:'var(--background-color-ui-warning)',contrastColor:'var(--text-color-ui-onwarning)'},
		{label:'bg-ui-warning-hover',color:'var(--background-color-ui-warning-hover)',contrastColor:'var(--text-color-ui-onwarning)'},
		{label:'bg-ui-warning-secondary',color:'var(--background-color-ui-warning-secondary)',contrastColor:'var(--text-color-ui-onwarning)'},
		{label:'bg-ui-caution',color:'var(--background-color-ui-caution)',contrastColor:'var(--text-color-ui-oncaution)'},
		{label:'bg-ui-caution-hover',color:'var(--background-color-ui-caution-hover)',contrastColor:'var(--text-color-ui-oncaution)'},
		{label:'bg-ui-caution-secondary',color:'var(--background-color-ui-caution-secondary)',contrastColor:'var(--text-color-ui-oncaution)'},
		{label:'bg-ui-neutral',color:'var(--background-color-ui-neutral)',contrastColor:'var(--text-color-ui-onneutral)'},
		{label:'bg-ui-neutral-hover',color:'var(--background-color-ui-neutral-hover)',contrastColor:'var(--text-color-ui-onneutral)'},
		{label:'bg-ui-neutral-secondary',color:'var(--background-color-ui-neutral-secondary)',contrastColor:'var(--text-color-ui-onneutral)'},
		{label:'bg-ui-disabled',color:'var(--background-color-ui-disabled)',contrastColor:'var(--text-color-ui-ondisabled)'},
		{label:'bg-uistatic',color:'var(--background-color-uistatic)',contrastColor:'var(--text-color-ui)'},
		{label:'bg-uistatic-0',color:'var(--background-color-uistatic-0)',contrastColor:'var(--text-color-ui-0)'},
		{label:'bg-uistatic-10',color:'var(--background-color-uistatic-10)',contrastColor:'var(--text-color-ui-10)'},
		{label:'bg-uistatic-25',color:'var(--background-color-uistatic-25)',contrastColor:'var(--text-color-ui-25)'},
		{label:'bg-uistatic-50',color:'var(--background-color-uistatic-50)',contrastColor:'var(--text-color-ui-50)'},
		{label:'bg-uistatic-75',color:'var(--background-color-uistatic-75)',contrastColor:'var(--text-color-ui-75)'},
		{label:'bg-uistatic-100',color:'var(--background-color-uistatic-100)',contrastColor:'var(--text-color-ui-100)'},
	];

	const colorSelectManager = new SelectManagerSingle({
		options: backgroundPresets, optionDisplayText: option => option.label,	selectionDisplayText: option => option.label,
	});

	const configH = reactive<PaneSplitterConfig>({minSplit: 20, maxSplit: 80, mode: 'horizontal', defaultSplit: 50});

	const { removeDragListeners, dragStart: dragStart1, thisStyle: leftStyle1, thatStyle: rightStyle1, dragger: dragger1 } = usePaneSplitter();
	const { dragStart: dragStart2, thisStyle: upperStyle1, thatStyle: lowerStyle1, dragger: dragger2 } = usePaneSplitter({ mode: 'horizontal' });

	onUnmounted(removeDragListeners);

</script>
