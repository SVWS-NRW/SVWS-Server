<template>
	<svws-ui-table :items="[]" :no-data="false" :columns="cols">
		<template #header>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td col-span-full">
					<span class="flex gap-1">
						<span class="svws-ui-badge">Alle Fächer</span>
						als Zusatzkurs
					</span>
				</div>
			</div>
		</template>
		<template #body>
			<template v-for="fws in fachwahlenstatistik" :key="fws.id">
				<template v-if="fws !== undefined">
					<div role="row" class="svws-ui-tr cursor-pointer" :style="{ '--background-color': fws ? getBgColor(fws) : 'transparent' }" @click="onClick(fws)">
						<div role="cell" class="svws-ui-td col-span-full">
							<div class="-ml-1 mr-0.5">
								<svws-ui-button type="icon" size="small">
									<i-ri-arrow-right-s-line v-if="aktuell.fachwahl?.id !== fws.id" />
									<i-ri-arrow-down-s-line v-else />
								</svws-ui-button>
							</div>
							<span :class="{'svws-ui-badge': aktuell.fachwahl?.id === fws.id}">{{ faecherManager.get(fws.id)?.bezeichnung }}</span>
						</div>
						<div role="row" class="svws-ui-tr">
							<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
								<div role="cell" class="svws-ui-td" v-if="fws.fachwahlen[halbjahr.id].wahlenZK > 0">
									{{ halbjahr.kuerzel }} ({{ fws.fachwahlen[halbjahr.id].wahlenZK }})
								</div>
							</template>
						</div>
						<template v-if="aktuell.fachwahl?.id === fws.id">
							<div role="row" class="svws-ui-tr">
								<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
									<div role="cell"  v-if="fws.fachwahlen[halbjahr.id].wahlenZK > 0" class="flex flex-col svws-ui-td mb-5 leading-tight !pl-4">
										<div v-for="schueler in getSchuelerListe(fws.id, halbjahr)" :key="schueler.id" class="flex gap-1 py-0.5 px-1 -mx-1 -mt-0.5 hover:bg-black/10 dark:hover:bg-white/10 rounded cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
											<i-ri-link class="text-sm" />
											<span class="line-clamp-1 break-all leading-tight -my-0.5" :title="schueler.nachname + ', ' + schueler.vorname">{{ schueler.nachname + ", " + schueler.vorname }}</span>
										</div>
									</div>
								</template>
							</div>
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
	}

	const aktuell = ref<Auswahl>({
		fachwahl: undefined // fachwahlenstatistik.value.length === 0 ? undefined : fachwahlenstatistik.value.at(0)
	});

	function onClick(fws : GostStatistikFachwahl): void {
		if (fws.id === aktuell.value.fachwahl?.id) {
			// Das Fach war zuvor ausgewählt und muss daher zusammengeklappt werden
			aktuell.value = { fachwahl: undefined };
		} else {
			// Ein anderes Fach wurde angeklickt
			aktuell.value = { fachwahl: fws };
		}
	}

	const cols: DataTableColumn[] = [
		{ key: "ZK1", label: "ZK HJ1", span: 1 },
		{ key: "ZK2", label: "ZK HJ2", span: 1 },
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
