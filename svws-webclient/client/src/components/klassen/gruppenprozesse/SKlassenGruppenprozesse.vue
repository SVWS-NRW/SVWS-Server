<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<div v-else class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDruckenSchuelerIndividualdaten" icon="i-ri-printer-line" title="Klassenliste drucken oder versenden" subtitle="Eine Liste mit den Daten der Schülerinnen und Schüler der ausgewählten Klassen drucken oder versenden."
				:is-open="currentAction === 'druckKlasseListeSchuelerKontaktdatenErzieher'" @update:is-open="isOpen => setCurrentAction('druckKlasseListeSchuelerKontaktdatenErzieher', isOpen)">
				<svws-ui-input-wrapper :grid="4" class="p-2">
					<div class="text-left col-span-4 mb-2">
						<p class="font-bold underline">Elemente der Liste:</p>
					</div>
					<div class="text-left">
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
							<svws-ui-radio-option :value="1" v-model="druckoptionKlasseListeSchuelerKontaktdatenErzieher" name="druckoptionKlasseListeSchuelerKontaktdatenErzieherGesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="druckoptionKlasseListeSchuelerKontaktdatenErzieher" name="druckoptionKlasseListeSchuelerKontaktdatenErzieherEinzelausdruckEinseitig" label="Einzelausdruck einseitig" />
							<svws-ui-radio-option :value="3" v-model="druckoptionKlasseListeSchuelerKontaktdatenErzieher" name="druckoptionKlasseListeSchuelerKontaktdatenErzieherGesamtausdruckDuplex" label="Gesamtausdruck duplex" />
							<svws-ui-radio-option :value="4" v-model="druckoptionKlasseListeSchuelerKontaktdatenErzieher" name="druckoptionKlasseListeSchuelerKontaktdatenErzieherEinzelausdruckDuplex" label="Einzelausdruck duplex" />
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
						<br><p class="font-bold mb-2">Die Klassenliste mit den Kontaktdaten als E-Mail an die Klassenleitungen versenden.</p>
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
			<ui-card v-if="hatKompetenzDruckenSchuelerLeistungsdaten" icon="i-ri-printer-line" title="Leistungsübersicht drucken" subtitle="Eine Liste mit den Leistungsdaten der Schülerinnen und Schüler der ausgewählten Klassen drucken"
				:is-open="currentAction === 'druckKlasseListeSchuelerLeistungsdaten'" @update:is-open="isOpen => setCurrentAction('druckKlasseListeSchuelerLeistungsdaten', isOpen)">
				<svws-ui-input-wrapper :grid="2" class="p-2">
					<div class="text-left mb-2">
						<p class="font-bold underline">Fächer der Liste einschränken:</p>
						<svws-ui-checkbox v-model="option2" name="nurZeugnisRelevant">Nur zeugnisrelevante Fächer</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option4" name="nurPruefungsordnungRelevant">Nur prüfungsordnungsrelevante Fächer</svws-ui-checkbox>
					</div>
					<div class="text-left mb-2">
						<p class="font-bold underline">Sortierung der Fächer:</p>
						<svws-ui-radio-group class="grid grid-cols-1 p-2 items-start">
							<svws-ui-radio-option :value="1" v-model="drucksortierungKlasseListeSchuelerLeistungsdaten" name="drucksortierungKlasseListeSchuelerLeistungsdatenFach" label="Fächersortierung" />
							<svws-ui-radio-option :value="2" v-model="drucksortierungKlasseListeSchuelerLeistungsdaten" name="drucksortierungKlasseListeSchuelerLeistungsdatenGost" label="GOSt-Sortierung" />
							<svws-ui-radio-option :value="3" v-model="drucksortierungKlasseListeSchuelerLeistungsdaten" name="drucksortierungKlasseListeSchuelerLeistungsdatenFachkuerzel" label="Fachkürzel" />
						</svws-ui-radio-group>
					</div>
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
							<svws-ui-radio-option :value="1" v-model="druckoptionKlasseListeSchuelerLeistungsdaten" name="druckoptionKlasseListeSchuelerLeistungsdatenGesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="druckoptionKlasseListeSchuelerLeistungsdaten" name="druckoptionKlasseListeSchuelerLeistungsdatenEinzelausdruckEinseitig" label="Einzelausdruck einseitig" />
							<svws-ui-radio-option :value="3" v-model="druckoptionKlasseListeSchuelerLeistungsdaten" name="druckoptionKlasseListeSchuelerLeistungsdatenGesamtausdruckDuplex" label="Gesamtausdruck duplex" />
							<svws-ui-radio-option :value="4" v-model="druckoptionKlasseListeSchuelerLeistungsdaten" name="druckoptionKlasseListeSchuelerLeistungsdatenEinzelausdruckDuplex" label="Einzelausdruck duplex" />
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
			<ui-card v-if="hatKompetenzDruckenStundenplan && (mapStundenplaene.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken oder versenden" subtitle="Die Stundenpläne der ausgewählten Klassen drucken oder versenden."
				:is-open="currentAction === 'druckKlasseStundenplan'" @update:is-open="isOpen => setCurrentAction('druckKlasseStundenplan', isOpen)">
				<svws-ui-input-wrapper :grid="2" class="p-2">
					<div>
						<svws-ui-select title="Stundenplan" v-model="stundenplanAuswahl" :items="mapStundenplaene.values()"
							:item-text="s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')'" />
					</div>
					<div />
					<div class="text-left">
						<svws-ui-checkbox v-model="option2" name="Pausenzeiten">Pausenzeiten anzeigen</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option4" name="Fachbezeichnungen">Fach- statt Kursbezeichnung verwenden (nicht Sek-II)</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option8" name="Fachkuerzel">Fachkürzel statt Fachbezeichnung verwenden</svws-ui-checkbox>
					</div>
					<div class="text-left">
						<p class="font-bold underline mb-2">Optionen zur Druckausgabe:</p>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="1" v-model="druckoptionKlasseStundenplan" name="druckoptionKlasseStundenplanGesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="druckoptionKlasseStundenplan" name="druckoptionKlasseStundenplanEinzelausdruckEinseitig" label="Einzelausdruck einseitig" />
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
					<div class="text-left col-span-2 mb-4" v-if="ServerMode.DEV.checkServerMode(serverMode)">
						<br><p class="font-bold mb-2">Den Klassenstundenplan als E-Mail an die Klassenleitungen versenden.</p>
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
				</svws-ui-input-wrapper>
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
			<log-box :logs :status="status">
				<template #button>
					<svws-ui-button v-if="status !== undefined" type="transparent" @click="clearLog" title="Log verwerfen">Log verwerfen</svws-ui-button>
				</template>
			</log-box>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { KlassenGruppenprozesseProps } from "./SKlassenGruppenprozesseProps";
	import type { StundenplanListeEintrag, List } from "@core";
	import { ReportingFilterDefinition, ReportingFilterKriterium, ReportingFilterOperation,	ReportingFilterVerknuepfung, ReportingFilterEintrag, ReportingSortierungDefinition,	ReportingVorlageParameterTyp,
		ServerMode, ReportingAusgabeformat, ReportingEMailDaten, ReportingEMailEmpfaengerTyp, ArrayList, BenutzerKompetenz, DateUtils, ReportingParameter, ReportingReportvorlage, ListUtils } from "@core";

	type Action = 'druckKlasseListeSchuelerKontaktdatenErzieher' | 'druckKlasseListeSchuelerLeistungsdaten' | 'druckKlasseStundenplan' | 'delete' | '';

	const props = defineProps<KlassenGruppenprozesseProps>();

	const hatKompetenzDrucken = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN) || props.benutzerKompetenzen.has(BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN)));
	const hatKompetenzDruckenStundenplan = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzDruckenSchuelerIndividualdaten = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzDruckenSchuelerLeistungsdaten = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const hatIrgendwelcheKompetenzen = computed(() => hatKompetenzDrucken.value || hatKompetenzLoeschen.value || hatKompetenzDruckenStundenplan.value || hatKompetenzDruckenSchuelerIndividualdaten.value || hatKompetenzDruckenSchuelerLeistungsdaten.value);

	const isPrintDisabled = computed<boolean>(() => !props.manager().liste.auswahlExists() || loading.value);
	const isPrintStundenplanDisabled = computed<boolean>(() => isPrintDisabled.value || stundenplanAuswahl.value === undefined);
	const isEmailDisabled = computed<boolean>(() => isPrintDisabled.value || ((emailBetreff.value.trim().length) === 0) || ((emailText.value.trim().length) === 0));
	const isEmailStundenplanDisabled = computed<boolean>(() => isEmailDisabled.value || ((emailBetreff.value.trim().length) === 0) || ((emailText.value.trim().length) === 0));

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
		druckoptionKlasseListeSchuelerKontaktdatenErzieher.value = 1;
		druckoptionKlasseListeSchuelerLeistungsdaten.value = 1;
		druckoptionKlasseStundenplan.value = 1;
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

	async function entferneKlassen() {
		loading.value = true;

		const [delStatus, logMessages] = await props.deleteKlassen();
		logs.value = logMessages;
		status.value = delStatus;
		currentAction.value = '';

		loading.value = false;
	}

	const emailBetreff = ref<string>("");
	const emailText = ref<string>("");
	const istPrivateEmailAlternative = ref<boolean>(false);

	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
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
	const druckoptionKlasseListeSchuelerKontaktdatenErzieher = ref(1);
	const druckoptionKlasseListeSchuelerLeistungsdaten = ref(1);
	const drucksortierungKlasseListeSchuelerLeistungsdaten = ref(1);
	const druckoptionKlasseStundenplan = ref(1);

	const vpLeistungsdaten = ref(new ArrayList(ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN.getVorlageParameterList()));

	async function downloadPDF() {
		const reportingParameter = new ReportingParameter();
		const listeIdsKlassen = new ArrayList<number>();
		for (const klasse of props.manager().liste.auswahl()) {
			listeIdsKlassen.add(klasse.id);
		}
		const sortierungReportingFach = new ReportingSortierungDefinition();
		switch (currentAction.value) {
			case 'druckKlasseListeSchuelerKontaktdatenErzieher':
				reportingParameter.reportvorlage = ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsKlassen;
				reportingParameter.einzelausgabeHauptdaten = ((druckoptionKlasseListeSchuelerKontaktdatenErzieher.value === 2) || (druckoptionKlasseListeSchuelerKontaktdatenErzieher.value === 4));
				reportingParameter.einzelausgabeDetaildaten = false;
				reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER.getVorlageParameterList());
				for (const vp of reportingParameter.vorlageParameter) {
					switch (vp.name) {
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
			case 'druckKlasseListeSchuelerLeistungsdaten':
				reportingParameter.reportvorlage = ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_LEISTUNGSDATEN.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsKlassen;
				reportingParameter.einzelausgabeHauptdaten = ((druckoptionKlasseListeSchuelerLeistungsdaten.value === 2) || (druckoptionKlasseListeSchuelerLeistungsdaten.value === 4));
				reportingParameter.einzelausgabeDetaildaten = false;
				reportingParameter.vorlageParameter = vpLeistungsdaten.value;

				// Filterung der Fächer
				if (option2.value || option4.value) {
					const filterReportingFach = new ReportingFilterDefinition();
					filterReportingFach.typ = "ReportingFach";
					const filterReportingFachKriterium = new ReportingFilterKriterium();
					filterReportingFachKriterium.verknuepfung = ReportingFilterVerknuepfung.AND.getId();

					if (option2.value) {
						const e1 = new ReportingFilterEintrag();
						e1.attribut = "aufZeugnis";
						e1.operation = ReportingFilterOperation.EQUAL.getId();
						e1.werte.add("true");
						filterReportingFachKriterium.eintraege.add(e1);
					}

					if (option4.value) {
						const e2 = new ReportingFilterEintrag();
						e2.attribut = "istPruefungsordnungsRelevant";
						e2.operation = ReportingFilterOperation.EQUAL.getId();
						e2.werte.add("true");
						filterReportingFachKriterium.eintraege.add(e2);
					}

					filterReportingFach.kriterien.add(filterReportingFachKriterium);
					reportingParameter.filterDefinitionen.add(filterReportingFach);
				}

				// Sortierung
				reportingParameter.sortierungDefinitionen = new ArrayList();
				sortierungReportingFach.typ = "ReportingFach";

				if (drucksortierungKlasseListeSchuelerLeistungsdaten.value === 1) {
					// Fächersortierung ist Standardsortierung
					sortierungReportingFach.verwendeStandardsortierung = true;
				} else if (drucksortierungKlasseListeSchuelerLeistungsdaten.value === 2) {
					// GOSt-Sortierung
					sortierungReportingFach.verwendeStandardsortierung = false;
					sortierungReportingFach.attribute.add("gostSortierung");
				} else if (drucksortierungKlasseListeSchuelerLeistungsdaten.value === 3) {
					// Sortierung nach Fachkürzel
					sortierungReportingFach.verwendeStandardsortierung = false;
					sortierungReportingFach.attribute.add("kuerzel");
				}
				reportingParameter.sortierungDefinitionen.add(sortierungReportingFach);
				break;
			case 'druckKlasseStundenplan':
				if (stundenplanAuswahl.value === undefined) {
					return;
				}
				reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN.getBezeichnung();
				reportingParameter.idsHauptdaten = ListUtils.create1(stundenplanAuswahl.value.id);
				reportingParameter.idsDetaildaten = listeIdsKlassen;
				reportingParameter.einzelausgabeHauptdaten = false;
				reportingParameter.einzelausgabeDetaildaten = (druckoptionKlasseStundenplan.value === 2);
				reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN.getVorlageParameterList());
				for (const vp of reportingParameter.vorlageParameter) {
					switch (vp.name) {
						case "mitPausenzeiten":
							vp.wert = option2.value.toString();
							break;
						case "mitFachStattKursbezeichnung":
							vp.wert = option4.value.toString();
							break;
						case "mitFachkuerzelStattFachbezeichnung":
							vp.wert = option8.value.toString();
							break;
					}
				}
				break;
			default:
				return;
		}
		reportingParameter.duplexdruck = ((druckoptionKlasseListeSchuelerKontaktdatenErzieher.value === 3) || (druckoptionKlasseListeSchuelerKontaktdatenErzieher.value === 4));
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
		const listeIdsKlassen = new ArrayList<number>();
		for (const klasse of props.manager().liste.auswahl()) {
			listeIdsKlassen.add(klasse.id);
		}

		const reportingParameter = new ReportingParameter();
		reportingParameter.ausgabeformat = ReportingAusgabeformat.EMAIL.getId();
		reportingParameter.ausgabeformat = ReportingAusgabeformat.EMAIL.getId();

		const emailDaten = new ReportingEMailDaten();
		emailDaten.empfaengerTyp = ReportingEMailEmpfaengerTyp.KLASSENLEHRER.getId();
		emailDaten.istPrivateEmailAlternative = istPrivateEmailAlternative.value;

		switch (currentAction.value) {
			case 'druckKlasseListeSchuelerKontaktdatenErzieher':
				reportingParameter.reportvorlage = ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsKlassen;
				reportingParameter.einzelausgabeHauptdaten = true;
				reportingParameter.einzelausgabeDetaildaten = false;
				emailDaten.empfaengerTyp = ReportingEMailEmpfaengerTyp.KLASSENLEHRER.getId();
				emailDaten.istPrivateEmailAlternative = istPrivateEmailAlternative.value;
				reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.KLASSEN_V_LISTE_SCHUELER_KONTAKTDATENERZIEHER.getVorlageParameterList());
				for (const vp of reportingParameter.vorlageParameter) {
					switch (vp.name) {
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
				emailDaten.betreff = ((emailBetreff.value.trim().length === 0) ? ("Klassenliste mit Kontaktdaten") : emailBetreff.value);
				emailDaten.text = ((emailText.value.trim().length === 0) ? ("Im Anhang dieser E-Mail ist die Klassenliste mit Kontaktdaten enthalten.") : emailText.value);
				break;
			case 'druckKlasseStundenplan':
				if (stundenplanAuswahl.value === undefined) {
					return;
				}
				reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN.getBezeichnung();
				reportingParameter.idsHauptdaten = ListUtils.create1(stundenplanAuswahl.value.id);
				reportingParameter.idsDetaildaten = listeIdsKlassen;
				reportingParameter.einzelausgabeHauptdaten = false;
				reportingParameter.einzelausgabeDetaildaten = true;
				reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN.getVorlageParameterList());
				for (const vp of reportingParameter.vorlageParameter) {
					switch (vp.name) {
						case "mitPausenzeiten":
							vp.wert = option2.value.toString();
							break;
						case "mitFachStattKursbezeichnung":
							vp.wert = option4.value.toString();
							break;
						case "mitFachkuerzelStattFachbezeichnung":
							vp.wert = option8.value.toString();
							break;
					}
				}
				emailDaten.betreff = (((emailBetreff.value.trim().length) === 0) ? ("Stundenplan " + stundenplanAuswahl.value.bezeichnung) : emailBetreff.value);
				emailDaten.text = (((emailText.value.trim().length) === 0) ? ("Im Anhang dieser E-Mail ist der Stundenplan " + stundenplanAuswahl.value.bezeichnung + " enthalten.") : emailText.value);
				break;
			default:
				return;
		}
		reportingParameter.eMailDaten = emailDaten;
		reportingParameter.duplexdruck = ((druckoptionKlasseStundenplan.value === 3) || (druckoptionKlasseStundenplan.value === 4));
		loading.value = true;
		const result = await props.sendEMail(reportingParameter);
		status.value = result.success;
		logs.value = result.log;
		loading.value = false;
	}

	async function selectAll() {
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
