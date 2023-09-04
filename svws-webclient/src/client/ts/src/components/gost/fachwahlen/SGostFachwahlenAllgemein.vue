<template>
	<svws-ui-data-table :items="[]" :no-data="false" :columns="cols" panel-height overflow-x-hidden>
		<template #header>
			<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact select-none">
				<div role="cell" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate select-none col-span-3">
					Informationen zu den Schülern bei den einzelnen Fachwahlen
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
			<template v-for="fws in fachwahlenstatistik" :key="fws.id">
				<template v-if="fws !== undefined">
					<div role="row" class="data-table__tr data-table__tbody__tr cursor-pointer" :style="{ 'background-color': getBgColor(fws) }" @click="onClick(fws, undefined)">
						<div role="cell" class="data-table__td data-table__td__align-left select-none col-span-3 text-sm-bold">
							{{ faecherManager.get(fws.id)?.bezeichnung }}
						</div>
					</div>
					<template v-if="aktuell.fachwahl?.id === fws.id">
						<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
							<template v-if="hatFachwahl(fws, halbjahr)">
								<div role="row" class="data-table__tr data-table__tbody__tr cursor-pointer" @click="onClick(fws, halbjahr)">
									<div role="cell" class="data-table__td data-table__td__align-center col-span-3 select-none text-sm-bold bg-slate-100">
										{{ halbjahr.kuerzel }}
									</div>
								</div>
								<div role="row" class="data-table__tr data-table__tbody__tr cursor-pointer" @click="onClick(fws, halbjahr)">
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
								<div v-if="aktuell.halbjahr?.id === halbjahr.id" role="row" class="data-table__tr data-table__tbody__tr">
									<div role="cell" class="data-table__td data-table__td__align-left select-none flex flex-col" v-for="col in [1, 2, 3]" :key="col">
										<div v-for="schueler in getSchuelerListe(fws.id, halbjahr, col)" :key="schueler.id" class="w-full flex flex-row">
											{{ schueler.nachname + ", " + schueler.vorname }} <i-ri-link class="ml-2 cursor-pointer" @click="gotoLaufbahnplanung(schueler.id)" />
										</div>
									</div>
								</div>
							</template>
						</template>
					</template>
				</template>
			</template>
		</template>
	</svws-ui-data-table>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@svws-nrw/svws-ui";
	import type { GostFachwahlenAllgemeinProps } from "./SGostFachwahlenAllgemeinProps";
	import type { ComputedRef} from "vue";
	import { computed, ref } from "vue";
	import { ZulaessigesFach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList, GostHalbjahr } from "@core";

	const props = defineProps<GostFachwahlenAllgemeinProps>();

	const fachwahlenstatistik: ComputedRef<GostStatistikFachwahl[]> = computed(() => props.fachwahlstatistik.toArray() as GostStatistikFachwahl[]);

	type Auswahl = {
		fachwahl: GostStatistikFachwahl | undefined;
		halbjahr: GostHalbjahr | undefined;
	}

	const aktuell = ref<Auswahl>({
		fachwahl: fachwahlenstatistik.value.length === 0 ? undefined : fachwahlenstatistik.value.at(0),
		halbjahr: GostHalbjahr.EF1,
	});

	function onClick(fws : GostStatistikFachwahl, halbjahr: GostHalbjahr | undefined): void {
		if (fws.id === aktuell.value.fachwahl?.id) {
			// Das gleiche Fach wurde angeklickt...
			if (halbjahr === undefined) { // Klick auf das Fach
				if (aktuell.value.halbjahr == undefined) {
					// Das Fach war zuvor nicht ausgewählt und muss daher aufgeklappt werden
					aktuell.value = { fachwahl: fws, halbjahr: GostHalbjahr.EF1 };   // TODO Bestimmung des ersten Halbjahres mit Fachwahlen
				} else {
					// Das Fach war zuvor ausgewählt und muss daher zusammengeklappt werden
					aktuell.value = { fachwahl: undefined, halbjahr: undefined };
				}
			} else { // Klick auf das Halbjahr
				if (aktuell.value.halbjahr?.id === halbjahr.id) {
					// Bei dem Fach wurde das gleiche Halbjahr angeklickt, dieses muss zusammengeklappt werden
					aktuell.value = { fachwahl: fws, halbjahr: undefined };
				} else {
					// Bei dem Fach wurde ein anderes Halbjahr angeklickt, dieses muss aufgeklappt werden
					aktuell.value = { fachwahl: fws, halbjahr: halbjahr };
				}
			}
		} else {
			// Ein anderes Fach wurde angeklickt...
			if (halbjahr === undefined) { // Klick auf das Fach
				aktuell.value = { fachwahl: fws, halbjahr: GostHalbjahr.EF1 };   // TODO Bestimmung des ersten Halbjahres mit Fachwahlen
			} else { // Klick auf das Halbjahr
				aktuell.value = { fachwahl: fws, halbjahr: halbjahr };
			}
		}
	}

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

	function getSchuelerListe(idFach : number, halbjahr: GostHalbjahr, col: number) : List<SchuelerListeEintrag> {
		const result = new ArrayList<SchuelerListeEintrag>();
		const schuelermenge = col === 1
			? props.fachwahlenManager.schuelerGetMengeGKByFachAndHalbjahrAsListOrException(idFach, halbjahr) : col === 2
				? props.fachwahlenManager.schuelerGetMengeGKSchriftlichByFachAndHalbjahrAsListOrException(idFach, halbjahr)
				: props.fachwahlenManager.schuelerGetMengeGKMuendlichByFachAndHalbjahrAsListOrException(idFach, halbjahr)
		for (const id of schuelermenge) {
			const schueler = props.mapSchueler.get(id);
			if (schueler !== undefined)
				result.add(schueler);
		}
		return doSortSchuelerListeByNachnameAndVornameAndId(result);
	}

	function hatFachwahl(fws: GostStatistikFachwahl, halbjahr: GostHalbjahr): boolean {
		return !props.fachwahlenManager.schuelerGetMengeGKByFachAndHalbjahrAsListOrException(fws.id, halbjahr).isEmpty();
	}

</script>
