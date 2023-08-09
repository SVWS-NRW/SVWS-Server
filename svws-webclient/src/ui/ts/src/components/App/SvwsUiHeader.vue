<template>
	<div class="page--header">
		<h2 class="text-headline">
			<slot />
		</h2>
	</div>
</template>

<script setup lang="ts">
</script>

<style lang="postcss">
	.svws-ui-header,
	.page--header {
		@apply px-6 lg:px-9 3xl:px-12 4xl:px-20;
		@apply py-2 gap-2;
		@apply flex-shrink-0;

		.svws-ui-avatar {
			@apply w-20;
		}

		.svws-headline-wrapper {
			@apply flex flex-wrap content-center gap-x-2 text-headline;
		}

		.svws-headline {
			@apply inline-flex gap-x-2 gap-y-1 items-center;

			.svws-ui-badge {
				@apply my-0;
			}
		}

		.svws-subline {
			@apply opacity-50 w-full;
		}
	}

	.svws-ui-header--title {
		@apply flex gap-5;
	}

	.svws-ui-header--actions {
		@apply flex flex-wrap gap-2 min-h-[3rem] items-start;
	}
</style>
