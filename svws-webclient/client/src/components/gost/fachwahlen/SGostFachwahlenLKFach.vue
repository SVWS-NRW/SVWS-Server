<template>
	<svws-ui-table :items="[]" :no-data="false" class="select-none">
		<template #header>
			<div role="row" class="svws-ui-tr text-uistatic">
				<template v-if="fws !== undefined">
					<div role="cell" class="svws-ui-td col-span-full" :style="'background-color: ' + getBgColor(fws)">
						<span class="flex gap-1">
							<span :style="{ 'background-color': getBgColor(fws) }">{{ faecherManager.get(fws.id)?.bezeichnung }}</span>
							Leistungskurs
						</span>
					</div>
				</template>
			</div>
		</template>
		<template #body>
			<template v-if="fws !== undefined">
				<div role="row" class="svws-ui-tr text-ui">
					<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
						<div role="cell" class="svws-ui-td svws-align-center" v-if="fws.fachwahlen[halbjahr.id].wahlenLK > 0">
							{{ halbjahr.kuerzel }} ({{ fws.fachwahlen[halbjahr.id].wahlenLK }})
						</div>
					</template>
				</div>
				<div role="row" class="svws-ui-tr text-ui">
					<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
						<div role="cell" v-if="fws.fachwahlen[halbjahr.id].wahlenLK > 0" class="flex flex-col svws-ui-td mb-5 leading-tight !pl-4">
							<div v-for="schueler in getSchuelerListe(fws.id, halbjahr)" :key="schueler.id" class="flex gap-1 py-0.5 px-1 -mx-1 -mt-0.5 hover:bg-ui-75 rounded-sm cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
								<span class="icon i-ri-link" />
								<span class="line-clamp-1 break-all leading-tight -my-0.5" :title="schueler.nachname + ', ' + schueler.vorname">{{ schueler.nachname + ", " + schueler.vorname }}</span>
							</div>
						</div>
					</template>
				</div>
			</template>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostFachwahlenAbiturFachProps } from "./SGostFachwahlenAbiturFachProps";
	import { GostHalbjahr, Fach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList } from "@core";

	const props = defineProps<GostFachwahlenAbiturFachProps>();

	const schuljahr = computed<number>(() => props.faecherManager.getSchuljahr());

	const fws = computed<GostStatistikFachwahl | undefined>(() => {
		for (const f of props.fachwahlstatistik)
			if (f.id === props.fachID)
				return f;
		return undefined;
	});

	function getBgColor(fws: GostStatistikFachwahl) : string {
		if (fws.kuerzelStatistik === null)
			return 'rgb(220,220,220)';
		return Fach.getBySchluesselOrDefault(fws.kuerzelStatistik).getHMTLFarbeRGBA(schuljahr.value, 1.0);
	}

	function getSchuelerListe(idFach : number, halbjahr: GostHalbjahr) : List<SchuelerListeEintrag> {
		const result = new ArrayList<SchuelerListeEintrag>();
		for (const id of props.fachwahlenManager.schuelerGetMengeLKByFachAndHalbjahrAsListOrException(idFach, halbjahr)) {
			const schueler = props.mapSchueler.get(id);
			if (schueler !== undefined)
				result.add(schueler);
		}
		result.sort({ compare(a : SchuelerListeEintrag, b : SchuelerListeEintrag) : number {
			let cmp = a.nachname.localeCompare(b.nachname);
			if (cmp !== 0)
				return cmp;
			cmp = a.vorname.localeCompare(b.vorname);
			if (cmp !== 0)
				return cmp;
			return b.id - a.id;
		} });
		return result;
	}

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: repeat(4, 1fr);
	}

</style>
