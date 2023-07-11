<template>
	<svws-ui-content-card class="col-span-full table--with-background">
		<svws-ui-data-table :items="[]" :no-data="false" :columns="cols">
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
					<div role="columnheader" v-for="heading in colHeadings" :key="heading.text" class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate"
						:class="{ [`col-span-${heading.cols.length}`]: true }">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								{{ heading.text }}
							</div>
						</div>
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
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
						<div role="cell" v-for="(h, n) in heading.cols" :key="n" class="data-table__td" :class="{ 'data-table__th__align-center': h.center, 'data-table__th__separate': (n === heading.cols.length - 1) }">
							{{ getData(row, heading.text, h.text) }}
						</div>
					</template>
				</div>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { GostStatistikFachwahl, List} from "@core";
	import { GostHalbjahr, ZulaessigesFach } from "@core";
	import type {DataTableColumn} from "@ui";

	const props = defineProps<{
		fachwahlen: List<GostStatistikFachwahl>;
	}>();

	const rows: ComputedRef<GostStatistikFachwahl[]> = computed(() => props.fachwahlen.toArray() as GostStatistikFachwahl[]);

	const getBgColor = (row: GostStatistikFachwahl) => ZulaessigesFach.getByKuerzelASD(row.kuerzelStatistik).getHMTLFarbeRGBA(1.0);

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

</script>
