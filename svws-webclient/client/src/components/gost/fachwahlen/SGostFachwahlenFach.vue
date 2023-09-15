<template>
	<svws-ui-table :items="[]" :no-data="false" :columns="cols">
		<template #header>
			<div role="row" class="svws-ui-tr" :style="{ '--background-color': fws ? getBgColor(fws) : 'transparent' }">
				<div role="cell" class="svws-ui-td col-span-full">
					<span class="svws-ui-badge">{{ faecherManager.get(fws?.id || props.fachID)?.bezeichnung || "&ndash;" }}</span>
				</div>
			</div>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td">HJ</div>
				<div role="cell" class="svws-ui-td">GK Gesamt</div>
				<div role="cell" class="svws-ui-td">Schriftlich</div>
				<div role="cell" class="svws-ui-td">Mündlich</div>
			</div>
		</template>
		<template #body>
			<template v-if="fws !== undefined">
				<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
					<template v-if="hatFachwahl(fws, halbjahr)">
						<div role="row" class="cursor-pointer svws-ui-tr" :class="{'svws-clicked': aktuell?.id === halbjahr.id}" @click="onClick(halbjahr)">
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
						<div v-if="aktuell?.id === halbjahr.id" role="row" class="svws-ui-tr">
							<div role="cell" class="flex flex-col svws-ui-td mb-5 leading-tight" v-for="col in [1, 2, 3]" :key="col" :class="{'col-span-2': col === 1}">
								<div v-for="schueler in getSchuelerListe(fws.id, halbjahr, col)" :key="schueler.id" class="flex gap-1 -mt-0.5 cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
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
