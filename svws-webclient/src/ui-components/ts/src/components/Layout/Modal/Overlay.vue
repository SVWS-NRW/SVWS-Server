<template>
	<div class="overlay"></div>
</template>

<style>
	.overlay {
		@apply backdrop-blur-[2px] backdrop-filter;
		@apply bg-dark-20 bg-opacity-80;
		@apply absolute top-0 left-0;
		@apply h-full w-full;
		@apply z-50;
	}
</style>
