<template>
	<svws-ui-table :items="[]" :no-data="false" :columns="cols">
		<template #header>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td col-span-full">
					<span class="flex gap-1">
						<span class="svws-ui-badge">Alle Fächer</span>
						in der {{ halbjahr.kuerzel }}
					</span>
				</div>
			</div>
		</template>
		<template #body>
			<template v-for="fws in fachwahlstatistik" :key="fws.id">
				<template v-if="hatFachwahl(fws, halbjahr)">
					<template v-if="fws !== undefined">
						<div role="row" class="svws-ui-tr cursor-pointer" :style="{ '--background-color': fws ? getBgColor(fws) : 'transparent' }" @click="onClick(fws)">
							<div role="cell" class="svws-ui-td col-span-full">
								<div class="-ml-1 mr-0.5">
									<svws-ui-button type="icon" size="small">
										<i-ri-arrow-right-s-line v-if="aktuell?.id !== fws.id" />
										<i-ri-arrow-down-s-line v-else />
									</svws-ui-button>
								</div>
								<span :class="{'svws-ui-badge': aktuell?.id === fws.id}">{{ faecherManager.get(fws.id)?.bezeichnung }}</span>
							</div>
						</div>
						<template v-if="aktuell?.id === fws.id">
							<div role="row" class="svws-ui-tr">
								<div role="cell" class="svws-ui-td svws-align-center">
									<i-ri-draft-line class="text-sm -my-0.5 mr-0.5" />
									<span v-if="fws.fachwahlen[halbjahr.id].wahlenGKSchriftlich > 0"> Schriftlich ({{ fws.fachwahlen[halbjahr.id].wahlenGKSchriftlich }}) </span>
									<span v-else class="opacity-25">Schriftlich (—)</span>
								</div>
								<div role="cell" class="svws-ui-td svws-align-center">
									<i-ri-speak-line class="text-sm -my-0.5 mr-0.5" />
									<span v-if="fws.fachwahlen[halbjahr.id].wahlenGKMuendlich > 0"> Mündlich ({{ fws.fachwahlen[halbjahr.id].wahlenGKMuendlich }}) </span>
									<span v-else class="opacity-25">Mündlich (—)</span>
								</div>
							</div>
							<div role="row" class="svws-ui-tr">
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
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { GostFachwahlenHalbjahrProps } from "./SGostFachwahlenHalbjahrProps";
	import { ref } from "vue";
	import { ZulaessigesFach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList, type GostHalbjahr } from "@core";

	const props = defineProps<GostFachwahlenHalbjahrProps>();

	/*const aktuell = ref<GostStatistikFachwahl | undefined>(fachwahlenstatistik.value.length === 0 ? undefined : fachwahlenstatistik.value.at(0));*/
	const aktuell = ref<GostStatistikFachwahl | undefined>(undefined);

	function onClick(fws : GostStatistikFachwahl): void {
		aktuell.value = (aktuell.value?.id === fws.id) ? undefined : fws;
	}

	const cols: DataTableColumn[] = [
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
