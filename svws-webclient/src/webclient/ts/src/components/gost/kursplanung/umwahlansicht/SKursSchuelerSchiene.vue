<template>
	<tr :class="{ 'bg-error': schiene_hat_kollisionen }">
		<td class="border-r-2 border-black/25">
			<div class="flex flex-col py-1">
				<span class="font-bold">{{ schiene_g?.bezeichnung }}</span>
				<span class="text-sm">{{ schiene.kurse.size() }} Kurse</span>
				<span class="text-sm">{{ anzahl_schueler }} Schüler</span>
			</div>
		</td>
		<s-kurs-schueler-schiene-kurs v-for="kurs of getSchieneKurse" :key="kurs.hashCode()" :kurs="kurs" :schueler="selected"
			:blockung="blockung" :pending="pending" :allow-regeln="allowRegeln" :update-kurs-schueler-zuordnung="updateKursSchuelerZuordnung" />
	</tr>
</template>

<script setup lang="ts">

	import { GostBlockungSchiene, GostBlockungsergebnisKurs, GostBlockungsergebnisManager,
		GostBlockungsergebnisSchiene, SchuelerListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";

	const props = defineProps<{
		updateKursSchuelerZuordnung: (idSchueler: number, idKursNeu: number, idKursAlt: number) => Promise<boolean>;
		schiene: GostBlockungsergebnisSchiene;
		selected: SchuelerListeEintrag;
		blockung: DataGostKursblockung;
		pending: boolean;
		allowRegeln: boolean;
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
