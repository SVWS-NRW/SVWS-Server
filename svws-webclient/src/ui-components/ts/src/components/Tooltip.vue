<template>
	<div class="tooltip">
		<slot />
	</div>
</template>
<script setup lang="ts">
</script>

<style lang="postcss">
.tooltip {
	@apply text-body;
}

.popper #arrow::before,
.popper:hover,
.popper:hover > #arrow::before{
	@apply bg-primary;
}
</style>
