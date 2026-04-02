<template>
	<label class="text-input-component"
		:class="{
			'text-input--filled': (`${data}`.length > 0 && data !== null) || (type === 'date') || (type === 'datetime-local'),
			'text-input--muss': ((validationResult.fehlerart === ValidatorFehlerart.MUSS) || !valid(data)),
			'text-input--kann': (validationResult.fehlerart === ValidatorFehlerart.KANN),
			'text-input--hinweis': (validationResult.fehlerart === ValidatorFehlerart.HINWEIS),
			'text-input--disabled': disabled,
			'text-input--readonly': readonly,
			'text-input--select': isSelectInput,
			'text-input--statistics': statistics,
			'text-input--search': type === 'search',
			'text-input--date': (type === 'date') || (type === 'datetime-local'),
			'text-input-component--headless': headless,
			'col-span-full': span === 'full',
			'col-span-2': span === '2',
		}">
		<span v-if="url" class="pointer-events-none absolute left-0 pl-3 opacity-60 top-[0.32rem]">https://</span>
		<span class="icon i-ri-search-line text-input--search-icon" v-if="type === 'search'" />
		<span v-if="readonly && !isSelectInput"
			:class="{
				'text-input--control': !headless,
				'text-input--headless': headless,
				'text-input--rounded': rounded,
				'text-input--prefix': url
			}">
			{{ data }}
		</span>
		<input v-else ref="input"
			v-focus
			:class="{ 'text-input--control': !headless, 'text-input--headless': headless, 'text-input--rounded': rounded, 'text-input--prefix': url, }"
			v-bind="{ ...$attrs }"
			:type
			:min="minDate"
			:max="maxDate"
			:value="data"
			:disabled
			:required
			:readonly
			title=""
			:aria-labelledby="labelId"
			:placeholder="headless || type === 'search' ? placeholder : ''"
			@input="onInput"
			@keyup.enter="onKeyEnter"
			@blur="onBlur">
		<span v-if="placeholder && !headless && (type !== 'search')" :id="labelId" class="text-input--placeholder gap-1"
			:class="{ 'text-input--placeholder--prefix': url }">
			<span v-if="statistics" class="cursor-pointer">
				<svws-ui-tooltip position="right">
					<span class="inline-flex items-center">
						<span class="icon i-ri-bar-chart-2-line text-input--statistic-icon" />
					</span>
					<template #content>
						Relevant für die Statistik
					</template>
				</svws-ui-tooltip>
			</span>
			<span>{{ placeholder }}</span>
			<span v-if="((maxLen !== undefined) || (minLen !== undefined)) && !disabled" class="inline-flex gap-1"
				:class="{
					'text-ui-danger': (validatorLength !== null) && (validatorLength.getFehlerart() !== ValidatorFehlerart.UNGENUTZT),
					'opacity-50': (validatorLength === null) || (validatorLength.getFehlerart() === ValidatorFehlerart.UNGENUTZT)
				}">
				{{ (maxLen !== undefined) && (minLen === undefined) ? ` (max. ${maxLen} Zeichen)` : '' }}
				{{ (minLen !== undefined) && (maxLen === undefined) ? ` (mind. ${minLen} Zeichen)` : '' }}
				{{ (minLen !== undefined) && (maxLen !== undefined) && (minLen !== maxLen) ? ` (zwischen ${minLen} und ${maxLen} Zeichen)` : '' }}
				{{ (minLen !== undefined) && (maxLen !== undefined) && (minLen === maxLen) ? ` (genau ${maxLen} Zeichen)` : '' }}
			</span>
			<span v-if="required" class="icon-xs i-ri-asterisk text-input--placeholder--required text-input--state-icon" aria-hidden="true" />
			<span v-if="required" class="sr-only">erforderlich</span>
			<span class="cursor-pointer inline-block -my-1">
				<ui-validation-tooltip v-if="!validierungFehler.isEmpty()" :validation-result :disabled />
			</span>
			<span v-if="readonly && !isSelectInput" class="icon-xs i-ri-lock-line" />
		</span>
		<span v-if="removable && (type === 'date' || type === 'datetime-local') && (!readonly)" @keydown.enter="updateData('')" @click.stop="updateData('')" class="svws-icon--remove icon i-ri-close-line" tabindex="0" />
		<span v-if="(type === 'date') && !firefox()" class="svws-icon icon i-ri-calendar-2-line" />
		<span v-if="type === 'email'" class="svws-icon icon i-ri-at-line" />
		<span v-if="type === 'tel'" class="svws-icon icon i-ri-phone-line" />
	</label>
</template>


<script setup lang="ts">

	import { computed, type InputTypeHTMLAttribute, onBeforeMount, onBeforeUnmount, onMounted, ref, useId, watch } from "vue";
	import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
	import type { List } from "../../../../core/src/java/util/List";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";
	import { ValidationResult } from "../../validation/ValidationResult";
	import type { ValidatorFehler } from "../../../../core/src/asd/validate/ValidatorFehler";
	import { ValidatorInputRequired } from "../../validation/common/ValidatorInputRequired";
	import { ValidatorStringLength } from "../../validation/common/ValidatorStringLength";
	import { StringPattern, ValidatorStringMatchesPattern } from "../../validation/common/ValidatorStringMatchesPattern";
	import { ValidatorDateRange } from "../../validation/common/ValidatorDateRange";

	defineOptions({
		inheritAttrs: false,
	});

	function firefox() {
		return globalThis.navigator.userAgent.includes('Firefox/');
	}
	const input = ref<null | HTMLInputElement>(null);

	const props = withDefaults(defineProps<{
		type?: InputTypeHTMLAttribute
		minDate?: string;
		maxDate?: string;
		modelValue?: string | null;
		modelModifiers?: { trim: boolean };
		placeholder?: string;
		statistics?: boolean;
		valid?: (value: string | null) => boolean;
		validation?: () => List<ValidatorFehler>;
		disabled?: boolean;
		required?: boolean;
		readonly?: boolean;
		headless?: boolean;
		isSelectInput?: boolean;
		focus?: boolean;
		rounded?: boolean;
		url?: boolean;
		maxLen?: number;
		minLen?: number;
		span?: 'full' | '2';
		removable?: boolean;
	}>(), {
		type: "text",
		minDate: undefined,
		maxDate: undefined,
		modelValue: null,
		modelModifiers: () => ({ trim: false }),
		placeholder: "",
		statistics: false,
		valid: (value: string | null) => true,
		validation: undefined,
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
		/* Emit value when the value changes. */
		"update:modelValue": [value: string | null];
		/* Emit value on blur and keyDown event when the value changes */
		"change": [value: string | null];
		/* Emit value on blur event */
		"blur": [value: string | null];
		/* Emit value on blur and keyDown event */
		"commit": [value: string | null];
		/* Emit methods in onMounted and onBeforeUnmounted hooks */
		"methods": [ methods: { focus: () => void } | undefined ];
	}>();

	const vFocus = {
		mounted: (el: HTMLInputElement) => {
			if (props.focus) {
				el.focus();
			}
		},
	};

	const data = ref<string | null>(null);
	onBeforeMount(() => data.value = props.modelValue);

	const methods = { focus: () => doFocus() };
	onMounted(() => emit("methods", methods));
	onBeforeUnmount(() => emit("methods", undefined));

	watch(() => props.modelValue, (value: string | null) => updateData(value), { immediate: false });

	const validationResult = computed(() => new ValidationResult(validierungFehler.value));

	const validatorRequired = computed<ValidatorInputRequired<string> | null>(() => {
		if (props.required && (props.validation === undefined)) {
			return new ValidatorInputRequired(() => data.value);
		}
		return null;
	});

	const validatorLength = computed<ValidatorStringLength | null>(() => {
		if (((props.minLen !== undefined) || (props.maxLen !== undefined)) && (props.validation === undefined)) {
			return new ValidatorStringLength(() => data.value, props.minLen, props.maxLen);
		}
		return null;
	});

	const validatorEmail = computed<ValidatorStringMatchesPattern | null>(() => {
		if ((props.type === "email") && (props.validation === undefined)) {
			return new ValidatorStringMatchesPattern(() => data.value, StringPattern.IS_EMAIL);
		}
		return null;
	});

	const validatorTelefon = computed<ValidatorStringMatchesPattern | null>(() => {
		if ((props.type === "tel") && (props.validation === undefined)) {
			return new ValidatorStringMatchesPattern(() => data.value, StringPattern.IS_PHONE_NUMBER);
		}
		return null;
	});

	const validatorDateRange = computed<ValidatorDateRange | null>(() => {
		const typeIsDate = (props.type === "date") || (props.type === "datetime-local");
		const dateRangeDefined = (props.minDate !== undefined) || (props.maxDate !== undefined);

		if (typeIsDate && dateRangeDefined && (props.validation === undefined)) {
			return new ValidatorDateRange(() => data.value, props.minDate, props.maxDate);
		}
		return null;
	});

	const validierungFehler = computed<List<ValidatorFehler>>(() => {
		if (props.validation === undefined) {
			return getDefaultValidatorErrors();
		}
		return props.validation();

	});

	function getDefaultValidatorErrors() {
		const fehler = new ArrayList<ValidatorFehler>();
		const defaultValidators = [validatorRequired.value, validatorDateRange.value, validatorEmail.value, validatorLength.value, validatorTelefon.value];
		for (const validator of defaultValidators) {
			if (validator !== null) {
				validator.run();
				fehler.addAll(validator.getFehler());
			}
		}
		return fehler;
	}

	function updateData(value: string | null) {
		if (data.value !== value) {
			data.value = value;
			emit("update:modelValue", data.value);
		}
	}

	function onInput(event: Event) {
		const value = (event.target as HTMLInputElement).value;
		if (value !== data.value) {
			updateData(value);
		}
	}

	function onBlur() {
		emit("commit", data.value);
		if (props.modelValue !== data.value) {
			emit("change", data.value);
		}
		emit("blur", data.value);
	}

	function onKeyEnter() {
		emit("commit", data.value);
		if (props.modelValue !== data.value) {
			emit("change", data.value);
		}
	}

	function reset() {
		data.value = props.modelValue;
	}

	function doFocus() {
		input.value?.focus();
	}

	const labelId = useId();

	const content = computed<string | null>(() => data.value);

	defineExpose({ content, input, reset, doFocus });

</script>
