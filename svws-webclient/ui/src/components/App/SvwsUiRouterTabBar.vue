<template>
	<div class="svws-ui-page" :class="{'svws-single-route': props.routes.length === 1}">
		<div class="svws-ui-tabs">
			<div class="svws-ui-tabs--wrapper">
				<div v-if="state.scrolled" class="svws-ui-tabs--scroll-button left-0 pl-1 bg-gradient-to-l" @click="scroll('left')">
					<svws-ui-button type="icon">
						<i-ri-arrow-left-s-line />
					</svws-ui-button>
				</div>
				<div ref="tabsListElement" class="svws-ui-tabs--list">
					<svws-ui-router-tab-bar-button v-for="(route, index) in props.routes" :route="route" :selected="selected"
						:hidden="isHidden(index)" @select="select(route)" :key="index" />
				</div>
				<div v-if="!state.scrolledMax" class="svws-ui-tabs--scroll-button right-0 pr-1 bg-gradient-to-r justify-end" @click="scroll('right')">
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
        .svws-ui-tabs {
          @apply hidden;
        }

        .svws-ui-tab-content .page--content {
          @apply pt-0;
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
        @apply px-6 lg:px-9 3xl:px-12 4xl:px-20 max-w-full;
        @apply print:hidden;
    }

	.svws-sub-nav-target {
		@apply w-full;
	}

    .svws-ui-tabs--wrapper {
        @apply flex items-center -mx-3 rounded-md w-auto relative z-30 flex-shrink-0 overflow-hidden;
        @apply bg-light dark:bg-white/5;
    }

    .svws-ui-tabs--list {
        @apply flex flex-row items-center relative w-full gap-x-[2px] p-[2px] overflow-x-scroll;
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
      @apply absolute z-20 top-0 text-base h-full flex items-center w-12 py-1 cursor-pointer;
      @apply from-light/0 via-60% via-light to-light;

      .button {
        @apply w-4 h-full p-0 rounded;
      }
    }

    .svws-sub-nav-target {
      @apply overflow-x-auto flex-shrink-0;
    }

    .svws-ui-title-tabs {
      @apply text-headline-md flex flex-wrap gap-5 mb-7 -mt-1 leading-none rounded-md;
    }

    /*.svws-ui-title-tab {
      @apply cursor-pointer py-1.5 px-3 rounded-md relative focus:outline-none focus-visible:ring-2 whitespace-nowrap;

      &:not(.svws-active) {
        @apply hover:bg-black/10 dark:hover:bg-white/10 focus-visible:bg-black/10 dark:focus-visible:bg-white/25 focus-visible:ring-black/25 text-black dark:text-white font-medium;
      }

      &.svws-active {
        @apply bg-white dark:bg-black text-svws shadow focus-visible:ring-svws/25;
      }
    }*/

    .svws-ui-title-tab {
      @apply cursor-pointer py-1 relative focus:outline-none focus-visible:ring focus-visible:rounded-md ring-offset-1 whitespace-nowrap border-b-2 border-transparent leading-tight;

      &:not(.svws-active) {
        @apply hover:border-black/10 dark:hover:border-white/10 focus-visible:ring-black/25 text-black dark:text-white font-medium;
      }

      &.svws-active {
        @apply border-svws text-svws focus-visible:border-transparent;
      }
    }

</style>
