<template>
	<div class="overlay"></div>
</template>

<style>
	.overlay {
		@apply backdrop-blur-sm backdrop-filter;
		@apply bg-white bg-opacity-50;
		@apply absolute top-0 left-0;
		@apply h-full w-full;
		@apply z-50;
	}
</style>
