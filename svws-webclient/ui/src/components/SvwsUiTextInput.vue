<template>
	<label class="text-input-component"
		:class="{
			'text-input--filled': (`${data}`.length > 0 && data !== null) || type === 'date',
			'text-input--invalid': (isValid === false),
			'text-input--disabled': disabled,
			'text-input--readonly': readonly,
			'text-input--statistics': statistics,
			'text-input--search': type === 'search',
			'text-input--date': type === 'date',
			'text-input--number': type === 'number',
			'text-input-component--headless': headless,
			'col-span-full': span === 'full',
			'col-span-2': span === '2',
		}">
		<span class="absolute">
			<slot name="tags" />
		</span>
		<span v-if="url" data-before="https://" class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3 opacity-60 before:content-[attr(data-before)]" />
		<i-ri-search-line v-if="type === 'search'" class="text-input--search-icon" />
		<input ref="input"
			v-focus
			:class="{
				'text-input--control': !headless,
				'text-input--headless': headless,
				'text-input--rounded': rounded,
				'text-input--prefix': url,
			}"
			v-bind="{ ...$attrs }"
			:type="type"
			:value="data"
			:disabled="disabled"
			:required="required"
			:readonly="readonly"
			:aria-labelledby="labelId"
			:placeholder="headless || type === 'search' ? placeholder : ''"
			@input="onInput"
			@keyup.enter="onKeyEnter"
			@blur="onBlur">
		<span v-if="placeholder && !headless && type !== 'search'"
			:id="labelId"
			class="text-input--placeholder"
			:class="{
				'text-input--placeholder--required': required,
				'text-input--placeholder--prefix': url
			}">
			<span>{{ placeholder }}</span>
			<i-ri-alert-line v-if="(isValid === false)" class="ml-0.5" />
			<span v-if="maxLen" class="inline-flex ml-1 gap-1" :class="{'text-error': !maxLenValid, 'opacity-50': maxLenValid}">{{ maxLen ? ` (${data?.toLocaleString() ? data?.toLocaleString().length + '/' : 'maximal '}${maxLen} Zeichen)` : '' }}</span>
			<span v-if="statistics" class="cursor-pointer">
				<svws-ui-tooltip position="right">
					<span class="inline-flex items-center">
						<i-ri-bar-chart-2-line class="pointer-events-auto ml-0.5" />
						<i-ri-alert-fill v-if="data === '' || data === null || data === undefined" />
					</span>
					<template #content>
						Relevant für die Statistik
					</template>
				</svws-ui-tooltip>
			</span>
		</span>
		<span v-if="type === 'date'" class="svws-icon">
			<i-ri-calendar-line />
		</span>
		<span v-if="type === 'email'" class="svws-icon">
			<i-ri-at-line />
		</span>
		<span v-if="type === 'tel'" class="svws-icon">
			<i-ri-phone-line />
		</span>
		<span v-if="type === 'number' && input" class="svws-input-stepper">
			<button role="button" @click="onInputNumber('down')" @blur="onBlur" :class="{'svws-disabled': $attrs?.min === input?.value || ($attrs?.min === '0' && !input?.value)}"><i-ri-subtract-line /></button>
			<button role="button" @click="onInputNumber('up')" @blur="onBlur" :class="{'svws-disabled': $attrs?.max === input?.value}"><i-ri-add-line /></button>
		</span>
	</label>
</template>


<script setup lang="ts">

	import type { InputType, InputDataType } from "../types";
	import { ref, computed, watch, type ComputedRef, type Ref } from "vue";
	import { genId } from "../utils";

	defineOptions({
		inheritAttrs: false,
	});

	const input = ref<null | HTMLInputElement>(null);

	const props = withDefaults(defineProps<{
		type?: InputType;
		modelValue?: InputDataType;
		placeholder?: string;
		statistics?: boolean;
		valid?: (value: InputDataType) => boolean;
		disabled?: boolean;
		required?: boolean;
		readonly?: boolean;
		headless?: boolean;
		focus?: boolean;
		rounded?: boolean;
		url?: boolean;
		maxLen?: number;
		span?: 'full' | '2';
	}>(), {
		type: "text",
		modelValue: null,
		placeholder: "",
		statistics: false,
		valid: () => true,
		disabled: false,
		required: false,
		readonly: false,
		headless: false,
		focus: false,
		rounded: false,
		url: false,
		maxLen: undefined,
		span: undefined,
	});

	const emit = defineEmits<{
		"update:modelValue": [value: string];   // TODO use InputDataType
		"change": [value: string];              // TODO use InputDataType
		"blur": [value: string];                // TODO use InputDataType
	}>();

	const vFocus = {
		mounted: (el: HTMLInputElement) => {
			if (props.focus)
				el.focus();
		}
	};

	// eslint-disable-next-line vue/no-setup-props-destructure
	const data = ref<InputDataType>(props.modelValue);

	watch(() => props.modelValue, (value: InputDataType) => updateData(value), { immediate: false });

	function validatorEmail(value: string) {
		if (value === '')
			return true;
		return (
			// eslint-disable-next-line no-useless-escape
			/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
			// eslint-disable-next-line no-useless-escape
			/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value)
		);
	}

	const isValid = computed(()=>{
		let tmpIsValid = true;
		if ((props.required === true) && (data.value === null))
			return false;
		if ((props.type === "email") && (typeof data.value === 'string'))
			tmpIsValid = validatorEmail(data.value);
		if (tmpIsValid && (props.maxLen !== undefined) && (data.value !== null) && (typeof data.value === 'string') && (data.value.toLocaleString().length <= props.maxLen))
			tmpIsValid = false;
		if (tmpIsValid && (data.value !== null || data.value !== '' || data.value !== undefined))
			tmpIsValid = props.valid(data.value);
		return tmpIsValid;
	})

	function updateData(value: InputDataType) {
		if (data.value !== value) {
			data.value = value;
			emit("update:modelValue", String(data.value));   // TODO do not use String()
		}
	}

	const maxLenValid = computed(() => {
		if ((props.maxLen === undefined) || (data.value === null))
			return true;
		return (typeof data.value === 'string') && (data.value.toLocaleString().length <= props.maxLen);
	})

	const emailValid = computed(() => {
		if ((props.type === "email") && (typeof data.value === 'string'))
			return validatorEmail(data.value);
		return true;
	});

	function onInput(event: Event) {
		const value = (event.target as HTMLInputElement).value;
		if (value != data.value)
			updateData(props.type === "number" ? Number(value) : value);
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
		if (props.modelValue !== data.value)
			emit("change", String(data.value));   // TODO do not use String()
		emit("blur", String(data.value));   // TODO do not use String()
	}

	function onKeyEnter(event: Event) {
		if (props.modelValue !== data.value)
			emit("change", String(data.value));   // TODO do not use String()
	}

	const labelId = genId();

	const content = computed<InputDataType>(() => data.value);

	defineExpose<{
		content: ComputedRef<InputDataType>,
		input: Ref<HTMLInputElement | null>,
	}>({
		content,
		input
	});

</script>


<style lang="postcss">
	.text-input-component {
		@apply flex;
		@apply relative;
		@apply w-full;
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

      &[type="email"],
      &[type="tel"] {
        @apply pr-[1.6rem];
      }

      &:focus {
        @apply outline-none;
      }
    }
	}

	.text-input-component .svws-icon {
		@apply pointer-events-none absolute top-1 right-1 bottom-1 bg-white dark:bg-black w-5 rounded inline-flex items-center justify-end pr-1 text-base;

    svg {
      @apply opacity-25 -mr-0.5;
    }
	}

  .text-input-component {
    &:hover,
    &:focus-within {
      .svws-icon svg {
        @apply opacity-50;
      }
    }
  }

  .text-input--date {
    .svws-ui-table & {
      input {
        @apply -my-0.5;
      }
    }

    .text-input--control {
      @apply pr-0;
    }

    .svws-icon {
      @apply w-7;
    }

    &.text-input-component--headless,
    .svws-ui-table .svws-ui-tbody .svws-ui-td & {
      @apply my-auto -ml-1;

      .svws-icon {
        @apply w-6 h-6 -top-1 right-0;

        svg {
          @apply relative top-px;
        }
      }
    }

    &:focus-within {
      .svws-icon svg {
        @apply opacity-75;
      }
    }
  }

  .text-input--statistics .svws-icon {
    @apply text-violet-500;

    svg {
      @apply opacity-50;
    }
  }

  .text-input--number {
    input {
      @apply pr-12;
      appearance: textfield;
    }

    .svws-input-stepper {
      @apply absolute top-1 right-1 bottom-1 flex justify-center items-center gap-0.5;

      button {
        @apply bg-light dark:bg-white/5 border border-black/10 dark:border-white/10 rounded text-base focus:outline-none;

        &:hover,
        &:focus-visible {
          @apply bg-black/10 dark:bg-white/10;
        }

        &:focus-visible {
          @apply ring-2 ring-offset-1 ring-black/25 dark:ring-white/25;
        }

        &.svws-disabled {
          @apply pointer-events-none opacity-25;

          svg {
            @apply opacity-50;
          }
        }
      }
    }

    .text-input--placeholder {
      max-width: calc(100% - 0.7em);
    }
  }

	.text-input--invalid .svws-icon {
		@apply text-error;
	}

	.text-input--control {
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

	.text-input--prefix {
		padding-left: 4.3em;
	}

	.text-input--rounded {
		@apply rounded-full;
	}

	.multiselect-input-component .text-input--control,
	.multiselect-input-component .text-input--headless {
		@apply overflow-hidden text-ellipsis;
		padding-right: 3.2rem;
	}

	.text-input-component:focus-within .text-input--control,
	.text-input--filled .text-input--control {
		@apply border-black dark:border-white;
		@apply outline-none;
	}

	.text-input--filled:not(:focus-within):not(:hover) .text-input--control {
		@apply border-black/25 dark:border-white/25;
	}

	.text-input--statistics.text-input--filled:not(:focus-within):not(:hover) .text-input--control,
	.text-input--statistics:not(:focus-within):not(:hover) .text-input--control {
		@apply border-violet-500/25 dark:border-violet-800/25;
	}

	.text-input--invalid.text-input--filled:not(:focus-within):not(:hover) .text-input--control {
		@apply border-error/25 dark:border-error/25;
	}

	.text-input--statistics.text-input--filled:not(:focus-within):hover .text-input--control,
	.text-input--statistics:not(:focus-within):hover .text-input--control {
		@apply border-violet-500/50 dark:border-violet-800/50;
	}

	.text-input--invalid.text-input--filled:not(:focus-within):hover .text-input--control {
		@apply border-error/50 dark:border-error/50;
	}

	.text-input--invalid.text-input--filled:focus-within:hover .text-input--control {
		@apply border-error/50 dark:border-error/50;
	}

	.text-input--filled:not(:focus-within):hover .text-input--control {
		@apply border-black/50 dark:border-white/50;
	}

	.text-input--statistics.text-input-component:focus-within .text-input--control,
	.text-input--statistics.text-input--filled .text-input--control {
		@apply border-violet-500;
	}

	.text-input--control--multiselect-tags {
		@apply border-b-0 rounded-b-none pt-1 pb-0;
	}

	.text-input--statistics {
		.tooltip-trigger--triggered svg {
			@apply text-violet-800;
		}
	}

	.text-input--search {
		@apply relative;

    &.text-input--filled {
      @apply text-svws;
    }

		&-icon {
			@apply absolute left-2 opacity-25;
			top: 50%;
			transform: translateY(-50%) scale(90%);

			.text-input-component:not(.text-input--filled):not(:focus-within):not(.text-input--disabled):hover & {
				@apply opacity-50;
			}

			.text-input-component:focus-within &,
			.text-input--filled & {
				@apply opacity-100;
        transform: translateY(-50%) scale(100%);
			}
		}

		input {
			@apply pl-8;
		}
	}

	.text-input--control[type="date"]::-webkit-inner-spin-button,
	.text-input--control[type="date"]::-webkit-calendar-picker-indicator {
		@apply opacity-0;
	}

	.text-input--readonly .text-input--control {
		@apply pointer-events-auto cursor-default select-none;
	}

	.text-input--placeholder {
		@apply absolute;
		@apply pointer-events-none;
		@apply opacity-60;
		@apply transform;
		@apply flex items-center font-medium;

		top: 0.5em;
		left: 0.7em;
		line-height: 1.33;
	}

	.text-input-component:not(.text-input--filled) .text-input--placeholder {
		@apply font-normal;

		.wrapper--tag-list & {
			@apply font-medium;
		}

		.wrapper--tag-list:not(.wrapper--filled) & {
			@apply font-medium;
		}
	}

	.text-input-component:not(.text-input--filled):not(:focus-within):not(.text-input--disabled):hover .text-input--placeholder {
		@apply opacity-100;
	}

	.text-input--placeholder--prefix {
		left: 4.5em;
		top: 0.5em;
	}

	.multiselect-input-component .text-input--placeholder {
		top: 0.5em;
	}

	.text-input-component:focus-within .text-input--placeholder,
	.text-input--filled .text-input--placeholder {
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

	.text-input--statistics .text-input--placeholder {
		@apply text-violet-500 font-bold;
	}

	.text-input--invalid:not(:focus-within) .text-input--placeholder,
	.text-input--invalid:not(:focus-within) .text-input--control {
		@apply text-error;
	}

	.text-input--disabled {
		@apply cursor-not-allowed;

		.text-input--placeholder {
			@apply text-black/25 dark:text-white/25;
		}
	}

	.text-input--control:disabled {
		@apply bg-black/10 dark:bg-white/10 border-black/25 dark:border-white/25 text-black;
		@apply opacity-20;
		@apply pointer-events-none;
	}

	.text-input-component:focus-within,
	.text-input--filled {
		@apply overflow-visible;
	}

	.text-input--placeholder--required:after {
		@apply text-error inline-block font-normal relative;
		content: "*";
		font-size: 1.2em;
		margin-bottom: -0.2em;
		top: -0.1em;
		left: 0.1em;
	}

	.text-input--headless,
	.svws-ui-table .text-input--control {
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

	.text-input--inline {
		@apply cursor-text underline decoration-dotted underline-offset-2;
	}

</style>
