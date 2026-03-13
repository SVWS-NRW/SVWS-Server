<template>
	<label :id="idComponent" class="textarea-input"
		:class="{
			'textarea-input--filled': data,
			'textarea-input--muss': ((validationResult.fehlerart === ValidatorFehlerart.MUSS) || !valid(data)),
			'textarea-input--kann': (validationResult.fehlerart === ValidatorFehlerart.KANN),
			'textarea-input--hinweis': (validationResult.fehlerart === ValidatorFehlerart.HINWEIS),
			'textarea-input--disabled': disabled,
			'textarea-input--statistics': statistics,
			'textarea-input--resize-none': resizeable === 'none',
			'textarea-input--resize-horizontal': resizeable === 'horizontal',
			'textarea-input--resize-vertical': resizeable === 'vertical',
			'textarea-input--resize-both': resizeable === 'both',
			'textarea-input--headless': headless,
			'col-span-full': span === 'full',
			'grow': span === 'grow'
		}">
		<textarea ref="textarea" v-model="input" @input="onInput" @blur="onBlur" class="textarea-input--control" :disabled :required :rows v-bind="$attrs"
			:class="{ 'contentFocusField': isContentFocusField }" />
		<span :id="idPlaceholder" v-if="placeholder.length > 0" class="textarea-input--placeholder">
			<span :id="idStatistics" v-if="statistics" class="cursor-pointer">
				<svws-ui-tooltip position="right">
					<span class="inline-flex items-center">
						<span class="icon i-ri-bar-chart-2-line textarea-input--statistic-icon" />
					</span>
					<template #content>
						Relevant für die Statistik
					</template>
				</svws-ui-tooltip>
			</span>
			<span>{{ placeholder }}</span>
			<span v-if="!headless && ((maxLen !== undefined) && (maxLen > 0)) && (data !== null)" class="inline-flex gap-1">
				{{ `(${(data.toLocaleString().length > 0) ? data.toLocaleString().length + '/' : 'maximal '}${maxLen} Zeichen)` }}
			</span>
			<span v-if="required" class="icon-xs i-ri-asterisk textarea-input--placeholder--required textarea-input--state-icon" aria-hidden="true" />
			<span v-if="required" class="sr-only">erforderlich</span>
			<ui-validation-tooltip v-if="!validierungFehler.isEmpty()" :disabled :validation-result />

		</span>
	</label>
</template>

<script setup lang="ts">

	import { ref, computed, watch, useId } from 'vue';
	import { useTextareaAutosize } from '@vueuse/core';
	import type { List } from "../../../../core/src/java/util/List";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";
	import { ValidationResult } from "../../validation/ValidationResult";
	import type { ValidatorFehler } from "../../../../core/src/asd/validate/ValidatorFehler";
	import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
	import { ValidatorStringLength } from "../../validation/common/ValidatorStringLength";
	import { ValidatorInputRequired } from "../../validation/common/ValidatorInputRequired";


	type ResizableOption = "both" | "horizontal" | "vertical" | "none";

	const props = withDefaults(defineProps<{
		modelValue?: string | null;
		placeholder?: string;
		/* deprecated. Nutze prop "validation" */
		valid?: (value: string | null) => boolean;
		statistics?: boolean;
		required?: boolean;
		disabled?: boolean;
		resizeable?: ResizableOption;
		autoresize?: boolean;
		cols?: number;
		rows?: number;
		maxLen?: number;
		span?: 'full' | 'grow';
		headless?: boolean;
		isContentFocusField?: boolean;
		validation?: () => List<ValidatorFehler>;
	}>(), {
		modelValue: "",
		placeholder: "",
		valid: () => true,
		statistics: false,
		required: false,
		disabled: false,
		resizeable: "vertical",
		autoresize: false,
		cols: 80,
		rows: 3,
		maxLen: undefined,
		span: undefined,
		headless: false,
		isContentFocusField: false,
		validation: undefined,
	});

	const emit = defineEmits<{
		"update:modelValue": [value: string | null];
		"change": [value: string | null];
		"blur": [value: string | null];
		"input": [value: string];
	}>();

	defineOptions({
		inheritAttrs: false,
	});

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const data = ref<string | null>(props.modelValue);

	const idComponent = useId();
	const idPlaceholder = useId();
	const idStatistics = useId();
	const dataOrEmpty = computed<string>({
		get: () => data.value === null ? '' : data.value,
		set: (value) => data.value = (value === '') ? null : value,
	});

	const { textarea, input } = useTextareaAutosize({ input: dataOrEmpty, styleProp: 'minHeight' });

	watch(() => props.modelValue, (value: string | null) => updateData(value), { immediate: false });

	const validationResult = computed(() => new ValidationResult(validierungFehler.value));

	const validatorRequired = computed<ValidatorInputRequired<string> | null>(() => {
		if (props.required && (props.validation === undefined)) {
			return new ValidatorInputRequired(() => props.modelValue);
		}
		return null;
	});

	const validatorLength = computed<ValidatorStringLength | null>(() => {
		if ((props.maxLen !== undefined) && (props.validation === undefined)) {
			return new ValidatorStringLength(() => data.value, null, props.maxLen);
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
		const defaultValidators = [validatorRequired.value, validatorLength.value];
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
		emit("input", value);
		if (value !== data.value) {
			updateData(value);
		}
	}

	function onBlur() {
		if (props.modelValue !== data.value) {
			emit("change", data.value);
		}
		emit("blur", data.value);
	}

	defineExpose({ content: data });

</script>
