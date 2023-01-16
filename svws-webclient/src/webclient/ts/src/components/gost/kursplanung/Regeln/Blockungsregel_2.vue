<template>
	<BlockungsregelBase v-model="regel" :regel-typ="regel_typ" @regel-hinzugefuegt="regel_hinzufuegen">
		<template #beschreibung="{ regel: r }">
			{{ getKursbezeichnung(r) }} auf Schiene {{ r.parameter.get(1) }} fixiert
		</template>
		Fixiere
		<parameter-kurs v-model="kurs" :data-faecher="dataFaecher" />
		in
		<parameter-schiene v-model="schiene" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
	import { ShallowRef, shallowRef } from "vue";
	import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
	import { createKursbezeichnungsGetter, useRegelParameterKurs, useRegelParameterSchiene  } from '../composables';
	import { useKurse } from '../composables'

	const { dataFaecher } = defineProps<{ 
		dataFaecher: DataGostFaecher;
	}>();

	const regel_typ = GostKursblockungRegelTyp.KURS_FIXIERE_IN_SCHIENE
	// public static readonly KURS_FIXIERE_IN_SCHIENE : GostKursblockungRegelTyp =
	// new GostKursblockungRegelTyp("KURS_FIXIERE_IN_SCHIENE", 2, 2, "Kurs: Fixiere in Schiene",
	//Arrays.asList(GostKursblockungRegelParameterTyp.KURS_ID, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	const regel: ShallowRef<GostBlockungRegel | undefined> = shallowRef(undefined)
	const kurs = useRegelParameterKurs(regel, 0)
	const schiene = useRegelParameterSchiene(regel, 1)


	const kurse = useKurse()
	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(kurse.value.get(0).id)
		r.parameter.add(1);
		regel.value = r;
	}

	const getKursbezeichnung = createKursbezeichnungsGetter()

</script>
