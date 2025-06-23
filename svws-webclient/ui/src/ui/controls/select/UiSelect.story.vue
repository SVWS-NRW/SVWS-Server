<template>
	<Story title="Select New" id="ui-select" icon="ri:expand-up-down-line" auto-props-disabled :layout="{ type: 'grid', width: '45%'}" :source="getSourceString()">
		<template #docs><Docs /></template>
		<Variant title="Single Selection" id="single">
			<svws-ui-input-wrapper>
				<ui-select label="BaseSelectManager mit String" :manager="sStringSelectManager" :searchable="state.searchable"
					:disabled="state.disabled" :statistics="state.statistics" :headless="state.headless"
					:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
				<ui-select label="BaseSelectManager mit Custom-Objekten" :manager="sObjectSelectManager" :searchable="state.searchable"
					:disabled="state.disabled" :statistics="state.statistics" :headless="state.headless"
					:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
				<ui-select label="CoreTypeSelectManager" :manager="sCoreTypeSelectManager" :searchable="state.searchable" :disabled="state.disabled"
					:statistics="state.statistics" :headless="state.headless"
					:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
			</svws-ui-input-wrapper>
			<template #controls>
				<HstCheckbox v-model="state.searchable" title="Searchable" />
				<HstCheckbox v-model="state.required" title="Required" />
				<HstCheckbox v-model="state.disabled" title="Disabled" />
				<HstCheckbox v-model="state.statistics" title="Statistik" />
				<HstCheckbox v-model="state.removable" title="Removable" />
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
		<Variant title="Multi Selection" :source="getSourceString(true)" id="multi">
			<svws-ui-input-wrapper>
				<ui-select label="BaseSelectManager mit String" :manager="mStringSelectManager" :searchable="state.searchable"
					:disabled="state.disabled" :statistics="state.statistics" :headless="state.headless"
					:min-options="state.minOptions" :max-options="state.maxOptions" :required="state.required"
					:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
				<ui-select label="BaseSelectManager mit Custom-Objekten" :manager="mObjectSelectManager" :searchable="state.searchable" :disabled="state.disabled"
					:statistics="state.statistics" :headless="state.headless" :min-options="state.minOptions"
					:max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
				<ui-select label="CoreTypeSelectManager" :manager="mCoreTypeSelectManager" :searchable="state.searchable" :disabled="state.disabled"
					:statistics="state.statistics" :headless="state.headless" :min-options="state.minOptions"
					:max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
			</svws-ui-input-wrapper>
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
				<ui-select label="CoreTypeSelectManager Fach abhängig von Fachgruppe" :manager="sFachSelectManager" :searchable="state.searchable"
					:disabled="state.disabled" :statistics="state.statistics" :headless="state.headless"
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
				<ui-select label="Deep Search BaseSelectManager" :manager="deepSearchBaseSelectManager" :searchable="true" :disabled="state.disabled"
					:statistics="state.statistics" :headless="state.headless" :min-options="state.minOptions"
					:max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
			</svws-ui-input-wrapper>
		</Variant>
		<Variant title="Sortierung" id="sortierung">
			<svws-ui-input-wrapper>
				<ui-select label="Sortiertes Select" :manager="sortableCoreTypeSelectManager" :searchable="true" :disabled="state.disabled"
					:statistics="state.statistics" :headless="state.headless" :min-options="state.minOptions"
					:max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
			</svws-ui-input-wrapper>
			<template #controls>
				<HstCheckbox v-model="state.searchable" title="Searchable" />
				<HstCheckbox v-model="state.required" title="Required" />
				<HstCheckbox v-model="state.disabled" title="Disabled" />
				<HstCheckbox v-model="state.statistics" title="Statistik" />
				<HstCheckbox v-model="state.removable" title="Removable" />
				<HstCheckbox v-model="state.headless" title="Headless" />
				<HstNumber v-model="state.minOptions" title="minOptions" />
				<HstNumber v-model="state.maxOptions" title="maxOptions" />
				<HstRadio v-model="state.sort" title="Sortierung" :options="[
					{ label: 'keine', value: 'unsorted' },
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
		<template #controls>
			<HstCheckbox v-model="state.searchable" title="Searchable" />
			<HstCheckbox v-model="state.required" title="Required" />
			<HstCheckbox v-model="state.disabled" title="Disabled" />
			<HstCheckbox v-model="state.statistics" title="Statistik" />
			<HstCheckbox v-model="state.removable" title="Removable" />
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

	import { reactive, watch } from "vue";
	import { BaseSelectManager } from "./selectManager/BaseSelectManager";
	import { CoreTypeSelectManager } from "./selectManager/CoreTypeSelectManager";
	import { FachSelectFilter } from "./filter/FachSelectFilter";
	import { ArrayList } from "../../../../../core/src/java/util/ArrayList";
	import { Fach } from "../../../../../core/src/asd/types/fach/Fach";
	import { Fachgruppe } from "../../../../../core/src/asd/types/fach/Fachgruppe";
	import { LehrerRechtsverhaeltnis } from "../../../../../core/src/asd/types/lehrer/LehrerRechtsverhaeltnis";
	import { Schulform } from "../../../../../core/src/asd/types/schule/Schulform";
	import Docs from "./UiSelect.story.md"

	const state = reactive({
		searchable: false,
		disabled: false,
		statistics: false,
		removable: true,
		required: false,
		headless: false,
		minOptions: undefined as number | undefined,
		maxOptions: undefined as number | undefined,
		bgColor: "",
		textColor: "",
		iconColor: "",
		borderColor: "",
		sort: "unsorted" as "unsorted" | "id" | "kuerzel" | "text",
	});


	const filterState1 = reactive({
		fremdsprache: false,
		musikUndKunst1: false,
	});

	const filterState2 = reactive({
		deutsch: false,
		musikUndKunst2: false,
	});

	const stringItems: string[] = ["Ananas", "Aprikose", "Banane", "Birne", "Apfelsine", "Brombeere", "Clementine", "Granatapfel", "Himbeere",
		"Ich will gleich den ganzen Obstkorb haben und am liebsten alles doppelt und dreifach, nur damit dieses Item einen langen Text zur Vorschau hat.",
		"Kirsche", "Kiwi", "Lemon", "Litschi", "Melone", "Orange", "Papaya", "Pfirsich", "Pflaume", "Rote Johannisbeere", "Zitronenmelisse",
	];
	const carItems: { marke: string, color: string, baujahr: number }[] = [{ marke: "BMW", color: "blue", baujahr: 2006 },
		{ marke: "Audi", color: "red", baujahr: 2008}, { marke: "Opel", color: "schwarz", baujahr: 2006 }];


	const sStringSelectManager = new BaseSelectManager({options: stringItems, removable: state.removable});

	const sCoreTypeSelectManager = new CoreTypeSelectManager({
		clazz: LehrerRechtsverhaeltnis.class, schuljahr: 2018, schulformen: Schulform.GY,
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText', removable: state.removable,
	});

	const sObjectSelectManager = new BaseSelectManager({
		options: carItems, removable: state.removable,
		selectionDisplayText: (option: { marke: string, color: string; }) => option.marke,
		optionDisplayText: (option: { marke: string, color: string; }) => `${option.marke} - ${option.color}`,
	});

	const deepSearchBaseSelectManager = new BaseSelectManager({
		options: carItems, removable: state.removable, deepSearchAttributes: ["marke", "color", "baujahr"],
		selectionDisplayText: (option: { marke: string, color: string, baujahr: number; }) => option.marke,
		optionDisplayText: (option: { marke: string, color: string, baujahr: number; }) => `${option.marke} - ${option.color}`,
	});

	const sFachSelectManager = new CoreTypeSelectManager({
		clazz: Fach.class, schuljahr: 2020, schulformen: Schulform.GY, optionDisplayText: 'kuerzelText', removable: state.removable,
		selectionDisplayText: 'text', filters: [new FachSelectFilter("fachgruppe1", [], 2020), new FachSelectFilter("fachgruppe2", [], 2020)],
	});

	watch(filterState1, () => {
		const list = new ArrayList<Fachgruppe>();
		if (filterState1.fremdsprache)
			list.add(Fachgruppe.FG_FS);
		if (filterState1.musikUndKunst1)
			list.add(Fachgruppe.FG_MS);
		sFachSelectManager.updateFilteredOptions(new FachSelectFilter("fachgruppe1", [ ...list ], 2020));
	});

	watch(filterState2, () => {
		const list = new ArrayList<Fachgruppe>();
		if (filterState2.deutsch)
			list.add(Fachgruppe.FG_D);
		if (filterState2.musikUndKunst2)
			list.add(Fachgruppe.FG_MS);
		sFachSelectManager.updateFilteredOptions(new FachSelectFilter("fachgruppe2", [ ...list ], 2020));
	});

	watch(() => state.removable, (newVal) => {
		sStringSelectManager.removable = newVal;
		sCoreTypeSelectManager.removable = newVal;
		sObjectSelectManager.removable = newVal;
		mStringSelectManager.removable = newVal;
		mCoreTypeSelectManager.removable = newVal;
		mObjectSelectManager.removable = newVal;
		deepSearchBaseSelectManager.removable = newVal;
		sFachSelectManager.removable = newVal;
		sortableCoreTypeSelectManager.removable = newVal;
	});


	const mStringSelectManager = new BaseSelectManager({options: stringItems, multi: true, removable: state.removable});

	const mCoreTypeSelectManager = new CoreTypeSelectManager({
		clazz: LehrerRechtsverhaeltnis.class, schuljahr: 2018, schulformen: Schulform.GY, multi: true,
		removable: state.removable, optionDisplayText: 'kuerzelText', selectionDisplayText: 'kuerzelText',
	});

	const mObjectSelectManager = new BaseSelectManager({
		options: carItems, multi: true, removable: state.removable,
		selectionDisplayText: (option: { marke: string, color: string, baujahr: number; }) => option.marke,
		optionDisplayText: (option: { marke: string, color: string, baujahr: number; }) => `${option.marke} - ${option.color}`,
	});

	watch(() => state.sort, (newVal) => {
		sortableCoreTypeSelectManager.updateSort();
	});

	const sortableCoreTypeSelectManager = new CoreTypeSelectManager({
		clazz: LehrerRechtsverhaeltnis.class, schuljahr: 2018, schulformen: Schulform.GY, multi: true, removable: state.removable,
		optionDisplayText: (a) => `${a.id} - ${a.kuerzel} - ${a.text}`, selectionDisplayText: 'kuerzelText',
		sort: (a: LehrerRechtsverhaeltnis, b: LehrerRechtsverhaeltnis) => {
			if (state.sort === "unsorted") return 0;
			if (state.sort === "id") {
				if (a.daten(2018)!.id < b.daten(2018)!.id)
					return -1;
				if (a.daten(2018)!.id > b.daten(2018)!.id)
					return 1;
			}
			if (state.sort === "kuerzel") {
				if (a.daten(2018)!.kuerzel < b.daten(2018)!.kuerzel)
					return -1;
				if (a.daten(2018)!.kuerzel > b.daten(2018)!.kuerzel)
					return 1;
			}
			if (state.sort === "text") {
				if (a.daten(2018)!.text < b.daten(2018)!.text)
					return -1;
				if (a.daten(2018)!.text > b.daten(2018)!.text)
					return 1;
			}
			return 0;
		},
	});

	function getSourceString (multi = false) {
		return `<ui-select
        label="..."
        :manager="..."
        ${state.searchable ? 'searchable' : ''}
        ${state.disabled ? 'disabled' : ''}
        ${state.statistics ? 'statistics' : ''}
		${state.required ? 'required' : ''}
        ${state.headless ? 'headless' : ''}
		${state.sort !== 'unsorted' ? ':sort="(a, b) => ..."' : '' }
		${multi ? (state.minOptions !== undefined ? `:min-options="${state.minOptions}"` : '') : ''}
		${multi ? (state.maxOptions !== undefined ? `:max-options="${state.maxOptions}"` : '') : ''}
/>
      `.split('\n').filter(line => line.trim() !== '').join('\n');

	};
</script>
