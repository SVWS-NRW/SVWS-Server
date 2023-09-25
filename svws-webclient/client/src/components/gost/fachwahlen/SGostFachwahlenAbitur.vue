<template>
	<svws-ui-table :items="[]" :no-data="false" :columns="cols">
		<template #header>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td col-span-full">
					<span class="flex gap-1">
						<span class="svws-ui-badge">Alle Fächer</span>
						im Abitur
					</span>
				</div>
			</div>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td col-span-2" :class="{'opacity-25': !aktuell?.id}">Gesamt im Leistungskurs</div>
				<div role="cell" class="svws-ui-td" :class="{'opacity-25': !aktuell?.id}">
					<i-ri-draft-line class="text-sm -my-0.5" />
					<span>3. Abiturfach</span>
				</div>
				<div role="cell" class="svws-ui-td" :class="{'opacity-25': !aktuell?.id}">
					<i-ri-speak-line class="text-sm -my-0.5" />
					<span>4. Abiturfach</span>
				</div>
			</div>
		</template>
		<template #body>
			<template v-for="fws in fachwahlstatistik" :key="fws.id">
				<template v-if="hatAbiFachwahl(fws)">
					<div role="row" class="cursor-pointer svws-ui-tr" :style="{ '--background-color': getBgColor(fws) }" @click="onClick(fws)">
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
					<div v-if="aktuell?.id === fws.id" role="row" class="cursor-pointer svws-ui-tr">
						<div role="cell" class="svws-ui-td col-span-2 !pl-7">
							<template v-if="fws.fachwahlen[5].wahlenLK > 0">
								{{ fws.fachwahlen[5].wahlenLK }}
							</template>
							<span v-else class="opacity-25">—</span>
						</div>
						<div role="cell" class="svws-ui-td">
							<template v-if="fws.wahlenAB3 > 0">
								{{ fws.wahlenAB3 }}
							</template>
							<span v-else class="opacity-25">—</span>
						</div>
						<div role="cell" class="svws-ui-td">
							<template v-if="fws.wahlenAB4 > 0">
								{{ fws.wahlenAB4 }}
							</template>
							<span v-else class="opacity-25">—</span>
						</div>
					</div>
					<div v-if="aktuell?.id === fws.id" role="row" class="svws-ui-tr">
						<div role="cell" class="flex flex-col svws-ui-td mb-5 leading-tight" v-for="(abifach, index) in [GostAbiturFach.LK1, GostAbiturFach.AB3, GostAbiturFach.AB4]" :key="abifach.id" :class="{'col-span-2 !pl-7 text-black/50 dark:text-white/50 hover:text-black focus-within:text-black dark:hover:text-white dark:focus-within:text-white': index === 0}">
							<div v-for="schueler in getSchuelerListe(fws.id, abifach)" :key="schueler.id" class="flex gap-1 py-0.5 px-1 -mx-1 -mt-0.5 hover:bg-black/10 dark:hover:bg-white/10 rounded cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
								<i-ri-link class="text-sm" />
								<span class="line-clamp-1 break-all leading-tight -my-0.5" :title="schueler.nachname + ', ' + schueler.vorname">{{ schueler.nachname + ", " + schueler.vorname }}</span>
							</div>
						</div>
					</div>
				</template>
			</template>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { GostFachwahlenAbiturProps } from "./SGostFachwahlenAbiturProps";
	import { ref } from "vue";
	import { GostAbiturFach, ZulaessigesFach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList } from "@core";

	const props = defineProps<GostFachwahlenAbiturProps>();

	const aktuell = ref<GostStatistikFachwahl | undefined>(undefined); //fachwahlenstatistik.value.length === 0 ? undefined : fachwahlenstatistik.value.at(0)

	const cols: DataTableColumn[] = [
		{ key: "HJ", label: "HJ", fixedWidth: 6 },
		{ key: "LK", label: "LK", span: 1 },
		{ key: "AB3", label: "AB3", span: 1 },
		{ key: "AB4", label: "Ab4", span: 1 },
	];

	const getBgColor = (fws: GostStatistikFachwahl) => ZulaessigesFach.getByKuerzelASD(fws.kuerzelStatistik).getHMTLFarbeRGBA(1.0);

	function onClick(fws: GostStatistikFachwahl): void {
		aktuell.value = (aktuell.value?.id === fws.id) ? undefined : fws;
	}

	function getSchuelerListe(idFach : number, abifach: GostAbiturFach) : List<SchuelerListeEintrag> {
		const result = new ArrayList<SchuelerListeEintrag>();
		for (const id of props.fachwahlenManager.schuelerGetMengeByFachAndAbifachAsListOrException(idFach, abifach)) {
			const schueler = props.mapSchueler.get(id);
			if (schueler !== undefined)
				result.add(schueler);
		}
		result.sort({ compare(a : SchuelerListeEintrag, b : SchuelerListeEintrag) : number {
			let cmp = a.nachname.localeCompare(b.nachname);
			if (cmp !== 0)
				return cmp;
			cmp = a.vorname.localeCompare(b.vorname);
			if (cmp !== 0)
				return cmp;
			return b.id - a.id;
		} });
		return result;
	}

	function hatAbiFachwahl(fws: GostStatistikFachwahl): boolean {
		if ((props.fachwahlenManager.schuelerGetMengeByFachAndAbifachAsListOrException(fws.id, GostAbiturFach.LK1).isEmpty())
			&& (props.fachwahlenManager.schuelerGetMengeByFachAndAbifachAsListOrException(fws.id, GostAbiturFach.AB3).isEmpty())
			&& (props.fachwahlenManager.schuelerGetMengeByFachAndAbifachAsListOrException(fws.id, GostAbiturFach.AB4).isEmpty()))
			return false;
		return true;
	}

</script>
