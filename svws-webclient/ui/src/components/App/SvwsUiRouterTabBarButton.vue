<template>
	<button v-if="(route.text !== '') && !hidden" @click="select()" class="svws-ui-tab-button flex flex-row" :class="{ 'svws-active': isSelected }">
		<span>{{ route.text }}</span>
		<!--<span class="icon i-ri-loader-4-line svws-ui-spinner"v-if="isSelected" />-->
		<template v-if="$slots.badge">
			<slot name="badge" />
		</template>
	</button>
</template>

<script lang="ts" setup>

	import { computed } from 'vue';
	import type { TabData } from './TabData';

	const emit = defineEmits<{
		(e: 'select', value: TabData) : void;
	}>();

	const props = defineProps<{
		route: TabData;
		selected: TabData;
		hidden?: boolean;
	}>();

	const isSelected = computed<boolean>(() => props.route.name.toString() === props.selected.name.toString());

	function select() {
		emit('select', props.route);
	}

</script>


<style lang="postcss">

	.svws-ui-tab-button {
		@apply bg-ui text-ui border border-transparent;
		@apply inline-flex items-center justify-center;
		@apply py-1 px-2;
		@apply rounded;
		@apply select-none;
		@apply text-sm font-bold;
		@apply whitespace-nowrap;
		@apply relative;

		.svws-ui-tabs--vertical & {
			@apply py-1.5 px-2.5;
		}

		.svws-ui-spinner {
			@apply w-4 h-4 absolute top-1.5 right-0.5;
		}

		&:hover {
			@apply bg-ui-hover text-ui-hover;

			&:active {
				@apply bg-ui-neutral;
			}
		}

		&:focus-visible {
			@apply ring ring-ui;
			.page--statistik & {
				@apply ring-ui-statistic;
			}
		}

		&:focus {
			@apply outline-none;
		}

		&.svws-active,
		&.svws-active:hover {
			@apply outline-none text-ui-brand;
			.svws-ui-tabs--vertical & {
				@apply bg-ui shadow;
			}
			.svws-ui-tabs &,
			.svws-ui-secondary-tabs & {
				&:before {
					@apply absolute left-2 right-2 -bottom-2 h-[2px] bg-ui-brand;
					content: '';
					.page--statistik & {
						@apply bg-ui-statistic;
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

		&.svws-active:hover {
			.svws-ui-tabs &,
			.svws-ui-secondary-tabs & {
				@apply bg-ui-brand-weak;

				.page--statistik & {
					@apply bg-ui-statistic-weak;
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
			@apply bg-transparent dark:bg-transparent text-ui;
			@apply opacity-20;
			@apply cursor-not-allowed pointer-events-none;
		}
	}

</style>
