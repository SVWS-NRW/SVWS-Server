<template>
	<div :class="{ 'svws-ui-page': !secondary, 'svws-single-tab': tabManager().tabs.length === 1 }">
		<div class="svws-ui-tabs" :class="{ 'svws-ui-tabs--secondary': secondary }">
			<div class="svws-ui-tabs--wrapper" :class="{'focus-region': focusSwitchingEnabled, 'highlighted': (tabManager().tabs.length > 1) && focusHelpVisible}">
				<p v-if="(tabManager().tabs.length > 1) && focusHelpVisible && secondary" class="region-enumeration">6</p>
				<p v-else-if="(tabManager().tabs.length > 1) && focusHelpVisible" class="region-enumeration">5</p>
				<div v-if="state.scrolled" class="svws-ui-tabs--scroll-button -left-1 pl-1 bg-gradient-to-l" @click="scroll('left')">
					<svws-ui-button type="icon">
						<span class="icon i-ri-arrow-left-s-line" />
					</svws-ui-button>
				</div>
				<div ref="tabsListElement" class="svws-ui-tabs--list">
					<template v-for="(tab, index) in props.tabManager().tabs" :key="index">
						<button v-if="!(tab.hide === true) && (tab.text !== '')" @click="tabManager().setTab(tab)" class="svws-ui-tab-button flex flex-row"
							:class="{
								'svws-active': tab.name === tabManager().tab.name,
								'tabsFirstLevelFocusField': (tab.name === tabManager().tab.name) && !secondary,
								'tabsSecondLevelFocusField': (tab.name === tabManager().tab.name) && secondary
							}">
							<span>{{ tab.text }}</span>
							<slot name="badge" :tab />
						</button>
					</template>
				</div>
				<div v-if="!state.scrolledMax" class="svws-ui-tabs--scroll-button -right-1 pr-1 bg-gradient-to-r justify-end" @click="scroll('right')">
					<svws-ui-button type="icon">
						<span class="icon i-ri-arrow-right-s-line" />
					</svws-ui-button>
				</div>
			</div>
		</div>
		<div class="svws-sub-nav-target" />
		<div class="svws-ui-tab-content">
			<slot />
		</div>
	</div>
</template>

<script lang="ts" setup>

	import { onMounted, onUnmounted, onUpdated, ref } from 'vue';
	import type { TabManager } from './TabManager';

	const props = withDefaults(defineProps<{
		tabManager: () => TabManager;
		secondary?: boolean;
		focusSwitchingEnabled? : boolean;
		focusHelpVisible? : boolean;
	}>(), {
		secondary: false,
		focusSwitchingEnabled: false,
		focusHelpVisible: false,
	});

	type ComponentData = {
		scrolled: boolean;
		scrolledMax: boolean;
		scrollFactor: number;
		maxScrollLeft: number;
		scrollOffset: number;
	}

	let processingKeyboardEvent = false;
	const tabsListElement = ref();

	const state = ref<ComponentData>({
		scrolled: false,
		scrolledMax: false,
		scrollFactor: 4,
		maxScrollLeft: 0,
		scrollOffset: 12,
	});

	onMounted(() => {
		state.value.maxScrollLeft = (tabsListElement.value?.scrollWidth ?? 0) - (tabsListElement.value?.clientWidth ?? 0);
		state.value.scrolledMax = (tabsListElement.value?.scrollLeft ?? 0) >= state.value.maxScrollLeft;
		tabsListElement.value?.addEventListener("scroll", handleScroll);
		window.addEventListener("resize", handleScroll);
		window.addEventListener("keydown", switchTab)
	})


	onUnmounted(() => {
		tabsListElement.value?.removeEventListener("scroll", handleScroll);
		window.removeEventListener("resize", handleScroll);
		window.removeEventListener("keydown", switchTab)
	});


	onUpdated(() => {
		handleScroll();
	});


	function handleScroll() {
		state.value.scrolled = (tabsListElement.value?.scrollLeft ?? 0) > state.value.scrollOffset;
		state.value.maxScrollLeft =
			(tabsListElement.value?.scrollWidth ?? 0) - (tabsListElement.value?.clientWidth ?? 0);
		state.value.scrolledMax = (tabsListElement.value?.scrollLeft ?? 0) >= state.value.maxScrollLeft - state.value.scrollOffset;
	}

	function scroll(direction: 'left' | 'right') {
		const dir = direction === "left" ? -1 : 1;
		tabsListElement.value?.scrollBy({
			top: 0,
			left: (dir * tabsListElement.value.scrollWidth) / state.value.scrollFactor,
			behavior: "smooth",
		});
	}

	function switchTab(event: KeyboardEvent) {
		if (event.altKey && event.ctrlKey && !processingKeyboardEvent && !event.repeat && ((event.key === "ArrowLeft") || (event.key === "ArrowRight"))) {
			processingKeyboardEvent = true;
			const backwards = (event.key === "ArrowLeft");
			const newTab = backwards ? props.tabManager().prevTab : props.tabManager().nextTab;
			void props.tabManager().setTab(newTab);
			processingKeyboardEvent = false;
		}
	}

</script>
