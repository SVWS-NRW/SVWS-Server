<template>
	<svws-ui-multi-select v-model="selected" :items="props.blockung.datenmanager?.getMengeOfSchienen() || []"
		:item-text="(i: GostBlockungSchiene) => i.bezeichnung.toString()" />
</template>

<script setup lang="ts">

	import { GostBlockungSchiene } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";

	const props = defineProps<{
		modelValue: GostBlockungSchiene;
		blockung: DataGostKursblockung;
	}>();
	const emit = defineEmits(['update:modelValue'])

	const selected: WritableComputedRef<GostBlockungSchiene> = computed({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	});

</script>

