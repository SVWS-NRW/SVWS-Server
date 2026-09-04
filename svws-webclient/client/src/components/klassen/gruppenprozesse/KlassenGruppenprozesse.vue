<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div v-else class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDruckenSchuelerIndividualdaten" icon="i-ri-printer-line" :title="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER.getUiTitel()" :subtitle="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER.getUiBeschreibung()"
				:is-open="currentAction === 'druckKlasseListeSchuelerKontaktdatenErzieher'" @update:is-open="isOpen => setCurrentAction('druckKlasseListeSchuelerKontaktdatenErzieher', isOpen)">
				<report-parameters :reportvorlage="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER"
					:ids-hauptdaten="[...manager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
			</ui-card>

			<ui-card v-if="hatKompetenzDruckenSchuelerIndividualdaten" icon="i-ri-printer-line" :title="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN.getUiTitel()" :subtitle="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN.getUiBeschreibung()"
				:is-open="currentAction === 'druckKlasseListeSchuelerFotos'" @update:is-open="isOpen => setCurrentAction('druckKlasseListeSchuelerFotos', isOpen)">
				<report-parameters :reportvorlage="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_FOTOS_NAMEN"
					:ids-hauptdaten="[...manager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
			</ui-card>

			<ui-card v-if="hatKompetenzDruckenSchuelerLeistungsdaten" icon="i-ri-printer-line" :title="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN.getUiTitel()" :subtitle="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN.getUiBeschreibung()"
				:is-open="currentAction === 'druckKlasseListeSchuelerLeistungsdaten'" @update:is-open="isOpen => setCurrentAction('druckKlasseListeSchuelerLeistungsdaten', isOpen)">
				<report-parameters :reportvorlage="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN"
					:ids-hauptdaten="[...manager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
			</ui-card>

			<ui-card v-if="hatKompetenzDruckenSchuelerLeistungsdaten" icon="i-ri-printer-line" :title="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN_DETAILLIERT.getUiTitel()" :subtitle="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN_DETAILLIERT.getUiBeschreibung()"
				:is-open="currentAction === 'druckKlasseListeSchuelerLeistungsdatenDetailliert'" @update:is-open="isOpen => setCurrentAction('druckKlasseListeSchuelerLeistungsdatenDetailliert', isOpen)">
				<report-parameters :reportvorlage="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN_DETAILLIERT" :server-mode
					:ids-hauptdaten="[...manager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
			</ui-card>

			<ui-card v-if="hatKompetenzDruckenStundenplan && (mapStundenplaene.size > 0)" icon="i-ri-printer-line" :title="ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN.getUiTitel()" :subtitle="ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN_DETAILLIERT.getUiBeschreibung()"
				:is-open="currentAction === 'druckKlasseStundenplan'" @update:is-open="isOpen => setCurrentAction('druckKlasseStundenplan', isOpen)">
				<div class="flex flex-col">
					<div>
						<ui-select v-model="stundenplanModel" :manager="stundenplanSelectManager" label="Stundenplan" />
					</div>
					<report-parameters :reportvorlage="ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN"
						:id-hauptdaten-objekt="stundenplanModel?.id ?? -1" :ids-hauptdaten="[...manager().liste.auswahl()].map(i=>i.id)" :ids-detaildaten="[]" />
				</div>
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Klassen werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div class="w-full">
					<span v-if="alleKlassenLeer">Alle ausgewählten Klassen sind bereit zum Löschen.</span>
					<span v-if="leereKlassenVorhanden">Einige Klassen haben noch Schüler, leere Klassen können gelöscht werden.</span>
					<div v-if="!alleKlassenLeer">
						<span v-for="message in nichtAlleKlassenLeer" :key="message" class="text-ui-danger"> {{ message }} <br> </span>
					</div>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="manager().getKlassenIDsMitSchuelern().size() === manager().liste.auswahlSize() || loading"
						:title="leereKlassenVorhanden ? 'Leere Klassen löschen' : 'Löschen'" @click="entferneKlassen" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						{{ leereKlassenVorhanden ? 'Leere Klassen löschen' : 'Löschen' }}
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
	import type { KlassenGruppenprozesseProps } from "./KlassenGruppenprozesseProps";
	import type { StundenplanListeEintrag } from "@core/core/data/stundenplan/StundenplanListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useServerState } from "@ui/states/ServerState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";

	type Action = 'druckKlasseListeSchuelerKontaktdatenErzieher' | 'druckKlasseListeSchuelerFotos' | 'druckKlasseListeSchuelerLeistungsdaten' | 'druckKlasseListeSchuelerLeistungsdatenDetailliert' | 'druckKlasseStundenplan' | 'delete' | '';

	const props = defineProps<KlassenGruppenprozesseProps>();
	const benutzerState = useBenutzerState();

	const serverMode = useServerState().mode;

	const hatKompetenzDrucken = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN) || benutzerState.benutzerHatKompetenz(BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN)));
	const hatKompetenzDruckenStundenplan = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzDruckenSchuelerIndividualdaten = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzDruckenSchuelerLeistungsdaten = computed(() => (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzLoeschen = computed(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const hatIrgendwelcheKompetenzen = computed(() => hatKompetenzDrucken.value || hatKompetenzLoeschen.value || hatKompetenzDruckenStundenplan.value || hatKompetenzDruckenSchuelerIndividualdaten.value || hatKompetenzDruckenSchuelerLeistungsdaten.value);

	const currentAction = ref<Action>('');

	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const alleKlassenLeer = computed(() => (currentAction.value === 'delete') && props.manager().getKlassenIDsMitSchuelern().isEmpty());

	const nichtAlleKlassenLeer = computed(() => {
		const errorLog: List<string> = new ArrayList<string>();
		if (!alleKlassenLeer.value) {
			for (const klasse of props.manager().getKlassenIDsMitSchuelern()) {
				errorLog.add(`Klasse ${props.manager().liste.get(klasse)?.kuerzel ?? '???'} (ID: ${klasse}) kann nicht gelöscht werden, da ihr noch Schüler zugeordnet sind.`);
			}
		}
		return errorLog;
	});

	const leereKlassenVorhanden = computed(() =>
		!alleKlassenLeer.value && (props.manager().getKlassenIDsMitSchuelern().size() !== props.manager().liste.auswahlSize()));

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

	async function entferneKlassen() {
		loading.value = true;

		const [delStatus, logMessages] = await props.deleteKlassen();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
	const stundenplanModel = computed({
		get: () => {
			if (stundenplanAuswahl.value === undefined) {
				if (props.mapStundenplaene.size > 0) {
					const [first] = props.mapStundenplaene.values();
					return first;
				}
			}
			return stundenplanAuswahl.value;
		},
		set: value => stundenplanAuswahl.value = value,
	});

	const stundenplanOptions = computed(() => props.mapStundenplaene.values());
	const stundenplanSelectManager = new SelectManager({
		options: stundenplanOptions.value,
		optionDisplayText: s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')',
		selectionDisplayText: s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')',
	});

	const wochentag = ['So.', 'Mo.', 'Di.', 'Mi.', 'Do.', 'Fr.', 'Sa.', 'So.'];

	function toDateStr(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return wochentag[date[3] % 7] + " " + date[2] + "." + date[1] + "." + date[0];
	}

	function toKW(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return "" + date[5];
	}
</script>
