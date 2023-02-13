<template>
	<div class="radio">
		<slot />
	</div>
</template>
<script setup lang="ts">
</script>
<style>
	.radio {
		@apply flex flex-col items-start gap-1;
	}

	.radio--row {
		@apply flex-row flex-wrap gap-0.5;
	}
</style>
