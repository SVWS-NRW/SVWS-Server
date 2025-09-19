<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper>
				<svws-ui-text-input class="contentFocusField" placeholder="Bezeichnung" :model-value="vermerkartenManager().auswahl().bezeichnung" :readonly
					@change="v => patch({ bezeichnung: v?.trim() ?? undefined })" />
				<svws-ui-spacing />
				<svws-ui-checkbox :model-value="vermerkartenManager().daten().istSichtbar" @update:model-value="istSichtbar => patch({ istSichtbar })" :readonly>
					Sichtbar
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card class="w-full" :title="'Alle Schüler mit der Vermerkart `' + vermerkartenManager().auswahl().bezeichnung + '`'"
			v-if="vermerkartenManager().getListSchuelerVermerkartZusammenfassung().list().size() > 0">
			<svws-ui-table class="w-full" :columns :items="vermerkartenManager().getListSchuelerVermerkartZusammenfassung().list()">
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
	</div>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { VermerkartenDatenProps } from "./SVermerkartenDatenProps";
	import { computed } from "vue";
	import { BenutzerKompetenz } from "@core";

	const props = defineProps<VermerkartenDatenProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const columns: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
		{ key: "anzahlVermerke", label: "Anzahl", fixedWidth: 8.75, sortable: true, span: 0.5 },
	];

</script>
