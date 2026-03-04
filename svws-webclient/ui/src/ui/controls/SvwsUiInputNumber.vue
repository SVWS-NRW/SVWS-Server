<template>
	<div class="input-number-component"
		:class="{
			'input-number--filled': (data !== null) && (data !== undefined),
			'input-number--muss': ((validationResult.fehlerart === ValidatorFehlerart.MUSS) || !valid(data)),
			'input-number--kann': (validationResult.fehlerart === ValidatorFehlerart.KANN),
			'input-number--hinweis': (validationResult.fehlerart === ValidatorFehlerart.HINWEIS),
			'input-number--disabled': disabled,
			'input-number--readonly': readonly,
			'input-number--statistics': statistics,
			'input-number--number': true,
			'input-number-component--headless': headless,
			'col-span-full': span === 'full',
			'col-span-2': span === '2',
		}">
		<label :for="id" />
		<div v-if="readonly" :class="{ 'input-number--control': !headless, 'input-number--headless': headless }">
			{{ data }}
		</div>
		<input v-else ref="input" :name="id"
			v-focus
			:class="[{ 'input-number--control': !headless, 'input-number--headless': headless }, 'appearance-none']"
			v-bind="{ ...$attrs }"
			type="number"
			inputmode="numeric"
			:value="data"
			:disabled
			:required
			:readonly
			:min
			:max
			:aria-labelledby="labelId"
			:placeholder="headless ? placeholder : ''"
			@input="onInput"
			@keyup.enter="onKeyEnter"
			@blur="onBlur">
		<span v-if="(placeholder && !headless) || numberLimitText || validationResult.hasFehler" :id="labelId" class="input-number--placeholder gap-1">
			<span v-if="statistics" class="cursor-pointer">
				<svws-ui-tooltip position="right">
					<span class="icon i-ri-bar-chart-2-line input-number--statistic-icon" />
					<template #content>Relevant für die Statistik</template>
				</svws-ui-tooltip>
			</span>
			<span>{{ placeholder }}</span>
			<span v-if="numberLimitText !== null" class="input-number--limittext">
				{{ numberLimitText }}
			</span>
			<span v-if="required" class="icon-xs i-ri-asterisk input-number--placeholder--required input-number--state-icon" aria-hidden="true" />
			<span v-if="required" class="sr-only">erforderlich</span>
			<span v-if="!valid(data)" class="icon i-ri-alert-line input-number--state-icon" />
			<span v-if="validationResult.hasFehler">
				<ui-validation-tooltip :validation-result :disabled />
			</span>
			<span v-if="statistics" class="cursor-pointer">
				<span class="icon i-ri-alert-fill input-number--state-icon" v-if="required && (data === null)" />
			</span>
			<span v-if="readonly" class="icon-xs i-ri-lock-line" />
		</span>

		<span v-if="data !== null && !hideStepper && !disabled && !readonly" class="svws-input-stepper">
			<button ref="btnMinus" role="button" @click="onInputNumber('down')" @blur="onBlur" :class="{'svws-disabled': String(min) === String(data)}"><span class="icon i-ri-subtract-line" /></button>
			<button ref="btnPlus" role="button" @click="onInputNumber('up')" @blur="onBlur" :class="{'svws-disabled': String(max) === String(data)}"><span class="icon i-ri-add-line" /></button>
		</span>
	</div>
</template>


<script setup lang="ts">

	import { ref, computed, watch, type ComputedRef, type Ref, useId } from "vue";
	import type { List } from "../../../../core/src/java/util/List";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";
	import type { ValidatorFehler } from '../../../../core/src/asd/validate/ValidatorFehler';
	import { ValidatorFehlerart } from '../../../../core/src/asd/validate/ValidatorFehlerart';
	import { ValidationResult } from "../../validation/ValidationResult";
	import { ValidatorNumberRequired } from "../../validation/common/ValidatorNumberRequired";
	import { ValidatorNumberMin } from "../../validation/common/ValidatorNumberMin";
	import { ValidatorNumberMax } from "../../validation/common/ValidatorNumberMax";

	defineOptions({
		inheritAttrs: false,
	});

	const input = ref<null | HTMLInputElement>(null);
	const btnPlus = ref<null | HTMLButtonElement>(null);
	const btnMinus = ref<null | HTMLButtonElement>(null);
	const id = useId();

	type SkippedDefaultValidators = { required?: boolean; min?: boolean; max?: boolean };

	const props = withDefaults(defineProps<{
		modelValue: number | null;
		placeholder?: string;
		statistics?: boolean;
		/* deprecated. Nutze prop "validation" */
		valid?: (value: number | null) => boolean;
		disabled?: boolean;
		required?: boolean;
		readonly?: boolean;
		headless?: boolean;
		focus?: boolean;
		hideStepper?: boolean;
		span?: 'full' | '2';
		min?: number | undefined;
		max?: number | undefined;
		validation?: () => List<ValidatorFehler>;
		skipDefaultValidation?: boolean | SkippedDefaultValidators;
	}>(), {
		placeholder: "",
		statistics: false,
		valid: () => true,
		disabled: false,
		required: false,
		readonly: false,
		headless: false,
		focus: false,
		hideStepper: false,
		span: undefined,
		min: undefined,
		max: undefined,
		validation: () => new ArrayList<ValidatorFehler>,
		skipDefaultValidation: false,
	});

	const emit = defineEmits<{
		"update:modelValue": [value: number | null];
		"change": [value: number | null];
		"blur": [value: number | null];
		"commit": [value: number | null];
	}>();

	const vFocus = {
		mounted: (el: HTMLInputElement) => {
			if (props.focus) {
				el.focus();
			}
		},
	};

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const data = ref<number | null>(props.modelValue);

	watch(() => props.modelValue, (value: number | null) => updateData(value), { immediate: false });

	const validationResult = computed(() => new ValidationResult(validierungFehler.value));

	const validatorRequired = computed<ValidatorNumberRequired | null>(() => {
		if (props.required && (!skipValidator('required'))) {
			return new ValidatorNumberRequired(() => props.modelValue);
		}
		return null;
	});

	const validatorMin = computed<ValidatorNumberMin | null>(() => {
		if ((props.min !== undefined) && (!skipValidator('min'))) {
			return new ValidatorNumberMin(() => props.modelValue, props.min);
		}
		return null;
	});

	const validatorMax = computed<ValidatorNumberMax | null>(() => {
		if ((props.max !== undefined) && (!skipValidator('max'))) {
			return new ValidatorNumberMax(() => props.modelValue, props.max);
		}
		return null;
	});

	const validierungFehler = computed<List<ValidatorFehler>>(() => {
		const fehler = new ArrayList<ValidatorFehler>();
		const defaultValidators = [validatorRequired.value, validatorMin.value, validatorMax.value];

		for (const validator of defaultValidators) {
			if (validator !== null) {
				validator.run();
				fehler.addAll(validator.getFehler());
			}
		}

		fehler.addAll(props.validation());
		return fehler;
	});

	/**
	 * Berechnet, ob der gegebene Default-Validator nicht ausgeführt werden soll
	 *
	 * @param defaultValidator   Name des Validators, der geprüft wird
	 */
	function skipValidator(defaultValidator: 'required' | 'min' | 'max'): boolean {
		if (typeof props.skipDefaultValidation === 'boolean') {
			return props.skipDefaultValidation;
		}
		return props.skipDefaultValidation[defaultValidator] ?? false;
	}

	function updateData(value: number | null) {
		if (data.value !== value) {
			data.value = value;
			emit("update:modelValue", data.value);
		}
	}

	function onInput(event: Event) {
		const strValue = (event.target as HTMLInputElement).value;
		const value = (strValue === "") ? null : Number(strValue);
		if (value !== data.value) {
			updateData(value);
		}
	}

	function onInputNumber(stepDirection: string) {
		if (input.value === null) {
			return;
		}
		if (stepDirection === 'up') {
			input.value.stepUp();
		} else if (stepDirection === 'down') {
			input.value.stepDown();
		}
		updateData(Number(input.value.value));
	}

	function onBlur(event: Event) {
		// prevent firing change/blur event, if the user only switches between input and button elements inside the SVWSUiInputNumber component itself
		if (event instanceof FocusEvent && ([input.value, btnPlus.value, btnMinus.value] as Array<HTMLElement>).includes(event.relatedTarget as HTMLElement)) {
			return;
		}
		emit("commit", data.value);
		if (props.modelValue !== data.value) {
			emit("change", data.value);
		}
		emit("blur", data.value);
	}

	function onKeyEnter(event: Event) {
		emit("commit", data.value);
		if (props.modelValue !== data.value) {
			emit("change", data.value);
		}
	}

	function reset() {
		data.value = props.modelValue;
	}

	const labelId = useId();

	/**
	 * Generiert den Text im Label, wenn es eine Ober-/Untergrenze für den Wert gibt
	 */
	const numberLimitText = computed(() => {
		if ((props.min === undefined) && (props.max === undefined)) {
			return null;
		}
		if ((props.min !== undefined) && (props.max !== undefined)) {
			const lower = Math.min(props.min, props.max);
			const upper = Math.max(props.min, props.max);
			return lower === upper ? `(${lower})` : `(zwischen ${lower} und ${upper})` ;
		}
		return (props.min === undefined) ? `(max. ${props.max})` : `(min. ${props.min})`;
	});

	const content = computed<number | null>(() => data.value);

	defineExpose<{
		content: ComputedRef<number | null>,
		input: Ref<HTMLInputElement | null>,
		reset: () => void;
	}>({ content, input, reset });

</script>
