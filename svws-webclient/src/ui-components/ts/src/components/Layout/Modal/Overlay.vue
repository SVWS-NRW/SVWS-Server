<template>
	<div class="overlay"></div>
</template>

<style>
	.overlay {
		@apply backdrop-blur backdrop-filter;
		@apply bg-dark-80 bg-opacity-50;
		@apply absolute top-0 left-0;
		@apply h-full w-full;
		@apply z-50;
	}
</style>
