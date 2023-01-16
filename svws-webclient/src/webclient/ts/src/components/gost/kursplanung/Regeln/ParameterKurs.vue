<template>
	<svws-ui-multi-select v-model="selected" :items="app.dataKursblockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || []" :item-text="text" />
</template>

<script setup lang="ts">

	import { injectMainApp, Main } from "~/apps/Main";
	import { GostBlockungKurs, GostKursart } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";

	const { modelValue, dataFaecher } = defineProps<{
		modelValue: GostBlockungKurs;
		dataFaecher: DataGostFaecher;
	}>();

	const main: Main = injectMainApp();
	const app = main.apps.gost;
	const faechermanager = dataFaecher.manager

	const emit = defineEmits(['update:modelValue'])
	const selected: WritableComputedRef<GostBlockungKurs> = computed({
		get: () => modelValue,
		set: (value: GostBlockungKurs) => emit('update:modelValue', value)
	});

	function text(i: GostBlockungKurs) {
		return `${faechermanager?.get(i.fach_id)?.kuerzel}-${i.kursart > 0 ? GostKursart.fromID(i.kursart) : 'kursart-fehlt' }${i.nummer}${i.suffix ? "-"+i.suffix:""}`
	}

</script>
