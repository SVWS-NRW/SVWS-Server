<template>
	<div :class="{ 'svws-ui-page': !secondary, 'svws-single-tab': tabManager().tabs.length === 1 }">
		<div class="svws-ui-tabs" :class="{ 'svws-ui-tabs--secondary': secondary }">
			<div class="svws-ui-tabs--wrapper">
				<div v-if="state.scrolled" class="svws-ui-tabs--scroll-button -left-1 pl-1 bg-gradient-to-l" @click="scroll('left')">
					<svws-ui-button type="icon">
						<span class="icon i-ri-arrow-left-s-line" />
					</svws-ui-button>
				</div>
				<div ref="tabsListElement" class="svws-ui-tabs--list">
					<template v-for="(tab, index) in props.tabManager().tabs" :key="index">
						<button v-if="!(tab.hide === true) && (tab.text !== '')" @click="tabManager().setTab(tab)" class="svws-ui-tab-button flex flex-row"
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
	}>(), {
		secondary: false,
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
		@apply text-button flex gap-[2px] pt-1.5 pb-3 px-1 -mt-px rounded-md;
	}

	.svws-ui-tab-button {
		@apply inline-flex items-center justify-center;
		@apply py-1 px-2;
		@apply rounded;
		@apply select-none;
		@apply text-sm font-bold text-black dark:text-white;
		@apply whitespace-nowrap;
		@apply relative border border-transparent;

		.svws-ui-tabs--vertical & {
			@apply py-1.5 px-2.5;
		}

		.svws-ui-spinner {
			@apply w-4 h-4 absolute top-1.5 right-0.5;
		}

		&:hover {
			@apply bg-black/10 dark:bg-white/10;
			&:active {
				@apply bg-black/20 dark:bg-white/20;
			}
		}

		&:focus-visible {
			@apply ring-2 ring-svws/50;
			.page--statistik & {
				@apply ring-violet-500/50;
			}
		}

		&:focus {
			@apply outline-none;
		}

		&.svws-active,
		&.svws-active:hover {
			@apply outline-none text-svws;
			.svws-ui-tabs--vertical & {
				@apply bg-white dark:bg-black shadow;
			}
			.svws-ui-tabs &,
			.svws-ui-secondary-tabs & {
				&:before {
					@apply absolute left-2 right-2 -bottom-2 h-[2px] bg-svws;
					content: '';
					.page--statistik & {
						@apply bg-violet-500;
					}
				}
			}
			.svws-ui-secondary-tabs & {
				@apply bg-white dark:bg-black;
				&:before {
					@apply -bottom-1;
				}
			}
			.page--statistik & {
				@apply text-violet-500;
			}
		}

		&.svws-active:hover {
			.svws-ui-tabs &,
			.svws-ui-secondary-tabs & {
				@apply bg-svws/5 dark:bg-svws/10;

				.page--statistik & {
					@apply bg-violet-500/5 dark:bg-violet-500/10;
				}
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
			@apply bg-transparent dark:bg-transparent text-black dark:text-white;
			@apply opacity-20;
			@apply cursor-not-allowed pointer-events-none;
		}
	}

</style>
