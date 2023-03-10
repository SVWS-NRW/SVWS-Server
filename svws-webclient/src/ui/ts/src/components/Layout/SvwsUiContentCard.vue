<script setup lang='ts'>
	const props = withDefaults(defineProps<{
		title?: string;
	}>(), {
		title: '',
	});
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
		<div :class="{'mt-4': title}">
			<slot />
		</div>
	</div>
</template>

<style lang="postcss">
	.content-card--wrapper {
		@apply h-fit;

		&:not(:last-child) {
			@apply max-lg:pb-12;
		}
	}

	.content-card--headline {
		@apply text-black;
		@apply flex-shrink-0;
	}

	.content-card--header {
		@apply inline-flex items-center justify-between mb-1 py-1 w-auto;
	}

	.content-card--wrapper--light-bg .content-card--header {
		@apply bg-white;
	}

	.content-card--header--has-actions {
		@apply flex w-full items-start;
	}

	.content-card--actions {
		@apply flex items-center space-x-2;
	}

	.page-wrapper[class*="gost_kursplanung_halbjahr_ergebnis"] .router-tab-bar--panel {
		@apply overflow-hidden;
	}
</style>
