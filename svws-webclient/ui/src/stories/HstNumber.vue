<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<div>
		<SvwsUiInputNumber v-model="checked" :placeholder="title" />
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	const props = withDefaults(defineProps<{
		title?: string;
		modelValue?: number | undefined;
	}>(), {
		title: '',
		modelValue: undefined,
	});

	const emit = defineEmits<{
		'update:modelValue': [value: number | undefined];
	}>();

	const checked = computed<number | null>({
		get: () => props.modelValue ?? null,
		set: (value) =>	emit("update:modelValue", value ?? undefined),
	});

</script>