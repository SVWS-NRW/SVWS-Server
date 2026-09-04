<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1>Floskel</h1>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table v-model="floskeln"
				v-model:clicked="selectedFloskeln"
				:items="props.manager().filtered()" :columns
				clickable :selectable="!readonly" count :focus-switching-enabled :focus-help-visible scroll scroll-into-view filter-open>
				<template #search>
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" />
				</template>
				<template #filterAdvanced>
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
					<svws-ui-tooltip position="bottom">
						<svws-ui-button type="icon"
							@click="gotoHinzufuegenView(true)"
							:has-focus="noFilteredEntries" :disabled="isHinzufuegenView">
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
	import type { FloskelnAuswahlProps } from "./FloskelnAuswahlProps";
	import { Floskelgruppenart } from "@core/asd/types/schule/Floskelgruppenart";
	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { Floskel } from "@core/core/data/schule/Floskel";
	import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { ViewType } from "@ui/ui/nav/ViewType";

	const props = defineProps<FloskelnAuswahlProps>();
	const benutzerState = useBenutzerState();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const manager = () => props.manager();
	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => manager().filtered().size() === 0);

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "text", label: "Text", span: 4, sortable: true },
	];

	const floskeln = computed<Floskel[]>({
		get: () => [...manager().liste.auswahl()],
		set: (v: Floskel[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const selectedFloskeln = computed<Floskel | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && manager().hasDaten()) ? manager().auswahl() : null,
		set: (v: Floskel | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	function setAuswahl(floskeln: Floskel[]) {
		manager().liste.auswahlClear();
		for (const floskel of floskeln) {
			if (manager().liste.hasValue(floskel)) {
				manager().liste.auswahlAdd(floskel);
			}
		}
	}

	async function navigateToView(): Promise<void> {
		if (manager().liste.auswahlExists()) {
			await props.gotoGruppenprozessView(true);
		} else {
			await props.gotoDefaultView(manager().getVorherigeAuswahl()?.id);
		}
	}

	const searchTerm = computed<string>({
		get: () => manager().searchTerm,
		set: (v: string) => {
			manager().searchTerm = v;
			void props.setFilter();
		},
	});

	const jahrgaenge = computed<JahrgangsDaten[]>(() => [...manager().jahrgaengeById.values()]);
	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => manager().filterJahrgaenge,
		set: (value: JahrgangsDaten[]) => {
			manager().filterJahrgaenge = value;
			void props.setFilter();
		},
	});

	const floskelgruppen = computed<Floskelgruppe[]>(() => [...manager().floskelgruppenById.values()]);
	const filterFloskelgruppe = computed<Floskelgruppe[]>({
		get: () => manager().filterFloskelgruppen,
		set: (values: Floskelgruppe[]) => {
			manager().filterFloskelgruppen = values;
			const istNichtFachbezogen = !(values.some(fg => istFachbezogeneFloskelgruppe(fg)));
			if (istNichtFachbezogen) {
				manager().filterFaecher = [];
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

	const faecher = computed<FachDaten[]>(() => [...manager().faecherById.values()]);
	const filterFaecher = computed<FachDaten[]>({
		get: () => manager().filterFaecher,
		set: (value: FachDaten[]) => {
			manager().filterFaecher = value;
			void props.setFilter();
		},
	});

	const filterNiveau = computed<number[]>({
		get: () => manager().filterNiveaus,
		set: (value: number[]) => {
			manager().filterNiveaus = value;
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
		options: manager().niveaus,
		optionDisplayText: String,
		selectionDisplayText: String,
	});

</script>
