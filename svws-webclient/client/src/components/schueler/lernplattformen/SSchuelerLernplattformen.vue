<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card class="col-span-full">
			<svws-ui-table :items="lernplattformenProxies" :columns
				no-data-text="Aktuell gibt es keine Einträge im Katalog 'Lernplattformen'."
				:no-data="noEntries">
				<template #cell(idLernplattform)="{ rowData }">
					{{ getBezeichnungLernplattform(rowData.proxy.idLernplattform) }}
				</template>
				<template #cell(EinwilligungAbgefragt)="{ rowData }">
					<svws-ui-checkbox v-model="rowData.proxy.einwilligungAbgefragt" :readonly />
				</template>
				<template #cell(EinwilligungNutzung)="{ rowData }">
					<svws-ui-checkbox v-model="rowData.proxy.einwilligungNutzung" :readonly />
				</template>
				<template #cell(benutzername)="{ rowData }">
					{{ rowData.proxy.benutzername }}
				</template>
				<template #cell(initialKennwort)="{ rowData }">
					{{ rowData.proxy.initialKennwort }}
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import type { SchuelerLernplattformenProps } from "./SchuelerLernplattformenProps";
	import { SchuelerLernplattformenModelProxy } from "./modelProxy/SchuelerLernplattformenModelProxy";
	import type { SchuelerLernplattform } from "@core/core/data/schueler/SchuelerLernplattform";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import type { DataTableColumn } from "@ui/types";

	const props = defineProps<SchuelerLernplattformenProps>();
	const benutzerState = useBenutzerState();

	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed(() => !hatKompetenzAendern.value);
	const noEntries = computed<boolean>(() => props.schuelerLernplattformen().isEmpty());
	const lernplattformenProxies = computed(() => {
		const result = new ArrayList<SchuelerLernplattformenModelProxy>();
		for (const lernplattform of props.schuelerLernplattformen()) {
			const modelProxy = new SchuelerLernplattformenModelProxy(() => lernplattform, (data: Partial<SchuelerLernplattform>) => props.patch(data, lernplattform.idLernplattform));
			result.add(modelProxy);
		}
		return result;
	});

	const columns: DataTableColumn[] = [
		{ key: "idLernplattform", label: "Lernplattform", sortable: true },
		{ key: "EinwilligungAbgefragt", label: "Einwilligung Abgefragt", sortable: true },
		{ key: "EinwilligungNutzung", label: "Einwilligung Nutzung", sortable: true },
		{ key: "benutzername", label: "Benutzername", sortable: true },
		{ key: "initialKennwort", label: "InitialKennwort", sortable: true },
	];

	function getBezeichnungLernplattform(idLernplattform: number): string {
		return props.mapLernplattformen.get(idLernplattform)?.bezeichnung ?? "";
	}

</script>
