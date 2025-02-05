
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


<style lang="postcss">

	@reference "../../assets/styles/index.css";

	.input-wrapper {
		@apply grid grow grid-cols-1 gap-3;

		.input-wrapper:not(.col-span-1) {
			@apply col-span-full;
		}

		.button {
			@apply mr-auto;
		}
	}

	.input-wrapper--2 {
			@apply sm:grid-cols-2;
		}

	.input-wrapper--4 {
		@apply grid-cols-2 md:grid-cols-4;
	}

	.input-wrapper--checkboxes {
		@apply gap-x-3 gap-y-0;
	}

	.input-wrapper--center {
		@apply justify-items-center;

		.button {
			@apply mx-auto;
		}
	}

</style>
