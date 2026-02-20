<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Schwerpunkte</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="selectedSchwerpunkte"
				v-model:clicked="clickedSchwerpunkt"
				:items="filteredSchwerpunkte" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisible">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions>
					<svws-ui-tooltip position="bottom" v-if="ServerMode.DEV.checkServerMode(serverMode)">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredItems"
							:disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neuen Schwerpunkt anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { type SchuelerSchwerpunkt as Schwerpunkt, ServerMode } from "@core";
	import { type DataTableColumn, useRegionSwitch } from "@ui";

	import type { SchwerpunkteAuswahlProps } from "./SchwerpunkteAuswahlProps";
	import { useKatalogAuswahl } from "~/composables/useKatalogAuswahl";

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" },
	];

	const props = defineProps<SchwerpunkteAuswahlProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const {
		filteredItems: filteredSchwerpunkte,
		selectedItems: selectedSchwerpunkte,
		clickedItem: clickedSchwerpunkt,
		readonly,
		isHinzufuegenView,
		searchTerm,
		showOnlyVisible,
		noFilteredItems,
	} = useKatalogAuswahl<Schwerpunkt>(props);
</script>
