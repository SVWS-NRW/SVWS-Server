<template>
	<Story title="Select New" id="ui-select" icon="ri:expand-up-down-line" auto-props-disabled :layout="{ type: 'grid', width: '45%'}"
		:source="getSourceString()">
		<template #docs>
			<Docs />
		</template>
		<Variant title="SelectManager" id="selectManager">
			<svws-ui-input-wrapper>
				<ui-select label="SelectManager mit String" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]"
					:manager="stringSelectManager"
					:searchable="state.searchable" :removable="state.removable" :disabled="state.disabled" :statistics="state.statistics"
					:headless="state.headless" :readonly="state.readonly" :required="state.required" />
				<ui-select label="SelectManager mit Custom-Objekten" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]"
					:manager="objectSelectManager"
					:searchable="state.searchable" :removable="state.removable" :disabled="state.disabled" :statistics="state.statistics"
					:headless="state.headless" :readonly="state.readonly" :required="state.required" />
				<ui-select label="CoreTypeSelectManager mit LehrerRechtsverhaeltnis"
					:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]"
					:manager="coreTypeSelectManager"
					:searchable="state.searchable" :removable="state.removable" :disabled="state.disabled" :statistics="state.statistics"
					:headless="state.headless" :readonly="state.readonly" :required="state.required" />
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
				<ui-select label="CoreTypeSelectManager Fach abhängig von Fachgruppe"
					:class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]"
					:manager="fachSelectManager"
					:searchable="state.searchable" :removable="state.removable" :disabled="state.disabled" :statistics="state.statistics"
					:headless="state.headless" :readonly="state.readonly" :required="state.required" />
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
				<ui-select label="Deep Search SelectManager" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]"
					:manager="deepSearchSelectManager"
					:deep-search-attributes="['marke', 'color', 'baujahr']"
					:searchable="state.searchable" :removable="state.removable" :disabled="state.disabled" :statistics="state.statistics"
					:headless="state.headless" :readonly="state.readonly" :required="state.required" />
			</svws-ui-input-wrapper>
		</Variant>
		<Variant title="Sortierung" id="sortierung">
			<svws-ui-input-wrapper>
				<ui-select label="Sortiertes Select" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]"
					:manager="sortableCoreTypeSelectManager"
					:searchable="state.searchable" :removable="state.removable" :disabled="state.disabled" :statistics="state.statistics"
					:headless="state.headless" :readonly="state.readonly" :required="state.required" />
			</svws-ui-input-wrapper>
			<template #controls>
				<HstCheckbox title="Searchable"
					v-model="state.searchable" />
				<HstCheckbox title="Required"
					v-model="state.required" />
				<HstCheckbox title="Disabled"
					v-model="state.disabled" />
				<HstCheckbox title="Statistik"
					v-model="state.statistics" />
				<HstCheckbox title="Removable"
					v-model="state.removable" />
				<HstCheckbox title="Readonly"
					v-model="state.readonly" />
				<HstCheckbox title="Headless"
					v-model="state.headless" />
				<HstRadio title="Sortierung"
					v-model="state.sort" :options="[
						{ label: 'ID', value: 'id' },
						{ label: 'Kürzel', value: 'kuerzel' },
						{ label: 'Text', value: 'text' },
					]" />
				<span class="text-headline-md">Farben</span>
				<HstRadio title="Hintergrund"
					v-model="state.bgColor"
					:options="[
						{ label: 'keine', value: '' },
						{ label: 'bg-ui-brand', value: 'bg-ui-brand' },
						{ label: 'bg-ui-success', value: 'bg-ui-success' },
						{ label: 'bg-ui-danger', value: 'bg-ui-danger' },
					]" />
				<HstRadio title="Text"
					v-model="state.textColor"
					:options="[
						{ label: 'keine', value: '' },
						{ label: 'text-ui-onbrand', value: 'text-ui-onbrand' },
						{ label: 'text-ui-onsuccess', value: 'text-ui-onsuccess' },
						{ label: 'text-ui-ondanger', value: 'text-ui-ondanger' },
					]" />
				<HstRadio title="Icon"
					v-model="state.iconColor"
					:options="[
						{ label: 'keine', value: '' },
						{ label: 'icon-ui-onbrand', value: 'icon-ui-onbrand' },
						{ label: 'icon-ui-onsuccess', value: 'icon-ui-onsuccess' },
						{ label: 'icon-ui-ondanger', value: 'icon-ui-ondanger' },
					]" />
				<HstRadio title="Border"
					v-model="state.borderColor"
					:options="[
						{ label: 'keine', value: '' },
						{ label: 'border-ui-brand', value: 'border-ui-brand' },
						{ label: 'border-ui-success', value: 'border-ui-success' },
						{ label: 'border-ui-danger', value: 'border-ui-danger' },
					]" />
			</template>
		</Variant>
		<Variant title="Validatoren" id="validatoren">
			<svws-ui-content-card class="p-5">
				<svws-ui-input-wrapper>
					<ui-select label="SelectManager mit Muss-Validator" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]"
						v-model="mussValidatorSelection"
						:manager="sMussValidatorSelectManager"
						:validation="validateMuss"
						:searchable="state.searchable" :removable="state.removable" :disabled="state.disabled" :statistics="state.statistics"
						:headless="state.headless" :readonly="state.readonly" :required="state.required" />
					<ui-select label="SelectManager mit Kann-Validator" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]"
						v-model="kannValidatorSelection"
						:manager="sKannValidatorSelectManager"
						:validation="validateKann"
						:searchable="state.searchable" :removable="state.removable" :disabled="state.disabled" :statistics="state.statistics"
						:headless="state.headless" :readonly="state.readonly" :required="state.required" />
					<ui-select label="SelectManager mit Hinweis-Validator" :class="[state.bgColor, state.textColor, state.iconColor, state.borderColor]"
						v-model="hinweisValidatorSelection"
						:manager="sHinweisValidatorSelectManager"
						:validation="validateHinweis"
						:searchable="state.searchable" :removable="state.removable" :disabled="state.disabled" :statistics="state.statistics"
						:headless="state.headless" :readonly="state.readonly" :required="state.required" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</Variant>
		<template #controls>
			<HstCheckbox title="Searchable"
				v-model="state.searchable" />
			<HstCheckbox title="Required"
				v-model="state.required" />
			<HstCheckbox title="Disabled"
				v-model="state.disabled" />
			<HstCheckbox title="Statistik"
				v-model="state.statistics" />
			<HstCheckbox title="Removable"
				v-model="state.removable" />
			<HstCheckbox title="Readonly"
				v-model="state.readonly" />
			<HstCheckbox title="Headless"
				v-model="state.headless" />
			<span class="text-headline-md">Farben</span>
			<HstRadio title="Hintergrund"
				v-model="state.bgColor"
				:options="[
					{ label: 'keine', value: '' },
					{ label: 'bg-ui-brand', value: 'bg-ui-brand' },
					{ label: 'bg-ui-success', value: 'bg-ui-success' },
					{ label: 'bg-ui-danger', value: 'bg-ui-danger' },
				]" />
			<HstRadio title="Text"
				v-model="state.textColor"
				:options="[
					{ label: 'keine', value: '' },
					{ label: 'text-ui-onbrand', value: 'text-ui-onbrand' },
					{ label: 'text-ui-onsuccess', value: 'text-ui-onsuccess' },
					{ label: 'text-ui-ondanger', value: 'text-ui-ondanger' },
				]" />
			<HstRadio title="Icon"
				v-model="state.iconColor"
				:options="[
					{ label: 'keine', value: '' },
					{ label: 'icon-ui-onbrand', value: 'icon-ui-onbrand' },
					{ label: 'icon-ui-onsuccess', value: 'icon-ui-onsuccess' },
					{ label: 'icon-ui-ondanger', value: 'icon-ui-ondanger' },
				]" />
			<HstRadio title="Border"
				v-model="state.borderColor"
				:options="[
					{ label: 'keine', value: '' },
					{ label: 'border-ui-brand', value: 'border-ui-brand' },
					{ label: 'border-ui-success', value: 'border-ui-success' },
					{ label: 'border-ui-danger', value: 'border-ui-danger' },
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
	import { BasicValidator } from "../../../../../core/src/asd/validate/BasicValidator";
	import type { LehrerRechtsverhaeltnisKatalogEintrag } from "../../../../../core/src/asd/data/lehrer/LehrerRechtsverhaeltnisKatalogEintrag";
	import { CoreTypeSelectManager } from "./manager/CoreTypeSelectManager";
	import { SelectManager } from "./manager/SelectManager";
	import Docs from "./UiSelect.story.md";
	import { ValidatorFehlerart } from "../../../../../core/src/asd/validate/ValidatorFehlerart";
	import type { List } from "../../../../../core/src/java/util/List";
	import type { ValidatorFehler } from "../../../../../core/src/asd/validate/ValidatorFehler";

	const state = reactive({
		searchable: false,
		disabled: false,
		statistics: false,
		removable: true,
		readonly: false,
		required: false,
		headless: false,
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
		{ marke: "Audi", color: "red", baujahr: 2008 }, { marke: "Opel", color: "schwarz", baujahr: 2006 }];


	const stringSelectManager = new SelectManager({ options: fruitItems });

	const coreTypeSelectManager = new CoreTypeSelectManager({
		clazz: LehrerRechtsverhaeltnis.class, schuljahr: 2018, schulformen: Schulform.GY,
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	});

	const objectSelectManager = new SelectManager({
		options: carItems,
		selectionDisplayText: (option: { marke: string, color: string; }): string => option.marke,
		optionDisplayText: (option: { marke: string, color: string; }): string => `${option.marke} - ${option.color}`,
	});

	const deepSearchSelectManager = new SelectManager({
		options: carItems,
		selectionDisplayText: (option: { marke: string, color: string, baujahr: number; }): string => option.marke,
		optionDisplayText: (option: { marke: string, color: string, baujahr: number; }): string => `${option.marke} - ${option.color}`,
	});

	const filter1 = computed<Fachgruppe[]>(() => {
		const result: Fachgruppe[] = [];
		if (filterState1.fremdsprache) {
			result.push(Fachgruppe.FG_FS);
		}
		if (filterState1.musikUndKunst1) {
			result.push(Fachgruppe.FG_MS);
		}
		return result;
	});

	const filter2 = computed<Fachgruppe[]>(() => {
		const result: Fachgruppe[] = [];
		if (filterState2.deutsch) {
			result.push(Fachgruppe.FG_D);
		}
		if (filterState2.musikUndKunst2) {
			result.push(Fachgruppe.FG_MS);
		}
		return result;
	});


	const filters = ref([new FachSelectFilter("fachgruppe1", filter1), new FachSelectFilter("fachgruppe2", filter2)]);

	const fachSelectManager = new CoreTypeSelectManager({
		clazz: Fach.class, schuljahr: 2020, schulformen: Schulform.GY, optionDisplayText: 'kuerzelText',
		selectionDisplayText: 'text', filters: filters,
	});

	const sortById = (a: LehrerRechtsverhaeltnisKatalogEintrag, b: LehrerRechtsverhaeltnisKatalogEintrag): number => {
		if (a.id < b.id) {
			return -1;
		}
		if (a.id > b.id) {
			return 1;
		}
		return 0;
	};

	const sortByKuerzel = (a: LehrerRechtsverhaeltnisKatalogEintrag, b: LehrerRechtsverhaeltnisKatalogEintrag): number => {
		if (a.kuerzel < b.kuerzel) {
			return -1;
		}
		if (a.kuerzel > b.kuerzel) {
			return 1;
		}
		return 0;
	};

	const sortByText = (a: LehrerRechtsverhaeltnisKatalogEintrag, b: LehrerRechtsverhaeltnisKatalogEintrag): number => {
		if (a.text < b.text) {
			return -1;
		}
		if (a.text > b.text) {
			return 1;
		}
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
		optionDisplayText: (a): string => `${a.id} - ${a.kuerzel} - ${a.text}`, selectionDisplayText: 'text',
		sort: computedSort,
	});

	/**
	 * Validatoren
	 */

	const hinweisValidatorSelection = ref<string | undefined>();
	const kannValidatorSelection = ref<string | undefined>();
	const mussValidatorSelection = ref<string | undefined>();

	const sHinweisValidatorSelectManager = new SelectManager({ options: ["Christian", "Anna"] });
	const sKannValidatorSelectManager = new SelectManager({ options: ["20 Pflichtstunden", "40 Pflichtstunden"] });
	const sMussValidatorSelectManager = new SelectManager({ options: ["Müller", "Meier"] });

	class ValidatorTest extends BasicValidator {
		private readonly testfn: () => string | null;
		constructor(testfn: () => string | null, art: ValidatorFehlerart) {
			super(art);
			this.testfn = testfn;
		}
		protected pruefe(): boolean {
			const result = this.testfn();
			if (result !== null) {
				this.addFehler(0, result);
			}
			return (result === null);
		}
	}
	function validateHinweis(): List<ValidatorFehler> {
		validatorHinweis.value.run();
		return validatorHinweis.value.getFehler();
	}
	function validateKann(): List<ValidatorFehler> {
		validatorKann.value.run();
		return validatorKann.value.getFehler();
	}
	function validateMuss(): List<ValidatorFehler> {
		validatorMuss.value.run();
		return validatorMuss.value.getFehler();
	}

	const validatorHinweis = computed(() => new ValidatorTest(() => (hinweisValidatorSelection.value === "Anna") ? null : "Hier ist die Eintragung von Anna gewünscht.", ValidatorFehlerart.HINWEIS));
	const validatorKann = computed(() => new ValidatorTest(() => (kannValidatorSelection.value === "20 Pflichtstunden") ? null : "Der Pflichstundensoll sollte eingetragen werden. Ein zu hoher Wert ist nicht plausibel.", ValidatorFehlerart.KANN));
	const validatorMuss = computed(() => new ValidatorTest(() => (mussValidatorSelection.value === "Müller") ? null : "In diesem Feld ist es verpflichtend, dass Müller ausgewählt wird. Alles andere führt zu einem Fehler.", ValidatorFehlerart.MUSS));

	function getSourceString(): string {
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
