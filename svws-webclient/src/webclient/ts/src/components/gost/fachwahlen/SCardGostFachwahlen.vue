<template>
	<svws-ui-content-card>
		<div class="content-wrapper">
			<div class="input-wrapper">
				<div class="table-container">
					<div class="default-font-small elevation-5 table"
						style="width: 68em">
						<div ref="tbody"
							class="default-font-small"
							style="width=100%; height: 100%">
							<div ref="thead"
								class="table-head tableRow"
								style="
									grid-template-columns: 20em 6em 6em 8em 8em 10em 10em;
								">
								<div class="head cell center">Fach</div>
								<div class="head cell center">EF.1</div>
								<div class="head cell center">EF.2</div>
								<div class="head cell center">Q1.1</div>
								<div class="head cell center">Q1.2</div>
								<div class="head cell center">Q2.1</div>
								<div class="head cell center">Q2.2</div>
							</div>
							<div ref="thead"
								class="table-head tableRow"
								style="
									grid-template-columns: 5em 15em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em;
								">
								<div class="head cell center">Kürzel</div>
								<div class="head cell center">Fach</div>
								<div class="head cell center">M</div>
								<div class="head cell center">S</div>
								<div class="head cell center">GK</div>
								<div class="head cell center">M</div>
								<div class="head cell center">S</div>
								<div class="head cell center">GK</div>
								<div class="head cell center">M</div>
								<div class="head cell center">S</div>
								<div class="head cell center">GK</div>
								<div class="head cell center">LK</div>
								<div class="head cell center">M</div>
								<div class="head cell center">S</div>
								<div class="head cell center">GK</div>
								<div class="head cell center">LK</div>
								<div class="head cell center">M</div>
								<div class="head cell center">S</div>
								<div class="head cell center">GK</div>
								<div class="head cell center">ZK</div>
								<div class="head cell center">LK</div>
								<div class="head cell center">M</div>
								<div class="head cell center">S</div>
								<div class="head cell center">GK</div>
								<div class="head cell center">ZK</div>
								<div class="head cell center">LK</div>
							</div>
							<div v-for="row in rows"
								:key="row.id"
								class="tableRow"
								style="grid-template-columns: 5em 15em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em 2em;">
								<div class="cell text-left"
									:style="{
										'background-color': getBgColor(row)
									}">
									{{ row.kuerzel }}
								</div>
								<div class="cell text-left"
									:style="{
										'background-color': getBgColor(row)
									}">
									{{ row.bezeichnung }}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[0] == null ||
											row.fachwahlen[0].wahlenGKMuendlich == 0
											? ""
											: row.fachwahlen[0]
												.wahlenGKMuendlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[0] == null ||
											row.fachwahlen[0].wahlenGKSchriftlich ==
											0
											? ""
											: row.fachwahlen[0]
												.wahlenGKSchriftlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[0] == null ||
											row.fachwahlen[0].wahlenGK == 0
											? ""
											: row.fachwahlen[0].wahlenGK
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[1] == null ||
											row.fachwahlen[1].wahlenGKMuendlich == 0
											? ""
											: row.fachwahlen[1]
												.wahlenGKMuendlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[1] == null ||
											row.fachwahlen[1].wahlenGKSchriftlich ==
											0
											? ""
											: row.fachwahlen[1]
												.wahlenGKSchriftlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[1] == null ||
											row.fachwahlen[1].wahlenGK == 0
											? ""
											: row.fachwahlen[1].wahlenGK
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[2] == null ||
											row.fachwahlen[2].wahlenGKMuendlich == 0
											? ""
											: row.fachwahlen[2]
												.wahlenGKMuendlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[2] == null ||
											row.fachwahlen[2].wahlenGKSchriftlich ==
											0
											? ""
											: row.fachwahlen[2]
												.wahlenGKSchriftlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[2] == null ||
											row.fachwahlen[2].wahlenGK == 0
											? ""
											: row.fachwahlen[2].wahlenGK
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[2] == null ||
											row.fachwahlen[2].wahlenLK == 0
											? ""
											: row.fachwahlen[2].wahlenLK
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[3] == null ||
											row.fachwahlen[3].wahlenGKMuendlich == 0
											? ""
											: row.fachwahlen[3]
												.wahlenGKMuendlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[3] == null ||
											row.fachwahlen[3].wahlenGKSchriftlich ==
											0
											? ""
											: row.fachwahlen[3]
												.wahlenGKSchriftlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[3] == null ||
											row.fachwahlen[3].wahlenGK == 0
											? ""
											: row.fachwahlen[3].wahlenGK
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[3] == null ||
											row.fachwahlen[3].wahlenLK == 0
											? ""
											: row.fachwahlen[3].wahlenLK
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[4] == null ||
											row.fachwahlen[4].wahlenGKMuendlich == 0
											? ""
											: row.fachwahlen[4]
												.wahlenGKMuendlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[4] == null ||
											row.fachwahlen[4].wahlenGKSchriftlich ==
											0
											? ""
											: row.fachwahlen[4]
												.wahlenGKSchriftlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[4] == null ||
											row.fachwahlen[4].wahlenGK == 0
											? ""
											: row.fachwahlen[4].wahlenGK
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[4] == null ||
											row.fachwahlen[4].wahlenZK == 0
											? ""
											: row.fachwahlen[4].wahlenZK
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[4] == null ||
											row.fachwahlen[4].wahlenLK == 0
											? ""
											: row.fachwahlen[4].wahlenLK
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[5] == null ||
											row.fachwahlen[5].wahlenGKMuendlich == 0
											? ""
											: row.fachwahlen[5]
												.wahlenGKMuendlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[5] == null ||
											row.fachwahlen[5].wahlenGKSchriftlich ==
											0
											? ""
											: row.fachwahlen[5]
												.wahlenGKSchriftlich
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[5] == null ||
											row.fachwahlen[5].wahlenGK == 0
											? ""
											: row.fachwahlen[5].wahlenGK
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[5] == null ||
											row.fachwahlen[5].wahlenZK == 0
											? ""
											: row.fachwahlen[5].wahlenZK
									}}
								</div>
								<div class="cell center">
									{{
										row.fachwahlen[5] == null ||
											row.fachwahlen[5].wahlenLK == 0
											? ""
											: row.fachwahlen[5].wahlenLK
									}}
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, ref, ShallowRef } from "vue";
	import { GostJahrgang, GostStatistikFachwahl, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { DataGostSchuelerFachwahlen } from "~/apps/gost/DataGostSchuelerFachwahlen";

	const { item, dataFachwahlen } = defineProps<{
		item: ShallowRef<GostJahrgang | undefined>;
		dataFachwahlen: DataGostSchuelerFachwahlen;
	}>();

	const nichtWaehlbar = ref("");
	const waehlbarAusgewaehlt = ref("\u2705");
	const waehlbarNichtAusgewaehlt = ref("\u274C");

	const bezeichnung: ComputedRef<string | undefined> = computed(() => item.value?.bezeichnung?.toString());

	const rows: ComputedRef<GostStatistikFachwahl[]> = computed(() => dataFachwahlen.daten?.toArray() as GostStatistikFachwahl[] || []);

	const getBgColor = (row: GostStatistikFachwahl) => ZulaessigesFach.getByKuerzelASD(row.kuerzelStatistik).getHMTLFarbeRGBA(1.0).valueOf();

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
