<template>
	<tr class="cursor-pointer px-2 text-left" :class="{ 'bg-red-400': kollision && !nichtwahl, 'bg-orange-400': nichtwahl && !kollision, 
			'bg-gradient-to-r':nichtwahl && kollision, 'from-red-400': nichtwahl && kollision, 'to-orange-400': nichtwahl && kollision}">
		<td class="px-2">
			<div class="flex justify-between">
				<span>
					{{ `${schueler.nachname}, ${schueler.vorname}`}} 
					<svws-ui-badge v-if="schueler.status !== 'Aktiv'" size="tiny" type="highlight"> {{schueler.status}} </svws-ui-badge>
				</span>
				<svws-ui-icon v-if="selected"> <i-ri-checkbox-blank-circle-fill class="text-blue-400"/> </svws-ui-icon>
			</div>
		</td>
	</tr>
</template>

<script setup lang="ts">

	import { GostBlockungsergebnisManager, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { ComputedRef, computed } from "vue";
	import { DataGostKursblockung } from "~/apps/gost/DataGostKursblockung";
	import { ListAbiturjahrgangSchueler } from "~/apps/gost/ListAbiturjahrgangSchueler";

	const props = defineProps<{ 
		schueler: SchuelerListeEintrag;
		selected: Boolean;
		blockung: DataGostKursblockung;
		listSchueler: ListAbiturjahrgangSchueler;
	}>();

	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> = computed(() => props.blockung.ergebnismanager);

	const kollision: ComputedRef<boolean> = computed(()=> {
		if (manager.value === undefined)
			return false;
		const kursid = props.listSchueler.filter.kurs?.id;
		if (kursid === undefined)
			return manager.value.getOfSchuelerHatKollision(props.schueler.id);
		return manager.value.getOfKursSchuelermengeMitKollisionen(kursid).contains(props.schueler.id);
	});

	const nichtwahl: ComputedRef<boolean> = computed(() =>
		(manager.value === undefined) ? false : manager.value.getOfSchuelerHatNichtwahl(props.schueler.id)
	);

</script>
