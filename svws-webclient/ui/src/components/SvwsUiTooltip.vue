<!-- eslint-disable vue/attribute-hyphenation -->
<template>
	<span @mouseenter="hoverEnterTooltip"
		@mouseleave="hoverLeaveTooltip"
		@focus="showTooltip"
		@blur="hideTooltip"
		@click="toggleTooltip"
		class="inline-flex flex-wrap items-center gap-0.5 tooltip-trigger"
		:class="{'tooltip-trigger--underline': indicator, 'tooltip-trigger--triggered': isOpen, 'tooltip-trigger--danger': color === 'danger' || (indicator && indicator === 'danger'), 'cursor-help': !hover, 'cursor-default': hover }"
		ref="reference" v-bind="$attrs">
		<slot />
		<template v-if="(indicator && indicator !== 'underline') || $slots.icon">
			<slot name="icon">
				<i-ri-information-fill class="icon--indicator" v-if="indicator === 'info'" />
				<i-ri-alert-fill class="icon--indicator" v-else-if="indicator === 'danger'" />
				<i-ri-question-line class="icon--indicator -my-1 text-headline-md" v-else />
			</slot>
		</template>
	</span>
	<Teleport to="body">
		<Transition enterActiveClass="duration-200 ease-out"
			enterFromClass="transform opacity-0"
			enterToClass="opacity-100"
			leaveActiveClass="duration-100 ease-in"
			leaveFromClass="opacity-100"
			leaveToClass="transform opacity-0">
			<div v-if="isOpen"
				:style="{ position: strategy, top: floatingTop, left: floatingLeft }"
				class="tooltip transition-opacity"
				:class="[`tooltip--${color}`, {'tooltip--autosize': autosize}]"
				ref="floating">
				<span v-if="showArrow"
					:style="{
						top: floatingArrowTop,
						left: floatingArrowLeft,
						...floatingArrowBalance,
					}"
					class="absolute rotate-45 bg-inherit aspect-square w-2"
					ref="floatingArrow" />
				<slot name="content" />
			</div>
		</Transition>
	</Teleport>
</template>

<script setup lang="ts">
	import { useFloating, autoUpdate, arrow, flip, offset, shift } from "@floating-ui/vue";
	import { Teleport, Transition, ref, computed } from "vue";

	const props = withDefaults(defineProps<{
		position?: "top" | "top-start" | "top-end" | "bottom" | "bottom-start" | "bottom-end" | "left" | "left-start" | "left-end" | "right" | "right-start" | "right-end";
		hover?: boolean;
		showArrow?: boolean;
		color?: "primary" | "light" | "dark" | "danger";
		indicator?: "help" | "info" | "danger" | "underline" | false;
		forceOpen?: boolean;
		autosize?: boolean;
	}>(), {
		position: "bottom",
		showArrow: true,
		color: "light",
		hover: true,
		indicator: "underline",
		forceOpen: false,
		autosize: false,
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

	if (props.forceOpen) {
		isOpen.value = true;
	}

	// eslint-disable-next-line vue/no-setup-props-destructure
	const {x, y, strategy, placement, middlewareData} = useFloating(
		reference,
		floating,
		{
			placement: props.position,
			// eslint-disable-next-line vue/no-setup-props-destructure
			middleware: [flip(), shift(), offset(props.showArrow ? 6 : 2), arrow({element: floatingArrow})],
			whileElementsMounted: autoUpdate,
		}
	);

	const side = computed(() => placement.value.split("-")[0] as "top" | "bottom" | "left" | "right");
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
		if (props.hover && !props.forceOpen) {
			showTooltip();
		}
	}

	function hideTooltip() {
		isOpen.value = false;
	}

	function hoverLeaveTooltip() {
		if (props.hover && !props.forceOpen) {
			hideTooltip();
		}
	}

	function toggleTooltip() {
		if (!props.forceOpen) {
			isOpen.value = !isOpen.value;
		}
	}
</script>

<style lang="postcss">
.tooltip-trigger {
	.icon--indicator {
		@apply text-black dark:text-white;
		width: 1em;
		height: 1em;
		margin-left: 0.1em;
	}

	.cursor-pointer & {
		cursor: pointer;
	}

	.cursor-auto & {
		cursor: auto;
	}

	&--triggered {
		.icon--indicator svg {
			@apply text-svws;

			.page--statistik & {
				@apply text-violet-500;
			}

			.text-error & {
				color: inherit;
			}
		}
	}

	&--danger {
		svg {
			@apply text-error;
		}
	}

	&--underline {
		@apply underline decoration-black/20 dark:decoration-white/20 decoration-dashed;

		&.tooltip-trigger--triggered {
			@apply no-underline;
		}
	}
}

.tooltip {
	@apply rounded-md z-50;
	@apply px-2 py-0.5 w-max max-w-[24rem];
	@apply bg-white text-black border border-light dark:bg-black dark:text-white dark:border-white/5;
	box-shadow: -8px -8px 25px -3px rgb(0 0 0 / 0.1), 8px 8px 25px -3px rgb(0 0 0 / 0.1), -4px 4px 6px -4px rgb(0 0 0 / 0.1);

	&--autosize {
		@apply max-w-none;
	}

	&--primary {
		@apply bg-svws text-white dark:bg-svws border-none;
		@apply shadow-sm shadow-black/25;

		.page--statistik & {
			@apply bg-violet-500 dark:bg-violet-500;
		}
	}

	&--danger {
		@apply bg-error dark:bg-error text-white border border-error/5 shadow-error/10 shadow-md;
	}
}
</style>
