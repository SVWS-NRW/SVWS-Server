<template>
	<svws-ui-content-card title="Klassenliste">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-data-table :columns="cols" :items="listSchueler" :footer="false" @update:clicked="gotoSchueler">
					<template #cell(status)="{ value }">{{ SchuelerStatus.fromID(value) }}</template>
				</svws-ui-data-table>
			</div>
		</div>
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
		{ key: "nachname", label: "Nachname", span: 1, sortable: false },
		{ key: "vorname", label: "Vorname", span: 1, sortable: false },
		{ key: "status", label: "Status" }
	];

</script>
