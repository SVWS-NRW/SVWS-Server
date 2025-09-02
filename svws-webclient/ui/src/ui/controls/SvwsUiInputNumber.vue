<template>
	<div class="input-number-component"
		:class="{
			'input-number--filled': (data !== null) && (data !== undefined),
			'input-number--invalid': (isValid === false),
			'input-number--disabled': disabled,
			'input-number--readonly': readonly,
			'input-number--statistics': statistics,
			'input-number--number': true,
			'input-number-component--headless': headless,
			'col-span-full': span === 'full',
			'col-span-2': span === '2',
		}">
		<label :for="id" />
		<div v-if="readonly" :class="{ 'input-number--control': !headless, 'input-number--headless': headless, 'input-number--rounded': rounded, }">
			{{ data }}
		</div>
		<input v-else ref="input" :name="id"
			v-focus
			:class="[{ 'input-number--control': !headless, 'input-number--headless': headless, 'input-number--rounded': rounded }, 'appearance-none']"
			v-bind="{ ...$attrs }"
			type="number"
			inputmode="numeric"
			:value="data"
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
			<span v-if="required" class="icon-xs i-ri-asterisk input-number--placeholder--required input-number--state-icon" aria-hidden />
			<span v-if="required" class="sr-only">erforderlich</span>
			<span v-if="!isValid && !statistics && !required" class="icon i-ri-alert-line input-number--state-icon" />
			<span v-if="statistics" class="cursor-pointer">
				<span class="icon i-ri-alert-fill input-number--state-icon" v-if="required && (data === null)" />
			</span>
			<span v-if="readonly" class="icon-xs i-ri-lock-line" />
		</span>

		<span v-if="data !== null && !hideStepper && !disabled && !readonly" class="svws-input-stepper">
			<button ref="btnMinus" role="button" @click="onInputNumber('down')" @blur="onBlur" :class="{'svws-disabled': String($attrs?.min) === String(data)}"><span class="icon i-ri-subtract-line" /></button>
			<button ref="btnPlus" role="button" @click="onInputNumber('up')" @blur="onBlur" :class="{'svws-disabled': String($attrs?.max) === String(data)}"><span class="icon i-ri-add-line" /></button>
		</span>
	</div>
</template>


<script setup lang="ts">

	import { ref, computed, watch, type ComputedRef, type Ref, useId, useAttrs } from "vue";

	defineOptions({
		inheritAttrs: false,
	});

	const attrs = useAttrs();
	const input = ref<null | HTMLInputElement>(null);
	const btnPlus = ref<null | HTMLButtonElement>(null);
	const btnMinus = ref<null | HTMLButtonElement>(null);
	const id = useId();

	const props = withDefaults(defineProps<{
		modelValue: number | null;
		placeholder?: string;
		statistics?: boolean;
		valid?: (value: number | null) => boolean;
		disabled?: boolean;
		required?: boolean;
		readonly?: boolean;
		headless?: boolean;
		focus?: boolean;
		rounded?: boolean;
		hideStepper?: boolean;
		span?: 'full' | '2';
	}>(), {
		placeholder: "",
		statistics: false,
		valid: () => true,
		disabled: false,
		required: false,
		readonly: false,
		headless: false,
		focus: false,
		rounded: false,
		hideStepper: false,
		span: undefined,
	});

	const emit = defineEmits<{
		"update:modelValue": [value: number | null];
		"change": [value: number | null];
		"blur": [value: number | null];
	}>();

	const vFocus = {
		mounted: (el: HTMLInputElement) => {
			if (props.focus)
				el.focus();
		},
	};

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const data = ref<number | null>(props.modelValue);

	watch(() => props.modelValue, (value: number | null) => updateData(value), { immediate: false });

	const isValid = computed(() => {
		if (props.required && (data.value === null))
			return false;

		if ((data.value !== null)
			&& (((attrs.min !== undefined) && (attrs.min !== null) && (data.value < Number(attrs.min)))
				|| ((attrs.max !== undefined) && (attrs.max !== null) && (data.value > Number(attrs.max)))))
			return false;

		return props.valid(data.value);
	})

	function updateData(value: number | null) {
		if (data.value !== value) {
			data.value = value;
			emit("update:modelValue", data.value);
		}
	}

	function onInput(event: Event) {
		const strValue = (event.target as HTMLInputElement).value;
		const value = (strValue === "") ? null : Number(strValue);
		if (value !== data.value)
			updateData(value);
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
		// prevent firing change/blur event, if the user only switches between input and button elements inside the SVWSUiInputNumber component itself
		if (event instanceof FocusEvent && ([input.value, btnPlus.value, btnMinus.value] as Array<HTMLElement>).includes(event.relatedTarget as HTMLElement))
			return;

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

	const labelId = useId();

	const content = computed<number | null>(() => data.value);

	defineExpose<{
		content: ComputedRef<number | null>,
		input: Ref<HTMLInputElement | null>,
		reset: () => void;
	}>({ content, input, reset });

</script>
