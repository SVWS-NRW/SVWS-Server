<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Leitungsfunktionen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="selectedLeitungsfunktionen"
				v-model:clicked="clickedLeitungsfunktion"
				:items="filteredLeitungsfunktionen" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisible">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions>
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredItems"
							:disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Leitungsfunktion anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { LeitungsfunktionenAuswahlProps } from "./LeitungsfunktionenAuswahlProps";
	import { useKatalogAuswahl } from "~/composables/useKatalogAuswahl";

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
	];

	const props = defineProps<LeitungsfunktionenAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const {
		filteredItems: filteredLeitungsfunktionen,
		selectedItems: selectedLeitungsfunktionen,
		clickedItem: clickedLeitungsfunktion,
		readonly,
		isHinzufuegenView,
		searchTerm,
		showOnlyVisible,
		noFilteredItems,
	} = useKatalogAuswahl<Leitungsfunktion>(props);
</script>
