<template>
	<label class="textarea-input"
		:class="{
			'textarea-input--filled': data !== null,
			'textarea-input--invalid': (isValid === false),
			'textarea-input--disabled': disabled,
			'textarea-input--statistics': statistics,
			'textarea-input--resize-none': resizeable === 'none',
			'textarea-input--resize-horizontal': resizeable === 'horizontal',
			'textarea-input--resize-vertical': resizeable === 'vertical',
			'textarea-input--resize-both': resizeable === 'both',
			'col-span-full': span === 'full',
			'flex-grow': span === 'grow'
		}">
		<textarea ref="textarea" v-model="dataOrEmpty" @input="onInput" @keyup.enter="onKeyEnter" @blur="onBlur"
			:required="required" :disabled="disabled" class="textarea-input--control" />
		<span v-if="placeholder"
			class="textarea-input--placeholder"
			:class="{
				'textarea-input--placeholder--required': required
			}">
			<span>{{ placeholder }}</span>
			<i-ri-alert-line v-if="(isValid === false)" class="ml-0.5" />
			<span v-if="maxLen" class="inline-flex ml-1 gap-1" :class="{'text-error': !maxLenValid, 'opacity-50': maxLenValid}">{{ maxLen ? ` (${data?.toLocaleString() ? data?.toLocaleString().length + '/' : 'maximal '}${maxLen} Zeichen)` : '' }}</span>
			<span v-if="statistics" class="cursor-pointer">
				<svws-ui-tooltip position="right">
					<span class="inline-flex items-center">
						<i-ri-bar-chart-2-line class="pointer-events-auto ml-0.5" />
						<i-ri-alert-fill v-if="data === '' || data === null" />
					</span>
					<template #content>
						Relevant für die Statistik
					</template>
				</svws-ui-tooltip>
			</span>
		</span>
	</label>
</template>


<script setup lang="ts">

	import { ref, computed, watch, nextTick, type WritableComputedRef, type ComputedRef } from 'vue';

	type ResizableOption = "both" | "horizontal" | "vertical" | "none";
	type InputDataType = string | null;

	const props = withDefaults(defineProps<{
		modelValue?: InputDataType;
		placeholder?: string;
		valid?: (value: InputDataType) => boolean;
		statistics?: boolean;
		required?: boolean;
		disabled?: boolean;
		resizeable?: ResizableOption;
		autoresize?: boolean;
		cols?: number;
		rows?: number;
		maxLen?: number;
		span?: 'full' | 'grow';
	}>(), {
		modelValue: "",
		placeholder: "",
		valid: () => true,
		statistics: false,
		required: false,
		disabled: false,
		resizeable: "both",
		autoresize: false,
		cols: 80,
		rows: 3,
		maxLen: undefined,
		span: undefined,
	})

	const emit = defineEmits<{
		"update:modelValue": [value: InputDataType];
		"change": [value: InputDataType];
		"blur": [value: InputDataType];
	}>();

	// eslint-disable-next-line vue/no-setup-props-destructure
	const data = ref<InputDataType>(props.modelValue);

	const dataOrEmpty: WritableComputedRef<string> = computed({
		get: () => data.value === null ? '' : data.value,
		set: (value) => data.value = (value === '') ? null : value
	});

	const textarea = ref<HTMLTextAreaElement | null>(null);
	watch([data], () => nextTick(() => {
		if (textarea.value !== null) {
			textarea.value.style.height = '1px';
			textarea.value.style.height = `${textarea.value.scrollHeight}px`;
		}
	}), { immediate: true })

	watch(() => props.modelValue, (value: InputDataType) => updateData(value), { immediate: false });

	const isValid = computed(() => {
		let tmpIsValid = true;
		if ((props.required === true) && (data.value === null))
			return false;
		if (tmpIsValid && (props.maxLen !== undefined) && (typeof data.value === 'string') && (data.value.toLocaleString().length <= props.maxLen))
			tmpIsValid = false;
		if (tmpIsValid && (((data.value !== null) || (data.value !== ''))))
			tmpIsValid = props.valid(data.value);
		return tmpIsValid;
	})

	function updateData(value: InputDataType) {
		if (data.value !== value) {
			data.value = value;
			emit("update:modelValue", data.value);
		}
	}

	const maxLenValid = computed(() => {
		if ((props.maxLen === undefined) || (data.value === null))
			return true;
		return (typeof data.value === 'string') && (data.value.toLocaleString().length <= props.maxLen);
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

	const content = computed<InputDataType>(() => data.value);

	defineExpose<{
		content: ComputedRef<InputDataType>,
	}>({
		content
	});

</script>


<style lang="postcss">
	.textarea-input {
		@apply flex;
		@apply relative;

		textarea::placeholder {
			@apply text-black/25;
		}
	}

	.textarea-input--control {
		@apply bg-white dark:bg-black;
		@apply rounded-md border border-black/5 dark:border-white/5;
		@apply w-full;
		@apply text-base;
		@apply cursor-text;
		padding: 0.5em 0.7em;
		min-height: theme("spacing.9");

		&:hover {
			@apply border-black/25 dark:border-white/25;
		}
	}

	span.textarea-input--control {
		padding-top: 0.4em;
		padding-bottom: 0.4em;
	}

	.textarea-input--focus .textarea-input--control,
	.textarea-input--filled .textarea-input--control {
		@apply border-black dark:border-white;
		@apply outline-none;
	}

	.textarea-input--filled:not(:focus-within):not(:hover) .textarea-input--control {
		@apply border-black/25 dark:border-white/25;
	}

	.textarea-input--filled:not(:focus-within):hover .textarea-input--control {
		@apply border-black/50 dark:border-white/50;
	}

	.textarea-input--disabled .textarea-input--control {
		@apply cursor-not-allowed;
	}

	.textarea-input--invalid:not(:focus-within) .textarea-input--control {
		@apply border-error;
	}

	.textarea-input--resize-none .textarea-input--control {
		@apply resize-none;
	}

	.textarea-input--resize-vertical .textarea-input--control {
		@apply resize-y;
	}

	.textarea-input--resize-horizontal .textarea-input--control {
		@apply resize-x;
	}

	.textarea-input--resize-both .textarea-input--control {
		@apply resize;
	}

	.textarea-input--placeholder {
		@apply absolute;
		@apply pointer-events-none;
		@apply opacity-60;
		@apply transform;
		@apply flex items-center font-medium;

		top: 0.5em;
		left: 0.7em;
		line-height: 1.33;
	}

	.textarea-input:not(.textarea-input--filled) .textarea-input--placeholder {
		@apply font-normal;
	}

	.textarea-input:not(.textarea-input--filled):not(:focus-within):not(.textarea-input--disabled):hover .textarea-input--placeholder {
		@apply opacity-100;
	}

	.textarea-input--statistics .textarea-input--control {
		@apply border-violet-500;
	}

	.textarea-input--statistics.textarea-input--invalid .textarea-input--control {
		@apply border-error;
	}

	.textarea-input--statistics .textarea-input--placeholder {
		@apply text-violet-500;
	}

	.textarea-input--statistics {
		.tooltip-trigger--triggered svg {
			@apply text-violet-800;
		}
	}

	.textarea-input--statistics.textarea-input--invalid .textarea-input--placeholder {
		@apply text-violet-500 font-medium;
	}

	.textarea-input--focus .textarea-input--placeholder,
	.textarea-input--filled .textarea-input--placeholder {
		@apply -translate-y-1/2;
		@apply bg-white dark:bg-black opacity-100;
		@apply rounded;
		@apply px-1;

		top: 0;
		left: 0.7em;
		font-size: 0.78rem;

		&:after {
			content: "";
		}
	}

	.textarea-input--invalid:not(:focus-within) .textarea-input--placeholder,
	.textarea-input--invalid:not(:focus-within) .textarea-input--control {
		@apply text-error;
	}

	.textarea-input--disabled {
		@apply cursor-not-allowed;

		.textarea-input--placeholder {
			@apply text-black/25 dark:text-white;
		}

		.textarea-input--control {
			@apply bg-black/10 dark:bg-white/10 border-black/25 dark:border-white/25 text-black dark:text-white;
			@apply opacity-20;
			@apply cursor-not-allowed pointer-events-none;
		}
	}

	.textarea-input--placeholder--required:after {
		@apply text-error;
		content: " *";
	}

</style>
