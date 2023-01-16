<template>
	<BlockungsregelBase v-model="regel" :regel-typ="regel_typ" @regel-hinzugefuegt="regel_hinzufuegen">
		<template #beschreibung="{ regel: r }">
			{{ name(r.parameter.get(0).valueOf()) }} in {{ getKursbezeichnung(r) }} verboten
		</template>
		Verbiete
		<parameter-schueler v-model="schueler" />
		in
		<parameter-kurs v-model="kurs" :data-faecher="dataFaecher" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
	import { ShallowRef, shallowRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { useSchuelerListe } from '../../composables';
	import { createKursbezeichnungsGetter, useKurse, useRegelParameterKurs, useRegelParameterSchueler } from '../composables'

	const { dataFaecher } = defineProps<{
		dataFaecher: DataGostFaecher;
	}>();

	const regel_typ = GostKursblockungRegelTyp.SCHUELER_VERBIETEN_IN_KURS
	// public static readonly SCHUELER_VERBIETEN_IN_KURS : GostKursblockungRegelTyp =
	// new GostKursblockungRegelTyp("SCHUELER_VERBIETEN_IN_KURS", 5, 5, "Schüler: Verbiete in Kurs",
	// Arrays.asList(GostKursblockungRegelParameterTyp.SCHUELER_ID, GostKursblockungRegelParameterTyp.KURS_ID));


	const regel: ShallowRef<GostBlockungRegel | undefined> = shallowRef(undefined)
	const schueler = useRegelParameterSchueler(regel, 0)
	const kurs = useRegelParameterKurs(regel, 1)

	const schuelerliste = useSchuelerListe()
	const kurse = useKurse()
	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(schuelerliste[0].id)
		r.parameter.add(kurse.value.get(0).id)
		regel.value = r;
	}

	const name = (id: number) => {
		const schueler = schuelerliste.find(s => s.id === id)
		return schueler ? `${schueler.nachname}, ${schueler.vorname}` : ""
	}

	const getKursbezeichnung = createKursbezeichnungsGetter(1)

</script>
