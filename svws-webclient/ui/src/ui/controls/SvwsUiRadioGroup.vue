<script setup lang="ts">
	const props = withDefaults(defineProps<{
		row?: boolean;
	}>(), {
		row: false,
	});
</script>

<template>
	<div class="flex flex-col items-start gap-1" :class="{'flex-row flex-wrap gap-0.5': row}">
		<slot />
	</div>
</template>
