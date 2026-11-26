<template>
	<ui-table-grid class="max-h-72! max-w-full"
		:manager="() => gridManager"
		hide-selection
		name="Schulleitungsfunktionen">
		<template #header>
			<template v-for="col of gridManager.cols.values()" :key="col.name">
				<th v-if="col.kuerzel === 'gotoLink'" :aria-label="col.name">
					<span class="icon i-ri-group-line" />
				</th>
				<th v-else class="text-left">
					{{ col.kuerzel }}
				</th>
			</template>
		</template>
		<template #default="{ row }">
			<td>
				<div class="flex h-full items-center justify-center">
					<svws-ui-button v-if="getLehrer(row) !== undefined" type="icon" class="ui-table-actions--button" title="Lehrkraft öffnen" aria-label="Lehrkraft öffnen" @click="gotoLehrer(row.idLehrer)">
						<span class="icon i-ri-link" />
					</svws-ui-button>
				</div>
			</td>
			<td class="text-left">{{ getLeitungsfunktionText(row) }}</td>
			<td class="text-left">
				<span v-if="isEmpty(row.bezeichnung)" class="text-ui-secondary">—</span>
				<template v-else>{{ row.bezeichnung }}</template>
			</td>
			<td class="text-left">
				<span v-if="getLehrer(row) === undefined" class="text-ui-secondary">—</span>
				<template v-else>{{ getLehrer(row)?.kuerzel }}</template>
			</td>
			<td class="text-left">
				<span v-if="getLehrer(row) === undefined" class="text-ui-secondary">—</span>
				<template v-else>{{ getLehrer(row)?.nachname }}</template>
			</td>
			<td class="text-left">
				<span v-if="getLehrer(row) === undefined" class="text-ui-secondary">—</span>
				<template v-else>{{ getLehrer(row)?.vorname }}</template>
			</td>
			<td class="text-left">
				<span v-if="row.datumBeginnLeitungsfunktion === null" class="text-ui-secondary">—</span>
				<template v-else>{{ formatToLocalDate(row.datumBeginnLeitungsfunktion) }}</template>
			</td>
			<td class="text-left">
				<span v-if="row.datumEndeLeitungsfunktion === null" class="text-ui-secondary">—</span>
				<template v-else>{{ formatToLocalDate(row.datumEndeLeitungsfunktion) }}</template>
			</td>
		</template>
	</ui-table-grid>
</template>

<script lang="ts" setup>

	import { computed } from "vue";

	import type { Schulleitung } from "@core/asd/data/schule/Schulleitung";
	import { LehrerLeitungsfunktion } from "@core/asd/types/lehrer/LehrerLeitungsfunktion";
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import type { List } from "@core/java/util/List";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";

	import { dateToday, formatToLocalDate } from "~/utils/date";

	const props = defineProps<{
		getListLeitungsfunktionen: () => List<Schulleitung>;
		getListLehrer: () => List<LehrerListeEintrag>;
		gotoLehrer: (idLehrer: number) => Promise<void>;
		nurAktive: boolean;
	}>();

	/** Die Leitungsfunktionen, bei Bedarf nur die aktiven, sortiert nach Funktion und Beginn der Leitungsfunktion */
	const sortedLeitungsfunktionen = computed<Schulleitung[]>(() => [...props.getListLeitungsfunktionen()]
		.filter(isShown)
		.sort(compareLeitungsfunktionen));

	/** Vergleicht zwei Leitungsfunktionen zunächst nach der Funktion und dann nach dem Beginn der Leitungsfunktion. */
	function compareLeitungsfunktionen(a: Schulleitung, b: Schulleitung): number {
		if (a.idLeitungsfunktion !== b.idLeitungsfunktion) {
			return a.idLeitungsfunktion - b.idLeitungsfunktion;
		}
		return (a.datumBeginnLeitungsfunktion ?? "").localeCompare(b.datumBeginnLeitungsfunktion ?? "");
	}

	const lehrerMap = computed(() => new Map([...props.getListLehrer()].map(lehrer => [lehrer.id, lehrer])));

	const gridManager = computed(() => new GridManager<number, Schulleitung, Schulleitung[]>({
		daten: sortedLeitungsfunktionen,
		getRowKey: leitungsfunktion => leitungsfunktion.id,
		columns: [
			{ kuerzel: "gotoLink", name: "Lehrkraft öffnen", width: "2.5rem", hideable: false },
			{ kuerzel: "Leitungsfunktion", name: "Leitungsfunktion", width: "minmax(8rem, 1.5fr)" },
			{ kuerzel: "Bezeichnung", name: "Bezeichnung", width: "minmax(8rem, 1.5fr)" },
			{ kuerzel: "Kürzel", name: "Kürzel", width: "minmax(5rem, 0.75fr)" },
			{ kuerzel: "Nachname", name: "Nachname", width: "minmax(6rem, 1fr)" },
			{ kuerzel: "Rufname", name: "Rufname", width: "minmax(6rem, 1fr)" },
			{ kuerzel: "Von", name: "Von", width: "minmax(6rem, 0.75fr)" },
			{ kuerzel: "Bis", name: "Bis", width: "minmax(6rem, 0.75fr)" },
		],
	}));

	function isShown(leitungsfunktion: Schulleitung): boolean {
		return !props.nurAktive || isActive(leitungsfunktion);
	}

	/** Eine Leitungsfunktion ist aktiv, wenn das heutige Datum zwischen Beginn und Ende liegt (leere Grenzen sind offen). */
	function isActive(leitungsfunktion: Schulleitung): boolean {
		const today = dateToday();
		const begin = leitungsfunktion.datumBeginnLeitungsfunktion;
		const end = leitungsfunktion.datumEndeLeitungsfunktion;
		return ((begin === null) || (begin <= today)) && ((end === null) || (end >= today));
	}

	function getLeitungsfunktionText(leitungsfunktion: Schulleitung): string {
		return LehrerLeitungsfunktion.data().getEintragByID(leitungsfunktion.idLeitungsfunktion)?.text ?? "—";
	}

	function getLehrer(leitungsfunktion: Schulleitung): LehrerListeEintrag | undefined {
		return lehrerMap.value.get(leitungsfunktion.idLehrer);
	}

	function isEmpty(value: string | null): boolean {
		return (value === null) || (value === "");
	}

</script>
