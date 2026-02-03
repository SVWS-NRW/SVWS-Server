<template>
	<div class="page page-grid-cards">
		<svws-ui-input-wrapper class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDruckenStundenplan && (mapStundenplaene.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken oder versenden" subtitle="Drucke oder versende die Stundenpläne der ausgewählten Lehrkräfte."
				:is-open="currentAction === 'druckLehrerStundenplan'" @update:is-open="isOpen => setCurrentAction('druckLehrerStundenplan', isOpen)">
				<svws-ui-input-wrapper :grid="2" class="p-2">
					<div class="text-left col-span-2">
						<ui-select :disabled="!lehrerListeManager().liste.auswahlExists()" label="Stundenplan" v-model="stundenplanAuswahl"
							:manager="stundenplanSelectManager" />
					</div>
					<div class="text-left col-span-2 mb-2">
						<svws-ui-checkbox v-model="option2">Pausenzeiten anzeigen</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option4">Pausenaufsichten anzeigen</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option8">Fachkürzel statt Fachbezeichnung verwenden</svws-ui-checkbox><br>
					</div>
					<div class="text-left mb-2">
						<br><p class="font-bold underline mb-2">Optionen zur Druckausgabe:</p>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="0" v-model="druckoptionLehrerStundenplan" name="druckoptionLehrerStundenplanGesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="1" v-model="druckoptionLehrerStundenplan" name="druckoptionLehrerStundenplanEinzelausdruckEinseitig" label="Einzelausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="druckoptionLehrerStundenplan" name="druckoptionLehrerStundenplanKombinierterAusdruck" label="Kombinierter Ausdruck" />
						</svws-ui-radio-group>
					</div>
					<div class="text-left mb-2">
						<br><p class="font-bold underline mb-2">Sortierung:</p>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="1" v-model="sortieroptionLehrerStundenplan" name="sortieroptionLehrerStundenplanName" label="Standard (Nachname, Vorname(n))" />
							<svws-ui-radio-option :value="2" v-model="sortieroptionLehrerStundenplan" name="sortieroptionLehrerStundenplanKuerzel" label="Kürzel, Nachname, Vorname(n)" />
						</svws-ui-radio-group>
					</div>
					<div class="text-left col-span-2">
						<svws-ui-button :disabled="isPrintStundenplanDisabled" @click="downloadPDF" :is-loading="loading" class="mt-4">
							<svws-ui-spinner v-if="loading" spinning />
							<span v-else class="icon i-ri-printer-line" />
							Drucken
						</svws-ui-button>
					</div>
					<!-- E-Mail-Eingabefelder -->
					<div class="text-left col-span-2 mb-2" v-if="ServerMode.DEV.checkServerMode(serverMode)">
						<br><p class="font-bold mb-2">Den individuellen Stundenplan per E-Mail an die Lehrkräfte versenden.</p>
						<div class="flex flex-col gap-2">
							<input type="text" v-model="emailBetreff" placeholder="Betreff eingeben" class="w-full border rounded px-2 py-1">
							<textarea v-model="emailText" rows="5" placeholder="E-Mail-Text eingeben" class="w-full border rounded px-2 py-1" />
							<svws-ui-checkbox v-model="istPrivateEmailAlternative" name="istPrivateEmailAlternative">
								Private E-Mail verwenden, wenn keine schulische E-Mail-Adresse vorhanden ist.
							</svws-ui-checkbox>
						</div>
					</div>
					<div class="text-left col-span-2" v-if="ServerMode.DEV.checkServerMode(serverMode)">
						<svws-ui-button :disabled="isEmailStundenplanDisabled" @click="sendPdfByEmail" :is-loading="loading" class="mt-4">
							<svws-ui-spinner v-if="loading" spinning />
							<span v-else class="icon i-ri-mail-send-line" />
							E-Mail senden
						</svws-ui-button>
					</div>
					<!-- Ende: E-Mail-Eingabefelder -->
					<div v-if="!lehrerListeManager().liste.auswahlExists()">
						<span class="text-ui-danger">Es ist keine Lehrkraft ausgewählt.</span>
					</div>
				</svws-ui-input-wrapper>
			</ui-card>
			<ui-card v-if="hatKompetenzDruckenSchuelerLeistungsdaten" icon="i-ri-printer-line" title="Leistungsübersicht drucken" subtitle="Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der ausgewählten Lehrkräfte drucken"
				:is-open="currentAction === 'druckLehrerListeSchuelerLeistungsdaten'" @update:is-open="isOpen => setCurrentAction('druckLehrerListeSchuelerLeistungsdaten', isOpen)">
				<svws-ui-input-wrapper :grid="2" class="p-2">
					<div class="text-left col-span-2 mb-2">
						<br><p class="font-bold underline">Elemente der Druckausgabe:</p>
						<div class="grid grid-cols-2 gap-x-4">
							<template v-for="vp in vpLeistungsdaten" :key="vp.name">
								<svws-ui-checkbox v-if="vp.typ === ReportingVorlageParameterTyp.BOOLEAN.getId()" :model-value="vp.wert === 'true'" @update:model-value="v => vp.wert = v.toString()" :name="vp.name">{{ vp.bezeichnung }}</svws-ui-checkbox>
							</template>
						</div>
					</div>
					<div class="text-left col-span-2 mb-2">
						<br><p class="font-bold underline">Optionen zur Druckausgabe:</p>
						<svws-ui-radio-group class="grid grid-cols-4 p-2 items-start">
							<svws-ui-radio-option :value="1" v-model="druckoptionLehrerListeSchuelerLeistungsdaten" name="druckoptionLehrerListeSchuelerLeistungsdatenGesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="druckoptionLehrerListeSchuelerLeistungsdaten" name="druckoptionLehrerListeSchuelerLeistungsdatenEinzelausdruckEinseitig" label="Einzelausdruck einseitig" />
							<svws-ui-radio-option :value="3" v-model="druckoptionLehrerListeSchuelerLeistungsdaten" name="druckoptionLehrerListeSchuelerLeistungsdatenGesamtausdruckDuplex" label="Gesamtausdruck duplex" />
							<svws-ui-radio-option :value="4" v-model="druckoptionLehrerListeSchuelerLeistungsdaten" name="druckoptionLehrerListeSchuelerLeistungsdatenEinzelausdruckDuplex" label="Einzelausdruck duplex" />
						</svws-ui-radio-group>
					</div>
					<div class="text-left col-span-2">
						<svws-ui-button :disabled="isPrintDisabled" @click="downloadPDF" :is-loading="loading" class="mt-4">
							<svws-ui-spinner v-if="loading" spinning />
							<span v-else class="icon i-ri-printer-line" />
							Drucken
						</svws-ui-button>
					</div>
				</svws-ui-input-wrapper>
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen"
				subtitle="Setze einen Löschvermerk bei den ausgewählten Lehrkräften." :is-open="currentAction === 'delete'"
				@update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="deleteCheck().success">Bereit zum Löschen.</span>
					<template v-else v-for="message in deleteCheck().logs" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="isDeleteDisabled" title="Löschen" @click="entferneLehrer" :is-loading="loading" class="mt-4">
						<svws-ui-spinner v-if="loading" spinning />
						<span v-else class="icon i-ri-play-line" />
						Löschen
					</svws-ui-button>
				</template>
			</ui-card>
		</svws-ui-input-wrapper>
		<log-box :logs :status="statusAction">
			<template #button>
				<svws-ui-button v-if="statusAction !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
			</template>
		</log-box>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { SLehrerAllgemeinesGruppenprozesseProps } from "./SLehrerAllgemeinesGruppenprozesseProps";
	import type { List, StundenplanListeEintrag } from "@core";
	import { DateUtils, ReportingParameter, ReportingReportvorlage, ReportingVorlageParameterTyp, ListUtils, ArrayList, BenutzerKompetenz, ReportingSortierungDefinition, ReportingEMailDaten, ReportingEMailEmpfaengerTyp, ReportingAusgabeformat, ServerMode } from "@core";
	import { SelectManager } from "@ui";

	type Action = 'druckLehrerStundenplan' | 'druckLehrerListeSchuelerLeistungsdaten' | 'delete' | '';

	const props = defineProps<SLehrerAllgemeinesGruppenprozesseProps>();

	const hatKompetenzDrucken = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN) || props.benutzerKompetenzen.has(BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN)));
	const hatKompetenzDruckenStundenplan = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzDruckenSchuelerLeistungsdaten = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LOESCHEN));

	const isPrintDisabled = computed<boolean>(() => !props.lehrerListeManager().liste.auswahlExists() || loading.value);
	const isPrintStundenplanDisabled = computed<boolean>(() => isPrintDisabled.value || stundenplanAuswahl.value === undefined);
	const isEmailStundenplanDisabled = computed<boolean>(() => isPrintStundenplanDisabled.value || ((emailBetreff.value.trim().length) === 0) || ((emailText.value.trim().length) === 0));
	const isDeleteDisabled = computed<boolean>(() => !hatKompetenzLoeschen.value || !props.lehrerListeManager().liste.auswahlExists() || !props.deleteCheck().success || loading.value);

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
	const currentAction = ref<Action>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const statusAction = ref<boolean | undefined>();

	const stundenplanDisplayText = (eintrag: StundenplanListeEintrag) => {
		return eintrag.bezeichnung.replace('Stundenplan ', '') + ': '
			+ toDateStr(eintrag.gueltigAb) + '—'
			+ toDateStr(eintrag.gueltigBis)
			+ ' (KW ' + toKW(eintrag.gueltigAb) + '—'
			+ toKW(eintrag.gueltigBis) + ')';
	};

	const stundenplaene = computed<Array<StundenplanListeEintrag>>(() => [...props.mapStundenplaene.values()]);
	const stundenplanSelectManager = new SelectManager({
		options: stundenplaene, optionDisplayText: stundenplanDisplayText, selectionDisplayText: stundenplanDisplayText,
	});

	const option2 = ref(false);
	const option4 = ref(false);
	const option8 = ref(false);

	const druckoptionLehrerStundenplan = ref(1);
	const druckoptionLehrerListeSchuelerLeistungsdaten = ref(1);
	const sortieroptionLehrerStundenplan = ref(1);

	const vpLeistungsdaten = ref(new ArrayList(ReportingReportvorlage.LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN.getVorlageParameterList()));

	const emailBetreff = ref<string>("");
	const emailText = ref<string>("");
	const istPrivateEmailAlternative = ref<boolean>(false);

	async function downloadPDF() {
		const reportingParameter = new ReportingParameter();
		const listeIdsLehrer = new ArrayList<number>();
		for (const lehrer of props.lehrerListeManager().liste.auswahl()) {
			listeIdsLehrer.add(lehrer.id);
		}

		switch (currentAction.value) {
			case 'druckLehrerStundenplan':
				if (stundenplanAuswahl.value === undefined) {
					return;
				}
				if (druckoptionLehrerStundenplan.value === 2) {
					reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT.getBezeichnung();
					reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN_KOMBINIERT.getVorlageParameterList());
				} else {
					reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN.getBezeichnung();
					reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN.getVorlageParameterList());
				}
				for (const vp of reportingParameter.vorlageParameter) {
					switch (vp.name) {
						case "mitPausenzeiten":
							vp.wert = option2.value.toString();
							break;
						case "mitPausenaufsichten":
							vp.wert = option4.value.toString();
							break;
						case "mitFachkuerzelStattFachbezeichnung":
							vp.wert = option8.value.toString();
							break;
					}
				}
				reportingParameter.idsHauptdaten = ListUtils.create1(stundenplanAuswahl.value.id);
				reportingParameter.idsDetaildaten = listeIdsLehrer;
				reportingParameter.einzelausgabeHauptdaten = false;
				reportingParameter.einzelausgabeDetaildaten = (druckoptionLehrerStundenplan.value === 1);
				reportingParameter.sortierungDetaildaten = new ReportingSortierungDefinition();
				reportingParameter.sortierungDetaildaten.verwendeStandardsortierung = (sortieroptionLehrerStundenplan.value === 1);
				if (sortieroptionLehrerStundenplan.value === 2) {
					const attribute = new ArrayList<string>();
					attribute.add("lehrer.kuerzel");
					attribute.add("lehrer.nachname");
					attribute.add("lehrer.vorname");
					attribute.add("lehrer.vornamen");
					attribute.add("lehrer.geburtsdatum");
					attribute.add("lehrer.id");
					reportingParameter.sortierungDetaildaten.attribute = attribute;
				}
				break;
			case 'druckLehrerListeSchuelerLeistungsdaten':
				reportingParameter.reportvorlage = ReportingReportvorlage.LEHRER_V_LISTE_SCHUELER_LEISTUNGSDATEN.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsLehrer;
				reportingParameter.einzelausgabeHauptdaten = ((druckoptionLehrerListeSchuelerLeistungsdaten.value === 2) || (druckoptionLehrerListeSchuelerLeistungsdaten.value === 4));
				reportingParameter.einzelausgabeDetaildaten = false;
				reportingParameter.vorlageParameter = vpLeistungsdaten.value;
				break;
			default:
				return;
		}

		reportingParameter.duplexdruck = ((currentAction.value === 'druckLehrerListeSchuelerLeistungsdaten') && ((druckoptionLehrerListeSchuelerLeistungsdaten.value === 3) || (druckoptionLehrerListeSchuelerLeistungsdaten.value === 4)));
		loading.value = true;
		const { data, name } = await props.getPDF(reportingParameter);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
		loading.value = false;
	}

	async function sendPdfByEmail() {
		if (stundenplanAuswahl.value === undefined) {
			return;
		}

		const listeIdsLehrer = new ArrayList<number>();
		for (const lehrer of props.lehrerListeManager().liste.auswahl()) {
			listeIdsLehrer.add(lehrer.id);
		}

		const reportingParameter = new ReportingParameter();
		reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN.getBezeichnung();
		reportingParameter.ausgabeformat = ReportingAusgabeformat.EMAIL.getId();
		reportingParameter.idsHauptdaten = ListUtils.create1(stundenplanAuswahl.value.id);
		reportingParameter.idsDetaildaten = listeIdsLehrer;
		reportingParameter.einzelausgabeHauptdaten = false;
		reportingParameter.einzelausgabeDetaildaten = true;

		const emailDaten = new ReportingEMailDaten();
		emailDaten.empfaengerTyp = ReportingEMailEmpfaengerTyp.LEHRER.getId();
		emailDaten.istPrivateEmailAlternative = istPrivateEmailAlternative.value;
		emailDaten.betreff = (((emailBetreff.value.trim().length) === 0) ? ("Stundenplan " + stundenplanAuswahl.value.bezeichnung) : emailBetreff.value);
		emailDaten.text = (((emailText.value.trim().length) === 0) ? ("Im Anhang dieser E-Mail ist der Stundenplan " + stundenplanAuswahl.value.bezeichnung + " enthalten.") : emailText.value);
		reportingParameter.eMailDaten = emailDaten;
		reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN.getVorlageParameterList());
		for (const vp of reportingParameter.vorlageParameter) {
			switch (vp.name) {
				case "mitPausenzeiten":
					vp.wert = option2.value.toString();
					break;
				case "mitPausenaufsichten":
					vp.wert = option4.value.toString();
					break;
				case "mitFachkuerzelStattFachbezeichnung":
					vp.wert = option8.value.toString();
					break;
			}
		}

		loading.value = true;
		const result = await props.sendEMail(reportingParameter);
		statusAction.value = result.success;
		logs.value = result.log;
		loading.value = false;
	}

	async function entferneLehrer() {
		loading.value = true;
		[statusAction.value, logs.value] = await props.deleteLehrer();
		loading.value = false;
	}

	function setCurrentAction(newAction: Action, open: boolean) {
		if ((newAction !== currentAction.value) && !open) {
			return;
		}

		option2.value = false;
		option4.value = false;
		option8.value = false;
		druckoptionLehrerStundenplan.value = 1;
		sortieroptionLehrerStundenplan.value = 1;
		emailBetreff.value = '';
		emailText.value = '';
		istPrivateEmailAlternative.value = false;

		currentAction.value = open ? newAction : "";
	}

	function clearLog() {
		loading.value = false;
		logs.value = undefined;
		statusAction.value = undefined;
	}

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
