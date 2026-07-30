<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Fachklassen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="selectedFachklassen"
				v-model:clicked="clickedFachklasse"
				:items="filteredFachklassenMitSchluesselSchulgliederung" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisible">Nur Sichtbare</svws-ui-checkbox>
					<ui-select-multi label="Schulgliederung"
						v-model="filterSchulgliederungen"
						:manager="schulgliederungManager" />
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

	import type { FachklasseEintrag, SchulgliederungKatalogEintrag } from "@core";
	import { Schulgliederung } from "@core";
	import { CoreTypeSelectManager, type DataTableColumn, useRegionSwitch, useSchuleState, useServerState } from "@ui";
	import { useKatalogAuswahl } from "~/composables/useKatalogAuswahl";
	import type { FachklassenAuswahlProps } from "~/components/schule/kataloge/fachklassen/FachklassenAuswahlProps";
	import { computed } from "vue";

	interface FachklasseEintragListe extends FachklasseEintrag {
		schluesselSchulgliederung: string;
	}

	const schuleState = useSchuleState();

	const columns: DataTableColumn[] = [
		{ key: "schluesselSchulgliederung", label: "SGL", sortable: true, defaultSort: 'asc' },
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 },
	];

	const props = defineProps<FachklassenAuswahlProps>();
	const serverState = useServerState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const manager = () => props.manager();
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

	// schluesselSchulgliederung wird zur Laufzeit aus idSchulgliederung gemappt,
	// damit die Tabellensortierung auf dem Schlüssel operieren kann.
	const filteredFachklassenMitSchluesselSchulgliederung = computed<FachklasseEintragListe[]>(() =>
		[...filteredFachklassen.value].map(eintrag => {
			const mapped = eintrag as FachklasseEintragListe;
			mapped.schluesselSchulgliederung =
				Schulgliederung.data().getEintragByID(eintrag.idSchulgliederung)?.schluessel ?? '';
			return mapped;
		})
	);

	const filterSchulgliederungen = computed<SchulgliederungKatalogEintrag[]>({
		get: () => manager().filterSchulgliederungen,
		set: (value: SchulgliederungKatalogEintrag[]) => {
			manager().filterSchulgliederungen = value;
			void props.setFilter();
		},
	});


	const schulgliederungManager = new CoreTypeSelectManager({
		clazz: Schulgliederung.class,
		schuljahr: schuleState.abschnitt.schuljahr,
		schulformen: schuleState.schulform,
		optionDisplayText: "kuerzel",
		selectionDisplayText: "kuerzel",
	});

</script>
