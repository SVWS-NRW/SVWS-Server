<template>
	<div class="dashboard-tile"
		:class="{
			'dashboard-tile--dark': color === 'dark',
			'dashboard-tile--transparent': color === 'transparent',
			'col-span-2': span === 2,
			'col-span-full': span === 'full',
		}">
		<div v-if="title || $slots.title" class="dashboard-tile--title">
			<slot name="title">
				{{ title }}
			</slot>
		</div>
		<slot />
		<div v-if="number || numberLabel" class="dashboard-tile--number">
			<span v-if="number" class="line-clamp-1">{{ number }}</span>
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
	}>(), {
		color: 'light',
		span: 1,
	});
</script>

<style lang="postcss">
	.dashboard-tile {
		@apply flex flex-col bg-violet-100 border border-violet-100 dark:border-violet-800 dark:bg-violet-900 dark:text-white rounded-lg p-3 sm:p-4 md:p-5;
		@apply text-base md:text-headline md:font-normal;

		.dashboard-tile--title {
			@apply text-sm md:text-headline-md mb-1 font-bold;
		}

		.dashboard-tile--number {
			@apply mt-auto pt-5 text-violet-400 dark:text-violet-400 flex flex-col font-bold leading-none text-headline-xl;

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
				@apply text-violet-300 dark:text-violet-400;
			}
		}

		&--transparent {
			@apply bg-transparent dark:bg-transparent border-transparent dark:border-transparent px-0;
		}

		.data-table {
			@apply text-base;

			.data-table__thead {
				@apply text-sm;
			}
		}
	}
</style>
