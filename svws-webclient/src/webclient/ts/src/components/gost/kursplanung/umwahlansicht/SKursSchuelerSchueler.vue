<template>
	<tr
		class="cursor-pointer border border-[#7f7f7f]/20 px-2 text-left"
		:class="{ 'bg-red-400': kollision && !nichtwahl, 'bg-orange-400': nichtwahl && !kollision, 'bg-gradient-to-r':nichtwahl && kollision,'from-red-400': nichtwahl && kollision, 'to-orange-400': nichtwahl && kollision}"
	>
		<td class="px-2">
			<div class="flex justify-between">
				<span>{{ `${schueler.nachname}, ${schueler.vorname}`}} <svws-ui-badge v-if="schueler.status !== 'Aktiv'" size="tiny" variant="highlight">{{schueler.status}}</svws-ui-badge></span>
				<svws-ui-icon v-if="selected"><i-ri-checkbox-blank-circle-fill class="text-blue-400"/></svws-ui-icon>
			</div>
		</td>
	</tr>
</template>

<script setup lang="ts">
	import { GostBlockungsergebnisManager, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { ComputedRef, computed } from "vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const {schueler} = defineProps({
		schueler: {
			type: SchuelerListeEintrag,
			required: true
		},
		selected: { type: Boolean, required: true }
	});

	const main: Main = injectMainApp();
	const app = main.apps.gost;
	
	const manager: ComputedRef<GostBlockungsergebnisManager | undefined> =
		computed(() => app.dataKursblockung.ergebnismanager);

	const kollision: ComputedRef<boolean> =
		computed(()=> {
			if (manager.value === undefined)
				return false;
			const kursid = app.listAbiturjahrgangSchueler.filter.kursid;
			if (kursid === undefined)
				return manager.value.getOfSchuelerHatKollision(schueler.id);
			return manager.value.getOfKursSchuelermengeMitKollisionen(kursid).contains(schueler.id);
		});

	const nichtwahl: ComputedRef<boolean> = 
		computed(() => {
			if (manager.value === undefined)
				return false;
			return manager.value.getOfSchuelerHatNichtwahl(schueler.id);
		});
</script>
