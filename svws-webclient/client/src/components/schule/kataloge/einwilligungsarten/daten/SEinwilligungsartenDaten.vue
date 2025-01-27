<template>
	<div class="page--content">
		<svws-ui-content-card title="Einwilligungsart" class="w-full">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField w-5/5" placeholder="Bezeichnung" :model-value="einwilligungsartenListeManager().auswahl().bezeichnung" @change="bezeichnung => patch({ bezeichnung: bezeichnung ?? undefined })" type="text" />
				<svws-ui-checkbox :model-value="einwilligungsartenListeManager().daten().istSichtbar" @update:model-value="value => patch({ istSichtbar: value === true })">
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card :title="'Alle Schüler mit der Einwilligungsart `' + einwilligungsartenListeManager().auswahl().bezeichnung + '`'" class="w-full" v-if="einwilligungsartenListeManager().getListSchuelerEinwilligungsartenZusammenfassung().list().size() > 0">
			<svws-ui-table :columns :items="einwilligungsartenListeManager().getListSchuelerEinwilligungsartenZusammenfassung().list()" class="w-full">
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
			<svws-ui-content-card :title="'Die Einwilligungsart `' + einwilligungsartenListeManager().auswahl().bezeichnung + '` wurde nie verwendet'" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { EinwilligungsartenDatenProps } from "./SEinwilligungsartenDatenProps";
	import type {DataTableColumn} from "@ui";

	defineProps<EinwilligungsartenDatenProps>();

	const columns: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
		{ key: "anzahlEinwilligungen", label: "Anzahl", fixedWidth: 8.75, sortable: true, span: 0.5 },
	];

</script>
