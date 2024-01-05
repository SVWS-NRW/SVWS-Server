<template>
	<div class="svws-ui-page" :class="{'svws-single-route': props.routes.length === 1}">
		<div class="svws-ui-tabs">
			<div class="svws-ui-tabs--wrapper">
				<div v-if="state.scrolled" class="svws-ui-tabs--scroll-button -left-1 pl-1 bg-gradient-to-l" @click="scroll('left')">
					<svws-ui-button type="icon">
						<i-ri-arrow-left-s-line />
					</svws-ui-button>
				</div>
				<div ref="tabsListElement" class="svws-ui-tabs--list">
					<template v-for="(route, index) in props.routes" :key="index">
						<svws-ui-router-tab-bar-button v-if="!isHidden(index)" :route="route" :selected="selected" @select="select(route)" />
					</template>
				</div>
				<div v-if="!state.scrolledMax" class="svws-ui-tabs--scroll-button -right-1 pr-1 bg-gradient-to-r justify-end" @click="scroll('right')">
					<svws-ui-button type="icon">
						<i-ri-arrow-right-s-line />
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
	import type { WritableComputedRef } from 'vue';
	import { computed, onMounted, onUnmounted, onUpdated, ref } from 'vue';
	import type { RouteRecordRaw } from 'vue-router';
	import type { AuswahlChildData } from '../../types';

	const props = defineProps<{
		routes: RouteRecordRaw[] | AuswahlChildData[]
		hidden: boolean[] | undefined
		modelValue: RouteRecordRaw | AuswahlChildData
	}>();

	const emit = defineEmits<{
		(e: 'update:modelValue', value: any): any,
	}>();

	type ComponentData = {
		scrolled: boolean;
		scrolledMax: boolean;
		scrollFactor: number;
		maxScrollLeft: number;
		scrollOffset: number;
	}

	const tabsListElement = ref();
	const selected: WritableComputedRef<RouteRecordRaw | AuswahlChildData> = computed({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	});

	function isHidden(index: number) {
		if ((props.hidden === undefined) || props.hidden[index] === undefined)
			return false;
		return props.hidden[index];
	}

	const state = ref<ComponentData>({
		scrolled: false,
		scrolledMax: false,
		scrollFactor: 4,
		maxScrollLeft: 0,
		scrollOffset: 12
	});

	onMounted(() => {
		state.value.maxScrollLeft = (tabsListElement.value?.scrollWidth ?? 0) - (tabsListElement.value?.clientWidth ?? 0);
		state.value.scrolledMax = (tabsListElement.value?.scrollLeft ?? 0) >= state.value.maxScrollLeft;
		tabsListElement.value?.addEventListener("scroll", handleScroll);
		window.addEventListener("resize", handleScroll);
	})


	onUnmounted(() => {
		tabsListElement.value?.removeEventListener("scroll", handleScroll);
		window.removeEventListener("resize", handleScroll);
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
		const dir = direction == "left" ? -1 : 1;
		tabsListElement.value?.scrollBy({
			top: 0,
			left: (dir * tabsListElement.value.scrollWidth) / state.value.scrollFactor,
			behavior: "smooth"
		});
	}

	function select(route: RouteRecordRaw | AuswahlChildData) {
		selected.value = route;
	}

</script>


<style lang="postcss">

	.svws-ui-page {
		@apply flex flex-col items-start overflow-hidden h-full;
		&.svws-single-route {
			.svws-ui-tabs--list {
				@apply invisible
			}
		}
	}

	.svws-ui-tab-content {
		@apply w-full relative flex-grow overflow-auto;
		.svws-api--pending & {
			@apply opacity-50 filter grayscale;
		}
	}

	.svws-ui-tabs,
	.svws-sub-nav-target {
		@apply px-6 lg:px-9 3xl:px-12 4xl:px-20 w-full;
		@apply print:hidden;
	}

	.svws-sub-nav-target {
		@apply w-full relative z-30;
	}

	.svws-ui-tabs--wrapper {
		@apply flex items-center -mx-3 px-0.5 w-auto relative z-30 flex-shrink-0 overflow-hidden;
		@apply bg-white dark:bg-black;
		&:before {
			@apply h-[2px] bg-light dark:bg-white/10 absolute left-0 right-0 bottom-0;
			content: '';
			.svws-has-sub-nav & {
				@apply rounded-md h-[10px] bottom-[-8px];
			}
		}
	}

	.svws-ui-tabs--list {
		@apply flex flex-row items-center relative w-full gap-x-[2px] p-[2px] overflow-x-scroll pb-2 -mb-px;
		-ms-overflow-style: none;
		scrollbar-width: none;

		&::-webkit-scrollbar {
			display: none;
		}

		&:focus-visible {
			@apply outline-none;
		}
	}

	.svws-ui-tabs--scroll-button {
		@apply absolute z-20 -top-0.5 text-base h-8 flex items-center w-12 py-1 cursor-pointer;
		@apply from-white/0 via-50% via-white to-white dark:from-black/0 dark:via-50% dark:via-black dark:to-black;

		.button {
			@apply w-5 h-full p-0 rounded bg-light dark:bg-white/5 border border-black/5 dark:border-white/5;

			&:hover,
			&:focus-visible {
				@apply brightness-95;
			}
		}
	}

	.svws-sub-nav-target {
		@apply overflow-x-auto flex-shrink-0;
	}

	.svws-ui-secondary-tabs {
		@apply text-button flex gap-[2px] -mx-3 pt-1.5 pb-3 px-1 -mt-px rounded-md;
	}

</style>
