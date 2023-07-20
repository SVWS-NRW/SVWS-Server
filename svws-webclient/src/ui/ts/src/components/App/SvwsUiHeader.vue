<template>
	<div class="page--header">
		<h2 class="text-headline">
			<slot />
		</h2>
	</div>
</template>

<script setup lang="ts">
</script>

<style>
	.page--header {
		@apply flex flex-row items-center justify-start;
		@apply px-6 lg:px-9 3xl:px-12 4xl:px-20;
		@apply py-2 space-x-2;
		@apply flex-shrink-0;
	}

	.tab-container .page--header {
		@apply px-0;
	}
</style>
