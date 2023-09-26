<template>
	<svws-ui-table :items="[]" :no-data="false" :columns="cols" class="select-none">
		<template #header>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td col-span-full">
					<span class="flex gap-1">
						<template v-if="fws !== undefined">
							<span class="svws-ui-badge" :style="{ '--background-color': getBgColor(fws) }">{{ faecherManager.get(fws.id)?.bezeichnung }}</span>
						</template>
						im Abitur
					</span>
				</div>
			</div>
		</template>
		<template #body>
			<template v-if="fws !== undefined && hatAbiFachwahl(fws)">
				<div role="row" class="svws-ui-tr">
					<div role="cell" class="svws-ui-td svws-align-center">
						<i-ri-draft-line class="text-sm -my-0.5" />
						<span v-if="fws.fachwahlen[5].wahlenLK > 0">Leistungskurs ({{ fws.fachwahlen[5].wahlenLK }})</span>
						<span v-else class="opacity-25">Leistungskurs (—)</span>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center">
						<i-ri-draft-line class="text-sm -my-0.5" />
						<span v-if="fws.wahlenAB3 > 0">3. Abiturfach ({{ fws.wahlenAB3 }})</span>
						<span v-else class="opacity-25">3. Abiturfach (—)</span>
					</div>
					<div role="cell" class="svws-ui-td svws-align-center">
						<i-ri-speak-line class="text-sm -my-0.5" />
						<span v-if="fws.wahlenAB4 > 0">4. Abiturfach ({{ fws.wahlenAB4 }})</span>
						<span v-else class="opacity-25">4. Abiturfach (—)</span>
					</div>
				</div>
				<div role="row" class="svws-ui-tr">
					<div role="cell" class="flex flex-col svws-ui-td mb-5 leading-tight" v-for="abifach in [GostAbiturFach.LK1, GostAbiturFach.AB3, GostAbiturFach.AB4]" :key="abifach.id">
						<div v-for="schueler in getSchuelerListe(fws.id, abifach)" :key="schueler.id" class="flex gap-1 py-0.5 px-1 -mx-1 -mt-0.5 hover:bg-black/10 dark:hover:bg-white/10 rounded cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
							<i-ri-link class="text-sm" />
							<span class="line-clamp-1 break-all leading-tight -my-0.5" :title="schueler.nachname + ', ' + schueler.vorname">{{ schueler.nachname + ", " + schueler.vorname }}</span>
						</div>
					</div>
				</div>
			</template>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { GostFachwahlenAbiturFachProps } from "./SGostFachwahlenAbiturFachProps";
	import type { ComputedRef} from "vue";
	import { computed } from "vue";
	import { GostAbiturFach, ZulaessigesFach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList } from "@core";

	const props = defineProps<GostFachwahlenAbiturFachProps>();

	const fws: ComputedRef<GostStatistikFachwahl | undefined> = computed(() => {
		for (const f of props.fachwahlstatistik)
			if (f.id === props.fachID)
				return f;
		return undefined;
	});

	const cols: DataTableColumn[] = [
		{ key: "LK", label: "LK", span: 1 },
		{ key: "AB3", label: "AB3", span: 1 },
		{ key: "AB4", label: "Ab4", span: 1 },
	];

	const getBgColor = (fws: GostStatistikFachwahl) => ZulaessigesFach.getByKuerzelASD(fws.kuerzelStatistik).getHMTLFarbeRGBA(1.0);

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
