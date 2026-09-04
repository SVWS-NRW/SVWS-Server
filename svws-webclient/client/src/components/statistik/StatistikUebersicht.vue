<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-statistik-uebersicht /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page flex gap-8">
		<div class="flex-1">
			<svws-ui-content-card title="Allgemein">
				<div>Es gibt insgesamt {{ validator.getFehler().size() }} Fehler in der Gesamtprüfung</div>
				<div>{{ statistikGesamt.lehrer.size() }} Lehrkräfte</div>
				<div>{{ statistikGesamt.schueler.size() }} Schüler</div>
				<div>{{ statistikGesamt.klassen.size() }} Klassen</div>
				<div>{{ statistikGesamt.kurse.size() }} Kurse</div>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
	import { ValidatorGesamt } from "@core/asd/validate/ValidatorGesamt";
	import type { List } from "@core/java/util/List";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import type { StatistikUebersichtProps } from "./StatistikUebersichtProps";
	import { computed } from "vue";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const props = defineProps<StatistikUebersichtProps>();

	const validator = computed(() => {
		const val = new ValidatorGesamt({ get: () => props.statistikGesamt }, props.validatorKontext());
		val.run();
		return val;
	});

	const gridManager = new GridManager<string, ValidatorFehler, List<ValidatorFehler>>({
		daten: computed(() => validator.value.getFehler()),
		getRowKey: row => `ID_x`,
		allowEmptyRowSelection: true,
		columns: [
			// { kuerzel: "Auswahl", name: "Auswahl", width: "3rem", hideable: false },
			{ kuerzel: "Validator", name: "Validator", width: '1fr' },
			{ kuerzel: "Fehlermeldung", name: "Fehlermeldung", width: '1fr' },
		],
	});
</script>
