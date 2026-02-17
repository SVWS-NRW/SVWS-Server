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
					<svws-ui-text-input v-model="searchTerm" type="search" placeholder="Suchen" removable />
				</template>
				<template #filterAdvanced>
					<svws-ui-checkbox type="toggle" v-model="showOnlyVisibleFloskeln">Nur Sichtbare</svws-ui-checkbox>
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

	import type { DataTableColumn } from "@ui";
	import { SelectManager, useRegionSwitch, ViewType } from "@ui";
	import type { FachDaten, Floskel, Floskelgruppe, JahrgangsDaten } from "@core";
	import { BenutzerKompetenz, Floskelgruppenart, ServerMode } from "@core";
	import { computed } from "vue";
	import type { FloskelnAuswahlProps } from "./FloskelnAuswahlProps";

	// --- Props & Composables ---

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const props = defineProps<FloskelnAuswahlProps>();

	// --- View-State ---

	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN));
	const isHinzufuegenView = computed<boolean>(() => props.activeViewType === ViewType.HINZUFUEGEN);
	const isGruppenprozesseOrHinzufuegenView = computed<boolean>(() => (props.activeViewType === ViewType.GRUPPENPROZESSE) || isHinzufuegenView.value);
	const noFilteredEntries = computed<boolean>(() => props.manager().filtered().size() === 0);

	// --- Tabelle ---

	const columns: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "text", label: "Text", span: 4, sortable: true },
	];

	// --- Auswahl & Navigation ---

	const floskeln = computed<Floskel[]>({
		get: () => [...props.manager().liste.auswahl()],
		set: (v: Floskel[]) => {
			setAuswahl(v);
			void navigateToView();
		},
	});

	const selectedFloskeln = computed<Floskel | null>({
		get: () => (!isGruppenprozesseOrHinzufuegenView.value && props.manager().hasDaten()) ? props.manager().auswahl() : null,
		set: (v: Floskel | null) => void props.gotoDefaultView(v?.id ?? null),
	});

	function setAuswahl(floskeln: Floskel[]) {
		props.manager().liste.auswahlClear();
		for (const floskel of floskeln) {
			if (props.manager().liste.hasValue(floskel)) {
				props.manager().liste.auswahlAdd(floskel);
			}
		}
	}

	async function navigateToView(): Promise<void> {
		if (props.manager().liste.auswahlExists()) {
			await props.gotoGruppenprozessView(true);
		} else {
			await props.gotoDefaultView(props.manager().getVorherigeAuswahl()?.id);
		}
	}

	// --- Suche ---

	const searchTerm = computed<string>({
		get: () => props.manager().searchTerm,
		set: (v: string) => {
			props.manager().searchTerm = v;
			void props.setFilter();
		},
	});

	// --- Filter: Sichtbarkeit ---

	const showOnlyVisibleFloskeln = computed<boolean>({
		get: () => props.manager().filterNurSichtbar(),
		set: (value: boolean) => {
			props.manager().setFilterNurSichtbar(value);
			void props.setFilter();
		},
	});

	// --- Filter: Jahrgänge ---

	const jahrgaenge = computed<JahrgangsDaten[]>(() => [...props.manager().getJahrgaenge().values()]);
	const jahrgaengeManager = new SelectManager<JahrgangsDaten>({
		options: jahrgaenge,
		optionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
		selectionDisplayText: (jg: JahrgangsDaten) => jg.kuerzel ?? '',
	});
	const filterJahrgaenge = computed<JahrgangsDaten[]>({
		get: () => props.manager().filterJahrgaenge(),
		set: (value: JahrgangsDaten[]) => {
			props.manager().setFilterJahrgang(value);
			void props.setFilter();
		},
	});

	// --- Filter: Floskelgruppen ---

	const floskelgruppen = computed<Floskelgruppe[]>(() => [...props.manager().getFloskelgruppen()]);
	const floskelgruppeManager = new SelectManager<Floskelgruppe>({
		options: floskelgruppen,
		optionDisplayText: (fg: Floskelgruppe) => fg.kuerzel,
		selectionDisplayText: (fg: Floskelgruppe) => fg.kuerzel,
	});
	const filterFloskelgruppe = computed<Floskelgruppe[]>({
		get: () => props.manager().filterFloskelgruppe(),
		set: (values: Floskelgruppe[]) => {
			props.manager().setFilterFloskelgruppe(values);
			const istNichtFachbezogen = !(values.some(fg => istFachbezogeneFloskelgruppe(fg)));
			if (istNichtFachbezogen) {
				props.manager().setFilterFaecher([]);
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


	// --- Filter: Fächer ---

	const faecher = computed<FachDaten[]>(() => [...props.manager().getFaecher().values()]);
	const faecherManager = new SelectManager<FachDaten>({
		options: faecher,
		optionDisplayText: (f: FachDaten) => f.bezeichnung,
		selectionDisplayText: (f: FachDaten) => f.bezeichnung,
	});
	const filterFaecher = computed<FachDaten[]>({
		get: () => props.manager().filterFaecher(),
		set: (value: FachDaten[]) => {
			props.manager().setFilterFaecher(value);
			void props.setFilter();
		},
	});

	// --- Filter: Niveaus ---

	const niveaus = computed<number[]>(() => props.manager().niveaus);
	const niveausManager = new SelectManager<number>({
		options: niveaus,
		optionDisplayText: (n: number) => String(n),
		selectionDisplayText: (n: number) => String(n),
	});
	const filterNiveau = computed<number[]>({
		get: () => props.manager().filterNiveaus(),
		set: (value: number[]) => {
			props.manager().setFilterNiveau(value);
			void props.setFilter();
		},
	});

</script>
