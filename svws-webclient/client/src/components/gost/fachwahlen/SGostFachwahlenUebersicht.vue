<template>
	<svws-ui-content-card title="Übersicht aller Fachwahlen im Jahrgang">
		<svws-ui-table :items="[]" :no-data="false" :columns="cols" has-background :class="{'svws-fachwahlen--has-selection': selected().bereich !== 'Fach' || selected().idFach}">
			<template #header>
				<div role="row" class="svws-ui-tr select-none">
					<div v-for="(heading, index) in colHeadings" :key="heading.text" class="svws-ui-td cursor-pointer" role="columnheader" @click="selectData(undefined, heading.text)"
						:class="{
							[`col-span-${heading.cols.length}`]: true,
							'svws-divider': index !== colHeadings.length - 1,
							'svws-align-center': index !== 0,
							'svws-selected': isSelected(undefined, heading.text).value,
						}">
						{{ heading.text }}
						<svws-ui-tooltip v-if="selected().bereich === heading.text && selected().bereich === 'Fach' && !selected().idFach" indicator="help">
							<span class="ml-auto">(alle ausgewählt)</span>
							<template #content>
								Klicke auf eine Reihe, Spalte oder einzelne Zelle, um Details zu sehen.
							</template>
						</svws-ui-tooltip>
					</div>
				</div>
				<div role="row" class="svws-ui-tr select-none">
					<template v-for="(heading, index) in colHeadings" :key="heading.text">
						<div v-for="(h, n) in heading.cols" class="svws-ui-td cursor-pointer" role="columnheader" :key="n" @click="selectData(undefined, heading.text)"
							:class="{
								'svws-align-center': h.center,
								'svws-divider': (n === heading.cols.length - 1 && index !== colHeadings.length - 1),
								'svws-selected': isSelected(undefined, heading.text).value,
							}">
							{{ h.text }}
						</div>
					</template>
				</div>
			</template>
			<template #body>
				<div role="row" v-for="row in fachwahlstatistik" :key="row.id" class="svws-ui-tr" :style="{ '--background-color': getBgColor(row) }">
					<template v-for="(heading, index) in colHeadings" :key="heading.text">
						<div v-for="(h, n) in heading.cols" :key="n" class="svws-ui-td select-none" role="cell" @click="selectData(row, heading.text)"
							:class="{
								'svws-align-center': h.center,
								'svws-divider': (n === heading.cols.length - 1) && (index !== colHeadings.length - 1),
								'svws-selected': isSelected(row, heading.text).value,
								'svws-selectable': isSelectable(row, heading.text).value,
							}">
							<span class="line-clamp-1 break-all leading-tight -my-0.5">{{ getData(row, heading.text, h.text) }}</span>
						</div>
					</template>
				</div>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import type { GostFachwahlenProps } from "./SGostFachwahlenProps";
	import type { DataTableColumn } from "@ui";
	import { GostHalbjahr, ZulaessigesFach, type GostStatistikFachwahl } from "@core";

	const props = defineProps<GostFachwahlenProps>();

	const getBgColor = (row: GostStatistikFachwahl) => ZulaessigesFach.getByKuerzelASD(row.kuerzelStatistik).getHMTLFarbeRGBA(1.0);

	async function selectData(row: GostStatistikFachwahl | undefined, bereich: string) {
		if ((row !== undefined) && (!isSelectable(row, bereich).value))
			return;
		// Lade die neue Auswahl
		const fach_id = (row === undefined) ? undefined : row.id;
		const halbjahr = GostHalbjahr.fromKuerzel(bereich); // nur für GKs
		switch (bereich) {
			case "Fach":
				await props.doSelect(fach_id, undefined);
				break;
			case "ZK":
			case "LK":
				await props.doSelect(fach_id, bereich);
				break;
			case "Abitur":
				await props.doSelect(fach_id, "Abi");
				break;
			default:
				if (halbjahr !== null)
					await props.doSelect(fach_id, "Halbjahr", halbjahr);
				break;
		}
	}

	const isSelected = (row: GostStatistikFachwahl | undefined, bereich: string) => computed<boolean>(() => {
		const selected = props.selected();
		if (selected.idFach === undefined) {
			// Prüfe, ob alle Fächer ausgewählt sind. In diesem Fall wird keine Auswahl angezeigt
			if (selected.bereich === "Fach")
				return false;
			// Prüfe, ob eine Spalte ausgewählt ist. In diesem Fall werden alle Zellen der Spalte hervorgehoben
			return (selected.bereich === bereich);
		} else {
			// Prüfe, ob ein Zeile, d.h. ein Fach ausgewählt ist
			if ((selected.idFach === row?.id) && (selected.bereich === "Fach"))
				return true;
			// Prüfe, ob die jeweilige Zelle ausgewählt ist
			return ((selected.idFach === row?.id) && (props.selected().bereich === bereich));
		}
	})

	const isSelectable = (row: GostStatistikFachwahl, bereich: string) => computed(() => {
		if (bereich === "Fach")
			return true;
		if (bereich === "Abitur")
			return (row.wahlenAB3 !== 0) || (row.wahlenAB4 !== 0);
		if ((bereich === "ZK") || (bereich === "LK"))
			return false; // Evtl. für weitere Ansichten: return getData(row, group, "") !== "";
		const hj = GostHalbjahr.fromKuerzel(bereich);
		if (hj === null)
			return false;
		const fw = row.fachwahlen[hj.id];
		if (fw === null)
			return false;
		return (fw.wahlenGK !== 0);
	})

	function getData(row: GostStatistikFachwahl, bereich: string, item: string) {
		if ((bereich === "Fach") && (item === "Kürzel"))
			return row.kuerzel;
		if ((bereich === "Fach") && (item === "Fach"))
			return row.bezeichnung;
		if ((bereich === "Abitur") && (item === "3"))
			return row.wahlenAB3 || "";
		if ((bereich === "Abitur") && (item === "4"))
			return row.wahlenAB4 || "";
		if ((bereich === "ZK") && (item === "")) {
			const maxZK = Math.max(row.fachwahlen[2].wahlenZK, row.fachwahlen[3].wahlenZK, row.fachwahlen[4].wahlenZK, row.fachwahlen[5].wahlenZK);
			return maxZK === 0 ? "" : maxZK;
		}
		if ((bereich === "LK") && (item === "")) {
			const maxLK = Math.max(row.fachwahlen[2].wahlenLK, row.fachwahlen[3].wahlenLK, row.fachwahlen[4].wahlenLK, row.fachwahlen[5].wahlenLK);
			return maxLK === 0 ? "" : maxLK;
		}
		const hj = GostHalbjahr.fromKuerzel(bereich);
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
		{ text: "Abitur", cols: [ { text : "3", center: true }, { text : "4", center: true } ] },
	]

	const cols: DataTableColumn[] = [
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

<style lang="postcss" scoped>
.svws-selectable {
  @apply cursor-pointer;
}
</style>
