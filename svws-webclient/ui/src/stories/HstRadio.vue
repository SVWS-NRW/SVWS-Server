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


	interface Options {
		label: string;
		value: string;
	}

	const emit = defineEmits<{
		(e: 'update:modelValue', value: object | number | string | boolean): void,
	}>();

	const checked = computed<object | number | boolean | string>({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value),
	});

	const props = withDefaults(defineProps<{
		title?: string;
		options: Options[];
		modelValue: object | number | string | boolean;
	}>(), {
		title: '',
	});

</script>