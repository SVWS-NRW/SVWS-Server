
<template>
	<div class="input-wrapper" :class="{'input-wrapper--2': grid === 2, 'input-wrapper--4': grid === 4, 'input-wrapper--center': center}">
		<slot />
	</div>
</template>

<script setup lang='ts'>
	const props = withDefaults(defineProps<{
		grid?: 1 | 2 | 4;
		center?: boolean;
	}>(), {
		grid: 1,
		center: false,
	});
</script>
