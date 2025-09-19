<template>
	<label class="text-input-component"
		:class="{
			'text-input--filled': (`${data}`.length > 0 && data !== null) || type === 'date',
			'text-input--invalid': (isValid === false),
			'text-input--statistic-muss': (((validator !== undefined) && (!validator().getFehler().isEmpty()) && (validator().getFehlerart() === ValidatorFehlerart.MUSS)) || ((isValid === false) && (fehlerart === ValidatorFehlerart.MUSS))),
			'text-input--statistic-kann': (((validator !== undefined) && (!validator().getFehler().isEmpty()) && (validator().getFehlerart() === ValidatorFehlerart.KANN)) || ((isValid === false) && (fehlerart === ValidatorFehlerart.KANN))),
			'text-input--statistic-hinweis': (((validator !== undefined) && (!validator().getFehler().isEmpty()) && (validator().getFehlerart() === ValidatorFehlerart.HINWEIS)) || ((isValid === false) && (fehlerart === ValidatorFehlerart.HINWEIS))),
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
		<span v-if="url" class="pointer-events-none absolute left-0 pl-3 opacity-60 top-[0.32rem]">https://</span>
		<span class="icon i-ri-search-line text-input--search-icon" v-if="type === 'search'" />
		<div v-if="readonly && !isSelectInput" :class="{ 'text-input--control': !headless, 'text-input--headless': headless, 'text-input--rounded': rounded, 'text-input--prefix': url, }">
			{{ data }}
		</div>
		<input v-else ref="input"
			v-focus
			:class="{ 'text-input--control': !headless, 'text-input--headless': headless, 'text-input--rounded': rounded, 'text-input--prefix': url, }"
			v-bind="{ ...$attrs }"
			:type="type"
			:min="minDate"
			:max="maxDate"
			:value="data"
			:disabled
			:required
			:readonly
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
			<span v-if="(maxLen !== undefined) || (minLen !== undefined)" class="inline-flex gap-1" :class="{'text-ui-danger': !maxLenValid || !minLenValid, 'opacity-50': maxLenValid && minLenValid}">
				{{ (maxLen !== undefined) && (minLen === undefined) ? ` (max. ${maxLen} Zeichen)` : '' }}
				{{ (minLen !== undefined) && (maxLen === undefined) ? ` (mind. ${minLen} Zeichen)` : '' }}
				{{ (minLen !== undefined) && (maxLen !== undefined) && (minLen !== maxLen) ? ` (zwischen ${minLen} und ${maxLen} Zeichen)` : '' }}
				{{ (minLen !== undefined) && (maxLen !== undefined) && (minLen === maxLen) ? ` (genau ${maxLen} Zeichen)` : '' }}
			</span>
			<span v-if="required" class="icon-xs i-ri-asterisk text-input--placeholder--required text-input--state-icon" aria-hidden />
			<span v-if="required" class="sr-only">erforderlich</span>
			<span v-if="(isValid === false && !required)" class="icon-sm i-ri-alert-line text-input--muss-icon" />
			<span v-if="statistics" class="cursor-pointer inline-block -my-1">
				<svws-ui-tooltip position="right">
					<span class="inline-flex items-center -mb-2 mt-0.5 pointer-events-auto">
						<template v-if="(validator === undefined) || (validator().getFehlerart() === ValidatorFehlerart.UNGENUTZT)">
							<span class="icon i-ri-alert-fill  text-input--state-icon" v-if="required && ((data === '') || (data === null) || (data === undefined))" />
						</template>
						<template v-else-if="!validator().getFehler().isEmpty()">
							<span class="icon i-ri-alert-fill  text-input--state-icon" v-if="validator().getFehlerart() === ValidatorFehlerart.MUSS" />
							<span class="icon i-ri-error-warning-fill  text-input--state-icon" v-if="validator().getFehlerart() === ValidatorFehlerart.KANN" />
							<span class="icon i-ri-question-fill  text-input--state-icon" v-if="validator().getFehlerart() === ValidatorFehlerart.HINWEIS" />
						</template>
					</span>
					<template #content>
						<template v-if="(validator !== undefined) && (!validator().getFehler().isEmpty()) && (validator().getFehlerart() !== ValidatorFehlerart.UNGENUTZT)">
							<div class="text-headline-sm text-center pt-1"> Relevant für die Statistik </div>
							<div v-for="fehler in validator().getFehler()" :key="fehler.getFehlermeldung() ?? '--'" class="pt-2 pb-2">
								<div class="rounded-sm pl-2" :class="{
									'bg-ui-danger': (fehler.getFehlerart() === ValidatorFehlerart.MUSS),
									'bg-ui-caution': (fehler.getFehlerart() === ValidatorFehlerart.KANN),
									'bg-ui-warning': (fehler.getFehlerart() === ValidatorFehlerart.HINWEIS)}">
									{{ fehler.getFehlerart() }} - Fehlercode: {{ fehler.getFehlercode() }}
								</div>
								<div class="pl-2"> {{ fehler.getFehlermeldung() }} </div>
							</div>
						</template>
						<template v-else>
							<div class="text-headline-sm text-center"> Relevant für die Statistik </div>
						</template>
					</template>
				</svws-ui-tooltip>
			</span>
			<span v-if="readonly && !isSelectInput" class="icon-xs i-ri-lock-line" />
		</span>
		<span v-if="removable && (type === 'date') && (!readonly)" @keydown.enter="updateData('')" @click.stop="updateData('')" class="svws-icon--remove icon i-ri-close-line" tabindex="0" />
		<span v-if="(type === 'date') && !firefox()" class="svws-icon icon i-ri-calendar-2-line" />
		<span v-if="type === 'email'" class="svws-icon icon i-ri-at-line" />
		<span v-if="type === 'tel'" class="svws-icon icon i-ri-phone-line" />
	</label>
</template>


<script setup lang="ts" generic="V extends Validator">

	import { ref, computed, watch, onBeforeMount, onMounted, onBeforeUnmount, useId } from "vue";
	import type { Validator } from "../../../../core/src/asd/validate/Validator";
	import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";

	defineOptions({
		inheritAttrs: false,
	});

	function firefox() {
		return window.navigator.userAgent.includes('Firefox/');
	}
	const input = ref<null | HTMLInputElement>(null);

	const props = withDefaults(defineProps<{
		type?: "text" | "date" | "email" | "search" | "tel" | "password";
		minDate?: string;
		maxDate?: string;
		modelValue?: string | null;
		modelModifiers?: { trim: boolean };
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
		/* Validatoren */
		fehlerart?: ValidatorFehlerart;
	}>(), {
		type: "text",
		minDate: undefined,
		maxDate: undefined,
		modelValue: null,
		modelModifiers: () => ({ trim: false }),
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
		/* Validatoren */
		fehlerart: () => ValidatorFehlerart.UNGENUTZT,
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
		},
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

	defineExpose({ content, input, reset, doFocus });

</script>
