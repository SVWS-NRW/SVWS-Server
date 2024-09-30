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
	import type { AuswahlChildData } from '../../types';

	const emit = defineEmits<{
		(e: 'select', value: AuswahlChildData) : void;
	}>();

	const props = defineProps<{
		route: AuswahlChildData;
		selected: AuswahlChildData;
		hidden?: boolean;
	}>();

	const isSelected = computed<boolean>(() => props.route.name.toString() === props.selected.name.toString());

	function select() {
		emit('select', props.route);
	}

</script>


<style lang="postcss">

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
