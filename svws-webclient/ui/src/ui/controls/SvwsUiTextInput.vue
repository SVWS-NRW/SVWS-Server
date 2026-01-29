<template>
	<label class="text-input-component"
		:class="{
			'text-input--filled': (`${data}`.length > 0 && data !== null) || (type === 'date') || (type === 'datetime-local'),
			'text-input--invalid': !validierungFehler.isEmpty(),
			'text-input--statistic-muss': !validierungFehler.isEmpty() && (validierungFehlerart === ValidatorFehlerart.MUSS),
			'text-input--statistic-kann': !validierungFehler.isEmpty() && (validierungFehlerart === ValidatorFehlerart.KANN),
			'text-input--statistic-hinweis': !validierungFehler.isEmpty() && (validierungFehlerart === ValidatorFehlerart.HINWEIS),
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
		<div v-if="readonly && !isSelectInput" :class="{ 'text-input--control': !headless, 'text-input--headless': headless, 'text-input--rounded': rounded, 'text-input--prefix': url, }">
			{{ data }}
		</div>
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
			<span v-if="required" class="icon-xs i-ri-asterisk text-input--placeholder--required text-input--state-icon" aria-hidden />
			<span v-if="required" class="sr-only">erforderlich</span>
			<span class="cursor-pointer inline-block -my-1">
				<ui-validation-tooltip v-if="!validierungFehler.isEmpty()" :validation-result />
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

	import { ref, computed, watch, onBeforeMount, onMounted, onBeforeUnmount, useId, type InputTypeHTMLAttribute } from "vue";
	import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
	import type { List } from "../../../../core/src/java/util/List";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";
	import { ValidatorFehler } from "../../../../core/src/asd/validate/ValidatorFehler";
	import { ValidatorStringNotEmpty } from "../../validation/ValidatorStringNotEmpty";
	import { ValidatorStringLength } from "../../validation/ValidatorStringLength";
	import { ValidatorEmail } from "../../validation/ValidatorEmail";
	import { ValidationResult } from "../../validation/ValidationResult";

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
		skipDefaultValidation?: boolean;
	}>(), {
		type: "text",
		minDate: undefined,
		maxDate: undefined,
		modelValue: null,
		modelModifiers: () => ({ trim: false }),
		placeholder: "",
		statistics: false,
		valid: (value: string | null) => true,
		validation: (): List<ValidatorFehler> => new ArrayList<ValidatorFehler>(),
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
		skipDefaultValidation: false,
	});

	const emit = defineEmits<{
		"update:modelValue": [value: string | null];
		"change": [value: string | null];
		"blur": [value: string | null];
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

	const validatorRequired = computed<ValidatorStringNotEmpty | null>(() => {
		if (props.required && !props.skipDefaultValidation) {
			return new ValidatorStringNotEmpty(() => data.value);
		}
		return null;
	});

	const validatorLength = computed<ValidatorStringLength | null>(() => {
		if (props.skipDefaultValidation) {
			return null;
		}
		return new ValidatorStringLength(() => data.value, props.maxLen, props.minLen);
	});

	const validatorEmail = computed<ValidatorEmail | null>(() => {
		if (props.type === "email" && !props.skipDefaultValidation) {
			return new ValidatorEmail(() => data.value, props.maxLen ?? 255);
		}
		return null;
	});

	const validatorDummy = new ValidatorStringNotEmpty(() => "dummy");

	const validierungFehler = computed<List<ValidatorFehler>>(() => {
		const result = new ArrayList<ValidatorFehler>();

		// Validierung, wenn required gesetz ist
		if (validatorRequired.value !== null) {
			validatorRequired.value.run();
			result.addAll(validatorRequired.value.getFehler());
		}

		// Validierung der Textlänge anhand der bei den props gesetzten Werte
		if (validatorLength.value !== null) {
			validatorLength.value.run();
			result.addAll(validatorLength.value.getFehler());
		}

		// Validiere eine Email-Adresse, wenn dies der Eingabe-Typ ist
		if (validatorEmail.value !== null) {
			validatorEmail.value.run();
			result.addAll(validatorEmail.value.getFehler());
		}

		// Validiere übergangsweise auf dem alten Prop `valid`, TODO entfernen inklusive Prop
		if (!props.valid(data.value) && !props.skipDefaultValidation) {
			const fehler = new ValidatorFehler(validatorDummy, 0, "Die Eingabe ist ungültig oder fehlerhaft");
			result.add(fehler);
		}

		// Validierung mit einer weiteren Validierung über die validate-Methode bei den props
		result.addAll(props.validation());
		return result;
	});

	const validierungFehlerart = computed<ValidatorFehlerart>(() => {
		let result = ValidatorFehlerart.UNGENUTZT;
		for (const fehler of validierungFehler.value) {
			const art = fehler.getFehlerart();
			if (art.ordinal() < result.ordinal()) {
				result = art;
			}
		}
		return result;
	});

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

	function onBlur(event: Event) {
		if (props.modelValue !== data.value) {
			emit("change", data.value);
		}
		emit("blur", data.value);
	}

	function onKeyEnter(event: Event) {
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
