<template>
	<div class="router-tab-bar--area" :class="{'router-tab-bar--area--single-route': props.routes.length === 1}">
		<div class="router-tab-bar--wrapper print:hidden">
			<div v-if="state.scrolled" class="router-tab-bar--scroll-button-background router-tab-bar--scroll-button-background-left">
				<svws-ui-button type="primary" @click="scroll('left')" class="router-tab-bar--scroll-button">
					<i-ri-arrow-left-s-line />
				</svws-ui-button>
			</div>
			<div ref="contentEl" class="router-tab-bar--content">
				<svws-ui-router-tab-bar-button v-for="(route, index) in props.routes" :route="route" :selected="selected"
					:hidden="isHidden(index)" @select="select(route)" :key="index" />
			</div>
			<div v-if="!state.scrolledMax"
				class="router-tab-bar--scroll-button-background router-tab-bar--scroll-button-background-right">
				<svws-ui-button type="primary" @click="scroll('right')" class="router-tab-bar--scroll-button">
					<i-ri-arrow-right-s-line />
				</svws-ui-button>
			</div>
		</div>
		<div class="router-tab-bar--subnav-target" />
		<div class="router-tab-bar--panel">
			<slot />
		</div>
	</div>
</template>

<script lang="ts" setup>
	import type { WritableComputedRef } from 'vue';
	import { computed, onMounted, onUnmounted, onUpdated, ref } from 'vue';
	import type { RouteRecordRaw } from "vue-router";
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

	const contentEl = ref();
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
		state.value.maxScrollLeft = (contentEl.value?.scrollWidth ?? 0) - (contentEl.value?.clientWidth ?? 0);
		state.value.scrolledMax = (contentEl.value?.scrollLeft ?? 0) >= state.value.maxScrollLeft;
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
		state.value.scrolled = (contentEl.value?.scrollLeft ?? 0) > state.value.scrollOffset;
		state.value.maxScrollLeft =
			(contentEl.value?.scrollWidth ?? 0) - (contentEl.value?.clientWidth ?? 0);
		state.value.scrolledMax = (contentEl.value?.scrollLeft ?? 0) >= state.value.maxScrollLeft - state.value.scrollOffset;
	}

	function scroll(direction: 'left' | 'right') {
		const dir = direction == "left" ? -1 : 1;
		contentEl.value?.scrollBy({
			top: 0,
			left: (dir * contentEl.value.scrollWidth) / state.value.scrollFactor,
			behavior: "smooth"
		});
	}

	function select(route: RouteRecordRaw | AuswahlChildData) {
		selected.value = route;
	}

	// get initial height of .router-tab-bar--panel, update on resize
	const panelHeight = ref(0);

	function getPanelHeight() {
		panelHeight.value = document.querySelector('.router-tab-bar--panel')?.clientHeight ?? 0;
		document.documentElement.style.setProperty('--panel-height', panelHeight.value > 0 ? `${panelHeight.value}px` : '100%');
		return panelHeight.value;
	}

	onMounted(() => {
		getPanelHeight();
		window.addEventListener('resize', getPanelHeight);
	});

	onUnmounted(() => {
		window.removeEventListener('resize', getPanelHeight);
	});

</script>


<style lang="postcss">
	.router-tab-bar {
		&--area {
			@apply h-full flex flex-col items-start overflow-hidden;
			@apply pt-1.5;

			&--single-route {
				@apply bg-transparent;
			}
		}

		&--panel {
			@apply h-full w-full relative overflow-auto flex-grow;

			&.pt-0 {
				padding-top: 0;
			}
		}

		&--wrapper {
			@apply w-full flex items-center flex-shrink-0 relative z-30;

			&:after {
				@apply absolute inset-x-0 bottom-0 pointer-events-none;
				@apply border-b-2 border-light dark:border-white/10;
				content: "";
			}
		}

		&--content {
			@apply relative w-full flex flex-row items-center;
			@apply overflow-x-scroll space-x-2 pb-2 px-6 lg:px-9 3xl:px-12 4xl:px-20;
			-ms-overflow-style: none;
			scrollbar-width: none;

			&::-webkit-scrollbar {
				@apply hidden;
			}

			&:focus-visible {
				@apply outline-none bg-svws/5;
			}
		}

		&--subnav-target {
			@apply w-full overflow-x-auto flex-shrink-0;
		}
	}

	.router-tab-bar--scroll-button {
		@apply w-7 h-7 p-0.5 pointer-events-auto;

		svg {
			@apply w-5 h-5;
		}

		&-background {
			@apply h-full absolute top-0.5 z-20 pointer-events-none text-base px-6;
			@apply from-white/0 to-white/90;
		}

		&-background-right {
			@apply bg-gradient-to-r right-0;
		}

		&-background-left {
			@apply bg-gradient-to-l left-0;
		}
	}

</style>
