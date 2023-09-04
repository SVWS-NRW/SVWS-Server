<template>
	<svws-ui-data-table :items="[]" :no-data="false" :columns="cols" panel-height overflow-x-hidden>
		<template #header>
			<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact select-none">
				<div role="cell" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate select-none col-span-3">
					Informationen zu den Schülern bei den Leistungskursen
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
							<template v-if="fws.fachwahlen[halbjahr.id].wahlenLK > 0">
								<div role="row" class="data-table__tr data-table__tbody__tr cursor-pointer" @click="onClick(fws, halbjahr)">
									<div role="cell" class="data-table__td data-table__td__align-center col-span-3 select-none text-sm-bold bg-slate-100">
										{{ halbjahr.kuerzel }} ({{ fws.fachwahlen[halbjahr.id].wahlenLK }})
									</div>
								</div>
								<div v-if="aktuell.halbjahr?.id === halbjahr.id" role="row" class="data-table__tr data-table__tbody__tr">
									<div role="cell" class="data-table__td data-table__td__align-left select-none flex flex-col">
										<div v-for="schueler in getSchuelerListe(fws.id, halbjahr)" :key="schueler.id" class="w-full">
											{{ schueler.nachname + ", " + schueler.vorname }}
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
	import type { GostFachwahlenLeistungskurseProps } from "./SGostFachwahlenLeistungskurseProps";
	import type { ComputedRef} from "vue";
	import { computed, ref } from "vue";
	import { ZulaessigesFach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList, GostHalbjahr } from "@core";

	const props = defineProps<GostFachwahlenLeistungskurseProps>();

	const fachwahlenstatistik: ComputedRef<GostStatistikFachwahl[]> = computed(() => {
		const result = new Array<GostStatistikFachwahl>();
		for (const fws of props.fachwahlstatistik)
			if (hatLeistungskurs(fws))
				result.push(fws);
		return result;
	});

	type Auswahl = {
		fachwahl: GostStatistikFachwahl | undefined;
		halbjahr: GostHalbjahr | undefined;
	}

	const aktuell = ref<Auswahl>({
		fachwahl: fachwahlenstatistik.value.length === 0 ? undefined : fachwahlenstatistik.value.at(0),
		halbjahr: GostHalbjahr.Q21,
	});

	function onClick(fws : GostStatistikFachwahl, halbjahr: GostHalbjahr | undefined): void {
		if (fws.id === aktuell.value.fachwahl?.id) {
			// Das gleiche Fach wurde angeklickt...
			if (halbjahr === undefined) { // Klick auf das Fach
				if (aktuell.value.halbjahr == undefined) {
					// Das Fach war zuvor nicht ausgewählt und muss daher aufgeklappt werden
					aktuell.value = { fachwahl: fws, halbjahr: GostHalbjahr.Q22 };
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
				aktuell.value = { fachwahl: fws, halbjahr: GostHalbjahr.Q22 };
			} else { // Klick auf das Halbjahr
				aktuell.value = { fachwahl: fws, halbjahr: halbjahr };
			}
		}
	}

	const cols: DataTableColumn[] = [
		{ key: "LK", label: "LK", span: 1, minWidth: 4 },
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

	function getSchuelerListe(idFach : number, halbjahr: GostHalbjahr) : List<SchuelerListeEintrag> {
		const result = new ArrayList<SchuelerListeEintrag>();
		const schuelermenge = props.fachwahlenManager.schuelerGetMengeLKByFachAndHalbjahrAsListOrException(idFach, halbjahr);
		for (const id of schuelermenge) {
			const schueler = props.mapSchueler.get(id);
			if (schueler !== undefined)
				result.add(schueler);
		}
		return doSortSchuelerListeByNachnameAndVornameAndId(result);
	}

	function hatLeistungskurs(fws: GostStatistikFachwahl): boolean {
		for (const halbjahr of GostHalbjahr.values())
			if (!props.fachwahlenManager.schuelerGetMengeLKByFachAndHalbjahrAsListOrException(fws.id, halbjahr).isEmpty())
				return true;
		return false;
	}

</script>
