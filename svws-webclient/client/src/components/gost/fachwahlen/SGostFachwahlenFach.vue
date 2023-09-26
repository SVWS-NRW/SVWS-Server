<template>
	<svws-ui-table :items="[]" :no-data="false" :columns="cols">
		<template #header>
			<div role="row" class="svws-ui-tr" :style="{ '--background-color': fws ? getBgColor(fws) : 'transparent' }">
				<div role="cell" class="svws-ui-td col-span-full">
					<span class="svws-ui-badge">{{ faecherManager.get(fws?.id || props.fachID)?.bezeichnung || "&ndash;" }}</span>
				</div>
			</div>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td">Halbjahr</div>
				<div role="cell" class="svws-ui-td">
					<i-ri-draft-line class="text-sm -my-0.5" />
					<span>Schriftlich</span>
				</div>
				<div role="cell" class="svws-ui-td">
					<i-ri-speak-line class="text-sm -my-0.5" />
					<span>Mündlich</span>
				</div>
			</div>
		</template>
		<template #body>
			<template v-if="fws !== undefined">
				<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
					<template v-if="hatFachwahl(fws, halbjahr)">
						<div role="row" class="cursor-pointer svws-ui-tr" @click="onClick(halbjahr)">
							<div role="cell" class="svws-ui-td">
								<span class="flex gap-1 -ml-1">
									<svws-ui-button type="icon" size="small">
										<i-ri-arrow-right-s-line v-if="aktuell?.id !== halbjahr.id" />
										<i-ri-arrow-down-s-line v-else />
									</svws-ui-button>
									<span>{{ halbjahr.kuerzel }}</span>
								</span>
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
						<div v-if="aktuell?.id === halbjahr.id" role="row" class="svws-ui-tr">
							<div> <!----> </div>
							<div role="cell" class="flex flex-col svws-ui-td mb-5 leading-tight" v-for="col in [1, 2]" :key="col">
								<div v-for="schueler in getSchuelerListe(fws.id, halbjahr, col)" :key="schueler.id" class="flex gap-1 py-0.5 px-1 -mx-1 -mt-0.5 hover:bg-black/10 dark:hover:bg-white/10 rounded cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
									<i-ri-link class="text-sm" />
									<span class="line-clamp-1 break-all leading-tight -my-0.5" :title="schueler.nachname + ', ' + schueler.vorname">{{ schueler.nachname + ", " + schueler.vorname }}</span>
								</div>
							</div>
						</div>
					</template>
				</template>
			</template>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { GostFachwahlenFachProps } from "./SGostFachwahlenFachProps";
	import type { ComputedRef} from "vue";
	import { computed, ref } from "vue";
	import { ZulaessigesFach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList, GostHalbjahr } from "@core";

	const props = defineProps<GostFachwahlenFachProps>();

	/*const aktuell = ref<GostHalbjahr | undefined>(GostHalbjahr.EF1);*/
	const aktuell = ref<GostHalbjahr | undefined>(undefined);

	function onClick(halbjahr: GostHalbjahr): void {
		aktuell.value = (aktuell.value?.id === halbjahr.id) ? undefined : halbjahr;
	}

	const fws: ComputedRef<GostStatistikFachwahl | undefined> = computed(() => {
		for (const f of props.fachwahlstatistik)
			if (f.id === props.fachID)
				return f;
		return undefined;
	});

	const cols: DataTableColumn[] = [
		{ key: "HJ", label: "HJ", fixedWidth: 6 },
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
		const schuelermenge = (col === 1)
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
