<template>
	<div class="page--content">
		<svws-ui-content-card title="Vermerkart" class="w-full">
			<svws-ui-text-input id="contentFocusField" class="w-5/5" placeholder="Bezeichnung" :model-value="vermerkartenManager().auswahl().bezeichnung" @change="bezeichnung => patch({ bezeichnung: bezeichnung ?? undefined })" type="text" />
		</svws-ui-content-card>

		<svws-ui-content-card :title="'Alle Schüler mit der Vermerkart `' + vermerkartenManager().auswahl().bezeichnung + '`'" class="w-full" v-if="vermerkartenManager().getListSchuelerVermerkartZusammenfassung().list().size() > 0">
			<svws-ui-table :columns :items="vermerkartenManager().getListSchuelerVermerkartZusammenfassung().list()" class="w-full">
				<template #cell(status)="{ value }: { value: number}">
					<span :class="{'opacity-25': value === 2}">{{ value }}</span>
				</template>
				<template #header(linkToSchueler)>
					<span class="icon i-ri-group-line" />
				</template>
				<template #cell(linkToSchueler)="{ rowData }">
					<button type="button" @click.stop="gotoSchueler(rowData)" class="button button--icon" title="Schüler ansehen">
						<span class="icon i-ri-link" />
					</button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>

		<div v-else>
			<svws-ui-content-card :title="'Die Vermerkart `' + vermerkartenManager().auswahl().bezeichnung + '` wurde nie verwendet'" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { VermerkartDatenProps } from "./SVermerkartDatenProps";

	defineProps<VermerkartDatenProps>();

	const columns: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
		{ key: "anzahlVermerke", label: "Anzahl", fixedWidth: 8.75, sortable: true, span: 0.5 }
	];

</script>
