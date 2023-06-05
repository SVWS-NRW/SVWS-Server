<template>
	<div class="router-tab-bar--subnav">
		<slot />
	</div>
</template>

<style lang="postcss">
.router-tab-bar--subnav {
	@apply relative z-10 w-full;
	@apply flex items-center gap-2 flex-shrink-0 min-h-fit;
	@apply bg-light;
	@apply border-b-2 border-light;
	@apply px-6 lg:px-9 3xl:px-12 4xl:px-16;
	@apply h-9 text-sm;

	> * {
		@apply flex-shrink-0;
	}
}
</style>
