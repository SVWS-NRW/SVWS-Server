<template>
	<BlockungsregelBase v-model="regel" :regel-typ="regel_typ" @regel-hinzugefuegt="regel_hinzufuegen" :blockung="blockung">
		<template #beschreibung="{ regel: r }">
			{{ name(r.parameter.get(0).valueOf()) }} in {{ getKursbezeichnung(r) }} verboten
		</template>
		Verbiete
		<parameter-schueler v-model="schueler" :list-schueler="listSchueler" />
		in
		<parameter-kurs v-model="kurs" :data-faecher="dataFaecher" :blockung="blockung" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
	import { ShallowRef, shallowRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { ListAbiturjahrgangSchueler } from "~/apps/gost/ListAbiturjahrgangSchueler";
	import { useSchuelerListe } from '../../composables';
	import { createKursbezeichnungsGetter, useKurse, useRegelParameterKurs, useRegelParameterSchueler } from '../composables'

	const props = defineProps<{
		dataFaecher: DataGostFaecher;
		blockung: DataGostKursblockung;
		listSchueler: ListAbiturjahrgangSchueler;
	}>();

	const regel_typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS
	// public static readonly SCHUELER_VERBIETEN_IN_KURS : GostKursblockungRegelTyp =
	// new GostKursblockungRegelTyp("SCHUELER_VERBIETEN_IN_KURS", 5, 5, "Schüler: Verbiete in Kurs",
	// Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.KURS_ID));


	const regel: ShallowRef<GostBlockungRegel | undefined> = shallowRef(undefined)
	const schueler = useRegelParameterSchueler(regel, 0)
	const kurs = useRegelParameterKurs(props.blockung, regel, 1)

	const schuelerliste = useSchuelerListe()
	const kurse = useKurse(props.blockung)
	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(schuelerliste[0].id)
		r.parameter.add(kurse.value.get(0).id)
		regel.value = r;
	}

	const name = (id: number) => {
		const schueler = schuelerliste.find(s => s.id === id)
		return schueler ? `${schueler.nachname}, ${schueler.vorname}` : ""
	}

	const getKursbezeichnung = createKursbezeichnungsGetter(props.blockung, 1)

</script>
