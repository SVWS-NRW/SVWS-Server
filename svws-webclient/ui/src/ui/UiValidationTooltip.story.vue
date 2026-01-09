
<template>
	<Story title="ValidationTooltip" id="svws-ui-validation-tooltip" icon="ri:cursor-line" :layout="{type: 'grid', width: '45%'}">
		<Variant id="ValidationTooltip" title="ValidationTooltip">
			<span v-if="!validationResult.hasFehler">Bitte wähle rechts in den Controls Fehler aus, die angezeigt werden sollen</span>
			<ui-validation-tooltip v-else :validation-result="validationResult" :disabled="state.disabled" />
		</Variant>
		<template #controls>
			<HstCheckbox title="disabled"
				v-model="state.disabled" />
			<h1 class="text-headline-md my-2">Nicht-Statistikfehler</h1>
			<HstCheckbox title="Muss"
				v-model="state.nonStatistikMuss" />
			<HstCheckbox title="Kann"
				v-model="state.nonStatistikKann" />
			<HstCheckbox title="Hinweis"
				v-model="state.nonStatistikHinweis" />
			<h1 class="text-headline-md my-2">Statistikfehler</h1>
			<HstCheckbox title="Muss"
				v-model="state.statistikMuss" />
			<HstCheckbox title="Kann"
				v-model="state.statistikKann" />
			<HstCheckbox title="Hinweis"
				v-model="state.statistikHinweis" />
		</template>
	</Story>
</template>

<script setup lang="ts">
	import { computed, reactive } from "vue";
	import type { ValidatorFehler } from "../../../core/src/asd/validate/ValidatorFehler";
	import { ValidationResult } from "../validation/ValidationResult";
	import { ArrayList } from "../../../core/src/java/util/ArrayList";
	import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
	import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

	const state = reactive({
		disabled: false,
		statistikMuss: true,
		statistikKann: false,
		statistikHinweis: false,
		nonStatistikMuss: false,
		nonStatistikKann: false,
		nonStatistikHinweis: false,
	});

	class ASDMussValidatorMock extends BasicValidator {
		constructor() {
			super(ValidatorFehlerart.MUSS);
			this.run();
		}

		getFehlercodePraefix(): string {
			return "MV";
		}

		protected pruefe(): boolean {
			this.addFehler(0, "Dies ist ein Test ASD Muss Fehler (1)");
			this.addFehler(1, "Dies ist ein Test ASD Muss Fehler (2)");
			return false;
		}
	}

	class ASDKannValidatorMock extends BasicValidator {
		constructor() {
			super(ValidatorFehlerart.KANN);
			this.run();
		}

		getFehlercodePraefix(): string {
			return "KV";
		}

		protected pruefe(): boolean {
			this.addFehler(0, "Dies ist ein Test ASD Kann Fehler (1)");
			this.addFehler(1, "Dies ist ein Test ASD Kann Fehler (2)");
			return false;
		}
	}

	class ASDHinweisValidatorMock extends BasicValidator {
		constructor() {
			super(ValidatorFehlerart.HINWEIS);
			this.run();
		}

		getFehlercodePraefix(): string {
			return "HV";
		}

		protected pruefe(): boolean {
			this.addFehler(0, "Dies ist ein Test ASD Hinweis Fehler (1)");
			this.addFehler(1, "Dies ist ein Test ASD Hinweis Fehler (2)");
			return false;
		}
	}

	class MussValidatorMock extends BasicValidator {
		constructor() {
			super(ValidatorFehlerart.MUSS);
			this.run();
		}

		protected pruefe(): boolean {
			this.addFehler(0, "Dies ist ein Test Muss Fehler (1)");
			this.addFehler(1, "Dies ist ein Test Muss Fehler (2)");
			return false;
		}
	}

	class KannValidatorMock extends BasicValidator {
		constructor() {
			super(ValidatorFehlerart.KANN);
			this.run();
		}

		protected pruefe(): boolean {
			this.addFehler(0, "Dies ist ein Test Kann Fehler (1)");
			this.addFehler(1, "Dies ist ein Test Kann Fehler der seeeehr lang ist, damit wir sehen können, wie das in unserem Tooltip so aussieht (2)");
			return false;
		}
	}

	class HinweisValidatorMock extends BasicValidator {
		constructor() {
			super(ValidatorFehlerart.HINWEIS);
			this.run();
		}

		protected pruefe(): boolean {
			this.addFehler(0, "Dies ist ein Test Hinweis Fehler (1)");
			this.addFehler(1, "Dies ist ein Test Hinweis Fehler (2)");
			return false;
		}
	}

	const activeValidatorClasses = computed<(new () => BasicValidator)[]>(() => {
		const classes: (new () => BasicValidator)[] = [];

		if (state.statistikMuss) {
			classes.push(ASDMussValidatorMock);
		}

		if (state.statistikKann) {
			classes.push(ASDKannValidatorMock);
		}

		if (state.statistikHinweis) {
			classes.push(ASDHinweisValidatorMock);
		}

		if (state.nonStatistikMuss) {
			classes.push(MussValidatorMock);
		}

		if (state.nonStatistikKann) {
			classes.push(KannValidatorMock);
		}

		if (state.nonStatistikHinweis) {
			classes.push(HinweisValidatorMock);
		}

		return classes;
	});

	const validationResult = computed(() => {
		const fehler = new ArrayList<ValidatorFehler>();
		activeValidatorClasses.value
			.map(cls => new cls())
			.forEach(v => fehler.addAll(v.getFehler()));
		return new ValidationResult(fehler);
	});


</script>
