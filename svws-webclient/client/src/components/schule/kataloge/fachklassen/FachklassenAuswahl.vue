<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Fachklassen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="selectedFachklassen"
				v-model:clicked="clickedFachklasse"
				:items="filteredFachklassen" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" removable />
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
							Neue Fachklasse anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { FachklasseEintrag } from "@core";
	import { type DataTableColumn, useRegionSwitch, useServerState } from "@ui";
	import { useKatalogAuswahl } from "~/composables/useKatalogAuswahl";
	import type { FachklassenAuswahlProps } from "~/components/schule/kataloge/fachklassen/FachklassenAuswahlProps";

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 },
	];

	const props = defineProps<FachklassenAuswahlProps>();
	const serverState = useServerState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const {
		filteredItems: filteredFachklassen,
		selectedItems: selectedFachklassen,
		clickedItem: clickedFachklasse,
		readonly,
		isHinzufuegenView,
		searchTerm,
		showOnlyVisible,
		noFilteredItems,
	} = useKatalogAuswahl<FachklasseEintrag>(props);

</script>
