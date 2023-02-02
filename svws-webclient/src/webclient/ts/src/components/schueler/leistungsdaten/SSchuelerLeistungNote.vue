<template>
	<span>
		<svws-ui-multi-select title="Note" v-model="inputNote" :items="Note.values()" :item-text="(item: Note) => item.kuerzel" headless />
	</span>
</template>

<script setup lang="ts">

	import { Note, SchuelerLeistungsdaten, SchuelerLernabschnittsdaten } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";

	const props = defineProps<{
		data: SchuelerLernabschnittsdaten;
		leistungsdaten: SchuelerLeistungsdaten
		patchLeistung: (data : Partial<SchuelerLeistungsdaten>, id : number) => Promise<void>;
	}>();

	const inputNote: WritableComputedRef<Note | undefined> = computed({
		get: () => Note.fromKuerzel(props.leistungsdaten.note),
		set: (value) => void props.patchLeistung({ note: value?.kuerzel }, props.leistungsdaten.id)
	});

</script>
