<template>
	<div :class="{ 'svws-ui-page': !secondary, 'svws-single-tab': tabManager().tabs.length === 1 }">
		<div class="svws-ui-tabs" :class="{ 'svws-ui-tabs--secondary': secondary }">
			<div :id="((tabManager().tabs.length > 1) && enableFocusSwitching) ? (secondary ? 'tabsSecondLevelFocusBorder' : 'tabsFirstLevelFocusBorder') : ''" class="svws-ui-tabs--wrapper focus-region">
				<p v-if="(tabManager().tabs.length > 1) && enableFocusSwitching && secondary" id="tabsSecondLevelFocusNumber" class="region-enumeration">6</p>
				<p v-else-if="(enableFocusSwitching && (tabManager().tabs.length > 1))" id="tabsFirstLevelFocusNumber" class="region-enumeration">5</p>
				<div v-if="state.scrolled" class="svws-ui-tabs--scroll-button -left-1 pl-1 bg-gradient-to-l" @click="scroll('left')">
					<svws-ui-button type="icon">
						<span class="icon i-ri-arrow-left-s-line" />
					</svws-ui-button>
				</div>
				<div ref="tabsListElement" class="svws-ui-tabs--list">
					<template v-for="(tab, index) in props.tabManager().tabs" :key="index">
						<button :id="tab.name === tabManager().tab.name
								? (secondary ? 'tabsSecondLevelFocusField' : 'tabsFirstLevelFocusField')
								: ''" v-if="!(tab.hide === true) && (tab.text !== '')" @click="tabManager().setTab(tab)" class="svws-ui-tab-button flex flex-row"
							:class="{ 'svws-active': tab.name === tabManager().tab.name }">
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
		enableFocusSwitching? : boolean;
	}>(), {
		secondary: false,
		enableFocusSwitching: false,
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
		scrollOffset: 12
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
			behavior: "smooth"
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


<style lang="postcss">

	.svws-ui-page {
		@apply flex flex-col items-start overflow-hidden h-full;
		&.svws-single-tab {
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

		.svws-ui-tabs--secondary {
			@apply px-0;
		}
	}


	.svws-sub-nav-target {
		@apply w-full relative z-30;
	}

	.svws-ui-tabs--wrapper {
		@apply bg-ui;
		@apply flex items-center -mx-3 px-0.5 w-auto relative z-30 flex-shrink-0 overflow-hidden;

		&:before {
			@apply bg-ui-neutral;
			@apply h-[2px] absolute left-0 right-0 bottom-0;
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
			@apply bg-ui-neutral border border-ui-secondary;
			@apply w-5 h-full p-0 rounded;

			&:focus {
				@apply outline-none;
			}

			&:hover,
			&:focus-visible {
				@apply bg-ui-neutral-hover border-ui-neutral-hover;
			}

			&:focus-visible {
				@apply ring ring-ui;
			}
		}
	}

	.svws-sub-nav-target {
		@apply overflow-x-auto flex-shrink-0;
	}

	.svws-ui-secondary-tabs {
		@apply text-button flex gap-[2px] pt-1.5 pb-3 px-1 -mt-px rounded-md;
	}

	.svws-ui-tab-button {
		@apply text-ui bg-ui;
		@apply inline-flex items-center justify-center;
		@apply py-1 px-2;
		@apply rounded;
		@apply select-none;
		@apply text-sm font-bold;
		@apply whitespace-nowrap;
		@apply relative border border-transparent;

		.svws-ui-spinner {
			@apply w-4 h-4 absolute top-1.5 right-0.5;
		}

		&:focus {
			@apply outline-none;
		}

		&:hover,
		&:focus-visible {
			@apply bg-ui-hover text-ui-hover;

			.page--statistik & {
				@apply text-ui-statistic;
			}

		}

		&:active {
			@apply bg-ui-selected text-ui-onselected;

			.page--statistik & {
				@apply bg-ui-statistic-weak text-ui-statistic;
			}
		}

		&:focus-visible {
			@apply ring ring-ui;
			@apply z-10;

			.page--statistik & {
				@apply ring-ui-statistic;
			}
		}

		&.svws-active,
		&.svws-active:hover {
			@apply text-ui-onselected bg-ui-selected;

			.svws-ui-tabs &,
			.svws-ui-secondary-tabs & {
				&:before {
					@apply absolute left-2 right-2 -bottom-2 h-[2px] border-b-2;
					content: '';

					.page--statistik & {
						@apply bg-ui-statistic-weak text-ui-statistic;
					}
				}
			}

			.svws-ui-secondary-tabs & {
				@apply bg-ui;

				&:before {
					@apply -bottom-1;
				}
			}

			.page--statistik & {
				@apply text-ui-statistic;
			}
		}

		&.svws-active {
			.svws-api--pending & {
				span {
					@apply animate-pulse;
				}
			}
		}

		&:disabled {
			@apply bg-transparent text-ui-disabled pointer-events-none;
		}
	}

</style>
