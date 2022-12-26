<template>
	<span class="icon">
		<slot></slot>
	</span>
</template>

<style>
.icon {
	line-height: 1 !important;

	&[type="danger"]:hover,
	&[type="danger"]:focus {
		@apply text-error;
	}
}
</style>
