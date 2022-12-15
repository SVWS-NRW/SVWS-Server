<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";
import { GostBlockungKurs, GostKursart } from "@svws-nrw/svws-core-ts";
import { computed } from "vue";

const main: Main = injectMainApp();
const app = main.apps.gost;
const faechermanager = app.dataFaecher.manager

const props = defineProps<{ modelValue: GostBlockungKurs; }>();
const emit = defineEmits(['update:modelValue'])
const selected = computed({
	get(): GostBlockungKurs {
		return props.modelValue;
	},
	set(val: GostBlockungKurs) {
		emit('update:modelValue', val);
	}
});

function text(i: GostBlockungKurs) {
	return `${faechermanager?.get(i.fach_id)?.kuerzel}-${i.kursart > 0 ? GostKursart.fromID(i.kursart) : 'kursart-fehlt' }${i.nummer}${i.suffix ? "-"+i.suffix:""}`
}
</script>

<template>
	<svws-ui-multi-select
		v-model="selected"
		:items="app.dataKursblockung.datenmanager?.getKursmengeSortiertNachKursartFachNummer() || []"
		:item-text="text"
		/>
</template>