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
	@apply py-2 px-6 text-headline;
	@apply relative z-20;
	@apply flex items-center;
	min-height: 6.5rem;
}

.secondary-menu {
	@apply relative;
	@apply bg-light;
	@apply border-x border-dark-20;
	@apply flex flex-shrink-0 flex-col;
	@apply h-screen w-1/4;
	@apply overflow-y-auto;
	@apply shadow;
	-webkit-overflow-scrolling: touch;
	min-width: 22rem;
	max-width: 32rem;
}

.secondary-menu .v-table {
	@apply border-l-0;
}

.secondary-menu .v-table th:last-child,
.secondary-menu .v-table td:last-child {
	@apply border-r-0;
}

.secondary-menu--header {
	@apply flex-shrink-0;
	@apply relative z-10;
}

.secondary-menu--content {
	@apply flex-grow;
	@apply overflow-y-auto;
	@apply relative z-0;
	-webkit-overflow-scrolling: touch;
}
</style>
