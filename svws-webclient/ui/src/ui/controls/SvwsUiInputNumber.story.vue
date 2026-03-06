<template>
	<Story title="Input Number" id="svws-ui-input-number" icon="ri:pencil-line" :layout="{type: 'grid', width: '45%'}" :source="sourceCode">
		<Variant title="Default" id="Default">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-input-number v-model="defaultState.modelValue.value" v-bind="defaultState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>

		<Variant title="Disabled" id="Disabled">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="disabledState.modelValue.value" v-bind="disabledState.props" @input="onInput" />
			</svws-ui-input-wrapper>
		</Variant>

		<Variant title="Range" id="Range">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="rangeState.modelValue.value" v-bind="rangeState.props" @input="onInput" />
			</svws-ui-input-wrapper>
		</Variant>

		<Variant title="Statistik" id="Statistik">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="statisticState.modelValue.value" v-bind="statisticState.props" @input="onInput" />
			</svws-ui-input-wrapper>
		</Variant>

		<Variant title="Headless" id="Headless">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="headlessState.modelValue.value" v-bind="headlessState.props" @input="onInput" />
			</svws-ui-input-wrapper>
		</Variant>

		<Variant title="Readonly" id="Readonly">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="readonlyState.modelValue.value" v-bind="readonlyState.props" @input="onInput" />
			</svws-ui-input-wrapper>
		</Variant>

		<Variant title="Required" id="Required">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="requiredState.modelValue.value" v-bind="requiredState.props" @input="onInput" />
			</svws-ui-input-wrapper>
		</Variant>

		<Variant title="Validation" id="Validation">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-input-number v-model="validationState.modelValue.value" v-bind="validationState.props" @input="onInput" />
			</svws-ui-input-wrapper>
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
				<HstCheckbox title="hideStepper"
					v-model="activeState.hideStepper.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Gibt an, ob die Knöpfe zum Anpassen des Wertes versteckt werden sollen.<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">hideStepper: false</code>
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
						Setzt die Klasse <code class="bg-ui-selected">col-span-2</code> bei "2" bzw. <code class="bg-ui-selected">col-span-full</code> bei "full". Dadurch können Inputs eine automatisch passende Breite in Zum Beispiel Grids oder dem InputWrapper erhalten.<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">span: undefined</code>
					</template>
				</svws-ui-tooltip>
			</div>

			<div class="flex">
				<HstNumber title="min"
					v-model="activeState.min.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Setzt die Untergrenze für den Wert des Inputs. Beim setzen dieser prop wird automatisch ein Validator hinzugefügt, der die Plausibilität des Wertes prüft.
						Dieses automatische Hinzufügen des Validators kann mit der prop <code class="bg-ui-selected">skipDefaultValidation</code> unterbunden werden. <br>
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
						Setzt die Obergrenze für den Wert des Inputs. Beim setzen dieser prop wird automatisch ein Validator hinzugefügt, der die Plausibilität des Wertes prüft.
						Dieses automatische Hinzufügen des Validators kann mit der prop <code class="bg-ui-selected">skipDefaultValidation</code> unterbunden werden. <br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">max: undefined</code>
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

			<div class="text-headline-sm">
				skipDefaultValidation
			</div>
			<div class="flex items-start gap-2">
				<HstCheckbox title="required"
					v-model="activeState.skipDefaultValidation.value.required" />
				<HstCheckbox title="min"
					v-model="activeState.skipDefaultValidation.value.range" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Diese prop kann die Generierung von allen oder von einzelnen Defaulvalidatoren überspringen.
						Defaultvalidatoren werden automatisch gesetzt, wenn <code class="bg-ui-selected">required</code>, <code class="bg-ui-selected">min</code>
						oder <code class="bg-ui-selected">max</code> gesetzt ist. Soll kein Defaultvalidator gesetzt werden, um zum Beispiel eigene Validatoren
						über die prop <code class="bg-ui-selected">validation</code> zu definieren oder die Validierung ganz abzustellen, muss diese prop auf
						<code class="bg-ui-selected">true</code> gesetzt werden. Um einzelne abzuschalten muss ein Objekt mit der entsprechenden Konfiguration
						gesetzt werden. <br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">skipDefaultValidation: false</code>
					</template>
				</svws-ui-tooltip>
			</div>
		</template>
	</Story>
</template>

<script setup lang="ts">

	import { computed, ref, reactive, type Ref, type ComputedRef } from "vue";
	import storyManager from '../../stories/StoryManager';
	import { logEvent } from '../../stories/helper';
	import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
	import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
	import type { ValidatorFehler } from "../../../../core/src/asd/validate/ValidatorFehler";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";
	import type { List } from "../../../../core/src/java/util/List";
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
		hideStepper?: boolean;
		span?: "full" | "2" | undefined;
		min?: number | undefined;
		max?: number | undefined;
		muss?: boolean;
		kann?: boolean;
		hinweis?: boolean;
		skipDefaultValidation?: { required: boolean; range: boolean };
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
		public muss = ref(false);
		public kann = ref(false);
		public hinweis = ref(false);
		public skipDefaultValidation = ref({ required: false, range: false });

		public validatorMuss = new ValidatorTest(() => (this.modelValue.value === 4) ? null : "Hier ist die Eintragung von 4 gewünscht", ValidatorFehlerart.MUSS);
		public validatorKann = new ValidatorTest(() => (this.modelValue.value === 4) ? null : "Hier ist die Eintragung von 4 gewünscht", ValidatorFehlerart.KANN);
		public validatorHinweis = new ValidatorTest(() => (this.modelValue.value === 4) ? null : "Hier ist die Eintragung von 4 gewünscht", ValidatorFehlerart.HINWEIS);


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
			placeholder: this.placeholder,
			statistics: this.statistics,
			disabled: this.disabled,
			required: this.required,
			readonly: this.readonly,
			headless: this.headless,
			focus: this.focus,
			hideStepper: this.hideStepper,
			span: this.spanValue,
			min: this.min,
			max: this.max,
			validation: this.validation,
			skipDefaultValidation: this.skipDefaultValidation,
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
	const rangeState = new VariantState({ min: 2, max: 6 });
	const statisticState = new VariantState({ statistics: true });
	const headlessState = new VariantState({ headless: true });
	const readonlyState = new VariantState({ readonly: true });
	const requiredState = new VariantState({ modelValue: null, required: true });
	const validationState = new VariantState({
		muss: true, kann: true, hinweis: true,
		skipDefaultValidation: { required: false, range: true },
		min: 2, max: 6,
	});

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
		const lines = [
			`v-model="${activeState.value.modelValue.value}"`,
			activeState.value.placeholder.value === "" ? "" : `placeholder="${activeState.value.placeholder.value}"`,
			activeState.value.statistics.value ? `statistics` : "",
			activeState.value.disabled.value ? `disabled` : "",
			activeState.value.required.value ? `required` : "",
			activeState.value.readonly.value ? `readonly` : "",
			activeState.value.headless.value ? `headless` : "",
			activeState.value.focus.value ? `focus` : "",
			activeState.value.hideStepper.value ? `hideStepper` : "",
			(activeState.value.span.value === "undefined") ? "" : `span="${activeState.value.span.value}"`,
			(activeState.value.min.value === undefined) ? "" : `:min="${activeState.value.min.value}"`,
			(activeState.value.max.value === undefined) ? "" : `:max="${activeState.value.max.value}"`,
			(activeState.value.muss.value || activeState.value.kann.value || activeState.value.hinweis.value) ? `:validation="() => getFehler()"` : "",
			skipValidationString.value,
		].filter(Boolean).map(l => indent + l).join("\n");
		return `<svws-ui-input-number
${lines}
 />`;
	});

	const skipValidationString = computed(() => {
		const v = activeState.value.skipDefaultValidation.value;

		if (v.required && v.range) {
			return ':skipDefaultValidation="true"';
		}

		if (!v.required && !v.range) {
			return "";
		}

		const requiredString = activeState.value.skipDefaultValidation.value.required ? 'required: ' + activeState.value.skipDefaultValidation.value.required : '';
		const rangeString = activeState.value.skipDefaultValidation.value.range ? 'range: ' + activeState.value.skipDefaultValidation.value.range : '';
		const parts = [requiredString, rangeString].filter(v => v !== '');
		return `:skipDefaultValidation="{ ${parts.join(', ')} }"`;
	});


</script>
