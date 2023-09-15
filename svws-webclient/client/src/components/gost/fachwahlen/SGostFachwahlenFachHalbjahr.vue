<template>
	<svws-ui-table :items="[]" :no-data="false" :columns="cols">
		<template #header>
			<div role="row" class="svws-ui-tr" :style="{ '--background-color': fws ? getBgColor(fws) : 'transparent' }">
				<div role="cell" class="svws-ui-td col-span-full">
					<div class="flex gap-1">
						<span class="svws-ui-badge">{{ faecherManager.get(props.fachID)?.bezeichnung || "&ndash;" }}</span>
						<span>in der {{ halbjahr.kuerzel }}</span>
					</div>
				</div>
			</div>
			<div role="row" class="svws-ui-tr">
				<div role="cell" class="svws-ui-td">GK Gesamt</div>
				<div role="cell" class="svws-ui-td">Schriftlich</div>
				<div role="cell" class="svws-ui-td">Mündlich</div>
			</div>
		</template>
		<template #body>
			<template v-if="fws !== undefined && hatFachwahl(fws)">
				<div role="row" class="svws-ui-tr">
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
				<div role="row" class="svws-ui-tr">
					<div role="cell" class="flex flex-col svws-ui-td mb-5 leading-tight" v-for="col in [1, 2, 3]" :key="col">
						<div v-for="schueler in getSchuelerListe(fws.id, col)" :key="schueler.id" class="flex gap-1 -mt-0.5 cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
							<button role="link" class="button button--icon button--small flex-shrink-0 relative top-0.5 !self-start">
								<i-ri-link />
							</button>
							<span class="line-clamp-1 break-all" :title="schueler.nachname + ', ' + schueler.vorname">{{ schueler.nachname + ", " + schueler.vorname }}</span>
						</div>
					</div>
				</div>
			</template>
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { GostFachwahlenFachHalbjahrProps } from "./SGostFachwahlenFachHalbjahrProps";
	import type { ComputedRef} from "vue";
	import { computed } from "vue";
	import { ZulaessigesFach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList } from "@core";

	const props = defineProps<GostFachwahlenFachHalbjahrProps>();

	const fws: ComputedRef<GostStatistikFachwahl | undefined> = computed(() => {
		for (const f of props.fachwahlstatistik)
			if (f.id === props.fachID)
				return f;
		return undefined;
	});

	const cols: DataTableColumn[] = [
		{ key: "GK", label: "GK", span: 1},
		{ key: "GKS", label: "GKS", span: 1},
		{ key: "GKM", label: "GKM", span: 1},
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

	function getSchuelerListe(idFach : number, col: number) : List<SchuelerListeEintrag> {
		const result = new ArrayList<SchuelerListeEintrag>();
		const schuelermenge = col === 1
			? props.fachwahlenManager.schuelerGetMengeGKByFachAndHalbjahrAsListOrException(idFach, props.halbjahr) : col === 2
				? props.fachwahlenManager.schuelerGetMengeGKSchriftlichByFachAndHalbjahrAsListOrException(idFach, props.halbjahr)
				: props.fachwahlenManager.schuelerGetMengeGKMuendlichByFachAndHalbjahrAsListOrException(idFach, props.halbjahr)
		for (const id of schuelermenge) {
			const schueler = props.mapSchueler.get(id);
			if (schueler !== undefined)
				result.add(schueler);
		}
		return doSortSchuelerListeByNachnameAndVornameAndId(result);
	}

	function hatFachwahl(fws: GostStatistikFachwahl): boolean {
		return !props.fachwahlenManager.schuelerGetMengeGKByFachAndHalbjahrAsListOrException(fws.id, props.halbjahr).isEmpty();
	}

</script>
