<template>
	<svws-ui-data-table :clicked="lernabschnitt" @update:clicked="setLernabschnitt" :columns="cols" :items="lernabschnitte" :footer="false" clickable>
		<template #cell="{rowData: row}">
			{{ row.schuljahr + "." + row.abschnitt + (row.wechselNr === null ? "" : " (alt)") }}
		</template>
	</svws-ui-data-table>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import { SchuelerLeistungenAuswahlProps } from "./SSchuelerLeistungenAuswahlProps";

	const props = defineProps<SchuelerLeistungenAuswahlProps>();

	const cols = ref([
		{ key: "schuljahresabschnitt", label: "Abschnitt" },
	]);

</script>
