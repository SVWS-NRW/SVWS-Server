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
			<h3 v-if="title" class="headline-5 content-card--headline">
				{{ title }}
			</h3>
			<div v-if="$slots.actions" class="content-card--actions">
				<slot name="actions" />
			</div>
		</div>
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
		@apply inline-flex items-center justify-between px-3 py-1 rounded bg-light mt-6 mb-2 w-auto -mx-1;
	}

	.content-card--header--has-actions {
		@apply flex w-full pr-1;
	}

	.content-card--actions {
		@apply flex items-center space-x-2;
	}
</style>
