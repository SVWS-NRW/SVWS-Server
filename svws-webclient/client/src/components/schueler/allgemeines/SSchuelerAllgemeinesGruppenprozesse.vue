<template>
	<div class="page page-grid-cards">
		<div v-if="!hatIrgendwelcheKompetenzen">
			Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
		</div>
		<svws-ui-input-wrapper v-else class="flex flex-col gap-4">
			<ui-card v-if="hatKompetenzDruckenSchuelerIndividualdaten" icon="i-ri-printer-line" title="Schülerliste drucken" subtitle="Drucke eine Liste mit den Daten der ausgewählten Schülerinnen und Schüler."
				:is-open="currentAction === 'druckSchuelerListeKontaktdatenErzieher'" @update:is-open="isOpen => setCurrentAction('druckSchuelerListeKontaktdatenErzieher', isOpen)">
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
					<div class="text-left col-span-2 mb-2">
						<br><p class="font-bold underline">Optionen zur Druckausgabe:</p>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="1" v-model="druckoptionSchuelerListeKontaktdatenErzieher" name="druckoptionSchuelerListeKontaktdatenErzieherGesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="3" v-model="druckoptionSchuelerListeKontaktdatenErzieher" name="druckoptionSchuelerListeKontaktdatenErzieherGesamtausdruckDuplex" label="Gesamtausdruck duplex" />
						</svws-ui-radio-group>
					</div>
					<div class="text-left col-span-2 mb-2">
						<br><p class="font-bold underline">Sortierung:</p>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="1" v-model="sortieroptionSchuelerListeKontaktdatenErzieher" name="sortieroptionSchuelerListeKontaktdatenErzieherName" label="Standard (Nachname, Vorname(n))" />
							<svws-ui-radio-option :value="2" v-model="sortieroptionSchuelerListeKontaktdatenErzieher" name="sortieroptionSchuelerListeKontaktdatenErzieherKlasseName" label="Klasse, Nachname, Vorname(n)" />
						</svws-ui-radio-group>
					</div>
					<div class="text-left col-span-4">
						<svws-ui-button :disabled="isPrintDisabled" @click="downloadPDF" :is-loading="loading" class="mt-4">
							<svws-ui-spinner v-if="loading" spinning />
							<span v-else class="icon i-ri-printer-line" />
							Drucken
						</svws-ui-button>
					</div>
				</svws-ui-input-wrapper>
			</ui-card>
			<ui-card v-if="hatKompetenzDruckenSchuelerIndividualdaten" icon="i-ri-printer-line" title="Schulbescheinigung drucken" subtitle="Drucke eine Schulbescheinigung für die ausgewählten Schülerinnen und Schüler."
				:is-open="currentAction === 'druckSchuelerSchulbescheinigung'" @update:is-open="isOpen => setCurrentAction('druckSchuelerSchulbescheinigung', isOpen)">
				<svws-ui-input-wrapper :grid="2" class="p-2">
					<div class="text-left">
						<svws-ui-checkbox v-model="option2" name="fuerErzieher">Erzieher als Adressat</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option4" name="mitBildBriefkopf">Bild des Briefkopfes verwenden</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option8" name="mitSchullogo">Schullogo verwenden</svws-ui-checkbox><br>
					</div>
					<div class="text-left">
						<svws-ui-checkbox v-model="option16" name="keineAnschrift">Keine Anschrift</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option32" name="keinInfoblock">Kein Infoblock</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option64" name="keineUnterschrift">Keine Unterschrift</svws-ui-checkbox><br>
					</div>
					<div class="text-left">
						<br><p class="font-bold underline mb-2">Optionen zur Druckausgabe:</p>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="1" v-model="druckoptionSchuelerSchulbescheinigung" name="druckoptionSchuelerSchulbescheinigungGesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="druckoptionSchuelerSchulbescheinigung" name="druckoptionSchuelerSchulbescheinigungEinzelausdruckEinseitig" label="Einzelausdruck einseitig" />
						</svws-ui-radio-group>
					</div>
					<div class="text-left">
						<br><p class="font-bold underline mb-2">Sortierung:</p>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="1" v-model="sortieroptionSchuelerSchulbescheinigung" name="sortieroptionSchuelerSchulbescheinigungName" label="Standard (Nachname, Vorname(n))" />
							<svws-ui-radio-option :value="2" v-model="sortieroptionSchuelerSchulbescheinigung" name="sortieroptionSchuelerSchulbescheinigungKlasseName" label="Klasse, Nachname, Vorname(n)" />
						</svws-ui-radio-group>
					</div>
					<div class="text-left col-span-4">
						<svws-ui-button :disabled="isPrintDisabled" @click="downloadPDF" :is-loading="loading" class="mt-4">
							<svws-ui-spinner v-if="loading" spinning />
							<span v-else class="icon i-ri-printer-line" />
							Drucken
						</svws-ui-button>
					</div>
				</svws-ui-input-wrapper>
			</ui-card>
			<ui-card v-if="hatKompetenzDruckenStundenplan && (mapStundenplaene.size > 0)" icon="i-ri-printer-line" title="Stundenplan drucken oder versenden" subtitle="Drucke oder versende die Stundenpläne der ausgewählten Schüler."
				:is-open="currentAction === 'druckSchuelerStundenplan'" @update:is-open="isOpen => setCurrentAction('druckSchuelerStundenplan', isOpen)">
				<svws-ui-input-wrapper :grid="2" class="p-2">
					<div class="text-left col-span-2">
						<ui-select :disabled="!schuelerListeManager().liste.auswahlExists()" label="Stundenplan" v-model="stundenplanAuswahl"
							:manager="stundenplanSelectManager" />
					</div>
					<div class="text-left">
						<svws-ui-checkbox v-model="option2">Pausenzeiten anzeigen</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option4">Fach- statt Kursbezeichnung verwenden (nicht Sek-II)</svws-ui-checkbox><br>
					</div>
					<div class="text-left">
						<svws-ui-checkbox v-model="option8">Fachkürzel statt Fachbezeichnung verwenden</svws-ui-checkbox><br>
						<svws-ui-checkbox v-model="option16">Individuelle Kursart anzeigen</svws-ui-checkbox>
					</div>
					<div class="text-left mb-2">
						<br><p class="font-bold underline mb-2">Optionen zur Druckausgabe:</p>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="1" v-model="druckoptionSchuelerStundenplan" name="druckoptionSchuelerStundenplanGesamtausdruckEinseitig" label="Gesamtausdruck einseitig" />
							<svws-ui-radio-option :value="2" v-model="druckoptionSchuelerStundenplan" name="druckoptionSchuelerStundenplanGesamtausdruckEinseitig" label="Einzelausdruck einseitig" />
						</svws-ui-radio-group>
					</div>
					<div class="text-left mb-2">
						<br><p class="font-bold underline mb-2">Sortierung:</p>
						<svws-ui-radio-group>
							<svws-ui-radio-option :value="1" v-model="sortieroptionSchuelerStundenplan" name="sortieroptionSchuelerStundenplanName" label="Standard (Nachname, Vorname(n))" />
							<svws-ui-radio-option :value="2" v-model="sortieroptionSchuelerStundenplan" name="sortieroptionSchuelerStundenplanKlasseName" label="Klasse, Nachname, Vorname(n)" />
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
					<template v-if="ServerMode.DEV.checkServerMode(serverMode)">
						<div class="text-left col-span-2 mb-2">
							<br><p class="font-bold mb-2">Den individuellen Stundenplan als E-Mail an die Schülerinnen und Schüler versenden.</p>
							<div class="flex flex-col gap-2">
								<input type="text" v-model="emailBetreff" placeholder="Betreff eingeben" class="w-full border rounded px-2 py-1">
								<textarea v-model="emailText" rows="5" placeholder="E-Mail-Text eingeben" class="w-full border rounded px-2 py-1" />
								<svws-ui-checkbox v-model="istPrivateEmailAlternative" name="istPrivateEmailAlternative">
									Private E-Mail verwenden, wenn keine schulische E-Mail-Adresse vorhanden ist.
								</svws-ui-checkbox>
							</div>
						</div>
						<div class="flex gap-2 text-left col-span-2" v-if="ServerMode.DEV.checkServerMode(serverMode)">
							<svws-ui-button :disabled="isEmailStundenplanDisabled" @click="sendPdfByEmail" :is-loading="loading" class="mt-4">
								<svws-ui-spinner v-if="loading" spinning />
								<span v-else class="icon i-ri-mail-send-line" />
								E-Mail senden
							</svws-ui-button>
							<!-- TODO Job-Management sollte an andere Stelle im Client allgemeiner implementiert werden -> Code entsprechend verschieben
							<svws-ui-button :disabled="!jobId || isEmailStundenplanDisabled" :is-loading="loading" @click="fetchStatus" class="mt-4">
								Job-Status abfragen
							</svws-ui-button>
							<svws-ui-button :disabled="!jobId || isEmailStundenplanDisabled" :is-loading="loading" @click="fetchLog" class="mt-4">
								Job-Log abfragen
							</svws-ui-button>
							-->
						</div>
					</template>
					<div v-if="!schuelerListeManager().liste.auswahlExists()">
						<span class="text-ui-danger">Es ist kein Schüler ausgewählt.</span>
					</div>
				</svws-ui-input-wrapper>
			</ui-card>
			<ui-card v-if="hatKompetenzLoeschen" icon="i-ri-delete-bin-line" title="Löschen"
				subtitle="Setze einen Löschvermerk bei den ausgewählten Schülern." :is-open="currentAction === 'delete'"
				@update:is-open="(isOpen) => setCurrentAction('delete', isOpen)">
				<div>
					<span v-if="deleteSchuelerCheck()[0]">Bereit zum Löschen.</span>
					<template v-else v-for="message in deleteSchuelerCheck()[1]" :key="message">
						<span class="text-ui-danger"> {{ message }} <br> </span>
					</template>
				</div>
				<template #buttonFooterLeft>
					<svws-ui-button :disabled="isDeleteDisabled" title="Löschen" @click="entferneSchueler" :is-loading="loading" class="mt-4">
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
	import type { SSchuelerAllgemeinesGruppenprozesseProps } from "./SSchuelerAllgemeinesGruppenprozesseProps";
	import type { StundenplanListeEintrag, List } from "@core";
	import { DateUtils, ReportingParameter, ReportingReportvorlage, ListUtils, ArrayList, BenutzerKompetenz, ReportingSortierungDefinition, ReportingEMailDaten, ReportingEMailEmpfaengerTyp, ReportingAusgabeformat, ServerMode } from "@core";
	import { SelectManager } from "@ui";

	type Action = 'druckSchuelerListeKontaktdatenErzieher' | 'druckSchuelerSchulbescheinigung' | 'druckSchuelerStundenplan' | 'delete' | '';

	const props = defineProps<SSchuelerAllgemeinesGruppenprozesseProps>();

	const hatKompetenzDrucken = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN) || props.benutzerKompetenzen.has(BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN)));
	const hatKompetenzDruckenStundenplan = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzDruckenSchuelerIndividualdaten = computed(() => (props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN) && hatKompetenzDrucken.value));
	const hatKompetenzLoeschen = computed(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_LOESCHEN));

	const hatIrgendwelcheKompetenzen = computed(() => hatKompetenzDrucken.value || hatKompetenzLoeschen.value || hatKompetenzDruckenStundenplan.value || hatKompetenzDruckenSchuelerIndividualdaten.value);

	const isPrintDisabled = computed<boolean>(() => !props.schuelerListeManager().liste.auswahlExists() || loading.value);
	const isPrintStundenplanDisabled = computed<boolean>(() => isPrintDisabled.value || stundenplanAuswahl.value === undefined);
	const isEmailStundenplanDisabled = computed<boolean>(() => isPrintStundenplanDisabled.value || ((emailBetreff.value.trim().length) === 0) || ((emailText.value.trim().length) === 0));
	const isDeleteDisabled = computed<boolean>(() => !hatKompetenzLoeschen.value || !props.schuelerListeManager().liste.auswahlExists() || !props.deleteSchuelerCheck()[0] || loading.value);

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

	const druckoptionSchuelerListeKontaktdatenErzieher = ref(1);
	const sortieroptionSchuelerListeKontaktdatenErzieher = ref(1);
	const druckoptionSchuelerSchulbescheinigung = ref(1);
	const sortieroptionSchuelerSchulbescheinigung = ref(1);
	const druckoptionSchuelerStundenplan = ref(1);
	const sortieroptionSchuelerStundenplan = ref(1);

	const emailBetreff = ref<string>("");
	const emailText = ref<string>("");
	const istPrivateEmailAlternative = ref<boolean>(false);

	async function downloadPDF() {
		const reportingParameter = new ReportingParameter();
		const listeIdsSchueler = new ArrayList<number>();
		for (const schueler of props.schuelerListeManager().liste.auswahl()) {
			listeIdsSchueler.add(schueler.id);
		}
		switch (currentAction.value) {
			case 'druckSchuelerListeKontaktdatenErzieher':
				reportingParameter.reportvorlage = ReportingReportvorlage.SCHUELER_V_LISTE_KONTAKTDATENERZIEHER.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsSchueler;
				reportingParameter.einzelausgabeHauptdaten = false;
				reportingParameter.einzelausgabeDetaildaten = false;
				reportingParameter.sortierungHauptdaten = new ReportingSortierungDefinition();
				reportingParameter.sortierungHauptdaten.verwendeStandardsortierung = (sortieroptionSchuelerListeKontaktdatenErzieher.value === 1);
				if (sortieroptionSchuelerListeKontaktdatenErzieher.value === 2) {
					const attribute = new ArrayList<string>();
					attribute.add("auswahlLernabschnitt.klasse.kuerzel");
					attribute.add("nachname");
					attribute.add("vorname");
					attribute.add("vornamen");
					attribute.add("geburtsdatum");
					attribute.add("id");
					reportingParameter.sortierungHauptdaten.attribute = attribute;

				}
				reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.SCHUELER_V_LISTE_KONTAKTDATENERZIEHER.getVorlageParameterList());
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
			case 'druckSchuelerSchulbescheinigung':
				reportingParameter.reportvorlage = ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG.getBezeichnung();
				reportingParameter.idsHauptdaten = listeIdsSchueler;
				reportingParameter.einzelausgabeHauptdaten = (druckoptionSchuelerSchulbescheinigung.value === 2);
				reportingParameter.einzelausgabeDetaildaten = false;
				reportingParameter.sortierungHauptdaten = new ReportingSortierungDefinition();
				reportingParameter.sortierungHauptdaten.verwendeStandardsortierung = (sortieroptionSchuelerSchulbescheinigung.value === 1);
				if (sortieroptionSchuelerSchulbescheinigung.value === 2) {
					const attribute = new ArrayList<string>();
					attribute.add("auswahlLernabschnitt.klasse.kuerzel");
					attribute.add("nachname");
					attribute.add("vorname");
					attribute.add("vornamen");
					attribute.add("geburtsdatum");
					attribute.add("id");
					reportingParameter.sortierungHauptdaten.attribute = attribute;
				}
				reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG.getVorlageParameterList());
				for (const vp of reportingParameter.vorlageParameter) {
					switch (vp.name) {
						case "fuerErzieher":
							vp.wert = option2.value.toString();
							break;
						case "mitBildBriefkopf":
							vp.wert = option4.value.toString();
							break;
						case "mitSchullogo":
							vp.wert = option8.value.toString();
							break;
						case "keineAnschrift":
							vp.wert = option16.value.toString();
							break;
						case "keinInfoblock":
							vp.wert = option32.value.toString();
							break;
						case "keineUnterschrift":
							vp.wert = option64.value.toString();
							break;
					}
				}
				break;
			case 'druckSchuelerStundenplan':
				if (stundenplanAuswahl.value === undefined) {
					return;
				}
				reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN.getBezeichnung();
				reportingParameter.idsHauptdaten = ListUtils.create1(stundenplanAuswahl.value.id);
				reportingParameter.idsDetaildaten = listeIdsSchueler;
				reportingParameter.einzelausgabeHauptdaten = false;
				reportingParameter.einzelausgabeDetaildaten = (druckoptionSchuelerStundenplan.value === 2);
				// Sortierung der Detaildaten (Schüler innerhalb des Stundenplans)
				reportingParameter.sortierungDetaildaten = new ReportingSortierungDefinition();
				reportingParameter.sortierungDetaildaten.verwendeStandardsortierung = (sortieroptionSchuelerStundenplan.value === 1);
				if (sortieroptionSchuelerStundenplan.value === 2) {
					const attribute = new ArrayList<string>();
					attribute.add("schueler.auswahlLernabschnitt.klasse.kuerzel");
					attribute.add("schueler.nachname");
					attribute.add("schueler.vorname");
					attribute.add("schueler.vornamen");
					attribute.add("schueler.geburtsdatum");
					attribute.add("schueler.id");
					reportingParameter.sortierungDetaildaten.attribute = attribute;
				}
				reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN.getVorlageParameterList());
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
						case "mitIndividuelleKursart":
							vp.wert = option16.value.toString();
							break;
					}
				}
				break;
			default:
				return;
		}
		reportingParameter.duplexdruck = ((druckoptionSchuelerStundenplan.value === 3) || (druckoptionSchuelerStundenplan.value === 4)
			|| (druckoptionSchuelerSchulbescheinigung.value === 3) || (druckoptionSchuelerSchulbescheinigung.value === 4)
			|| (druckoptionSchuelerListeKontaktdatenErzieher.value === 3) || (druckoptionSchuelerListeKontaktdatenErzieher.value === 4));
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

	const jobId = ref<number | null>(null);

	async function sendPdfByEmail() {
		if (stundenplanAuswahl.value === undefined) {
			return;
		}

		const listeIdsSchueler = new ArrayList<number>();
		for (const schueler of props.schuelerListeManager().liste.auswahl()) {
			listeIdsSchueler.add(schueler.id);
		}

		const reportingParameter = new ReportingParameter();
		reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN.getBezeichnung();
		reportingParameter.ausgabeformat = ReportingAusgabeformat.EMAIL.getId();
		reportingParameter.idsHauptdaten = ListUtils.create1(stundenplanAuswahl.value.id);
		reportingParameter.idsDetaildaten = listeIdsSchueler;
		reportingParameter.einzelausgabeHauptdaten = false;
		reportingParameter.einzelausgabeDetaildaten = true;

		const emailDaten = new ReportingEMailDaten();
		emailDaten.empfaengerTyp = ReportingEMailEmpfaengerTyp.SCHUELER.getId();
		emailDaten.istPrivateEmailAlternative = istPrivateEmailAlternative.value;
		emailDaten.betreff = (((emailBetreff.value.trim().length) !== 0) ? emailBetreff.value : ("Stundenplan " + stundenplanAuswahl.value.bezeichnung));
		emailDaten.text = (((emailText.value.trim().length) !== 0) ? emailText.value : ("Im Anhang dieser E-Mail ist der Stundenplan " + stundenplanAuswahl.value.bezeichnung + " enthalten."));
		reportingParameter.eMailDaten = emailDaten;

		reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.STUNDENPLANUNG_V_SCHUELER_STUNDENPLAN.getVorlageParameterList());
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
				case "mitIndividuelleKursart":
					vp.wert = option16.value.toString();
					break;
			}
		}

		reportingParameter.duplexdruck = ((druckoptionSchuelerStundenplan.value === 3) || (druckoptionSchuelerStundenplan.value === 4));
		loading.value = true;
		jobId.value = null;
		if (logs.value === undefined) {
			logs.value = new ArrayList();
		}
		const result = await props.sendEMail(reportingParameter);
		jobId.value = result.id;
		statusAction.value = result.success;
		logs.value.addAll(result.log);
		try {
			// Erste Statusabfrage durchführen
			await fetchStatus();
		} catch (e: any) {
			logs.value.add(`Fehler: ${e?.message ?? 'Unbekannter Fehler'}`);
		} finally {
			loading.value = false;
		}
	}

	async function fetchStatus() {
		if (jobId.value === null) {
			return;
		}
		const result = await props.fetchEMailJobStatus(jobId.value);
		logs.value?.addAll(result.log);
		logs.value?.add("");
	}

	async function fetchLog() {
		if (jobId.value === null) {
			return;
		}
		const result = await props.fetchEMailJobLog(jobId.value);
		logs.value?.addAll(result.log);
		logs.value?.add("");
	}

	async function entferneSchueler() {
		loading.value = true;
		[statusAction.value, logs.value] = await props.deleteSchueler();
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

	const wochentag = ['So.', 'Mo.', 'Di.', 'Mi.', 'Do.', 'Fr.', 'Sa.', 'So.'];

	function toDateStr(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return wochentag[date[3] % 7] + " " + date[2] + "." + date[1] + "." + date[0];
	}

	function toKW(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return "" + date[5];
	}

	function setCurrentAction(newAction: Action, open: boolean) {
		if ((newAction !== currentAction.value) && !open) {
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
		druckoptionSchuelerListeKontaktdatenErzieher.value = 1;
		sortieroptionSchuelerListeKontaktdatenErzieher.value = 1;
		druckoptionSchuelerSchulbescheinigung.value = 1;
		sortieroptionSchuelerSchulbescheinigung.value = 1;
		druckoptionSchuelerStundenplan.value = 1;
		sortieroptionSchuelerStundenplan.value = 1;
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

</script>
