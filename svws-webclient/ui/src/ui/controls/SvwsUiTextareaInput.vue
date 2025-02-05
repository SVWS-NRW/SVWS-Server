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
		<textarea ref="textarea" v-model="dataOrEmpty" @input="onInput" @blur="onBlur" class="textarea-input--control" :disabled :required :rows v-bind="{ ...$attrs }" />
		<span :id="idPlaceholder" v-if="placeholder.length > 0" class="textarea-input--placeholder" :class="{ 'textarea-input--placeholder--required': required }">
			<span>{{ placeholder }}</span>
			<span class="icon i-ri-alert-line ml-0.5 -my-0.5 icon-error" v-if="isValid === false" />
			<span v-if="(maxLen > 0) && (data !== null)" class="inline-flex ml-1 gap-1" :class="maxLenValid ? 'opacity-50' : 'text-ui-danger'">
				{{ `(${(data.toLocaleString().length > 0) ? data.toLocaleString().length + '/' : 'maximal '}${maxLen} Zeichen)` }}
			</span>
			<span :id="idStatistics" v-if="statistics" class="cursor-pointer">
				<svws-ui-tooltip position="right">
					<span class="inline-flex items-center">
						<span class="icon i-ri-bar-chart-2-line icon-statistics pointer-events-auto ml-0.5" />
						<span class="icon i-ri-alert-fill icon-error" v-if="(data === '') || (data === null)" />
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
		if ((props.required === true) && (data.value === null))
			return false;
		if (!maxLenValid.value)
			tmpIsValid = false;
		if (tmpIsValid && (data.value !== null) && (data.value !== ''))
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


<style lang="postcss">

	@reference "../../assets/styles/index.css";

	.textarea-input {
		@apply flex;
		@apply relative;

		textarea::placeholder {
			@apply text-ui-secondary;
		}
		&.textarea-input--headless {
			@apply bg-transparent;
			textarea {
				@apply bg-transparent border-none ;
			}
		}
	}

	.textarea-input--control {
		@apply bg-ui border border-ui-secondary text-ui;
		@apply rounded-md;
		@apply w-full;
		@apply text-base;
		@apply cursor-text;
		padding: 0.5em 0.7em;
		min-height: 2.5em;
		min-width: 10em;

		&:focus {
			@apply outline-hidden;
		}

		&:focus-visible {
			@apply ring-3 ring-ui-neutral;
		}
	}

	span.textarea-input--control {
		padding-top: 0.4em;
		padding-bottom: 0.4em;
	}

	.textarea-input--focus .textarea-input--control,
	.textarea-input--filled .textarea-input--control {
		@apply border-ui;
	}

	.textarea-input--invalid:not(:focus-within) .textarea-input--control {
		@apply border-ui-danger;
	}

	.textarea-input--statistics .textarea-input--control {
		@apply border-ui-statistic;
	}

	.textarea-input--statistics:not(.textarea-input--filled) .textarea-input--control {
		@apply border-ui-statistic-secondary;
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

		.icon {
			@apply w-[1.4em];
		}
	}

	.textarea-input:not(.textarea-input--filled):not(:focus-within):not(.textarea-input--disabled):hover .textarea-input--placeholder {
		@apply opacity-80;
	}

	.textarea-input--statistics.textarea-input--invalid .textarea-input--control {
		@apply border-ui-danger;
	}

	.textarea-input--statistics .textarea-input--placeholder {
		@apply text-ui-statistic;
	}

	.textarea-input--statistics.textarea-input--invalid .textarea-input--placeholder {
		@apply text-ui-statistic;
	}

	.textarea-input--focus:not(.textarea-input--headless) .textarea-input--placeholder,
	.textarea-input--filled:not(.textarea-input--headless) .textarea-input--placeholder {
		@apply bg-ui opacity-100;
		@apply -translate-y-1/2;
		@apply rounded;
		@apply px-1;

		top: 0;
		left: 0.7em;
		font-size: 0.78rem;

		&:after {
			content: "";
		}
	}

	.textarea-input--headless.textarea-input--filled .textarea-input--placeholder {
		@apply opacity-0;
	}

	.textarea-input--invalid .textarea-input--placeholder,
	.textarea-input--invalid:not(:focus-within) .textarea-input--control {
		@apply text-ui-danger;
	}

	.textarea-input--placeholder--required:after {
		@apply text-ui-danger;
		content: " *";
	}

	.textarea-input--disabled .textarea-input--control {
		@apply bg-ui text-ui-disabled border-ui-disabled;
		@apply pointer-events-none resize-none;
	}

	.textarea-input--disabled .textarea-input--placeholder {
		@apply bg-ui text-ui-disabled;
	}



</style>
