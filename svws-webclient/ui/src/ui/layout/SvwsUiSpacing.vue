<template>
	<div class="svws-ui-spacing" :class="{ 'svws-ui-spacing--2': size === 2 }">
		<slot />
	</div>
</template>


<script setup lang='ts'>

	const props = withDefaults(defineProps<{
		size?: 1 | 2;
	}>(), {
		size: 1,
	});

</script>
