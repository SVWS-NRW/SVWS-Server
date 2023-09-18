<script setup lang='ts'>
	import { computed, ref, onMounted } from 'vue';

	throw new Error('Deprecated: SvwsUiSelectInput');

	type Option = {
		index: string;
		label: string;
		selected?: boolean;
		disabled?: boolean;
	}

	const props = withDefaults(defineProps<{
		placeholder?: string;
		options?: Array<Option>;
		valid?: boolean;
		disabled?: boolean;
		modelValue?: string;
	}>(), {
		placeholder: '',
		options: undefined,
		valid: true,
		disabled: false,
		modelValue: ''
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', value: string): void,
		(e: 'focus', event: Event): void,
		(e: 'blur', event: Event): void,
		(e: 'click', event: Event): void,
		(e: 'mousedown', event: Event): void,
		(e: 'keydown', event: Event): void,
	}>();

	const value = computed({
		get: () => props.modelValue,
		set: (value: string) => emit('update:modelValue', value)
	})
	const focused = ref(false);

	onMounted(() => {
		if (props.options === undefined)
			return;
		props.options.forEach(option => {
			if ("selected" in option) {
				value.value = option.index;
			}
		});
	});

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

	function onKeyDown(event: InputEvent) {
		emit("keydown", event);
	}
</script>

<template>
	<label class="select-input" :class="{
		'select-input-filled': !!value,
		'select-input-focus': focused,
		'select-input-invalid': !valid,
		'select-input-disabled': disabled
	}">
		<select v-model="value" class="select-input--control" :disabled="disabled" @focus="onFocus" @blur="onBlur"
			@click="onClick" @mousedown="onMouseDown" @onkeydown="onKeyDown">
			<option v-if="!value" disabled selected>{{ placeholder }}</option>
			<option v-for="option in options" :key="option.index" :value="option.index"
				:disabled="option.disabled">
				{{ option.label }}
			</option>
		</select>
		<span v-if="placeholder" class="select-input--placeholder">{{
			placeholder
		}}</span>
		<span class="icon dropdown--icon">
			<i-ri-arrow-up-s-line v-if="focused" />
			<i-ri-arrow-down-s-line v-else />
		</span>
	</label>
</template>

<style>
.select-input {
	@apply flex;
	@apply relative;
}

.select-input .icon {
	@apply absolute;
	@apply flex;
	@apply inset-y-0 right-0;
	@apply items-center justify-center;
	@apply opacity-20;
	@apply pointer-events-none;
	@apply rounded;
	@apply w-8;

	margin-top: 1px;
	margin-right: 1px;
	margin-bottom: 1px;
}

.select-input-focus .icon,
.select-input-filled .icon {
	@apply opacity-100;
}

.select-input-invalid .icon {
	@apply text-error;
}

.select-input--control {
	@apply bg-white;
	@apply rounded border border-black border-opacity-20;
	@apply h-9 w-full;
	@apply px-4 py-2;
	@apply text-base text-black;

	-webkit-appearance: none;
	-moz-appearance: none;
}

.select-input-focus .select-input--control,
.select-input-filled .select-input--control {
	@apply border-gray border-opacity-100;
	@apply outline-none;
}

.select-input-disabled .select-input--control {
	@apply cursor-not-allowed;
}

.select-input-invalid .select-input--control {
	@apply border-error;
}

.select-input--placeholder {
	@apply -translate-y-1/2;
	@apply absolute;
	@apply bg-white;
	@apply pointer-events-none;
	@apply px-1;
	@apply text-sm text-gray;
	@apply transform;

	top: 0;
	left: theme("spacing.1");
}

.select-input-invalid .select-input--placeholder {
	@apply text-error;
}

.select-input-disabled {
	@apply opacity-50;
}
</style>
