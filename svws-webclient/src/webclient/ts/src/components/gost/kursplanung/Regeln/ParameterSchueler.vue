<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";
import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed } from "vue";

const main: Main = injectMainApp();
const app = main.apps.gost;

const { modelValue } = defineProps<{ modelValue: SchuelerListeEintrag; }>();
const emit = defineEmits(['update:modelValue'])
const selected = computed({
	get(): SchuelerListeEintrag {
		return modelValue;
	},
	set(val: SchuelerListeEintrag) {
		emit('update:modelValue', val);
	}
});
const filter = (
	items: SchuelerListeEintrag[],
	search: string
) => {
	return items.filter((i: SchuelerListeEintrag) => {
		if (i) {
			return (
				i.vorname
					.toLocaleLowerCase()
					.includes(search.toLocaleLowerCase()) ||
				i.nachname
					.toLocaleLowerCase()
					.includes(search.toLocaleLowerCase())
			);
		}
		return [];
	});
};

const sort = (
	a: SchuelerListeEintrag,
	b: SchuelerListeEintrag
) => {
	if (a.nachname && b.nachname) {
		return a.nachname.localeCompare(
			b.nachname.toString()
		);
	} else if (a.nachname && !b.nachname) {
		return -1;
	} else if (!a.nachname && !b.nachname) {
		return 1;
	}
	return 0;
};
</script>
	
	<template>
	<svws-ui-multi-select 
		v-model="selected"
		:items="app.listAbiturjahrgangSchueler.liste"
		:item-text="(i: SchuelerListeEintrag) => `${i.nachname}, ${i.vorname}`"
		:item-filter="filter"
		:item-sort="sort"
		autocomplete
		/>
</template>