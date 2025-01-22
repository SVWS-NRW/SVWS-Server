<template>
	<template v-if="disabled"><slot /></template>
	<template v-else>
		<span v-bind="$attrs" ref="reference" @mouseenter="hoverEnterTooltip" @mouseleave="hoverLeaveTooltip" @focus="showTooltip" @blur="hideTooltip"
			@click="toggleTooltip" class="inline-flex flex-wrap items-center gap-0.5 tooltip-trigger"
			:class="{'tooltip-trigger--underline': indicator, 'tooltip-trigger--triggered': isOpen, 'tooltip-trigger--danger': color === 'danger' || (indicator && indicator === 'danger'), 'cursor-help': !hover, 'cursor-default': hover }">
			<slot />
			<template v-if="(indicator && indicator !== 'underline') || $slots.icon">
				<slot name="icon">
					<span class="icon i-ri-information-fill icon--indicator" v-if="indicator === 'info'" />
					<span class="icon i-ri-alert-fill icon--indicator" v-else-if="indicator === 'danger'" />
					<span class="icon i-ri-question-line icon--indicator -my-1 text-headline-md" v-else />
				</slot>
			</template>
		</span>
		<Teleport to="body">
			<Transition enter-active-class="duration-200 ease-out" enter-from-class="transform opacity-0" enter-to-class="opacity-100"
				leave-active-class="duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="transform opacity-0">
				<div v-if="isOpen" ref="floating" :style="floatingStyles" class="tooltip transition-opacity"
					:class="[`tooltip--${color}`, {'tooltip--autosize': autosize}]">
					<span v-if="showArrow" ref="floatingArrow" class="absolute rotate-45 bg-inherit aspect-square w-2 tooltip-arrow" :style="{
						left: middlewareData.arrow?.x !== undefined ? middlewareData.arrow.x + 'px' : '',
						top: middlewareData.arrow?.y !== undefined ? middlewareData.arrow.y + 'px' : '',
						...floatingArrowBalance,
					}" />
					<div class="tooltip-content"><slot name="content" /></div>
				</div>
			</Transition>
		</Teleport>
	</template>
</template>

<script setup lang="ts">
	import { useFloating, autoUpdate, arrow, flip, offset, shift } from "@floating-ui/vue";
	import { ref, computed, toRefs } from "vue";
	import { onClickOutside } from '@vueuse/core'

	const props = withDefaults(defineProps<{
		position?: "top" | "top-start" | "top-end" | "bottom" | "bottom-start" | "bottom-end" | "left" | "left-start" | "left-end" | "right" | "right-start" | "right-end";
		hover?: boolean;
		showArrow?: boolean;
		color?: "primary" | "light" | "dark" | "danger";
		indicator?: "help" | "info" | "danger" | "underline" | false;
		keepOpen?: boolean;
		initOpen?: boolean;
		autosize?: boolean;
		disabled?: boolean;
	}>(), {
		position: "bottom",
		showArrow: true,
		color: "light",
		hover: true,
		indicator: "underline",
		keepOpen: false,
		initOpen: false,
		autosize: false,
		disabled: false,
	});

	const emit = defineEmits<{
		"close": [value: void];
	}>();

	const isOpen = ref(false);
	const reference = ref(null);
	const floating = ref(null);
	const floatingArrow = ref(null);

	if (props.hover === false || props.initOpen === true)
		onClickOutside(floating, hideTooltip, { ignore: [reference] });

	if ((props.keepOpen === true) || (props.initOpen === true))
		isOpen.value = true;

	const { position, showArrow } = toRefs(props);

	const { placement, middlewareData, floatingStyles } = useFloating(
		reference,
		floating,
		{
			placement: position.value,
			middleware: [flip(), shift(), offset(showArrow.value ? 6 : 2), arrow({element: floatingArrow})],
			whileElementsMounted: autoUpdate,
		}
	);

	const floatingArrowBalance = computed(() => {
		if (props.showArrow === false)
			return {};
		const flipped = {
			top: "bottom",
			right: "left",
			bottom: "top",
			left: "right",
		};
		const side = placement.value.split('-')[0] as keyof typeof flipped;
		return {[flipped[side]]: '-4px'};
	});

	function showTooltip() {
		isOpen.value = true;
	}

	function hoverEnterTooltip() {
		if (props.hover && !props.keepOpen)
			showTooltip();
	}

	function hideTooltip() {
		if (props.keepOpen)
			return;
		isOpen.value = false;
		emit("close");
	}

	function hoverLeaveTooltip() {
		if (props.hover && !props.keepOpen)
			isOpen.value = false;
	}

	function toggleTooltip() {
		if (!props.keepOpen)
			isOpen.value = !isOpen.value;
	}

</script>

<style lang="postcss">

	.tooltip-trigger {
		.icon--indicator {
			@apply text-ui;
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
			.icon--indicator span.icon {
				-webkit-filter: invert(44%) sepia(52%) saturate(1260%) hue-rotate(173deg) brightness(91%) contrast(86%);
				filter: invert(44%) sepia(52%) saturate(1260%) hue-rotate(173deg) brightness(91%) contrast(86%);

				.page--statistik & {
					@apply text-ui-statistic;
				}
			}
		}

		&--danger {
			span.icon {
				-webkit-filter: invert(22%) sepia(96%) saturate(2323%) hue-rotate(331deg) brightness(88%) contrast(103%);
				filter: invert(22%) sepia(96%) saturate(2323%) hue-rotate(331deg) brightness(88%) contrast(103%);
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
		@apply bg-ui text-ui border border-ui-secondary;
		@apply rounded-md z-50;
		@apply w-max max-w-[24rem];
		box-shadow: -8px -8px 25px -3px rgb(0 0 0 / 0.1), 8px 8px 25px -3px rgb(0 0 0 / 0.1), -4px 4px 6px -4px rgb(0 0 0 / 0.1);

		&-content {
			@apply px-2 py-0.5 relative z-10 bg-ui rounded-md;
		}

		&-arrow {
			@apply border border-ui-secondary;
		}

		&--autosize {
			@apply max-w-none;
		}

		&--primary {
			&,
			.tooltip-content {
				@apply bg-ui-brand text-ui-onbrand;
			}

			&,
			.tooltip-arrow {
				@apply border-ui-brand;
			}

			.page--statistik &,
			.page--statistik & .tooltip-content {
				@apply bg-ui-statistic text-ui-onstatistic;
			}

			.page--statistik &,
			.page--statistik & .tooltip-arrow {
				@apply border-ui-statistic;
			}
		}

		&--danger {
			&,
			.tooltip-content {
				@apply bg-ui-danger text-ui-ondanger;
			}

			&,
			.tooltip-arrow {
				@apply border-ui-danger;
			}
		}

		&--dark {
			&,
			.tooltip-content {
				@apply bg-ui-danger text-ui-ondanger;
			}

			&,
			.tooltip-arrow {
				@apply border-ui-danger;
			}
		}
	}

</style>
