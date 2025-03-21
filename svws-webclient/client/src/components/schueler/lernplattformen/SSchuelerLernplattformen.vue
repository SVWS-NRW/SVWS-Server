<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card class="col-span-full">
			<svws-ui-table :items="props.schuelerLernplattformen()" :model-value="[...props.schuelerLernplattformen()]" :columns>
				<template #cell(bezeichnung)="{ rowData }">
					{{ getBezeichnungLernplattform(rowData.idLernplattform) }}
				</template>
				<template #cell(EinwilligungAbgefragt)="{ rowData }">
					<svws-ui-checkbox :model-value="rowData.einwilligungAbgefragt"
						@update:model-value="value => patch({ einwilligungAbgefragt: value }, rowData.idLernplattform)" />
				</template>
				<template #cell(EinwilligungNutzung)="{ rowData }">
					<svws-ui-checkbox :model-value="rowData.einwilligungNutzung"
						@update:model-value="value => patch({ einwilligungNutzung: value }, rowData.idLernplattform)" />
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerLernplattformenProps } from "~/components/schueler/lernplattformen/SchuelerLernplattformenProps";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<SchuelerLernplattformenProps>();

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Lernplattform", sortable: true },
		{ key: "EinwilligungAbgefragt", label: "Einwilligung Abgefragt", sortable: true },
		{ key: "EinwilligungNutzung", label: "Einwilligung Nutzung", sortable: true },
	];

	function getBezeichnungLernplattform(idLernplattform: number): string {
		return props.mapLernplattformen.get(idLernplattform)?.bezeichnung ?? "";
	}

</script>
