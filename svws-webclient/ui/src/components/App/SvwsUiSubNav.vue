<template>
	<div class="router-tab-bar--subnav" :class="{'router-tab-bar--subnav--tabs': type === 'tabs'}">
		<slot />
	</div>
</template>

<script setup lang="ts">
	import {onMounted, onUnmounted} from "vue";

	const props = withDefaults(defineProps<{
		type?: 'default' | 'tabs';
	}>(), {
		type: 'default',
	});

	onMounted(() => {
		document.body.classList.add('svws-has-sub-nav');
	});

	onUnmounted(() => {
		document.body.classList.remove('svws-has-sub-nav');
	});

</script>

<style lang="postcss">
.router-tab-bar--subnav {
	@apply relative z-10 w-auto h-9 py-2 px-[2px] -mt-[2px] -mx-3;
	@apply flex items-center gap-[2px] flex-shrink-0 bg-light dark:bg-white/5;
	@apply text-sm rounded-md;

	&:before {
		content: '';
	}

	> * {
		@apply flex-shrink-0;
	}

	.button {
		@apply border-0;
	}
}
</style>
