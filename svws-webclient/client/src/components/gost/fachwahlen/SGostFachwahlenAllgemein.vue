<template>
	<svws-ui-table :items="[]" :no-data="false">
		<template #header>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td col-span-full">
					<span class="flex gap-1">
						<span class="svws-ui-badge">Alle Fächer</span>
						in allen Halbjahren
					</span>
				</div>
			</div>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td col-span-2" :class="{'opacity-25': !aktuell.fachwahl?.id}">Fach / Halbjahr / Summe</div>
				<div role="cell" class="svws-ui-td" :class="{'opacity-25': !aktuell.fachwahl?.id}">
					<span class="icon i-ri-draft-line -my-0.5" />
					<span>Grundkurs</span>
				</div>
				<div role="cell" class="svws-ui-td" :class="{'opacity-25': !aktuell.fachwahl?.id}">
					<span class="icon i-ri-speak-line -my-0.5" />
					<span>Grundkurs</span>
				</div>
				<div role="cell" class="svws-ui-td" :class="{'opacity-25': !aktuell.fachwahl?.id}">
					<span class="icon i-ri-draft-line -my-0.5" />
					<span>Leistungskurs</span>
				</div>
				<div role="cell" class="svws-ui-td" :class="{'opacity-25': !aktuell.fachwahl?.id}">
					<span class="icon i-ri-speak-line -my-0.5" />
					<span>Zusatzkurs</span>
				</div>
			</div>
		</template>
		<template #body>
			<template v-for="fws in fachwahlstatistik" :key="fws.id">
				<template v-if="fws !== undefined">
					<div role="row" class="cursor-pointer svws-ui-tr text-uistatic">
						<div role="cell" class="svws-ui-td col-span-full" :style="'background-color: ' + getBgColor(fws)" @click="onClick(fws, undefined)">
							<div class="-ml-1 mr-0.5 cursor-pointer">
								<span class="icon-uistatic icon i-ri-arrow-right-s-line" v-if="aktuell.fachwahl?.id !== fws.id" />
								<span class="icon-uistatic icon i-ri-arrow-down-s-line" v-else />
							</div>
							<span :class="{'font-bold': aktuell.fachwahl?.id === fws.id}">{{ faecherManager.get(fws.id)?.bezeichnung }}</span>
						</div>
						<template v-if="aktuell.fachwahl?.id === fws.id">
							<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
								<template v-if="hatFachwahl(fws, halbjahr)">
									<div role="row" class="cursor-pointer svws-ui-tr text-ui border-solid! border-ui-25!" @click="onClick(fws, halbjahr)">
										<div role="cell" class="svws-ui-td">
											<span class="flex gap-1 pl-0.5">
												<div type="icon" size="small">
													<span class="icon i-ri-arrow-right-s-line" v-if="aktuell.halbjahr?.id !== halbjahr.id" />
													<span class="icon i-ri-arrow-down-s-line" v-else />
												</div>
												<span>{{ halbjahr.kuerzel }}</span>
											</span>
										</div>
										<div role="cell" class="svws-ui-td">
											<template v-if="fws.fachwahlen[halbjahr.id].wahlenGK + fws.fachwahlen[halbjahr.id].wahlenLK + fws.fachwahlen[halbjahr.id].wahlenZK > 0">
												{{ fws.fachwahlen[halbjahr.id].wahlenGK + fws.fachwahlen[halbjahr.id].wahlenLK + fws.fachwahlen[halbjahr.id].wahlenZK }}
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
										<div role="cell" class="svws-ui-td">
											<template v-if="fws.fachwahlen[halbjahr.id].wahlenLK > 0">
												{{ fws.fachwahlen[halbjahr.id].wahlenLK }}
											</template>
											<span v-else class="opacity-25">—</span>
										</div>
										<div role="cell" class="svws-ui-td">
											<template v-if="fws.fachwahlen[halbjahr.id].wahlenZK > 0">
												{{ fws.fachwahlen[halbjahr.id].wahlenZK }}
											</template>
											<span v-else class="opacity-25">—</span>
										</div>
									</div>
									<div class="svws-ui-tr text-ui" role="row" v-if="aktuell.halbjahr?.id === halbjahr.id">
										<div role="cell" class="svws-ui-td col-span-2 !pl-4 text-ui-50 hover:text-ui-100 focus-within:text-ui-100" />
										<div role="cell" class="flex flex-col svws-ui-td mb-5 leading-tight" v-for="col in [1, 2, 3, 4]" :key="col">
											<div v-for="schueler in getSchuelerListe(fws.id, halbjahr, col)" :key="schueler.id" class="flex gap-1 py-0.5 px-1 -mx-1 -mt-0.5 hover:bg-ui-75 rounded-sm cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
												<span class="icon i-ri-link" />
												<span class="line-clamp-1 break-all leading-tight -my-0.5" :title="schueler.nachname + ', ' + schueler.vorname">{{ schueler.nachname + ", " + schueler.vorname }}</span>
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

	import { computed, ref } from "vue";
	import type { GostFachwahlenAllgemeinProps } from "./SGostFachwahlenAllgemeinProps";
	import { Fach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList, GostHalbjahr } from "@core";

	const props = defineProps<GostFachwahlenAllgemeinProps>();

	const schuljahr = computed<number>(() => props.faecherManager.getSchuljahr());

	type Auswahl = {
		fachwahl: GostStatistikFachwahl | undefined;
		halbjahr: GostHalbjahr | undefined;
	}

	const aktuell = ref<Auswahl>({
		fachwahl: undefined, // fachwahlenstatistik.value.at(0)
		halbjahr: undefined, // GostHalbjahr.EF1
	});

	function onClick(fws : GostStatistikFachwahl, halbjahr: GostHalbjahr | undefined): void {
		if (fws.id === aktuell.value.fachwahl?.id) {
			// Das gleiche Fach wurde angeklickt
			if (halbjahr === undefined) { // Klick auf das Fach
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

	function getBgColor(fws: GostStatistikFachwahl) : string {
		if (fws.kuerzelStatistik === null)
			return 'rgb(220,220,220)';
		return Fach.getBySchluesselOrDefault(fws.kuerzelStatistik).getHMTLFarbeRGBA(schuljahr.value, 1.0);
	}

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
		let schuelermenge : List<number> = new ArrayList<number>();
		if (col === 1)
			schuelermenge = props.fachwahlenManager.schuelerGetMengeGKSchriftlichByFachAndHalbjahrAsListOrException(idFach, halbjahr);
		else if (col === 2)
			schuelermenge = props.fachwahlenManager.schuelerGetMengeGKMuendlichByFachAndHalbjahrAsListOrException(idFach, halbjahr);
		else if (col === 3)
			schuelermenge = props.fachwahlenManager.schuelerGetMengeLKByFachAndHalbjahrAsListOrException(idFach, halbjahr);
		else if (col === 4)
			schuelermenge = props.fachwahlenManager.schuelerGetMengeZKByFachAndHalbjahrAsListOrException(idFach, halbjahr);
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

<style scoped>

	.svws-ui-tr {
		grid-template-columns: 6rem repeat(5, 1fr);
	}

</style>
