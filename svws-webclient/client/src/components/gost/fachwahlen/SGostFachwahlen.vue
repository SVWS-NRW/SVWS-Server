<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe> <hilfe-gost-fachwahlen /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-flex-row">
		<div title="Übersicht aller Fachwahlen im Jahrgang" class="max-w-300 min-w-fit h-full flex flex-col">
			<div class="flex-0 text-headline-md mb-4">Übersicht aller Fachwahlen im Jahrgang</div>
			<div class="grow overflow-y-auto pr-3 scrollbar-thin">
				<svws-ui-table :items="[]" :no-data="false" has-background :class="{'svws-fachwahlen--has-selection': selected().bereich !== 'Fach' || selected().idFach}">
					<template #header>
						<div role="row" class="svws-ui-tr select-none">
							<div v-for="(heading, index) in colHeadings" :key="heading.text" class="svws-ui-td cursor-pointer" role="columnheader" @click="isSelected(undefined, heading.text).value ? selectData(undefined, 'Fach') : selectData(undefined, heading.text)"
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
								<div v-for="(h, n) in heading.cols" class="svws-ui-td cursor-pointer" role="columnheader" :key="n" @click="isSelected(undefined, heading.text).value ? selectData(undefined, 'Fach') : selectData(undefined, heading.text)"
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
						<div role="row" v-for="row in fachwahlstatistik" :key="row.id" class="svws-ui-tr text-uistatic" :style="{ 'background-color': getBgColor(row) }">
							<template v-for="(heading, index) in colHeadings" :key="heading.text">
								<div v-for="(h, n) in heading.cols" :key="n" class="svws-ui-td select-none" role="cell" @click="isSelected(row, heading.text).value ? selectData(undefined, 'Fach') : selectData(row, heading.text)"
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
			</div>
		</div>
		<div class="min-w-176 h-full overflow-y-hidden flex flex-col">
			<div class="flex-0 text-headline-md mb-4">Schülerliste</div>
			<div class="grow h-full overflow-y-auto pr-3 scrollbar-thin">
				<router-view />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { onMounted, ref, computed } from "vue";
	import type { GostFachwahlenProps } from "./SGostFachwahlenProps";
	import type { GostStatistikFachwahl, GostStatistikFachwahlHalbjahr } from "@core";
	import { Fach, GostHalbjahr } from "@core";

	const props = defineProps<GostFachwahlenProps>();

	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const schuljahr = computed<number>(() => props.faecherManager.getSchuljahr());

	function getBgColor(fws: GostStatistikFachwahl) : string {
		if (fws.kuerzelStatistik === null)
			return 'rgb(220,220,220)';
		return Fach.getBySchluesselOrDefault(fws.kuerzelStatistik).getHMTLFarbeRGBA(schuljahr.value, 1.0);
	}

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

	const isSelectable = (row: GostStatistikFachwahl, bereich: string) => computed<boolean>(() => {
		if (bereich === "Fach")
			return true;
		if (bereich === "Abitur")
			return (row.wahlenAB3 !== 0) || (row.wahlenAB4 !== 0);
		if (bereich === "ZK")
			return getData(row, "ZK", "") !== "";
		if (bereich === "LK")
			return getData(row, "LK", "") !== "";
		const hj = GostHalbjahr.fromKuerzel(bereich);
		if (hj === null)
			return false;
		const fw = <GostStatistikFachwahlHalbjahr | null>row.fachwahlen[hj.id];
		if (fw === null)
			return false;
		return (fw.wahlenGK !== 0);
	})

	function getData(row: GostStatistikFachwahl, bereich: string, item: string): string | number {
		if ((bereich === "Fach") && (item === "Kürzel"))
			return row.kuerzel ?? "???";
		if ((bereich === "Fach") && (item === "Fach"))
			return row.bezeichnung ?? "???";
		if ((bereich === "Abitur") && (item === "3"))
			return row.wahlenAB3 > 0 ? row.wahlenAB3 : "";
		if ((bereich === "Abitur") && (item === "4"))
			return row.wahlenAB4 > 0 ? row.wahlenAB4 : "";
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
		const fw = <GostStatistikFachwahlHalbjahr | null>row.fachwahlen[hj.id];
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

</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: minmax(4rem, 1fr) minmax(12rem, 3fr) repeat(16, 3rem);
	}

	.svws-selectable {
		cursor: pointer;
	}

	.scrollbar-thin {
		scrollbar-gutter: stable;
		scrollbar-width: thin;
		scrollbar-color: rgba(0,0,0,0.2) transparent;
	}

</style>

