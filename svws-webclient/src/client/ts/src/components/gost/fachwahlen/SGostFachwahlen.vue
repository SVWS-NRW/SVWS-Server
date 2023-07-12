<template>
	<div class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<div class="flex-grow">
			<svws-ui-content-card class="table--with-background">
				<svws-ui-data-table :items="[]" :no-data="false" :columns="cols" panel-height overflow-x-hidden>
					<template #header>
						<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact select-none">
							<div role="columnheader" v-for="heading in colHeadings" :key="heading.text"
								class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate cursor-pointer"
								:class="{ [`col-span-${heading.cols.length}`]: true, 'bg-svws/5 text-svws': isSelected(undefined, heading.text) }"
								@click="selectData(undefined, heading.text)">
								<div class="data-table__th-wrapper">
									<div class="data-table__th-title">
										{{ heading.text }}
									</div>
								</div>
							</div>
						</div>
						<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact select-none">
							<template v-for="heading in colHeadings" :key="heading.text">
								<div role="columnheader" v-for="(h, n) in heading.cols" :key="n" class="data-table__th data-table__thead__th cursor-pointer"
									:class="{ 'data-table__th__align-center': h.center, 'data-table__th__separate': (n === heading.cols.length - 1),
										'bg-svws/5 text-svws': isSelected(undefined, heading.text) }"
									@click="selectData(undefined, heading.text)">
									<div class="data-table__th-wrapper">
										<div class="data-table__th-title">
											{{ h.text }}
										</div>
									</div>
								</div>
							</template>
						</div>
					</template>
					<template #body>
						<div role="row" v-for="row in rows" :key="row.id" class="data-table__tr data-table__tbody__tr" :style="{ 'background-color': getBgColor(row) }">
							<template v-for="heading in colHeadings" :key="heading.text">
								<div role="cell" v-for="(h, n) in heading.cols" :key="n" class="data-table__td select-none"
									:class="{
										'data-table__th__align-center': h.center, 'data-table__th__separate': (n === heading.cols.length - 1),
										'bg-white/75 text-svws font-bold': isSelected(row, heading.text),
										'cursor-pointer': isSelectable(row, heading.text) }"
									@click="selectData(row, heading.text)">
									{{ getData(row, heading.text, h.text) }}
								</div>
							</template>
						</div>
					</template>
				</svws-ui-data-table>
			</svws-ui-content-card>
		</div>
		<div class="w-2/5 3xl:w-1/2 min-w-[36rem]">
			<div v-if="(selected.row === undefined) && (selected.group === 'Fach')" class="flex flex-col gap-16">
				<s-gost-fachwahlen-card-allgemein v-if="(selected.row === undefined) && (selected.group === 'Fach')" :fachwahlen="fachwahlen" />
				<template v-if="(selected.row === undefined) && (selected.group !== 'Fach')">
					Weitere Informationen zu allen Fächern in Bezug auf ...
				</template>
				<template v-if="(selected.row !== undefined)">
					<template v-if="(selected.group === 'Fach')">
						Fachwahlen zum Fach {{ selected.row.bezeichnung }}
					</template>
					<template v-if="(selected.group !== 'Fach')">
						Fachwahlen zum Fach {{ selected.row.bezeichnung }} in Bezug auf...
					</template>
				</template>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, type ComputedRef } from "vue";
	import { GostHalbjahr, ZulaessigesFach, type GostStatistikFachwahl } from "@core";
	import { type DataTableColumn } from "@ui";
	import type { GostFachwahlenProps } from "./SGostFachwahlenProps";

	const props = defineProps<GostFachwahlenProps>();

	const rows: ComputedRef<GostStatistikFachwahl[]> = computed(() => props.fachwahlen.toArray() as GostStatistikFachwahl[]);

	const getBgColor = (row: GostStatistikFachwahl) => ZulaessigesFach.getByKuerzelASD(row.kuerzelStatistik).getHMTLFarbeRGBA(1.0);

	const selected = ref<{ row?: GostStatistikFachwahl, group: string}>({ group : "Fach" });

	async function doSelect(idFach: number | undefined, bereich: string | undefined, halbjahr?: GostHalbjahr) {
		console.log(idFach, bereich);
	}

	async function selectData(row: GostStatistikFachwahl | undefined, group: string) {
		// Lade die neue Auswahl
		const fach_id = (row === undefined) ? undefined : row.id;
		const halbjahr = GostHalbjahr.fromKuerzel(group); // nur für GKs
		switch (group) {
			case "Fach":
				await doSelect(fach_id, undefined);
				break;
			case "ZK":
			case "LK":
				await doSelect(fach_id, group);
				break;
			case "Abiturfach":
				await doSelect(fach_id, "Abi");
				break;
			default:
				if (halbjahr !== null)
					await doSelect(fach_id, "Halbjahr", halbjahr);
				break;
		}
		// Setze die Auswahl
		if (row === undefined) {
			selected.value = { row: undefined, group };
		} else if (isSelectable(row, group)) {
			selected.value = { row, group };
		}
	}

	function isSelected(row: GostStatistikFachwahl | undefined, group: string): boolean {
		if (selected.value.row === undefined) {
			// Prüfe, ob alle Fächer ausgewählt sind. In diesem Fall wird keine Auswahl angezeigt
			if (selected.value.group === "Fach")
				return false;
			// Prüfe, ob eine Spalte ausgewählt ist. In diesem Fall werden alle Zellen der Spalte hervorgehoben
			return (selected.value.group === group);
		} else {
			// Prüfe, ob ein Zeile, d.h. ein Fach ausgewählt ist
			if ((selected.value.row.id === row?.id) && (selected.value.group === "Fach"))
				return true;
			// Prüfe, ob die jeweilige Zelle ausgewählt ist
			return ((selected.value.row.id === row?.id) && (selected.value.group === group));
		}
	}

	function isSelectable(row: GostStatistikFachwahl, group: string) : boolean {
		if (group === "Fach")
			return true;
		if (group === "Abiturfach")
			return (row.wahlenAB3 !== 0) || (row.wahlenAB4 !== 0);
		if ((group == "ZK") || (group == "LK"))
			return getData(row, group, "") !== "";
		const hj = GostHalbjahr.fromKuerzel(group);
		if (hj === null)
			return false;
		const fw = row.fachwahlen[hj.id];
		if (fw === null)
			return false;
		return (fw.wahlenGK !== 0);
	}

	function getData(row: GostStatistikFachwahl, group: string, item: string) {
		if ((group === "Fach") && (item === "Kürzel"))
			return row.kuerzel;
		if ((group === "Fach") && (item === "Fach"))
			return row.bezeichnung;
		if ((group === "Abiturfach") && (item === "3"))
			return row.wahlenAB3 || "";
		if ((group === "Abiturfach") && (item === "4"))
			return row.wahlenAB4 || "";
		if ((group === "ZK") && (item === "")) {
			const maxZK = Math.max(row.fachwahlen[2].wahlenZK, row.fachwahlen[3].wahlenZK, row.fachwahlen[4].wahlenZK, row.fachwahlen[5].wahlenZK);
			return maxZK === 0 ? "" : maxZK;
		}
		if ((group === "LK") && (item === "")) {
			const maxLK = Math.max(row.fachwahlen[2].wahlenLK, row.fachwahlen[3].wahlenLK, row.fachwahlen[4].wahlenLK, row.fachwahlen[5].wahlenLK);
			return maxLK === 0 ? "" : maxLK;
		}
		const hj = GostHalbjahr.fromKuerzel(group);
		if (hj === null)
			return "";
		const fw = row.fachwahlen[hj.id];
		if (fw === null)
			return "";
		switch (item) {
			case "S": return (fw.wahlenGKSchriftlich === 0 ? "" : fw.wahlenGKSchriftlich);
			case "GK": return fw.wahlenGK === 0 ? "" : fw.wahlenGK;
		}
		return "";
	}

	const colHeadings: Array<{ text: string, cols: Array<{ text: string, center? : boolean }> }> = [
		{ text: "Fach", cols: [ { text : "Kürzel" }, { text: "Fach" } ] },
		{ text: "EF.1", cols: [ { text : "GK", center: true }, { text : "S", center: true } ] },
		{ text: "EF.2", cols: [ { text : "GK", center: true }, { text : "S", center: true } ] },
		{ text: "Q1.1", cols: [ { text : "GK", center: true }, { text : "S", center: true } ] },
		{ text: "Q1.2", cols: [ { text : "GK", center: true }, { text : "S", center: true } ] },
		{ text: "Q2.1", cols: [ { text : "GK", center: true }, { text : "S", center: true } ] },
		{ text: "Q2.2", cols: [ { text : "GK", center: true }, { text : "S", center: true } ] },
		{ text: "ZK", cols: [ { text : "", center: true } ] },
		{ text: "LK", cols: [ { text : "", center: true } ] },
		{ text: "Abiturfach", cols: [ { text : "3", center: true }, { text : "4", center: true } ] },
	]

	const cols: Array<DataTableColumn> = [
		{ key: "Kuerzel", label: "Kürzel", minWidth: 4 },
		{ key: "Fach", label: "Fach", span: 3, minWidth: 12},
		{ key: "GK", label: "GK", align: 'center', fixedWidth: 3 },
		{ key: "S", label: "S", align: 'center', fixedWidth: 3 },
		{ key: "GK", label: "GK", align: 'center', fixedWidth: 3 },
		{ key: "S", label: "S", align: 'center', fixedWidth: 3 },
		{ key: "GK", label: "GK", align: 'center', fixedWidth: 3 },
		{ key: "S", label: "S", align: 'center', fixedWidth: 3 },
		{ key: "GK", label: "GK", align: 'center', fixedWidth: 3 },
		{ key: "S", label: "S", align: 'center', fixedWidth: 3 },
		{ key: "GK", label: "GK", align: 'center', fixedWidth: 3 },
		{ key: "S", label: "S", align: 'center', fixedWidth: 3 },
		{ key: "GK", label: "GK", align: 'center', fixedWidth: 3 },
		{ key: "S", label: "S", align: 'center', fixedWidth: 3 },
		{ key: "LK", label: "LK", align: 'center', fixedWidth: 3 },
		{ key: "ZK", label: "ZK", align: 'center', fixedWidth: 3 },
		{ key: "AB3", label: "3", align: 'center', fixedWidth: 3 },
		{ key: "AB4", label: "4", align: 'center', fixedWidth: 3 }
	];

</script>
