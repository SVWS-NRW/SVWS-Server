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
						<div v-for="(h, n) in heading.cols" :key="n" role="columnheader" class="data-table__th data-table__thead__th"
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
				<div v-for="row in rows"
					:key="row.id"
					class="data-table__tr data-table__tbody__tr"
					:style="{ 'background-color': getBgColor(row) }"
					role="row">
					<div role="cell" class="data-table__td" :style="{ 'background-color': getBgColor(row) }">
						{{ row.kuerzel }}
					</div>
					<div role="cell" class="data-table__td data-table__td__separate" :style="{ 'background-color': getBgColor(row) }">
						{{ row.bezeichnung }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[0] === null || row.fachwahlen[0].wahlenGKMuendlich === 0 ? "" : row.fachwahlen[0].wahlenGKMuendlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[0] === null || row.fachwahlen[0].wahlenGKSchriftlich === 0 ? "" : row.fachwahlen[0] .wahlenGKSchriftlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
						{{ row.fachwahlen[0] === null || row.fachwahlen[0].wahlenGK === 0 ? "" : row.fachwahlen[0].wahlenGK }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[1] === null || row.fachwahlen[1].wahlenGKMuendlich === 0 ? "" : row.fachwahlen[1].wahlenGKMuendlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[1] === null || row.fachwahlen[1].wahlenGKSchriftlich === 0 ? "" : row.fachwahlen[1].wahlenGKSchriftlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
						{{ row.fachwahlen[1] === null || row.fachwahlen[1].wahlenGK === 0 ? "" : row.fachwahlen[1].wahlenGK }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[2] === null || row.fachwahlen[2].wahlenGKMuendlich === 0 ? "" : row.fachwahlen[2].wahlenGKMuendlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[2] === null || row.fachwahlen[2].wahlenGKSchriftlich === 0 ? "" : row.fachwahlen[2].wahlenGKSchriftlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[2] === null || row.fachwahlen[2].wahlenGK === 0 ? "" : row.fachwahlen[2].wahlenGK }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
						{{ row.fachwahlen[2] === null || row.fachwahlen[2].wahlenLK === 0 ? "" : row.fachwahlen[2].wahlenLK }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[3] === null || row.fachwahlen[3].wahlenGKMuendlich === 0 ? "" : row.fachwahlen[3].wahlenGKMuendlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[3] === null || row.fachwahlen[3].wahlenGKSchriftlich === 0 ? "" : row.fachwahlen[3].wahlenGKSchriftlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[3] === null || row.fachwahlen[3].wahlenGK === 0 ? "" : row.fachwahlen[3].wahlenGK }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
						{{ row.fachwahlen[3] === null || row.fachwahlen[3].wahlenLK === 0 ? "" : row.fachwahlen[3].wahlenLK }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[4] === null || row.fachwahlen[4].wahlenGKMuendlich === 0 ? "" : row.fachwahlen[4].wahlenGKMuendlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[4] === null || row.fachwahlen[4].wahlenGKSchriftlich === 0 ? "" : row.fachwahlen[4].wahlenGKSchriftlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[4] === null || row.fachwahlen[4].wahlenGK === 0 ? "" : row.fachwahlen[4].wahlenGK }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[4] === null || row.fachwahlen[4].wahlenZK === 0 ? "" : row.fachwahlen[4].wahlenZK }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
						{{ row.fachwahlen[4] === null || row.fachwahlen[4].wahlenLK === 0 ? "" : row.fachwahlen[4].wahlenLK }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[5] === null || row.fachwahlen[5].wahlenGKMuendlich === 0 ? "" : row.fachwahlen[5].wahlenGKMuendlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[5] === null || row.fachwahlen[5].wahlenGKSchriftlich === 0 ? "" : row.fachwahlen[5].wahlenGKSchriftlich }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[5] === null || row.fachwahlen[5].wahlenGK === 0 ? "" : row.fachwahlen[5].wahlenGK }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.fachwahlen[5] === null || row.fachwahlen[5].wahlenZK === 0 ? "" : row.fachwahlen[5].wahlenZK }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center data-table__td__separate">
						{{ row.fachwahlen[5]?.wahlenLK || "" }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.wahlenAB3 || "" }}
					</div>
					<div role="cell" class="data-table__td data-table__td__align-center">
						{{ row.wahlenAB4 || "" }}
					</div>
				</div>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { GostStatistikFachwahl, List} from "@core";
	import { ZulaessigesFach } from "@core";
	import type {DataTableColumn} from "@ui";

	const props = defineProps<{
		fachwahlen: List<GostStatistikFachwahl>;
	}>();

	const rows: ComputedRef<GostStatistikFachwahl[]> = computed(() => props.fachwahlen.toArray() as GostStatistikFachwahl[]);

	const getBgColor = (row: GostStatistikFachwahl) => ZulaessigesFach.getByKuerzelASD(row.kuerzelStatistik).getHMTLFarbeRGBA(1.0);

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
