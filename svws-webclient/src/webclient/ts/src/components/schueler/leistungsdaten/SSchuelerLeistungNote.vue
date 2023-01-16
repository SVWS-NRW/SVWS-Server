<template>
	<span>
		<svws-ui-multi-select title="Note" v-model="inputNote" :items="Note.values()" :item-text="(item: Note) => item.kuerzel.toString()" headless />
	</span>
</template>

<script setup lang="ts">

	import { Note, SchuelerLeistungsdaten } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";
	import { DataSchuelerAbschnittsdaten } from "~/apps/schueler/DataSchuelerAbschnittsdaten";

	const props = defineProps<{
		data: DataSchuelerAbschnittsdaten,
		leistungsdaten: SchuelerLeistungsdaten
	}>();

	const inputNote: WritableComputedRef<Note | undefined> = computed({
		get: () => Note.fromKuerzel(props.leistungsdaten.note),
		set: (value) => props.data.patchLeistung({ note: value?.kuerzel }, props.leistungsdaten.id)
	});

</script>
