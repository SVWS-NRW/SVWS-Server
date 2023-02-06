<template>
	<svws-ui-table :model-value="lernabschnitt" @update:model-value="setLernabschnitt" :columns="cols" :data="(lernabschnitte.toArray() as SchuelerLernabschnittListeEintrag[])" :footer="false">
		<template #cell-schuljahresabschnitt="{row}">
			{{ row.schuljahr + "." + row.abschnitt + (row.wechselNr === null ? "" : " (alt)") }}
		</template>
	</svws-ui-table>
</template>

<script setup lang="ts">

	import { List, SchuelerLernabschnittListeEintrag } from "@svws-nrw/svws-core-ts";
	import { DataTableItem } from "@svws-nrw/svws-ui";
	import { ref } from "vue";

	const props = defineProps<{
		lernabschnitt: SchuelerLernabschnittListeEintrag | undefined;
		lernabschnitte: List<SchuelerLernabschnittListeEintrag>;
		setLernabschnitt: (value: SchuelerLernabschnittListeEintrag | undefined) => Promise<void>;
	}>();

	async function setLernabschnitt(value: DataTableItem) {
		return await props.setLernabschnitt(value as SchuelerLernabschnittListeEintrag | undefined);
	}

	const cols = ref([
		{ key: "schuljahresabschnitt", label: "Abschnitt" },
	]);

</script>
