<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div v-else class="flex flex-col gap-4">
			<!-- Karte: Kursliste Schüler-Kontaktdaten/Erzieher drucken/versenden -->
			<ui-card v-if="hatKompetenzDruckenSchuelerIndividualdaten" icon="i-ri-printer-line" title="Kursliste drucken oder versenden" subtitle="Eine Liste mit den Daten der Schülerinnen und Schüler der ausgewählten Kurse drucken oder versenden."
				:is-open="currentAction === 'druckKursListeSchuelerKontaktdatenErzieher'" @update:is-open="isOpen => setCurrentAction('druckKursListeSchuelerKontaktdatenErzieher', isOpen)">
				<report-parameters :reportvorlage="ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER" :ids-hauptdaten="[...manager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
			</ui-card>
			<!-- Karte: Kursliste Schüler-Fotos drucken/versenden -->
			<ui-card v-if="hatKompetenzDruckenSchuelerIndividualdaten" icon="i-ri-printer-line" title="Kursliste mit Fotos drucken oder versenden" subtitle="Eine Liste mit den Fotos der Schülerinnen und Schüler der ausgewählten Kurse drucken oder versenden."
				:is-open="currentAction === 'druckKursListeSchuelerFotos'" @update:is-open="isOpen => setCurrentAction('druckKursListeSchuelerFotos', isOpen)">
				<report-parameters :reportvorlage="ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_FOTOS_NAMEN" :ids-hauptdaten="[...manager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
			</ui-card>
			<ui-card v-if="hatKompetenzDruckenSchuelerLeistungsdaten" icon="i-ri-printer-line" title="Leistungsübersicht drucken" subtitle="Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der ausgewählten Kurse drucken"
				:is-open="currentAction === 'druckKursListeSchuelerLeistungsdaten'" @update:is-open="isOpen => setCurrentAction('druckKursListeSchuelerLeistungsdaten', isOpen)">
				<report-parameters :reportvorlage="ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN" :ids-hauptdaten="[...manager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
			</ui-card>
			<!-- Karte: Löschen (bestehende Funktionalität, DEV) -->
			<ui-card v-if="serverState.hasDev && hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Kurse werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="preConditionCheck.success">Alle ausgewählten Kurse sind bereit zum Löschen.</span>
					<template v-else v-for="(message, i) in preConditionCheck.logs" :key="i">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="!preConditionCheck.success || loading" title="Löschen" @click="entferneKurse" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
			<log-box :logs :status>
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { ref, computed } from "vue";
	import type { KurseGruppenprozesseProps } from "./SKurseGruppenprozesseProps";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useServerState } from "@ui/states/ServerState";
	import { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";

	type Action = 'druckKursListeSchuelerKontaktdatenErzieher' | 'druckKursListeSchuelerFotos' | 'druckKursListeSchuelerLeistungsdaten' | 'delete' | '';

	const props = defineProps<KurseGruppenprozesseProps>();
	const benutzerState = useBenutzerState();
	const serverState = useServerState();

	const currentAction = ref<Action>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const hatKompetenzDrucken = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN) || benutzerState.benutzerHatKompetenz(BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN)));
	const hatKompetenzDruckenSchuelerIndividualdaten = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzDruckenSchuelerLeistungsdaten = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const hatIrgendwelcheKompetenzen = computed(() => hatKompetenzDrucken.value || hatKompetenzLoeschen.value || hatKompetenzDruckenSchuelerIndividualdaten.value);

	const preConditionCheck = computed(() => {
		if (currentAction.value === 'delete') {
			return { success: true, logs: new ArrayList<string>() };
		}
		return { success: false, logs: new ArrayList<string>() };
	});

	function setCurrentAction(newAction: Action, open: boolean) {
		if (newAction !== currentAction.value && !open) {
			return;
		}

		currentAction.value = open ? newAction : '';
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		status.value = undefined;
	}

	async function entferneKurse() {
		loading.value = true;

		const [delStatus, logMessages] = await props.deleteKurse();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

</script>
