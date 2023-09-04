<template>
	<svws-ui-data-table :items="[]" :no-data="false" :columns="cols" panel-height overflow-x-hidden>
		<template #header>
			<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact select-none">
				<div role="cell" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate select-none col-span-3">
					Informationen zu den Fachwahlen in {{ faecherManager.get(props.fachID)?.bezeichnung || "&ndash;" }} in der {{ halbjahr.kuerzel }}
				</div>
			</div>
			<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact select-none">
				<div role="cell" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate select-none">
					GK (gesamt)
				</div>
				<div role="cell" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate select-none">
					GK (schriftlich)
				</div>
				<div role="cell" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate select-none">
					GK (mündlich)
				</div>
			</div>
		</template>
		<template #body>
			<template v-if="fws !== undefined && hatFachwahl(fws)">
				<div role="row" class="data-table__tr data-table__tbody__tr" :style="{ 'background-color': getBgColor(fws) }">
					<div role="cell" class="data-table__td data-table__td__align-left select-none col-span-3 text-sm-bold">
						{{ faecherManager.get(fws.id)?.bezeichnung }}
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__tbody__tr">
					<div role="cell" class="data-table__td data-table__td__align-center select-none text-sm-bold">
						{{ fws.fachwahlen[halbjahr.id].wahlenGK > 0 ? fws.fachwahlen[halbjahr.id].wahlenGK : "&ndash;" }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center select-none text-sm-bold">
						{{ fws.fachwahlen[halbjahr.id].wahlenGKSchriftlich > 0 ? fws.fachwahlen[halbjahr.id].wahlenGKSchriftlich : "&ndash;" }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center select-none text-sm-bold">
						{{ fws.fachwahlen[halbjahr.id].wahlenGKMuendlich > 0 ? fws.fachwahlen[halbjahr.id].wahlenGKMuendlich : "&ndash;" }}
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__tbody__tr">
					<div role="cell" class="data-table__td data-table__td__align-left select-none flex flex-col" v-for="col in [1, 2, 3]" :key="col">
						<div v-for="schueler in getSchuelerListe(fws.id, col)" :key="schueler.id" class="w-full">
							{{ schueler.nachname + ", " + schueler.vorname }}
						</div>
					</div>
				</div>
			</template>
		</template>
	</svws-ui-data-table>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@svws-nrw/svws-ui";
	import type { GostFachwahlenFachHalbjahrProps } from "./SGostFachwahlenFachHalbjahrProps";
	import type { ComputedRef} from "vue";
	import { computed } from "vue";
	import { ZulaessigesFach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList } from "@core";

	const props = defineProps<GostFachwahlenFachHalbjahrProps>();

	const fws: ComputedRef<GostStatistikFachwahl | undefined> = computed(() => {
		for (const f of props.fachwahlstatistik)
			if (f.id === props.fachID)
				return f;
		return undefined;
	});

	const cols: DataTableColumn[] = [
		{ key: "GK", label: "GK", span: 1, minWidth: 4 },
		{ key: "GKS", label: "GKS", span: 1, minWidth: 4 },
		{ key: "GKM", label: "GKM", span: 1, minWidth: 4 },
	];

	const getBgColor = (fws: GostStatistikFachwahl) => ZulaessigesFach.getByKuerzelASD(fws.kuerzelStatistik).getHMTLFarbeRGBA(1.0);

	function doSortSchuelerListeByNachnameAndVornameAndId(liste : List<SchuelerListeEintrag>): List<SchuelerListeEintrag> {
		liste.sort({ compare(a : SchuelerListeEintrag, b : SchuelerListeEintrag) : number {
			let cmp = a.nachname.localeCompare(b.nachname);
			if (cmp !== 0)
				return cmp;
			cmp = a.vorname.localeCompare(b.vorname);
			if (cmp !== 0)
				return cmp;
			return b.id - a.id;
		} });
		return liste;
	}

	function getSchuelerListe(idFach : number, col: number) : List<SchuelerListeEintrag> {
		const result = new ArrayList<SchuelerListeEintrag>();
		const schuelermenge = col === 1
			? props.fachwahlenManager.schuelerGetMengeGKByFachAndHalbjahrAsListOrException(idFach, props.halbjahr) : col === 2
				? props.fachwahlenManager.schuelerGetMengeGKSchriftlichByFachAndHalbjahrAsListOrException(idFach, props.halbjahr)
				: props.fachwahlenManager.schuelerGetMengeGKMuendlichByFachAndHalbjahrAsListOrException(idFach, props.halbjahr)
		for (const id of schuelermenge) {
			const schueler = props.mapSchueler.get(id);
			if (schueler !== undefined)
				result.add(schueler);
		}
		return doSortSchuelerListeByNachnameAndVornameAndId(result);
	}

	function hatFachwahl(fws: GostStatistikFachwahl): boolean {
		return !props.fachwahlenManager.schuelerGetMengeGKByFachAndHalbjahrAsListOrException(fws.id, props.halbjahr).isEmpty();
	}

</script>
