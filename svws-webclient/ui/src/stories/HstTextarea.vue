<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<div>
		<SvwsUiTextareaInput :model-value="text" :placeholder="title" @input="text = $event" />
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';


	const props = withDefaults(defineProps<{
		title?: string;
		modelValue: string;
	}>(), {
		title: '',
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', event: string): void;
	}>();

	const text = computed<string>({
		get: () => props.modelValue,
		set: (value) =>	emit("update:modelValue", value),
	});

</script>