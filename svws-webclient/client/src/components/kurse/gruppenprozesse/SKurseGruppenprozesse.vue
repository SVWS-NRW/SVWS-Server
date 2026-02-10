<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div v-else class="flex flex-col gap-4">
			<!-- Karte: Kursliste Schüler-Kontaktdaten/Erzieher drucken/versenden -->
			<ui-card v-if="hatKompetenzDruckenSchuelerIndividualdaten" icon="i-ri-printer-line" title="Kursliste drucken oder versenden" subtitle="Eine Liste mit den Daten der Schülerinnen und Schüler der ausgewählten Kurse drucken oder versenden."
				:is-open="currentAction === 'druckKursListeSchuelerKontaktdatenErzieher'" @update:is-open="isOpen => setCurrentAction('druckKursListeSchuelerKontaktdatenErzieher', isOpen)">
				<svws-ui-input-wrapper :grid="4" class="p-2">
					<div class="text-left col-span-4 mb-2">
						<p class="font-bold underline">Elemente der Liste:</p>
					</div>
					<div class="text-left">
						<svws-ui-checkbox v-model="option1" name="mitSchuelerKlasse">Klasse</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option2" name="nurSchuelerRufname">Nur Rufname</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option4" name="mitSchuelerGeschlecht">Geschlecht</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option8" name="mitSchuelerGebDat">Geburtsdatum</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option16" name="mitSchuelerStaat">Staatsangehörigkeit</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option32" name="mitSchuelerAnschrift">Anschrift</svws-ui-checkbox><br>
					</div>
					<div class="text-left">
						<svws-ui-checkbox v-model="option64" name="mitSchuelerTelefonPrivat">Telefon</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option128" name="mitSchuelerEmailSchule">Schulische E-Mail</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option256" name="mitSchuelerEmailPrivat">Private E-Mail</svws-ui-checkbox><br>
					</div>
					<div class="text-left">
						<svws-ui-checkbox v-model="option512" name="mitSchuelerTelefonkontakte">Telefonische Kontakte</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option1024" name="mitErzieher">Erzieher</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option2048" name="mitErzieherAnschrift">Erzieher Anschrift</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option4096" name="mitErzieherEmailPrivat">Erzieher E-Mail</svws-ui-checkbox><br>
					</div>
					<div class="text-left">
						<svws-ui-button @click="selectAll" class="mt-4">
							<span class="icon i-ri-checkbox-line" />
							Alle auswählen
						</svws-ui-button>
						<svws-ui-button @click="deselectAll" class="mt-4">
							<span class="icon i-ri-checkbox-blank-line" />
							Alle abwählen
						</svws-ui-button>
					</div>
					<div class="text-left col-span-4 mb-2">
						<br><p class="font-bold underline">Optionen zur Druckausgabe:</p>
						<svws-ui-radio-group class="grid grid-cols-4 p-2 items-start">
							<svws-ui-radio-option :value="1" v-model="druckoptionListeSchuelerKontaktdatenErzieher" name="druckoptionListeSchuelerKontaktdatenErzieherGesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="druckoptionListeSchuelerKontaktdatenErzieher" name="druckoptionListeSchuelerKontaktdatenErzieherEinzelausdruckEinseitig" label="Einzelausdruck einseitig" />
							<svws-ui-radio-option :value="3" v-model="druckoptionListeSchuelerKontaktdatenErzieher" name="druckoptionListeSchuelerKontaktdatenErzieherGesamtausdruckDuplex" label="Gesamtausdruck duplex" />
							<svws-ui-radio-option :value="4" v-model="druckoptionListeSchuelerKontaktdatenErzieher" name="druckoptionListeSchuelerKontaktdatenErzieherEinzelausdruckDuplex" label="Einzelausdruck duplex" />
						</svws-ui-radio-group>
					</div>
					<div class="text-left col-span-4">
						<svws-ui-button :disabled="isPrintDisabled" @click="downloadPDF" :is-loading="loading" class="mt-4">
							<svws-ui-spinner v-if="loading" spinning />
							<span v-else class="icon i-ri-printer-line" />
							Drucken
						</svws-ui-button>
					</div>
					<!-- E-Mail-Eingabefelder -->
					<div class="text-left col-span-4 mb-4" v-if="ServerMode.DEV.checkServerMode(serverMode)">
						<br><p class="font-bold mb-2">Die Kursliste mit den Kontaktdaten als E-Mail an die Kursleitungen versenden.</p>
						<div class="flex flex-col gap-2">
							<input type="text" v-model="emailBetreff" placeholder="Betreff eingeben" class="w-full border rounded px-2 py-1">
							<textarea v-model="emailText" rows="5" placeholder="E-Mail-Text eingeben" class="w-full border rounded px-2 py-1" />
							<svws-ui-checkbox v-model="istPrivateEmailAlternative" name="istPrivateEmailAlternative">
								Private E-Mail verwenden, wenn keine schulische E-Mail-Adresse vorhanden ist.
							</svws-ui-checkbox>
						</div>
					</div>
					<div class="text-left col-span-4" v-if="ServerMode.DEV.checkServerMode(serverMode)">
						<svws-ui-button :disabled="isEmailDisabled" @click="sendPdfByEmail" :is-loading="loading" class="mt-4">
							<svws-ui-spinner v-if="loading" spinning />
							<span v-else class="icon i-ri-mail-send-line" />
							E-Mail senden
						</svws-ui-button>
					</div>
					<!-- Ende: E-Mail-Eingabefelder -->
				</svws-ui-input-wrapper>
			</ui-card>
			<ui-card v-if="hatKompetenzDruckenSchuelerLeistungsdaten" icon="i-ri-printer-line" title="Leistungsübersicht drucken" subtitle="Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der ausgewählten Kurse drucken"
				:is-open="currentAction === 'druckKursListeSchuelerLeistungsdaten'" @update:is-open="isOpen => setCurrentAction('druckKursListeSchuelerLeistungsdaten', isOpen)">
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
							<svws-ui-radio-option :value="1" v-model="druckoptionKursListeSchuelerLeistungsdaten" name="druckoptionKursListeSchuelerLeistungsdatenGesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="druckoptionKursListeSchuelerLeistungsdaten" name="druckoptionKursListeSchuelerLeistungsdatenEinzelausdruckEinseitig" label="Einzelausdruck einseitig" />
							<svws-ui-radio-option :value="3" v-model="druckoptionKursListeSchuelerLeistungsdaten" name="druckoptionKursListeSchuelerLeistungsdatenGesamtausdruckDuplex" label="Gesamtausdruck duplex" />
							<svws-ui-radio-option :value="4" v-model="druckoptionKursListeSchuelerLeistungsdaten" name="druckoptionKursListeSchuelerLeistungsdatenEinzelausdruckDuplex" label="Einzelausdruck duplex" />
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
			<!-- Karte: Löschen (bestehende Funktionalität, DEV) -->
			<ui-card v-if="ServerMode.DEV.checkServerMode(serverMode) && hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen" subtitle="Ausgewählte Kurse werden gelöscht."
				:is-open="currentAction === 'delete'" @update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="preConditionCheck.success">Alle ausgewählten Kurse sind bereit zum Löschen.</span>
					<template v-else v-for="message, i in preConditionCheck.logs" :key="i">
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
	import type { List } from "@core";
	import { ServerMode, ReportingAusgabeformat, ReportingEMailDaten, ReportingEMailEmpfaengerTyp, ReportingVorlageParameterTyp, ArrayList, BenutzerKompetenz, ReportingParameter, ReportingReportvorlage } from "@core";

	type Action = 'druckKursListeSchuelerKontaktdatenErzieher' | 'druckKursListeSchuelerLeistungsdaten' | 'delete' | '';

	const props = defineProps<KurseGruppenprozesseProps>();

	const currentAction = ref<Action>('');
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();
	const status = ref<boolean | undefined>();

	const hatKompetenzDrucken = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN) || props.benutzerKompetenzen.has(BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN)));
	const hatKompetenzDruckenSchuelerIndividualdaten = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzDruckenSchuelerLeistungsdaten = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const hatIrgendwelcheKompetenzen = computed(() => hatKompetenzDrucken.value || hatKompetenzLoeschen.value || hatKompetenzDruckenSchuelerIndividualdaten.value);

	// Auswahlabhängige States
	const isPrintDisabled = computed<boolean>(() => !props.manager().liste.auswahlExists() || loading.value);
	const isEmailDisabled = computed<boolean>(() => isPrintDisabled.value || ((emailBetreff.value.trim().length) === 0) || ((emailText.value.trim().length) === 0));

	// E-Mail Felder
	const emailBetreff = ref<string>("");
	const emailText = ref<string>("");
	const istPrivateEmailAlternative = ref<boolean>(false);

	// Detail-Optionen und Druckoption analog
	const option1 = ref(false);
	const option2 = ref(false);
	const option4 = ref(false);
	const option8 = ref(false);
	const option16 = ref(false);
	const option32 = ref(false);
	const option64 = ref(false);
	const option128 = ref(false);
	const option256 = ref(false);
	const option512 = ref(false);
	const option1024 = ref(false);
	const option2048 = ref(false);
	const option4096 = ref(false);
	const druckoptionListeSchuelerKontaktdatenErzieher = ref(1);
	const druckoptionKursListeSchuelerLeistungsdaten = ref(1);

	const vpLeistungsdaten = ref(new ArrayList(ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN.getVorlageParameterList()));

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
		option1.value = false;
		option2.value = false;
		option4.value = false;
		option8.value = false;
		option16.value = false;
		option32.value = false;
		option64.value = false;
		option128.value = false;
		option256.value = false;
		option512.value = false;
		option1024.value = false;
		option2048.value = false;
		option4096.value = false;
		druckoptionListeSchuelerKontaktdatenErzieher.value = 1;
		emailBetreff.value = '';
		emailText.value = '';
		istPrivateEmailAlternative.value = false;

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

	// Druck analog implementiert: Kontaktdaten/Erzieher für Kurse
	async function downloadPDF() {
		const reportingParameter = new ReportingParameter();
		const listeIdsKurse = new ArrayList<number>();
		for (const kurs of props.manager().liste.auswahl()) {
			listeIdsKurse.add(kurs.id);
		}

		switch (currentAction.value) {
			case 'druckKursListeSchuelerKontaktdatenErzieher':
				reportingParameter.reportvorlage = ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsKurse;
				reportingParameter.einzelausgabeHauptdaten = ((druckoptionListeSchuelerKontaktdatenErzieher.value === 2) || (druckoptionListeSchuelerKontaktdatenErzieher.value === 4));
				reportingParameter.einzelausgabeDetaildaten = false;
				reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER.getVorlageParameterList());
				for (const vp of reportingParameter.vorlageParameter) {
					switch (vp.name) {
						case "mitSchuelerKlasse":
							vp.wert = option1.value.toString();
							break;
						case "nurSchuelerRufname":
							vp.wert = option2.value.toString();
							break;
						case "mitSchuelerGeschlecht":
							vp.wert = option4.value.toString();
							break;
						case "mitSchuelerGebDat":
							vp.wert = option8.value.toString();
							break;
						case "mitSchuelerStaat":
							vp.wert = option16.value.toString();
							break;
						case "mitSchuelerAnschrift":
							vp.wert = option32.value.toString();
							break;
						case "mitSchuelerTelefonPrivat":
							vp.wert = option64.value.toString();
							break;
						case "mitSchuelerEmailSchule":
							vp.wert = option128.value.toString();
							break;
						case "mitSchuelerEmailPrivat":
							vp.wert = option256.value.toString();
							break;
						case "mitSpalteSchuelerTelefonKontakte":
							vp.wert = option512.value.toString();
							break;
						case "mitErzieher":
							vp.wert = option1024.value.toString();
							break;
						case "mitErzieherAnschrift":
							vp.wert = option2048.value.toString();
							break;
						case "mitErzieherEmailPrivat":
							vp.wert = option4096.value.toString();
							break;
					}
				}
				break;
			case 'druckKursListeSchuelerLeistungsdaten':
				reportingParameter.reportvorlage = ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_LEISTUNGSDATEN.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsKurse;
				reportingParameter.einzelausgabeHauptdaten = ((druckoptionKursListeSchuelerLeistungsdaten.value === 2) || (druckoptionKursListeSchuelerLeistungsdaten.value === 4));
				reportingParameter.einzelausgabeDetaildaten = false;
				reportingParameter.vorlageParameter = vpLeistungsdaten.value;
				break;
			default:
				return;
		}
		reportingParameter.duplexdruck = (currentAction.value === 'druckKursListeSchuelerKontaktdatenErzieher')
			? ((druckoptionListeSchuelerKontaktdatenErzieher.value === 3) || (druckoptionListeSchuelerKontaktdatenErzieher.value === 4))
			: ((druckoptionKursListeSchuelerLeistungsdaten.value === 3) || (druckoptionKursListeSchuelerLeistungsdaten.value === 4));
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

	// E-Mail analog implementiert
	async function sendPdfByEmail() {
		const listeIdsKurse = new ArrayList<number>();
		for (const kurs of props.manager().liste.auswahl()) {
			listeIdsKurse.add(kurs.id);
		}

		const reportingParameter = new ReportingParameter();
		reportingParameter.ausgabeformat = ReportingAusgabeformat.EMAIL.getId();

		const emailDaten = new ReportingEMailDaten();
		emailDaten.empfaengerTyp = ReportingEMailEmpfaengerTyp.KURSLEHRER.getId();
		emailDaten.istPrivateEmailAlternative = istPrivateEmailAlternative.value;

		if (currentAction.value === 'druckKursListeSchuelerKontaktdatenErzieher') {
			reportingParameter.reportvorlage = ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER.getBezeichnung();
			reportingParameter.idsHauptdaten = listeIdsKurse;
			reportingParameter.einzelausgabeHauptdaten = true;
			reportingParameter.einzelausgabeDetaildaten = false;
			reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.KURSE_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER.getVorlageParameterList());
			for (const vp of reportingParameter.vorlageParameter) {
				switch (vp.name) {
					case "mitSchuelerKlasse":
						vp.wert = option1.value.toString();
						break;
					case "nurSchuelerRufname":
						vp.wert = option2.value.toString();
						break;
					case "mitSchuelerGeschlecht":
						vp.wert = option4.value.toString();
						break;
					case "mitSchuelerGebDat":
						vp.wert = option8.value.toString();
						break;
					case "mitSchuelerStaat":
						vp.wert = option16.value.toString();
						break;
					case "mitSchuelerAnschrift":
						vp.wert = option32.value.toString();
						break;
					case "mitSchuelerTelefonPrivat":
						vp.wert = option64.value.toString();
						break;
					case "mitSchuelerEmailSchule":
						vp.wert = option128.value.toString();
						break;
					case "mitSchuelerEmailPrivat":
						vp.wert = option256.value.toString();
						break;
					case "mitSpalteSchuelerTelefonKontakte":
						vp.wert = option512.value.toString();
						break;
					case "mitErzieher":
						vp.wert = option1024.value.toString();
						break;
					case "mitErzieherAnschrift":
						vp.wert = option2048.value.toString();
						break;
					case "mitErzieherEmailPrivat":
						vp.wert = option4096.value.toString();
						break;
				}
			}
			emailDaten.betreff = (((emailBetreff.value.trim().length) === 0) ? ("Kursliste mit Kontaktdaten") : emailBetreff.value);
			emailDaten.text = (((emailText.value.trim().length) === 0) ? ("Im Anhang dieser E-Mail ist die Kursliste mit Kontaktdaten enthalten.") : emailText.value);
		} else {
			return;
		}
		reportingParameter.eMailDaten = emailDaten;

		reportingParameter.duplexdruck = ((druckoptionListeSchuelerKontaktdatenErzieher.value === 3) || (druckoptionListeSchuelerKontaktdatenErzieher.value === 4));
		loading.value = true;
		const result = await props.sendEMail(reportingParameter);
		status.value = result.success;
		logs.value = result.log;
		loading.value = false;
	}

	async function selectAll() {
		option1.value = true;
		option2.value = true;
		option4.value = true;
		option8.value = true;
		option16.value = true;
		option32.value = true;
		option64.value = true;
		option128.value = true;
		option256.value = true;
		option512.value = true;
		option1024.value = true;
		option2048.value = true;
		option4096.value = true;
	}

	async function deselectAll() {
		option1.value = false;
		option2.value = false;
		option4.value = false;
		option8.value = false;
		option16.value = false;
		option32.value = false;
		option64.value = false;
		option128.value = false;
		option256.value = false;
		option512.value = false;
		option1024.value = false;
		option2048.value = false;
		option4096.value = false;
	}
</script>
