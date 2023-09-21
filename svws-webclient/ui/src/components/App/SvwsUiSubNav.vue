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
	@apply relative z-10 w-full min-h-fit h-10;
	@apply flex items-center gap-2 flex-shrink-0;
	@apply text-sm border-b-2 border-black/5 dark:border-white/5;
	@apply px-6 lg:px-9 3xl:px-12 4xl:px-20;
  @apply bg-light dark:bg-white/10;

	> * {
		@apply flex-shrink-0;
	}

  &--tabs {
    @apply bg-transparent dark:bg-transparent h-11 pt-1;
  }
}
</style>
