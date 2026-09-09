<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-statistik-uebersicht /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="p-4 page--statistik">
		<div class="mt-10 mb-4">Statistik:</div>
		<div class="svws-ui-dashboard">
			<svws-ui-dashboard-tile title="Adresse">
				<div>{{ statistikState.statistikGesamt.schule.bezeichnung1 }}</div>
				<div>{{ statistikState.statistikGesamt.schule.bezeichnung2 }}</div>
				<div>{{ statistikState.statistikGesamt.schule.bezeichnung3 }}</div>
				<div>{{ statistikState.statistikGesamt.schule.strassenname }} {{ statistikState.statistikGesamt.schule.hausnummer }} {{ statistikState.statistikGesamt.schule.hausnummerZusatz }} </div>
				<div>{{ statistikState.statistikGesamt.schule.plz }} {{ statistikState.statistikGesamt.schule.ort }}</div>
			</svws-ui-dashboard-tile>
			<svws-ui-dashboard-tile title="Meldung der Statistik" :span="2" color="dark">
				<div>Nächster Termin <span class="font-bold">{{ new Date().toLocaleDateString("de-DE", { day: '2-digit', month: '2-digit', year: 'numeric' }) }}</span></div>
				<div class="mt-8">Frist zur Einreichung · Schuljahr 2027/28</div>
			</svws-ui-dashboard-tile>

			<svws-ui-dashboard-tile title="Daten senden">
				<div class="font-bold">Amtliche Schulstatistik an das Bildungsportal NRW übermitteln.</div>
				<div v-if="!validator.getFehler().isEmpty()" class="font-bold text-ui-danger"> <span class="icon i-ri-error-warning-line icon-ui-danger" /> {{ validator.getFehler().size() }} Fehler in der Gesamtprüfung</div>
				<div><svws-ui-button type="primary">Senden</svws-ui-button></div>
			</svws-ui-dashboard-tile>
			<svws-ui-dashboard-tile title="Schülerdaten">
				<div class="font-bold">{{ statistikState.statistikGesamt.schueler.size() }} Schueler angemeldet</div>
				<div class="font-bold text-ui-danger">{{ statistikState.validatorGesamt.getFehlerSchueler().size() }} Fehler</div>
			</svws-ui-dashboard-tile>
			<svws-ui-dashboard-tile title="Lehrerdaten">
				<div class="font-bold">{{ statistikState.statistikGesamt.lehrer.size() }} Lehrkräfte angestellt</div>
				<div class="font-bold text-ui-danger">{{ statistikState.validatorGesamt.getFehlerLehrer().size() }} Fehler</div>
			</svws-ui-dashboard-tile>
			<svws-ui-dashboard-tile title="Klassendaten">
				<div class="font-bold"> {{ statistikState.statistikGesamt.klassen.size() }} Klassen</div>
				<div class="font-bold text-ui-danger">{{ statistikState.validatorGesamt.getFehlerKlassen().size() }} Fehler</div>
			</svws-ui-dashboard-tile>
			<svws-ui-dashboard-tile title="Unterrichtsdaten">
				<div class="font-bold"> {{ statistikState.statistikGesamt.kurse.size() }} Kurse</div>
				<div class="font-bold text-ui-danger">{{ statistikState.validatorGesamt.getFehlerKurse().size() }} Fehler</div>
			</svws-ui-dashboard-tile>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
	import { ValidatorGesamt } from "@core/asd/validate/ValidatorGesamt";
	import type { List } from "@core/java/util/List";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { useStatistikState } from "@ui/states/statistik/StatistikState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";

	import type { StatistikUebersichtProps } from "./StatistikUebersichtProps";

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const props = defineProps<StatistikUebersichtProps>();
	const schuleState = useSchuleState();
	const statistikState = useStatistikState();


	const validator = computed(() => {
		const val = new ValidatorGesamt({ get: () => statistikState.statistikGesamt }, schuleState.validatorKontext);
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
