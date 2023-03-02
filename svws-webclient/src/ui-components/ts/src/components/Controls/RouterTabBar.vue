<template>
	<div class="router-tab-bar--area">
		<div class="router-tab-bar--wrapper print:hidden">
			<div v-if="state.scrolled" class="router-tab-bar--scroll-button-background router-tab-bar--scroll-button-background-left">
				<button class="router-tab-bar--scroll-button" @click="scroll('left')">
					<Icon> <i-ri-arrow-left-s-line /> </Icon>
				</button>
			</div>
			<div ref="contentEl" class="router-tab-bar--content">
				<router-tab-bar-button v-for="(route, index) in props.routes" :route="route" :selected="selected"
					:hidden="isHidden(index)" @select="select(route)" :key="index" />
			</div>
			<div v-if="!state.scrolledMax"
				class="router-tab-bar--scroll-button-background router-tab-bar--scroll-button-background-right">
				<button class="router-tab-bar--scroll-button" @click="scroll('right')">
					<Icon> <i-ri-arrow-right-s-line /> </Icon>
				</button>
			</div>
		</div>
		<div class="router-tab-bar--panel">
			<slot />
		</div>
	</div>
</template>

<script lang="ts" setup>
	import { computed, onMounted, onUnmounted, onUpdated, ref, WritableComputedRef } from 'vue';
	import { RouteRecordRaw } from "vue-router";
	import { AuswahlChildData } from '~/types';

	const props = defineProps<{
		routes: RouteRecordRaw[] | AuswahlChildData[]
		hidden: boolean[] | undefined
		modelValue: RouteRecordRaw | AuswahlChildData
	}>();

	const emit = defineEmits<{ (e: 'update:modelValue', value: RouteRecordRaw | AuswahlChildData): void, }>();

	type ComponentData = {
		scrolled: boolean;
		scrolledMax: boolean;
		scrollFactor: number;
		maxScrollLeft: number;
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
		state.value.scrolled = (contentEl.value?.scrollLeft ?? 0) > 0;
		state.value.maxScrollLeft =
			(contentEl.value?.scrollWidth ?? 0) - (contentEl.value?.clientWidth ?? 0);
		state.value.scrolledMax = (contentEl.value?.scrollLeft ?? 0) >= state.value.maxScrollLeft;
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

</script>


<style lang="postcss">
    .router-tab-bar--area {
        @apply flex flex-col items-start overflow-hidden;
    }

    .router-tab-bar--panel {
        @apply h-full;
        @apply flex-grow overflow-auto flex-auto w-full;
		@apply px-6 lg:px-9 3xl:px-12 4xl:px-16;
		@apply py-8;
		@apply relative;
		-webkit-overflow-scrolling: touch;
    }

	.router-tab-bar--area {
		@apply h-full;
	}

    .router-tab-bar--wrapper {
		@apply flex items-center;
		@apply relative z-50;
		@apply w-full;
		@apply flex-shrink-0;

		&:after {
			@apply absolute inset-x-0 bottom-0;
			@apply border-b-2 border-light;
			@apply pointer-events-none;
			content: "";
		}
	}

    .router-tab-bar--content {
		@apply flex flex-row items-center;
		@apply overflow-x-scroll pb-2;
		@apply relative w-full space-x-2;
		@apply px-6 lg:px-9 3xl:px-12 4xl:px-16;
		-webkit-overflow-scrolling: touch;

		-ms-overflow-style: none;
		/* Remove Scrollbar in IE and Edge */
		scrollbar-width: none;
		/* Remove Scrollbar in Firefox */
    }

    .router-tab-bar--content::-webkit-scrollbar {
        display: none;
        /* Remove Scrollbar in Chromium basesd Browsers */
    }

    .router-tab-bar--scroll-button-background {
		@apply absolute z-20;
		@apply h-full;
		@apply pointer-events-none;
		@apply text-base px-4 pb-2;
		@apply from-white/0 via-white/75 to-white/75;
    }

    .router-tab-bar--scroll-button-background-right {
		@apply bg-gradient-to-r;
		@apply right-0;
    }

    .router-tab-bar--scroll-button-background-left {
		@apply bg-gradient-to-l;
		@apply left-0;
    }

    .router-tab-bar--scroll-button {
		@apply h-full;
		@apply inline-flex items-center justify-center;
		@apply pointer-events-auto;
		@apply px-2;
		@apply text-black;
		@apply rounded-md;
    }

    .router-tab-bar--scroll-button:focus {
		@apply outline-none ring ring-primary ring-opacity-50 ring-inset;
    }

	.router-tab-bar--subnav {
		@apply absolute inset-0 z-20;
		@apply flex items-center gap-2;
		@apply bg-light bg-opacity-50;
		@apply border-b-2 border-light;
		@apply px-6 lg:px-9 3xl:px-12 4xl:px-16;
		@apply h-9 text-sm;
	}

</style>
