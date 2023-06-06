<template>
	<svws-ui-content-card class="col-span-full">
		<svws-ui-data-table :items="[]" :no-data="false" :columns="cols">
			<template #header>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-2 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Fach
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-3 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								EF.1
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-3 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								EF.2
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-4 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q1.1
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-4 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q1.2
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-5 data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q2.1
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-5 data-table__th__separate	">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Q2.2
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center col-span-2 data-table__th__separate	">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Abiturfach
							</div>
						</div>
					</div>
				</div>
				<div role="row" class="data-table__tr data-table__thead__tr data-table__thead__tr__compact">
					<div role="columnheader"
						class="data-table__th data-table__thead__th">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Kürzel
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								Fach
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								M
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								S
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								GK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								M
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								S
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								GK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								M
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								S
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								GK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								LK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								M
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								S
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								GK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								LK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								M
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								S
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								GK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								ZK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								LK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								M
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								S
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								GK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								ZK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center data-table__th__separate">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								LK
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								3
							</div>
						</div>
					</div>
					<div role="columnheader"
						class="data-table__th data-table__thead__th data-table__th__align-center">
						<div class="data-table__th-wrapper">
							<div class="data-table__th-title">
								4
							</div>
						</div>
					</div>
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

<style scoped>
	.table-container {
		height: 100%;
		width: 100%;

		display: flex;
		position: relative;
		flex: 1 1 auto;
		flex-direction: row;
	}

	.table {
		height: 100%;
		width: calc(100% - 0.5em);
		overflow: visible;

		display: flex;
		position: relative;
		flex: 1 1 auto;
		flex-direction: column;
	}

	.tableRow {
		height: 1.6em;
		line-height: 1.6em;
		display: grid;
		grid-template-rows: 1.6em;
	}

	div.tableRow.table-head {
		color: var(--table-head-foreground-color);
		background-color: var(--table-head-background-color);
	}

	.cell {
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		border: 1px solid rgb(127, 127, 127, 0.2);
		border-left: 0px;
		border-top: 0px;
		padding: var(--table-cell-default-padding);
	}

	.combocell {
		overflow: visible;
	}

	.head {
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		border: 1px solid var(--table-head-foreground-color);
		border-left: 0px;
		border-top: 0px;
		padding: var(--table-cell-default-padding);
	}

	.waehlbar {
		cursor: pointer;
	}
</style>
