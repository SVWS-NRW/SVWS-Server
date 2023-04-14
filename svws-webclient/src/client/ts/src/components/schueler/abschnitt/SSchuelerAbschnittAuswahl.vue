<template>
	<svws-ui-data-table :clicked="lernabschnitt" @update:clicked="gotoLernabschnitt" clickable :columns="cols" :items="lernabschnitte" :footer="false">
		<template #cell="{rowData: row}">
			{{ row.schuljahr + "." + row.abschnitt + (row.wechselNr === null ? "" : " (alt)") }}
		</template>
	</svws-ui-data-table>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { SchuelerAbschnittAuswahlProps } from "./SSchuelerAbschnittAuswahlProps";

	const props = defineProps<SchuelerAbschnittAuswahlProps>();

	const cols = ref([
		{ key: "schuljahresabschnitt", label: "Abschnitt" },
	]);

</script>
