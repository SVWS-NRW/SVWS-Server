<template>
	<svws-ui-content-card title="Kursliste">
		<svws-ui-data-table :columns="cols" :items="listSchueler" :footer="false">
			<template #cell(nachname)="{ rowData }">
				<span class="inline-flex items-center gap-1">
					<svws-ui-button type="icon" size="small" @click.stop="gotoSchueler(rowData as Schueler)"><i-ri-link /></svws-ui-button>
					{{ rowData.nachname }}
				</span>
			</template>
			<template #cell(status)="{ value }: { value: number}">
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
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
		{ key: "status", label: "Status", sortable: true, span: 0.5 }
	];

</script>
