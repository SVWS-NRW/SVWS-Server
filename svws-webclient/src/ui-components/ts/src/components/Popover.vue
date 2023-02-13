<script setup lang='ts'>
	import Popper from "vue3-popper";
	import { Placement } from "../types";


	const {
		placement = "auto",
		disableClickAway = false,
		offsetX = '0',
		offsetY = '0',
		openDelay = 0,
		hover = true,
	} = defineProps<{
		placement?: Placement;
		disableClickAway?: boolean;
		offsetX?: string;
		offsetY?: string;
		openDelay?: number;
		hover?: boolean;
	}>();
</script>

<template>
	<Popper :hover="hover" :placement="placement" :disable-click-away="disableClickAway" :offset-x="offsetX"
		:offset-y="offsetY" :open-delay="openDelay" arrow>
		<slot name="trigger" />
		<template #content>
			<slot name="content" />
		</template>
	</Popper>
</template>

<style lang="postcss">
	.popper {
		@apply w-max z-50 leading-tight;
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

	.popper--statistics {
		--popper-theme-background-color: theme("colors.purple");
		--popper-theme-background-color-hover: theme("colors.purple");
		--popper-theme-text-color: theme("colors.white");
	}
</style>
