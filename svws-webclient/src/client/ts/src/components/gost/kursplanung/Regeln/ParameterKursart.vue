<template>
	<svws-ui-multi-select v-model="selected" :items="kursarten" :item-text="(i: GostKursart) => i.beschreibung" />
</template>

<script setup lang="ts">

	import { GostKursart } from "@svws-nrw/svws-core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		modelValue: GostKursart;
	}>();

	const kursarten = GostKursart.values()

	const emit = defineEmits(['update:modelValue'])
	const selected: WritableComputedRef<GostKursart> = computed({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	});

</script>

