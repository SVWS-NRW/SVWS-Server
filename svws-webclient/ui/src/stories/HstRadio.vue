<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<div>
		{{ title }}
		<SvwsUiRadioGroup>
			<SvwsUiRadioOption v-for="option of options" v-model="checked" :label="option.label" :key="option.value" :value="option.value" />
		</SvwsUiRadioGroup>
	</div>
</template>

<script setup lang="ts">

	import { computed } from 'vue';

	type Value = object | number | string | boolean;

	interface Options {
		label: string;
		value: string;
	}

	const emit = defineEmits<{
		'update:modelValue': [value: Value];
	}>();

	const checked = computed<Value>({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value),
	});

	const props = withDefaults(defineProps<{
		title?: string;
		options: Options[];
		modelValue: Value;
	}>(), {
		title: '',
	});

</script>