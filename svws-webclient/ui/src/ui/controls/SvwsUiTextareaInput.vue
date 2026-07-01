<template>
	<label :id="idComponent" class="textarea-input"
		:class="{
			'textarea-input--filled': !isEmpty,
			'textarea-input--muss': ((validationResult.fehlerart === ValidatorFehlerart.MUSS) || !valid(localValue)),
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
		<textarea class="textarea-input--control"
			:class="{ 'contentFocusField': isContentFocusField, 'textarea-input--auto-grow': autoresize }"
			ref="textarea"
			:value="localValue ?? ''"
			:rows
			:cols
			:required
			:disabled
			@input="onInput"
			@focus="onFocus"
			@blur="onBlur"
			v-bind="$attrs" />
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
			<span v-if="!headless && localValue !== null && (maxLen !== undefined) && (maxLen > 0)" class="inline-flex gap-1">
				{{ `(${localValue.length > 0 ? localValue.length + '/' : 'maximal '}${maxLen} Zeichen)` }}
			</span>
			<span v-if="required" class="icon-xs i-ri-asterisk textarea-input--placeholder--required textarea-input--state-icon" aria-hidden="true" />
			<span v-if="required" class="sr-only">erforderlich</span>
			<ui-validation-tooltip v-if="!validierungFehler.isEmpty()" :disabled :validation-result />
		</span>
	</label>
</template>

<script setup lang="ts">
	import { computed, useId, useTemplateRef, onMounted, nextTick, onUnmounted, watch, ref } from 'vue';
	import type { List } from "../../../../core/src/java/util/List";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";
	import type { ValidatorFehler } from "../../../../core/src/asd/validate/ValidatorFehler";
	import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
	import { ValidationResult } from "../../validation/ValidationResult";
	import { ValidatorStringLength } from "../../validation/common/ValidatorStringLength";
	import { ValidatorInputRequired } from "../../validation/common/ValidatorInputRequired";

	type ResizableOption = "both" | "horizontal" | "vertical" | "none";

	const props = withDefaults(defineProps<{
		modelValue?: string | null;
		placeholder?: string;
		/** @deprecated Nutze prop "validation" */
		valid?: (value: string | null) => boolean;
		statistics?: boolean;
		required?: boolean;
		disabled?: boolean;
		resizeable?: ResizableOption;
		/* Automatic resize of the textarea height */
		autoresize?: boolean;
		cols?: number;
		rows?: number;
		maxLen?: number;
		span?: 'full' | 'grow';
		headless?: boolean;
		isContentFocusField?: boolean;
		validation?: () => List<ValidatorFehler>;
	}>(), {
		modelValue: null,
		placeholder: "",
		valid: () => true,
		statistics: false,
		required: false,
		disabled: false,
		resizeable: "vertical",
		autoresize: true,
		cols: 80,
		rows: 3,
		maxLen: undefined,
		span: undefined,
		headless: false,
		isContentFocusField: false,
		validation: undefined,
	});

	const emit = defineEmits<{
		/** Emitted on every input – drives v-model. */
		"update:modelValue": [value: string | null];
		/** Emitted on input */
		"input": [value: string | null];
		/** Emitted on blur when the value changed */
		"change": [value: string | null];
		/** Emitted on every blur. */
		"blur": [value: string | null];
	}>();

	defineOptions({ inheritAttrs: false });

	const textareaRef = useTemplateRef('textarea');

	const idComponent = useId();
	const idPlaceholder = useId();
	const idStatistics = useId();

	// Tracks the value at the time of focus to implement correct "change" semantics
	let valueOnFocus: string | null = null;

	// Store value as localValue - to enable controlled (v-model) as well as uncontrolles (modelValue only) usage
	const localValue = ref<string | null>(null);

	const isEmpty = computed<boolean>(() => localValue.value === null);

	const validationResult = computed(() => new ValidationResult(validierungFehler.value));

	const validatorRequired = computed<ValidatorInputRequired<string> | null>(() =>
		props.required && props.validation === undefined
			? new ValidatorInputRequired(() => localValue.value)
			: null
	);

	const validatorLength = computed<ValidatorStringLength | null>(() =>
		props.maxLen !== undefined && props.validation === undefined
			? new ValidatorStringLength(() => localValue.value, null, props.maxLen)
			: null
	);

	const validierungFehler = computed<List<ValidatorFehler>>(() => {
		if (props.validation !== undefined) {
			return props.validation();
		}
		const fehler = new ArrayList<ValidatorFehler>();
		const defaultValidators = [validatorRequired.value, validatorLength.value];
		for (const validator of defaultValidators) {
			if (validator !== null) {
				validator.run();
				fehler.addAll(validator.getFehler());
			}
		}
		return fehler;
	});

	function normalize(value: string): string | null {
		return value === '' ? null : value;
	}

	function onFocus(event: Event) {
		valueOnFocus = normalize((event.target as HTMLTextAreaElement).value);
	}

	async function onInput(event: Event) {
		const value = normalize((event.target as HTMLTextAreaElement).value);

		// Immer lokal setzen – funktioniert auch ohne Two-Way-Binding
		localValue.value = value;

		// Weiterhin emitten – damit v-model funktioniert (controlled)
		emit("update:modelValue", value);
		emit("input", value);
	}

	function onBlur(event: Event) {
		const value = normalize((event.target as HTMLTextAreaElement).value);
		emit("blur", value);

		if (value !== valueOnFocus) {
			emit("change", value);
		}
	}

	function onResize() {
		if (textareaRef.value === null) {
			return;
		}
		textareaRef.value.style.height = "auto";
		textareaRef.value.style.height = `${textareaRef.value.scrollHeight}px`;
	}

	onMounted(async () => {
		// set inital localValue
		localValue.value = (props.modelValue !== null) ? normalize(props.modelValue) : null;

		if (!props.autoresize) {
			return;
		}
		window.addEventListener("resize", onResize);

		await nextTick();
		requestAnimationFrame(onResize);
	});

	onUnmounted(() => {
		if (props.autoresize) {
			window.removeEventListener("resize", onResize);
		}
	});

	// Sync modelValue changes to localValue
	watch(
		() => props.modelValue,
		(v) => {
			localValue.value = v ?? null;
		}
	);

	// Trigger onResize when relevant props or localValue changes
	watch(
		() => [localValue.value, props.cols, props.rows, props.disabled, props.autoresize],
		async () => {
			if (!props.autoresize) {
				return;
			}
			await nextTick();
			requestAnimationFrame(onResize);
		}
	);

	defineExpose({ content: computed(() => localValue.value), localValue });

</script>
