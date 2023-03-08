<script lang="ts" setup>
	import { TabGroup, TabList, TabPanels } from '@headlessui/vue';

	const {
		modelValue = 0,
	} = defineProps<{
		modelValue?: number;
	}>();

	const emit = defineEmits<{
		(e: 'update:modelValue', value: number): void,
	}>();

	type ComponentData = {
		scrolled: boolean;
		scrolledMax: boolean;
		scrollFactor: number;
		maxScrollLeft: number;
		tabs: HTMLElement[];
	}

	const contentEl = ref();
	const selected = computed({
		get() {
			return modelValue;
		},
		set(value: number) {
			emit('update:modelValue', value);
		}
	});

	const state = ref<ComponentData>({
		scrolled: false,
		scrolledMax: false,
		scrollFactor: 4,
		maxScrollLeft: 0,
		tabs: [],
	});

	onMounted(() => {
		state.value.maxScrollLeft =
			(contentEl.value?.scrollWidth ?? 0) - (contentEl.value?.clientWidth ?? 0);
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

	function changeTab(index: number) {
		selected.value = index
	}

</script>

<template>
	<TabGroup :selected-index="selected" @change="changeTab">
		<TabList as="div" class="tab-bar--wrapper print:hidden">
			<div v-if="state.scrolled" class="tab-bar--scroll-button-background tab-bar--scroll-button-background-left">
				<button class="tab-bar--scroll-button" @click="scroll('left')">
					<svws-ui-icon>
						<i-ri-arrow-left-line />
					</svws-ui-icon>
				</button>
			</div>
			<div ref="contentEl" class="tab-bar">
				<slot name="tabs" />
			</div>
			<div v-if="!state.scrolledMax"
				class="tab-bar--scroll-button-background tab-bar--scroll-button-background-right">
				<button class="tab-bar--scroll-button" @click="scroll('right')">
					<svws-ui-icon>
						<i-ri-arrow-right-line />
					</svws-ui-icon>
				</button>
			</div>
		</TabList>
		<TabPanels as="template">
			<slot name="panels" />
		</TabPanels>
	</TabGroup>
</template>

<style lang="postcss">
.tab-bar--wrapper {
    @apply flex items-center;
    @apply overflow-hidden;
    @apply relative;
    @apply rounded-full;
    @apply w-full;
    @apply flex-shrink-0;
    @apply px-4 mb-6;
}

.tab-bar {
	@apply bg-light;
    @apply flex flex-row items-center;
    @apply overflow-x-scroll;
    @apply relative;
    @apply rounded-full;
    @apply space-x-2 p-1;
    @apply w-full;

    -ms-overflow-style: none;
    /* Remove Scrollbar in IE and Edge */
    scrollbar-width: none;
    /* Remove Scrollbar in Firefox */
}

.tab-bar::-webkit-scrollbar {
    display: none;
    /* Remove Scrollbar in Chromium basesd Browsers */
}

.tab-bar--scroll-button-background {
    @apply absolute z-20;
    @apply h-full;
    @apply pointer-events-none;
    @apply from-transparent via-light to-light;
}

.tab-bar--scroll-button-background-right {
    @apply bg-gradient-to-r;
    @apply pl-8;
    @apply rounded-r-full;
	right: 0.875rem;
}

.tab-bar--scroll-button-background-left {
    @apply bg-gradient-to-l;
    @apply pr-8;
    @apply rounded-l-full;
	left: 0.875rem;
}

.tab-bar--scroll-button {
    @apply h-full;
    @apply inline-flex items-center justify-center;
    @apply pointer-events-auto;
    @apply px-3.5;
    @apply rounded-full;
    @apply text-black;
}

.tab-bar--scroll-button:focus {
    @apply outline-none ring ring-inset ring-primary ring-opacity-75;
}
</style>
