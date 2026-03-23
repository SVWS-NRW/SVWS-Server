<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<div>
		<SvwsUiInputNumber v-model="checked" :placeholder="title" :steps :decimal-places :disabled :min :max />
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	const props = withDefaults(defineProps<{
		title?: string;
		modelValue?: number | undefined;
		steps?: false | number;
		decimalPlaces?: 0 | 1 | 2 | 3 | 4;
		disabled?: boolean;
		min?: number;
		max?: number;
	}>(), {
		title: '',
		modelValue: undefined,
		steps: 1,
		decimalPlaces: 0,
		disabled: false,
		min: undefined,
		max: undefined,
	});

	const emit = defineEmits<{
		'update:modelValue': [value: number | undefined];
	}>();

	const checked = computed<number | null>({
		get: () => props.modelValue ?? null,
		set: (value) =>	emit("update:modelValue", value ?? undefined),
	});

</script>
