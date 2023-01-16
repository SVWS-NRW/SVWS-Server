<template>
	<BlockungsregelBase v-model="regel" :regel-typ="regel_typ" @regel-hinzugefuegt="regel_hinzufuegen" :blockung="blockung">
		<template #beschreibung="{ regel: r }">
			{{ GostKursart.fromID(r.parameter.get(0).valueOf()).beschreibung }} alleine in Schiene {{ r.parameter.get(1) }} bis {{ r.parameter.get(2) }}
		</template>
		Nur
		<parameter-kursart v-model="kursart" />
		von
		<parameter-schiene v-model="start" :blockung="blockung" />
		bis
		<parameter-schiene v-model="ende" :blockung="blockung" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">

	import { GostBlockungRegel, GostKursart, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
	import { ShallowRef, shallowRef } from "vue";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { useRegelParameterKursart, useRegelParameterSchiene } from '../composables';

	const props = defineProps<{
		blockung: DataGostKursblockung;
	}>();

	const regel_typ = GostKursblockungRegelTyp.KURSART_ALLEIN_IN_SCHIENEN_VON_BIS
	// public static readonly KURSART_ALLEIN_IN_SCHIENEN_VON_BIS : GostKursblockungRegelTyp =
	//new GostKursblockungRegelTyp("KURSART_ALLEIN_IN_SCHIENEN_VON_BIS", 2, 6, "Kursart: Allein in Schienen (von/bis)",
	//Arrays.asList(GostKursblockungRegelParameterTyp.KURSART, GostKursblockungRegelParameterTyp.SCHIENEN_NR, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	const regel: ShallowRef<GostBlockungRegel | undefined> = shallowRef(undefined)

	const kursart = useRegelParameterKursart(regel, 0)
	const start = useRegelParameterSchiene(props.blockung, regel, 1)
	const ende = useRegelParameterSchiene(props.blockung, regel, 2)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(1);
		r.parameter.add(1);
		r.parameter.add(1);
		regel.value = r;
	}

</script>
