<script setup lang='ts'>
	const props = withDefaults(defineProps<{
		grid?: 1 | 2 | 4;
		center?: boolean;
	}>(), {
		grid: 1,
		center: false,
	});
</script>

<template>
	<div class="input-wrapper" :class="{'input-wrapper--2': grid === 2, 'input-wrapper--4': grid === 4, 'input-wrapper--center': center}">
		<slot />
	</div>
</template>

<style lang="postcss">
.input-wrapper {
	@apply grid flex-grow grid-cols-1 gap-3;

	&--2 {
		@apply sm:grid-cols-2;
	}

	&--4 {
		@apply grid-cols-2 md:grid-cols-4;
	}

	.input-wrapper:not(.col-span-1) {
		@apply col-span-full;
	}

	&--checkboxes {
		@apply gap-x-3 gap-y-0;
	}

	.button {
		@apply mr-auto;
	}

	&--center {
		.button {
			@apply mx-auto;
		}
	}
}
</style>
