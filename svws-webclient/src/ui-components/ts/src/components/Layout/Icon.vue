<template>
	<span class="icon">
		<slot></slot>
	</span>
</template>

<style>
.icon {
	line-height: 1 !important;

	&[type="error"]:hover,
	&[type="error"]:focus {
		@apply text-error;
	}
}
</style>
