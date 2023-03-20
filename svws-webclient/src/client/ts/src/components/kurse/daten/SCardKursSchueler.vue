<template>
	<svws-ui-content-card title="Kursliste">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-data-table :columns="cols" :items="listSchueler" :footer="false" @update:clicked="gotoSchueler" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { List, Schueler } from "@svws-nrw/svws-core";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<{
		listSchueler: List<Schueler>;
		gotoSchueler: (eintrag: Schueler) => Promise<void>,
	}>();

	const cols: DataTableColumn[] = [
		{ key: "nachname", label: "Nachname", span: 1, sortable: false },
		{ key: "vorname", label: "Vorname", span: 1, sortable: false },
	];

</script>
