<template>
	<svws-ui-data-table :items="[]" :no-data="false" :columns="cols" panel-height overflow-x-hidden>
		<template #header>
			<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact select-none">
				<div role="cell" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate select-none col-span-3">
					Informationen zu den Abiturfachwahlen der Schüler
				</div>
			</div>
			<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact select-none">
				<div role="cell" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate select-none">
					Leistungskurs
				</div>
				<div role="cell" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate select-none">
					3. Abiturfach
				</div>
				<div role="cell" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate select-none">
					4. Abiturfach
				</div>
			</div>
		</template>
		<template #body>
			<template v-for="fws in fachwahlenstatistik" :key="fws.id">
				<template v-if="hatAbiFachwahl(fws)">
					<div role="row" class="data-table__tr data-table__tbody__tr cursor-pointer" :style="{ 'background-color': getBgColor(fws) }" @click="onClick(fws)">
						<div role="cell" class="data-table__td data-table__td__align-left select-none col-span-3 text-sm-bold">
							{{ faecherManager.get(fws.id)?.bezeichnung }}
						</div>
					</div>
					<div role="row" class="data-table__tr data-table__tbody__tr cursor-pointer" @click="onClick(fws)">
						<div role="cell" class="data-table__td data-table__td__align-center select-none text-sm-bold">
							{{ fws.fachwahlen[5].wahlenLK > 0 ? fws.fachwahlen[5].wahlenLK : "&ndash;" }}
						</div>
						<div role="cell" class="data-table__td data-table__td__align-center select-none text-sm-bold">
							{{ fws.wahlenAB3 > 0 ? fws.wahlenAB3 : "&ndash;" }}
						</div>
						<div role="cell" class="data-table__td data-table__td__align-center select-none text-sm-bold">
							{{ fws.wahlenAB4 > 0 ? fws.wahlenAB4 : "&ndash;" }}
						</div>
					</div>
					<div v-if="aktuell?.id === fws.id" role="row" class="data-table__tr data-table__tbody__tr">
						<div role="cell" class="data-table__td data-table__td__align-left select-none flex flex-col" v-for="abifach in [GostAbiturFach.LK1, GostAbiturFach.AB3, GostAbiturFach.AB4]" :key="abifach.id">
							<div v-for="schueler in getSchuelerListe(fws.id, abifach)" :key="schueler.id" class="w-full flex flex-row">
								{{ schueler.nachname + ", " + schueler.vorname }} <i-ri-link class="ml-2 cursor-pointer" @click="gotoLaufbahnplanung(schueler.id)" />
							</div>
						</div>
					</div>
				</template>
			</template>
		</template>
	</svws-ui-data-table>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { GostFachwahlenAbiturProps } from "./SGostFachwahlenAbiturProps";
	import type { ComputedRef} from "vue";
	import { computed, ref } from "vue";
	import { GostAbiturFach, ZulaessigesFach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList } from "@core";

	const props = defineProps<GostFachwahlenAbiturProps>();

	const fachwahlenstatistik: ComputedRef<GostStatistikFachwahl[]> = computed(() => props.fachwahlstatistik.toArray() as GostStatistikFachwahl[]);

	const aktuell = ref<GostStatistikFachwahl | undefined>(fachwahlenstatistik.value.length === 0 ? undefined : fachwahlenstatistik.value.at(0));

	const cols: DataTableColumn[] = [
		{ key: "LK", label: "LK", span: 1, minWidth: 4 },
		{ key: "AB3", label: "AB3", span: 1, minWidth: 4 },
		{ key: "AB4", label: "Ab4", span: 1, minWidth: 4 },
	];

	const getBgColor = (fws: GostStatistikFachwahl) => ZulaessigesFach.getByKuerzelASD(fws.kuerzelStatistik).getHMTLFarbeRGBA(1.0);

	function onClick(fws: GostStatistikFachwahl): void {
		aktuell.value = (aktuell.value?.id === fws.id) ? undefined : fws;
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
