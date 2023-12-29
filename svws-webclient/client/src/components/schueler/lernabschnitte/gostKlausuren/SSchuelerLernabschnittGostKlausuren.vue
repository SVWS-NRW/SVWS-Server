<template>
	<svws-ui-content-card title="Klausuren" v-if="hatKlausurManager()">
		<svws-ui-table :items="klausurManager().schuelerklausurGetMengeAsList()" :columns="colsKlausuren">
			<template #cell(quartal)="{ rowData }">
				{{ klausurManager().vorgabeBySchuelerklausur(rowData).quartal }}
			</template>
			<template #cell(datum)="{ rowData }">
				{{ klausurManager().terminKursklausurBySchuelerklausur(rowData).datum !== null ? DateUtils.gibDatumGermanFormat(klausurManager().terminKursklausurBySchuelerklausur(rowData).datum) : "" }}
			</template>
			<template #cell(kurs)="{ rowData }">
				{{ manager().kursGetByIdOrException(klausurManager().kursklausurBySchuelerklausur(rowData).idKurs).kuerzel }}
			</template>
			<template #cell(lehrer)="{ rowData }">
				{{ manager().kursGetByIdOrException(klausurManager().kursklausurBySchuelerklausur(rowData).idKurs).lehrer !== null ? manager().lehrerGetByIdOrException(manager().kursGetByIdOrException(klausurManager().kursklausurBySchuelerklausur(rowData).idKurs).lehrer!).kuerzel : "N.N." }}
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
	<div v-else>
		Kein Gost-Lernabschnitt ausgewählt.
	</div>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import { DateUtils } from "@core";
	import type { SchuelerLernabschnittGostKlausurenProps } from "./SSchuelerLernabschnittGostKlausurenProps";

	const props = defineProps<SchuelerLernabschnittGostKlausurenProps>();

	const colsKlausuren: Array<DataTableColumn> = [
		{ key: "quartal", label: "Quartal", tooltip: "Ursprüngliches Datum der Klausur" },
		{ key: "kurs", label: "Kurs", tooltip: "Kurs" },
		{ key: "lehrer", label: "Lehrer:in", tooltip: "Lehrer:in" },
		{ key: "datum", label: "Datum", tooltip: "Ursprüngliches Datum der Klausur", sortable: true},
	];

</script>
