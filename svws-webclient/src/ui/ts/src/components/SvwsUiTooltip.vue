<template>
	<span
		@mouseenter="hoverEnterTooltip"
		@mouseleave="hoverLeaveTooltip"
		@focus="showTooltip"
		@blur="hideTooltip"
		@click="toggleTooltip"
		class="inline-flex flex-wrap items-center gap-0.5 tooltip-trigger"
		:class="{'tooltip-trigger--underline': indicator, 'tooltip-trigger--triggered': isOpen, 'tooltip-trigger--danger': color === 'danger' || (indicator && indicator === 'danger'), 'cursor-help': !hover, 'cursor-default': hover }"
		ref="reference"
	>
		<slot/>
		<template v-if="(indicator && indicator !== 'underline') || $slots.icon">
			<slot name="icon">
				<i-ri-information-fill v-if="indicator === 'info'"/>
				<i-ri-alert-fill v-else-if="indicator === 'danger'"/>
				<i-ri-question-fill v-else/>
			</slot>
		</template>
	</span>
	<Teleport to="body">
		<Transition
			enter-active-class="duration-200 ease-out"
			enter-from-class="transform opacity-0"
			enter-to-class="opacity-100"
			leave-active-class="duration-100 ease-in"
			leave-from-class="opacity-100"
			leave-to-class="transform opacity-0"
		>
			<div
				v-if="isOpen"
				:style="{ position: strategy, top: floatingTop, left: floatingLeft }"
				class="tooltip transition-opacity"
				:class="`tooltip--${color}`"
				ref="floating"
			>
        <span
			v-if="showArrow"
			:style="{
            top: floatingArrowTop,
            left: floatingArrowLeft,
            ...floatingArrowBalance,
          }"
			class="absolute rotate-45 bg-inherit aspect-square w-2"
			ref="floatingArrow"
		/>
				<slot name="content"/>
			</div>
		</Transition>
	</Teleport>
</template>

<script setup lang="ts">
import {Teleport, Transition, ref, computed} from "vue";
import {useFloating, autoUpdate, arrow, flip, offset, shift} from "@floating-ui/vue";

const props = withDefaults(defineProps<{
	position?: "top" | "top-start" | "top-end" | "bottom" | "bottom-start" | "bottom-end" | "left" | "left-start" | "left-end" | "right" | "right-start" | "right-end";
	hover?: boolean;
	showArrow?: boolean;
	color?: "primary" | "light" | "dark" | "danger";
	indicator?: "help" | "info" | "danger" | "underline" | false;
}>(), {
	position: "bottom",
	showArrow: true,
	color: "light",
	hover: true,
	indicator: "underline",
});

const flipped = {
	top: "bottom",
	right: "left",
	bottom: "top",
	left: "right",
};

const isOpen = ref(false);
const reference = ref(null);
const floating = ref(null);
const floatingArrow = ref(null);

const {x, y, strategy, placement, middlewareData} = useFloating(
	reference,
	floating,
	{
		placement: props.position,
		middleware: [flip(), shift(), offset(props.showArrow ? 6 : 2), arrow({element: floatingArrow})],
		whileElementsMounted: autoUpdate,
	}
);

const side = computed(() => placement.value.split("-")[0]);
const floatingTop = computed(() => `${y.value ?? 0}px`);
const floatingLeft = computed(() => `${x.value ?? 0}px`);

const floatingArrowX = computed(() => middlewareData.value.arrow?.x ?? null);
const floatingArrowY = computed(() => middlewareData.value.arrow?.y ?? null);
const floatingArrowTop = computed(() =>
	floatingArrowY.value === null ? "" : `${floatingArrowY.value}px`
);
const floatingArrowLeft = computed(() =>
	floatingArrowX.value === null ? "" : `${floatingArrowX.value}px`
);
const floatingArrowBalance = computed(() => ({
	[flipped[side.value]]: "-4px",
}));

function showTooltip() {
	isOpen.value = true;
}

function hoverEnterTooltip() {
	if (props.hover) {
		showTooltip();
	}
}

function hideTooltip() {
	isOpen.value = false;
}

function hoverLeaveTooltip() {
	if (props.hover) {
		hideTooltip();
	}
}

function toggleTooltip() {
	isOpen.value = !isOpen.value;
}
</script>

<style lang="postcss">
.tooltip-trigger {
	svg {
		@apply w-4 h-4 text-black;
	}

	&--triggered {
		svg {
			@apply text-primary;
		}
	}

	&--danger {
		svg {
			@apply text-error;
		}
	}

	&--underline {
		@apply underline decoration-black/10;

		&.tooltip-trigger--triggered {
			@apply no-underline;
		}
	}
}

.tooltip {
	@apply rounded-md;
	@apply px-2 py-0.5 w-max max-w-[12rem];
	@apply bg-white text-black border border-light;
	box-shadow: -8px -8px 25px -3px rgb(0 0 0 / 0.1), 8px 8px 25px -3px rgb(0 0 0 / 0.1), -4px 4px 6px -4px rgb(0 0 0 / 0.1);

	&--primary {
		@apply bg-primary text-white border-none;
		@apply shadow-sm shadow-black/25;
	}

	&--dark {
		@apply bg-dark-20 text-black border border-black/5 shadow-md;
	}

	&--danger {
		@apply bg-error-light border border-error/5 text-error shadow-error/10 shadow-md;
	}
}
</style>
