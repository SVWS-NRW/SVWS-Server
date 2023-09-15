<template>
	<svws-ui-table :items="[]" :no-data="false" :columns="cols">
		<template #header>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td col-span-full">Alle Daten</div>
			</div>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td">HJ</div>
				<div role="cell" class="svws-ui-td">GK Gesamt</div>
				<div role="cell" class="svws-ui-td">Schriftlich</div>
				<div role="cell" class="svws-ui-td">Mündlich</div>
			</div>
		</template>
		<template #body>
			<template v-for="fws in fachwahlenstatistik" :key="fws.id">
				<template v-if="fws !== undefined">
					<div role="row" class="cursor-pointer svws-ui-tr" :style="{ '--background-color': getBgColor(fws) }">
						<div role="cell" class="col-span-full svws-ui-td" @click="onClick(fws, undefined)">
							<div class="-ml-1 mr-1">
								<svws-ui-button type="icon" size="small">
									<i-ri-arrow-right-s-line v-if="aktuell.fachwahl?.id !== fws.id" />
									<i-ri-arrow-down-s-line v-else />
								</svws-ui-button>
							</div>
							<span class="svws-ui-badge">{{ faecherManager.get(fws.id)?.bezeichnung }}</span>
						</div>
						<template v-if="aktuell.fachwahl?.id === fws.id">
							<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
								<template v-if="hatFachwahl(fws, halbjahr)">
									<div role="row" class="cursor-pointer svws-ui-tr" :class="{'svws-clicked': aktuell.halbjahr?.id === halbjahr.id}" @click="onClick(fws, halbjahr)">
										<div role="cell" class="svws-ui-td">
											{{ halbjahr.kuerzel }}
										</div>
										<div role="cell" class="svws-ui-td">
											<template v-if="fws.fachwahlen[halbjahr.id].wahlenGK > 0">
												{{ fws.fachwahlen[halbjahr.id].wahlenGK }}
											</template>
											<span v-else class="opacity-25">—</span>
										</div>
										<div role="cell" class="svws-ui-td">
											<template v-if="fws.fachwahlen[halbjahr.id].wahlenGKSchriftlich > 0">
												{{ fws.fachwahlen[halbjahr.id].wahlenGKSchriftlich }}
											</template>
											<span v-else class="opacity-25">—</span>
										</div>
										<div role="cell" class="svws-ui-td">
											<template v-if="fws.fachwahlen[halbjahr.id].wahlenGKMuendlich > 0">
												{{ fws.fachwahlen[halbjahr.id].wahlenGKMuendlich }}
											</template>
											<span v-else class="opacity-25">—</span>
										</div>
									</div>
									<div v-if="aktuell.halbjahr?.id === halbjahr.id" role="row" class="svws-ui-tr">
										<div role="cell" class="flex flex-col svws-ui-td mb-5 leading-tight" v-for="col in [1, 2, 3]" :key="col" :class="{'col-span-2': col === 1}">
											<div v-for="schueler in getSchuelerListe(fws.id, halbjahr, col)" :key="schueler.id" class="flex gap-1 -mt-0.5" @click="gotoLaufbahnplanung(schueler.id)">
												<button role="link" class="button button--icon button--small flex-shrink-0 relative top-0.5 !self-start">
													<i-ri-link />
												</button>
												<span class="line-clamp-1 break-all" :title="schueler.nachname + ', ' + schueler.vorname">{{ schueler.nachname + ", " + schueler.vorname }}</span>
											</div>
										</div>
									</div>
								</template>
							</template>
						</template>
					</div>
				</template>
			</template>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
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
		fachwahl: fachwahlenstatistik.value.length === 0 ? undefined : undefined, // fachwahlenstatistik.value.at(0)
		halbjahr: undefined, // GostHalbjahr.EF1
	});

	function onClick(fws : GostStatistikFachwahl, halbjahr: GostHalbjahr | undefined): void {
		if (fws.id === aktuell.value.fachwahl?.id) {
			// Das gleiche Fach wurde angeklickt
			if (halbjahr == undefined) { // Klick auf das Fach
				if (aktuell.value.halbjahr === undefined) {
					// Das Fach war zuvor nicht ausgewählt und muss daher aufgeklappt werden
					aktuell.value = { fachwahl: undefined, halbjahr: undefined }; // GostHalbjahr.EF1 // TODO Bestimmung des ersten Halbjahres mit Fachwahlen
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
			// Ein anderes Fach wurde angeklickt
			if (halbjahr === undefined) { // Klick auf das Fach
				aktuell.value = { fachwahl: fws, halbjahr: undefined }; // GostHalbjahr.EF1  // TODO Bestimmung des ersten Halbjahres mit Fachwahlen
			} else { // Klick auf das Halbjahr
				aktuell.value = { fachwahl: fws, halbjahr: halbjahr };
			}
		}
	}

	const cols: DataTableColumn[] = [
		{ key: "HJ", label: "HJ", fixedWidth: 4 },
		{ key: "GK", label: "GK", span: 1 },
		{ key: "GKS", label: "GKS", span: 1 },
		{ key: "GKM", label: "GKM", span: 1 },
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
