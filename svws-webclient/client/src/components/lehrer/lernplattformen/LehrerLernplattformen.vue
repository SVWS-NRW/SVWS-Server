<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-lehrer-lernplattformen /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<svws-ui-content-card class="col-span-full">
			<svws-ui-table :items="props.lehrerLernplattformen()" :columns>
				<template #cell(idLernplattform)="{ value }">
					{{ getBezeichnungLernplattform(value) }}
				</template>
				<template #cell(EinwilligungAbgefragt)="{ rowData }">
					<svws-ui-checkbox :model-value="rowData.einwilligungAbgefragt"
						@update:model-value="value => patch({ einwilligungAbgefragt: value }, rowData.idLernplattform)" :readonly />
				</template>
				<template #cell(EinwilligungNutzung)="{ rowData }">
					<svws-ui-checkbox :model-value="rowData.einwilligungNutzung"
						@update:model-value="value => patch({ einwilligungNutzung: value }, rowData.idLernplattform)" :readonly />
				</template>
				<template #cell(benutzername)="{ value }">
					{{ value }}
				</template>
				<template #cell(initialKennwort)="{ value }">
					{{ value }}
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { DataTableColumn } from "@ui";
	import type { LehrerLernplattformenProps } from "~/components/lehrer/lernplattformen/LehrerLernplattformenProps";
	import { computed } from "vue";
	import { BenutzerKompetenz } from "@core";

	const props = defineProps<LehrerLernplattformenProps>();

	const hatKompetenzAendern = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.LEHRERDATEN_AENDERN));
	const readonly = computed(() => !hatKompetenzAendern.value);

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
