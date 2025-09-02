<template>
	<Story title="Select Multi New" id="ui-select-multi" icon="ri:expand-up-down-line" auto-props-disabled :layout="{ type: 'grid', width: '45%'}" :source="getSourceString()">
		<template #docs><Docs /></template>
		<Variant title="SelectManager" :source="getSourceString()" id="selectManager">
			<svws-ui-input-wrapper>
				<ui-select-multi label="SelectManagerMulti mit String" :manager="stringSelectManager" :searchable="state.searchable"
					:disabled="state.disabled" :statistics="state.statistics" :headless="state.headless"
					:min-options="state.minOptions" :max-options="state.maxOptions" :required="state.required"
					:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" />
				<ui-select-multi label="SelectManagerMulti mit Custom-Objekten" :manager="objectSelectManager" :searchable="state.searchable" :disabled="state.disabled"
					:statistics="state.statistics" :headless="state.headless" :min-options="state.minOptions"
					:max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
				<ui-select-multi label="CoreTypeSelectManagerMulti mit LehrerRechtsverhaeltnis" :manager="coreTypeSelectManager" :searchable="state.searchable" :disabled="state.disabled"
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
				<ui-select-multi label="CoreTypeSelectManager Fach abhängig von Fachgruppe" :manager="fachSelectManager" :searchable="state.searchable"
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
				<ui-select-multi label="Deep Search SelectManager" :manager="deepSearchSelectManager" :searchable="true" :disabled="state.disabled"
					:statistics="state.statistics" :headless="state.headless" :min-options="state.minOptions"
					:max-options="state.maxOptions" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]" :required="state.required" />
			</svws-ui-input-wrapper>
		</Variant>
		<Variant title="Sortierung" id="sortierung">
			<svws-ui-input-wrapper>
				<ui-select-multi label="Sortiertes Select" :manager="sortableCoreTypeSelectManager" :searchable="true" :disabled="state.disabled"
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
	import { FachSelectFilter } from "./filter/FachSelectFilter";
	import { ArrayList } from "../../../../../core/src/java/util/ArrayList";
	import { Fach } from "../../../../../core/src/asd/types/fach/Fach";
	import { Fachgruppe } from "../../../../../core/src/asd/types/fach/Fachgruppe";
	import { LehrerRechtsverhaeltnis } from "../../../../../core/src/asd/types/lehrer/LehrerRechtsverhaeltnis";
	import { Schulform } from "../../../../../core/src/asd/types/schule/Schulform";
	import type { LehrerRechtsverhaeltnisKatalogEintrag } from "../../../../../core/src/asd/data/lehrer/LehrerRechtsverhaeltnisKatalogEintrag";
	import { SelectManagerMulti } from "./selectManager/SelectManagerMulti";
	import { CoreTypeSelectManagerMulti } from "./selectManager/CoreTypeSelectManagerMulti";
	import Docs from "./UiSelectMulti.story.md"

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

	const stringSelectManager = new SelectManagerMulti({options: stringItems, removable: state.removable});

	const coreTypeSelectManager = new CoreTypeSelectManagerMulti({
		clazz: LehrerRechtsverhaeltnis.class, schuljahr: 2018, schulformen: Schulform.GY, removable: state.removable, optionDisplayText: 'kuerzelText',
		selectionDisplayText: 'kuerzelText',
	});

	const objectSelectManager = new SelectManagerMulti({
		options: carItems, removable: state.removable,
		selectionDisplayText: (option: { marke: string, color: string, baujahr: number; }) => option.marke,
		optionDisplayText: (option: { marke: string, color: string, baujahr: number; }) => `${option.marke} - ${option.color}`,
	});

	const deepSearchSelectManager = new SelectManagerMulti({
		options: carItems, removable: state.removable, deepSearchAttributes: ["marke", "color", "baujahr"],
		selectionDisplayText: (option: { marke: string, color: string, baujahr: number; }) => option.marke,
		optionDisplayText: (option: { marke: string, color: string, baujahr: number; }) => `${option.marke} - ${option.color}`,
	});

	const fachSelectManager = new CoreTypeSelectManagerMulti({
		clazz: Fach.class, schuljahr: 2020, schulformen: Schulform.GY, optionDisplayText: 'kuerzelText', removable: state.removable,
		selectionDisplayText: 'text', filters: [new FachSelectFilter("fachgruppe1", [], 2020), new FachSelectFilter("fachgruppe2", [], 2020)],
	});

	watch(filterState1, () => {
		const list = new ArrayList<Fachgruppe>();
		if (filterState1.fremdsprache)
			list.add(Fachgruppe.FG_FS);
		if (filterState1.musikUndKunst1)
			list.add(Fachgruppe.FG_MS);
		fachSelectManager.updateFilteredOptions(new FachSelectFilter("fachgruppe1", [ ...list ], 2020));
	});

	watch(filterState2, () => {
		const list = new ArrayList<Fachgruppe>();
		if (filterState2.deutsch)
			list.add(Fachgruppe.FG_D);
		if (filterState2.musikUndKunst2)
			list.add(Fachgruppe.FG_MS);
		fachSelectManager.updateFilteredOptions(new FachSelectFilter("fachgruppe2", [ ...list ], 2020));
	});

	watch(() => state.removable, (newVal) => {
		stringSelectManager.removable = newVal;
		coreTypeSelectManager.removable = newVal;
		objectSelectManager.removable = newVal;
		deepSearchSelectManager.removable = newVal;
		fachSelectManager.removable = newVal;
		sortableCoreTypeSelectManager.removable = newVal;
	});

	watch(() => state.sort, () => {
		sortableCoreTypeSelectManager.updateSort();
	});

	const sortableCoreTypeSelectManager = new CoreTypeSelectManagerMulti({
		clazz: LehrerRechtsverhaeltnis.class, schuljahr: 2018, schulformen: Schulform.GY, removable: state.removable,
		optionDisplayText: (a) => `${a.id} - ${a.kuerzel} - ${a.text}`, selectionDisplayText: 'kuerzelText',
		sort: (a: LehrerRechtsverhaeltnisKatalogEintrag, b: LehrerRechtsverhaeltnisKatalogEintrag) => {
			if (state.sort === "unsorted") return 0;
			if (state.sort === "id") {
				if (a.id < b.id)
					return -1;
				if (a.id > b.id)
					return 1;
			}
			if (state.sort === "kuerzel") {
				if (a.kuerzel < b.kuerzel)
					return -1;
				if (a.kuerzel > b.kuerzel)
					return 1;
			}
			if (state.sort === "text") {
				if (a.text < b.text)
					return -1;
				if (a.text > b.text)
					return 1;
			}
			return 0;
		},
	});

	function getSourceString () {
		return `<ui-select-multi
        label="..."
        :manager="..."
        ${state.searchable ? 'searchable' : ''}
        ${state.disabled ? 'disabled' : ''}
        ${state.statistics ? 'statistics' : ''}
		${state.required ? 'required' : ''}
        ${state.headless ? 'headless' : ''}
		${state.sort !== 'unsorted' ? ':sort="(a, b) => ..."' : '' }
		${(state.minOptions !== undefined ? `:min-options="${state.minOptions}"` : '')}
		${(state.maxOptions !== undefined ? `:max-options="${state.maxOptions}"` : '')}
/>
      `.split('\n').filter(line => line.trim() !== '').join('\n');

	};
</script>
