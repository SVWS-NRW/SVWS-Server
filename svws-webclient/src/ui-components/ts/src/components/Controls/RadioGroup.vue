<script setup lang="ts">
const {
	row = false,
} = defineProps<{
	row?: boolean;
}>();
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
