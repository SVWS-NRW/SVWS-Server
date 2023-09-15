<template>
	<svws-ui-table :items="[]" :no-data="false" :columns="cols">
		<template #header>
			<div role="row" class="svws-ui-tr">
				<div role="columnheader" class="svws-ui-td col-span-full">
					Zusatzkurse
				</div>
			</div>
		</template>
		<template #body>
			<template v-for="fws in fachwahlenstatistik" :key="fws.id">
				<template v-if="fws !== undefined">
					<div role="row" class="svws-ui-tr cursor-pointer" :style="{ '--background-color': fws ? getBgColor(fws) : 'transparent' }" @click="onClick(fws, undefined)">
						<div role="cell" class="svws-ui-td col-span-full">
							<div class="-ml-1 mr-1">
								<svws-ui-button type="icon" size="small">
									<i-ri-arrow-right-s-line v-if="aktuell.fachwahl?.id !== fws.id" />
									<i-ri-arrow-down-s-line v-else />
								</svws-ui-button>
							</div>
							<span class="svws-ui-badge">{{ faecherManager.get(fws.id)?.bezeichnung || "&ndash;" }}</span>
						</div>
						<template v-if="aktuell.fachwahl?.id === fws.id">
							<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
								<div role="row" class="svws-ui-tr grid-cols-2" v-if="fws.fachwahlen[halbjahr.id].wahlenZK > 0" :class="{'svws-clicked': aktuell?.halbjahr?.id === halbjahr.id}" @click.stop="onClick(fws, halbjahr)">
									<div role="cell" class="svws-ui-td">
										<span class="w-16">{{ halbjahr.kuerzel }}</span>
										{{ fws.fachwahlen[halbjahr.id].wahlenZK }}
									</div>
								</div>
								<div v-if="aktuell?.halbjahr?.id === halbjahr.id" role="row" class="svws-ui-tr">
									<div role="cell" class="flex flex-col svws-ui-td mb-5 leading-tight">
										<div v-for="schueler in getSchuelerListe(fws.id, halbjahr)" :key="schueler.id" class="flex gap-1 -mt-0.5 cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
											<button role="link" class="button button--icon button--small flex-shrink-0 relative top-0.5 !self-start">
												<i-ri-link />
											</button>
											<span class="line-clamp-1 break-all" :title="schueler.nachname + ', ' + schueler.vorname">{{ schueler.nachname + ", " + schueler.vorname }}</span>
										</div>
									</div>
								</div>
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
	import type { GostFachwahlenZusatzkurseProps } from "./SGostFachwahlenZusatzkurseProps";
	import type { ComputedRef} from "vue";
	import { computed, ref } from "vue";
	import { ZulaessigesFach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList, GostHalbjahr } from "@core";

	const props = defineProps<GostFachwahlenZusatzkurseProps>();

	const fachwahlenstatistik: ComputedRef<GostStatistikFachwahl[]> = computed(() => {
		const result = new Array<GostStatistikFachwahl>();
		for (const fws of props.fachwahlstatistik)
			if (hatZusatzkurs(fws))
				result.push(fws);
		return result;
	});

	type Auswahl = {
		fachwahl: GostStatistikFachwahl | undefined;
		halbjahr: GostHalbjahr | undefined;
	}

	const aktuell = ref<Auswahl>({
		fachwahl: undefined, //fachwahlenstatistik.value.length === 0 ? undefined : fachwahlenstatistik.value.at(0)
		halbjahr: undefined, //GostHalbjahr.Q21
	});

	function onClick(fws : GostStatistikFachwahl, halbjahr: GostHalbjahr | undefined): void {
		if (fws.id === aktuell.value.fachwahl?.id) {
			// Das gleiche Fach wurde angeklickt
			if (halbjahr === undefined) { // Klick auf das Fach
				if (aktuell.value.halbjahr == undefined) {
					// Das Fach war zuvor nicht ausgewählt und muss daher aufgeklappt werden
					aktuell.value = { fachwahl: fws, halbjahr: undefined }; //GostHalbjahr.Q21   // TODO Bestimmung des ersten Halbjahres mit ZK-Fachwahlen
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
				aktuell.value = { fachwahl: fws, halbjahr: undefined }; //GostHalbjahr.Q21  // TODO Bestimmung des ersten Halbjahres mit ZK-Fachwahlen
			} else { // Klick auf das Halbjahr
				aktuell.value = { fachwahl: fws, halbjahr: halbjahr };
			}
		}
	}

	const cols: DataTableColumn[] = [
		{ key: "ZK", label: "ZK", span: 1, minWidth: 4 },
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
		const schuelermenge = props.fachwahlenManager.schuelerGetMengeZKByFachAndHalbjahrAsListOrException(idFach, halbjahr);
		for (const id of schuelermenge) {
			const schueler = props.mapSchueler.get(id);
			if (schueler !== undefined)
				result.add(schueler);
		}
		return doSortSchuelerListeByNachnameAndVornameAndId(result);
	}

	function hatZusatzkurs(fws: GostStatistikFachwahl): boolean {
		for (const halbjahr of GostHalbjahr.values())
			if (!props.fachwahlenManager.schuelerGetMengeZKByFachAndHalbjahrAsListOrException(fws.id, halbjahr).isEmpty())
				return true;
		return false;
	}

</script>
