<template>
	<svws-ui-table :items="[]" :no-data="false" :columns="cols">
		<template #header>
			<div role="row" class="svws-ui-tr text-uistatic" :style="gridTemplateColumns">
				<template v-if="fws !== undefined">
					<div role="cell" class="svws-ui-td col-span-full" :style="'background-color: ' + getBgColor(fws)">
						<span>{{ faecherManager.get(fws.id)?.bezeichnung ?? "&ndash;" }}</span>
					</div>
				</template>
			</div>
			<div role="row" class="svws-ui-tr" :style="gridTemplateColumns">
				<div role="cell" class="svws-ui-td">Halbjahr</div>
				<div role="cell" class="svws-ui-td">
					<span class="icon i-ri-draft-line -my-0.5" />
					<span>Grundkurs</span>
				</div>
				<div role="cell" class="svws-ui-td">
					<span class="icon i-ri-speak-line -my-0.5" />
					<span>Grundkurs</span>
				</div>
				<div role="cell" class="svws-ui-td">
					<span class="icon i-ri-draft-line -my-0.5" />
					<span>Leistungskurs</span>
				</div>
				<div v-if="istZKMoeglich" role="cell" class="svws-ui-td">
					<span class="icon i-ri-speak-line -my-0.5" />
					<span>Zusatzkurs</span>
				</div>
			</div>
		</template>
		<template #body>
			<template v-if="fws !== undefined">
				<template v-for="halbjahr in GostHalbjahr.values()" :key="halbjahr.id">
					<template v-if="hatFachwahl(fws, halbjahr)">
						<div role="row" class="cursor-pointer svws-ui-tr" :style="gridTemplateColumns" @click="onClick(halbjahr)">
							<div role="cell" class="svws-ui-td">
								<span class="flex gap-1 -ml-1 cursor-pointer">
									<span class="icon-uistatic icon i-ri-arrow-right-s-line" v-if="aktuell?.id !== halbjahr.id" />
									<span class="icon-uistatic icon i-ri-arrow-down-s-line" v-else />
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
							<div role="cell" class="svws-ui-td">
								<template v-if="fws.fachwahlen[halbjahr.id].wahlenLK > 0">
									{{ fws.fachwahlen[halbjahr.id].wahlenLK }}
								</template>
								<span v-else class="opacity-25">—</span>
							</div>
							<div v-if="istZKMoeglich" role="cell" class="svws-ui-td">
								<template v-if="fws.fachwahlen[halbjahr.id].wahlenZK > 0">
									{{ fws.fachwahlen[halbjahr.id].wahlenZK }}
								</template>
								<span v-else class="opacity-25">—</span>
							</div>
						</div>
						<div v-if="aktuell?.id === halbjahr.id" role="row" class="svws-ui-tr text-ui" :style="gridTemplateColumns">
							<div> <!----> </div>
							<div role="cell" class="flex flex-col svws-ui-td mb-5 leading-tight" v-for="col in (istZKMoeglich ? [1, 2, 3, 4] : [1, 2, 3])" :key="col">
								<div v-for="schueler in getSchuelerListe(fws.id, halbjahr, col)" :key="schueler.id" class="flex gap-1 py-0.5 px-1 -mx-1 -mt-0.5 hover:bg-ui-75 rounded-sm cursor-pointer" role="link" @click="gotoLaufbahnplanung(schueler.id)">
									<span class="icon i-ri-link" />
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
	import { Fach, type GostStatistikFachwahl, type SchuelerListeEintrag, type List, ArrayList, GostHalbjahr } from "@core";

	const props = defineProps<GostFachwahlenFachProps>();

	/*const aktuell = ref<GostHalbjahr | undefined>(GostHalbjahr.EF1);*/
	const aktuell = ref<GostHalbjahr | undefined>(undefined);

	const istZKMoeglich = computed<boolean>(() => {
		const fach = props.faecherManager.get(props.fachID);
		if (fach === null)
			return false;
		const zfach = Fach.getBySchluesselOrDefault(fach.kuerzel);
		return (zfach === Fach.GE) || (zfach === Fach.SW);
	});

	function onClick(halbjahr: GostHalbjahr): void {
		aktuell.value = (aktuell.value?.id === halbjahr.id) ? undefined : halbjahr;
	}

	const fws: ComputedRef<GostStatistikFachwahl | undefined> = computed(() => {
		for (const f of props.fachwahlstatistik)
			if (f.id === props.fachID)
				return f;
		return undefined;
	});

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
		let schuelermenge : List<number> = new ArrayList<number>();
		if (col === 1)
			schuelermenge = props.fachwahlenManager.schuelerGetMengeGKSchriftlichByFachAndHalbjahrAsListOrException(idFach, halbjahr);
		else if (col === 2)
			schuelermenge = props.fachwahlenManager.schuelerGetMengeGKMuendlichByFachAndHalbjahrAsListOrException(idFach, halbjahr);
		else if (col === 3)
			schuelermenge = props.fachwahlenManager.schuelerGetMengeLKByFachAndHalbjahrAsListOrException(idFach, halbjahr);
		else if ((istZKMoeglich.value) && (col === 4))
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

	const cols = computed<Array<DataTableColumn>>(() => {
		const result : DataTableColumn[] = [
			{ key: "HJ", label: "HJ", fixedWidth: 6 },
			{ key: "GKS", label: "GKS", span: 1 },
			{ key: "GKM", label: "GKM", span: 1 },
			{ key: "LK", label: "LK", span: 1 },
		];
		if (istZKMoeglich.value) {
			result.push({ key: "ZK", label: "ZK", span: 1 });
		}
		return result;
	});

	const gridTemplateColumns = computed<string>(() => "grid-template-columns: 6rem repeat(" + (istZKMoeglich.value ? "4" : "3") + ", minmax(4rem, 1fr))");

</script>
