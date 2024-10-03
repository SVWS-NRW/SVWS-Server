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
			behavior: "smooth"
		});
	}

</script>


<style lang="postcss">

	.svws-ui-tabs--vertical {
		@apply flex flex-row flex-grow items-start;
		@apply w-full;
	}

	.tab-bar-vertical--panel {
		@apply flex-grow overflow-y-auto;
	}

	.tab-bar-vertical--wrapper {
		@apply flex flex-col flex-shrink items-start;
		@apply overflow-hidden;
		@apply relative;
		@apply mr-8;
	}

	.tab-bar-vertical--content {
		@apply bg-light rounded-md p-[2px];
		@apply flex flex-col items-center;
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
			@apply overflow-hidden bg-white;

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
		@apply from-transparent via-light to-light;
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
		@apply text-black;
	}

	.tab-bar-vertical--scroll-button:focus {
		@apply outline-none ring ring-inset ring-primary ring-opacity-75;
	}

	.tab-bar-vertical--button {
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
