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
	@apply relative z-10 w-auto h-8 mt-1.5 p-[2px] -mx-3;
	@apply flex items-center gap-[2px] flex-shrink-0 bg-light dark:bg-white/5;
	@apply text-sm rounded-md;

	> * {
		@apply flex-shrink-0;
	}
}
</style>
