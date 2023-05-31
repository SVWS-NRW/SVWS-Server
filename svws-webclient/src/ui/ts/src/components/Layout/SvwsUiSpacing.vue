<script setup lang='ts'>
	const props = withDefaults(defineProps<{
		size?: 1 | 2;
	}>(), {
		size: 1
	});
</script>

<template>
	<div class="svws-ui-spacing" :class="{'svws-ui-spacing--2': size === 2}">
		<slot />
	</div>
</template>

<style lang="postcss">
.svws-ui-spacing {
	@apply col-span-full h-2;

	&--2 {
		@apply h-9;
	}
}
</style>
