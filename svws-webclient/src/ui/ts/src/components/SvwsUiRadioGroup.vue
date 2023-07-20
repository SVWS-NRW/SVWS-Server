<script setup lang="ts">
	const props = withDefaults(defineProps<{
		row?: boolean;
	}>(), {
		row: false,
	});
</script>
<template>
	<div class="radio" :class="{'radio--row': row}">
		<slot />
	</div>
</template>
<style>
	.radio {
		@apply flex flex-col items-start gap-1;
	}

	.radio--row {
		@apply flex-row flex-wrap gap-0.5;
	}
</style>
