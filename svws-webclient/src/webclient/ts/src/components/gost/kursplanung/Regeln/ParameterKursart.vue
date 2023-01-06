<script setup lang="ts">
import { GostKursart } from "@svws-nrw/svws-core-ts";
import { computed } from "vue";

const props = defineProps<{ modelValue: GostKursart; }>();
const kursarten = GostKursart.values()
	
const emit = defineEmits(['update:modelValue'])
const selected = computed({
	get(): GostKursart {
		return props.modelValue;
	},
	set(val: GostKursart) {
		emit('update:modelValue', val);
	}
});
</script>

<template>
	<svws-ui-multi-select
		v-model="selected"
		:items="kursarten"
		:item-text="(i: GostKursart) => i.beschreibung.toString()" 
		/>
</template>