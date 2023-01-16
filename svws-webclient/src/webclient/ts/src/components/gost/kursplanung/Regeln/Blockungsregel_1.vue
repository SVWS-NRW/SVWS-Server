<template>
	<BlockungsregelBase v-model="regel" :regel-typ="regel_typ" @regel-hinzugefuegt="regel_hinzufuegen">
		<template #beschreibung="{ regel: r }">
			{{ GostKursart.fromID(r.parameter.get(0)?.valueOf()).beschreibung }}, von Schiene {{ r.parameter.get(1) }} bis {{ r.parameter.get(2) }} gesperrt
		</template>
		Sperre
		<parameter-kursart v-model="kursart" />
		von
		<parameter-schiene v-model="start" />
		bis
		<parameter-schiene v-model="ende" />
	</BlockungsregelBase>
</template>

<script setup lang="ts">
	import { GostBlockungRegel, GostKursart, GostKursblockungRegelTyp } from "@svws-nrw/svws-core-ts";
	import { ShallowRef, shallowRef } from "vue";
	import { useRegelParameterKursart, useRegelParameterSchiene } from '../composables';

	const regel_typ = GostKursblockungRegelTyp.KURSART_SPERRE_SCHIENEN_VON_BIS
	//public static readonly KURSART_SPERRE_SCHIENEN_VON_BIS : GostKursblockungRegelTyp = 
	//new GostKursblockungRegelTyp("KURSART_SPERRE_SCHIENEN_VON_BIS", 1, 1, 
	//"Kursart: Sperre Schienen (von/bis)", Arrays.asList(GostKursblockungRegelParameterTyp.KURSART, GostKursblockungRegelParameterTyp.SCHIENEN_NR, GostKursblockungRegelParameterTyp.SCHIENEN_NR));

	const regel: ShallowRef<GostBlockungRegel | undefined> = shallowRef(undefined)
	const kursart = useRegelParameterKursart(regel, 0)
	const start = useRegelParameterSchiene(regel, 1)
	const ende = useRegelParameterSchiene(regel, 2)

	const regel_hinzufuegen = (r: GostBlockungRegel) => {
		r.parameter.add(1);
		r.parameter.add(1);
		r.parameter.add(1);
		regel.value = r;
	}
</script>

