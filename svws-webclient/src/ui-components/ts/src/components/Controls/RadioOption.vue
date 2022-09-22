<script setup lang='ts'>
const {
	name = '',
	label = '',
	value = '',
	disabled = false,
	statistics = false,
	modelValue = '',
} = defineProps<{
	name?: string;
	label?: string;
	value?: string;
	disabled?: boolean;
	statistics?: boolean;
	modelValue?: string;
}>();

const emit = defineEmits<{
	(e: 'update:modelValue', value: string): void,
}>();

function onInput(event: Event) {
	if (!disabled) {
		emit("update:modelValue", (event.target as HTMLInputElement).value);
	}
}

const checked = computed({
	get() {
		return modelValue;
	},
	set(value: string) {
		emit('update:modelValue', value);
	}
})
</script>

<template>
	<label
class="radio--label" :class="{
		'radio--label--disabled': disabled,
		'radio--statistics': statistics
	}">
		<input
v-model="checked" type="radio" :name="name" :value="value" :disabled="disabled" class="radio--indicator"
			@input="onInput" />
		<span class="radio--label">{{ label }}
			<i-ri-bar-chart-fill v-if="statistics" class="ml-2" />
		</span>
	</label>
</template>

<style>
.radio--label {
	@apply cursor-pointer;
	@apply flex flex-row items-center;
	@apply select-none;
	@apply space-x-2;
	@apply text-input;
}

.radio--label.radio--statistics {
	@apply text-purple;
}

.radio--statistics .radio--indicator {
	@apply border-purple;
}

.radio--statistics .radio--indicator:checked::before {
	@apply bg-purple;
}

.radio--indicator {
	@apply appearance-none;
	@apply rounded-full border-2 border-black;
	@apply cursor-pointer;
	@apply flex flex-shrink-0 items-center justify-center;
	@apply h-5 w-5;
}

.radio--indicator:focus {
	@apply outline-none ring-2 ring-primary ring-opacity-50;
}

.radio--indicator:checked::before {
	@apply bg-black;
	@apply block;
	@apply rounded-full;
	@apply h-3 w-3;
	content: "";
}

.radio--indicator:disabled {
	@apply bg-disabled;
	@apply border-disabled-medium;
	@apply cursor-not-allowed;
	@apply text-disabled-dark;
}

.radio--label--disabled {
	@apply cursor-not-allowed;
	@apply text-disabled-dark;
}
</style>
