<template>
	<label :id="idComponent" class="textarea-input"
		:class="{
			'textarea-input--filled': data,
			'textarea-input--invalid': !isValid,
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
		<textarea ref="textarea" v-model="dataOrEmpty" @input="onInput" @blur="onBlur" class="textarea-input--control" :disabled :required :rows v-bind="{ ...$attrs }" :class="{ 'contentFocusField': isContentFocusField }" />
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
			<span v-if="(maxLen > 0) && (data !== null)" class="inline-flex gap-1" :class="maxLenValid ? 'opacity-50' : 'text-ui-danger'">
				{{ `(${(data.toLocaleString().length > 0) ? data.toLocaleString().length + '/' : 'maximal '}${maxLen} Zeichen)` }}
			</span>
			<span v-if="required" class="icon-xs i-ri-asterisk textarea-input--placeholder--required textarea-input--state-icon" aria-hidden />
			<span v-if="required" class="sr-only">erforderlich</span>
			<span class="icon i-ri-alert-line textarea-input--state-icon" v-if="(isValid === false && !required)" />
			<span :id="idStatistics" v-if="statistics" class="cursor-pointer">
				<span class="icon i-ri-alert-fill textarea-input--state-icon" v-if="required && (data === '') || (data === null)" />
			</span>

		</span>
	</label>
</template>


<script setup lang="ts">

	import { ref, computed, watch, useId } from 'vue';

	type ResizableOption = "both" | "horizontal" | "vertical" | "none";

	const props = withDefaults(defineProps<{
		modelValue?: string | null;
		placeholder?: string;
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
	})

	const emit = defineEmits<{
		"update:modelValue": [value: string | null];
		"change": [value: string | null];
		"blur": [value: string | null];
		"input": [value: string];
	}>();

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const data = ref<string | null>(props.modelValue);

	const idComponent = useId();
	const idPlaceholder = useId();
	const idStatistics = useId();
	const dataOrEmpty = computed<string>({
		get: () => data.value === null ? '' : data.value,
		set: (value) => data.value = (value === '') ? null : value,
	});

	const textarea = ref<HTMLTextAreaElement | null>(null);

	watch(() => props.modelValue, (value: string | null) => updateData(value), { immediate: false });

	const isValid = computed(() => {
		let tmpIsValid = true;
		if (props.required && ((data.value === null) || (data.value === '')))
			return false;
		if (!maxLenValid.value)
			tmpIsValid = false;
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

	const maxLenValid = computed(() => {
		if ((props.maxLen === undefined) || (data.value === null))
			return true;
		return data.value.toLocaleString().length <= props.maxLen;
	})

	watch(data, () => {
		if (textarea.value !== null) {
			textarea.value.style.height = 'auto';
			textarea.value.style.height = `${textarea.value.scrollHeight+12}px`;
		}
	}, { immediate: true });

	function onInput(event: Event) {
		const value = (event.target as HTMLInputElement).value;
		emit("input", value);
		if (value !== data.value)
			updateData(value);
	}

	function onBlur() {
		if (props.modelValue !== data.value)
			emit("change", data.value);
		emit("blur", data.value);
	}

	defineExpose({ content: data });

</script>
