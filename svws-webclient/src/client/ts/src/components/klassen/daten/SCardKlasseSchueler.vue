<template>
	<svws-ui-content-card title="Klassenliste">
		<svws-ui-data-table :columns="cols" :items="listSchueler">
			<template #cell(nachname)="{ rowData }">
				<span @click.stop="gotoSchueler(rowData as Schueler)" class="icon mr-2 text-primary hover:opacity-50 cursor-pointer" title="Schüler ansehen"> <i-ri-link /> </span> {{ rowData.nachname }}
			</template>
			<template #cell(status)="{ value } : { value: number}">
				<span>{{ SchuelerStatus.fromID(value)?.bezeichnung || "" }}</span>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, Schueler} from "@core";
	import { SchuelerStatus } from "@core";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<{
		listSchueler: List<Schueler>;
		gotoSchueler: (eintrag: Schueler) => Promise<void>,
	}>();

	const cols: DataTableColumn[] = [
		{ key: "nachname", label: "Nachname", span: 1, sortable: true },
		{ key: "vorname", label: "Vorname", span: 1, sortable: true },
		{ key: "status", label: "Status", sortable: true, span: 0.5 }
	];

</script>
