<script setup lang='ts'>
	const {
		title = '',
	} = defineProps<{
		title?: string;
	}>();
</script>

<template>
	<div class="content-card--wrapper">
		<div v-if="title || $slots.actions" class="content-card--header" :class="{
			'content-card--header--has-actions': $slots.actions
		}">
			<h3 v-if="title" class="content-card--headline text-headline-sm">
				{{ title }}
			</h3>
			<div v-if="$slots.actions" class="content-card--actions">
				<slot name="actions" />
			</div>
		</div>
		<slot name="title" />
		<div class="mt-4">
			<slot />
		</div>
	</div>
</template>

<style lang="postcss">
	.content-card--wrapper {
		@apply h-fit;
	}

	.content-card--headline {
		@apply text-black;
		@apply flex-shrink-0;
	}

	.content-card--header {
		@apply inline-flex items-center justify-between px-3 py-1 rounded bg-light mt-6 mb-2 w-auto;
	}

	.content-card--wrapper--light-bg .content-card--header {
		@apply bg-white;
	}

	.content-card--header--has-actions {
		@apply flex w-full pr-1;
	}

	.content-card--actions {
		@apply flex items-center space-x-2;
	}

	.page-wrapper[class*="gost_kursplanung_halbjahr_ergebnis"] .router-tab-bar--panel {
		@apply overflow-hidden;
	}

	.content-card--blockungsuebersicht {
		@apply gap-x-16;

		.content-card--wrapper {
			@apply h-full;

			> .mt-4 {
				@apply pt-4 mt-0 h-full flex flex-col;
			}

			.v-table--container {
				@apply h-full overflow-y-scroll;
			}
		}

		.content-card--header {
			@apply mt-0 mb-0;
		}

	}
</style>
