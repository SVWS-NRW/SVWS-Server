<!-- eslint-disable vue/multi-word-component-names -->
<template>
	<div>
		<SvwsUiTextInput v-model="checked" :placeholder="title" />
	</div>
</template>

<script setup lang="ts">
	import { computed } from 'vue';

	const props = withDefaults(defineProps<{
		title?: string;
		modelValue: string | null | undefined;
	}>(), {
		title: '',
	});

	const emit = defineEmits<{
		(e: 'update:modelValue', event: string | undefined | null): void;
	}>();

	const checked = computed<string | undefined | null>({
		get: () => props.modelValue,
		set: (value) =>	emit("update:modelValue", value),
	});

</script>