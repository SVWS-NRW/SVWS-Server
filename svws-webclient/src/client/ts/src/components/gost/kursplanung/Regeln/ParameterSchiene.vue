<template>
	<svws-ui-multi-select v-model="selected" :items="schienen"
		:item-text="(i: GostBlockungSchiene) => i.bezeichnung" />
</template>

<script setup lang="ts">

	import type { GostBlockungSchiene } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		modelValue: GostBlockungSchiene;
		schienen: GostBlockungSchiene[];
	}>();
	const emit = defineEmits(['update:modelValue'])

	const selected: WritableComputedRef<GostBlockungSchiene> = computed({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	});

</script>

