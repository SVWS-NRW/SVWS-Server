<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Ankreuzkompetenzen</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="selectedAnkreuzkompetenzen"
				v-model:clicked="clickedAnkreuzkompetenz"
				:items="filteredAnkreuzkompetenz" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input placeholder="Suchen" type="search" v-model="searchTerm" />
				</template>

				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisible">
						Nur Sichtbare
					</svws-ui-checkbox>
					<ui-select-multi label="Fach"
						v-model="filterFaecher"
						:manager="faecherManager" />
					<ui-select-multi label="Schulgliederung"
						v-model="filterSchulgliederungen"
						:manager="schulgliederungManager" />
					<ui-select-multi label="Jahrgang"
						v-model="filterJahrgaenge"
						:manager="jahrgangManager" />
				</template>

				<template #cell(fach)="{ rowData }">
					<span v-if="rowData.istASV">ASV</span>
					<span v-else>{{ manager().faecherById.get(rowData.idFach ?? -1)?.kuerzel || '—' }}</span>
				</template>
				<template #cell(floskelText)="{ value }">
					<span class="line-clamp-2 break-words" :title="value">{{ value }}</span>
				</template>

				<template #actions v-if="!readonly">
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredItems" :disabled="isHinzufuegenView">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Neue Ankreuzkompetenz anlegen
						</template>
					</svws-ui-tooltip>
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { useKatalogAuswahl } from "~/composables/useKatalogAuswahl";
	import type { AnkreuzkompetenzenAuswahlProps } from "~/components/schule/kataloge/ankreuzkompetenzen/AnkreuzkompetenzenAuswahlProps";
	import { computed } from "vue";
	import { Schulgliederung } from "@core/asd/types/schule/Schulgliederung";
	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { Ankreuzkompetenz } from "@core/core/data/schule/Ankreuzkompetenz";
	import { useSchuleState } from "@ui/states/SchuleState";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	const props = defineProps<AnkreuzkompetenzenAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const schuleState = useSchuleState();

	const manager = () => props.manager();
	const {
		filteredItems: filteredAnkreuzkompetenz,
		selectedItems: selectedAnkreuzkompetenzen,
		clickedItem: clickedAnkreuzkompetenz,
		readonly,
		isHinzufuegenView,
		searchTerm,
		showOnlyVisible,
		noFilteredItems,
	} = useKatalogAuswahl<Ankreuzkompetenz>(props);

	const columns: DataTableColumn[] = [
		{ key: "fach", label: "Fach", width: 8 },
		{ key: "schulgliederung", label: "SGL", width: 8 },
		{ key: "floskelText", label: "Text", span: 4, sortable: true, defaultSort: 'asc' },
	];

	const ASV_FACH = { id: -1, kuerzel: 'ASV', bezeichnung: 'ASV' } as FachDaten;
	const faecher = computed<Iterable<FachDaten>>(() => [ASV_FACH, ...manager().faecherById.values()]);
	const schulgliederungen = computed<Iterable<Schulgliederung>>(() => Schulgliederung.data().getListBySchuljahrAndSchulform(schuleState.abschnitt.schuljahr, schuleState.schulform));
	const jahrgaenge = computed<Iterable<JahrgangsDaten>>(() => manager().jahrgaengeById.values());

	const filterFaecher = computed<FachDaten[]>({
		get: () => manager().filterFaecher,
		set: (value: FachDaten[]) => {
			manager().filterFaecher = value;
			void props.setFilter();
		},
	});

	const filterSchulgliederungen = computed<Schulgliederung[]>({
		get: () => manager().filterSchulgliederungen,
		set: (value: Schulgliederung[]) => {
			manager().filterSchulgliederungen = value;
			void props.setFilter();
		},
	});

	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => manager().filterJahrgaenge,
		set: (value: JahrgangsDaten[]) => {
			manager().filterJahrgaenge = value;
			void props.setFilter();
		},
	});

	const faecherManager = new SelectManager<FachDaten>({
		options: faecher,
		optionDisplayText: f => f.bezeichnung,
		selectionDisplayText: f => f.bezeichnung,
	});

	const schulgliederungManager = new SelectManager<Schulgliederung>({
		options: schulgliederungen,
		optionDisplayText: s => s.name(),
		selectionDisplayText: s => s.name(),
	});

	const jahrgangManager = new SelectManager<JahrgangsDaten>({
		options: jahrgaenge,
		optionDisplayText: j => j.kuerzel ?? '',
		selectionDisplayText: j => j.kuerzel ?? '',
	});

</script>
