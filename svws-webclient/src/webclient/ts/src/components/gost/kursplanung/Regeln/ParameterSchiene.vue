<template>
	<svws-ui-multi-select v-model="selected" :items="app.dataKursblockung.datenmanager?.getMengeOfSchienen() || []"
		:item-text="(i: GostBlockungSchiene) => i.bezeichnung.toString()" />
</template>

<script setup lang="ts">

	import { injectMainApp, Main } from "~/apps/Main";
	import { GostBlockungSchiene } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";

	const main: Main = injectMainApp();
	const app = main.apps.gost;

	const { modelValue } = defineProps<{
		modelValue: GostBlockungSchiene;
	}>();
	const emit = defineEmits(['update:modelValue'])

	const selected: WritableComputedRef<GostBlockungSchiene> = computed({
		get: () => modelValue,
		set: (value) => emit('update:modelValue', value)
	});

</script>

