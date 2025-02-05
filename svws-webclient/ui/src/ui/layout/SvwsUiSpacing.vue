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


<style lang="postcss">

	@reference "../../assets/styles/index.css";

	.svws-ui-spacing {
		@apply col-span-full h-2;
	}

	.svws-ui-spacing--2 {
		@apply h-9;
	}

</style>
