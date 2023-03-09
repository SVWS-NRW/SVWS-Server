<template>
	<div class="header--wrapper">
		<h2 class="text-headline">
			<slot />
		</h2>
	</div>
</template>

<script setup lang="ts">
</script>

<style>
	.header--wrapper {
		@apply flex flex-row items-center justify-start;
		@apply px-6 lg:px-9 3xl:px-12 4xl:px-16;
		@apply py-2 space-x-2;
		@apply flex-shrink-0;
	}

	.tab-container .header--wrapper {
		@apply px-0;
	}
</style>
