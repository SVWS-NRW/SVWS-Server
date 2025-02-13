<template>
	<svws-ui-table :items="[]" :no-data="false">
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
						<div role="row" class="svws-ui-tr text-ui-static cursor-pointer">
							<div role="cell" class="svws-ui-td col-span-full" :style="'background-color: ' + getBgColor(fws)" @click="onClick(fws)">
								<div class="-ml-1 mr-0.5 cursor-pointer">
									<span class="icon-ui-static icon i-ri-arrow-right-s-line" v-if="aktuell?.id !== fws.id" />
									<span class="icon-ui-static icon i-ri-arrow-down-s-line" v-else />
								</div>
								<span :class="{'font-bold': aktuell?.id === fws.id}">{{ faecherManager.get(fws.id)?.bezeichnung }}</span>
							</div>
						</div>
						<template v-if="aktuell?.id === fws.id">
							<div role="row" class="svws-ui-tr text-ui">
								<div role="cell" class="svws-ui-td svws-align-center">
									<span class="icon i-ri-draft-line -my-0.5 mr-0.5" />
									<span v-if="fws.fachwahlen[halbjahr.id].wahlenGKSchriftlich > 0"> Schriftlich ({{ fws.fachwahlen[halbjahr.id].wahlenGKSchriftlich }}) </span>
									<span v-else class="opacity-25">Schriftlich (—)</span>
								</div>
								<div role="cell" class="svws-ui-td svws-align-center">
									<span class="icon i-ri-speak-line -my-0.5 mr-0.5" />
									<span v-if="fws.fachwahlen[halbjahr.id].wahlenGKMuendlich > 0"> Mündlich ({{ fws.fachwahlen[halbjahr.id].wahlenGKMuendlich }}) </span>
									<span v-else class="opacity-25">Mündlich (—)</span>
								</div>
							</div>
							<div role="row" class="svws-ui-tr text-ui">
								<div role="cell" class="flex flex-col svws-ui-td mb-5 leading-tight" v-for="col in [1, 2]" :key="col">
									<div v-for="schueler in getSchuelerListe(fws.id, halbjahr, col)" :key="schueler.id" class="flex gap-1 py-0.5 px-1 -mx-1 -mt-0.5 hover:bg-ui-contrast-10 rounded-sm cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
										<span class="icon i-ri-link" />
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

	import type { GostFachwahlenHalbjahrProps } from "./SGostFachwahlenHalbjahrProps";
	import { computed, ref } from "vue";
	import { Fach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList, type GostHalbjahr } from "@core";

	const props = defineProps<GostFachwahlenHalbjahrProps>();

	/*const aktuell = ref<GostStatistikFachwahl | undefined>(fachwahlenstatistik.value.length === 0 ? undefined : fachwahlenstatistik.value.at(0));*/
	const aktuell = ref<GostStatistikFachwahl | undefined>(undefined);

	function onClick(fws : GostStatistikFachwahl): void {
		aktuell.value = (aktuell.value?.id === fws.id) ? undefined : fws;
	}

	const schuljahr = computed<number>(() => props.faecherManager.getSchuljahr());

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

<style scoped>

	.svws-ui-tr {
		grid-template-columns: repeat(2, 1fr);
	}

</style>
