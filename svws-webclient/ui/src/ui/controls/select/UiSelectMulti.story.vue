<template>
	<Story title="Select Multi New" id="ui-select-multi" icon="ri:expand-up-down-line" auto-props-disabled :layout="{ type: 'grid', width: '45%'}"
		:source="getSourceString()">
		<template #docs>
			<Docs />
		</template>

		<Variant title="SelectManager" id="selectManager">
			<svws-ui-input-wrapper>
				<ui-select-multi label="SelectManager mit String"
					:class="[selectManagerState.bgColor.value, selectManagerState.textColor.value, selectManagerState.iconColor.value, selectManagerState.borderColor.value]"
					:manager="stringSelectManager"
					v-bind="selectManagerState.props" />

				<ui-select-multi label="SelectManager mit Custom-Objekten"
					:class="[selectManagerState.bgColor.value, selectManagerState.textColor.value, selectManagerState.iconColor.value, selectManagerState.borderColor.value]"
					:manager="objectSelectManager"
					v-bind="selectManagerState.props" />

				<ui-select-multi label="CoreTypeSelectManager mit LehrerRechtsverhaeltnis"
					:class="[selectManagerState.bgColor.value, selectManagerState.textColor.value, selectManagerState.iconColor.value, selectManagerState.borderColor.value]"
					:manager="coreTypeSelectManager"
					v-bind="selectManagerState.props" />
			</svws-ui-input-wrapper>
		</Variant>

		<Variant title="Filter" id="filter">
			<svws-ui-input-wrapper>
				In diesem Beispiel werden zwei Filter an das Select übergeben. Jeder Filter hat 2 Fachgruppen, die ausgewählt werden können. Solange kein
				Filter gesetzt ist, werden alle Optionen angezeigt. Sobald ein Filter gesetzt wird, werden nur noch dazu passende Optionen zur Verfügung
				gestellt. Gesetzte Fachgruppen in einem Filter ergänzen sich dabei. Wird jedoch in beiden Filtern eine Fachgruppe gesetzt, dann werden nur
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
					Deutsch
				</svws-ui-checkbox>

				<svws-ui-checkbox v-model="filterState2.musikUndKunst2">
					Musik und Kunst
				</svws-ui-checkbox>

				<ui-select-multi label="CoreTypeSelectManager Fach abhängig von Fachgruppe"
					:class="[filterSelectState.bgColor.value, filterSelectState.textColor.value, filterSelectState.iconColor.value, filterSelectState.borderColor.value]"
					:manager="fachSelectManager"
					v-bind="filterSelectState.props" />
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

				<ui-select-multi label="Deep Search SelectManager"
					:class="[deepSearchState.bgColor.value, deepSearchState.textColor.value, deepSearchState.iconColor.value, deepSearchState.borderColor.value]"
					:manager="deepSearchSelectManager"
					:deep-search-attributes="['marke', 'color', 'baujahr']"
					v-bind="deepSearchState.props" />
			</svws-ui-input-wrapper>
		</Variant>

		<Variant title="Sortierung" id="sortierung">
			<svws-ui-input-wrapper>
				<ui-select-multi label="Sortiertes Select"
					:class="[sortState.bgColor.value, sortState.textColor.value, sortState.iconColor.value, sortState.borderColor.value]"
					:manager="sortableCoreTypeSelectManager"
					v-bind="sortState.props" />
			</svws-ui-input-wrapper>

			<template #controls>
				<HstCheckbox title="Searchable"
					v-model="sortState.searchable.value" />

				<HstCheckbox title="Required"
					v-model="sortState.required.value" />

				<HstCheckbox title="Disabled"
					v-model="sortState.disabled.value" />

				<HstCheckbox title="Statistik"
					v-model="sortState.statistics.value" />

				<HstCheckbox title="Removable"
					v-model="sortState.removable.value" />

				<HstCheckbox title="Readonly"
					v-model="sortState.readonly.value" />

				<HstCheckbox title="Headless"
					v-model="sortState.headless.value" />

				<HstNumber title="minOptions"
					v-model="sortState.minOptions.value" />

				<HstNumber title="maxOptions"
					v-model="sortState.maxOptions.value" />

				<HstRadio title="Sortierung"
					v-model="sortState.sort.value"
					:options="[
						{ label: 'ID', value: 'id' },
						{ label: 'Kürzel', value: 'kuerzel' },
						{ label: 'Text', value: 'text' },
					]" />

				<span class="text-headline-md">Farben</span>

				<HstRadio title="Hintergrund"
					v-model="sortState.bgColor.value"
					:options="[
						{ label: 'keine', value: '' },
						{ label: 'bg-ui-brand', value: 'bg-ui-brand' },
						{ label: 'bg-ui-success', value: 'bg-ui-success' },
						{ label: 'bg-ui-danger', value: 'bg-ui-danger' },
					]" />

				<HstRadio title="Text"
					v-model="sortState.textColor.value"
					:options="[
						{ label: 'keine', value: '' },
						{ label: 'text-ui-onbrand', value: 'text-ui-onbrand' },
						{ label: 'text-ui-onsuccess', value: 'text-ui-onsuccess' },
						{ label: 'text-ui-ondanger', value: 'text-ui-ondanger' },
					]" />

				<HstRadio title="Icon"
					v-model="sortState.iconColor.value"
					:options="[
						{ label: 'keine', value: '' },
						{ label: 'icon-ui-onbrand', value: 'icon-ui-onbrand' },
						{ label: 'icon-ui-onsuccess', value: 'icon-ui-onsuccess' },
						{ label: 'icon-ui-ondanger', value: 'icon-ui-ondanger' },
					]" />

				<HstRadio title="Border"
					v-model="sortState.borderColor.value"
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
					<ui-select-multi label="SelectManager mit Muss-Validator"
						:class="[validatorState.bgColor.value, validatorState.textColor.value, validatorState.iconColor.value, validatorState.borderColor.value]"
						v-model="mussValidatorSelection"
						:manager="sMussValidatorSelectManager"
						:validation="validateMuss"
						v-bind="validatorState.props" />

					<ui-select-multi label="SelectManager mit Kann-Validator"
						:class="[validatorState.bgColor.value, validatorState.textColor.value, validatorState.iconColor.value, validatorState.borderColor.value]"
						v-model="kannValidatorSelection"
						:manager="sKannValidatorSelectManager"
						:validation="validateKann"
						v-bind="validatorState.props" />

					<ui-select-multi label="SelectManager mit Hinweis-Validator"
						:class="[validatorState.bgColor.value, validatorState.textColor.value, validatorState.iconColor.value, validatorState.borderColor.value]"
						v-model="hinweisValidatorSelection"
						:manager="sHinweisValidatorSelectManager"
						:validation="validateHinweis"
						v-bind="validatorState.props" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</Variant>

		<template #controls>
			<HstCheckbox title="Searchable"
				v-model="activeState.searchable.value" />

			<HstCheckbox title="Required"
				v-model="activeState.required.value" />

			<HstCheckbox title="Disabled"
				v-model="activeState.disabled.value" />

			<HstCheckbox title="Statistik"
				v-model="activeState.statistics.value" />

			<HstCheckbox title="Removable"
				v-model="activeState.removable.value" />

			<HstCheckbox title="Readonly"
				v-model="activeState.readonly.value" />

			<HstCheckbox title="Headless"
				v-model="activeState.headless.value" />

			<HstNumber title="minOptions"
				v-model="activeState.minOptions.value" />

			<HstNumber title="maxOptions"
				v-model="activeState.maxOptions.value" />

			<span class="text-headline-md">Farben</span>

			<HstRadio title="Hintergrund"
				v-model="activeState.bgColor.value"
				:options="[
					{ label: 'keine', value: '' },
					{ label: 'bg-ui-brand', value: 'bg-ui-brand' },
					{ label: 'bg-ui-success', value: 'bg-ui-success' },
					{ label: 'bg-ui-danger', value: 'bg-ui-danger' },
				]" />

			<HstRadio title="Text"
				v-model="activeState.textColor.value"
				:options="[
					{ label: 'keine', value: '' },
					{ label: 'text-ui-onbrand', value: 'text-ui-onbrand' },
					{ label: 'text-ui-onsuccess', value: 'text-ui-onsuccess' },
					{ label: 'text-ui-ondanger', value: 'text-ui-ondanger' },
				]" />

			<HstRadio title="Icon"
				v-model="activeState.iconColor.value"
				:options="[
					{ label: 'keine', value: '' },
					{ label: 'icon-ui-onbrand', value: 'icon-ui-onbrand' },
					{ label: 'icon-ui-onsuccess', value: 'icon-ui-onsuccess' },
					{ label: 'icon-ui-ondanger', value: 'icon-ui-ondanger' },
				]" />

			<HstRadio title="Border"
				v-model="activeState.borderColor.value"
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

	import { computed, reactive, ref, type Ref } from "vue";
	import storyManager from "../../../stories/StoryManager";
	import { FachSelectFilter } from "./filter/FachSelectFilter";
	import { Fach } from "../../../../../core/src/asd/types/fach/Fach";
	import { Fachgruppe } from "../../../../../core/src/asd/types/fach/Fachgruppe";
	import { LehrerRechtsverhaeltnis } from "../../../../../core/src/asd/types/lehrer/LehrerRechtsverhaeltnis";
	import { Schulform } from "../../../../../core/src/asd/types/schule/Schulform";
	import { CoreTypeSelectManager } from "./manager/CoreTypeSelectManager";
	import { SelectManager } from "./manager/SelectManager";
	import type { LehrerRechtsverhaeltnisKatalogEintrag } from "../../../../../core/src/asd/data/lehrer/LehrerRechtsverhaeltnisKatalogEintrag";
	import Docs from "./UiSelectMulti.story.md";
	import { BasicValidator } from "../../../../../core/src/asd/validate/BasicValidator";
	import { ValidatorFehlerart } from "../../../../../core/src/asd/validate/ValidatorFehlerart";
	import type { ValidatorFehler } from "../../../../../core/src/asd/validate/ValidatorFehler";
	import type { List } from "../../../../../core/src/java/util/List";

	type Sortierung = "id" | "kuerzel" | "text";

	type State = {
		searchable?: boolean;
		disabled?: boolean;
		statistics?: boolean;
		removable?: boolean;
		readonly?: boolean;
		required?: boolean;
		headless?: boolean;
		minOptions?: number;
		maxOptions?: number;
		bgColor?: string;
		textColor?: string;
		iconColor?: string;
		borderColor?: string;
		sort?: Sortierung;
	};

	class VariantState {

		public searchable: Ref<boolean> = ref(true);
		public disabled: Ref<boolean> = ref(false);
		public statistics: Ref<boolean> = ref(false);
		public removable: Ref<boolean> = ref(true);
		public readonly: Ref<boolean> = ref(false);
		public required: Ref<boolean> = ref(false);
		public headless: Ref<boolean> = ref(false);

		public minOptions: Ref<number | undefined> = ref();
		public maxOptions: Ref<number | undefined> = ref();

		public bgColor: Ref<string> = ref("");
		public textColor: Ref<string> = ref("");
		public iconColor: Ref<string> = ref("");
		public borderColor: Ref<string> = ref("");

		public sort: Ref<Sortierung> = ref("id");

		public props = reactive({
			searchable: this.searchable,
			removable: this.removable,
			disabled: this.disabled,
			statistics: this.statistics,
			headless: this.headless,
			readonly: this.readonly,
			required: this.required,
			minOptions: this.minOptions,
			maxOptions: this.maxOptions,
		});

		constructor(state: State = {}) {
			this.searchable.value = state.searchable ?? this.searchable.value;
			this.disabled.value = state.disabled ?? this.disabled.value;
			this.statistics.value = state.statistics ?? this.statistics.value;
			this.removable.value = state.removable ?? this.removable.value;
			this.readonly.value = state.readonly ?? this.readonly.value;
			this.required.value = state.required ?? this.required.value;
			this.headless.value = state.headless ?? this.headless.value;

			this.minOptions.value = state.minOptions;
			this.maxOptions.value = state.maxOptions;

			this.bgColor.value = state.bgColor ?? this.bgColor.value;
			this.textColor.value = state.textColor ?? this.textColor.value;
			this.iconColor.value = state.iconColor ?? this.iconColor.value;
			this.borderColor.value = state.borderColor ?? this.borderColor.value;

			this.sort.value = state.sort ?? this.sort.value;
		}
	}

	const selectManagerState = new VariantState();
	const filterSelectState = new VariantState();
	const deepSearchState = new VariantState();
	const sortState = new VariantState();
	const validatorState = new VariantState();

	const variantControlsMap = new Map<string, VariantState>([
		["selectManager", selectManagerState],
		["filter", filterSelectState],
		["search", deepSearchState],
		["sortierung", sortState],
		["validatoren", validatorState],
	]);

	const activeState = computed(() =>
		variantControlsMap.get(storyManager.variant.id) ?? selectManagerState
	);

	const filterState1 = reactive({
		fremdsprache: false,
		musikUndKunst1: false,
	});

	const filterState2 = reactive({
		deutsch: false,
		musikUndKunst2: false,
	});

	const fruitItems: string[] = [
		"Ananas",
		"Aprikose",
		"Banane",
		"Birne",
		"Apfelsine",
		"Brombeere",
		"Clementine",
		"Granatapfel",
		"Himbeere",
		"Ich will gleich den ganzen Obstkorb haben und am liebsten alles doppelt und dreifach, nur damit dieses Item einen langen Text zur Vorschau hat.",
		"Kirsche",
		"Kiwi",
		"Lemon",
		"Litschi",
		"Melone",
		"Orange",
		"Papaya",
		"Pfirsich",
		"Pflaume",
		"Rote Johannisbeere",
		"Zitronenmelisse",
	];

	const carItems: { marke: string, color: string, baujahr: number }[] = [
		{ marke: "BMW", color: "blue", baujahr: 2006 },
		{ marke: "Audi", color: "red", baujahr: 2008 },
		{ marke: "Opel", color: "schwarz", baujahr: 2006 },
	];

	const stringSelectManager = new SelectManager({
		options: fruitItems,
	});

	const coreTypeSelectManager = new CoreTypeSelectManager({
		clazz: LehrerRechtsverhaeltnis.class,
		schuljahr: 2018,
		schulformen: Schulform.GY,
		selectionDisplayText: "text",
		optionDisplayText: "kuerzelText",
	});

	const objectSelectManager = new SelectManager({
		options: carItems,
		selectionDisplayText: (option: { marke: string, color: string }): string => option.marke,
		optionDisplayText: (option: { marke: string, color: string }): string => `${option.marke} - ${option.color}`,
	});

	const deepSearchSelectManager = new SelectManager({
		options: carItems,
		selectionDisplayText: (option: { marke: string, color: string, baujahr: number }): string => option.marke,
		optionDisplayText: (option: { marke: string, color: string, baujahr: number }): string => `${option.marke} - ${option.color}`,
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

	const filters = ref([
		new FachSelectFilter("fachgruppe1", filter1),
		new FachSelectFilter("fachgruppe2", filter2),
	]);

	const fachSelectManager = new CoreTypeSelectManager({
		clazz: Fach.class,
		schuljahr: 2020,
		schulformen: Schulform.GY,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "text",
		filters: filters,
	});

	const sortById = (
		a: LehrerRechtsverhaeltnisKatalogEintrag,
		b: LehrerRechtsverhaeltnisKatalogEintrag
	): number => {
		if (a.id < b.id) {
			return -1;
		}

		if (a.id > b.id) {
			return 1;
		}

		return 0;
	};

	const sortByKuerzel = (
		a: LehrerRechtsverhaeltnisKatalogEintrag,
		b: LehrerRechtsverhaeltnisKatalogEintrag
	): number => {
		if (a.kuerzel < b.kuerzel) {
			return -1;
		}

		if (a.kuerzel > b.kuerzel) {
			return 1;
		}

		return 0;
	};

	const sortByText = (
		a: LehrerRechtsverhaeltnisKatalogEintrag,
		b: LehrerRechtsverhaeltnisKatalogEintrag
	): number => {
		if (a.text < b.text) {
			return -1;
		}

		if (a.text > b.text) {
			return 1;
		}

		return 0;
	};

	const computedSort = computed(() => {
		switch (sortState.sort.value) {
			case "kuerzel":
				return sortByKuerzel;
			case "text":
				return sortByText;
			default:
				return sortById;
		}
	});

	const sortableCoreTypeSelectManager = new CoreTypeSelectManager({
		clazz: LehrerRechtsverhaeltnis.class,
		schuljahr: 2018,
		schulformen: Schulform.GY,
		optionDisplayText: (a): string => `${a.id} - ${a.kuerzel} - ${a.text}`,
		selectionDisplayText: "text",
		sort: computedSort,
	});

	/**
	 * Validatoren
	 */

	const hinweisValidatorSelection = ref<string[]>([]);
	const kannValidatorSelection = ref<string[]>([]);
	const mussValidatorSelection = ref<string[]>([]);

	const sHinweisValidatorSelectManager = new SelectManager({
		options: ["Christian", "Anna"],
	});

	const sKannValidatorSelectManager = new SelectManager({
		options: ["20 Pflichtstunden", "40 Pflichtstunden"],
	});

	const sMussValidatorSelectManager = new SelectManager({
		options: ["Müller", "Meier"],
	});

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

			return result === null;
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

	const validatorHinweis = computed(() =>
		new ValidatorTest(
			() => hinweisValidatorSelection.value.includes("Anna")
				? null
				: "Hier ist die Eintragung von Anna gewünscht.",
			ValidatorFehlerart.HINWEIS
		)
	);

	const validatorKann = computed(() =>
		new ValidatorTest(
			() => kannValidatorSelection.value.includes("20 Pflichtstunden")
				? null
				: "Der Pflichstundensoll sollte eingetragen werden. Ein zu hoher Wert ist nicht plausibel.",
			ValidatorFehlerart.KANN
		)
	);

	const validatorMuss = computed(() =>
		new ValidatorTest(
			() => mussValidatorSelection.value.includes("Müller")
				? null
				: "In diesem Feld ist es verpflichtend, dass Müller ausgewählt wird. Alles andere führt zu einem Fehler.",
			ValidatorFehlerart.MUSS
		)
	);

	function getSourceString(): string {
		const currentState = activeState.value;

		return `<ui-select-multi
		label="..."
		:manager="..."
		${currentState.searchable.value ? "" : ':searchable="false"'}
		${currentState.removable.value ? "" : ':removable="false"'}
		${currentState.disabled.value ? "disabled" : ""}
		${currentState.statistics.value ? "statistics" : ""}
		${currentState.required.value ? "required" : ""}
		${currentState.headless.value ? "headless" : ""}
		${currentState.minOptions.value === undefined ? "" : `:min-options="${currentState.minOptions.value}"`}
		${currentState.maxOptions.value === undefined ? "" : `:max-options="${currentState.maxOptions.value}"`}
		${currentState === sortState ? ':sort="(a, b) => ..."' : ""}
		${currentState === validatorState ? ':validation="() => modelProxy.getFehler(\'feldname\')"' : ""}
	`.split("\n").filter(line => line.trim() !== "").join("\n");
	}

</script>
