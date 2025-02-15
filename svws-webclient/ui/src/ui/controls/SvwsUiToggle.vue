<template>
	<label class="toggle" :class="{
		'toggle--statistics': statistics,
		'toggle--headless': headless,
		'toggle--disabled': disabled
	}">
		<input v-model="value" class="toggle--control" type="checkbox">
		<span class="toggle--indicator" />
		<span v-if="$slots.default || statistics" class="toggle--label">
			<slot />
			<span class="icon i-ri-bar-chart-2-line icon-ui-statistic ml-2" v-if="statistics" />
		</span>
	</label>
</template>

<script setup lang='ts'>
	import { computed } from 'vue';

	const props = withDefaults(defineProps<{
		modelValue?: boolean;
		statistics?: boolean;
		headless?: boolean;
		disabled?: boolean;
	}>(), {
		modelValue: false,
		statistics: false,
		headless: false,
		disabled: false,
	});

	defineSlots();
	const emit = defineEmits<{
		(e: 'update:modelValue', value: boolean): void;
	}>();

	const value = computed({
		get: () => props.modelValue,
		set: (value: boolean) => emit('update:modelValue', value),
	});
</script>
