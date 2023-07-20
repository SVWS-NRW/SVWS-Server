<script lang="ts">
	export default {
		inheritAttrs: false
	};
</script>
<script setup lang="ts">
	import type { InputType } from "../types";
	import { useSlots, ref, computed } from "vue";
	import { genId } from "../utils";

	const props = withDefaults(defineProps<{
		type?: InputType;
		modelValue?: string | number | null;
		placeholder?: string;
		statistics?: boolean;
		valid?: boolean;
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
		modelValue: "",
		placeholder: "",
		statistics: false,
		valid: true,
		disabled: false,
		required: false,
		readonly: false,
		headless: false,
		focus: false,
		rounded: false,
		url: false,
		maxLen: undefined,
		span: undefined
	});

	const emit = defineEmits<{
		(e: "update:modelValue", value: string | number): void;
	}>();

	const slots = useSlots();
	const input = ref<null | HTMLElement>(null);
	const vFocus = {
		mounted: (el: HTMLInputElement) => {
			if (props.focus) el.focus();
		}
	};

	const maxLenValid = computed(()=>{
		if (props.maxLen === undefined)
			return true;
		return typeof props.modelValue === 'string' && props.modelValue.toLocaleString().length <= props.maxLen;
	})

	const emailValid = computed(() => {
		if (props.type !== "email" || !props.modelValue || typeof props.modelValue === 'number')
			return true;
		else
			return (
				// eslint-disable-next-line no-useless-escape
				/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(props.modelValue) ||
				// eslint-disable-next-line no-useless-escape
				/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
					props.modelValue
				)
			);
	});

	const hasIcon = computed(() => !!slots.default);

	function onInput(event: Event) {
		emit("update:modelValue", (event.target as HTMLInputElement).value);
	}

	defineExpose({
		input
	});

	const labelId = genId();
</script>

<template>
	<label class="text-input-component"
		:class="{
			'text-input--filled': `${modelValue}`.length > 0 && `${modelValue}` !== 'null',
			'text-input--invalid': (valid === false) || (emailValid === false) || (maxLenValid === false),
			'text-input--disabled': disabled,
			'text-input--readonly': readonly,
			'text-input--icon': hasIcon,
			'text-input--statistics': statistics,
			'text-input--search': type === 'search',
			'col-span-full': span === 'full',
			'col-span-2': span === '2',
		}">
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
			:value="modelValue"
			:disabled="disabled"
			:required="required"
			:readonly="readonly"
			:aria-labelledby="labelId"
			:placeholder="headless || type === 'search' ? placeholder : ''"
			@input="onInput">
		<span v-if="placeholder && !headless && type !== 'search'"
			:id="labelId"
			class="text-input--placeholder"
			:class="{
				'text-input--placeholder--required': required,
				'text-input--placeholder--prefix': url
			}">
			{{ placeholder }}
			<i-ri-alert-line v-if="(valid === false) || (emailValid === false) || (maxLenValid === false)" class="ml-0.5" />
			<span v-if="maxLen" class="inline-flex ml-1 gap-1" :class="{'text-error': !maxLenValid, 'opacity-50': maxLenValid}">{{ maxLen ? ` (${modelValue?.toLocaleString() ? modelValue?.toLocaleString().length + '/' : 'maximal '}${maxLen} Zeichen)` : '' }}</span>
			<span v-if="statistics" class="cursor-pointer">
				<svws-ui-tooltip position="right">
					<span class="inline-flex items-center">
						<i-ri-bar-chart-2-line class="pointer-events-auto ml-0.5" />
						<i-ri-alert-fill v-if="`${modelValue}`.length === 0 || `${modelValue}` === 'null'" />
					</span>
					<template #content>
						Relevant für die Statistik
					</template>
				</svws-ui-tooltip>
			</span>
		</span>
		<span v-if="type !== 'date' && hasIcon" class="icon">
			<slot />
		</span>
		<span v-else-if="type === 'date'" class="icon text-input--calendar-icon">
			<i-ri-calendar-line />
		</span>
	</label>
</template>

<style lang="postcss" scoped>
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
	}

	.text-input-component .icon {
		@apply absolute;
		@apply flex;
		@apply inset-y-0 right-0;
		@apply items-center justify-center;
		@apply opacity-20;
		@apply pointer-events-none;
		@apply rounded;
		@apply w-8;

		margin-bottom: 1px;
		margin-right: 1px;
		margin-top: 1px;
	}

	.text-input-component:focus-within .icon,
	.text-input--filled .icon {
		@apply opacity-100;
	}

	.text-input--invalid .icon {
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

	.text-input--control[type="number"] {
		padding-right: 0.5em;
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

		&-icon {
			@apply absolute left-2 opacity-40;
			top: 50%;
			transform: translateY(-50%);

			.text-input-component:not(.text-input--filled):not(:focus-within):not(.text-input--disabled):hover & {
				@apply opacity-60;
			}

			.text-input-component:focus-within &,
			.text-input--filled & {
				@apply opacity-100;
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

	.text-input--control[type="date"] {
		color: transparent;
		@apply bg-transparent dark:bg-transparent;
	}

	.text-input-component:focus-within .text-input--control[type="date"],
	.text-input--filled .text-input--control[type="date"] {
		@apply text-black dark:text-white;
		@apply pr-1;
	}

	@-moz-document url-prefix() {
		.text-input-component:focus-within .text-input--calendar-icon,
		.text-input--filled .text-input--calendar-icon {
			@apply hidden;
		}
	}

	.text-input--readonly .text-input--control {
		@apply pointer-events-none cursor-default select-none;
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

	.text-input--icon .text-input--control {
		@apply pr-8;
	}

	.text-input--headless,
	.data-table .text-input--control {
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

			&::placeholder {
				@apply opacity-60;
			}
		}
	}
	.text-input--inline {
		@apply cursor-text underline decoration-dotted underline-offset-2;
	}
</style>
