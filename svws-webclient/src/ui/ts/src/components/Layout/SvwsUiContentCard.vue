<script setup lang='ts'>
	const props = withDefaults(defineProps<{
		title?: string;
		overflowScroll?: boolean;
	}>(), {
		title: '',
		overflowScroll: false
	});
</script>

<template>
	<div class="content-card" :class="{'h-full': overflowScroll}">
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
		<div class="content-card--content" :class="{'content-card--content--with-title': title || $slots.title, 'content-card--content--overflow-scroll': overflowScroll}">
			<slot />
		</div>
	</div>
</template>

<style lang="postcss">
	.content-card {
		@apply h-fit;

		&:not(:last-child) {
			@apply max-lg:pb-12;
		}

		&--content {
			&--with-title {
				@apply mt-4;
			}

			&--overflow-scroll {
				@apply h-full flex flex-col;
			}
		}

		&--headline {
			@apply text-black;
			@apply flex-shrink-0;
		}

		&--header {
			@apply inline-flex items-center justify-between mb-1 py-1 w-auto;

			&--has-actions {
				@apply flex flex-wrap gap-x-4 gap-y-2 w-full items-start;
			}
		}

		&--actions {
			@apply flex items-center space-x-2;
		}
	}

	.content-card--wrapper--light-bg .content-card--header {
		@apply bg-white;
	}

	.page--wrapper[class*="gost_kursplanung_halbjahr_ergebnis"] .router-tab-bar--panel {
		@apply overflow-hidden;
	}
</style>
