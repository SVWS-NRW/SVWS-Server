<template>
	<svws-ui-multi-select v-model="selected" :items="kursarten" :item-text="(i: GostKursart) => i.beschreibung.toString()" />
</template>

<script setup lang="ts">

	import { GostKursart } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";

	const { modelValue } = defineProps<{ 
		modelValue: GostKursart; 
	}>();

	const kursarten = GostKursart.values()
		
	const emit = defineEmits(['update:modelValue'])
	const selected: WritableComputedRef<GostKursart> = computed({
		get: () => modelValue,
		set: (value) => emit('update:modelValue', value)
	});

</script>

