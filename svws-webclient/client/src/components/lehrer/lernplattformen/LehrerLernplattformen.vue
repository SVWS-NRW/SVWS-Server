<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-lehrer-lernplattformen /> </svws-ui-modal-hilfe>
	</Teleport>
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
	import { useBenutzerState, type DataTableColumn, useModelProxyList } from "@ui";
	import type { LehrerLernplattformenProps } from "~/components/lehrer/lernplattformen/LehrerLernplattformenProps";
	import { computed } from "vue";
	import type { LehrerLernplattform } from "@core";
	import { BenutzerKompetenz } from "@core";
	import { LehrerLernplattformenModelProxy } from "./modelproxy/LehrerLernplattformenModelProxy";

	const props = defineProps<LehrerLernplattformenProps>();
	const benutzerState = useBenutzerState();
	const noEntries = computed<boolean>(() => props.lehrerLernplattformen().isEmpty());

	const hatKompetenzAendern = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_AENDERN));
	const readonly = computed(() => !hatKompetenzAendern.value);

	const lernplattformenProxies = useModelProxyList(
		() => props.lehrerLernplattformen(),
		(lernplattform) => lernplattform.idLernplattform,
		(lernplattform) => new LehrerLernplattformenModelProxy(() => lernplattform,
			(data: Partial<LehrerLernplattform>) => props.patch(data, lernplattform.idLernplattform))
	);

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
