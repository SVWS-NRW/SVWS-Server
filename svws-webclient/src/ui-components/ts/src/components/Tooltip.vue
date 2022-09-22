<template>
	<div class="tooltip">
		<slot />
	</div>
</template>

<style>
.tooltip {
	@apply block;
	@apply bg-primary;
	@apply text-body text-white;
	@apply rounded;
	@apply px-4 py-2;
}
</style>
