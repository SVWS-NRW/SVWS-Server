<template>
	<div class="input-number-component"
		:class="{
			'input-number--filled': (visualData !== null) && (visualData !== undefined),
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
			{{ visualData }}
		</div>
		<input v-else ref="input" :name="id" lang="de"
			v-focus
			:class="[{ 'input-number--control': !headless, 'input-number--headless': headless }, 'appearance-none']"
			v-bind="{ ...$attrs }"
			type="text"
			inputmode="decimal"
			v-model="visualData"
			:disabled
			:required
			:readonly
			:aria-labelledby="labelId"
			:placeholder="headless ? placeholder : ''"
			@input="onInput"
			@keyup.enter="onKeyEnter"
			@blur="onBlur">
		<span v-if="placeholder && !headless" :id="labelId" class="input-number--placeholder gap-1">
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
			<span v-if="validationResult.hasFehler">
				<ui-validation-tooltip :validation-result :disabled />
			</span>
			<span v-if="readonly" class="icon-xs i-ri-lock-line" />
		</span>

		<span v-if="visualData !== null && (steps !== false) && !disabled && !readonly" class="svws-input-stepper">
			<button ref="btnMinus" @click="onStepperClick('down')" @blur="onBlur" :class="{'svws-disabled': disableMinusButton}"><span class="icon i-ri-subtract-line" /></button>
			<button ref="btnPlus" @click="onStepperClick('up')" @blur="onBlur" :class="{'svws-disabled': disablePlusButton}"><span class="icon i-ri-add-line" /></button>
		</span>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed, watch, type ComputedRef, type Ref, useId, onMounted } from "vue";
	import type { List } from "../../../../core/src/java/util/List";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";
	import type { ValidatorFehler } from "../../../../core/src/asd/validate/ValidatorFehler";
	import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
	import { ValidationResult } from "../../validation/ValidationResult";
	import { ValidatorInputRequired } from "../../validation/common/ValidatorInputRequired";
	import { ValidatorNumberRange } from "../../validation/common/ValidatorNumberRange";
	import { DeveloperNotificationException } from "../../../../core/src/core/exceptions/DeveloperNotificationException";

	defineOptions({
		inheritAttrs: false,
	});

	const input = ref<null | HTMLInputElement>(null);
	const btnPlus = ref<null | HTMLButtonElement>(null);
	const btnMinus = ref<null | HTMLButtonElement>(null);
	const id = useId();

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
		steps?: false | number;
		decimalPlaces?: 0 | 1 | 2 | 3 | 4;
		span?: 'full' | '2';
		min?: number | undefined;
		max?: number | undefined;
		validation?: (() => List<ValidatorFehler>) | undefined;
	}>(), {
		placeholder: "",
		statistics: false,
		valid: () => true,
		disabled: false,
		required: false,
		readonly: false,
		headless: false,
		focus: false,
		steps: 1,
		decimalPlaces: 0,
		span: undefined,
		min: undefined,
		max: undefined,
		validation: undefined,
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

	onMounted(() => {
		if (typeof props.steps === "number") {
			warnIfStepsNotCompatible(props.steps);
		}
		syncVisualData();
	});

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const data = ref<number | null>(props.modelValue);

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const visualData = ref<string | null>(props.modelValue === null ? null : String(props.modelValue).replaceAll(".", ","));

	const labelId = useId();

	watch(() => props.modelValue, (value: number | null) => {
		if (value !== data.value) {
			updateData(value);
			syncVisualData();
		}
	}, { immediate: true });

	const disablePlusButton = computed(() => {
		if ((data.value === null) || (props.max === undefined)) {
			return false;
		}
		return data.value >= props.max;
	});

	const disableMinusButton = computed(() => {
		if ((data.value === null) || (props.min === undefined)) {
			return false;
		}
		return data.value <= props.min;
	});

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

	/**
	 * Validatoren
	 */

	const validationResult = computed(() => new ValidationResult(validierungFehler.value));

	const validatorRequired = computed<ValidatorInputRequired<number> | null>(() => {
		if (props.required && (props.validation === undefined)) {
			return new ValidatorInputRequired(() => props.modelValue);
		}
		return null;
	});

	const validatorRange = computed<ValidatorNumberRange | null>(() => {
		if (((props.min !== undefined) || (props.max !== undefined)) && (props.validation === undefined)) {
			return new ValidatorNumberRange(() => props.modelValue, props.min, props.max);
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
		const defaultValidators = [validatorRequired.value, validatorRange.value];
		for (const validator of defaultValidators) {
			if (validator !== null) {
				validator.run();
				fehler.addAll(validator.getFehler());
			}
		}
		return fehler;
	}

	/**
	 * Datenverarbeitung
	 */

	function updateData(value: string | number | null) {
		const numberValue = getNumberFromInput(value);

		if (numberValue !== null && Number.isNaN(numberValue)) {
			return;
		}

		if (data.value !== numberValue) {
			data.value = numberValue;
			emit("update:modelValue", numberValue);
		}
	}

	function onInput() {
		if ((input.value === null) || (visualData.value === null)) {
			return;
		}

		const cursorPositionBeforeParse = input.value.selectionStart ?? 0;
		const inputLengthBeforeParse = input.value.value.length;

		visualData.value = parseInput(visualData.value);
		input.value.value = visualData.value ?? ""; // Synchronisation mit der Anzeige im Input
		setCursorPosition(inputLengthBeforeParse, cursorPositionBeforeParse);

		const value = (visualData.value === null) ? null : Number(visualData.value.replaceAll(",", "."));
		updateData(value);
	}

	function syncVisualData(): void {
		if (data.value === null) {
			visualData.value = null;
			return;
		}

		const factor = Math.pow(10, props.decimalPlaces);
		const rounded = Math.round(data.value * factor) / factor;
		visualData.value = String(rounded).replaceAll(".", ",");
	}

	function onStepperClick(stepDirection: string) {
		if ((props.steps === false) || (data.value === null) || (visualData.value === null)) {
			return;
		}
		warnIfStepsNotCompatible(props.steps);

		// Nutze, wenn möglich den Wert aus visualData, da es sein kann, dass dieser nur ein gerundeter Wert von data ist.
		// In dem Fall würde es so aussehen, als würde die falsche Zahl addiert/subtrahiert werden
		const visualNumber = Number(visualData.value.replaceAll(",", "."));
		let newValue = (Number.isNaN(visualNumber)) ? data.value : visualNumber;

		if (stepDirection === 'up') {
			newValue = Math.round((newValue + props.steps) * 1e10) / 1e10;
		} else if (stepDirection === 'down') {
			newValue = Math.round((newValue - props.steps) * 1e10) / 1e10;
		}
		updateData(newValue);
		syncVisualData();
	}

	function onBlur(event: Event) {
		// prevent firing change/blur event, if the user only switches between input and button elements inside the SVWSUiInputNumber component itself
		if (event instanceof FocusEvent && ([input.value, btnPlus.value, btnMinus.value] as Array<HTMLElement>).includes(event.relatedTarget as HTMLElement)) {
			return;
		}
		syncVisualData();
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
		visualData.value = String(props.modelValue);
	}

	/**
	 * Input Parsing
	 */

	function parseInput(input: string): string | null {
		// Erlaubte Zeichen sind Zahlen, Minus und bei Dezimalzahlen Komma und Punkt
		const allowed = (props.decimalPlaces === 0) ? /[^0-9-]/g : /[^0-9\-,.]/g;

		let parsedString = input
			.replaceAll(allowed, "") // Unerlaubte Zeichen entfernen
			.replaceAll(/(?!^)-/g, ""); // Minus nur am Anfang erlauben
		parsedString = removeExtraSeparators(parsedString);
		parsedString = removeTooManyDecimals(parsedString);

		return (parsedString === "") ? null : parsedString;
	}

	function removeExtraSeparators(value: string): string {
		const match = /[,.]/.exec(value);
		const first = (match === null) ? -1 : match.index;
		if (first === -1) {
			return value;
		}
		const prefix = value.slice(0, first + 1);
		const suffix = value.slice(first + 1)
			.replaceAll(",", "")
			.replaceAll(".", "");
		return prefix + suffix;
	}

	function removeTooManyDecimals(value: string): string {
		const separator = getSeparator(value);
		if (separator === null) {
			return value;
		}
		const parts = value.split(separator);
		if (hasTooManyDecimals(parts)) {
			return parts[0] + separator + (parts[1].slice(0, props.decimalPlaces));
		}
		return value;
	}

	/**
	 * Hilfsfunktionen
	 */

	function getSeparator(value: string): string | null {
		if (value.includes(("."))) {
			return ".";
		}
		if (value.includes(",")) {
			return ",";
		}
		return null;
	}

	function getNumberFromInput(value: string | number | null): number | null {
		if (value === "") {
			return null;
		}

		if (typeof value === "string") {
			return Number(value.replaceAll(",", ".").trim());
		}

		return value;
	}

	function setCursorPosition(inputLengthBeforeParse: any, cursorPosition: any) {
		if ((visualData.value !== null) && (input.value !== null)) {
			const inputLengthAfterParse = visualData.value.length;
			const diff = inputLengthBeforeParse - inputLengthAfterParse;

			// zurücksetzen der Cursorposition, da diese beim Neusetzen des Inputs nach dem Parsen automatisch verschoben wird
			input.value.setSelectionRange(cursorPosition - diff, cursorPosition - diff);
		}
	}

	function hasTooManyDecimals(parts: string[]) {
		const hasDecimals = parts.length === 2;
		return hasDecimals && (parts[1].length > props.decimalPlaces);
	}

	function warnIfStepsNotCompatible(steps: number) {
		const partsOfSteps = String(steps).split(".");
		if ((partsOfSteps.length === 2) && (partsOfSteps[1].length > props.decimalPlaces)) {
			throw new DeveloperNotificationException(
				"Für das Input mit dem Label '" + props.placeholder + "' wurde mit der prop 'steps = " + props.steps + "' eine Schrittweite " +
					"mit mehr Nachkommastellen definiert, als die prop 'decimalPlaces = " + props.decimalPlaces + "' zulässt."
			);
		}
	}

	/**
	 * Expose
	 */

	const content = computed<number | null>(() => data.value);

	defineExpose<{
		content: ComputedRef<number | null>,
		input: Ref<HTMLInputElement | null>,
		reset: () => void;
	}>({ content, input, reset });

</script>
