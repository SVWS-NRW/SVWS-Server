<template>
	<svws-ui-multi-select v-model="selected" :items="props.blockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || []" :item-text="text" />
</template>

<script setup lang="ts">

	import { GostBlockungKurs, GostKursart } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";

	const props = defineProps<{
		modelValue: GostBlockungKurs;
		dataFaecher: DataGostFaecher;
		blockung: DataGostKursblockung;
	}>();


	const emit = defineEmits(['update:modelValue'])
	const selected: WritableComputedRef<GostBlockungKurs> = computed({
		get: () => props.modelValue,
		set: (value: GostBlockungKurs) => emit('update:modelValue', value)
	});

	function text(i: GostBlockungKurs) {
		return `${props.dataFaecher.manager?.get(i.fach_id)?.kuerzel}-${i.kursart > 0 ? GostKursart.fromID(i.kursart) : 'kursart-fehlt' }${i.nummer}${i.suffix ? "-"+i.suffix:""}`
	}

</script>
