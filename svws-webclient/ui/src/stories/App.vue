<template>
	<div class="histoire-base-split-pane flex isolate overflow-auto h-screen bg-ui-100">
		<div class="relative top-0 left-0 z-20 border-r border-ui-25" style="width: 15%;">
			<div class="flex flex-col h-full bg-ui-75">
				<div class="histoire-app-header px-4 h-16 flex items-center gap-2 flex-none">
					<div class="py-3 sm:py-4 flex-1 h-full flex items-center pr-2"><img src="/src/assets/img/histoire-svws.svg" width="50%"></div>
					<div class="ml-auto flex-none flex" />
				</div>
				<div class="histoire-story-list overflow-y-auto flex-1 pl-2">
					<svws-ui-table clickable v-model:clicked="clicked" :items="router.getRoutes()" scroll-into-view scroll :columns @update:clicked="routeTo">
						<template #cell(path)="{ rowData }">
							{{ rowData.path.split('/').join(' → ') }}
						</template>
					</svws-ui-table>
				</div>
			</div>
		</div>
		<div class="relative bottom-0 right-0" style="width: 85%;">
			<div class="histoire-story-view histoire-with-story h-full flex isolate" ref="el">
				<div class="flex flex-col relative top-0 left-0 z-20 border-r border-ui-25 h-full bg-ui-100" :style="leftStyle">
					<div class="flex-none flex justify-around h-10 mx-8 items-center gap-4">
						<div class="text-ui-caution font-bold">
							Zur alten Histoire-Version der Doku geht es hier: <a href="https://eloquent-baklava-d6aa9d.netlify.app/">Link</a>
						</div>
						<div class="w-60"><SvwsUiButton type="icon" @click="updateTheme(themeRef === 'dark' ? 'light':'dark')"><span class="icon" :class="themeRef === 'light' ? 'i-ri-sun-line':'i-ri-moon-line'" /> </SvwsUiButton></div>
						<div class="w-60"><SvwsUiButton type="transparent" @click="gridView = (gridView === 'single') ? 'grid':'single'">{{ gridView === 'single' ? 'Single':'Grid' }} aktiviert</SvwsUiButton></div>
						<div class="w-60">
							<ui-select label="Hintergrundfarbe" v-model="color" :manager="colorSelectManager" searchable removable headless />
						</div>
					</div>
					<div class="histoire-story-responsive-preview pr-8 size-full flex gap-4 relative overflow-hidden histoire-story-variant-single-preview-native">
						<template v-if="gridView === 'single' && storyManager.story !== undefined">
							<div class="relative top-0 left-0 h-full w-96 border-r border-ui-25 bg-ui-75 overflow-auto pt-4 pl-2 border-t">
								<svws-ui-table clickable :clicked="storyManager.variant" :items="storyManager.story.mapVariants.values()" scroll-into-view scroll :columns="columnsVariant" @update:clicked="storyManager.setVariantById($event.id)" />
							</div>
						</template>
						<div class="bottom-0 right-0 overflow-auto size-full pr-4 pl-4" :class="{'  grid-cols-2 grid gap-4 ': (gridView === 'grid') && (storyManager.story.mapVariants.size > 1) }">
							<RouterView />
						</div>
					</div>
					<!-- <div class="dragger absolute z-100 hover:bg-ui-brand-hover transition-colors duration-150 delay-150 top-0 bottom-0 cursor-ew-resize w-4" /> -->
				</div>
				<div class="relative bottom-0 right-0 histoire-base-split-pane flex isolate overflow-auto flex-col portrait histoire-story-side-panel histoire-loaded h-full" :style="rightStyle">
					<div class="dragger absolute z-100 hover:bg-ui-selected-hover transition-colors duration-150 delay-150 top-0 bottom-0 cursor-ew-resize w-3" @mousedown.prevent="dragStart" />
					<div class="relative top-0 left-0 z-20" style="height: 50%;">
						<div class="flex flex-col h-full">
							<div class="histoire-base-overflow-menu flex overflow-hidden relative histoire-pane-tabs h-10 flex-none border-b border-ui-25">
								<div @click="visible = 'controls'" class="px-4 h-full inline-flex items-center relative histoire-base-tab cursor-pointer"
									:class="visible === 'controls' ? 'bg-ui-brand text-ui-onbrand':'text-ui-brand hover:text-ui-onbrand-hover hover:bg-ui-brand-hover bg-ui-brand-secondary'">
									Controls
								</div>
								<div @click="visible = 'docs'" class="px-4 h-full inline-flex items-center hover:bg-ui-brand-hover relative text-ui-10 histoire-base-tab cursor-pointer"
									:class="visible === 'docs' ? 'bg-ui-brand text-ui-onbrand':'text-ui-brand hover:text-ui-onbrand-hover hover:bg-ui-brand-hover bg-ui-brand-secondary'">
									Docs
								</div>
								<div @click="visible = 'events'" class="px-4 h-full inline-flex items-center hover:bg-ui-brand-hover relative text-ui-10 histoire-base-tab cursor-pointer"
									:class="visible === 'events' ? 'bg-ui-brand text-ui-onbrand':'text-ui-brand hover:text-ui-onbrand-hover hover:bg-ui-brand-hover bg-ui-brand-secondary'">
									Events <SvwsUiBadge v-if="eventCounter > 0" type="primary" class="ml-3"> {{ eventCounter > 0 ? eventCounter : '' }}</SvwsUiBadge>
								</div>
							</div>
							<div data-test-id="story-controls" class="histoire-story-controls flex flex-col divide-y divide-gray-100 dark:divide-gray-750 h-full overflow-auto p-2">
								<div class="h-9 flex-none px-2 flex items-center relative" />
								<div class="histoire-generic-render-story flex-none">
									<div id="controls" :class="visible === 'controls' ? 'visible':'hidden'" />
									<div id="docs" :class="visible === 'docs' ? 'visible':'hidden'" />
									<div id="events" :class="visible === 'events' ? 'visible':'hidden'" />
								</div>
							</div>
						</div>
						<!-- <div class="dragger absolute z-100 hover:bg-ui-brand-hover transition-colors duration-150 delay-150 left-0 right-0 cursor-ns-resize h-4" /> -->
					</div>
					<div class="relative bottom-0 right-0 border-t border-ui-25" style="height: 50%;">
						<div class="histoire-story-source-code bg-ui-100 overflow-hidden flex flex-col h-full">
							<div class="w-full h-full overflow-auto">
								<div class="p-4 w-fit" id="source" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, onUnmounted, ref, watchEffect } from 'vue';
	import type { RouteRecord } from 'vue-router';
	import type { ColorPreset } from './StoryManager';
	import storyManager from './StoryManager';
	import { ObjectSelectManager } from '~/ui/controls/select/selectManager/ObjectSelectManager';
	import router from '../router';

	const clicked = ref<RouteRecord>(router.getRoutes().find(r => r.path === router.currentRoute.value.path) || router.getRoutes()[0]);
	const columns = [ {key: 'path', label: 'Komponente'} ];
	const columnsVariant = [ {key: 'title', label: 'Variant'} ];

	async function routeTo(option: RouteRecord) {
		await router.push({ path: option.path });
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

	const currentSplit = ref(80);

	const boundSplit = computed(() => {
		if (currentSplit.value < 20)
			return 20;
		else if (currentSplit.value > 80)
			return 80;
		else
			return currentSplit.value;
	});

	const leftStyle = computed(() => `width: ${boundSplit.value}%`);
	const rightStyle = computed(() => `width: ${100 - boundSplit.value}%`);

	const dragging = ref(false)
	let startPosition = 0;
	let startSplit = 0;

	const el = ref<HTMLDivElement|null>(null);

	function dragStart(e: MouseEvent) {
		dragging.value = true;
		startPosition = e.pageX;
		startSplit = boundSplit.value;
		window.addEventListener('mousemove', dragMove);
		window.addEventListener('mouseup', dragEnd);
	}

	function dragMove(e: MouseEvent) {
		if (dragging.value && (el.value !== null)) {
			const position = e.pageX;
			const totalSize = el.value.offsetWidth;
			const dPosition = position - startPosition;
			currentSplit.value = startSplit + (~~(dPosition / totalSize * 200) / 2);
		}
	}

	function dragEnd() {
		dragging.value = false;
		removeDragListeners();
	}

	function removeDragListeners() {
		window.removeEventListener('mousemove', dragMove);
		window.removeEventListener('mouseup', dragEnd);
	}

	onUnmounted(() => removeDragListeners());

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

	const colorSelectManager = new ObjectSelectManager(false, backgroundPresets, (option: ColorPreset) => option.label, (option: ColorPreset) => option.label);
	colorSelectManager.setDeepSearchAttributes(['label']);
	colorSelectManager.removable = true;


	const themeRef = ref<'light'|'dark'|'auto'>('light');

	const updateTheme = (theme: ('light'|'dark'|'auto') | null) => {
		if (theme === null)
			return;
		document.documentElement.classList.remove('light', 'dark');
		if (theme !== 'auto')
			document.documentElement.classList.add(theme);
		localStorage.setItem('theme', theme);
		themeRef.value = theme;
	};

	updateTheme(<'light'|'dark'|'auto'>localStorage.getItem('theme'));
</script>
