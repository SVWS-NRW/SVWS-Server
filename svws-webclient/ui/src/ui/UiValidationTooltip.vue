<template>
	<svws-ui-tooltip position="right" :disabled>
		<span class="validation-tooltip-icon" :class="{'disabled': disabled}">
			<span class="icon" :class="tooltipIconClasses" />
		</span>
		<template #content>
			<div class="validation-tooltip-content">
				<!-- Nicht-Statistik Fehlergruppen -->
				<div v-if="hasNonStatisticFehler">
					<div v-if="!mussNonStatisticFehler.isEmpty()" class="pt-2 pb-2">
						<div class="fehlergruppe-headline-muss">
							<span>{{ ValidatorFehlerart.MUSS }}</span>
						</div>
						<div v-for="f in mussNonStatisticFehler" :key="f.getFehlermeldung() + f.getFehlercode()" class="fehlergruppe-item">
							<span class="fehler-icon" />
							<span class="fehler-text">{{ f.getFehlermeldung() }}</span>
						</div>
					</div>

					<div v-if="!kannNonStatisticFehler.isEmpty()" class="pt-2 pb-2">
						<div class="fehlergruppe-headline-kann">
							<span>{{ ValidatorFehlerart.KANN }}</span>
						</div>
						<div v-for="f in kannNonStatisticFehler" :key="f.getFehlermeldung() + f.getFehlercode()" class="fehlergruppe-item">
							<span class="fehler-icon" />
							<span class="fehler-text">{{ f.getFehlermeldung() }}</span>
						</div>
					</div>

					<div v-if="!hinweisNonStatisticFehler.isEmpty()" class="pt-2 pb-2">
						<div class="fehlergruppe-headline-hinweis">
							<span>{{ ValidatorFehlerart.HINWEIS }}</span>
						</div>
						<div v-for="f in hinweisNonStatisticFehler" :key="f.getFehlermeldung() + f.getFehlercode()" class="fehlergruppe-item">
							<span class="fehler-icon" />
							<span class="fehler-text">{{ f.getFehlermeldung() }}</span>
						</div>
					</div>
				</div>

				<!-- Statistik Fehlergruppen -->
				<div v-if="hasStatisticFehler">
					<!-- Statistik Headline -->
					<div class="statistic-headline">
						<span class="icon i-ri-bar-chart-2-line icon-ui-statistic pointer-events-auto mt-0.5" />
						<span class="text-headline-md font-medium">Statistik</span>
					</div>

					<!-- Statistik Fehlergruppen -->
					<div v-if="!mussStatisticFehler.isEmpty()" class="pt-2 pb-2">
						<div class="fehlergruppe-headline-muss">
							<span>{{ ValidatorFehlerart.MUSS }}</span>
						</div>
						<div v-for="f in mussStatisticFehler" :key="f.getFehlermeldung() + f.getFehlercode()" class="fehlergruppe-item">
							<div type="light" class="fehler-badge">{{ f.getFehlercode() }}</div>
							<span class="fehler-text">{{ f.getFehlermeldung() }}</span>
						</div>
					</div>

					<div v-if="!kannStatisticFehler.isEmpty()" class="pt-2 pb-2">
						<div class="fehlergruppe-headline-kann">
							<span>{{ ValidatorFehlerart.KANN }}</span>
						</div>
						<div v-for="f in kannStatisticFehler" :key="f.getFehlermeldung() + f.getFehlercode()" class="fehlergruppe-item">
							<div type="light" class="fehler-badge">{{ f.getFehlercode() }}</div>
							<span class="fehler-text">{{ f.getFehlermeldung() }}</span>
						</div>
					</div>

					<div v-if="!hinweisStatisticFehler.isEmpty()" class="pt-2 pb-2">
						<div class="fehlergruppe-headline-hinweis">
							<span>{{ ValidatorFehlerart.HINWEIS }}</span>
						</div>
						<div v-for="f in hinweisStatisticFehler" :key="f.getFehlermeldung() + f.getFehlercode()" class="fehlergruppe-item">
							<div type="light" class="fehler-badge">{{ f.getFehlercode() }}</div>
							<span class="fehler-text">{{ f.getFehlermeldung() }}</span>
						</div>
					</div>
				</div>
			</div>
		</template>
	</svws-ui-tooltip>
</template>
<script setup lang="ts">

	import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";
	import type { ValidatorFehler } from "../../../core/src/asd/validate/ValidatorFehler";
	import type { List } from "../../../core/src/java/util/List";
	import { ArrayList } from "../../../core/src/java/util/ArrayList";
	import { computed } from "vue";
	import type { ValidationResult } from "../validation/ValidationResult";

	const props = defineProps<{
		validationResult: ValidationResult;
		disabled?: boolean;
	}>();

	const hasStatisticFehler = computed(() => !statisticFehler.value.isEmpty());
	const hasNonStatisticFehler = computed(() => !nonStatisticFehler.value.isEmpty());

	const mussStatisticFehler = computed(() => getFehlerByFehlerart(statisticFehler.value, ValidatorFehlerart.MUSS));
	const kannStatisticFehler = computed(() => getFehlerByFehlerart(statisticFehler.value, ValidatorFehlerart.KANN));
	const hinweisStatisticFehler = computed(() => getFehlerByFehlerart(statisticFehler.value, ValidatorFehlerart.HINWEIS));

	const mussNonStatisticFehler = computed(() => getFehlerByFehlerart(nonStatisticFehler.value, ValidatorFehlerart.MUSS));
	const kannNonStatisticFehler = computed(() => getFehlerByFehlerart(nonStatisticFehler.value, ValidatorFehlerart.KANN));
	const hinweisNonStatisticFehler = computed(() => getFehlerByFehlerart(nonStatisticFehler.value, ValidatorFehlerart.HINWEIS));

	const statisticFehler = computed<List<ValidatorFehler>>(() => {
		const statisticFehler = new ArrayList<ValidatorFehler>();
		for (const fehler of props.validationResult.fehler) {
			if (isStatisticFehler(fehler)) {
				statisticFehler.add(fehler);
			}
		}
		return statisticFehler;
	});

	const nonStatisticFehler = computed<List<ValidatorFehler>>(() => {
		const nonStatisticFehler = new ArrayList<ValidatorFehler>();
		for (const fehler of props.validationResult.fehler) {
			if (!isStatisticFehler(fehler)) {
				nonStatisticFehler.add(fehler);
			}
		}
		return nonStatisticFehler;
	});

	const parentFehlerart = computed(() => props.validationResult.fehlerart);

	const tooltipIconClasses = computed(() => {
		return {
			'icon-ui-disabled': props.disabled,
			'i-ri-alert-fill': (parentFehlerart.value.ordinal() === ValidatorFehlerart.MUSS.ordinal()),
			'icon-ui-danger': !props.disabled && (parentFehlerart.value.ordinal() === ValidatorFehlerart.MUSS.ordinal()),
			'i-ri-error-warning-fill': (parentFehlerart.value.ordinal() === ValidatorFehlerart.KANN.ordinal()),
			'icon-ui-caution': !props.disabled && (parentFehlerart.value.ordinal() === ValidatorFehlerart.KANN.ordinal()),
			'i-ri-question-fill': (parentFehlerart.value.ordinal() === ValidatorFehlerart.HINWEIS.ordinal()),
			'icon-ui-warning': !props.disabled && (parentFehlerart.value.ordinal() === ValidatorFehlerart.HINWEIS.ordinal()),
		};
	});

	function getFehlerByFehlerart(fehler: List<ValidatorFehler>, fehlerart: ValidatorFehlerart) {
		const fehlerByFehlerart = new ArrayList<ValidatorFehler>();
		for (const f of fehler) {
			if (f.getFehlerart().ordinal() === fehlerart.ordinal()) {
				fehlerByFehlerart.add(f);
			}
		}
		return fehlerByFehlerart;
	}

	function isStatisticFehler(fehler: ValidatorFehler) {
		return fehler.getFehlercode() !== fehler.getPruefschritt().toString();
	}

</script>
