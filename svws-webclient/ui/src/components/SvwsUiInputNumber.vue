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
			@apply text-black/25 dark:text-white/25;

			.placeholder--visible & {
				@apply text-black dark:text-white;
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
		@apply pointer-events-none absolute top-1 right-1 bottom-1 bg-white dark:bg-black w-5 rounded inline-flex items-center justify-end pr-1 leading-none;

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
		@apply text-violet-500;

		span.icon {
			-webkit-filter: brightness(0) saturate(100%) invert(37%) sepia(71%) saturate(868%) hue-rotate(224deg) brightness(103%) contrast(93%);
			filter: brightness(0) saturate(100%) invert(37%) sepia(71%) saturate(868%) hue-rotate(224deg) brightness(103%) contrast(93%);
		}
	}

	.input-number--number {
		input {
			@apply pr-12;
			appearance: textfield;
		}

		.svws-input-stepper {
			@apply absolute top-1 right-1 bottom-1 flex justify-center items-center gap-0.5;

			button {
				@apply bg-light dark:bg-white/5 border border-black/10 dark:border-white/10 rounded focus:outline-none leading-none;

				&:hover,
				&:focus-visible {
					@apply bg-black/10 dark:bg-white/10;
				}

				&:focus-visible {
					@apply ring-2 ring-offset-1 ring-black/25 dark:ring-white/25;
				}

				&.svws-disabled {
					@apply pointer-events-none opacity-25;

					span.icon {
						@apply opacity-50;
					}
				}
			}
		}

		.input-number--placeholder {
			max-width: calc(100% - 0.7em);
		}
	}

	.input-number--invalid .svws-icon {
		@apply text-error;
	}

	.input-number--control {
		@apply bg-white dark:bg-black;
		@apply rounded-md border border-black/5 dark:border-white/5;
		@apply h-9 w-full;
		@apply text-base;
		@apply whitespace-nowrap;
		padding: 0.5em 0.7em;

		&:hover {
			@apply border-black/25 dark:border-white/25;
		}
	}

	.input-number--rounded {
		@apply rounded-full;
	}

	.input-number-component:focus-within .input-number--control,
	.input-number--filled .input-number--control {
		@apply border-black dark:border-white;
		@apply outline-none;
	}

	.input-number--filled:not(:focus-within):not(:hover) .input-number--control {
		@apply border-black/25 dark:border-white/25;
	}

	.input-number--statistics.input-number--filled:not(:focus-within):not(:hover) .input-number--control,
	.input-number--statistics:not(:focus-within):not(:hover) .input-number--control {
		@apply border-violet-500/25 dark:border-violet-800/25;
	}

	.input-number--invalid.input-number--filled:not(:focus-within):not(:hover) .input-number--control {
		@apply border-error/25 dark:border-error/25;
	}

	.input-number--statistics.input-number--filled:not(:focus-within):hover .input-number--control,
	.input-number--statistics:not(:focus-within):hover .input-number--control {
		@apply border-violet-500/50 dark:border-violet-800/50;
	}

	.input-number--invalid.input-number--filled:not(:focus-within):hover .input-number--control {
		@apply border-error/50 dark:border-error/50;
	}

	.input-number--invalid.input-number--filled:focus-within:hover .input-number--control {
		@apply border-error/50 dark:border-error/50;
	}

	.input-number--filled:not(:focus-within):hover .input-number--control {
		@apply border-black/50 dark:border-white/50;
	}

	.input-number--statistics.input-number-component:focus-within .input-number--control,
	.input-number--statistics.input-number--filled .input-number--control {
		@apply border-violet-500;
	}

	.input-number--readonly:hover .input-number--control {
		@apply border-black/5 dark:border-white/5;
	}

	.input-number--readonly.input-number--filled:hover .input-number--control {
		@apply border-black/25 dark:border-white/25;
	}

	.input-number--control--multiselect-tags {
		@apply border-b-0 rounded-b-none pt-1 pb-0;
	}

	.input-number--statistics {
		.tooltip-trigger--triggered svg {
			@apply text-violet-800;
		}
	}

    &.input-number--filled {
      @apply text-svws;
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

	.input-number-component:not(.input-number--filled):not(:focus-within):not(.input-number--disabled):not(.input-number--readonly):hover .input-number--placeholder {
		@apply opacity-100;
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
		@apply -translate-y-1/2;
		@apply bg-white dark:bg-black opacity-100;
		@apply rounded;
		@apply px-1;

		top: 0;
		left: 0.7em;
		font-size: 0.78rem;

		&:after {
			content: "";
		}
	}

	.input-number--statistics .input-number--placeholder {
		@apply text-violet-500 font-bold;
	}

	.input-number--invalid:not(:focus-within) .input-number--placeholder,
	.input-number--invalid:not(:focus-within) .input-number--control {
		@apply text-error;
	}

	.input-number--disabled {
		@apply cursor-default;

		.input-number--placeholder {
			@apply text-black/25 dark:text-white/25;
		}
	}

	.input-number--control:disabled {
		@apply bg-black/10 dark:bg-white/10 border-black/25 dark:border-white/25 text-black;
		@apply opacity-20;
		@apply pointer-events-none;
	}

	.input-number-component:focus-within,
	.input-number--filled {
		@apply overflow-visible;
	}

	.input-number--placeholder--required:after {
		@apply text-error inline-block font-normal relative;
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
