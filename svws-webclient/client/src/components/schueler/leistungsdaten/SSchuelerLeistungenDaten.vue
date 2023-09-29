<template>
	<svws-ui-content-card v-if="data">
		<svws-ui-table :columns="cols" :items="props.data?.leistungsdaten">
			<template #cell(fachID)="{rowData}">
				<s-schueler-leistung-fach :fach="rowData.fachID" :map-faecher="mapFaecher" />
			</template>
			<template #cell(kursID)="{rowData}">
				<s-schueler-leistung-kurs :kurs="rowData.kursID" :map-kurse="mapKurse" />
			</template>
			<template #cell(lehrerID)="{rowData}">
				<s-schueler-leistung-fachlehrer :fachlehrer="rowData.lehrerID" :map-lehrer="props.mapLehrer" />
			</template>
			<template #cell(note)="{rowData}">
				<s-schueler-leistung-note :data="data" :note="rowData.note ?? ''" :patch-leistung="patchLeistung" />
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { SchuelerLeistungenDatenProps } from "./SSchuelerLeistungenDatenProps";

	const props = defineProps<SchuelerLeistungenDatenProps>();

	const cols = [
		{ key: "fachID", label: "Fach", span: 0.75, sortable: true, minWidth: 14 },
		{ key: "kursID", label: "Kurs", span: 0.75, sortable: true, minWidth: 14 },
		{ key: "lehrerID", label: "Lehrer", span: 1, sortable: true, minWidth: 20 },
		{ key: "note", label: "Note", span: 0.25, sortable: true },
	];

</script>


