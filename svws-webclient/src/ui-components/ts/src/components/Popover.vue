<script setup lang='ts'>
import Popper from "vue3-popper";

type PLACEMENT = "auto" | "auto-start" | "auto-end" | "top" | "top-start" | "top-end" | "bottom" | "bottom-start" | "bottom-end" | "right" | "right-start" | "right-end" | "left" | "left-start" | "left-end";

const {
	placement = "auto",
	disableClickAway = false,
	offsetX = '0',
	offsetY = '0',
	openDelay = 0,
	hover = true,
} = defineProps<{
	placement?: PLACEMENT;
	disableClickAway?: boolean;
	offsetX?: string;
	offsetY?: string;
	openDelay?: number;
	hover?: boolean;
}>();
</script>

<template>
	<Popper
:hover="hover" :placement="placement" :disable-click-away="disableClickAway" :offset-x="offsetX"
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
	}
</style>
