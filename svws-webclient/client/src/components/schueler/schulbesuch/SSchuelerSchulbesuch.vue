<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Vor der Aufnahme besucht">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Schule" :items="manager().schulenById.values()" :item-text="textSchule" autocomplete :item-filter="filterSchulenEintraege"
					:model-value="manager().getVorherigeSchule()" @update:model-value="v => manager().patchSchule(v, 'idVorherigeSchule')" removable />
				<svws-ui-button type="transparent" @click="goToSchule(manager().daten.idVorherigeSchule ?? -1)">
					<span class="icon i-ri-link" />Zur Schule
				</svws-ui-button>
				<svws-ui-text-input placeholder="allgemeine Herkunft" :model-value="manager().getVorigeAllgHerkunft()" readonly />
				<svws-ui-text-input v-autofocus placeholder="Statistik-Schulnummer" :statistics="true" readonly
					:model-value="manager().getVorherigeSchule()?.schulnummerStatistik ?? ' - '" />
				<svws-ui-text-input placeholder="Entlassen am" type="date" :model-value="manager().daten.vorigeEntlassdatum"
					@change="vorigeEntlassdatum => manager().doPatch({ vorigeEntlassdatum })" />
				<svws-ui-select title="Entlassjahrgang" :items="Jahrgaenge.values()" :model-value="manager().getEntlassjahrgang('vorigeEntlassjahrgang')"
					@update:model-value="v => manager().patchEntlassjahrgang(v, 'vorigeEntlassjahrgang')" :item-text="textJahrgang" removable />
				<svws-ui-text-input placeholder="Bemerkung" span="full" :model-value="manager().daten.vorigeBemerkung" :max-len="255"
					@change="v => { if ((v ?? '').length <= 255) manager().doPatch({ vorigeBemerkung : v }) } " />
				<svws-ui-spacing />
				<svws-ui-select title="Entlassgrund" :items="manager().entlassgruendeById" :item-text="v => v.bezeichnung" removable
					:model-value="manager().getEntlassgrund('vorigeEntlassgrundID')"
					@update:model-value="v => manager().patchEntlassgrund(v, 'vorigeEntlassgrundID')" />
				<svws-ui-text-input placeholder="höchster Abschluss, der von der anderen Schule mitgebracht wurde" disabled
					@change="vorigeAbschlussartID => manager().doPatch({ vorigeAbschlussartID })" :model-value="manager().daten.vorigeAbschlussartID" />
				<svws-ui-select class="col-span-full" title="Versetzung" :items="manager().getHerkunftsarten()" :item-text="textHerkunftsarten" removable
					:model-value="manager().getVorigeArtLetzteVersetzung()" @update:model-value="v => manager().patchVorigeArtLetzteVersetzung(v)" :statistics="true" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Entlassung von eigener Schule">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Entlassen am" type="date" :model-value="manager().daten.entlassungDatum"
					@change="entlassungDatum => manager().doPatch({ entlassungDatum })" />
				<svws-ui-select title="Entlassjahrgang" :items="Jahrgaenge.values()" :model-value="manager().getEntlassjahrgang('entlassungJahrgang')"
					@update:model-value="v => manager().patchEntlassjahrgang(v, 'entlassungJahrgang')" :item-text="textJahrgang" removable />
				<svws-ui-select title="Entlassgrund" :items="manager().entlassgruendeById" :item-text="v => v.bezeichnung" removable
					:model-value="manager().getEntlassgrund('entlassungGrundID')"
					@update:model-value="v => manager().patchEntlassgrund(v, 'entlassungGrundID')" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Art des Abschlusses" span="full" :model-value="manager().daten.entlassungAbschlussartID" disabled
					@change="entlassungAbschlussartID => manager().doPatch({ entlassungAbschlussartID })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
			<template #actions>
				<svws-ui-checkbox :model-value="manager().daten.aufnehmendBestaetigt === true" :indeterminate="manager().daten.aufnehmendBestaetigt === null"
					@update:model-value="aufnehmendBestaetigt => manager().doPatch({ aufnehmendBestaetigt })" focus-class-content>
					Aufnahme bestätigt
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Schule" :items="manager().schulenById.values()" :item-text="textSchule" autocomplete
					:model-value="manager().schulenById.get(manager().daten.idAufnehmendeSchule ?? -1)" :item-filter="filterSchulenEintraege" removable
					@update:model-value="v => manager().patchSchule(v, 'idAufnehmendeSchule')" />
				<svws-ui-button type="transparent" @click="goToSchule(manager().daten.idAufnehmendeSchule ?? -1)">
					<span class="icon i-ri-link" />Zur Schule
				</svws-ui-button>

				<svws-ui-text-input placeholder="Wechseldatum" :model-value="manager().daten.aufnehmendWechseldatum"
					@change="aufnehmendWechseldatum => manager().doPatch({ aufnehmendWechseldatum })" type="date" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Grundschulbesuch">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number class="contentFocusField" placeholder="Einschulung" :model-value="manager().daten.grundschuleEinschulungsjahr"
					@change="grundschuleEinschulungsjahr => manager().doPatch({ grundschuleEinschulungsjahr })" :min="1900" :max="2100" />
				<svws-ui-select disabled title="Einschulungsart" :items="Einschulungsart.values()" :model-value="manager().getEinschulungsart()"
					@update:model-value="v => manager().doPatch({ grundschuleEinschulungsartID : v?.daten.id ?? null })" :item-text="textEinschulungsart" />
				<svws-ui-select title="EP-Jahre" :items="PrimarstufeSchuleingangsphaseBesuchsjahre.values()" removable :item-text="textEPJahre" :model-value="manager().getEPJahre()"
					@update:model-value="v => manager().doPatch({grundschuleJahreEingangsphase : Number(v?.daten(manager().schuljahr)?.schluessel ?? null)})" />
				<svws-ui-select title="Übergangsempfehlung Jg. 5" :items="Uebergangsempfehlung.values()" :item-text="textUebergangsempfehlung" removable
					@update:model-value="v => manager().doPatch({kuerzelGrundschuleUebergangsempfehlung : v?.daten(manager().schuljahr)?.kuerzel ?? null})"
					:model-value="manager().getUebergangsempfehlung()" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sekundarstufe I">
			<svws-ui-input-wrapper>
				<svws-ui-input-number class="contentFocusField" placeholder="Jahr Wechsel Sek I" :model-value="manager().daten.sekIWechsel"
					@change="sekIWechsel => manager().doPatch({ sekIWechsel })" :min="1900" :max="2100" />
				<svws-ui-select title="Erste Schulform Sek I" :items="Schulform.values()" :item-text="textSchulformSek1" :model-value="manager().getSchulformSek1()"
					@update:model-value="v => manager().doPatch({ sekIErsteSchulform : v?.daten(manager().schuljahr)?.kuerzel ?? null })" />
				<svws-ui-input-number placeholder="Jahr Wechsel Sek II" :model-value="manager().daten.sekIIWechsel"
					@change="sekIIWechsel => manager().doPatch({ sekIIWechsel })" :min="1900" :max="2100" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Besondere Merkmale für die Statistik">
			<svws-ui-table :clickable="true" @update:clicked="v => patchMerkmal(v)" :columns="columnsMerkmale" :items="merkmale"
				v-model="auswahlMerkmale" :selectable="hatUpdateKompetenz">
				<template #cell(merkmal)="{ rowData: s }">
					<span>{{ manager().merkmaleById.get(s.idMerkmal ?? -1)?.bezeichnung ?? " - " }}</span>
				</template>
				<template #cell(datumVon)="{ rowData: s }">
					<span>{{ formatDate(s.datumVon) }}</span>
				</template>
				<template #cell(datumBis)="{ rowData: s }">
					<span>{{ formatDate(s.datumBis) }}</span>
				</template>
				<template #actions v-if="hatUpdateKompetenz">
					<svws-ui-button @click="deleteAuswahlMerkmale" type="trash" :disabled="auswahlMerkmale.length === 0" />
					<svws-ui-button @click="addMerkmal" type="icon" title="Merkmal hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
				</template>
			</svws-ui-table>
			<!-- Modal zum Erzeugen und Patchen eines Eintrags der besonderen Merkmale für die Statistik -->
			<svws-ui-modal :show="showModalMerkmal" @update:show="closeModalMerkmal">
				<template #modalTitle>Merkmal hinzufügen</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2" style="text-align: left">
						<svws-ui-select title="Merkmal" :items="manager().merkmaleById.values()" :item-text="textMerkmal" removable required
							@update:model-value="v => newEntryMerkmal.idMerkmal = v?.id ?? null"
							:model-value="manager().merkmaleById.get(newEntryMerkmal.idMerkmal ?? -1)" />
						<svws-ui-text-input placeholder="Von" type="date" :max-date="newEntryMerkmal.datumBis" v-model="newEntryMerkmal.datumVon" />
						<svws-ui-text-input placeholder="Bis" type="date" :min-date="newEntryMerkmal.datumVon" v-model="newEntryMerkmal.datumBis" />
					</svws-ui-input-wrapper>
					<div class="mt-7 flex flex-row gap-4 justify end">
						<svws-ui-button type="secondary" @click="closeModalMerkmal">Abbrechen</svws-ui-button>
						<svws-ui-button @click="sendRequestMerkmal(currentMode)" :disabled="!newEntryMerkmal.idMerkmal">Speichern</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
		</svws-ui-content-card>
		<svws-ui-content-card title="Alle bisher besuchten Schulen" class="col-span-full">
			<!-- Tabelle der bisher besuchten Schulen -->
			<svws-ui-table :clickable="true" @update:clicked="v => patchBisherigeSchule(v)" :columns="columnsBisherigeSchulen" :items="bisherigeSchulen"
				v-model="auswahlBisherigeSchulen" :selectable="hatUpdateKompetenz">
				<template #cell(schulform)="{ rowData }">
					<span>{{ Schulform.data().getEintragByID(manager().schulenById.get(rowData.idSchule ?? -1)?.idSchulform ?? -1)?.kuerzel ?? ' - ' }}</span>
				</template>
				<template #cell(schulname)="{ rowData }">
					<span>{{ manager().schulenById.get(rowData.idSchule ?? -1)?.name ?? '-' }}</span>
				</template>
				<template #cell(datumVon)="{ rowData }">
					<span>{{ formatDate(rowData.datumVon) }}</span>
				</template>
				<template #cell(datumBis)="{ rowData }">
					<span>{{ formatDate(rowData.datumBis) }}</span>
				</template>
				<!-- Todo: JahrgangVon und JahrgangBis Siehe Issue #2214 -->
				<template #cell(jahrgangVon)>
					<span>-</span>
				</template>
				<template #cell(jahrgangBis)>
					<span>-</span>
				</template>
				<template #cell(schulgliederung)="{ rowData }">
					<span>{{ textSchulgliederung(rowData.schulgliederung) }}</span>
				</template>
				<template #cell(entlassart)="{ rowData }">
					<span>{{ manager().entlassgruendeById.get(rowData.entlassgrundID ?? -1)?.bezeichnung ?? '-' }}</span>
				</template>
				<template #actions v-if="hatUpdateKompetenz">
					<svws-ui-button @click="deleteAuswahlBisherigeSchulen" type="trash" :disabled="auswahlBisherigeSchulen.length === 0" />
					<svws-ui-button @click="addBisherigeSchule" type="icon" title="Schuleintrag hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
				</template>
			</svws-ui-table>
			<!-- Modal zum Erzeugen und Patchen eines Eintrags der bisher besuchten Schulen -->
			<svws-ui-modal :show="showModalBisherigeSchule" @update:show="closeModalBisherigeSchule">
				<template #modalTitle>Schule hinzufügen</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2" style="text-align: left">
						<svws-ui-select class="col-span-full" title="Schule" :items="manager().schulenById.values()" autocomplete removable
							:item-filter="filterSchulenEintraege" @update:model-value="v => newEntryBisherigeSchule.idSchule = v?.id ?? null"
							:model-value="manager().schulenById.get(newEntryBisherigeSchule.idSchule ?? -1)" :item-text="textSchule" />
						<svws-ui-text-input span="full" placeholder="Adresse" readonly :model-value="adresseBisherigeSchule" />
						<svws-ui-text-input placeholder="Schulnummer" :statistics="true" readonly
							:model-value="manager().schulenById.get(newEntryBisherigeSchule.idSchule ?? -1)?.schulnummerStatistik ?? '-'" />
						<svws-ui-text-input placeholder="Schulform" readonly :model-value="selectedSchulformBisherigeSchule?.text" />
						<svws-ui-spacing />
						<svws-ui-text-input placeholder="Start des Schulbesuchs" type="date" :max-date="newEntryBisherigeSchuleDatumBis" v-model="newEntryBisherigeSchule.datumVon" />
						<svws-ui-text-input placeholder="Ende des Schulbesuchs" type="date" :min-date="newEntryBisherigeSchule.datumVon" v-model="newEntryBisherigeSchuleDatumBis" />
						<svws-ui-select class="col-span-full" title="Schulgliederung" :items="schulgliederungenBisherigeSchule"
							:disabled="(!newEntryBisherigeSchule.datumBis || !selectedSchulformBisherigeSchule)" autocomplete
							:item-filter="coreTypeDataFilter" v-model="schulgliederungBisherigeSchule"
							:item-text="v => (v as SchulgliederungKatalogEintrag).text" />
						<!-- Todo: JahrgangVon und JahrgangBis Siehe Issue #2214 -->
						<svws-ui-text-input placeholder="JahrgangVon" disabled />
						<svws-ui-text-input placeholder="JahrgangBis" disabled />
					</svws-ui-input-wrapper>
					<div class="mt-7 flex flex-row gap-4 justify end">
						<svws-ui-button type="secondary" @click="closeModalBisherigeSchule">Abbrechen</svws-ui-button>
						<svws-ui-button @click="sendRequestBisherigeSchule(currentMode)" :disabled="!newEntryBisherigeSchule.idSchule">Speichern</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz, Einschulungsart, Jahrgaenge, PrimarstufeSchuleingangsphaseBesuchsjahre, SchuelerSchulbesuchSchule, Schulform, Schulgliederung,
		Uebergangsempfehlung, SchulEintrag, AdressenUtils, ArrayList, SchuelerSchulbesuchMerkmal } from "@core";
	import type { Herkunftsarten, SchulformKatalogEintrag, SchulgliederungKatalogEintrag, Merkmal } from "@core";
	import type { SchuelerSchulbesuchProps } from './SSchuelerSchulbesuchProps';
	import type { DataTableColumn } from "@ui";
	import { coreTypeDataFilter, filterSchulenEintraege, formatDate } from "~/utils/helfer";
	import { ref, computed, watch } from "vue";

	const props = defineProps<SchuelerSchulbesuchProps>();
	const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		resetBisherigeSchule();
		resetMerkmal();
		closeModalBisherigeSchule();
		closeModalMerkmal();
	}

	// --- Tabelle Merkmale ---
	const merkmale = computed(() => [...props.manager().daten.merkmale])
	const newEntryMerkmal = ref<SchuelerSchulbesuchMerkmal>(new SchuelerSchulbesuchMerkmal());
	const columnsMerkmale: DataTableColumn[] = [
		{ key: "merkmal", label: "Merkmal"},
		{ key: "datumVon", label: "Von"},
		{ key: "datumBis", label: "Bis"},
	]

	// --- Merkmale Modal ---
	const showModalMerkmal = ref<boolean>(false);
	function addMerkmal() {
		resetMerkmal();
		setMode(Mode.ADD);
		openModalMerkmal();
	}

	function openModalMerkmal() {
		showModalMerkmal.value = true;
	}

	function resetMerkmal() {
		newEntryMerkmal.value = new SchuelerSchulbesuchMerkmal();
	}

	function closeModalMerkmal() {
		resetMerkmal();
		setMode(Mode.DEFAULT)
		showModalMerkmal.value = false;
	}

	// --- api calls Merkmal ---
	async function sendRequestMerkmal(type : Mode) {
		const { id, ...partialDataWithoutId } = newEntryMerkmal.value;
		if (type === Mode.ADD)
			await props.addSchuelerSchulbesuchMerkmal(partialDataWithoutId);
		if (type === Mode.PATCH)
			await props.patchSchuelerSchulbesuchMerkmal(newEntryMerkmal.value.id, partialDataWithoutId);
		enterDefaultMode();
	}

	// Patch
	function patchMerkmal(merkmal : SchuelerSchulbesuchMerkmal) {
		resetMerkmal();
		setMode(Mode.PATCH);
		newEntryMerkmal.value.id = merkmal.id;
		newEntryMerkmal.value.idMerkmal = merkmal.idMerkmal;
		newEntryMerkmal.value.datumVon = merkmal.datumVon;
		newEntryMerkmal.value.datumBis = merkmal.datumBis;
		openModalMerkmal();
	}

	// Delete
	const auswahlMerkmale = ref<SchuelerSchulbesuchMerkmal[]>([]);
	async function deleteAuswahlMerkmale() {
		if (auswahlMerkmale.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of auswahlMerkmale.value) {
			ids.add(s.id);
		}
		await props.deleteSchuelerSchulbesuchMerkmale(ids);
		auswahlMerkmale.value = [];
	}

	// --- Table Bisherige Schulen ---
	const bisherigeSchulen = computed(() => [...props.manager().daten.alleSchulen])
	const newEntryBisherigeSchule = ref<SchuelerSchulbesuchSchule>(new SchuelerSchulbesuchSchule());
	enum Mode { ADD, PATCH , DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);
	const columnsBisherigeSchulen: DataTableColumn[] = [
		{ key: "schulform", label: "Schulform", span: 0.2, align: "center" },
		{ key: "schulname", label: "Schulname" },
		{ key: "datumVon", label: "Aufnahme-Datum", span: 0.25, align: "center" },
		{ key: "datumBis", label: "Entlass-Datum", span: 0.25, align: "center" },
		{ key: "jahrgangVon", label: "Jahrgang Von", span: 0.2, align: "center" },
		{ key: "jahrgangBis", label: "Jahrgang Bis", span: 0.2, align: "center" },
		{ key: "schulgliederung", label: "Schulgliederung", align: "center" },
		{ key: "entlassart", label: "Entlassart", span: 0.2, align: "center" },
	]
	// datumBis (+ watcher) wird als computed benötigt, damit die Vorauswahl der Schulgliederung direkt in das Datenobjekt (newEntryBisherigeSchule) geschrieben wird.
	const newEntryBisherigeSchuleDatumBis = computed({
		get: () => newEntryBisherigeSchule.value.datumBis,
		set: (val: string) => newEntryBisherigeSchule.value.datumBis = val,
	})

	watch(newEntryBisherigeSchuleDatumBis, () => {
		if (currentMode.value === Mode.ADD)
			newEntryBisherigeSchule.value.schulgliederung = schulgliederungenBisherigeSchule.value[0]?.schluessel ?? null;
	})

	// --- Bisherige Schulen Modal ---
	const showModalBisherigeSchule = ref<boolean>(false);
	function addBisherigeSchule() {
		resetBisherigeSchule();
		setMode(Mode.ADD);
		openModalBisherigeSchule();
	}

	function setMode(newMode: Mode) {
		return currentMode.value = newMode;
	}

	function openModalBisherigeSchule() {
		showModalBisherigeSchule.value = true;
	}

	function closeModalBisherigeSchule() {
		resetBisherigeSchule();
		setMode(Mode.DEFAULT)
		showModalBisherigeSchule.value = false;
	}

	function resetBisherigeSchule() {
		newEntryBisherigeSchule.value = new SchuelerSchulbesuchSchule();
	}

	const selectedSchuleForNewEntryBisherigeSchule = computed<SchulEintrag>(() => {
		if (newEntryBisherigeSchule.value.idSchule === null)
			return new SchulEintrag();
		const schule = props.manager().schulenById.get(newEntryBisherigeSchule.value.idSchule);
		return (schule === undefined) ? new SchulEintrag() : schule;
	});

	const selectedSchulformBisherigeSchule = computed<SchulformKatalogEintrag | null>(() => {
		if (selectedSchuleForNewEntryBisherigeSchule.value.idSchulform === null)
			return null;
		return Schulform.data().getEintragByID(selectedSchuleForNewEntryBisherigeSchule.value.idSchulform);
	});

	const schulgliederungenBisherigeSchule = computed<SchulgliederungKatalogEintrag[]>(() => {
		if ((!selectedSchulformBisherigeSchule.value) || (newEntryBisherigeSchule.value.datumBis === null))
			return [];
		const jahr = Number(newEntryBisherigeSchule.value.datumBis.toString().substring(0, 4));
		const gliederungenByJahr = Schulgliederung.data().getEintraegeBySchuljahr(jahr);
		const result = [];
		for (const g of gliederungenByJahr) {
			if (g.schulformen.contains(selectedSchulformBisherigeSchule.value.kuerzel))
				result.push(g);
		}
		return result;
	});

	const schulgliederungBisherigeSchule = computed({
		get: () => newEntryBisherigeSchule.value.schulgliederung === "" ? schulgliederungenBisherigeSchule.value[0] : findGliederung(),
		set: (v : SchulgliederungKatalogEintrag) => newEntryBisherigeSchule.value.schulgliederung = v.schluessel,
	});

	function findGliederung() {
		for (const g of schulgliederungenBisherigeSchule.value) {
			if (g.schluessel === newEntryBisherigeSchule.value.schulgliederung)
				return g;
		}
	}

	const adresseBisherigeSchule = computed<string>(() => {
		if (selectedSchuleForNewEntryBisherigeSchule.value.id === -1)
			return '';
		const strasse = AdressenUtils.combineStrasse(selectedSchuleForNewEntryBisherigeSchule.value.strassenname,
			selectedSchuleForNewEntryBisherigeSchule.value.hausnummer,
			selectedSchuleForNewEntryBisherigeSchule.value.zusatzHausnummer);
		return strasse + ', ' + selectedSchuleForNewEntryBisherigeSchule.value.plz + ' ' +
			selectedSchuleForNewEntryBisherigeSchule.value.ort;
	});

	// --- api calls Bisherige Schulen ---
	async function sendRequestBisherigeSchule(type : Mode) {
		const { id, ...partialDataWithoutId } = newEntryBisherigeSchule.value;
		if (type === Mode.ADD)
			await props.addSchuelerSchulbesuchSchule(partialDataWithoutId);
		if (type === Mode.PATCH)
			await props.patchSchuelerSchulbesuchSchule(newEntryBisherigeSchule.value.id, partialDataWithoutId);
		enterDefaultMode();
	}

	// Patch
	function patchBisherigeSchule(schule : SchuelerSchulbesuchSchule) {
		resetBisherigeSchule();
		setMode(Mode.PATCH);
		newEntryBisherigeSchule.value.id = schule.id;
		newEntryBisherigeSchule.value.idSchule = schule.idSchule;
		newEntryBisherigeSchule.value.datumVon = schule.datumVon;
		newEntryBisherigeSchule.value.datumBis = schule.datumBis;
		newEntryBisherigeSchule.value.schulgliederung = schule.schulgliederung;
		newEntryBisherigeSchule.value.jahrgangVon = schule.jahrgangVon;
		newEntryBisherigeSchule.value.jahrgangBis = schule.jahrgangBis;
		openModalBisherigeSchule();
	}

	// Delete
	const auswahlBisherigeSchulen = ref<SchuelerSchulbesuchSchule[]>([]);
	async function deleteAuswahlBisherigeSchulen() {
		if (auswahlBisherigeSchulen.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of auswahlBisherigeSchulen.value) {
			ids.add(s.id);
		}
		await props.deleteSchuelerSchulbesuchSchulen(ids);
		auswahlBisherigeSchulen.value = [];
	}

	// --- allgemeiner Abschnitt ---
	function textHerkunftsarten(h: Herkunftsarten) {
		return h.getBezeichnung(props.manager().schuljahr, props.manager().getVorigeSchulform() || Schulform.G) + ' (' + h.daten.kuerzel + ')';
	}

	function textSchule(s: SchulEintrag) {
		return s.name;
	}

	function textMerkmal(m : Merkmal) {
		if (m.bezeichnung === null)
			return '';
		return m.bezeichnung;
	}

	function textJahrgang(j : Jahrgaenge) {
		return j.daten(props.manager().schuljahr)?.kuerzel ?? '-';
	}

	function textEinschulungsart(e : Einschulungsart) {
		return e.daten.kuerzel + ' - '+ e.daten.beschreibung;
	}

	function textEPJahre(p : PrimarstufeSchuleingangsphaseBesuchsjahre) {
		return p.daten(props.manager().schuljahr)?.text ?? '-';
	}

	function textUebergangsempfehlung(u : Uebergangsempfehlung) {
		return u.daten(props.manager().schuljahr)?.text ?? '-';
	}

	function textSchulformSek1(s : Schulform) {
		return s.daten(props.manager().schuljahr)?.text ?? '-';
	}

	function textSchulgliederung(v : string | null) {
		if (v === null)
			return '-';
		const wertBySchluessel = Schulgliederung.data().getWertBySchluessel(v);
		if (!wertBySchluessel)
			return '-';
		const historienEintrag = wertBySchluessel.daten(props.manager().schuljahr);
		if (!historienEintrag)
			return '-'
		return historienEintrag.text;
	}

</script>
