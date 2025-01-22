<template>
	<div class="svws-ui-dashboard-tile"
		:class="{
			'svws-ui-dashboard-tile--dark': color === 'dark',
			'svws-ui-dashboard-tile--transparent': color === 'transparent',
			'col-span-2': span === 2,
			'col-span-full': span === 'full',
			'svws-ui-dashboard-tile--clickable': clickable,
		}">
		<div v-if="title || $slots.title" class="dashboard-tile--title">
			<slot name="title">
				{{ title }}
			</slot>
		</div>
		<slot />
		<div v-if="(number || $slots.number) || numberLabel" class="dashboard-tile--number">
			<slot name="number">
				<span v-if="number" class="line-clamp-1">{{ number }}</span>
			</slot>
			<span v-if="numberLabel" class="dashboard-tile--number-label">{{ numberLabel }}</span>
		</div>
	</div>
</template>

<script setup lang='ts'>
	const props = withDefaults(defineProps<{
		color?: 'light' | 'dark' | 'transparent';
		span?: 1 | 2 | 'full';
		title?: string;
		number?: string;
		numberLabel?: string;
		clickable?: boolean;
	}>(), {
		color: 'light',
		span: 1,
		clickable: false,
		title: undefined,
		number: undefined,
		numberLabel: undefined,
	});
</script>

<style lang="postcss">
	.svws-ui-dashboard {
		@apply grid grid-cols-2 lg:grid-cols-4 gap-2 md:gap-4 grid-flow-dense;
	}

	.svws-ui-dashboard-tile {
		@apply bg-ui-brand-weak text-ui;
		@apply rounded-lg p-3 sm:p-4 md:p-5 flex flex-col;
		@apply text-base md:text-headline md:font-normal;

		.page--statistik & {
			@apply bg-ui-statistic-weak;
		}

		.dashboard-tile--title {
			@apply text-ui-brand;
			@apply text-sm md:text-headline-md mb-0.5 font-medium md:font-medium leading-none md:leading-none;

			.page--statistik & {
				@apply text-ui-statistic;
			}

			+ .dashboard-tile--number {
				@apply pt-5;
			}
		}

		.dashboard-tile--number {
			@apply text-ui-brand;
			@apply mt-auto flex flex-col font-bold leading-none text-headline-xl;

			@media (min-width: theme('screens.md')) {
				font-size: 4rem;
			}

			.page--statistik & {
				@apply text-ui-statistic;
			}
		}

		.dashboard-tile--number-label {
			@apply text-base md:text-headline-md font-bold relative mb-1;
		}

		&--transparent,
		.page--statistik &--transparent {
			@apply bg-transparent;
			@apply px-2;
		}

		&--dark {
			@apply bg-ui-brand text-ui-onbrand;
			@apply font-medium;

			.dashboard-tile--title {
				@apply text-ui-onbrand-secondary;

				.page--statistik & {
					@apply text-ui-onstatistic-secondary;
				}
			}

			.dashboard-tile--number {
				@apply text-ui-onbrand-secondary;
			}

			.page--statistik & {
				@apply bg-ui-statistic text-ui-onstatistic;

				.dashboard-tile--number {
					@apply text-ui-onstatistic-secondary;
				}
			}
		}

		&--clickable {
			@apply cursor-pointer;

			&:hover,
			&:focus-visible {
				@apply bg-ui-brand-hover text-ui-onbrand;

				.page--statistik & {
					@apply bg-ui-statistic-hover text-ui-onstatistic;
				}

				.dashboard-tile--title {
					@apply text-ui-onbrand-secondary-hover;

					.page--statistik & {
						@apply text-ui-onstatistic-secondary-hover;
					}
				}
			}
		}

		.data-table {
			@apply text-base;

			.data-table__thead {
				@apply text-sm;
			}
		}
	}
</style>
