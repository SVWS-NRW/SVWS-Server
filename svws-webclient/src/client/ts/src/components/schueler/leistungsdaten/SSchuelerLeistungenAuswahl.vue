<template>
	<svws-ui-data-table :clicked="lernabschnitt" @update:clicked="gotoLernabschnitt" :columns="cols"
		:items="lernabschnitte" :footer="false" clickable>
		<template #cell="{rowData: row}">
			<span>
				{{ row.schuljahr + "." + row.abschnitt }}
			</span>
			<svws-ui-tooltip v-if="row.wechselNr !== null">
				<span class="opacity-50 ml-1 inline-block cursor-pointer">
					(alt)
					<svws-ui-icon name="info" />
				</span>
				<template #content>
					Daten vor dem {{ row.wechselNr }}. Wechsel
				</template>
			</svws-ui-tooltip>
		</template>
	</svws-ui-data-table>
</template>

<script setup lang="ts">

	import {ref} from "vue";
	import type {SchuelerLeistungenAuswahlProps} from "./SSchuelerLeistungenAuswahlProps";

	const props = defineProps<SchuelerLeistungenAuswahlProps>();

	const cols = ref([
		{key: "schuljahresabschnitt", label: "Abschnitt"},
	]);

</script>
