<template>
	<BlockungsregelBase v-model="regel" :regel-typ="regel_typ" @regel-hinzugefuegt="regel_hinzufuegen">
		<template #beschreibung="{ regel: r }">
			{{ kursbezeichnung1(r) }} nie zusammen mit {{ kursbezeichnung2(r) }}
		</template>
		<parameter-kurs v-model="kurs1" :data-faecher="dataFaecher" />
		nie zusammen mit
		<parameter-kurs v-model="kurs2" :data-faecher="dataFaecher" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
	import { ShallowRef, shallowRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { useKurse, useRegelParameterKurs, createKursbezeichnungsGetter } from '../composables';

	const { dataFaecher } = defineProps<{
		dataFaecher: DataGostFaecher;
	}>();

	const regel: ShallowRef<GostBlockungRegel | undefined> = shallowRef(undefined)
	const regel_typ = GostKursblockungRegelTyp.KURS_VERBIETEN_MIT_KURS
	//public static readonly KURS_VERBIETEN_MIT_KURS : GostKursblockungRegelTyp =
	//new GostKursblockungRegelTyp("KURS_VERBIETEN_MIT_KURS", 7, 7, "Kurs verbiete mit Kurs",
	//Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.KURS_ID));

	const kurse = useKurse()

	const kurs1 = useRegelParameterKurs(regel, 0)
	const kurs2 = useRegelParameterKurs(regel, 1)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(kurse.value.get(0).id)
		r.parameter.add(kurse.value.get(0).id);
		regel.value = r;
	}

	const kursbezeichnung1 = createKursbezeichnungsGetter(0)
	const kursbezeichnung2 = createKursbezeichnungsGetter(1)

</script>

