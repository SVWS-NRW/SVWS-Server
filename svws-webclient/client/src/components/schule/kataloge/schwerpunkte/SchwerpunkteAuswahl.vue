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
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisible">Nur Sichtbare</svws-ui-checkbox>
				</template>
				<template #actions v-if="!readonly">
					<svws-ui-tooltip v-if="serverState.hasDev" position="bottom">
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

	import type { SchuelerSchwerpunkt } from "@core/core/data/kataloge/SchuelerSchwerpunkt";
	import { useServerState } from "@ui/states/ServerState";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { SchwerpunkteAuswahlProps } from "./SchwerpunkteAuswahlProps";
	import { useKatalogAuswahl } from "~/composables/useKatalogAuswahl";

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" },
	];

	const props = defineProps<SchwerpunkteAuswahlProps>();
	const serverState = useServerState();

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
	} = useKatalogAuswahl<SchuelerSchwerpunkt>(props);
</script>
