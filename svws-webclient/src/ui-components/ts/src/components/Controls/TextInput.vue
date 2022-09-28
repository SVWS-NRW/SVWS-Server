<script setup lang='ts'>
import { InputHTMLAttributes } from 'vue';

type InputType = 'text' | 'number' | 'date' | 'email';

const {
	type = 'text',
	modelValue = '',
	placeholder = '',
	statistics = false,
	valid = true,
	disabled = false,
	required = false,
	readonly = false,
	headless = false,
	focus = false
} = defineProps<{
	type?: InputType;
	modelValue: string | number;
	placeholder?: string;
	statistics?: boolean;
	valid?: boolean;
	disabled?: boolean;
	required?: boolean;
	readonly?: boolean;
	headless?: boolean;
	focus?: boolean;
}>();

const emit = defineEmits<{
	(e: 'update:modelValue', value: string | number): void,
	(e: 'focus', event: Event): void,
	(e: 'blur', event: Event): void,
	(e: 'click', event: Event): void,
	(e: 'mousedown', event: Event): void,
	(e: 'keydown', event: Event): void,
}>();

const slots = useSlots();

const input = ref<null | HTMLElement>(null);
const focused = ref(false);

const vFocus = {
  mounted: (el: HTMLInputElement) => { if (focus) el.focus() }
}

const emailValid = computed(() => {
	if (type !== "email" || !modelValue) return true;
	else {
		return (
			// eslint-disable-next-line no-useless-escape
			/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(
				modelValue as string
			) ||
			// eslint-disable-next-line no-useless-escape
			/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
				modelValue as string
			)
		);
	}
});
const hasIcon = computed(() => !!slots.default);

function onInput(event: Event) {
	emit("update:modelValue", (event.target as HTMLInputElement).value);
}

function onFocus(event: Event) {
	focused.value = true;
	emit("focus", event);
}

function onBlur(event: Event) {
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

defineExpose({
	input,
});

</script>
	
<template>
	<label class="text-input-component" :class="{
		'text-input-focus': focused,
		'text-input-filled':
			!!modelValue || (type === 'number' && modelValue === '0'),
		'text-input-invalid': !valid || !emailValid,
		'text-input-disabled': disabled,
		'text-input-readonly': readonly,
		'text-input--icon': hasIcon,
		'text-input--statistics': statistics
	}">
		<input ref="input" v-focus :class="{
			'text-input--control': !headless,
			'text-input--headless': headless
		}" :type="type" :value="modelValue" :disabled="disabled" :required="required" :readonly="readonly" @input="onInput"
			@focus="onFocus" @blur="onBlur" @click="onClick" @mousedown="onMouseDown" @keydown="onKeyDown" />
		<span v-if="placeholder && !headless" class="text-input--placeholder" :class="{
			'text-input--placeholder--required': required
		}">
			{{ placeholder }}
			<i-ri-bar-chart-fill v-if="statistics" class="ml-2" />
		</span>
		<Icon v-if="type !== 'date' && hasIcon">
			<slot></slot>
		</Icon>
		<Icon v-else-if="type === 'date'" class="text-input--calendar-icon">
			<i-ri-calendar-line />
		</Icon>
	</label>
</template>

<style>
.text-input-component {
	@apply flex;
	@apply relative;
	@apply w-full;
	@apply overflow-hidden whitespace-nowrap;
}

.text-input-component .icon {
	@apply absolute;
	@apply flex;
	@apply inset-y-0 right-0;
	@apply items-center justify-center;
	@apply opacity-20;
	@apply pointer-events-none;
	@apply rounded;
	@apply w-8;

	margin-bottom: 1px;
	margin-right: 1px;
	margin-top: 1px;
}

.text-input-focus .icon,
.text-input-filled .icon {
	@apply opacity-100;
}

.text-input-invalid .icon {
	@apply text-error;
}

.text-input--control {
	@apply bg-white;
	@apply rounded border border-black border-opacity-20;
	@apply h-9 w-full;
	@apply px-4 py-2;
	@apply text-input text-black;
	@apply whitespace-nowrap;
}

.text-input-focus .text-input--control,
.text-input-filled .text-input--control {
	@apply border-gray border-opacity-100;
	@apply outline-none;
}

.text-input--control[type="date"]::-webkit-inner-spin-button,
.text-input--control[type="date"]::-webkit-calendar-picker-indicator {
	@apply opacity-0;
}

.text-input--control[type="date"] {
	color: transparent;
	@apply bg-transparent;
}

.text-input-focus .text-input--control[type="date"],
.text-input-filled .text-input--control[type="date"] {
	@apply text-black;
	@apply pr-2;
}

@-moz-document url-prefix() {

	.text-input-focus .text-input--calendar-icon,
	.text-input-filled .text-input--calendar-icon {
		@apply hidden;
	}
}

.text-input-disabled .text-input--control {
	@apply cursor-not-allowed;
}

.text-input-readonly .text-input--control {
	@apply cursor-default;
}

.text-input-invalid .text-input--control {
	@apply border-error;
}

.text-input--placeholder {
	@apply absolute;
	@apply pointer-events-none;
	@apply text-input text-gray;
	@apply transform;
	@apply flex items-center;

	top: theme("spacing.2");
	left: theme("spacing.4");
}

.text-input-focus .text-input--placeholder,
.text-input-filled .text-input--placeholder {
	@apply -translate-y-1/2;
	@apply bg-white;
	@apply rounded;
	@apply px-1;
	@apply text-button;

	top: 0;
	left: theme("spacing.1");
}

.text-input-invalid .text-input--placeholder {
	@apply text-error;
}

.text-input--statistics .text-input--control {
	@apply border-purple;
	@apply bg-purple bg-opacity-5;
}

.text-input--statistics.text-input-invalid .text-input--control {
	@apply border-error;
}

.text-input--statistics .text-input--placeholder {
	@apply text-purple;
}

.text-input-disabled {
	@apply opacity-50;
}

.text-input-focus,
.text-input-filled {
	@apply overflow-visible;
}

.text-input--placeholder--required:after {
	@apply text-error;
	content: " *";
}

.text-input--icon .text-input--control {
	@apply pr-8;
}

.text-input--headless {
	@apply bg-white w-full text-black whitespace-nowrap outline-none border-0 px-3;
}
</style>
