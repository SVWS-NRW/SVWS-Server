<template>
	<label class="text-input-component"
		:class="{
			'text-input--filled': (`${data}`.length > 0 && data !== null) || type === 'date',
			'text-input--invalid': (isValid === false),
			'text-input--statistic-muss': ((validator !== undefined) && (!validator().getFehler().isEmpty()) && (validator().getFehlerart() === ValidatorFehlerart.MUSS)),
			'text-input--statistic-kann': ((validator !== undefined) && (!validator().getFehler().isEmpty()) && (validator().getFehlerart() === ValidatorFehlerart.KANN)),
			'text-input--statistic-hinweis': ((validator !== undefined) && (!validator().getFehler().isEmpty()) && (validator().getFehlerart() === ValidatorFehlerart.HINWEIS)),
			'text-input--disabled': disabled,
			'text-input--readonly': readonly,
			'text-input--select': isSelectInput,
			'text-input--statistics': statistics,
			'text-input--search': type === 'search',
			'text-input--date': type === 'date',
			'text-input-component--headless': headless,
			'col-span-full': span === 'full',
			'col-span-2': span === '2',
		}">
		<span v-if="url" data-before="https://" class="pointer-events-none absolute inset-y-0 left-0 flex items-center pl-3 opacity-25 before:content-[attr(data-before)]" />
		<span class="icon i-ri-search-line text-input--search-icon" v-if="type === 'search'" />
		<div v-if="readonly && !isSelectInput" :class="{ 'text-input--control': !headless, 'text-input--headless': headless, 'text-input--rounded': rounded, 'text-input--prefix': url, }">
			{{ data }}
		</div>
		<input v-else ref="input"
			v-focus
			:class="{ 'text-input--control': !headless, 'text-input--headless': headless, 'text-input--rounded': rounded, 'text-input--prefix': url, }"
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
		<span v-if="placeholder && !headless && (type !== 'search')" :id="labelId" class="text-input--placeholder"
			:class="{ 'text-input--placeholder--required': required, 'text-input--placeholder--prefix': url }">
			<span>{{ placeholder }}</span>
			<span class="icon-sm i-ri-alert-line ml-0.5 inline-block -mt-0.5 icon-error" v-if="(isValid === false && !required)" />
			<span v-if="(maxLen !== undefined) || (minLen !== undefined)" class="inline-flex ml-1 gap-1" :class="{'text-ui-danger': !maxLenValid || !minLenValid, 'opacity-50': maxLenValid && minLenValid}">
				{{ (maxLen !== undefined) && (minLen === undefined) ? ` (max. ${maxLen} Zeichen)` : '' }}
				{{ (minLen !== undefined) && (maxLen === undefined) ? ` (mind. ${minLen} Zeichen)` : '' }}
				{{ (minLen !== undefined) && (maxLen !== undefined) ? ` (zwischen ${minLen} und ${maxLen} Zeichen)` : '' }}
			</span>
			<span v-if="statistics" class="cursor-pointer inline-block -my-1">
				<svws-ui-tooltip position="right">
					<span class="inline-flex items-center ml-1 -mb-2 mt-0.5 pointer-events-auto">
						<span class="icon i-ri-bar-chart-2-line icon-statistics" />
						<template v-if="(validator === undefined) || (validator().getFehler().isEmpty()) || (validator().getFehlerart() === ValidatorFehlerart.UNGENUTZT)">
							<span class="icon i-ri-alert-fill icon-error" v-if="(data === '') || (data === null) || (data === undefined)" />
						</template>
						<template v-else>
							<span class="icon i-ri-alert-fill icon-danger" v-if="validator().getFehlerart() === ValidatorFehlerart.MUSS" />
							<span class="icon i-ri-error-warning-fill icon-caution" v-if="validator().getFehlerart() === ValidatorFehlerart.KANN" />
							<span class="icon i-ri-question-fill icon-warning" v-if="validator().getFehlerart() === ValidatorFehlerart.HINWEIS" />
						</template>
					</span>
					<template #content>
						<template v-if="(validator !== undefined) && (!validator().getFehler().isEmpty()) && (validator().getFehlerart() !== ValidatorFehlerart.UNGENUTZT)">
							<div class="text-ui-statistic text-headline-sm text-center pt-1"> Relevant für die Statistik </div>
							<div v-for="fehler in validator().getFehler()" :key="fehler.hashCode" class="pt-2 pb-2">
								<div class="rounded pl-2" :class="{
									'bg-ui-danger': (validator().getFehlerart() === ValidatorFehlerart.MUSS),
									'bg-ui-caution': (validator().getFehlerart() === ValidatorFehlerart.KANN),
									'bg-ui-warning': (validator().getFehlerart() === ValidatorFehlerart.HINWEIS)}">
									{{ fehler.getFehlerart() }}
								</div>
								<div class="pl-2"> {{ fehler.getFehlermeldung() }} </div>
							</div>
						</template>
						<template v-else>
							<div class="text-ui-statistic text-headline-sm text-center"> Relevant für die Statistik </div>
						</template>
					</template>
				</svws-ui-tooltip>
			</span>
		</span>
		<span v-if="removable && (type === 'date') && (!readonly)" @keydown.enter="updateData('')" @click.stop="updateData('')" class="svws-icon--remove icon i-ri-close-line" tabindex="0" />
		<span v-if="(type === 'date') && !firefox()" class="svws-icon icon i-ri-calendar-2-line" />
		<span v-if="type === 'email'" class="svws-icon icon i-ri-at-line" />
		<span v-if="type === 'tel'" class="svws-icon icon i-ri-phone-line" />
	</label>
</template>


<script setup lang="ts" generic="T extends JavaObject, V extends Validator<T>">

	import { ref, computed, watch, type ComputedRef, type Ref, onBeforeMount, onMounted, onBeforeUnmount, useId } from "vue";
	import type { Validator } from "../../../core/src/asd/validate/Validator";
	import type { JavaObject } from "../../../core/src/java/lang/JavaObject";
	import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

	defineOptions({
		inheritAttrs: false,
	});

	function firefox() {
		return window.navigator.userAgent.includes('Firefox/')
	}
	const input = ref<null | HTMLInputElement>(null);

	const props = withDefaults(defineProps<{
		type?: "text" | "date" | "email" | "search" | "tel" | "password";
		modelValue?: string | null;
		placeholder?: string;
		statistics?: boolean;
		valid?: (value: string | null) => boolean;
		validator?: () => V;
		doValidate?: (validator: V, value: string | null) => boolean;
		disabled?: boolean;
		required?: boolean;
		readonly?: boolean;
		headless?: boolean;
		isSelectInput? : boolean;
		focus?: boolean;
		rounded?: boolean;
		url?: boolean;
		maxLen?: number;
		minLen?: number;
		span?: 'full' | '2';
		removable?: boolean;
	}>(), {
		type: "text",
		modelValue: null,
		placeholder: "",
		statistics: false,
		valid: (value: string | null) => true,
		validator: undefined,
		doValidate: (validator: V, value: string | null) : boolean => validator.run(),
		disabled: false,
		required: false,
		readonly: false,
		headless: false,
		isSelectInput: false,
		focus: false,
		rounded: false,
		url: false,
		maxLen: undefined,
		minLen: undefined,
		span: undefined,
		removable: false,
	});

	const emit = defineEmits<{
		"update:modelValue": [value: string | null];
		"change": [value: string | null];
		"blur": [value: string | null];
		"methods": [ methods: { focus: () => void } | undefined ];
	}>();

	const vFocus = {
		mounted: (el: HTMLInputElement) => {
			if (props.focus)
				el.focus();
		}
	};

	const data = ref<string | null>(null);
	onBeforeMount(() => data.value = props.modelValue);

	const methods = { focus: () => doFocus() };
	onMounted(() => emit("methods", methods));
	onBeforeUnmount(() => emit("methods", undefined));

	watch(() => props.modelValue, (value: string | null) => updateData(value), { immediate: false });

	const validatorEmail = (value: string | null) : boolean => ((value === null) || (value === '')) ? true : (
		/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(value) ||
		/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value)
	);

	const isValid = computed((): boolean => {
		let tmpIsValid = true;
		if (props.required && ((data.value === null) || (data.value === '')))
			tmpIsValid = false;
		if (props.validator !== undefined)
			return props.doValidate(props.validator(), data.value);
		if (tmpIsValid && (!minLenValid.value || !maxLenValid.value))
			tmpIsValid = false;
		if (tmpIsValid && props.type === "email")
			tmpIsValid = validatorEmail(data.value ?? '');
		if (tmpIsValid)
			tmpIsValid = props.valid(data.value);
		return tmpIsValid;
	})

	function updateData(value: string | null) {
		if (data.value !== value) {
			data.value = value;
			emit("update:modelValue", data.value);
		}
	}

	const minLenValid = computed((): boolean => {
		if ((props.minLen === undefined) || ((data.value === null) && (props.minLen <= 0)))
			return true;
		return (data.value !== null) && (data.value.toLocaleString().length >= props.minLen);
	})

	const maxLenValid = computed((): boolean => {
		if ((props.maxLen === undefined) || (data.value === null))
			return true;
		return data.value.toLocaleString().length <= props.maxLen;
	})

	function onInput(event: Event) {
		const value = (event.target as HTMLInputElement).value;
		if (value !== data.value)
			updateData(value);
	}

	function onBlur(event: Event) {
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

	function doFocus() {
		input.value?.focus();
	}

	const labelId = useId();

	const content = computed<string | null>(() => data.value);

	defineExpose<{
		content: ComputedRef<string | null>,
		input: Ref<HTMLInputElement | null>,
		reset: () => void;
		doFocus: () => void;
	}>({ content, input, reset, doFocus });

</script>


<style lang="postcss">

	.text-input-component {
		@apply text-ui;
		@apply flex;
		@apply relative;
		@apply w-full;
		@apply overflow-hidden whitespace-nowrap text-base;

		input::placeholder {
			@apply opacity-30;

			.placeholder--visible & {
				@apply opacity-100;
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

	.dark .text-input-component .icon.svws-icon,
	.htw-dark .text-input-component .icon.svws-icon,
	.dark .text-input-component .text-input--search-icon,
	.htw-dark .text-input-component .text-input--search-icon {
		@apply icon-white;
	}

	.text-input-component .icon.svws-icon {
		@apply pointer-events-none absolute top-1 right-1.5 bottom-1 w-5 rounded inline-flex items-center justify-end pr-1 text-base opacity-50 mt-1;

		&--remove {
			@apply pointer-events-auto cursor-pointer absolute top-1 right-1 bottom-1 w-5 rounded inline-flex items-center justify-end pr-1 text-base mr-6 mt-1;

			.dark &,
			.htw-dark & {
				@apply icon-white;
			}

			&:hover {
				@apply icon-danger;
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

			.svws-icon.icon {
				@apply w-6 h-6 -top-1 right-0 relative;
			}
		}
	}

	.text-input--statistics .svws-icon.icon {
		@apply text-ui-statistic-secondary icon-statistics;
	}

	.text-input--invalid .svws-icon {
		@apply text-ui-danger icon-danger;
	}

	.text-input--statistic-kann .svws-icon {
		@apply text-ui-caution icon-caution;
	}

	.text-input--statistic-hinweis .svws-icon {
		@apply text-ui-warning icon-warning;
	}

	.text-input--control {
		@apply bg-ui border border-ui-secondary;
		@apply rounded-md;
		@apply h-9 w-full;
		@apply text-base;
		@apply whitespace-nowrap;
		padding: 0.5em 0.7em;

		&:focus {
			@apply outline-none;
		}
	}

	.text-input--prefix {
		padding-left: 4.2em;
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
		@apply border-ui;
	}

	.text-input-component .text-input--control:focus-visible {
		@apply ring ring-ui-neutral;
	}

	.text-input--statistics.text-input-component:focus-within .text-input--control,
	.text-input--statistics .text-input--control {
		@apply border-ui-statistic;
	}

	.text-input--invalid.text-input-component:focus-within .text-input--control,
	.text-input--invalid .text-input--control {
		@apply border-ui-danger;
	}

	.text-input--statistic-kann.text-input-component:focus-within .text-input--control,
	.text-input--statistic-kann .text-input--control {
		@apply border-ui-caution;
	}

	.text-input--statistic-hinweis.text-input-component:focus-within .text-input--control,
	.text-input--statistic-hinweis .text-input--control {
		@apply border-ui-warning;
	}

	.text-input--statistics:not(.text-input--filled) .text-input--control {
		@apply border-ui-statistic-secondary;
	}

	.text-input--invalid.text-input--filled:not(:focus-within) .text-input--control {
		@apply border-ui-danger;
	}

	.text-input--statistic-kann.text-input--filled:not(:focus-within) .text-input--control {
		@apply border-ui-caution;
	}

	.text-input--statistic-hinweis.text-input--filled:not(:focus-within) .text-input--control {
		@apply border-ui-warning;
	}

	.text-input--control--multiselect-tags {
		@apply border-b-0 rounded-b-none pt-1 pb-0;
	}

	.text-input--statistics {
		.tooltip-trigger--triggered span.icon {
			@apply text-ui-statistic;
		}
	}

	.text-input--search {
		@apply relative;

		&.text-input--filled {
			@apply text-ui-brand;
		}

		&-icon {
			@apply absolute left-2 opacity-25;
			top: 50%;
			transform: translateY(-50%) scale(90%);

			.text-input-component:not(.text-input--filled):not(:focus-within):not(.text-input--disabled):hover & {
				@apply opacity-100;
			}

			.text-input-component:focus-within &,
			.text-input--filled & {
				@apply opacity-100;
				transform: translateY(-50%) scale(100%);
			}
		}

		input {
			@apply pl-8;

			&::placeholder {
				@apply text-ui opacity-50 font-normal;
			}
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
		@apply opacity-50 font-medium;
		@apply transform;
		@apply flex items-center font-medium;

		top: 0.5em;
		left: 0.7em;
		line-height: 1.33;
	}

	.text-input-component:not(.text-input--filled):not(:focus-within) .text-input--placeholder {
		@apply font-normal;

		.wrapper--tag-list &,
		.wrapper--tag-list:not(.wrapper--filled) & {
			@apply font-medium;
		}
	}

	.text-input-component:not(.text-input--filled):not(:focus-within):not(.text-input--disabled):not(.text-input--readonly):hover .text-input--placeholder,
	.text-input-component.text-input--search:not(:focus-within):not(.text-input--disabled):hover input::placeholder {
		@apply opacity-75;
	}

	.text-input--placeholder--prefix {
		left: 4.3em;
		top: 0.5em;
	}

	.multiselect-input-component .text-input--placeholder {
		top: 0.5em;
	}

	.text-input-component:focus-within .text-input--placeholder,
	.text-input--filled .text-input--placeholder {
		@apply bg-ui opacity-100;
		@apply -translate-y-1/2;
		@apply rounded;
		@apply px-1;

		top: 0;
		left: 0.7em;
		font-size: 0.78rem;
	}

	.text-input--statistics .text-input--placeholder {
		@apply text-ui-statistic font-bold;
	}

	.text-input--invalid .text-input--placeholder,
	.text-input--invalid:not(:focus-within) .text-input--control {
		@apply text-ui-danger;
	}

	.text-input--statistic-kann .text-input--placeholder,
	.text-input--statistic-kann:not(:focus-within) .text-input--control {
		@apply text-ui-caution;
	}

	.text-input--statistic-hinweis .text-input--placeholder,
	.text-input--statistic-hinweis:not(:focus-within) .text-input--control {
		@apply text-ui-warning;
	}

	.text-input--control:disabled,
	.text-input--disabled {
		.text-input--control {
			@apply bg-ui text-ui-disabled border-ui-disabled;
			@apply pointer-events-none;

		}

		.text-input--placeholder {
			@apply bg-ui text-ui-disabled;
		}
	}

	.text-input-component:focus-within,
	.text-input--filled {
		@apply overflow-visible;
	}

	.text-input--placeholder--required:after {
		@apply inline-block font-bold relative opacity-50;
		content: "*";
		font-size: 1em;
		margin-bottom: -0.2em;
		top: -0.2em;
		left: 0.1em;
	}

	.text-input--invalid .text-input--placeholder--required:after {
		@apply text-ui-danger opacity-100;
	}

	.text-input--statistic-kann .text-input--placeholder--required:after {
		@apply text-ui-caution opacity-100;
	}

	.text-input--statistic-hinweis .text-input--placeholder--required:after {
		@apply text-ui-warning opacity-100;
	}

	.text-input-component--headless .text-input--control {
		&:not([class*="text-ui"]) {
			color: inherit;
		}
	}

	.text-input--headless,
	.svws-ui-table .text-input--control {
		@apply w-full whitespace-nowrap border-0 outline-none;

		&:not([class*="bg-"]) {
			background-color: unset;
		}

		&:not([class*="text-"]) {
			color: inherit;
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

	.text-input-component .icon.svws-icon--remove:focus-visible {
		-webkit-filter: invert(22%) sepia(96%) saturate(2323%) hue-rotate(331deg) brightness(88%) contrast(103%);
		filter: invert(22%) sepia(96%) saturate(2323%) hue-rotate(331deg) brightness(88%) contrast(103%);
	}

</style>
