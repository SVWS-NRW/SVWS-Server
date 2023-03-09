<template>
	<svws-ui-table :model-value="lernabschnitt" @update:model-value="setLernabschnitt" :columns="cols" :data="(lernabschnitte.toArray() as SchuelerLernabschnittListeEintrag[])" :footer="false">
		<template #cell-schuljahresabschnitt="{row}">
			{{ row.schuljahr + "." + row.abschnitt + (row.wechselNr === null ? "" : " (alt)") }}
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { SchuelerLernabschnittListeEintrag } from "@svws-nrw/svws-core";
	import { DataTableItem } from "@ui";
	import { ref } from "vue";
	import { SchuelerAbschnittAuswahlProps } from "./SSchuelerAbschnittAuswahlProps";

	const props = defineProps<SchuelerAbschnittAuswahlProps>();

	async function setLernabschnitt(value: DataTableItem) {
		return await props.setLernabschnitt(value as SchuelerLernabschnittListeEintrag | undefined);
	}

	const cols = ref([
		{ key: "schuljahresabschnitt", label: "Abschnitt" },
	]);

</script>
