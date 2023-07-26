<template>
	<div class="router-tab-bar--subnav">
		<slot />
	</div>
</template>

<style lang="postcss">
.router-tab-bar--subnav {
	@apply relative z-10 w-full min-h-fit h-9;
	@apply flex items-center gap-2 flex-shrink-0;
	@apply text-sm bg-light dark:bg-white/5 border-b-2 border-light dark:border-white/5;
	@apply px-6 lg:px-9 3xl:px-12 4xl:px-20;

	> * {
		@apply flex-shrink-0;
	}
}
</style>
