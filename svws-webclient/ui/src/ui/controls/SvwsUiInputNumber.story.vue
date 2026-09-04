<template>
	<Story title="Input Number" id="svws-ui-input-number" icon="ri:pencil-line" :layout="{type: 'grid', width: '45%'}" :source="sourceCode">
		<Variant title="Default" id="Default">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="defaultState.modelValue.value" v-bind="defaultState.props" @input="onInput" />
			</svws-ui-input-wrapper>
			modelValue: {{ defaultState.modelValue.value }}
		</Variant>

		<Variant title="Disabled" id="Disabled">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="disabledState.modelValue.value" v-bind="disabledState.props" @input="onInput" />
			</svws-ui-input-wrapper>
			modelValue: {{ disabledState.modelValue.value }}
		</Variant>

		<Variant title="Range" id="Range">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="rangeState.modelValue.value" v-bind="rangeState.props" @input="onInput" />
			</svws-ui-input-wrapper>
			modelValue: {{ rangeState.modelValue.value }}
		</Variant>

		<Variant title="Statistik" id="Statistik">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="statisticState.modelValue.value" v-bind="statisticState.props" @input="onInput" />
			</svws-ui-input-wrapper>
			modelValue: {{ statisticState.modelValue.value }}
		</Variant>

		<Variant title="Headless" id="Headless">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="headlessState.modelValue.value" v-bind="headlessState.props" @input="onInput" />
			</svws-ui-input-wrapper>
			modelValue: {{ headlessState.modelValue.value }}
		</Variant>

		<Variant title="Readonly" id="Readonly">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="readonlyState.modelValue.value" v-bind="readonlyState.props" @input="onInput" />
			</svws-ui-input-wrapper>
			modelValue: {{ readonlyState.modelValue.value }}
		</Variant>

		<Variant title="Required" id="Required">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="requiredState.modelValue.value" v-bind="requiredState.props" @input="onInput" />
			</svws-ui-input-wrapper>
			modelValue: {{ requiredState.modelValue.value }}
		</Variant>

		<Variant title="Validation" id="Validation">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="validationState.modelValue.value" v-bind="validationState.props" @input="onInput" />
			</svws-ui-input-wrapper>
			modelValue: {{ validationState.modelValue.value }}
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
						Gibt an, ob das Input einen Wert enthalten muss. Falls true und es werden keine Validatorfehler über die prop
						<code class="bg-ui-selected">validation</code> von außen in die Komponente gegeben, dann wird automatisch ein Validator hinzugefügt,
						der dies prüft und ggf. Fehler anzeigt, ohne diese nach außen weiterzureichen. <br>
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
			<div class="flex flex-col border p-1 rounded border-ui-50">
				<div class="text-headline-sm">
					span
					<svws-ui-tooltip position="top">
						<span class="icon i-ri-question-line" />
						<template #content>
							Setzt die Klasse <code class="bg-ui-selected">col-span-2</code> bei "2" bzw. <code class="bg-ui-selected">col-span-full</code> bei "full". Dadurch können Inputs eine automatisch passende Breite in Zum Beispiel Grids oder dem InputWrapper erhalten.<br>
							<span class="font-bold">Default:</span>  <code class="bg-ui-selected">span: undefined</code>
						</template>
					</svws-ui-tooltip>
				</div>
				<HstRadio row
					v-model="activeState.span.value" :options="[
						{ label: 'undefined', value: 'undefined' },
						{ label: 'full', value: 'full' },
						{ label: '2', value: '2' },
					]" />
			</div>
			<div class="flex">
				<HstNumber title="min"
					v-model="activeState.min.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Setzt die Untergrenze für den Wert des Inputs. Falls gesetzt und es werden keine Validatorfehler über die prop
						<code class="bg-ui-selected">validation</code> von außen in die Komponente gegeben, dann wird automatisch ein Validator hinzugefügt,
						der dies prüft und ggf. Fehler anzeigt, ohne diese nach außen weiterzureichen. <br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">min: undefined</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstNumber title="max"
					v-model="activeState.max.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Setzt die Obergrenze für den Wert des Inputs. Falls gesetzt und es werden keine Validatorfehler über die prop
						<code class="bg-ui-selected">validation</code> von außen in die Komponente gegeben, dann wird automatisch ein Validator hinzugefügt,
						der dies prüft und ggf. Fehler anzeigt, ohne diese nach außen weiterzureichen. <br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">max: undefined</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex flex-col border p-1 rounded border-ui-50">
				<div class="text-headline-sm">
					steps
					<svws-ui-tooltip position="top">
						<span class="icon i-ri-question-line" />
						<template #content>
							Definiert die Zahl, die beim Drücken des Plus- und Minus-Buttons addiert/subtrahiert wird. Bei
							<code class="bg-ui-selected">false</code> werden keine Buttons gerendert. <br>
							Ist für steps eine Zahl definiert, so muss diese mit <code class="bg-ui-selected">decimalPlaces</code> kompatibel sein. Bedeutet
							eine Angabe von steps mit mehr Nachkommastellen, als durch <code class="bg-ui-selected">decimalPlaces</code> erlaubt ist führt zu
							einer Fehlermeldung. <br>
							<span class="font-bold">Default:</span>  <code class="bg-ui-selected">steps: 1</code>
						</template>
					</svws-ui-tooltip>
				</div>
				<HstCheckbox title="false"
					v-model="activeState.hideStepper.value" />
				<HstNumber title="Schrittweite"
					v-model="activeState.steps.value" :disabled="activeState.hideStepper.value" :decimal-places="4" />
			</div>
			<div class="flex">
				<HstNumber title="decimalPlaces"
					v-model="activeState.decimalPlaces.value" :min="0" :max="4" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Definiert die Anzahl der Nachkommastellen, die für die eingegebene Zahl erlaubt sind. Bei <code class="bg-ui-selected">0</code>
						sind nur ganze Zahlen erlaubt und die Eingabe von Komma oder Punkt wird unterbunden. Bei <code class="bg-ui-selected">&gt; 0</code>
						werden die Nachkommastellen automatisch auf die angegebene Länge gekürzt. Es sind nur Zahlen von 0 bis 4 erlaubt<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">decimalPlaces: 0</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex flex-col border p-1 rounded border-ui-50">
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
			</div>
		</template>
	</Story>
</template>

<script setup lang="ts">

	import { BasicValidator } from "@core/asd/validate/BasicValidator";
	import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
	import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { logEvent } from "@ui/stories/helper";
	import storyManager from "@ui/stories/StoryManager";
	import { computed, ref, reactive, type Ref } from "vue";

	const activeState = computed(() => variantControlsMap.get(storyManager.variant.id) ?? defaultState);

	type State = {
		modelValue?: number | null;
		placeholder?: string;
		statistics?: boolean;
		disabled?: boolean;
		required?: boolean;
		readonly?: boolean;
		headless?: boolean;
		focus?: boolean;
		span?: "full" | "2";
		min?: number;
		max?: number;
		hideStepper?: boolean;
		steps?: number;
		decimalPlaces?: 0 | 1 | 2 | 3 | 4;
		muss?: boolean;
		kann?: boolean;
		hinweis?: boolean;
	};

	class VariantState {

		public modelValue: Ref<number | null> = ref(null);
		public placeholder = ref("Titel");
		public statistics = ref(false);
		public disabled = ref(false);
		public required = ref(false);
		public readonly = ref(false);
		public headless = ref(false);
		public focus = ref(false);
		public hideStepper = ref(false);
		public span = ref<"full" | "2" | "undefined">("2");
		public min = ref<number | undefined>(undefined);
		public max = ref<number | undefined>(undefined);
		public steps = ref<number>();
		public decimalPlaces = ref<0 | 1 | 2 | 3 | 4>(0);
		public muss = ref(false);
		public kann = ref(false);
		public hinweis = ref(false);

		public validatorMuss = new ValidatorTest(() => (this.modelValue.value === 4) ? null : "Hier ist die Eintragung von 4 gewünscht", ValidatorFehlerart.MUSS);
		public validatorKann = new ValidatorTest(() => (this.modelValue.value === 4) ? null : "Hier ist die Eintragung von 4 gewünscht", ValidatorFehlerart.KANN);
		public validatorHinweis = new ValidatorTest(() => (this.modelValue.value === 4) ? null : "Hier ist die Eintragung von 4 gewünscht", ValidatorFehlerart.HINWEIS);


		public validation = computed(() => {
			const validatorFehler = new ArrayList<ValidatorFehler>();

			if (!this.muss.value && !this.kann.value && !this.hinweis.value) {
				return undefined;
			}

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

		public stepsProp = computed(() => {
			if (this.hideStepper.value === true) {
				return false;
			}

			return this.steps.value;
		});

		private spanValue = computed(() => {
			if (this.span.value === "undefined") {
				return undefined;
			} else {
				return this.span.value;
			}
		});

		public props = reactive({
			placeholder: this.placeholder,
			statistics: this.statistics,
			disabled: this.disabled,
			required: this.required,
			readonly: this.readonly,
			headless: this.headless,
			focus: this.focus,
			span: this.spanValue,
			min: this.min,
			max: this.max,
			steps: this.stepsProp,
			decimalPlaces: this.decimalPlaces,
			validation: this.validation,
		});

		constructor(state: State) {
			this.modelValue.value = state.modelValue ?? this.modelValue.value;
			this.placeholder.value = state.placeholder ?? this.placeholder.value;
			this.statistics.value = state.statistics ?? this.statistics.value;
			this.disabled.value = state.disabled ?? this.disabled.value;
			this.required.value = state.required ?? this.required.value;
			this.readonly.value = state.readonly ?? this.readonly.value;
			this.headless.value = state.headless ?? this.headless.value;
			this.focus.value = state.focus ?? this.focus.value;
			this.hideStepper.value = state.hideStepper ?? this.hideStepper.value;
			this.span.value = state.span ?? this.span.value;
			this.min.value = state.min;
			this.max.value = state.max;
			this.steps.value = state.steps ?? this.steps.value;
			this.decimalPlaces.value = state.decimalPlaces ?? this.decimalPlaces.value;
			this.muss.value = state.muss ?? this.muss.value;
			this.kann.value = state.kann ?? this.kann.value;
			this.hinweis.value = state.hinweis ?? this.hinweis.value;
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
	const rangeState = new VariantState({ min: 2, max: 6 });
	const statisticState = new VariantState({ statistics: true });
	const headlessState = new VariantState({ headless: true });
	const readonlyState = new VariantState({ readonly: true });
	const requiredState = new VariantState({ modelValue: null, required: true });
	const validationState = new VariantState({ muss: false, kann: true, hinweis: false	});

	const variantControlsMap = new Map<string, VariantState>();
	variantControlsMap.set('Default', defaultState);
	variantControlsMap.set('Disabled', disabledState);
	variantControlsMap.set('Range', rangeState);
	variantControlsMap.set('Statistik', statisticState);
	variantControlsMap.set('Headless', headlessState);
	variantControlsMap.set('Readonly', readonlyState);
	variantControlsMap.set('Required', requiredState);
	variantControlsMap.set('Validation', validationState);

	function onInput(event: Event) {
		logEvent('input', event);
	}

	const sourceCode = computed(() => {
		const indent = "\t";
		const attrs = [
			'v-model="inputValueRef"',
			activeState.value.placeholder.value === "" ? "" : `placeholder="${activeState.value.placeholder.value}"`,
			activeState.value.statistics.value ? `statistics` : "",
			activeState.value.disabled.value ? `disabled` : "",
			activeState.value.required.value ? `required` : "",
			activeState.value.readonly.value ? `readonly` : "",
			activeState.value.headless.value ? `headless` : "",
			activeState.value.focus.value ? `focus` : "",
			(activeState.value.stepsProp.value === undefined) || (activeState.value.stepsProp.value === 1) ? "" : `:steps="${activeState.value.stepsProp.value}"`,
			(activeState.value.decimalPlaces.value === 0) ? "" : `:decimal-places="${activeState.value.decimalPlaces.value}"`,
			(activeState.value.span.value === "undefined") ? "" : `span="${activeState.value.span.value}"`,
			(activeState.value.min.value === undefined) ? "" : `:min="${activeState.value.min.value}"`,
			(activeState.value.max.value === undefined) ? "" : `:max="${activeState.value.max.value}"`,
			(activeState.value.muss.value || activeState.value.kann.value || activeState.value.hinweis.value) ? `:validation="() => getFehler()"` : "",
		].filter(Boolean);

		const lines = attrs.map(l => indent + l).join("\n");

		return [
			"<svws-ui-input-number",
			lines,
		].join("\n") + " />";
	});

</script>
