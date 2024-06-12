<template>
	<svws-ui-content-card title="Leitungsfunktionen">
		<svws-ui-table :columns="cols" :items="lehrerListeManager().daten().leitungsfunktionen" count>
			<template #cell(funktion)="{ rowData }">
				{{ LehrerLeitungsfunktion.getByID(rowData.idLeitungsfunktion)?.daten.bezeichnung }}
			</template>
			<template #cell(von)="{ rowData }">
				<svws-ui-text-input placeholder="Geburtsdatum" :model-value="rowData.beginn" @change="datum => datum && patch({ beginn: datum })" type="date" headless />
			</template>
			<template #cell(bis)="{ rowData }">
				<svws-ui-text-input placeholder="Geburtsdatum" :model-value="rowData.ende" @change="datum => datum && patch({ ende: datum })" type="date" headless />
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { LehrerLeitungsfunktion, type LehrerListeManager } from "@core";

	const props = defineProps<{
		lehrerListeManager: () => LehrerListeManager;
	}>();

	const cols = [
		{key: 'funktion', label: 'Funktion', span: 2 },
		{key: 'von', label: 'Von', span: 1 },
		{key: 'bis', label: 'Bis', span: 1 }
	]

</script>
