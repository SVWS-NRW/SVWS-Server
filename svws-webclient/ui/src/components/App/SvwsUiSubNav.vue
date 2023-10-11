<template>
	<div class="router-tab-bar--subnav" :class="{'router-tab-bar--subnav--tabs': type === 'tabs'}">
		<slot />
	</div>
</template>

<script setup lang="ts">
	const props = withDefaults(defineProps<{
		type?: 'default' | 'tabs';
	}>(), {
		type: 'default',
	});

</script>

<style lang="postcss">
.router-tab-bar--subnav {
	@apply relative z-10 w-auto h-auto mt-2 mb-4 -mx-3;
	@apply flex items-center gap-1 flex-shrink-0;
	@apply text-sm rounded-md;

	> * {
		@apply flex-shrink-0;
	}
}
</style>
