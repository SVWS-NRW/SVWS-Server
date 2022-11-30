<script setup lang='ts'>
	const {
		title = '',
	} = defineProps<{
		title?: string;
	}>();
</script>

<template>
	<div class="content-card--wrapper">
		<div class="content-card--header">
			<h5 class="headline-5 content-card--headline">
				{{ title }}
			</h5>
			<div class="content-card--actions">
				<slot name="actions" />
			</div>
		</div>
		<div class="mt-4">
			<slot />
		</div>
	</div>
</template>

<style>
	.content-card--headline {
		@apply text-black;
		@apply flex-shrink-0;
	}

	.content-card--header {
		@apply flex items-center justify-between;
	}

	.content-card--actions {
		@apply flex items-center space-x-2;
	}
</style>
