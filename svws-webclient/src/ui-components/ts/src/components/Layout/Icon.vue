<template>
	<span class="icon">
		<slot />
	</span>
</template>
<script setup lang="ts">
</script>
<style>
.icon {
	line-height: 1 !important;

	&[type="error"]:hover,
	&[type="error"]:focus {
		@apply text-error;
	}
}
</style>
