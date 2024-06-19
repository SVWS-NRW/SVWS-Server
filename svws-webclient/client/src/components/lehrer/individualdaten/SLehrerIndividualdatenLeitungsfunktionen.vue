<template>
	<svws-ui-content-card title="Leitungsfunktionen">
		<svws-ui-table :columns="cols" :items="lehrerListeManager().daten().leitungsfunktionen" count>
			<template #cell(idLeitungsfunktion)="{ value }">
				{{ LehrerLeitungsfunktion.getByID(value)?.daten.bezeichnung }}
			</template>
			<template #cell(von)="{ value }">
				{{ value ? DateUtils.gibDatumGermanFormat(value) : '–' }}
			</template>
			<template #cell(bis)="{ value }">
				{{ value ? DateUtils.gibDatumGermanFormat(value) : '–' }}
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { DateUtils, LehrerLeitungsfunktion, type LehrerListeManager } from "@core";

	const props = defineProps<{
		lehrerListeManager: () => LehrerListeManager;
	}>();

	const cols = [
		{key: 'idLeitungsfunktion', label: 'Funktion', span: 2 },
		{key: 'von', label: 'Von', span: 1 },
		{key: 'bis', label: 'Bis', span: 1 }
	]

</script>
