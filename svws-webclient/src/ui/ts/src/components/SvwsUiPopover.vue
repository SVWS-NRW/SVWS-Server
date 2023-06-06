<script setup lang='ts'>
	import Popper from "vue3-popper";
	import type { Placement } from "../types";

	const props = withDefaults(defineProps<{
		placement?: Placement;
		disableClickAway?: boolean;
		offsetX?: string;
		offsetY?: string;
		openDelay?: number;
		hover?: boolean;
		context?: boolean;
	}>(), {
		placement: "auto",
		disableClickAway: false,
		offsetX: '0',
		offsetY: '0',
		openDelay: 0,
		hover: true,
		context: false,
	});
</script>

<template>
	<Popper :hover="hover" :placement="placement" :disable-click-away="disableClickAway" :offset-x="offsetX"
		:offset-y="offsetY" :open-delay="openDelay" arrow :class="{'popper--context': context}">
		<slot name="trigger" />
		<template #content>
			<slot name="content" />
		</template>
	</Popper>
</template>

<style lang="postcss">
	.popper {
		@apply w-max z-50 leading-tight font-medium;
		max-width: 16rem;
		color: var(--popper-theme-text-color);
	}

	.popper--small .popper {
		@apply text-sm;
	}

	.popper--no-arrow .popper {
		#arrow {
			@apply hidden;
		}
	}

	.popper--danger {
		--popper-theme-background-color: theme("colors.error");
		--popper-theme-background-color-hover: theme("colors.error");
		--popper-theme-text-color: theme("colors.white");
	}

	.popper--dark {
		--popper-theme-background-color: theme("colors.dark");
		--popper-theme-background-color-hover: theme("colors.dark");
		--popper-theme-text-color: theme("colors.white");
	}

	.popper--light {
		--popper-theme-background-color: theme("colors.light");
		--popper-theme-background-color-hover: theme("colors.light");
		--popper-theme-text-color: theme("colors.black");
	}

	.popper--context {
		--popper-theme-background-color: theme("colors.white");
		--popper-theme-background-color-hover: theme("colors.white");
		--popper-theme-text-color: theme("colors.black");
		--popper-theme-padding: theme("spacing.2");
		--popper-theme-border-radius: theme("borderRadius.lg");
		--popper-theme-border-style: solid thin;
	}

	.popper--statistics {
		--popper-theme-background-color: theme("colors.purple.500");
		--popper-theme-background-color-hover: theme("colors.purple.500");
		--popper-theme-text-color: theme("colors.white");
	}

	.rich-text {
		ul {
			@apply list-disc list-inside;
		}
	}
</style>
