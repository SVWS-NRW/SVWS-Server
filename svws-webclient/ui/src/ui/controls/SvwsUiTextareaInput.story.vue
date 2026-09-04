<template>
	<Story title="Textarea Input" id="svws-ui-textarea-input" icon="ri:align-left" :layout="{type: 'grid', width: '45%'}" :source="sourceCode">
		<Variant title="Default" id="Default">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-textarea-input v-model="defaultState.modelValue.value" v-bind="defaultState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Disabled" id="Disabled">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-textarea-input v-model="disabledState.modelValue.value" v-bind="disabledState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Zeichenlänge" id="Zeichenlänge">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-textarea-input v-model="lengthState.modelValue.value" v-bind="lengthState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Statistik" id="Statistik">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-textarea-input v-model="statisticState.modelValue.value" v-bind="statisticState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Headless" id="Headless">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-textarea-input v-model="headlessState.modelValue.value" v-bind="headlessState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Required" id="Required">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-textarea-input v-model="requiredState.modelValue.value" v-bind="requiredState.props" @input="onInput" />
				</svws-ui-input-wrapper>
			</div>
		</Variant>
		<Variant title="Validation" id="Validation">
			<div class="p-4">
				<svws-ui-input-wrapper :grid="4">
					<svws-ui-textarea-input v-model="validationState.modelValue.value" v-bind="validationState.props" @input="onInput" />
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
			<div class="flex items-start gap-2 text-headline-sm mt-3">
				<HstRadio title="resizeable" row
					v-model="activeState.resizeable.value" :options="[
						{ label: 'none', value: 'none' },
						{ label: 'horizontal', value: 'horizontal' },
						{ label: 'vertical', value: 'vertical' },
						{ label: 'both', value: 'both' },
					]" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Bestimmt, ob und in welche Richtung die Größe des Inputs verändert werden kann.<br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">resizeable: 'vertical'</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex items-start gap-2 text-headline-sm mt-3">
				<HstRadio title="span" row
					v-model="activeState.span.value" :options="[
						{ label: 'undefined', value: 'undefined' },
						{ label: 'full', value: 'full' },
						{ label: 'grow', value: 'grow' },
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
				<HstNumber title="maxLen"
					v-model="activeState.maxLen.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Setzt die Obergrenze für die Zeichenlänge des Inputs. Falls ein Wert gesetzt wird und es werden keine Validatorfehler über die prop
						<code class="bg-ui-selected">validation</code> von außen in die Komponente gegeben, dann wird automatisch ein Validator hinzugefügt,
						der dies prüft und ggf. Fehler anzeigt, ohne diese nach außen weiterzureichen.
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">maxLen: undefined</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstNumber title="rows"
					v-model="activeState.rows.value" />
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line" />
					<template #content>
						Bestimmt die Anzahl der Zeilen, die im Input angezeigt werden ohne einen Scrollbalken zu erzeugen. Minimum ist 2.
						Ist kein Wert definiert oder er liegt darunter, dann werden automatisch 2 Zeilen gerendert. <br>
						<span class="font-bold">Default:</span>  <code class="bg-ui-selected">rows: undefined</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="text-headline-sm mt-3">
				validation: Validatorfehler mit Härtegrad
				<svws-ui-tooltip position="top">
					<span class="icon i-ri-question-line inline" />
					<template #content>
						Erstellt eine Liste mit ValidatorFehlern der entsprechenden Härtegrade.
						Diese wird dann per <code class="bg-ui-selected">() => validatorFehler</code> an die prop
						<code class="bg-ui-selected">validation</code> übergeben. Sobald die prop <code class="bg-ui-selected">validation</code>
						gesetzt ist, werden keine Defaultvalidatoren für <code class="bg-ui-selected">required</code> oder
						<code class="bg-ui-selected">maxLen</code> gesetzt. <br>
						<span class="font-bold">Default:</span> <code class="bg-ui-selected">validation: leere ArrayList&lt;ValidatorFehler&gt;</code>
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="flex">
				<HstCheckbox title="Muss"
					v-model="activeState.muss.value" />
				<HstCheckbox title="Kann"
					v-model="activeState.kann.value" />
				<HstCheckbox title="Hinweis"
					v-model="activeState.hinweis.value" />
			</div>
		</template>
	</Story>
</template>

<script setup lang="ts">

	import { BasicValidator } from '@core/asd/validate/BasicValidator';
	import type { ValidatorFehler } from '@core/asd/validate/ValidatorFehler';
	import { ValidatorFehlerart } from '@core/asd/validate/ValidatorFehlerart';
	import { ArrayList } from '@core/java/util/ArrayList';
	import { logEvent } from '@ui/stories/helper';
	import storyManager from '@ui/stories/StoryManager';
	import { computed, ref, type Ref, reactive } from 'vue';

	const activeState = computed(() => variantControlsMap.get(storyManager.variant.id) ?? defaultState);

	type ResizeableOption = "both" | "horizontal" | "vertical" | "none";
	type State = {
		modelValue?: string | null;
		placeholder?: string;
		statistics?: boolean;
		disabled?: boolean;
		required?: boolean;
		headless?: boolean;
		resizeable?: ResizeableOption;
		cols?: number;
		rows?: number;
		maxLen?: number;
		span?: "full" | "grow";
		muss?: boolean;
		kann?: boolean;
		hinweis?: boolean;
	};

	class VariantState {

		public modelValue: Ref<string | null> = ref(null);
		public placeholder = ref("Titel");
		public maxLen: Ref<number | undefined> = ref();
		public statistics = ref(false);
		public disabled = ref(false);
		public required = ref(false);
		public headless = ref(false);
		public resizeable = ref("both" as ResizeableOption);
		public rows: Ref<number | undefined> = ref(3);
		public span = ref<"full" | "grow" | "undefined">("grow");
		public muss = ref(false);
		public kann = ref(false);
		public hinweis = ref(false);

		public validatorMuss = new ValidatorTest(() => (this.modelValue.value === "Test") ? null : "Hier ist die Eintragung von 'Test' gewünscht", ValidatorFehlerart.MUSS);
		public validatorKann = new ValidatorTest(() => (this.modelValue.value === "Test") ? null : "Hier ist die Eintragung von 'Test' gewünscht", ValidatorFehlerart.KANN);
		public validatorHinweis = new ValidatorTest(() => (this.modelValue.value === "Test") ? null : "Hier ist die Eintragung von 'Test' gewünscht", ValidatorFehlerart.HINWEIS);


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
			headless: this.headless,
			span: this.spanValue,
			maxLen: this.maxLen,
			resizeable: this.resizeable,
			rows: this.rows,
			validation: this.validation,
		});

		constructor(state: State) {
			this.modelValue.value = state.modelValue ?? this.modelValue.value;
			this.placeholder.value = state.placeholder ?? this.placeholder.value;
			this.statistics.value = state.statistics ?? this.statistics.value;
			this.disabled.value = state.disabled ?? this.disabled.value;
			this.required.value = state.required ?? this.required.value;
			this.headless.value = state.headless ?? this.headless.value;
			this.resizeable.value = state.resizeable ?? this.resizeable.value;
			this.rows.value = state.rows ?? this.rows.value;
			this.span.value = state.span ?? this.span.value;
			this.maxLen.value = state.maxLen;
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

	const defaultState = new VariantState({ modelValue: "Hello" });
	const disabledState = new VariantState({ disabled: true });
	const lengthState = new VariantState({ maxLen: 5 });
	const statisticState = new VariantState({ statistics: true });
	const headlessState = new VariantState({ headless: true });
	const requiredState = new VariantState({ required: true });
	const validationState = new VariantState({ kann: true });

	const variantControlsMap = new Map<string, VariantState>();
	variantControlsMap.set('Default', defaultState);
	variantControlsMap.set('Disabled', disabledState);
	variantControlsMap.set('Zeichenlänge', lengthState);
	variantControlsMap.set('Statistik', statisticState);
	variantControlsMap.set('Headless', headlessState);
	variantControlsMap.set('Required', requiredState);
	variantControlsMap.set('Validation', validationState);


	const sourceCode = computed(() => {
		const indent = "\t";
		const lines = [
			`v-model="inputValueRef"`,
			activeState.value.placeholder.value === "" ? "" : `placeholder="${activeState.value.placeholder.value}"`,
			activeState.value.statistics.value ? `statistics` : "",
			activeState.value.disabled.value ? `disabled` : "",
			activeState.value.required.value ? `required` : "",
			activeState.value.headless.value ? `headless` : "",
			(activeState.value.resizeable.value === "vertical") ? "" : `resizeable="${activeState.value.resizeable.value}"`,
			(activeState.value.span.value === "undefined") ? "" : `span="${activeState.value.span.value}"`,
			(activeState.value.maxLen.value === undefined) ? "" : `:max-len="${activeState.value.maxLen.value}"`,
			(activeState.value.rows.value === undefined) || (activeState.value.rows.value === 3) ? "" : `:rows="${activeState.value.rows.value}"`,
			(activeState.value.muss.value || activeState.value.kann.value || activeState.value.hinweis.value) ? `:validation="() => getFehler()"` : "",
		].filter(Boolean).map(l => indent + l).join("\n");
		return `<svws-ui-textarea-input
${lines} />`;
	});

	function onInput(value: string | null) {
		logEvent('input', value);
	}

</script>
