<template>
	<svws-ui-content-card title="Klassenliste">
		<svws-ui-table :columns="cols" :items="klassenListeManager().daten().schueler">
			<template #cell(status)="{ value } : { value: number}">
				<span :class="{'opacity-25': value === 2}">{{ SchuelerStatus.fromID(value)?.bezeichnung || "" }}</span>
			</template>
			<template #header(linkToSchueler)>
				<i-ri-group-line />
			</template>
			<template #cell(linkToSchueler)="{ rowData }">
				<button type="button" @click.stop="gotoSchueler(rowData)" class="button button--icon" title="Schüler ansehen">
					<i-ri-link />
				</button>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { KlassenListeManager, Schueler} from "@core";
	import { SchuelerStatus } from "@core";
	import type {DataTableColumn} from "@ui";

	const props = defineProps<{
		klassenListeManager: () => KlassenListeManager;
		gotoSchueler: (eintrag: Schueler) => Promise<void>,
	}>();

	const cols: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", span: 1, sortable: true },
		{ key: "vorname", label: "Vorname", span: 1, sortable: true },
		{ key: "status", label: "Status", sortable: true, span: 0.5 }
	];

</script>
