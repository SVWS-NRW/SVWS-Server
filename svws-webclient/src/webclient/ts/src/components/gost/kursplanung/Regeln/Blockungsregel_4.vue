<template>
	<BlockungsregelBase v-model="regel" @update:model-value="e => emit('update:modelValue', e)" :regel-typ="regel_typ" :regeln="regeln"
		@regel-hinzugefuegen="regel_hinzufuegen" @regel-speichern="emit('regelSpeichern')" @regel-entfernen="e=>emit('regelEntfernen', e)">
		<template #beschreibung="{ regel: r }">
			{{ name(r.parameter.get(0).valueOf()) }} in {{ getKursbezeichnung(r) }} fixiert
		</template>
		Fixiere
		<parameter-schueler v-model="schueler" :list-schueler="listSchueler" />
		in
		<parameter-kurs v-model="kurs" :data-faecher="dataFaecher" :blockung="blockung" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";
	import { useRegelParameterKurs, useRegelParameterSchueler, createKursbezeichnungsGetter } from '../composables';
	import { useKurse } from '../composables'
	import { useSchuelerListe } from "../../composables";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { ListAbiturjahrgangSchueler } from "~/apps/gost/ListAbiturjahrgangSchueler";

	const props = defineProps<{
		modelValue: GostBlockungRegel | undefined;
		dataFaecher: DataGostFaecher;
		blockung: DataGostKursblockung;
		listSchueler: ListAbiturjahrgangSchueler;
		regeln: GostBlockungRegel[];
	}>();

	const emit = defineEmits<{
		(e: 'update:modelValue', v: GostBlockungRegel | undefined): void;
		(e: 'regelSpeichern'): void;
		(e: 'regelEntfernen', v: GostBlockungRegel): void;
	}>()

	const regel: WritableComputedRef<GostBlockungRegel | undefined> = computed({
		get: () => props.modelValue,
		set: (value) => emit('update:modelValue', value)
	});

	const regel_typ = GostKursblockungRegelTyp.SCHUELER_FIXIEREN_IN_KURS

	const schueler = useRegelParameterSchueler(regel, 0)
	const kurs = useRegelParameterKurs(props.blockung, regel, 1)

	const schuelerliste = useSchuelerListe();
	const kurse = useKurse(props.blockung)
	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(schuelerliste[0].id)
		r.parameter.add(kurse.value.get(0).id)
		regel.value = r;
	}

	const name = (id: number) => {
		const schueler = schuelerliste.find(s => s.id === id);
		return schueler ? `${schueler.nachname}, ${schueler.vorname}` : "";
	}

	const getKursbezeichnung = createKursbezeichnungsGetter(props.blockung, 1)

</script>

