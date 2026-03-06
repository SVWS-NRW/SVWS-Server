<template>
	<Story title="Text Input" id="svws-ui-text-input" icon="ri:pencil-line" :layout="{type: 'grid', width: '45%'}" :source="sourceCode">
		<Variant title="Default" id="Default">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="defaultState.modelValue.value" v-bind="defaultState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Disabled" id="Disabled">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="disabledState.modelValue.value" v-bind="disabledState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Zeichenlänge" id="Zeichenlänge">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="lengthState.modelValue.value" v-bind="lengthState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Statistik" id="Statistik">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="statisticState.modelValue.value" v-bind="statisticState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Headless" id="Headless">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="headlessState.modelValue.value" v-bind="headlessState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Readonly" id="Readonly">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="readonlyState.modelValue.value" v-bind="readonlyState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Required" id="Required">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="requiredState.modelValue.value" v-bind="requiredState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Email" id="Email">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="emailState.modelValue.value" v-bind="emailState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Telefon" id="Telefon">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="telState.modelValue.value" v-bind="telState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Date" id="Date">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="dateState.modelValue.value" v-bind="dateState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="DateTime" id="DateTime">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="dateTimeState.modelValue.value" v-bind="dateTimeState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Search" id="Search">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="searchState.modelValue.value" v-bind="searchState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="URL" id="URL">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="urlState.modelValue.value" v-bind="urlState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Validation" id="Validation">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-text-input v-model="validationState.modelValue.value" v-bind="validationState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<template #controls>
			<div class="flex">
				<HstText title="placeholder" v-model="activeState.placeholder.value" class="flex-1" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Der Titel des Inputs. Dieser wird bei <code class="bg-ui-selected">headless = false</code> immer angezeigt.
						Bei <code class="bg-ui-selected">headless = true</code> nur, wenn kein Wert vorhanden ist und kein Fokus auf dem Feld liegt. <br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">placeholder: ""</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<ui-select label="type" v-model="activeState.type.value" :manager="typeSelectManager" :removable="false" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Der Typ des Inputs. Grundsätzlich können alle Typen aus <code class="bg-ui-selected">InputTypeHTMLAttribute</code> mitgegeben werden,
						die dann an das intern verwendete Input weitergegeben werden. Tatsächlich umgesetzte und relevant types sind aber:
						<code class="bg-ui-selected">text</code>, <code class="bg-ui-selected">email</code>, <code class="bg-ui-selected">date</code>,
						<code class="bg-ui-selected">datetime-local</code>, <code class="bg-ui-selected">search</code> und
						<code class="bg-ui-selected">tel</code>.<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">type: "text"</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstCheckbox title="statistics"
					v-model="activeState.statistics.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Gibt an, ob ein Statistik-Icon gerendert werden soll.
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">statistics: false</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstCheckbox title="disabled"
					v-model="activeState.disabled.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Gibt an, ob das Input disabled gerendert werden soll. Dabei ist es ausgegraut und nicht editierbar.<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">disabled: false</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstCheckbox title="required"
					v-model="activeState.required.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Gibt an, ob das Input einen Wert enthalten muss. Falls ja wird automatisch ein Validator hinzugefügt, der dies prüft und ggf. Fehler anzeigt.
						Das automatische Hinzufügen des Validators kann mit der prop <code class="bg-ui-selected">skipDefaultValidation</code> unterbunden werden. <br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">required: false</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstCheckbox title="readonly"
					v-model="activeState.readonly.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Gibt an, ob das Input readonly ist. Dadurch ist es nicht editierbar und erhält ein Schloss-Icon, um dies zu visualisieren. Es wird aber nicht ausgegraut wie bei <code class="bg-ui-selected">disabled</code>.<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">readonly: false</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstCheckbox title="removable"
					v-model="activeState.removable.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Gibt an, ob das Input mit einem Button geleert werden kann. Nur relevant für die Typen <code class="bg-ui-selected">date</code> und
						<code class="bg-ui-selected">datetime-local</code>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">removable: false</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstCheckbox title="headless"
					v-model="activeState.headless.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Gibt an, ob das Input headless gerendert wird. Falls ja, wird die Komponente ohne Rahmen und bei vorhandenem Wert ohne Label angezeigt.
						Die ist so nutzbar für zum Beispiel Tabellenzellen.<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">headless: false</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstCheckbox title="focus"
					v-model="activeState.focus.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Gibt an, ob das Input beim Betreten der Seite automatisch im Fokus sein soll.<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">focus: false</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstCheckbox title="url"
					v-model="activeState.url.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Gibt an, ob ein URL prefix im Input angezeigt werden soll. Nur sinnvoll für den Type <code class="bg-ui-selected">text</code><br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">url: false</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex items-start gap-2 text-headline-sm">
				<HstRadio title="span"
					v-model="activeState.span.value" :options="[
						{ label: 'undefined', value: 'undefined' },
						{ label: 'full', value: 'full' },
						{ label: '2', value: '2' },
					]" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Setzt die Klasse <code class="bg-ui-selected">col-span-2</code> bei "2" bzw. <code class="bg-ui-selected">col-span-full</code> bei
						"full". Dadurch können Inputs eine automatisch passende Breite in Zum Beispiel Grids oder dem InputWrapper erhalten.<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">span: undefined</code>
					</template>
				</svws-ui-tooltip>
			</div>

			<div class="flex">
				<HstNumber title="minLen"
					v-model="activeState.minLen.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Setzt die Untergrenze für die Zeichenlänge des Inputs. Beim setzen dieser prop wird automatisch ein Validator hinzugefügt, der die
						Plausibilität des Wertes prüft. Dieses automatische Hinzufügen des Validators kann mit der prop
						<code class="bg-ui-selected">skipDefaultValidation</code> unterbunden werden. <br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">minLen: undefined</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstNumber title="maxLen"
					v-model="activeState.maxLen.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Setzt die Obergrenze für die Zeichenlänge des Inputs. Beim setzen dieser prop wird automatisch ein Validator hinzugefügt, der die
						Plausibilität des Wertes prüft. Dieses automatische Hinzufügen des Validators kann mit der prop
						<code class="bg-ui-selected">skipDefaultValidation</code> unterbunden werden. <br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">maxLen: undefined</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<div>
					<svws-ui-text-input placeholder="MinDate" v-model="activeState.minDate.value" type="date" removable />
				</div>
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Setzt das Mindestdatum, welches das Inputfeld haben muss. Wirkt nur bei <code class="bg-ui-selected">type: date</code>
						oder <code class="bg-ui-selected">type: datetime-local</code>.<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">minDate: undefined</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<div>
					<svws-ui-text-input placeholder="MaxDate" v-model="activeState.maxDate.value" type="date" removable />
				</div>
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Setzt das Maximaldatum, welches das Inputfeld haben darf. Wirkt nur bei <code class="bg-ui-selected">type: date</code>
						oder <code class="bg-ui-selected">type: datetime-local</code>.<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">maxDate: undefined</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="text-headline-sm">
				validation: Validatorfehler mit Härtegrad
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line inline" />
					<template #content>
						Erstellt eine Liste mit ValidatorFehlern der entsprechenden Härtegrade.
						Diese wird dann per <code class="bg-ui-selected">() => validatorFehler</code> an die prop
						<code class="bg-ui-selected">validation</code> übergeben. <br>
						<span class="font-bold">Default:</span> <code class="bg-ui-selected">validation: leere ArrayList&lt;ValidatorFehler&gt;</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex items-start gap-2">
				<HstCheckbox title="Muss"
					v-model="activeState.muss.value" />
				<HstCheckbox title="Kann"
					v-model="activeState.kann.value" />
				<HstCheckbox title="Hinweis"
					v-model="activeState.hinweis.value" />
			</div>

			<div class="flex items-start gap-2">
				<div class="text-headline-sm">
					skipDefaultValidation
				</div>

				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Diese prop kann die Generierung von allen oder von einzelnen Defaultvalidatoren überspringen.
						Defaultvalidatoren werden automatisch gesetzt, wenn <code class="bg-ui-selected">required</code>,
						<code class="bg-ui-selected">minLen</code>, <code class="bg-ui-selected">maxLen</code>, <code class="bg-ui-selected">minDate</code>,
						<code class="bg-ui-selected">maxDate</code> oder <code class="bg-ui-selected">type: email</code> gesetzt ist. Soll kein
						Defaultvalidator gesetzt werden, um zum Beispiel eigene Validatoren	über die prop <code class="bg-ui-selected">validation</code> zu
						definieren oder die Validierung ganz abzustellen, muss diese prop auf <code class="bg-ui-selected">true</code> gesetzt werden. Um
						einzelne abzuschalten muss ein Objekt mit der entsprechenden Konfiguration gesetzt werden. <br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">skipDefaultValidation: false</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstCheckbox title="required"
					v-model="activeState.skipDefaultValidation.value.required" />
				<HstCheckbox title="length"
					v-model="activeState.skipDefaultValidation.value.length" />
				<HstCheckbox title="email"
					v-model="activeState.skipDefaultValidation.value.email" />
				<HstCheckbox title="date"
					v-model="activeState.skipDefaultValidation.value.dateRange" />
			</div>
		</template>
	</Story>
</template>

<script setup lang="ts">

	import { computed, reactive, ref, type ComputedRef, type Ref, type InputTypeHTMLAttribute } from "vue";
	import storyManager from '../../stories/StoryManager';
	import { logEvent } from '../../stories/helper';
	import type { List } from '../../../../core/src/java/util/List';
	import type { ValidatorFehler } from '../../../../core/src/asd/validate/ValidatorFehler';
	import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
	import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";
	import { SelectManager } from "./select/manager/SelectManager";

	const activeState = computed(() => variantControlsMap.get(storyManager.variant.id) ?? defaultState);

	type State = {
		modelValue?: string | null;
		type?: InputTypeHTMLAttribute;
		placeholder?: string;
		minDate?: string;
		maxDate?: string;
		statistics?: boolean;
		disabled?: boolean;
		required?: boolean;
		readonly?: boolean;
		headless?: boolean;
		focus?: boolean;
		url?: boolean;
		maxLen?: number;
		minLen?: number;
		span?: "full" | "2" | undefined;
		removable?: boolean;
		muss?: boolean;
		kann?: boolean;
		hinweis?: boolean;
		skipDefaultValidation?: { required: boolean, length: boolean, email: boolean, dateRange: boolean };
	};

	class VariantState {

		public modelValue: Ref<string | null> = ref(null);
		public type = ref("text");
		public placeholder = ref("Titel");
		public minDate: Ref<string | undefined> = ref();
		public maxDate: Ref<string | undefined> = ref();
		public maxLen: Ref<number | undefined> = ref();
		public minLen: Ref<number | undefined> = ref();
		public statistics = ref(false);
		public removable = ref(false);
		public disabled = ref(false);
		public required = ref(false);
		public readonly = ref(false);
		public headless = ref(false);
		public focus = ref(false);
		public url = ref(false);
		public span = ref<"full" | "2" | "undefined">("2");
		public muss = ref(false);
		public kann = ref(false);
		public hinweis = ref(false);
		public skipDefaultValidation = ref({ required: false, length: false, email: false, dateRange: false });

		public validatorMuss = new ValidatorTest(() => (this.modelValue.value === "Test") ? null : "Hier ist die Eintragung von 'Test' gewünscht", ValidatorFehlerart.MUSS);
		public validatorKann = new ValidatorTest(() => (this.modelValue.value === "Test") ? null : "Hier ist die Eintragung von 'Test' gewünscht", ValidatorFehlerart.KANN);
		public validatorHinweis = new ValidatorTest(() => (this.modelValue.value === "Test") ? null : "Hier ist die Eintragung von 'Test' gewünscht", ValidatorFehlerart.HINWEIS);


		public validation: ComputedRef<() => List<ValidatorFehler>> = computed(() => {
			const validatorFehler = new ArrayList<ValidatorFehler>();

			if (this.muss.value) {
				this.validatorMuss.run();
				validatorFehler.addAll((this.validatorMuss.getFehler()));
			}
			if (this.kann.value) {
				this.validatorKann.run();
				validatorFehler.addAll((this.validatorKann.getFehler()));
			}
			if (this.hinweis.value) {
				this.validatorHinweis.run();
				validatorFehler.addAll((this.validatorHinweis.getFehler()));
			}

			return () => validatorFehler;
		});

		private spanValue = computed(() => {
			if (this.span.value === "undefined") {
				return undefined;
			} else {
				return this.span.value;
			}
		});

		public props = reactive({
			type: this.type,
			placeholder: this.placeholder,
			statistics: this.statistics,
			disabled: this.disabled,
			required: this.required,
			readonly: this.readonly,
			removable: this.removable,
			headless: this.headless,
			focus: this.focus,
			span: this.spanValue,
			minLen: this.minLen,
			maxLen: this.maxLen,
			minDate: this.minDate,
			maxDate: this.maxDate,
			url: this.url,
			validation: this.validation,
			skipDefaultValidation: this.skipDefaultValidation,
		});

		constructor(state: State) {
			this.modelValue.value = state.modelValue ?? this.modelValue.value;
			this.type.value = state.type ?? this.type.value;
			this.placeholder.value = state.placeholder ?? this.placeholder.value;
			this.statistics.value = state.statistics ?? this.statistics.value;
			this.disabled.value = state.disabled ?? this.disabled.value;
			this.required.value = state.required ?? this.required.value;
			this.readonly.value = state.readonly ?? this.readonly.value;
			this.headless.value = state.headless ?? this.headless.value;
			this.focus.value = state.focus ?? this.focus.value;
			this.span.value = state.span ?? this.span.value;
			this.minDate.value = state.minDate;
			this.maxDate.value = state.maxDate;
			this.minLen.value = state.minLen;
			this.maxLen.value = state.maxLen;
			this.maxDate.value = state.maxDate;
			this.url.value = state.url ?? this.url.value;
			this.muss.value = state.muss ?? this.muss.value;
			this.kann.value = state.kann ?? this.kann.value;
			this.hinweis.value = state.hinweis ?? this.hinweis.value;
			this.skipDefaultValidation.value = state.skipDefaultValidation ?? this.skipDefaultValidation.value;
		}
	}

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

	const defaultState = new VariantState({ focus: true });
	const disabledState = new VariantState({ disabled: true });
	const lengthState = new VariantState({ minLen: 2, maxLen: 5 });
	const statisticState = new VariantState({ statistics: true });
	const headlessState = new VariantState({ headless: true });
	const readonlyState = new VariantState({ readonly: true });
	const requiredState = new VariantState({ required: true });
	const dateState = new VariantState({ type: "date" });
	const emailState = new VariantState({ type: "email" });
	const searchState = new VariantState({ type: "search" });
	const telState = new VariantState({ type: "tel" });
	const dateTimeState = new VariantState({ type: "datetime-local" });
	const urlState = new VariantState({ type: "text", url: true, placeholder: "my-website.de" });
	const validationState = new VariantState({ kann: true, minLen: 2, skipDefaultValidation: { required: false, dateRange: true, email: false, length: true } });

	const variantControlsMap = new Map<string, VariantState>();
	variantControlsMap.set('Default', defaultState);
	variantControlsMap.set('Disabled', disabledState);
	variantControlsMap.set('Zeichenlänge', lengthState);
	variantControlsMap.set('Statistik', statisticState);
	variantControlsMap.set('Headless', headlessState);
	variantControlsMap.set('Readonly', readonlyState);
	variantControlsMap.set('Required', requiredState);
	variantControlsMap.set('Date', dateState);
	variantControlsMap.set('DateTime', dateTimeState);
	variantControlsMap.set('Email', emailState);
	variantControlsMap.set('Telefon', telState);
	variantControlsMap.set('Search', searchState);
	variantControlsMap.set('Validation', validationState);
	variantControlsMap.set('URL', urlState);

	const inputTypes = ['text', 'email', 'date', 'datetime-local', 'search', 'tel'];
	const typeSelectManager = new SelectManager({ options: inputTypes });

	const sourceCode = computed(() => {
		const indent = "\t";
		const lines = [
			`v-model="inputValueRef"`,
			activeState.value.placeholder.value === "" ? "" : `placeholder="${activeState.value.placeholder.value}"`,
			activeState.value.type.value === "text" ? "" : `type="${activeState.value.type.value}"`,
			activeState.value.statistics.value ? `statistics` : "",
			activeState.value.disabled.value ? `disabled` : "",
			activeState.value.required.value ? `required` : "",
			activeState.value.readonly.value ? `readonly` : "",
			activeState.value.removable.value ? `removable` : "",
			activeState.value.headless.value ? `headless` : "",
			activeState.value.focus.value ? `focus` : "",
			activeState.value.url.value ? "url" : "",
			(activeState.value.span.value === "undefined") ? "" : `span="${activeState.value.span.value}"`,
			(activeState.value.minLen.value === undefined) ? "" : `:minLen="${activeState.value.minLen.value}"`,
			(activeState.value.maxLen.value === undefined) ? "" : `:maxLen="${activeState.value.maxLen.value}"`,
			(activeState.value.minDate.value === undefined) ? "" : `minDate="${activeState.value.minDate.value}"`,
			(activeState.value.maxDate.value === undefined) ? "" : `maxDate="${activeState.value.maxDate.value}"`,
			(activeState.value.muss.value || activeState.value.kann.value || activeState.value.hinweis.value) ? `:validation="() => getFehler()"` : "",
			skipValidationString.value,
		].filter(Boolean).map(l => indent + l).join("\n");
		return `<svws-ui-text-input
${lines} />`;
	});

	const skipValidationString = computed(() => {
		const v = activeState.value.skipDefaultValidation.value;

		if (v.required && v.dateRange && v.email && v.length) {
			return ':skipDefaultValidation="true"';
		}

		if (!v.required && !v.dateRange && !v.email && !v.length) {
			return "";
		}

		const requiredString = activeState.value.skipDefaultValidation.value.required ? 'required: ' + activeState.value.skipDefaultValidation.value.required : '';
		const dateRange = activeState.value.skipDefaultValidation.value.dateRange ? 'dateRange: ' + activeState.value.skipDefaultValidation.value.dateRange : '';
		const length = activeState.value.skipDefaultValidation.value.length ? 'len: ' + activeState.value.skipDefaultValidation.value.length : '';
		const email = activeState.value.skipDefaultValidation.value.email ? 'email: ' + activeState.value.skipDefaultValidation.value.email : '';
		const parts = [requiredString, dateRange, length, email].filter(v => v !== '');
		return `:skipDefaultValidation="{ ${parts.join(', ')} }"`;
	});

	function onInput(event: Event) {
		logEvent('input', event);
	}

</script>
