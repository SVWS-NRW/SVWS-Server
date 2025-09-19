<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-button @click="downloadPDF" type="secondary"><svws-ui-spinner v-if="loading" spinning /><span v-else class="icon i-ri-printer-line" /> Schulbescheinigung drucken</svws-ui-button>
		<svws-ui-modal-hilfe> <hilfe-schueler-individualdaten /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" v-autofocus placeholder="Nachname" :readonly :model-value="data.nachname"
					@change="nachname => patch({ nachname: nachname ?? undefined })" type="text" />
				<svws-ui-text-input placeholder="Rufname" :readonly :model-value="data.vorname"
					@change="vorname => patch({ vorname: vorname ?? undefined })" type="text" />
				<svws-ui-text-input placeholder="Alle Vornamen" :readonly :model-value="data.alleVornamen"
					@change="alleVornamen => patch({ alleVornamen: alleVornamen ?? undefined })" type="text" />
				<svws-ui-spacing />
				<svws-ui-select title="Geschlecht" :readonly v-model="geschlecht" :items="Geschlecht.values()"
					statistics :item-text="(i: Geschlecht)=>i.text" />
				<svws-ui-text-input placeholder="Geburtsdatum" :readonly :model-value="data.geburtsdatum"
					@change="geburtsdatum => geburtsdatum && patch({geburtsdatum})" type="date" :valid="istGeburtsdatumGueltig" required statistics />
				<svws-ui-text-input placeholder="Geburtsort" :readonly :model-value="data.geburtsort"
					@change="geburtsort => patch({ geburtsort })" type="text" />
				<svws-ui-text-input placeholder="Geburtsname" :readonly :model-value="data.geburtsname"
					@change="geburtsname => patch({ geburtsname })" type="text" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Statusdaten" v-if="hatKompetenzAnsehen">
			<template #actions v-if="schulform === Schulform.BK || schulform === Schulform.SB">
				<svws-ui-checkbox :readonly :model-value="data.istDuplikat" @update:model-value="istDuplikat => patch({istDuplikat})">Ist Duplikat</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Status" :readonly :model-value="SchuelerStatus.data().getWertByKuerzel('' + data.status)"
					@update:model-value="status => (status?.daten(schuljahr)?.id !== undefined) && patch({ status: status?.daten(schuljahr)?.id })"
					:items="SchuelerStatus.values()" :item-text="i => i.daten(schuljahr)?.text ?? '—'" statistics focus-class-content />
				<svws-ui-select v-if="schuelerListeManager().daten().status === SchuelerStatus.EXTERN.daten(schuljahr)?.id" :readonly
					title="Stammschule" v-model="inputStammschule" :items="mapSchulen.values()" :item-text="i => i.kuerzel ?? i.schulnummerStatistik ?? i.kurzbezeichnung ?? i.name" removable />
				<div v-else />
				<svws-ui-select title="Fahrschüler" :readonly v-model="inputFahrschuelerArtID" :items="mapFahrschuelerarten"
					:item-text="i => i.bezeichnung ?? ''" removable />
				<svws-ui-select title="Haltestelle" :readonly v-model="inputHaltestelleID" :items="mapHaltestellen"
					:item-text="i => i.bezeichnung ?? ''" removable />
				<svws-ui-text-input placeholder="Anmeldedatum" :readonly :model-value="data.anmeldedatum"
					@change="anmeldedatum => patch({ anmeldedatum : anmeldedatum ?? null })" type="date" removable />
				<svws-ui-text-input placeholder="Aufnahmedatum" :readonly :model-value="data.aufnahmedatum"
					@change="aufnahmedatum => patch({ aufnahmedatum : aufnahmedatum ?? null })" type="date" statistics />
				<svws-ui-spacing />
				<svws-ui-input-wrapper :grid="2" class="input-wrapper--checkboxes">
					<svws-ui-checkbox :readonly :model-value="data.istVolljaehrig" statistics
						@update:model-value="istVolljaehrig => patch({ istVolljaehrig })">
						Volljährig
					</svws-ui-checkbox>
					<svws-ui-checkbox :readonly :model-value="data.keineAuskunftAnDritte"
						@update:model-value="keineAuskunftAnDritte => patch({ keineAuskunftAnDritte })">
						Keine Auskunft an Dritte
					</svws-ui-checkbox>
					<svws-ui-checkbox :readonly :model-value="data.istSchulpflichtErfuellt" statistics>
						Schulpflicht erfüllt
					</svws-ui-checkbox>
					<svws-ui-checkbox :readonly :model-value="data.istBerufsschulpflichtErfuellt"
						@update:model-value="istBerufsschulpflichtErfuellt => patch({ istBerufsschulpflichtErfuellt })">
						Schulpflicht SII erfüllt
					</svws-ui-checkbox>
					<svws-ui-checkbox :readonly :model-value="data.hatMasernimpfnachweis"
						@update:model-value="hatMasernimpfnachweis => patch({ hatMasernimpfnachweis })">
						Masern Impfnachweis
					</svws-ui-checkbox>
					<svws-ui-checkbox :readonly :model-value="data.erhaeltSchuelerBAFOEG"
						@update:model-value="erhaeltSchuelerBAFOEG => patch({ erhaeltSchuelerBAFOEG })">
						BAFöG
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wohnort und Kontaktdaten" v-if="hatKompetenzAnsehen">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Straße" :readonly :model-value="strasse"
					@change="patchStrasse" type="text" span="full" />
				<svws-ui-select title="Wohnort" :readonly v-model="wohnortID" :items="mapOrte"
					:item-filter="orte_filter" :item-sort="orte_sort" :item-text="i => `${i.plz} ${i.ortsname}`" autocomplete statistics />
				<svws-ui-select title="Ortsteil" :readonly v-model="ortsteilID" :items="ortsteile"
					:item-text="i => i.ortsteil ?? ''" :item-sort="ortsteilSort" :item-filter="ortsteilFilter" removable />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" :readonly :model-value="data.telefon"
					@change="telefon => patch({ telefon })" type="tel" :max-len="20" />
				<svws-ui-text-input placeholder="Mobil oder Fax" :readonly :model-value="data.telefonMobil"
					@change="telefonMobil => patch({ telefonMobil })" type="tel" :max-len="20" />
				<svws-ui-text-input placeholder="Private E-Mail-Adresse" :readonly :model-value="data.emailPrivat"
					@change="emailPrivat => patch({ emailPrivat })" type="email" verify-email />
				<svws-ui-text-input placeholder="Schulische E-Mail-Adresse" :readonly :model-value="data.emailSchule"
					@change="emailSchule => patch({ emailSchule })" type="email" verify-email />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Staatsangehörigkeit und Konfession" v-if="hatKompetenzAnsehen">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="1. Staatsangehörigkeit" :readonly v-model="staatsangehoerigkeit" autocomplete
					:items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" required statistics focus-class-content />
				<svws-ui-select title="2. Staatsangehörigkeit" :readonly v-model="staatsangehoerigkeit2" autocomplete removable
					:items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" />
				<svws-ui-select title="Konfession" :readonly v-model="religion" :items="mapReligionen" :item-text="i => i.bezeichnungZeugnis ?? ''" required statistics />
				<div class="flex items-center pl-2">
					<svws-ui-checkbox v-model="druckeKonfessionAufZeugnisse" :readonly>Konfession aufs Zeugnis</svws-ui-checkbox>
				</div>
				<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht" :readonly :model-value="data.religionabmeldung"
					@change="religionabmeldung => patch({religionabmeldung})" type="date" statistics />
				<svws-ui-text-input placeholder="Wiederanmeldung" :readonly :model-value="data.religionanmeldung"
					@change="religionanmeldung => patch({religionanmeldung})" type="date" statistics />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Weitere Telefonnummern" v-if="serverMode === ServerMode.DEV">
			<svws-ui-table :clickable="!readonly" @update:clicked="v => patchTelefonnummer(v)" :items="getListSchuelerTelefoneintraege()" :columns :selectable="!readonly" v-model="selected">
				<template #cell(idTelefonArt)="{ value }">
					{{ getBezeichnungTelefonart(value) }}
				</template>
				<template #cell(istGesperrt)="{ value }">
					{{ value ? 'Gesperrt' : 'Nicht gesperrt' }}
				</template>
				<template #actions v-if="!readonly">
					<div class="inline-flex gap-4">
						<svws-ui-button @click="deleteTelefonnummern" type="trash" :disabled="selected.length === 0" />
						<svws-ui-button @click="addTelefonnummer" type="icon" title="Telefonnummer hinzufügen"><span class="icon i-ri-add-line" /></svws-ui-button>
					</div>
				</template>
			</svws-ui-table>
			<svws-ui-modal :show="showModalTelefonnummer" @update:show="closeModalTelefonnummer">
				<template #modalTitle>Telefonnummer hinzufügen</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2" class="text-left">
						<svws-ui-select title="Telefonart" :items="mapTelefonArten.values()" v-model="selectedTelefonArt" :item-text="i => i.bezeichnung" />
						<svws-ui-text-input v-model="newEntryTelefonnummer.telefonnummer" type="text" placeholder="Telefonnummer" :max-len="20" />
						<svws-ui-tooltip class="col-span-full">
							<svws-ui-text-input v-model="newEntryTelefonnummer.bemerkung" type="text" placeholder="Bemerkung" />
							<template #content>
								{{ newEntryTelefonnummer.bemerkung ?? 'Bemerkung' }}
							</template>
						</svws-ui-tooltip>
						<svws-ui-spacing />
						<svws-ui-checkbox v-model="newEntryTelefonnummer.istGesperrt" type="checkbox" title="Für Weitergabe gesperrt" class="col-span-full">
							Für Weitergabe gesperrt
						</svws-ui-checkbox>
					</svws-ui-input-wrapper>
					<svws-ui-notification type="warning" v-if="mapTelefonArten.size === 0">Die Liste der Telefonarten ist leer, es sollte mindestens eine Telefonart unter Schule/Kataloge angelegt werden, damit zusätzliche Telefonnummern eine gültige Zuordnung haben. </svws-ui-notification>
					<div class="mt-7 flex flex-row gap-4 justify end">
						<svws-ui-button type="secondary" @click="closeModalTelefonnummer">Abbrechen</svws-ui-button>
						<svws-ui-button @click="sendRequestTelefonnummer" :disabled="(selectedTelefonArt === null) || (mapTelefonArten.size === 0) || (newEntryTelefonnummer.telefonnummer === null) || (newEntryTelefonnummer.telefonnummer.length === 0)">
							Speichern
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
		</svws-ui-content-card>
		<svws-ui-content-card title="Migrationshintergrund" v-if="hatKompetenzAnsehen">
			<template #actions>
				<svws-ui-checkbox :readonly class="mt-3 xl:mt-0" :model-value="hatMigrationshintergrund" statistics
					@update:model-value="hatMigrationshintergrund => patch({hatMigrationshintergrund})" focus-class-content>
					Migrationshintergrund vorhanden
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number placeholder="Zuzugsjahr" :model-value="data.zuzugsjahr" @change="zuzugsjahr => patch({zuzugsjahr})"
					:disabled="!hatMigrationshintergrund" :readonly="hatMigrationshintergrund && readonly" statistics hide-stepper :min :max />
				<svws-ui-select title="Geburtsland" v-model="geburtsland" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter"
					:disabled="!hatMigrationshintergrund" :readonly="hatMigrationshintergrund && readonly" autocomplete statistics />
				<svws-ui-select title="Verkehrssprache" v-model="verkehrsprache" autocomplete :items="Verkehrssprache.values()"
					:item-text="i => `${i.historie().getLast().text} (${i.historie().getLast().iso3})`" :item-sort="verkehrsspracheKatalogEintragSort"
					:item-filter="verkehrsspracheKatalogEintragFilter" :disabled="!hatMigrationshintergrund" :readonly="hatMigrationshintergrund && readonly" class="col-span-full" statistics />
				<svws-ui-select title="Geburtsland Mutter" v-model="geburtslandMutter" :items="Nationalitaeten.values()"
					:item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`" :item-sort="nationalitaetenKatalogEintragSort"
					:item-filter="nationalitaetenKatalogEintragFilter" :disabled="!hatMigrationshintergrund" :readonly="hatMigrationshintergrund && readonly" autocomplete statistics />
				<svws-ui-select title="Geburtsland Vater" v-model="geburtslandVater" :items="Nationalitaeten.values()"
					:item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`" :item-sort="nationalitaetenKatalogEintragSort"
					:item-filter="nationalitaetenKatalogEintragFilter" :disabled="!hatMigrationshintergrund" :readonly="hatMigrationshintergrund && readonly" autocomplete statistics />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { SchuelerIndividualdatenProps } from "./SSchuelerIndividualdatenProps";
	import type { SchuelerStammdaten, OrtKatalogEintrag, OrtsteilKatalogEintrag, ReligionEintrag, SchulEintrag, TelefonArt, Haltestelle, Fahrschuelerart } from "@core";
	import { SchuelerStatus, Schulform, Nationalitaeten, Geschlecht, AdressenUtils, Verkehrssprache, BenutzerKompetenz, DateUtils, SchuelerTelefon, ServerMode, ArrayList, ReportingParameter, ReportingSortierungDefinition, ReportingReportvorlage } from "@core";
	import { verkehrsspracheKatalogEintragFilter, verkehrsspracheKatalogEintragSort, nationalitaetenKatalogEintragFilter, nationalitaetenKatalogEintragSort,
		staatsangehoerigkeitKatalogEintragSort, staatsangehoerigkeitKatalogEintragFilter, orte_sort, orte_filter, ortsteilSort, ortsteilFilter } from "~/utils/helfer";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<SchuelerIndividualdatenProps>();

	const schuljahr = computed<number>(() => props.schuelerListeManager().schuelerGetSchuljahrOrException());

	const hatKompetenzAnsehen = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN));
	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const data = computed<SchuelerStammdaten>(() => props.schuelerListeManager().daten());

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		resetTelefonnummer();
		closeModalTelefonnummer();
	}

	const selected = ref<SchuelerTelefon[]>([]);
	const newEntryTelefonnummer = ref<SchuelerTelefon>(new SchuelerTelefon());

	const columns: DataTableColumn[] = [
		{ key: "idTelefonArt", label: "Ansprechpartner" },
		{ key: "telefonnummer", label: "Telefonnummern" },
		{ key: "bemerkung", label: "Bemerkung", span: 2 },
		{ key: "istGesperrt", label: "Gesperrt", span: 1, align: "right" },
	];

	function getBezeichnungTelefonart(idTelefonArt: number): string {
		return props.mapTelefonArten.get(idTelefonArt)?.bezeichnung ?? "";
	}

	const selectedTelefonArt = computed<TelefonArt|null>({
		get: () => props.mapTelefonArten.get(newEntryTelefonnummer.value.idTelefonArt) ?? null,
		set: (selected) => newEntryTelefonnummer.value.idTelefonArt = (selected !== null) ? selected.id : 0,
	});

	enum Mode { ADD, PATCH, DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);
	const showModalTelefonnummer = ref<boolean>(false);

	function addTelefonnummer() {
		resetTelefonnummer();
		setMode(Mode.ADD);
		openModalTelefonnummer();
	}

	async function sendRequestTelefonnummer() {
		const { id, idSchueler, ...partialDataWithoutId } = newEntryTelefonnummer.value;
		const schuelerId = props.schuelerListeManager().daten().id;
		if (currentMode.value === Mode.ADD)
			await props.addSchuelerTelefoneintrag(partialDataWithoutId, schuelerId);
		if (currentMode.value === Mode.PATCH)
			await props.patchSchuelerTelefoneintrag(partialDataWithoutId, newEntryTelefonnummer.value.id);
		enterDefaultMode();
	}

	function patchTelefonnummer(telefonnummer: SchuelerTelefon) {
		resetTelefonnummer();
		setMode(Mode.PATCH);
		newEntryTelefonnummer.value.id = telefonnummer.id;
		newEntryTelefonnummer.value.idTelefonArt = telefonnummer.idTelefonArt;
		newEntryTelefonnummer.value.telefonnummer = telefonnummer.telefonnummer;
		newEntryTelefonnummer.value.bemerkung = telefonnummer.bemerkung;
		newEntryTelefonnummer.value.istGesperrt = telefonnummer.istGesperrt;
		openModalTelefonnummer();
	}

	async function deleteTelefonnummern() {
		if (selected.value.length === 0)
			return;
		const ids = new ArrayList<number>();
		for (const s of selected.value)
			ids.add(s.id);
		await props.deleteSchuelerTelefoneintrage(ids);
		selected.value = [];
	}

	function openModalTelefonnummer() {
		showModalTelefonnummer.value = true;
	}

	function closeModalTelefonnummer() {
		resetTelefonnummer();
		setMode(Mode.DEFAULT)
		showModalTelefonnummer.value = false;
	}

	function setMode(newMode: Mode) {
		return currentMode.value = newMode;
	}

	function resetTelefonnummer() {
		const defaultTelefon = new SchuelerTelefon();
		defaultTelefon.telefonnummer = '+49';
		const ersteTelefonArt = props.mapTelefonArten.values().next().value;
		defaultTelefon.idTelefonArt = ersteTelefonArt?.id ?? 0;
		newEntryTelefonnummer.value = defaultTelefon;
	}

	function istGeburtsdatumGueltig(strDate: string | null) {
		if (strDate === null || typeof strDate === 'number')
			return true;
		try {
			const date = DateUtils.extractFromDateISO8601(strDate);
			const curDate = new Date();
			const diffYear = curDate.getFullYear() - date[0];
			return (diffYear > 3) && (diffYear < 51);
		} catch (e) {
			return false;
		}
	}

	const geschlecht = computed<Geschlecht>({
		get: () => Geschlecht.fromValue(data.value.geschlecht) ?? Geschlecht.X,
		set: (value) => void props.patch({ geschlecht: value.id }),
	});

	const strasse = computed(() => AdressenUtils.combineStrasse(data.value.strassenname ?? "", data.value.hausnummer ?? "", data.value.hausnummerZusatz ?? ""))

	async function patchStrasse(value: string | null) {
		if (value !== null) {
			const vals = AdressenUtils.splitStrasse(value);
			await props.patch({ strassenname: vals[0], hausnummer: vals[1], hausnummerZusatz: vals[2] });
		}
	}

	const wohnortID = computed<OrtKatalogEintrag | undefined>({
		get: () => {
			const id = data.value.wohnortID;
			return id === null ? undefined : props.mapOrte.get(id);
		},
		set: (value) => void props.patch({ wohnortID: value === undefined ? null : value.id }),
	});

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result : Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values())
			if ((ortsteil.ort_id === null) || (ortsteil.ort_id === data.value.wohnortID))
				result.push(ortsteil);
		return result;
	});

	const ortsteilID = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => {
			const id = data.value.ortsteilID;
			return id === null ? undefined : props.mapOrtsteile.get(id);
		},
		set: (value) => void props.patch({ ortsteilID: value === undefined ? null : value.id }),
	});


	const staatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeitID) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ staatsangehoerigkeitID: value.historie().getLast().iso3 }),
	});

	const staatsangehoerigkeit2 = computed<Nationalitaeten | null>({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeit2ID),
		set: (value) => void props.patch({ staatsangehoerigkeit2ID: value?.historie().getLast().iso3 ?? null }),
	});

	const religion = computed<ReligionEintrag | undefined>({
		get: () => {
			const id = data.value.religionID;
			return id === null ? undefined : props.mapReligionen.get(id);
		},
		set: (value) => void props.patch({ religionID: value === undefined ? null : value.id }),
	});

	const druckeKonfessionAufZeugnisse = computed<boolean>({
		get: () => data.value.druckeKonfessionAufZeugnisse,
		set: (value) => void props.patch({ druckeKonfessionAufZeugnisse: value }),
	});


	const hatMigrationshintergrund = computed<boolean>(() => props.schuelerListeManager().daten().hatMigrationshintergrund);
	const max = new Date().getFullYear() + 1;
	const min = max - 100;

	const geburtsland = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.geburtsland) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ geburtsland: value.historie().getLast().iso3 }),
	});

	const geburtslandMutter = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.geburtslandMutter) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ geburtslandMutter: value.historie().getLast().iso3 }),
	});

	const geburtslandVater = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.geburtslandVater) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ geburtslandVater: value.historie().getLast().iso3 }),
	});

	const verkehrsprache = computed<Verkehrssprache>({
		get: () => Verkehrssprache.getByIsoKuerzel(data.value.verkehrspracheFamilie) || Verkehrssprache.data().getWertBySchluesselOrException("de"),
		set: (value) => void props.patch({ verkehrspracheFamilie: value.historie().getLast().iso3 }),
	});

	const inputStammschule = computed<SchulEintrag | undefined>({
		get: () => (data.value.externeSchulNr === null) ? undefined : (props.mapSchulen.get(data.value.externeSchulNr) || undefined),
		set: (value) => void props.patch({ externeSchulNr: value === undefined ? null : value.schulnummerStatistik }),
	});

	const inputFahrschuelerArtID = computed<Fahrschuelerart | undefined>({
		get: () => {
			const id = data.value.fahrschuelerArtID;
			return id === null ? undefined : props.mapFahrschuelerarten.get(id);
		},
		set: (value) => void props.patch({ fahrschuelerArtID: value === undefined ? null : value.id }),
	});

	const inputHaltestelleID = computed<Haltestelle | undefined>({
		get: () => {
			const id = data.value.haltestelleID;
			return id === null ? undefined : props.mapHaltestellen.get(id);
		},
		set: (value) => void props.patch({ haltestelleID: value === undefined ? null : value.id }),
	});

	const loading = ref<boolean>(false);

	async function downloadPDF() {
		const reportingParameter = new ReportingParameter();
		const listeIdsSchueler = new ArrayList<number>();
		listeIdsSchueler.add(props.schuelerListeManager().auswahlID());
		reportingParameter.reportvorlage = ReportingReportvorlage.SCHUELER_v_SCHULBESCHEINIGUNG.getBezeichnung();
		reportingParameter.idsHauptdaten = listeIdsSchueler;
		reportingParameter.einzelausgabeHauptdaten = true;
		reportingParameter.einzelausgabeDetaildaten = false;
		reportingParameter.sortierungHauptdaten = new ReportingSortierungDefinition();
		reportingParameter.sortierungHauptdaten.verwendeStandardsortierung = true;
		reportingParameter.detailLevel = 8;

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

</script>
