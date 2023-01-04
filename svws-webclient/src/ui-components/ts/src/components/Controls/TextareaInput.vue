<script setup lang='ts'>
type ResizableOption = 'both' | 'horizontal' | 'vertical' | 'none';

const {
	modelValue = '',
	placeholder = '',
	valid = true,
	statistics = false,
	required = false,
	disabled = false,
	resizeable = 'both',
	cols = 80,
	rows = 6
} = defineProps<{
	modelValue?: string;
	placeholder?: string;
	valid?: boolean;
	statistics?: boolean;
	required?: boolean;
	disabled?: boolean;
	resizeable?: ResizableOption;
	cols?: number;
	rows?: number;
}>();

const emit = defineEmits<{
	(e: 'update:modelValue', value: string): void,
	(e: 'focus', event: Event): void,
	(e: 'blur', event: Event): void,
	(e: 'click', event: Event): void,
	(e: 'mousedown', event: Event): void,
	(e: 'keydown', event: Event): void,
}>();

const focused = ref(false);

function onInput(event: Event) {
	emit("update:modelValue", (event.target as HTMLInputElement).value);
}

function onFocus(event: FocusEvent) {
	focused.value = true;
	emit("focus", event);
}

function onBlur(event: FocusEvent) {
	focused.value = false;
	emit("blur", event);
}

function onClick(event: MouseEvent) {
	emit("click", event);
}

function onMouseDown(event: MouseEvent) {
	emit("mousedown", event);
}

function onKeyDown(event: KeyboardEvent) {
	emit("keydown", event);
}

</script>

<template>
	<label
		class="textarea-input" :class="{
			'textarea-input-focus': focused,
			'textarea-input-filled': !!modelValue,
			'textarea-input-invalid': !valid,
			'textarea-input-disabled': disabled,
			'textarea-input--statistics': statistics,
			'textarea-input--resize-none': resizeable === 'none',
			'textarea-input--resize-horizontal':
				resizeable === 'horizontal',
			'textarea-input--resize-vertical':
				resizeable === 'vertical',
			'textarea-input--resize-both': resizeable === 'both'
		}">
		<textarea
			class="textarea-input--control"
			:value="modelValue"
			:required="required"
			:disabled="disabled"
			:cols="cols"
			:rows="rows"
			@input="onInput"
			@focus="onFocus"
			@blur="onBlur"
			@click="onClick"
			@mousedown="onMouseDown"
			@keydown="onKeyDown"
			></textarea>
		<span
			v-if="placeholder"
			class="textarea-input--placeholder"
			:class="{
				'textarea-input--placeholder--required': required
			}">
			{{ placeholder }}
			<i-ri-bar-chart-fill v-if="statistics" class="ml-2" />
		</span>
	</label>
</template>

<style>
.textarea-input {
	@apply flex;
	@apply relative;
}

.textarea-input--control {
	@apply bg-white;
	@apply rounded-md border border-black border-opacity-20;
	@apply w-full;
	@apply text-base;
	padding: 0.5em 0.7em;
}

.textarea-input-focus .textarea-input--control,
.textarea-input-filled .textarea-input--control {
	@apply border-gray border-opacity-100;
	@apply outline-none;
}

.textarea-input-disabled .textarea-input--control {
	@apply cursor-not-allowed;
}

.textarea-input-invalid .textarea-input--control {
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
	@apply opacity-50;
	@apply transform;
	@apply flex items-center;

	top: 0.5em;
	left: 0.7em;
	line-height: 1.33;
}

.textarea-input--statistics .textarea-input--control {
	@apply border-purple;
	@apply bg-purple bg-opacity-[0.02];
}

.textarea-input--statistics.textarea-input-invalid .textarea-input--control {
	@apply border-error;
}

.textarea-input--statistics .textarea-input--placeholder {
	@apply text-purple;
}

.textarea-input--statistics.textarea-input-invalid .textarea-input--placeholder {
	@apply text-purple;
}

.textarea-input-focus .textarea-input--placeholder,
.textarea-input-filled .textarea-input--placeholder {
	@apply -translate-y-1/2;
	@apply bg-white opacity-100;
	@apply rounded;
	@apply px-1;

	top: 0;
	left: 0.7em;
	font-size: 0.78rem;

	&:after {
		content: ''
	}
}

.textarea-input-invalid .textarea-input--placeholder {
	@apply text-error;
}

.textarea-input-disabled {
	@apply opacity-50;
}

.textarea-input--placeholder--required:after {
	@apply text-error;
	content: " *";
}
</style>
