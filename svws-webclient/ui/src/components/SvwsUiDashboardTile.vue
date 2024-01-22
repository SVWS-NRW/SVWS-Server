<template>
	<div class="dashboard-tile"
		:class="{
			'dashboard-tile--dark': color === 'dark',
			'dashboard-tile--transparent': color === 'transparent',
			'col-span-2': span === 2,
			'col-span-full': span === 'full',
			'dashboard-tile--clickable': clickable,
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
		numberLabel: undefined
	});
</script>

<style lang="postcss">
	.dashboard-tile {
		@apply flex flex-col bg-violet-100 border border-violet-100 dark:border-violet-800 dark:bg-violet-900 dark:text-white rounded-lg p-3 sm:p-4 md:p-5;
		@apply text-base md:text-headline md:font-normal;

		.dashboard-tile--title {
			@apply text-sm md:text-headline-md mb-1.5 font-medium md:font-medium leading-none md:leading-none;

			+ .dashboard-tile--number {
				@apply pt-5;
			}
		}

		.dashboard-tile--number {
			@apply mt-auto text-violet-500 dark:text-violet-400 flex flex-col font-bold leading-none text-headline-xl;

			@media (min-width: theme('screens.md')) {
				font-size: 4rem;
			}
		}

		.dashboard-tile--number-label {
			@apply text-base md:text-headline-md font-bold relative mb-1;
		}

		&--dark {
			@apply bg-violet-600 dark:bg-violet-950 text-white font-medium border-transparent dark:border-transparent;

			.dashboard-tile--number {
				@apply text-violet-200 dark:text-violet-400;
			}
		}

		&--transparent {
			@apply bg-transparent dark:bg-transparent border-transparent dark:border-transparent px-2;
		}

		&--clickable {
			@apply cursor-pointer;

			&:hover,
			&:focus-visible {
				/*@apply filter brightness-105;*/
				@apply ring ring-violet-500/25 dark:ring-violet-400/25;
			}

			&.dashboard-tile--dark {
				&:hover,
				&:focus-visible {
					@apply ring ring-violet-800/25 dark:ring-violet-400/25;
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
