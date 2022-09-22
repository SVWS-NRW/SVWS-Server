<template>
	<div class="app-layout--wrapper">
		<div class="app-layout--sidebar-wrapper">
			<slot name="sidebar" />
		</div>
		<slot name="secondaryMenu" />
		<slot name="main">
			<div class="app-layout--pattern-wrapper">
				<div class="app-layout--pattern-overlay"></div>
				<div class="app-layout--pattern"></div>
			</div>
		</slot>
		<slot name="contentSidebar" />
	</div>
</template>

<style>
	.app-layout--pattern-wrapper {
		@apply h-full w-full;
		@apply relative;
	}

	.app-layout--pattern-overlay {
		@apply absolute inset-0 z-10;
		@apply bg-white bg-opacity-92;
		@apply block;
		@apply h-full w-full;
	}

	.app-layout--pattern {
		background-image: url("/img/pattern.png");
		background-size: 12.5rem;

		@apply absolute inset-0 z-20;
		@apply bg-repeat;
	}

	.app-layout--wrapper {
		@apply absolute inset-0;
		@apply flex flex-row;
	}

	.app-layout--sidebar-wrapper {
		@apply flex-shrink-0;
		@apply relative z-20;
	}
</style>
