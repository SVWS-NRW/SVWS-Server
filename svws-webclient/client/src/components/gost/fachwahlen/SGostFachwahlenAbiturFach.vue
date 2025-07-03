<template>
	<svws-ui-table :items="[]" :no-data="false" class="select-none">
		<template #header>
			<div role="row" class="svws-ui-tr text-uistatic">
				<template v-if="fws !== undefined">
					<div role="cell" class="svws-ui-td col-span-full" :style="'background-color: ' + getBgColor(fws)">
						<span class="flex gap-1">
							<span :style="{ 'background-color': getBgColor(fws) }">{{ faecherManager.get(fws.id)?.bezeichnung }}</span>
							im Abitur
						</span>
					</div>
				</template>
			</div>
		</template>
		<template #body>
			<template v-if="fws !== undefined && hatAbiFachwahl(fws)">
				<div role="row" class="svws-ui-tr text-ui">
					<div role="cell" class="svws-ui-td svws-align-center">
						<span class="icon i-ri-draft-line  -my-0.5" />
						<span v-if="fws.fachwahlen[5].wahlenLK > 0">Leistungskurs ({{ fws.fachwahlen[5].wahlenLK }})</span>
						<span v-else class="opacity-25">Leistungskurs (—)</span>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center">
						<span class="icon i-ri-draft-line  -my-0.5" />
						<span v-if="fws.wahlenAB3 > 0">3. Abiturfach ({{ fws.wahlenAB3 }})</span>
						<span v-else class="opacity-25">3. Abiturfach (—)</span>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center">
						<span class="icon i-ri-speak-line  -my-0.5" />
						<span v-if="fws.wahlenAB4 > 0">4. Abiturfach ({{ fws.wahlenAB4 }})</span>
						<span v-else class="opacity-25">4. Abiturfach (—)</span>
					</div>
				</div>
				<div role="row" class="svws-ui-tr text-ui">
					<div role="cell" class="flex flex-col svws-ui-td mb-5 leading-tight" v-for="abifach in [GostAbiturFach.LK1, GostAbiturFach.AB3, GostAbiturFach.AB4]" :key="abifach.id">
						<div v-for="schueler in getSchuelerListe(fws.id, abifach)" :key="schueler.id" class="flex gap-1 py-0.5 px-1 -mx-1 -mt-0.5 hover:bg-ui-75 rounded-sm cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
							<span class="icon i-ri-link" />
							<span class="line-clamp-1 break-all leading-tight -my-0.5" :title="schueler.nachname + ', ' + schueler.vorname">{{ schueler.nachname + ", " + schueler.vorname }}</span>
						</div>
					</div>
				</div>
			</template>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostFachwahlenAbiturFachProps } from "./SGostFachwahlenAbiturFachProps";
	import { GostAbiturFach, Fach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList } from "@core";

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

	function getSchuelerListe(idFach : number, abifach: GostAbiturFach) : List<SchuelerListeEintrag> {
		const result = new ArrayList<SchuelerListeEintrag>();
		for (const id of props.fachwahlenManager.schuelerGetMengeByFachAndAbifachAsListOrException(idFach, abifach)) {
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

	function hatAbiFachwahl(fws: GostStatistikFachwahl): boolean {
		if ((props.fachwahlenManager.schuelerGetMengeByFachAndAbifachAsListOrException(fws.id, GostAbiturFach.LK1).isEmpty())
			&& (props.fachwahlenManager.schuelerGetMengeByFachAndAbifachAsListOrException(fws.id, GostAbiturFach.AB3).isEmpty())
			&& (props.fachwahlenManager.schuelerGetMengeByFachAndAbifachAsListOrException(fws.id, GostAbiturFach.AB4).isEmpty()))
			return false;
		return true;
	}

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: repeat(3, 1fr);
	}

</style>
