<template>
	<svws-ui-content-card title="Kursliste">
		<svws-ui-data-table :columns="cols" :items="listSchueler" :footer="false">
			<template #cell(nachname)="{ rowData }">
				<svws-ui-icon @click.stop="gotoSchueler(rowData as Schueler)" class="mr-2 text-primary hover:opacity-50 cursor-pointer"> <i-ri-link /> </svws-ui-icon> {{ rowData.nachname }}
			</template>
			<template #cell(status)="{ value }">
				<svws-ui-badge type="light" size="big"> {{ SchuelerStatus.fromID(value) }} </svws-ui-badge>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { List, Schueler} from "@svws-nrw/svws-core";
	import { SchuelerStatus } from "@svws-nrw/svws-core";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<{
		listSchueler: List<Schueler>;
		gotoSchueler: (eintrag: Schueler) => Promise<void>,
	}>();

	const cols: DataTableColumn[] = [
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
		{ key: "status", label: "Status", sortable: true }
	];

</script>
