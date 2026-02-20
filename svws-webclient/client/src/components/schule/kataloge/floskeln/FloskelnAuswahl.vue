<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Floskel</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="selectedItems"
				v-model:clicked="focusedItem"
				:items="filteredItems" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisibleItems">Nur Sichtbare</svws-ui-checkbox>
					<ui-select-multi label="Jahrgang"
						v-model="filterJahrgaenge"
						:manager="jahrgaengeManager"
						searchable removable />
					<ui-select-multi label="Floskelgruppe"
						v-model="filterFloskelgruppe"
						:manager="floskelgruppeManager"
						searchable removable />
					<ui-select-multi v-if="istFilterFachbezogeneFloskelgruppeAktiv" label="Fach"
						v-model="filterFaecher"
						:manager="faecherManager"
						searchable removable />
					<ui-select-multi label="Niveau"
						v-model="filterNiveau"
						:manager="niveausManager"
						searchable removable />
				</template>
				<template #cell(text)="{ value }">
					<span class="line-clamp-2">{{ value }}</span>
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip position="bottom" v-if="ServerMode.DEV.checkServerMode(serverMode)">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredItems" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Floskel anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import type { DataTableColumn } from "@ui";
	import { FloskelnListeManager } from "@ui";
	import { SelectManager, useRegionSwitch } from "@ui";
	import type { FachDaten, Floskel, Floskelgruppe, JahrgangsDaten } from "@core";
	import { Floskelgruppenart, ServerMode } from "@core";
	import type { FloskelnAuswahlProps } from "./FloskelnAuswahlProps";
	import { useKatalogAuswahl } from "~/composables/useKatalogAuswahl";

	const props = defineProps<FloskelnAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const {
		filteredItems,
		selectedItems,
		focusedItem,
		readonly,
		isHinzufuegenView,
		searchTerm,
		showOnlyVisibleItems,
		noFilteredItems,
	} = useKatalogAuswahl<Floskel>(props);

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "text", label: "Text", span: 4, sortable: true },
	];

	const jahrgaenge = computed<JahrgangsDaten[]>(() => [...props.manager().jahrgaengeById.values()]);
	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => props.manager().filterJahrgaenge,
		set: (value: JahrgangsDaten[]) => {
			props.manager().filterJahrgaenge = value;
			void props.setFilter();
		},
	});

	const floskelgruppen = computed<Floskelgruppe[]>(() => [...props.manager().floskelgruppenById.values()]);
	const filterFloskelgruppe = computed<Floskelgruppe[]>({
		get: () => props.manager().filterFloskelgruppen,
		set: (values: Floskelgruppe[]) => {
			props.manager().filterFloskelgruppen = values;
			const istNichtFachbezogen = !(values.some(fg => istFachbezogeneFloskelgruppe(fg)));
			if (istNichtFachbezogen) {
				props.manager().filterFloskelgruppen = [];
			}
			void props.setFilter();
		},
	});

	const istFilterFachbezogeneFloskelgruppeAktiv = computed<boolean>(() =>
		filterFloskelgruppe.value.some(fg => istFachbezogeneFloskelgruppe(fg))
	);

	function istFachbezogeneFloskelgruppe(floskelgruppe: Floskelgruppe) {
		return Floskelgruppenart.data().getWertByIDOrNull(floskelgruppe.idFloskelgruppenart ?? -1)?.name() === 'FACH';
	}

	const faecher = computed<FachDaten[]>(() => [...props.manager().faecherById.values()]);
	const filterFaecher = computed<FachDaten[]>({
		get: () => props.manager().filterFaecher,
		set: (value: FachDaten[]) => {
			props.manager().filterFaecher = value;
			void props.setFilter();
		},
	});

	const filterNiveau = computed<number[]>({
		get: () => props.manager().filterNiveaus,
		set: (value: number[]) => {
			props.manager().filterNiveaus = value;
			void props.setFilter();
		},
	});

	// --- manager ---

	const jahrgaengeManager = new SelectManager<JahrgangsDaten>({
		options: jahrgaenge,
		optionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
		selectionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
	});

	const floskelgruppeManager = new SelectManager<Floskelgruppe>({
		options: floskelgruppen,
		optionDisplayText: (fg: Floskelgruppe) => fg.kuerzel,
		selectionDisplayText: (fg: Floskelgruppe) => fg.kuerzel,
	});

	const faecherManager = new SelectManager<FachDaten>({
		options: faecher,
		optionDisplayText: (f: FachDaten) => f.bezeichnung,
		selectionDisplayText: (f: FachDaten) => f.bezeichnung,
	});

	const niveausManager = new SelectManager<number>({
		options: FloskelnListeManager.NIVEAUS,
		optionDisplayText: (n: number) => String(n),
		selectionDisplayText: (n: number) => String(n),
	});

</script>
