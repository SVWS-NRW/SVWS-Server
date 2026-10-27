<template>
	<Story title="Select Multi New" id="ui-select-multi" icon="ri:expand-up-down-line" auto-props-disabled :layout="{ type: 'grid', width: '45%'}"
		:source="getSourceString()">
		<template #docs>
			<Docs />
		</template>

		<Variant title="SelectManager" id="selectManager">
			<svws-ui-input-wrapper>
				<ui-select-multi label="SelectManager mit String"
					:manager="stringSelectManager"
					v-bind="selectManagerState.props" />

				<ui-select-multi label="SelectManager mit Custom-Objekten"
					:manager="objectSelectManager"
					v-bind="selectManagerState.props" />

				<ui-select-multi label="CoreTypeSelectManager mit LehrerRechtsverhaeltnis"
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
					:manager="fachSelectManager"
					v-bind="filterSelectState.props" />
			</svws-ui-input-wrapper>
		</Variant>

		<Variant title="Deep Search" id="search">
			<svws-ui-input-wrapper>
				Das folgende Select verwendet Deep Search und lässt auch die Suche nach Attributen zu, die nicht im Optiontext dargestellt werden, zum Beispiel das Baujahr.

				<ui-select-multi label="Deep Search SelectManager"
					:manager="deepSearchSelectManager"
					:deep-search-attributes="['marke', 'color', 'baujahr']"
					v-bind="deepSearchState.props" />
			</svws-ui-input-wrapper>
		</Variant>

		<Variant title="Validatoren" id="validatoren">
			<svws-ui-content-card class="p-5">
				<svws-ui-input-wrapper>
					<ui-select-multi label="SelectManager mit Muss-Validator"
						v-model="mussValidatorSelection"
						:manager="sMussValidatorSelectManager"
						:validation="validateMuss"
						v-bind="validatorState.props" />

					<ui-select-multi label="SelectManager mit Kann-Validator"
						v-model="kannValidatorSelection"
						:manager="sKannValidatorSelectManager"
						:validation="validateKann"
						v-bind="validatorState.props" />

					<ui-select-multi label="SelectManager mit Hinweis-Validator"
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

			<HstRadio title="Sortierung"
				v-model="activeState.sort.value"
				:options="[
					{ label: 'Prop ID', value: 'idProp' },
					{ label: 'Prop Sortierung', value: 'sortierungProp' },
					{ label: 'Alphabetisch', value: 'alphabetical' },
					{ label: 'Unsortiert', value: 'unsorted' },
					{ label: 'Baujahr (Autos)', value: 'baujahr' },
					{ label: 'Undefined/Null', value: 'undefined' },
				]" />
		</template>
	</Story>
</template>

<script setup lang="ts">

	import { computed, reactive, type Ref, ref } from "vue";

	import { Fach } from "@core/asd/types/fach/Fach";
	import { Fachgruppe } from "@core/asd/types/fach/Fachgruppe";
	import { LehrerRechtsverhaeltnis } from "@core/asd/types/lehrer/LehrerRechtsverhaeltnis";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { BasicValidator } from "@core/asd/validate/BasicValidator";
	import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
	import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
	import type { List } from "@core/java/util/List";
	import storyManager from "@ui/stories/StoryManager";

	import { FachSelectFilter } from "./filter/FachSelectFilter";
	import { CoreTypeSelectManager } from "./manager/CoreTypeSelectManager";
	import { SelectManager } from "./manager/SelectManager";
	import Docs from "./UiSelectMulti.story.md";

	type Sortierung = "idProp" | "sortierungProp" | "unsorted" | "undefined" | "baujahr" | "alphabetical";

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
		public sort: Ref<Sortierung> = ref("unsorted");

		public minOptions: Ref<number | undefined> = ref();
		public maxOptions: Ref<number | undefined> = ref();



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
			this.sort.value = state.sort ?? this.sort.value;

			this.minOptions.value = state.minOptions;
			this.maxOptions.value = state.maxOptions;

		}
	}

	const selectManagerState = new VariantState();
	const filterSelectState = new VariantState();
	const deepSearchState = new VariantState();
	const validatorState = new VariantState();

	const variantControlsMap = new Map<string, VariantState>([
		["selectManager", selectManagerState],
		["filter", filterSelectState],
		["search", deepSearchState],
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

	const sortByBaujahr = (a: Car, b: Car): number => {
		if (a.baujahr < b.baujahr) {
			return -1;
		}

		if (a.baujahr > b.baujahr) {
			return 1;
		}

		return 0;
	};

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

	type Car = { id: number; marke: string; color: string; sortierung: number; baujahr: number;	};

	const carItems: Car[] = [
		{ id: 104, marke: "BMW", color: "blue", baujahr: 2006, sortierung: 70 },
		{ id: 211, marke: "Audi", color: "red", baujahr: 2008, sortierung: 20 },
		{ id: 307, marke: "Opel", color: "schwarz", baujahr: 2009, sortierung: 90 },
		{ id: 18, marke: "Mercedes", color: "silber", baujahr: 2015, sortierung: 40 },
		{ id: 502, marke: "Volkswagen", color: "weiss", baujahr: 2010, sortierung: 10 },
		{ id: 73, marke: "Ford", color: "grau", baujahr: 2004, sortierung: 60 },
		{ id: 160, marke: "Porsche", color: "schwarz", baujahr: 2020, sortierung: 30 },
		{ id: 41, marke: "Skoda", color: "grün", baujahr: 2012, sortierung: 100 },
		{ id: 390, marke: "Toyota", color: "weiss", baujahr: 2007, sortierung: 50 },
		{ id: 8, marke: "Honda", color: "rot", baujahr: 2018, sortierung: 80 },
		{ id: 129, marke: "Renault", color: "blau", baujahr: 2003, sortierung: 15 },
		{ id: 276, marke: "Peugeot", color: "silber", baujahr: 2014, sortierung: 110 },
		{ id: 95, marke: "Seat", color: "schwarz", baujahr: 2005, sortierung: 5 },
		{ id: 341, marke: "Volvo", color: "grau", baujahr: 2011, sortierung: 35 },
		{ id: 227, marke: "Fiat", color: "gelb", baujahr: 2017, sortierung: 25 },
	];

	const carSort = computed(() => {
		if (activeState.value.sort.value === "undefined") {
			return undefined;
		}
		if (activeState.value.sort.value === "baujahr") {
			return sortByBaujahr;
		}

		return activeState.value.sort.value;
	});

	const simpleSort = computed(() => {
		if ((activeState.value.sort.value === "undefined") || (activeState.value.sort.value === "baujahr")) {
			return undefined;
		}

		return activeState.value.sort.value;
	});

	const stringSelectManager = new SelectManager({
		options: fruitItems,
		sort: simpleSort,
	});

	const coreTypeSelectManager = new CoreTypeSelectManager({
		clazz: LehrerRechtsverhaeltnis.class,
		schuljahr: 2018,
		schulformen: Schulform.GY,
		selectionDisplayText: "text",
		optionDisplayText: "kuerzelText",
		sort: simpleSort,
	});

	const objectSelectManager = new SelectManager({
		options: carItems,
		selectionDisplayText: (option: Car): string => option.marke,
		optionDisplayText: (option: Car): string => `ID: ${option.id} - Sortierung: ${option.sortierung} - ${option.marke} - ${option.color} - ${option.baujahr}`,
		sort: carSort,
	});

	const deepSearchSelectManager = new SelectManager({
		options: carItems,
		selectionDisplayText: (option: Car): string => option.marke,
		optionDisplayText: (option: Car): string => `${option.marke} - ${option.color}`,
		sort: carSort,
	});

	const fachSelectManager = new CoreTypeSelectManager({
		clazz: Fach.class,
		schuljahr: 2020,
		schulformen: Schulform.GY,
		optionDisplayText: "kuerzelText",
		selectionDisplayText: "text",
		filters: filters,
		sort: simpleSort,
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
		let sortText;
		switch (currentState.sort.value) {
			case "undefined":
			case "sortierungProp":
				sortText = "";
				break;

			case "baujahr":
				sortText = ':sort="(a, b) => a.baujahr - b.baujahr..."';
				break;

			default:
				sortText = `sort="${currentState.sort.value}"`;
				break;
		}

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
		${sortText}
		${currentState === validatorState ? ':validation="() => modelProxy.getFehler(\'feldname\')"' : ""}
	`.split("\n").filter(line => line.trim() !== "").join("\n");
	}

</script>
