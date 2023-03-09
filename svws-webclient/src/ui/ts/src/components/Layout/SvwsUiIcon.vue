<template>
	<span class="icon">
		<slot />
	</span>
</template>

<script setup lang="ts">
</script>

<style lang="postcss">
.icon {
	line-height: 1 !important;

	&[type="error"]:hover,
	&[type="error"]:focus {
		@apply text-error;
	}
}

.icon--1-em svg {
	width: 1em;
	height: 1em;
}
</style>
