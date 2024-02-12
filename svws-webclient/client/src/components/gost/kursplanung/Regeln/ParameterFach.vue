<template>
	<svws-ui-select v-model="selected" :items="mapFaecher" :item-text="text" />
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostFach} from "@core";
	import type { WritableComputedRef } from "vue";

	const props = defineProps<{
		modelValue: GostFach;
		mapFaecher: Map<number, GostFach>;
	}>();

	const emit = defineEmits(['update:modelValue'])
	const selected: WritableComputedRef<GostFach> = computed({
		get: () => props.modelValue,
		set: value => emit('update:modelValue', value)
	});

	function text(i: GostFach) {
		return i.bezeichnung ?? '??';
	}

</script>
