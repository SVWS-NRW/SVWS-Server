<template>
	<svws-ui-content-card title="Klassenliste">
		<svws-ui-data-table :columns="cols" :items="listSchueler">
			<template #cell(nachname)="{ rowData }">
				<svws-ui-icon @click.stop="gotoSchueler(rowData as Schueler)" class="mr-2 bg-green-light"> <i-ri-link /> </svws-ui-icon> {{ rowData.nachname }}
			</template>
			<template #cell(status)="{ value }">
				<svws-ui-badge type="light" size="big" class="mr-1"> {{ SchuelerStatus.fromID(value) }} </svws-ui-badge>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { List, Schueler, SchuelerStatus } from "@svws-nrw/svws-core";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<{
		listSchueler: List<Schueler>;
		gotoSchueler: (eintrag: Schueler) => Promise<void>,
	}>();

	const cols: DataTableColumn[] = [
		{ key: "nachname", label: "Nachname", span: 1, sortable: true },
		{ key: "vorname", label: "Vorname", span: 1, sortable: true },
		{ key: "status", label: "Status", sortable: true }
	];

</script>
