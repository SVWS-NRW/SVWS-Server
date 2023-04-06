<script lang="ts" setup>
	import { computed } from 'vue';

	type CheckboxValue = string | number | boolean | null;
	type ModelValue = boolean | Array<CheckboxValue> | undefined;

	const props = withDefaults(defineProps<{
		value?: CheckboxValue;
		modelValue: ModelValue | null;
		statistics?: boolean;
		disabled?: boolean;
		circle?: boolean;
		headless?: boolean;
	}>(), {
		value: '',
		statistics: false,
		disabled: false,
		circle: false,
		headless: false
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', event: ModelValue): void;
	}>();

	const model = computed({
		get(): ModelValue {
			return props.modelValue ?? undefined;
		},
		set(value: ModelValue) {
			emit("update:modelValue", value);
		}
	});
</script>

<template>
	<label role="none" class="checkbox"
		:class="{
			'checkbox--disabled': disabled,
			'checkbox--statistics': statistics,
			'checkbox--checked': modelValue,
			'checkbox--circle': circle,
			'checkbox--headless': headless,
			'checkbox--indeterminate': modelValue === undefined || modelValue === null
		}">
		<input v-model="model" class="checkbox--control" type="checkbox" :value="value" :disabled="disabled" :title="disabled ? 'Deaktiviert' : ''">
		<svws-ui-icon v-if="modelValue === null" role="checkbox">
			<i-ri-checkbox-indeterminate-line />
		</svws-ui-icon>
		<svws-ui-icon v-else-if="modelValue" role="checkbox">
			<i-ri-checkbox-line v-if="!circle" />
			<i-ri-checkbox-circle-fill v-if="circle" />
		</svws-ui-icon>
		<svws-ui-icon v-else-if="!modelValue" role="checkbox">
			<i-ri-checkbox-blank-line v-if="!circle" />
		</svws-ui-icon>
		<span class="checkbox--label" v-if="$slots.default || statistics">
			<slot />
			<svws-ui-icon v-if="statistics" class="ml-1">
				<i-ri-bar-chart-fill />
			</svws-ui-icon>
		</span>
	</label>
</template>

<style>
.checkbox {
	@apply cursor-pointer;
	@apply inline-flex;
	@apply items-start justify-start;
	@apply select-none;
	@apply text-base font-normal leading-none;
	@apply my-1;
}

.checkbox:not(.checkbox--checked):not(.checkbox--indeterminate) .icon {
	@apply opacity-50;
}

.checkbox--control {
	@apply hidden;
}

.checkbox--disabled {
	@apply opacity-50;
	@apply cursor-not-allowed;
}

.checkbox--checked,
.checkbox--indeterminate {
	@apply font-medium;
}

.checkbox--label {
	margin: 0.1rem 0 0 0.2rem;
}

.checkbox--disabled .checkbox--label {
	@apply text-gray;
}
.checkbox--statistics {
	@apply text-purple-500;
}

.checkbox--statistics .checkbox--label .icon {
	@apply opacity-100 inline-block;
	width: 0.8em;
	height: 0.8em;

	svg {
		@apply w-full h-full;
	}
}

.checkbox--wrapper {
	margin-left: -1rem;
}

.checkbox--wrapper .checkbox {
	margin-left: 1rem;
}

.checkbox--headless {
	margin: 0;
}
</style>
