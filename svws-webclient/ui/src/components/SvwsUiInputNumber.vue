<template>
	<div class="input-number-component"
		:class="{
			'input-number--filled': (data !== null) && (data !== undefined),
			'input-number--invalid': (isValid === false),
			'input-number--disabled': disabled,
			'input-number--readonly': readonly,
			'input-number--statistics': statistics,
			'input-number--number': true,
			'input-number-component--headless': headless,
			'col-span-full': span === 'full',
			'col-span-2': span === '2',
		}">
		<label :for="id" />
		<div v-if="readonly" :class="{ 'input-number--control': !headless, 'input-number--headless': headless, 'input-number--rounded': rounded, }">
			{{ data }}
		</div>
		<input v-else ref="input" :name="id"
			v-focus
			:class="{ 'input-number--control': !headless, 'input-number--headless': headless, 'input-number--rounded': rounded, }"
			v-bind="{ ...$attrs }"
			type="number"
			inputmode="numeric"
			:value="data"
			:disabled="disabled"
			:required="required"
			:readonly="readonly"
			:aria-labelledby="labelId"
			:placeholder="headless ? placeholder : ''"
			@input="onInput"
			@keyup.enter="onKeyEnter"
			@blur="onBlur">
		<span v-if="placeholder && !headless"
			:id="labelId"
			class="input-number--placeholder"
			:class="{
				'input-number--placeholder--required': required,
			}">
			<span>{{ placeholder }}</span>
			<span class="icon i-ri-alert-line ml-0.5 icon-error" v-if="!isValid" />
			<span v-if="statistics" class="cursor-pointer">
				<svws-ui-tooltip position="right">
					<span class="inline-flex items-center">
						<span class="icon i-ri-bar-chart-2-line icon-statistics pointer-events-auto ml-0.5" />
						<span class="icon i-ri-alert-fill" v-if="data === null || data === undefined || !isValid" />
					</span>
					<template #content>
						Relevant für die Statistik
					</template>
				</svws-ui-tooltip>
			</span>
		</span>
		<span v-if="data != null && !hideStepper && !disabled" class="svws-input-stepper">
			<button ref="btnMinus" role="button" @click="onInputNumber('down')" @blur="onBlur" :class="{'svws-disabled': String($attrs?.min) === String(data)}"><span class="icon i-ri-subtract-line inline-block" /></button>
			<button ref="btnPlus" role="button" @click="onInputNumber('up')" @blur="onBlur" :class="{'svws-disabled': String($attrs?.max) === String(data)}"><span class="icon i-ri-add-line inline-block" /></button>
		</span>
	</div>
</template>


<script setup lang="ts">

	import { ref, computed, watch, type ComputedRef, type Ref, useId, useAttrs } from "vue";
	import { genId } from "../utils";

	defineOptions({
		inheritAttrs: false,
	});

	const attrs = useAttrs();
	const input = ref<null | HTMLInputElement>(null);
	const btnPlus = ref<null | HTMLButtonElement>(null);
	const btnMinus = ref<null | HTMLButtonElement>(null);
	const id = useId();

	const props = withDefaults(defineProps<{
		modelValue: number | null;
		placeholder?: string;
		statistics?: boolean;
		valid?: (value: number | null) => boolean;
		disabled?: boolean;
		required?: boolean;
		readonly?: boolean;
		headless?: boolean;
		focus?: boolean;
		rounded?: boolean;
		hideStepper?: boolean;
		span?: 'full' | '2';
	}>(), {
		placeholder: "",
		statistics: false,
		valid: () => true,
		disabled: false,
		required: false,
		readonly: false,
		headless: false,
		focus: false,
		rounded: false,
		hideStepper: false,
		span: undefined,
	});

	const emit = defineEmits<{
		"update:modelValue": [value: number | null];
		"change": [value: number | null];
		"blur": [value: number | null];
	}>();

	const vFocus = {
		mounted: (el: HTMLInputElement) => {
			if (props.focus)
				el.focus();
		}
	};

	const data = ref<number | null>(props.modelValue);

	watch(() => props.modelValue, (value: number | null) => updateData(value), { immediate: false });

	const isValid = computed(() => {
		if (props.required && (data.value === null))
			return false;

		if ((data.value !== null)
			&& (((attrs.min !== undefined) && (attrs.min !== null) && (data.value < Number(attrs.min)))
				|| ((attrs.max !== undefined) && (attrs.max !== null) && (data.value > Number(attrs.max)))))
			return false;

		return props.valid(data.value);
	})

	function updateData(value: number | null) {
		if (data.value !== value) {
			data.value = value;
			emit("update:modelValue", data.value);
		}
	}

	function onInput(event: Event) {
		const strValue = (event.target as HTMLInputElement).value;
		const value = (strValue === "") ? null : Number(strValue);
		if (value !== data.value)
			updateData(value);
	}

	function onInputNumber(stepDirection: string) {
		if (input.value === null)
			return;
		if (stepDirection === 'up') {
			input.value.stepUp();
		} else if (stepDirection === 'down') {
			input.value.stepDown();
		}
		updateData(Number(input.value.value));
	}

	function onBlur(event: Event) {
		// prevent firing change/blur event, if the user only switches between input and button elements inside the SVWSUiInputNumber component itself
		if (event instanceof FocusEvent && ([input.value, btnPlus.value, btnMinus.value] as Array<HTMLElement>).includes(event.relatedTarget as HTMLElement))
			return;

		if (props.modelValue !== data.value)
			emit("change", data.value);
		emit("blur", data.value);
	}

	function onKeyEnter(event: Event) {
		if (props.modelValue !== data.value)
			emit("change", data.value);
	}

	function reset() {
		data.value = props.modelValue;
	}

	const labelId = genId();

	const content = computed<number | null>(() => data.value);

	defineExpose<{
		content: ComputedRef<number | null>,
		input: Ref<HTMLInputElement | null>,
		reset: () => void;
	}>({ content, input, reset, });

</script>


<style lang="postcss">

	.input-number-component {
		@apply flex;
		@apply relative;
		@apply w-full min-w-16;
		@apply overflow-hidden whitespace-nowrap text-base;

		input::placeholder {
			@apply text-ui-secondary;

			.placeholder--visible & {
				@apply text-ui;
			}
		}

		&:focus {
			@apply outline-none;
		}

		input {
			@apply cursor-text overflow-ellipsis;

			&:focus {
				@apply outline-none;
			}
		}
	}

	.input-number-component .svws-icon {
		@apply bg-ui;
		@apply pointer-events-none absolute top-1 right-1 bottom-1 w-5 rounded inline-flex items-center justify-end pr-1 leading-none;

		span.icon {
			@apply opacity-25 -mr-0.5;
		}
	}

	.input-number-component {
		&:hover,
		&:focus-within {
			.svws-icon span.icon {
				@apply opacity-50;
			}
		}
	}

	.input-number--statistics .svws-icon {
		@apply text-ui-statistic;

		span.icon {
			-webkit-filter: brightness(0) saturate(100%) invert(37%) sepia(71%) saturate(868%) hue-rotate(224deg) brightness(103%) contrast(93%);
			filter: brightness(0) saturate(100%) invert(37%) sepia(71%) saturate(868%) hue-rotate(224deg) brightness(103%) contrast(93%);
		}

		/* TODO: COLORS icon darkmode */
	}

	.input-number--number {
		input {
			@apply pr-14;
			appearance: textfield;
		}

		.svws-input-stepper {
			@apply absolute top-1 right-1.5 bottom-1 flex justify-center items-center gap-1;

			button {
				@apply bg-ui-neutral border border-ui-secondary;
				@apply rounded-[0.2rem] focus:outline-none leading-none;

				&:hover,
				&:focus-visible {
					@apply bg-ui-neutral-hover border-ui-neutral-hover;
				}

				&:focus-visible {
					@apply ring ring-ui;
				}

				&.svws-disabled {
					@apply pointer-events-none opacity-25;

					span.icon {
						@apply opacity-50;
					}
				}

				.icon {
					margin-top: 0.1rem;
					/* TODO: COLORS icon darkmode */
				}
			}
		}

		.input-number--placeholder {
			max-width: calc(100% - 0.7em);
		}
	}

	.input-number--invalid .svws-icon {
		@apply text-ui-danger;
	}

	.input-number--control {
		@apply bg-ui border border-ui-secondary;
		@apply rounded-md;
		@apply h-9 w-full;
		@apply text-base;
		@apply whitespace-nowrap;
		padding: 0.5em 0.7em;

		&:hover,
		&:focus-active {
			@apply border-ui;
		}
	}

	.input-number--rounded {
		@apply rounded-full;
	}

	.input-number-component:focus-within .input-number--control,
	.input-number--filled .input-number--control {
		@apply border-ui;
		@apply outline-none;
	}

	.input-number-component:focus-within .input-number--control {
		@apply ring ring-ui-neutral;
	}

	.input-number--filled:not(:focus-within):not(:hover) .input-number--control {
		@apply border-ui-secondary;
	}

	.input-number--statistics.input-number--filled:not(:focus-within):not(:hover) .input-number--control,
	.input-number--statistics:not(:focus-within):not(:hover) .input-number--control {
		@apply border-ui-statistic;
	}

	.input-number--invalid.input-number--filled:not(:focus-within):not(:hover) .input-number--control {
		@apply border-ui-danger;
	}

	.input-number--statistics.input-number--filled:not(:focus-within):hover .input-number--control,
	.input-number--statistics:not(:focus-within):hover .input-number--control {
		@apply border-ui-statistic;
	}

	.input-number--invalid.input-number--filled:not(:focus-within):hover .input-number--control {
		@apply border-ui-danger;
	}

	.input-number--filled:not(:focus-within):hover .input-number--control {
		@apply border-ui-secondary;
	}

	.input-number--statistics.input-number-component:focus-within .input-number--control,
	.input-number--statistics.input-number--filled .input-number--control {
		@apply border-ui-statistic;
	}

	.input-number--readonly:hover .input-number--control {
		@apply border-ui-secondary;
	}

	.input-number--readonly.input-number--filled:hover .input-number--control {
		@apply border-ui;
	}

	.input-number--control--multiselect-tags {
		@apply border-b-0 rounded-b-none pt-1 pb-0;
	}

	.input-number--statistics {
		.tooltip-trigger--triggered svg {
			@apply text-ui-statistic;
		}
	}

    &.input-number--filled {
      @apply text-ui-brand;
    }

	/* Das sieht gut aus, aber führt zu Problemen, wenn das Fenster verkleinert wird. Min/Max verwenden?
	input {
		@apply pl-8;
	} */

	.input-number--readonly .input-number--control {
		@apply pointer-events-auto cursor-default select-none;
	}

	.input-number--placeholder {
		@apply absolute;
		@apply pointer-events-none;
		@apply opacity-60;
		@apply transform;
		@apply flex items-center font-medium;

		top: 0.5em;
		left: 0.7em;
		line-height: 1.33;
	}

	.input-number-component:not(.input-number--filled) .input-number--placeholder {
		@apply font-normal;

		.wrapper--tag-list & {
			@apply font-medium;
		}

		.wrapper--tag-list:not(.wrapper--filled) & {
			@apply font-medium;
		}
	}

	.input-number--placeholder--prefix {
		left: 4.5em;
		top: 0.5em;
	}

	.multiselect-input-component .input-number--placeholder {
		top: 0.5em;
	}

	.input-number-component:focus-within .input-number--placeholder,
	.input-number--filled .input-number--placeholder {
		@apply bg-ui;
		@apply -translate-y-1/2 opacity-100 rounded px-1;

		top: 0;
		left: 0.7em;
		font-size: 0.78rem;

		&:after {
			content: "";
		}
	}

	.input-number--statistics .input-number--placeholder {
		@apply text-ui-statistic;
		@apply font-bold;
	}

	.input-number--invalid .input-number--placeholder,
	.input-number--invalid:not(:focus-within) .input-number--control {
		@apply text-ui-danger;
	}

	.input-number--disabled {
		@apply cursor-default pointer-events-none;

		.input-number--placeholder {
			@apply text-ui-disabled;
		}
	}

	.input-number--control:disabled {
		@apply text-ui-disabled !border-ui-disabled;
		@apply pointer-events-none;
	}

	.input-number-component:focus-within,
	.input-number--filled {
		@apply overflow-visible;
	}

	.input-number--placeholder--required:after {
		@apply text-ui-danger;
		@apply inline-block font-normal relative;
		content: "*";
		font-size: 1.2em;
		margin-bottom: -0.2em;
		top: -0.1em;
		left: 0.1em;
	}

	.input-number--headless,
	.svws-ui-table .input-number--control {
		@apply w-full whitespace-nowrap border-0 outline-none;

		&:not([class*="bg-"]) {
			background-color: unset;
		}

		&::placeholder {
			@apply font-normal;
			color: inherit;
		}

		&:hover:not(:focus) {
			@apply underline decoration-dotted underline-offset-2;
		}
	}

	.input-number--inline {
		@apply cursor-text underline decoration-dotted underline-offset-2;
	}

</style>
