<script lang="ts" setup>
type CheckboxValue = string | number | boolean;
type ModelValue = boolean | Array<CheckboxValue>;

const {
	value = '',
	statistics = false,
	disabled = false,
	modelValue
} = defineProps<{
	value?: CheckboxValue;
	modelValue: ModelValue;
	statistics?: boolean;
	disabled?: boolean;
}>();

const emit = defineEmits<{
	(e: 'update:modelValue', event: ModelValue): void;
}>();

const model = computed({
	get(): ModelValue {
		return modelValue;
	},
	set(value: ModelValue) {
		emit("update:modelValue", value);
	}
});
</script>

<template>
	<label
		class="checkbox" 
		:class="{
			'checkbox--disabled': disabled,
			'checkbox--statistics': statistics
		}">
		<input v-model="model" class="checkbox--control" type="checkbox" :value="value" :disabled="disabled" />
		<span class="checkbox--indicator">
			<Icon>
				<i-ri-check-line />
			</Icon>
		</span>
		<span class="checkbox--label">
			<slot />
			<i-ri-bar-chart-fill v-if="statistics" class="ml-2" />
		</span>
	</label>
</template>

<style>
.checkbox {
	@apply cursor-pointer;
	@apply inline-flex;
	@apply items-center justify-start;
	@apply select-none;
	@apply text-input;
}

.checkbox--control {
	@apply hidden;
}

.checkbox--diabled {
	@apply border-gray;
	@apply text-gray;
}

.checkbox--indicator {
	@apply bg-white;
	@apply border-2 border-black;
	@apply inline-flex items-center justify-center;
	@apply h-5 w-5;
}

.checkbox--disabled .checkbox--indicator {
	@apply border-gray;
}
.checkbox--statistics .checkbox--indicator {
	@apply border-purple;
}

.checkbox--indicator .icon {
	@apply opacity-0;

	font-size: 1rem;
}

.checkbox:focus .checkbox--indicator,
.checkbox input:focus+.checkbox--indicator {
	@apply border-primary;
}

.checkbox input:checked+.checkbox--indicator .icon {
	@apply opacity-100;
	@apply text-black;
}

.checkbox--disabled input:checked+.checkbox--indicator .icon {
	@apply opacity-100;
	@apply text-gray;
	@apply bg-gray;
	@apply bg-opacity-5;
}
.checkbox--statistics input:checked+.checkbox--indicator .icon {
	@apply opacity-100;
	@apply text-purple;
	@apply bg-purple;
	@apply bg-opacity-5;
}

.checkbox--label {
	@apply ml-2;
	@apply text-black;
	@apply flex items-center;
}

.checkbox--disabled .checkbox--label {
	@apply text-gray;
}
.checkbox--statistics .checkbox--label {
	@apply text-purple;
}
</style>
