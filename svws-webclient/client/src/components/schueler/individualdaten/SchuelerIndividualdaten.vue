<template>
	<Teleport v-if="zeigeAlles" to=".svws-ui-header--actions" defer>
		<wiedervorlage-modal type="schueler" mode="create"
			:data="{
				idPerson: model.proxy.id,
				namePerson: `${model.proxy.vorname} ${model.proxy.nachname}`
			}">
			<template #default="{openModal}">
				<svws-ui-button @click="openModal" type="secondary">
					<span class="icon i-ri-alarm-line" aria-hidden="true" /> Wiedervorlage anlegen
				</svws-ui-button>
			</template>
		</wiedervorlage-modal>
		<svws-ui-button v-if="hatKompetenzDrucken" @click="downloadPDF" type="secondary">
			<svws-ui-spinner v-if="loading" spinning />
			<span v-else class="icon i-ri-printer-line" aria-hidden="true" /> Schulbescheinigung drucken
		</svws-ui-button>
		<svws-ui-modal-hilfe>
			<hilfe-schueler-individualdaten />
		</svws-ui-modal-hilfe>
	</Teleport>

	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Nachname" class="contentFocusField"
					v-model="model.proxy.nachname"
					:validation="() => model.getFehler('nachname')"
					:min-len="1" :max-len="120"
					required :readonly v-autofocus
					@change="model.patch" />
				<svws-ui-text-input placeholder="Rufname"
					v-model="model.proxy.vorname"
					:validation="() => model.getFehler('vorname')"
					:min-len="1" :max-len="80"
					required :readonly
					@change="model.patch" />
				<svws-ui-text-input placeholder="Alle Vornamen"
					v-model="model.proxy.alleVornamen"
					:validation="() => model.getFehler('alleVornamen')"
					:max-len="255" :readonly
					@change="model.patch" />
				<svws-ui-spacing />
				<ui-select label="Geschlecht"
					v-model="model.geschlecht.value"
					:manager="geschlechtManager"
					:readonly required statistics />
				<svws-ui-text-input placeholder="Geburtsdatum" :readonly
					v-model="model.proxy.geburtsdatum"
					:validation="() => model.getFehler('geburtsdatum')"
					@change="model.patch"
					type="date" required statistics />
				<svws-ui-text-input placeholder="Geburtsort" :readonly
					v-model="model.proxy.geburtsort"
					:validation="() => model.getFehler('geburtsort')"
					:max-len="100"
					@change="model.patch" />
				<svws-ui-text-input placeholder="Geburtsname" :readonly
					v-model="model.proxy.geburtsname"
					:validation="() => model.getFehler('geburtsname')"
					:max-len="120"
					@change="model.patch" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Statusdaten" v-if="hatKompetenzAnsehen">
			<template #actions v-if="schuleState.schulform === Schulform.BK || schuleState.schulform === Schulform.SB">
				<svws-ui-checkbox :readonly v-model="model.proxy.istDuplikat">
					Ist Duplikat
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Status"
					v-model="model.status.value"
					:manager="statusManager"
					:readonly :removable="false" required statistics focus-class-content />
				<ui-select v-if="model.proxy.status === SchuelerStatus.EXTERN.daten(schuljahr)?.id"
					label="Stammschule"
					v-model="model.externeSchulNr.value"
					:manager="stammschuleManager"
					:removable="model.proxy.externeSchulNr !== null"
					:readonly searchable />
				<div v-else />
				<template v-if="serverState.hasDev">
					<svws-ui-text-input placeholder="Schülerausweis-Nummer"
						v-model="model.proxy.idSchuelerausweis"
						:validation="() => model.getFehler('idSchuelerausweis')"
						@change="model.patch"
						:max-len="30"
						:readonly />
					<div v-if="!istSchulformBerufskolleg" />
				</template>
				<div v-if="!serverState.hasDev" />
				<svws-ui-text-input v-if="istSchulformBerufskolleg" placeholder="Beruf"
					v-model="model.proxy.beruf"
					:validation="() => model.getFehler('beruf')"
					@change="model.patch"
					:readonly :max-len="100" />
				<ui-select label="Fahrschüler"
					v-model="model.fahrschuelerArtID.value"
					:manager="fahrschuelerartManager"
					:removable="model.fahrschuelerArtID.value !== null" :readonly />
				<ui-select label="Haltestelle"
					v-model="model.haltestelleID.value"
					:manager="haltestellenManager"
					:removable="model.haltestelleID.value !== null" :readonly />
				<svws-ui-text-input placeholder="Anmeldedatum" :readonly
					v-model="model.proxy.anmeldedatum"
					@change="model.patch"
					type="date" />
				<svws-ui-text-input placeholder="Aufnahmedatum" :readonly
					v-model="model.proxy.aufnahmedatum"
					@change="model.patch"
					type="date" statistics />
				<svws-ui-spacing />
				<svws-ui-input-wrapper :grid="2" class="input-wrapper--checkboxes">
					<svws-ui-checkbox :readonly
						v-model="model.proxy.istVolljaehrig" statistics>
						Volljährig
					</svws-ui-checkbox>
					<svws-ui-checkbox :readonly
						v-model="model.proxy.keineAuskunftAnDritte">
						Keine Auskunft an Dritte
					</svws-ui-checkbox>
					<svws-ui-checkbox :readonly
						v-model="model.proxy.istSchulpflichtErfuellt"
						statistics>
						Schulpflicht erfüllt
					</svws-ui-checkbox>
					<svws-ui-checkbox :readonly
						v-model="model.proxy.istBerufsschulpflichtErfuellt">
						Schulpflicht SII erfüllt
					</svws-ui-checkbox>
					<svws-ui-checkbox :readonly
						v-model="model.proxy.hatMasernimpfnachweis">
						Masern-Schutznachweis
					</svws-ui-checkbox>
					<svws-ui-checkbox :readonly
						v-model="model.proxy.erhaeltSchuelerBAFOEG">
						BAFöG
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wohnort und Kontaktdaten" v-if="hatKompetenzAnsehen">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Straße" :readonly
					v-model="model.adresse.value"
					:validation="() => model.getFehler('strassenname')"
					@change="model.patch"
					span="full" />
				<ui-select label="Wohnort"
					v-model="model.selectedOrt.value"
					:manager="orteManager"
					:removable="model.selectedOrt.value !== null"
					searchable :readonly statistics />
				<ui-select label="Ortsteil"
					v-model="model.selectedOrtsteil.value"
					:manager="ortsteilManager"
					:disabled="istOrtsteilDisabled"
					:removable="model.selectedOrtsteil.value !== null"
					searchable :readonly statistics />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" :readonly
					v-model="model.proxy.telefon"
					:validation="() => model.getFehler('telefon')"
					@change="model.patch"
					type="tel" :max-len="20" />
				<svws-ui-text-input placeholder="Mobil oder Fax" :readonly
					v-model="model.proxy.telefonMobil"
					:validation="() => model.getFehler('telefonMobil')"
					@change="model.patch"
					type="tel" :max-len="20" />
				<svws-ui-text-input placeholder="Private E-Mail-Adresse" :readonly
					v-model="model.proxy.emailPrivat"
					:validation="() => model.getFehler('emailPrivat')"
					@change="model.patch"
					:max-len="100"
					type="email" />
				<svws-ui-text-input placeholder="Schulische E-Mail-Adresse" :readonly
					v-model="model.proxy.emailSchule"
					:validation="() => model.getFehler('emailSchule')"
					@change="model.patch"
					:max-len="100"
					type="email" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Staatsangehörigkeit und Konfession" v-if="hatKompetenzAnsehen">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="1. Staatsangehörigkeit"
					v-model="model.staatsangehoerigkeitID.value"
					:manager="staatsangehoerigkeitManager"
					:readonly required searchable statistics focus-class-content />
				<ui-select label="2. Staatsangehörigkeit"
					v-model="model.staatsangehoerigkeit2ID.value"
					:manager="staatsangehoerigkeitManager"
					:removable="model.staatsangehoerigkeit2ID.value !== null" :readonly searchable />
				<ui-select label="Konfession"
					v-model="model.religionID.value"
					:manager="religionManager"
					:readonly required statistics />
				<div class="flex items-center pl-2">
					<svws-ui-checkbox
						v-model="model.proxy.druckeKonfessionAufZeugnisse" :readonly>
						Konfession aufs Zeugnis
					</svws-ui-checkbox>
				</div>
				<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht" :readonly
					v-model="model.proxy.religionabmeldung"
					@change="model.patch"
					type="date" statistics />
				<svws-ui-text-input placeholder="Wiederanmeldung" :readonly
					v-model="model.proxy.religionanmeldung"
					@change="model.patch"
					type="date" statistics />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<schueler-telefonnummern v-if="serverState.hasDev && hatKompetenzAnsehen && zeigeAlles"
			:readonly
			:id-schueler="model.proxy.id"
			:map-telefon-arten="props.mapTelefonArten"
			:get-list-schueler-telefoneintraege="props.getListSchuelerTelefoneintraege"
			:add-schueler-telefoneintrag="props.addSchuelerTelefoneintrag"
			:patch-schueler-telefoneintrag="props.patchSchuelerTelefoneintrag"
			:delete-schueler-telefoneintrage="props.deleteSchuelerTelefoneintrage" />
		<svws-ui-content-card title="Migrationshintergrund" v-if="hatKompetenzAnsehen">
			<template #actions>
				<svws-ui-checkbox :readonly class="mt-3 xl:mt-0" v-model="model.proxy.hatMigrationshintergrund" statistics focus-class-content>
					Migrationshintergrund vorhanden
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number placeholder="Zuzugsjahr"
					v-model="model.proxy.zuzugsjahr"
					:validation="() => model.getFehler('zuzugsjahr')"
					:disabled="!model.proxy.hatMigrationshintergrund"
					:readonly="model.proxy.hatMigrationshintergrund && readonly"
					statistics :steps="false" :min :max
					@change="model.patch" />
				<ui-select label="Geburtsland"
					v-model="model.geburtsland.value"
					:manager="geburtslandManager"
					:removable="model.geburtsland.value !== null" :disabled="!model.proxy.hatMigrationshintergrund"
					:readonly="model.proxy.hatMigrationshintergrund && readonly" searchable statistics />
				<ui-select label="Verkehrssprache"
					v-model="model.verkehrspracheFamilie.value"
					:manager="verkehrsspracheManager"
					:removable="model.verkehrspracheFamilie.value !== null" :disabled="!model.proxy.hatMigrationshintergrund"
					:readonly="model.proxy.hatMigrationshintergrund && readonly" class="col-span-full" searchable statistics />
				<ui-select label="Geburtsland Mutter"
					v-model="model.geburtslandMutter.value"
					:manager="geburtslandManager"
					:removable="model.geburtslandMutter.value !== null" :disabled="!model.proxy.hatMigrationshintergrund"
					:readonly="model.proxy.hatMigrationshintergrund && readonly" searchable statistics />
				<ui-select label="Geburtsland Vater"
					v-model="model.geburtslandVater.value"
					:manager="geburtslandManager"
					:removable="model.geburtslandVater.value !== null" :disabled="!model.proxy.hatMigrationshintergrund"
					:readonly="model.proxy.hatMigrationshintergrund && readonly" searchable statistics />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { SchuelerIndividualdatenProps } from "./SchuelerIndividualdatenProps";
	import type { JavaSet, NationalitaetenKatalogEintrag, ReligionEintrag, Fahrschuelerart, Haltestelle, SchuelerStatusKatalogEintrag, VerkehrsspracheKatalogEintrag } from "@core";
	import { SchuelerStatus, Schulform, Nationalitaeten, Geschlecht, Verkehrssprache, BenutzerKompetenz, ReportingReportvorlage, HashSet } from "@core";
	import { orte_sort, ortsteilSort } from "~/utils/helfer";
	import { CoreTypeSelectManager, SelectManager, useBenutzerState, useOrteState, useReportingState, useSchuleState, useServerState } from "@ui";
	import { SchuelerIndividualdatenModel } from "~/components/schueler/individualdaten/modelproxy/SchuelerIndividualdatenModelProxy";
	import WiedervorlageModal from "~/components/wiedervorlage/WiedervorlageModal.vue";
	import SchuelerTelefonnummern from "~/components/schueler/individualdaten/telefonnummern/SchuelerTelefonnummern.vue";

	// --- Setup ---

	const props = defineProps<SchuelerIndividualdatenProps>();
	const reportingState = useReportingState();
	const schuleState = useSchuleState();
	const serverState = useServerState();

	const schuljahr = computed<number>(() => props.schuelerListeManager().schuelerGetSchuljahrOrException());

	const model = new SchuelerIndividualdatenModel(
		() => props.schuelerListeManager().daten(),
		() => schuleState.validatorKontext,
		() => schuljahr.value,
		() => props.religionenById,
		() => props.fahrschuelerartenById,
		() => props.haltestellenById,
		props.patch
	);
	const benutzerState = useBenutzerState();

	// --- Benutzerkompetenzen ---

	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const hatKompetenzAnsehen = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN));
	const hatKompetenzDrucken = computed(() =>
		benutzerState.benutzerHatKompetenz(BenutzerKompetenz.BERICHTE_ALLE_FORMULARE_DRUCKEN) || benutzerState.benutzerHatKompetenz(BenutzerKompetenz.BERICHTE_STANDARDFORMULARE_DRUCKEN));

	// --- Karte "Allgemein" ---

	const geschlechtManager = new SelectManager<Geschlecht>({
		options: computed(() => Geschlecht.values()),
		optionDisplayText: i => i.text,
		selectionDisplayText: i => i.text,
	});

	// --- Karte "Statusdaten" ---

	const istSchulformBerufskolleg = computed(() => [Schulform.BK, Schulform.SB, Schulform.WB].includes(schuleState.schulform));

	const statusManager = new CoreTypeSelectManager<SchuelerStatusKatalogEintrag, SchuelerStatus>({
		clazz: SchuelerStatus.class,
		schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const fahrschuelerartManager = new SelectManager<Fahrschuelerart>({
		options: computed(() => props.fahrschuelerartenById.values()),
		optionDisplayText: i => i.bezeichnung ?? '',
		selectionDisplayText: i => i.bezeichnung ?? '',
	});

	const haltestellenManager = new SelectManager<Haltestelle>({
		options: computed(() => props.haltestellenById.values()),
		optionDisplayText: i => i.bezeichnung ?? '',
		selectionDisplayText: i => i.bezeichnung ?? '',
	});

	const eigeneSchulnummer = computed<string>(() => `${schuleState.validatorKontext.getSchulnummer()}`);
	const moeglicheStammschulnummern = computed<JavaSet<string>>(() => {
		// Füge zunächst alle Schulnummern mit eingetragenen Kürzeln im Schul-Katalog hinzu
		const result = new HashSet<string>();
		for (const schule of props.mapSchulen.values()) {
			if ((schule.schulnummerStatistik !== null) && (schule.schulnummerStatistik !== eigeneSchulnummer.value)) {
				result.add(schule.schulnummerStatistik);
			}
		}
		return result;
	});

	const stammschuleManager = new SelectManager({
		options: moeglicheStammschulnummern,
		selectionDisplayText: getSchulnummerText,
		optionDisplayText: getSchulnummerText,
	});

	function getSchulnummerText(schulnummer: string): string {
		const eintrag = props.mapSchulen.get(schulnummer);
		const text = `${eintrag?.schulnummerStatistik ?? ''} ${eintrag?.kuerzel ?? eintrag?.kurzbezeichnung ?? ''}`;
		return text.length > 0 ? text : 'Fehlende Angaben';
	}

	// --- Karte "Staatsangehörigkeit und Konfession" ---

	const religionManager = new SelectManager<ReligionEintrag>({
		options: computed(() => props.religionenById.values()),
		optionDisplayText: i => i.bezeichnung,
		selectionDisplayText: i => i.bezeichnung,
	});

	const staatsangehoerigkeitManager = new CoreTypeSelectManager<NationalitaetenKatalogEintrag, Nationalitaeten>({
		clazz: Nationalitaeten.class,
		schuljahr,
		sort: (a, b) => {
			if (a.staatsangehoerigkeit.length > 0 && b.staatsangehoerigkeit.length > 0) {
				return a.staatsangehoerigkeit.localeCompare(b.staatsangehoerigkeit);
			} else if (a.staatsangehoerigkeit.length > 0) {
				return -1;
			} else if (b.staatsangehoerigkeit.length > 0) {
				return 1;
			}
			return 0;
		},
		optionDisplayText: i => i.staatsangehoerigkeit,
		selectionDisplayText: i => i.staatsangehoerigkeit,
	});

	// --- Karte "Migrationshintergrund" ---

	const max = new Date().getFullYear() + 1;
	const min = max - 100;

	const geburtslandManager = new CoreTypeSelectManager<NationalitaetenKatalogEintrag, Nationalitaeten>({
		clazz: Nationalitaeten.class,
		schuljahr,
		sort: (a, b) => a.bezeichnung.localeCompare(b.bezeichnung),
		optionDisplayText: i => `${i.bezeichnung} (${i.iso3})`,
		selectionDisplayText: i => `${i.bezeichnung} (${i.iso3})`,
	});

	const verkehrsspracheManager = new CoreTypeSelectManager<VerkehrsspracheKatalogEintrag, Verkehrssprache>({
		clazz: Verkehrssprache.class,
		schuljahr,
		sort: (a, b) => a.text.localeCompare(b.text),
		optionDisplayText: i => `${i.text} (${i.iso3})`,
		selectionDisplayText: i => `${i.text} (${i.iso3})`,
	});

	// --- Karte "Wohnort und Kontaktdaten" ---

	const orteState = useOrteState();

	const istOrtsteilDisabled = computed(() => (model.selectedOrt.value === null) && (model.selectedOrtsteil.value === null));

	const orteManager = new SelectManager({
		options: computed(() => orteState.orte.list),
		sort: orte_sort,
		optionDisplayText: v => `${v.plz ?? ''} ${v.ortsname ?? ''}`.trim(),
		selectionDisplayText: v => `${v.plz ?? ''} ${v.ortsname ?? ''}`.trim(),
	});

	const ortsteilManager = new SelectManager({
		options: computed(() => orteState.ortsteile.listByOrtId(model.proxy.wohnortID)),
		sort: ortsteilSort,
		optionDisplayText: v => v.ortsteil ?? '',
		selectionDisplayText: v => v.ortsteil ?? '',
	});

	// --- PDF ---

	const loading = ref<boolean>(false);

	async function downloadPDF() {
		const reportingParameter = ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG.getReportingParameter();
		ReportingReportvorlage.SCHUELER_V_SCHULBESCHEINIGUNG.setReportingParameterVorlageparameter(reportingParameter, "mitSchullogo", "true");
		reportingParameter.idsHauptdaten.add(props.schuelerListeManager().auswahlID());
		loading.value = true;
		await reportingState.createPDFReport(reportingParameter);
		loading.value = false;
	}

</script>
