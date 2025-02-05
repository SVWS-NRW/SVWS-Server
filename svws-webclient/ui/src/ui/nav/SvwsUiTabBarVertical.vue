<template>
	<div class="svws-ui-tabs--vertical">
		<div class="tab-bar-vertical--wrapper print:hidden">
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


<style lang="postcss">

	@reference "../../assets/styles/index.css";

	.svws-ui-tabs--vertical {
		@apply flex flex-row grow items-start;
		@apply w-full;
	}

	.tab-bar-vertical--panel {
		@apply grow overflow-y-auto;
	}

	.tab-bar-vertical--wrapper {
		@apply flex flex-col shrink items-start;
		@apply overflow-hidden;
		@apply relative;
		@apply mr-8;
	}

	.tab-bar-vertical--content {
		@apply bg-ui-neutral;
		@apply rounded-md p-[4px];
		@apply flex flex-col;
		@apply overflow-y-scroll;
		@apply relative;
		@apply gap-[2px];

		-ms-overflow-style: none;
		/* Remove Scrollbar in IE and Edge */
		scrollbar-width: none;
		/* Remove Scrollbar in Firefox */

		.svws-ui-tab-button {
			@apply w-full;
		}

		.svws-ui-tab-button.svws-active {
			@apply overflow-hidden bg-ui;

			&:after {
				@apply hidden;
				@apply bottom-0 border-b-0 border-r-2 h-full pointer-events-none;
			}
		}
	}

	.tab-bar-vertical--content::-webkit-scrollbar {
		display: none;
		/* Remove Scrollbar in Chromium basesd Browsers */
	}

	.tab-bar-vertical--scroll-button-background {
		@apply absolute z-20;
		@apply w-full;
		@apply pointer-events-none;
		@apply from-white/0 via-50% via-white to-white dark:from-black/0 dark:via-50% dark:via-black dark:to-black;
	}

	.tab-bar-vertical--scroll-button-background-down {
		@apply bg-gradient-to-b;
		@apply pt-8;
		@apply rounded-b-full;
		bottom: 0.875rem;
	}

	.tab-bar-vertical--scroll-button-background-up {
		@apply bg-gradient-to-t;
		@apply pb-8;
		@apply rounded-t-full;
		top: 0.875rem;
	}

	.tab-bar-vertical--scroll-button {
		@apply w-full;
		@apply inline-flex items-center justify-center;
		@apply pointer-events-auto;
		@apply py-3.5;
		@apply rounded-full;
		@apply text-ui;
	}

	.tab-bar-vertical--scroll-button:focus {
		@apply outline-hidden ring-3 ring-inset ring-ui;
	}

	.tab-bar-vertical--button {
		@apply text-ui;
		@apply inline-flex items-center;
		@apply py-1.5 px-2;
		@apply rounded;
		@apply select-none;
		@apply text-sm font-bold;
		@apply whitespace-nowrap;
		@apply relative border border-transparent;

		.svws-ui-spinner {
			@apply w-4 h-4 absolute top-1.5 right-0.5;
		}

		&:focus {
			@apply outline-hidden;
		}

		&:hover,
		&:focus-visible {
			@apply bg-ui-neutral-hover text-ui-onneutral-hover;

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
			@apply ring-3 ring-ui;
			@apply z-10;

			.page--statistik & {
				@apply ring-ui-statistic;
			}
		}

		&.svws-active,
		&.svws-active:hover {
			@apply text-ui-onselected bg-ui-selected;

			.svws-ui-tabs--vertical & {
				&:before {
					@apply absolute -left-[5px] -top-px -bottom-px w-[2px] border-r-2;
					content: '';

					.page--statistik & {
						@apply bg-ui-statistic-weak text-ui-statistic;
					}
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
