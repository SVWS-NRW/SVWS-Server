<template>
	<div class="page--content">
		<svws-ui-content-card class="col-span-full table--with-background">
			<svws-ui-data-table :items="[]" :no-data="false" :columns="cols">
				<template #header>
					<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact select-none">
						<div role="columnheader" v-for="heading in colHeadings" :key="heading.text" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate"
							:class="{ [`col-span-${heading.cols.length}`]: true }">
							<div class="data-table__th-wrapper">
								<div class="data-table__th-title">
									{{ heading.text }}
								</div>
							</div>
						</div>
					</div>
					<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact select-none">
						<template v-for="heading in colHeadings" :key="heading.text">
							<div role="columnheader" v-for="(h, n) in heading.cols" :key="n" class="data-table__th data-table__thead__th"
								:class="{ 'data-table__th__align-center': h.center, 'data-table__th__separate': (n === heading.cols.length - 1) }">
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
									'bg-white/75 font-bold': isSelected(row, heading.text),
									'cursor-pointer': isSelectable(row, heading.text) }"
								@click="selectData(row, heading.text)">
								{{ getData(row, heading.text, h.text) }}
							</div>
						</template>
					</div>
				</template>
			</svws-ui-data-table>
		</svws-ui-content-card>
		<svws-ui-content-card :title="titleDetails">
			<div v-if="true">
				<!-- -->
			</div>
		</svws-ui-content-card>
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

	const selected = ref<{ row?: GostStatistikFachwahl, group?: string}>({ });

	function selectData(row: GostStatistikFachwahl, group: string) {
		if (isSelectable(row, group))
			selected.value = { row, group };
	}

	function isSelected(row: GostStatistikFachwahl, group: string): boolean {
		return ((selected.value.row?.id === row.id) && (selected.value.group === group));
	}

	function isSelectable(row: GostStatistikFachwahl, group: string) : boolean {
		if (group === "Fach")
			return true;
		if (group === "Abiturfach")
			return (row.wahlenAB3 !== 0) || (row.wahlenAB4 !== 0);
		const hj = GostHalbjahr.fromKuerzel(group);
		if (hj === null)
			return false;
		const fw = row.fachwahlen[hj.id];
		if (fw === null)
			return false;
		return (fw.wahlenGK !== 0) || (fw.wahlenZK !== 0) || (fw.wahlenLK !== 0);
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
		const hj = GostHalbjahr.fromKuerzel(group);
		if (hj === null)
			return "";
		const fw = row.fachwahlen[hj.id];
		if (fw === null)
			return "";
		switch (item) {
			case "M": return fw.wahlenGKMuendlich === 0 ? "" : fw.wahlenGKMuendlich;
			case "S": return fw.wahlenGKSchriftlich === 0 ? "" : fw.wahlenGKSchriftlich;
			case "GK": return fw.wahlenGK === 0 ? "" : fw.wahlenGK;
			case "ZK": return fw.wahlenZK === 0 ? "" : fw.wahlenZK;
			case "LK": return fw.wahlenLK === 0 ? "" : fw.wahlenLK;
		}
		return "";
	}

	const colHeadings: Array<{ text: string, cols: Array<{ text: string, center? : boolean }> }> = [
		{ text: "Fach", cols: [ { text : "Kürzel" }, { text: "Fach" } ] },
		{ text: "EF.1", cols: [ { text : "M", center: true }, { text : "S", center: true }, { text : "GK", center: true } ] },
		{ text: "EF.2", cols: [ { text : "M", center: true }, { text : "S", center: true }, { text : "GK", center: true } ] },
		{ text: "Q1.1", cols: [ { text : "M", center: true }, { text : "S", center: true }, { text : "GK", center: true }, { text : "LK", center: true } ] },
		{ text: "Q1.2", cols: [ { text : "M", center: true }, { text : "S", center: true }, { text : "GK", center: true }, { text : "LK", center: true } ] },
		{ text: "Q2.1", cols: [ { text : "M", center: true }, { text : "S", center: true }, { text : "GK", center: true }, { text : "ZK", center: true }, { text : "LK", center: true } ] },
		{ text: "Q2.2", cols: [ { text : "M", center: true }, { text : "S", center: true }, { text : "GK", center: true }, { text : "ZK", center: true }, { text : "LK", center: true } ] },
		{ text: "Abiturfach", cols: [ { text : "3", center: true }, { text : "4", center: true } ] },
	]

	const cols: Array<DataTableColumn> = [
		{ key: "Kuerzel", label: "Kürzel", span: 0.5, minWidth: 6 },
		{ key: "Fach", label: "Fach", span: 1.5, minWidth: 12},
		{ key: "M", label: "M", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "S", label: "S", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "GK", label: "GK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "M", label: "M", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "S", label: "S", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "GK", label: "GK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "M", label: "M", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "S", label: "S", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "GK", label: "GK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "LK", label: "LK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "M", label: "M", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "S", label: "S", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "GK", label: "GK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "LK", label: "LK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "M", label: "M", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "S", label: "S", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "GK", label: "GK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "ZK", label: "ZK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "LK", label: "LK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "M", label: "M", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "S", label: "S", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "GK", label: "GK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "ZK", label: "ZK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "LK", label: "LK", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "AB3", label: "3", align: 'center', span: 0.25, minWidth: 2.5 },
		{ key: "AB4", label: "4", align: 'center', span: 0.25, minWidth: 2.5 }
	];

	const titleDetails : ComputedRef<string> = computed(() => {
		if (selected.value.row === undefined) {
			return "Informationen zu allen Fächern";
		}
		return "Fachwahlen zum Fach " + selected.value.row.bezeichnung;
	});

</script>
