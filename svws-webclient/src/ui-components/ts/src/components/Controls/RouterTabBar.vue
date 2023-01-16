<template>
	<div class="router-tab-bar--area">
		<div class="router-tab-bar--wrapper print:hidden">
			<div v-if="state.scrolled" class="router-tab-bar--scroll-button-background router-tab-bar--scroll-button-background-left">
				<button class="router-tab-bar--scroll-button" @click="scroll('left')">
					<Icon> <i-ri-arrow-left-line /> </Icon>
				</button>
			</div>
			<div ref="contentEl" class="router-tab-bar--content">
				<router-tab-bar-button v-for="(route, index) in props.routes" :route="route" :selected="selected"
					:hidden="isHidden(index)" @select="select(route)" />
			</div>
			<div v-if="!state.scrolledMax"
				class="router-tab-bar--scroll-button-background router-tab-bar--scroll-button-background-right">
				<button class="router-tab-bar--scroll-button" @click="scroll('right')">
					<Icon> <i-ri-arrow-right-line /> </Icon>
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

	const props = defineProps<{
		routes: RouteRecordRaw[]
		hidden: boolean[] | undefined
		modelValue: RouteRecordRaw
	}>();

	const emit = defineEmits<{ (e: 'update:modelValue', value: RouteRecordRaw): void, }>();

	type ComponentData = {
		scrolled: boolean;
		scrolledMax: boolean;
		scrollFactor: number;
		maxScrollLeft: number;
	}

	const contentEl = ref();
	const selected: WritableComputedRef<RouteRecordRaw> = computed({
		get() : RouteRecordRaw {
			return props.modelValue;
		},
		set(value: RouteRecordRaw ) {
			emit('update:modelValue', value);
		}
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

	function select(route: RouteRecordRaw) {
		selected.value = route;
	}

</script>


<style lang="postcss">
    .router-tab-bar--area {
        @apply flex flex-col items-start
    }

    .router-tab-bar--panel {
        @apply h-full;
        @apply mt-0 mb-8 flex-grow overflow-y-auto px-6 flex-auto w-full;
    }

	.router-tab-bar--area {
		@apply h-full overflow-y-auto;
		-webkit-overflow-scrolling: touch;
	}

    .router-tab-bar--wrapper {
		@apply flex items-center;
		@apply overflow-hidden;
		@apply relative;
		@apply rounded-full;
		@apply w-full;
		@apply flex-shrink-0;
		@apply px-4 mb-6;
		@apply sticky top-0 left-0 z-20 bg-white;
		box-shadow: 0 0 1.5rem 1rem theme("colors.white");
    }

    .router-tab-bar--content {
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

    .router-tab-bar--content::-webkit-scrollbar {
        display: none;
        /* Remove Scrollbar in Chromium basesd Browsers */
    }

    .router-tab-bar--scroll-button-background {
		@apply absolute z-20;
		@apply h-full;
		@apply pointer-events-none;
		@apply from-transparent via-light to-light;
    }

    .router-tab-bar--scroll-button-background-right {
		@apply bg-gradient-to-r;
		@apply pl-8;
		@apply rounded-r-full;
		right: 0.875rem;
    }

    .router-tab-bar--scroll-button-background-left {
		@apply bg-gradient-to-l;
		@apply pr-8;
		@apply rounded-l-full;
		left: 0.875rem;
    }

    .router-tab-bar--scroll-button {
		@apply h-full;
		@apply inline-flex items-center justify-center;
		@apply pointer-events-auto;
		@apply px-3.5;
		@apply rounded-full;
		@apply text-black;
    }

    .router-tab-bar--scroll-button:focus {
		@apply outline-none ring ring-inset ring-primary ring-opacity-75;
    }

</style>
