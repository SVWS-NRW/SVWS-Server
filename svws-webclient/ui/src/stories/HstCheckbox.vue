<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<div>
		<SvwsUiCheckbox v-model="checked"> {{ title }} </SvwsUiCheckbox>
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';


	const props = withDefaults(defineProps<{
		title?: string;
		modelValue: boolean;
	}>(), {
		title: '',
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', event: boolean): void;
	}>();

	const checked = computed<boolean>({
		get: () => props.modelValue,
		set: (value) =>	emit("update:modelValue", value),
	});

</script>