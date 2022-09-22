<template>
	<div class="radio">
		<slot />
	</div>
</template>

<style>
	.radio {
		@apply inline-flex flex-col;
		@apply py-1;
		@apply space-y-2;
	}
</style>
