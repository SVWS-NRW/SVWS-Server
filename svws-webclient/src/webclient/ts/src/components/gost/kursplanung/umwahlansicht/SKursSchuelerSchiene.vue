<template>
	<tr class="px-2 text-left" :class="{ 'bg-red-400': schiene_hat_kollisionen }">
		<td class="bg-slate-100 px-2">
			{{ schiene_g?.bezeichnung }} <br />
			{{ schiene.kurse.size() }} Kurse <br />
			{{ anzahl_schueler }} Schüler
		</td>
		<s-kurs-schueler-schiene-kurs v-for="kurs of getSchieneKurse" :key="kurs.hashCode()" :kurs="kurs" :schueler="selected"
			:blockung="blockung" :ergebnis="ergebnis" :allow_regeln="allow_regeln" />
	</tr>
</template>

<script setup lang="ts">

	import { GostBlockungSchiene, GostBlockungsergebnisKurs, GostBlockungsergebnisManager,
		GostBlockungsergebnisSchiene, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { DataGostKursblockungsergebnis } from "~/apps/gost/DataGostKursblockungsergebnis";

	const props = defineProps<{ 
		schiene: GostBlockungsergebnisSchiene;
		selected: SchuelerListeEintrag;
		blockung: DataGostKursblockung;
		ergebnis: DataGostKursblockungsergebnis;
		allow_regeln: boolean;
	}>();

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => props.blockung.ergebnismanager);

	const anzahl_schueler: ComputedRef<number> = computed(() => manager.value?.getOfSchieneAnzahlSchueler(props.schiene.id) || 0);

	const schiene_g: ComputedRef<GostBlockungSchiene | undefined> = computed(() => manager.value?.getSchieneG(props.schiene.id))

	const schiene_hat_kollisionen: ComputedRef<boolean> = computed(() => {
		if (manager.value === undefined)
			return false;
		return manager.value.getOfSchieneSchuelermengeMitKollisionen(props.schiene.id).contains(props.selected.id);
	});

	const getSchieneKurse: ComputedRef<Vector<GostBlockungsergebnisKurs>> = computed(()=> props.schiene.kurse)

</script>
