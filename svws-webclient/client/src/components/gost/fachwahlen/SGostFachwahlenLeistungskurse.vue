<template>
	<svws-ui-table :items="[]" :no-data="false" :columns="cols">
		<template #header>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td col-span-full">
					<span class="flex gap-1">
						<span class="svws-ui-badge">Alle Fächer</span>
						als Leistungskurs
					</span>
				</div>
			</div>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td col-span-full" :class="{'opacity-25': !aktuell.fachwahl?.id}">Gesamt im Halbjahr</div>
			</div>
		</template>
		<template #body>
			<template v-for="fws in fachwahlenstatistik" :key="fws.id">
				<template v-if="fws !== undefined">
					<div role="row" class="svws-ui-tr cursor-pointer" :style="{ '--background-color': fws ? getBgColor(fws) : 'transparent' }" @click="onClick(fws, undefined)">
						<div role="cell" class="svws-ui-td col-span-full">
							<div class="-ml-1 mr-0.5">
								<svws-ui-button type="icon" size="small">
									<i-ri-arrow-right-s-line v-if="aktuell.fachwahl?.id !== fws.id" />
									<i-ri-arrow-down-s-line v-else />
								</svws-ui-button>
							</div>
							<span :class="{'svws-ui-badge': aktuell.fachwahl?.id === fws.id}">{{ faecherManager.get(fws.id)?.bezeichnung }}</span>
						</div>
						<template v-if="aktuell.fachwahl?.id === fws.id">
							<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
								<div role="row" class="cursor-pointer svws-ui-tr !border-solid !border-black/25 !dark:border-white/25" v-if="fws.fachwahlen[halbjahr.id].wahlenLK > 0" @click.stop="onClick(fws, halbjahr)">
									<div role="cell" class="svws-ui-td">
										<span class="flex gap-1 pl-0.5">
											<svws-ui-button type="icon" size="small">
												<i-ri-arrow-right-s-line v-if="aktuell.halbjahr?.id !== halbjahr.id" />
												<i-ri-arrow-down-s-line v-else />
											</svws-ui-button>
											<span>{{ halbjahr.kuerzel }}</span>
										</span>
									</div>
									<div role="cell" class="svws-ui-td col-span-3">
										{{ fws.fachwahlen[halbjahr.id].wahlenLK }}
									</div>
								</div>
								<div v-if="aktuell?.halbjahr?.id === halbjahr.id" role="row" class="svws-ui-tr">
									<div role="cell" class="flex flex-col svws-ui-td col-span-full mb-5 leading-tight !pl-4">
										<div v-for="schueler in getSchuelerListe(fws.id, halbjahr)" :key="schueler.id" class="flex gap-1 py-0.5 px-1 -mx-1 -mt-0.5 hover:bg-black/10 dark:hover:bg-white/10 rounded cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
											<i-ri-link class="text-sm" />
											<span class="line-clamp-1 break-all leading-tight -my-0.5" :title="schueler.nachname + ', ' + schueler.vorname">{{ schueler.nachname + ", " + schueler.vorname }}</span>
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
		fachwahl: undefined, //fachwahlenstatistik.value.length === 0 ? undefined : fachwahlenstatistik.value.at(0)
		halbjahr: undefined, //GostHalbjahr.Q21
	});

	function onClick(fws : GostStatistikFachwahl, halbjahr: GostHalbjahr | undefined): void {
		if (fws.id === aktuell.value.fachwahl?.id) {
			// Das gleiche Fach wurde angeklickt
			if (halbjahr === undefined) { // Klick auf das Fach
				if (aktuell.value.halbjahr == undefined) {
					// Das Fach war zuvor nicht ausgewählt und muss daher aufgeklappt werden
					aktuell.value = { fachwahl: fws, halbjahr: undefined }; //GostHalbjahr.Q22
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
				aktuell.value = { fachwahl: fws, halbjahr: undefined }; //GostHalbjahr.Q22
			} else { // Klick auf das Halbjahr
				aktuell.value = { fachwahl: fws, halbjahr: halbjahr };
			}
		}
	}

	const cols: DataTableColumn[] = [
		{ key: "HJ", label: "HJ", fixedWidth: 6 },
		{ key: "LK", label: "LK", span: 1 },
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
