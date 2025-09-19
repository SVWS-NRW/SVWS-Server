<template>
	<Story title="Select New" id="ui-select" icon="ri:expand-up-down-line" auto-props-disabled :layout="{ type: 'grid', width: '45%'}" :source="getSourceString()">
		<template #docs><Docs /></template>
		<Variant title="SelectManager" id="selectManager">
			<svws-ui-input-wrapper>
				<ui-select label="SelectManager mit String" :manager="stringSelectManager" :searchable="state.searchable" :removable="state.removable"
					:disabled="state.disabled" :statistics="state.statistics" :headless="state.headless" :readonly="state.readonly"
					:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
				<ui-select label="SelectManager mit Custom-Objekten" :manager="objectSelectManager" :searchable="state.searchable" :removable="state.removable"
					:disabled="state.disabled" :statistics="state.statistics" :headless="state.headless" :readonly="state.readonly"
					:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
				<ui-select label="CoreTypeSelectManager mit LehrerRechtsverhaeltnis" :manager="coreTypeSelectManager" :searchable="state.searchable" :disabled="state.disabled"
					:statistics="state.statistics" :headless="state.headless" :removable="state.removable" :readonly="state.readonly"
					:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
			</svws-ui-input-wrapper>
			<template #controls>
				<HstCheckbox v-model="state.searchable" title="Searchable" />
				<HstCheckbox v-model="state.required" title="Required" />
				<HstCheckbox v-model="state.disabled" title="Disabled" />
				<HstCheckbox v-model="state.statistics" title="Statistik" />
				<HstCheckbox v-model="state.removable" title="Removable" />
				<HstCheckbox v-model="state.readonly" title="Readonly" />
				<HstCheckbox v-model="state.headless" title="Headless" />
				<span class="text-headline-md">Farben</span>
				<HstRadio v-model="state.bgColor" title="Hintergrund" :options="[
					{ label: 'keine', value: '' },
					{ label: 'bg-ui-brand', value: 'bg-ui-brand' },
					{ label: 'bg-ui-success', value: 'bg-ui-success' },
					{ label: 'bg-ui-danger', value: 'bg-ui-danger' },
				]" />
				<HstRadio v-model="state.textColor" title="Text" :options="[
					{ label: 'keine', value: '' },
					{ label: 'text-ui-onbrand', value: 'text-ui-onbrand' },
					{ label: 'text-ui-onsuccess', value: 'text-ui-onsuccess' },
					{ label: 'text-ui-ondanger', value: 'text-ui-ondanger' },
				]" />
				<HstRadio v-model="state.iconColor" title="Icon" :options="[
					{ label: 'keine', value: '' },
					{ label: 'icon-ui-onbrand', value: 'icon-ui-onbrand' },
					{ label: 'icon-ui-onsuccess', value: 'icon-ui-onsuccess' },
					{ label: 'icon-ui-ondanger', value: 'icon-ui-ondanger' },
				]" />
				<HstRadio v-model="state.borderColor" title="Border" :options="[
					{ label: 'keine', value: '' },
					{ label: 'border-ui-brand', value: 'border-ui-brand' },
					{ label: 'border-ui-success', value: 'border-ui-success' },
					{ label: 'border-ui-danger', value: 'border-ui-danger' },
				]" />
			</template>
		</Variant>
		<Variant title="Filter" id="filter">
			<svws-ui-input-wrapper>
				In diesem Beispiel werden zwei Filter an das Select übergeben. Jeder Filter hat 2 Fachgruppen, die ausgewählt werden können. Solange kein
				Filter gesetzt ist, werden alle Optionen angezeigt. Sobald ein Filter gesetzt wird, werden nur noch dazu passende Optionen zur Verfügung
				gestellt. Gesetzte Fachgruppen in einem Filter ergänzen sich dabei. Wird jedoch in beiden Filtern eine Fachruppe gesetzt, dann werden nur
				Optionen angezeigt, die zu beiden Fachgruppen passen.
				<strong>Filter 1</strong>
				<svws-ui-checkbox v-model="filterState1.fremdsprache">
					Fremdsprachen
				</svws-ui-checkbox>
				<svws-ui-checkbox v-model="filterState1.musikUndKunst1">
					Musik und Kunst
				</svws-ui-checkbox>
				<strong>Filter 2</strong>
				<svws-ui-checkbox v-model="filterState2.deutsch">
					Deustch
				</svws-ui-checkbox>
				<svws-ui-checkbox v-model="filterState2.musikUndKunst2">
					Musik und Kunst
				</svws-ui-checkbox>
				<ui-select label="CoreTypeSelectManager Fach abhängig von Fachgruppe" :manager="fachSelectManager" :searchable="state.searchable"
					:disabled="state.disabled" :statistics="state.statistics" :headless="state.headless" :removable="state.removable" :readonly="state.readonly"
					:min-options="state.minOptions" :max-options="state.maxOptions" :required="state.required"
					:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
			</svws-ui-input-wrapper>
		</Variant>
		<Variant title="Deep Search" id="search">
			<svws-ui-input-wrapper>
				Das folgende Select verwendet Deep Search und lässt auch die Suche nach Attributen zu, die nicht im Optiontext dargestellt werden. So ergibt
				die Suche nach "2006" oder "2008" ebenfalls jeweils ein Auto, da die Optionen folgende sind:
				<pre class="bg-ui-neutral border border-ui-neutral rounded w-fit whitespace-normal p-2">
					<code>
						[<br>
							{ marke: "BMW", color: "blue", baujahr: 2006 },<br>
							{ marke: "Audi", color: "red", baujahr: 2008 }<br>
							{ marke: "Opel", color: "schwarz", baujahr: 2006 }<br>
						]
					</code>
				</pre>
				<ui-select label="Deep Search SelectManager" :manager="deepSearchSelectManager" :searchable="true" :disabled="state.disabled"
					:statistics="state.statistics" :headless="state.headless" :min-options="state.minOptions"
					:deep-search-attributes="['marke', 'color', 'baujahr']" :removable="state.removable" :readonly="state.readonly"
					:max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
			</svws-ui-input-wrapper>
		</Variant>
		<Variant title="Sortierung" id="sortierung">
			<svws-ui-input-wrapper>
				<ui-select label="Sortiertes Select" :manager="sortableCoreTypeSelectManager" :searchable="true" :disabled="state.disabled"
					:statistics="state.statistics" :headless="state.headless" :min-options="state.minOptions" :removable="state.removable"
					:max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required"
					:readonly="state.readonly" />
			</svws-ui-input-wrapper>
			<template #controls>
				<HstCheckbox v-model="state.searchable" title="Searchable" />
				<HstCheckbox v-model="state.required" title="Required" />
				<HstCheckbox v-model="state.disabled" title="Disabled" />
				<HstCheckbox v-model="state.statistics" title="Statistik" />
				<HstCheckbox v-model="state.removable" title="Removable" />
				<HstCheckbox v-model="state.readonly" title="Readonly" />
				<HstCheckbox v-model="state.headless" title="Headless" />
				<HstNumber v-model="state.minOptions" title="minOptions" />
				<HstNumber v-model="state.maxOptions" title="maxOptions" />
				<HstRadio v-model="state.sort" title="Sortierung" :options="[
					{ label: 'ID', value: 'id' },
					{ label: 'Kürzel', value: 'kuerzel' },
					{ label: 'Text', value: 'text' },
				]" />
				<span class="text-headline-md">Farben</span>
				<HstRadio v-model="state.bgColor" title="Hintergrund" :options="[
					{ label: 'keine', value: '' },
					{ label: 'bg-ui-brand', value: 'bg-ui-brand' },
					{ label: 'bg-ui-success', value: 'bg-ui-success' },
					{ label: 'bg-ui-danger', value: 'bg-ui-danger' },
				]" />
				<HstRadio v-model="state.textColor" title="Text" :options="[
					{ label: 'keine', value: '' },
					{ label: 'text-ui-onbrand', value: 'text-ui-onbrand' },
					{ label: 'text-ui-onsuccess', value: 'text-ui-onsuccess' },
					{ label: 'text-ui-ondanger', value: 'text-ui-ondanger' },
				]" />
				<HstRadio v-model="state.iconColor" title="Icon" :options="[
					{ label: 'keine', value: '' },
					{ label: 'icon-ui-onbrand', value: 'icon-ui-onbrand' },
					{ label: 'icon-ui-onsuccess', value: 'icon-ui-onsuccess' },
					{ label: 'icon-ui-ondanger', value: 'icon-ui-ondanger' },
				]" />
				<HstRadio v-model="state.borderColor" title="Border" :options="[
					{ label: 'keine', value: '' },
					{ label: 'border-ui-onbrand', value: 'border-ui-onbrand' },
					{ label: 'border-ui-onsuccess', value: 'border-ui-onsuccess' },
					{ label: 'border-ui-ondanger', value: 'border-ui-ondanger' },
				]" />
			</template>
		</Variant>
		<Variant title="Validatoren" id="validatoren">
			<svws-ui-content-card class="p-5">
				<svws-ui-input-wrapper>
					<ui-select label="SelectManager mit Muss-Validator" :manager="sMussValidatorSelectManager"
						:validator="() => validatorMuss" :do-validate="validateSelect" v-model="mussValidatorSelection"
						:searchable="state.searchable" :disabled="state.disabled" :statistics="state.statistics"
						:removable="state.removable" :headless="state.headless" :required="state.required"
						:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :readonly="state.readonly" />
					<ui-select label="SelectManager mit Kann-Validator" :manager="sKannValidatorSelectManager"
						:validator="() => validatorKann" :do-validate="validateSelect" v-model="kannValidatorSelection"
						:searchable="state.searchable" :disabled="state.disabled" :statistics="state.statistics"
						:removable="state.removable" :headless="state.headless" :required="state.required"
						:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :readonly="state.readonly" />
					<ui-select label="SelectManager mit Hinweis-Validator" :manager="sHinweisValidatorSelectManager"
						:validator="() => validatorHinweis" :do-validate="validateSelect" v-model="hinweisValidatorSelection"
						:searchable="state.searchable" :disabled="state.disabled" :statistics="state.statistics"
						:removable="state.removable" :headless="state.headless" :required="state.required"
						:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :readonly="state.readonly" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<template #controls>
				<HstCheckbox v-model="state.searchable" title="Searchable" />
				<HstCheckbox v-model="state.required" title="Required" />
				<HstCheckbox v-model="state.disabled" title="Disabled" />
				<HstCheckbox v-model="state.statistics" title="Statistik" />
				<HstCheckbox v-model="state.removable" title="Removable" />
				<HstCheckbox v-model="state.readonly" title="Readonly" />
				<HstCheckbox v-model="state.headless" title="Headless" />
				<span class="text-headline-md">Farben</span>
				<HstRadio v-model="state.bgColor" title="Hintergrund" :options="[
					{ label: 'keine', value: '' },
					{ label: 'bg-ui-brand', value: 'bg-ui-brand' },
					{ label: 'bg-ui-success', value: 'bg-ui-success' },
					{ label: 'bg-ui-danger', value: 'bg-ui-danger' },
				]" />
				<HstRadio v-model="state.textColor" title="Text" :options="[
					{ label: 'keine', value: '' },
					{ label: 'text-ui-onbrand', value: 'text-ui-onbrand' },
					{ label: 'text-ui-onsuccess', value: 'text-ui-onsuccess' },
					{ label: 'text-ui-ondanger', value: 'text-ui-ondanger' },
				]" />
				<HstRadio v-model="state.iconColor" title="Icon" :options="[
					{ label: 'keine', value: '' },
					{ label: 'icon-ui-onbrand', value: 'icon-ui-onbrand' },
					{ label: 'icon-ui-onsuccess', value: 'icon-ui-onsuccess' },
					{ label: 'icon-ui-ondanger', value: 'icon-ui-ondanger' },
				]" />
				<HstRadio v-model="state.borderColor" title="Border" :options="[
					{ label: 'keine', value: '' },
					{ label: 'border-ui-brand', value: 'border-ui-brand' },
					{ label: 'border-ui-success', value: 'border-ui-success' },
					{ label: 'border-ui-danger', value: 'border-ui-danger' },
				]" />
			</template>
		</Variant>
		<template #controls>
			<HstCheckbox v-model="state.searchable" title="Searchable" />
			<HstCheckbox v-model="state.required" title="Required" />
			<HstCheckbox v-model="state.disabled" title="Disabled" />
			<HstCheckbox v-model="state.statistics" title="Statistik" />
			<HstCheckbox v-model="state.removable" title="Removable" />
			<HstCheckbox v-model="state.readonly" title="Readonly" />
			<HstCheckbox v-model="state.headless" title="Headless" />
			<HstNumber v-model="state.minOptions" title="minOptions" />
			<HstNumber v-model="state.maxOptions" title="maxOptions" />
			<span class="text-headline-md">Farben</span>
			<HstRadio v-model="state.bgColor" title="Hintergrund" :options="[
				{ label: 'keine', value: '' },
				{ label: 'bg-ui-brand', value: 'bg-ui-brand' },
				{ label: 'bg-ui-success', value: 'bg-ui-success' },
				{ label: 'bg-ui-danger', value: 'bg-ui-danger' },
			]" />
			<HstRadio v-model="state.textColor" title="Text" :options="[
				{ label: 'keine', value: '' },
				{ label: 'text-ui-onbrand', value: 'text-ui-onbrand' },
				{ label: 'text-ui-onsuccess', value: 'text-ui-onsuccess' },
				{ label: 'text-ui-ondanger', value: 'text-ui-ondanger' },
			]" />
			<HstRadio v-model="state.iconColor" title="Icon" :options="[
				{ label: 'keine', value: '' },
				{ label: 'icon-ui-onbrand', value: 'icon-ui-onbrand' },
				{ label: 'icon-ui-onsuccess', value: 'icon-ui-onsuccess' },
				{ label: 'icon-ui-ondanger', value: 'icon-ui-ondanger' },
			]" />
			<HstRadio v-model="state.borderColor" title="Border" :options="[
				{ label: 'keine', value: '' },
				{ label: 'border-ui-onbrand', value: 'border-ui-onbrand' },
				{ label: 'border-ui-onsuccess', value: 'border-ui-onsuccess' },
				{ label: 'border-ui-ondanger', value: 'border-ui-ondanger' },
			]" />
		</template>
	</Story>
</template>

<script setup lang="ts">

	import { computed, ref, reactive } from "vue";
	import { FachSelectFilter } from "./filter/FachSelectFilter";
	import { Fach } from "../../../../../core/src/asd/types/fach/Fach";
	import { Fachgruppe } from "../../../../../core/src/asd/types/fach/Fachgruppe";
	import { LehrerRechtsverhaeltnis } from "../../../../../core/src/asd/types/lehrer/LehrerRechtsverhaeltnis";
	import { Schulform } from "../../../../../core/src/asd/types/schule/Schulform";
	import type { Validator } from "../../../../../core/src/asd/validate/Validator";
	import type { LehrerRechtsverhaeltnisKatalogEintrag } from "../../../../../core/src/asd/data/lehrer/LehrerRechtsverhaeltnisKatalogEintrag";
	import { LehrerPersonalabschnittsdaten } from "../../../../../core/src/asd/data/lehrer/LehrerPersonalabschnittsdaten";
	import { LehrerStammdaten } from "../../../../../core/src/asd/data/lehrer/LehrerStammdaten";
	import { SchuleStammdaten } from "../../../../../core/src/asd/data/schule/SchuleStammdaten";
	import { Schuljahresabschnitt } from "../../../../../core/src/asd/data/schule/Schuljahresabschnitt";
	import { ValidatorKontext } from "../../../../../core/src/asd/validate/ValidatorKontext";
	import { ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll } from "../../../../../core/src/asd/validate/lehrer/ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll";
	import { ValidatorLehrerStammdatenNachname } from "../../../../../core/src/asd/validate/lehrer/ValidatorLehrerStammdatenNachname";
	import { ValidatorLehrerStammdatenVorname } from "../../../../../core/src/asd/validate/lehrer/ValidatorLehrerStammdatenVorname";
	import { CoreTypeSelectManager } from "./selectManager/CoreTypeSelectManager";
	import { SelectManager } from "./selectManager/SelectManager";
	import Docs from "./UiSelect.story.md"

	const state = reactive({
		searchable: false,
		disabled: false,
		statistics: false,
		removable: true,
		readonly: false,
		required: false,
		headless: false,
		minOptions: undefined as number | undefined,
		maxOptions: undefined as number | undefined,
		bgColor: "",
		textColor: "",
		iconColor: "",
		borderColor: "",
		sort: "id" as "id" | "kuerzel" | "text",
	});


	const filterState1 = reactive({
		fremdsprache: false,
		musikUndKunst1: false,
	});

	const filterState2 = reactive({
		deutsch: false,
		musikUndKunst2: false,
	});

	const fruitItems: string[] = ["Ananas", "Aprikose", "Banane", "Birne", "Apfelsine", "Brombeere", "Clementine", "Granatapfel", "Himbeere",
		"Ich will gleich den ganzen Obstkorb haben und am liebsten alles doppelt und dreifach, nur damit dieses Item einen langen Text zur Vorschau hat.",
		"Kirsche", "Kiwi", "Lemon", "Litschi", "Melone", "Orange", "Papaya", "Pfirsich", "Pflaume", "Rote Johannisbeere", "Zitronenmelisse",
	];
	const carItems: { marke: string, color: string, baujahr: number }[] = [{ marke: "BMW", color: "blue", baujahr: 2006 },
		{ marke: "Audi", color: "red", baujahr: 2008}, { marke: "Opel", color: "schwarz", baujahr: 2006 }];


	const stringSelectManager = new SelectManager({options: fruitItems});

	const coreTypeSelectManager = new CoreTypeSelectManager({
		clazz: LehrerRechtsverhaeltnis.class, schuljahr: 2018, schulformen: Schulform.GY,
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	});

	const objectSelectManager = new SelectManager({
		options: carItems,
		selectionDisplayText: (option: { marke: string, color: string; }) => option.marke,
		optionDisplayText: (option: { marke: string, color: string; }) => `${option.marke} - ${option.color}`,
	});

	const deepSearchSelectManager = new SelectManager({
		options: carItems,
		selectionDisplayText: (option: { marke: string, color: string, baujahr: number; }) => option.marke,
		optionDisplayText: (option: { marke: string, color: string, baujahr: number; }) => `${option.marke} - ${option.color}`,
	});

	const filter1 = computed<Fachgruppe[]>(() => {
		const result: Fachgruppe[] = [];
		if (filterState1.fremdsprache)
			result.push(Fachgruppe.FG_FS);
		if (filterState1.musikUndKunst1)
			result.push(Fachgruppe.FG_MS);
		return result;
	});

	const filter2 = computed<Fachgruppe[]>(() => {
		const result: Fachgruppe[] = [];
		if (filterState2.deutsch)
			result.push(Fachgruppe.FG_D);
		if (filterState2.musikUndKunst2)
			result.push(Fachgruppe.FG_MS);
		return result;
	});


	const filters = ref([new FachSelectFilter("fachgruppe1", filter1), new FachSelectFilter("fachgruppe2", filter2)]);

	const fachSelectManager = new CoreTypeSelectManager({
		clazz: Fach.class, schuljahr: 2020, schulformen: Schulform.GY, optionDisplayText: 'kuerzelText',
		selectionDisplayText: 'text', filters: filters,
	});

	const sortById = (a: LehrerRechtsverhaeltnisKatalogEintrag, b: LehrerRechtsverhaeltnisKatalogEintrag) => {
		if (a.id < b.id) return -1;
		if (a.id > b.id) return 1;
		return 0;
	};

	const sortByKuerzel = (a: LehrerRechtsverhaeltnisKatalogEintrag, b: LehrerRechtsverhaeltnisKatalogEintrag) => {
		if (a.kuerzel < b.kuerzel) return -1;
		if (a.kuerzel > b.kuerzel) return 1;
		return 0;
	};

	const sortByText = (a: LehrerRechtsverhaeltnisKatalogEintrag, b: LehrerRechtsverhaeltnisKatalogEintrag) => {
		if (a.text < b.text) return -1;
		if (a.text > b.text) return 1;
		return 0;
	};

	const computedSort = computed(() => {
		switch (state.sort) {
			case "kuerzel":
				return sortByKuerzel;
			case "text":
				return sortByText;
			default:
				return sortById;
		}
	});


	const sortableCoreTypeSelectManager = new CoreTypeSelectManager({
		clazz: LehrerRechtsverhaeltnis.class, schuljahr: 2018, schulformen: Schulform.GY,
		optionDisplayText: (a) => `${a.id} - ${a.kuerzel} - ${a.text}`, selectionDisplayText: 'text',
		sort: computedSort,
	});

	/**
	 * Validatoren
	 */

	const schuleStammdaten = new SchuleStammdaten();
	schuleStammdaten.schulform = Schulform.GY.toString();
	schuleStammdaten.idSchuljahresabschnitt = 16;
	const schuljahresabschnitt = new Schuljahresabschnitt();
	schuljahresabschnitt.id = 16;
	schuljahresabschnitt.schuljahr = 2023;
	schuljahresabschnitt.abschnitt = 1;
	schuljahresabschnitt.idVorigerAbschnitt = null;
	schuljahresabschnitt.idFolgeAbschnitt = null;
	schuleStammdaten.abschnitte.add(schuljahresabschnitt);
	const selectValidatorKontext = new ValidatorKontext(schuleStammdaten, false);


	const hinweisValidatorSelection = ref<string | undefined>();
	const kannValidatorSelection = ref<string | undefined>();
	const mussValidatorSelection = ref<string | undefined>();


	const sHinweisValidatorSelectManager = new SelectManager({options: ["Christian", "Anna"]});

	const sKannValidatorSelectManager = new SelectManager({options: ["30 Pflichtstunden", "100 Pflichtstunden"] });

	const sMussValidatorSelectManager = new SelectManager({ options: ["Müller", "Meier"] });


	const validatorHinweis = computed(() => {
		const lehrerStammdaten = new LehrerStammdaten();
		lehrerStammdaten.vorname = hinweisValidatorSelection.value ?? "";
		return new ValidatorLehrerStammdatenVorname(lehrerStammdaten, selectValidatorKontext);
	});

	const validatorKann = computed(() => {
		const lehrerPersonalabschnittsdaten = new LehrerPersonalabschnittsdaten();
		switch (kannValidatorSelection.value) {
			case "30 Pflichtstunden":
				lehrerPersonalabschnittsdaten.pflichtstundensoll = 30.0;
				break;
			case "100 Pflichtstunden":
				lehrerPersonalabschnittsdaten.pflichtstundensoll = 100.0;
				break;
			default:
				break;
		}
		return new ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll(lehrerPersonalabschnittsdaten, selectValidatorKontext);
	});


	const validatorMuss = computed(() => {
		const lehrerStammdaten = new LehrerStammdaten();
		lehrerStammdaten.nachname = mussValidatorSelection.value ?? "";
		return new ValidatorLehrerStammdatenNachname(lehrerStammdaten, selectValidatorKontext);
	});

	function validateSelect(validator: Validator, value: string | Iterable<string> | null): boolean {
		return validator.run();
	}

	function getSourceString (multi = false) {
		return `<ui-select
        label="..."
        :manager="..."
        ${state.searchable ? 'searchable' : ''}
        ${state.disabled ? 'disabled' : ''}
        ${state.statistics ? 'statistics' : ''}
		${state.required ? 'required' : ''}
        ${state.headless ? 'headless' : ''}
		':sort="(a, b) => ..."' : ''
      `.split('\n').filter(line => line.trim() !== '').join('\n');

	}
</script>
