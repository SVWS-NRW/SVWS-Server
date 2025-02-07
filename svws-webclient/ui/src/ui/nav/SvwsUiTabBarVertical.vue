<template>
	<div class="svws-ui-tabs--vertical">
		<div class="tab-bar-vertical--wrapper print:!hidden">
			<div v-if="state.scrolled" class="tab-bar-vertical--scroll-button-background tab-bar-vertical--scroll-button-background-up">
				<button class="tab-bar-vertical--scroll-button" @click="scroll('up')">
					<span class="icon"> <span class="icon i-ri-arrow-up-line" /> </span>
				</button>
			</div>
			<div ref="contentEl" class="tab-bar-vertical--content">
				<template v-for="(tab, index) in props.tabManager().tabs" :key="index">
					<button v-if="!(tab.hide === true) && (tab.text !== '')" @click="tabManager().setTab(tab)" class="tab-bar-vertical--button flex flex-row"
						:class="{ 'svws-active': tab.name === tabManager().tab.name }">
						<span>{{ tab.text }}</span>
						<template v-if="$slots.badge">
							<slot name="badge" />
						</template>
					</button>
				</template>
			</div>
			<div v-if="!state.scrolledMax"
				class="tab-bar-vertical--scroll-button-background tab-bar-vertical--scroll-button-background-down">
				<button class="tab-bar-vertical--scroll-button" @click="scroll('down')">
					<span class="icon"> <span class="icon i-ri-arrow-down-line" /> </span>
				</button>
			</div>
		</div>
		<div class="tab-bar-vertical--panel">
			<slot />
		</div>
	</div>
</template>

<script lang="ts" setup>

	import { onMounted, onUnmounted, onUpdated, ref } from 'vue';
	import type { TabManager } from './TabManager';

	const props = defineProps<{
		tabManager: () => TabManager;
	}>();

	type ComponentData = {
		scrolled: boolean;
		scrolledMax: boolean;
		scrollFactor: number;
		maxScrollTop: number;
	}

	const contentEl = ref();

	const state = ref<ComponentData>({
		scrolled: false,
		scrolledMax: false,
		scrollFactor: 4,
		maxScrollTop: 0,
	});

	onMounted(() => {
		state.value.maxScrollTop = (contentEl.value?.scrollHeight ?? 0) - (contentEl.value?.clientHeight ?? 0);
		state.value.scrolledMax = (contentEl.value?.scrollTop ?? 0) >= state.value.maxScrollTop;
		contentEl.value?.addEventListener("scroll", handleScroll);
		window.addEventListener("resize", handleScroll);
	})


	onUnmounted(() => {
		contentEl.value?.removeEventListener("scroll", handleScroll);
		window.removeEventListener("resize", handleScroll);
	});


	onUpdated(() => {
		handleScroll();
	});


	function handleScroll() {
		state.value.scrolled = (contentEl.value?.scrollTop ?? 0) > 0;
		state.value.maxScrollTop =
			(contentEl.value?.scrollHEight ?? 0) - (contentEl.value?.clientHeight ?? 0);
		state.value.scrolledMax = (contentEl.value?.scrollTop ?? 0) >= state.value.maxScrollTop;
	}

	function scroll(direction: 'up' | 'down') {
		const dir = direction === "up" ? -1 : 1;
		contentEl.value?.scrollBy({
			left: 0,
			top: (dir * contentEl.value.scrollHeight) / state.value.scrollFactor,
			behavior: "smooth",
		});
	}

</script>
