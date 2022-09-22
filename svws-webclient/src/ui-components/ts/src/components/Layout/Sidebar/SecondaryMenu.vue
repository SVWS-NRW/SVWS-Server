<template>
	<div class="secondary-menu">
		<h3 class="secondary-menu--headline">
			<slot name="headline" />
		</h3>
		<div class="secondary-menu--header">
			<slot name="header" />
		</div>
		<div class="secondary-menu--content">
			<slot name="content" />
		</div>
	</div>
</template>

<style>
.secondary-menu--headline {
	@apply px-6 text-headline-3 font-bold text-dark;
	@apply relative z-20;
}

.secondary-menu {
	@apply relative;
	@apply bg-light;
	@apply border-r-2 border-dark-20;
	@apply flex flex-shrink-0 flex-col;
	@apply h-screen w-1/4 min-w-84 max-w-116;
	@apply overflow-y-auto;
	@apply pt-4;
	@apply shadow;
}

.secondary-menu--header {
	@apply flex-shrink-0;
	@apply relative z-10;
}

.secondary-menu--content {
	@apply flex-grow;
	@apply mt-4;
	@apply overflow-y-auto;
	@apply relative z-0;
}
</style>
