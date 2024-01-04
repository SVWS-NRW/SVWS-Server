<template>
	<svws-ui-content-card title="Klausuren" v-if="hatKlausurManager()">
		<svws-ui-table :items="klausurManager().schuelerklausurGetMengeAsList()" :columns="colsKlausuren">
			<template #cell(quartal)="{ rowData }">
				{{ klausurManager().vorgabeBySchuelerklausur(rowData).quartal }}
			</template>
			<template #cell(termin)="{ rowData }">
				<svws-ui-table :items="rowData.schuelerklausurTermine" :columns="colsTermine" disable-header>
					<template #cell(datum)="{ rowData: termin }">
						{{ klausurManager().terminBySchuelerklausurTermin(termin) !== null ? (klausurManager().terminBySchuelerklausurTermin(termin)!.datum !== null ? DateUtils.gibDatumGermanFormat(klausurManager().terminBySchuelerklausurTermin(termin)!.datum!) : "N.N.") : "N.N." }}
					</template>
					<template #cell(button)="{ rowData: termin }">
						<svws-ui-button v-if="klausurManager().istAktuellerSchuelerklausurtermin(termin)" class="mt-4" @click="createSchuelerklausurTermin(rowData)">Nicht teilgenommen</svws-ui-button>
					</template>
				</svws-ui-table>
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

				<!--{{ klausurManager().terminKursklausurBySchuelerklausur(rowData).datum !== null ? DateUtils.gibDatumGermanFormat(klausurManager().terminKursklausurBySchuelerklausur(rowData).datum) : "" }}-->

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import { DateUtils } from "@core";
	import type { SchuelerLernabschnittGostKlausurenProps } from "./SSchuelerLernabschnittGostKlausurenProps";

	const props = defineProps<SchuelerLernabschnittGostKlausurenProps>();

	const colsKlausuren: Array<DataTableColumn> = [
		{ key: "quartal", label: "Quartal", tooltip: "Ursprüngliches Datum der Klausur" },
		{ key: "kurs", label: "Kurs", tooltip: "Kurs" },
		{ key: "lehrer", label: "Lehrer:in", tooltip: "Lehrer:in" },
		{ key: "termin", label: " ", tooltip: "Ursprüngliches Datum der Klausur", minWidth: 6 },
	];

	const colsTermine: Array<DataTableColumn> = [
		{ key: "datum", label: "Datum", tooltip: "Ursprüngliches Datum der Klausur" },
		{ key: "button", label: " ", tooltip: "" },
	];

</script>
