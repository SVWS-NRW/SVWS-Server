<template>
	<Teleport to=".svws-ui-header--actions" defer>
		<svws-ui-modal-hilfe> <hilfe-schueler-schulbesuch /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Vor der Aufnahme besucht">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Schule" :items="manager().schulenById.values()" :item-text="textSchule" autocomplete :item-filter="filterSchulenEintraege"
					:model-value="manager().getVorherigeSchule()" @update:model-value="v => manager().patchSchule(v, 'idVorherigeSchule')" removable
					statistics :readonly />
				<svws-ui-button type="transparent" @click="goToSchule(manager().daten.idVorherigeSchule ?? -1)">
					<span class="icon i-ri-link" />Zur Schule
				</svws-ui-button>
				<svws-ui-text-input placeholder="allgemeine Herkunft" :model-value="manager().getVorigeAllgHerkunft()" readonly statistics />
				<svws-ui-text-input v-autofocus placeholder="Statistik-Schulnummer" :statistics="true" readonly
					:model-value="manager().getVorherigeSchule()?.schulnummerStatistik ?? ' - '" />
				<svws-ui-text-input placeholder="Entlassen am" type="date" :model-value="manager().daten.vorigeEntlassdatum" statistics :readonly
					@change="vorigeEntlassdatum => manager().doPatch({ vorigeEntlassdatum })" />
				<svws-ui-select title="Entlassjahrgang" :items="manager().getJahrgaengeBySchulform(manager().getVorigeSchulform())" :item-text="textJahrgang"
					:model-value="manager().getEntlassjahrgang('vorigeEntlassjahrgang')" :disabled="manager().getVorherigeSchule() === undefined"
					@update:model-value="v => manager().patchEntlassjahrgang(v, 'vorigeEntlassjahrgang')" removable statistics :readonly />
				<svws-ui-text-input placeholder="Bemerkung" span="full" :model-value="manager().daten.vorigeBemerkung" :max-len="255"
					@change="v => { if ((v ?? '').length <= 255) manager().doPatch({ vorigeBemerkung : v }) } " :readonly />
				<svws-ui-spacing />
				<svws-ui-select title="Entlassgrund" :items="manager().entlassgruendeById.values()" :item-text="v => v.bezeichnung" removable
					:model-value="manager().getEntlassgrund('vorigeEntlassgrundID')" :readonly
					@update:model-value="v => manager().patchEntlassgrund(v, 'vorigeEntlassgrundID')" />
				<svws-ui-text-input placeholder="höchster Abschluss, der von der anderen Schule mitgebracht wurde" disabled statistics :readonly
					@change="vorigeAbschlussartID => manager().doPatch({ vorigeAbschlussartID })" :model-value="manager().daten.vorigeAbschlussartID" />
				<svws-ui-select class="col-span-full" title="Versetzung" :items="manager().getHerkunftsarten()" :item-text="textHerkunftsarten" removable :readonly
					:model-value="manager().getVorigeArtLetzteVersetzung()" @update:model-value="v => manager().patchVorigeArtLetzteVersetzung(v)" statistics />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Entlassung von eigener Schule">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Entlassen am" type="date" :model-value="manager().daten.entlassungDatum"
					@change="entlassungDatum => manager().doPatch({ entlassungDatum })" statistics :readonly />
				<svws-ui-select title="Entlassjahrgang" :items="manager().getJahrgaengeBySchulform(props.schulform)"
					:model-value="manager().getEntlassjahrgang('entlassungJahrgang')" :readonly
					@update:model-value="v => manager().patchEntlassjahrgang(v, 'entlassungJahrgang')" :item-text="textJahrgang" removable />
				<svws-ui-select title="Entlassgrund" :items="manager().entlassgruendeById.values()" :item-text="v => v.bezeichnung" removable
					:model-value="manager().getEntlassgrund('entlassungGrundID')" :readonly
					@update:model-value="v => manager().patchEntlassgrund(v, 'entlassungGrundID')" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Art des Abschlusses" span="full" :model-value="manager().daten.entlassungAbschlussartID" disabled
					@change="entlassungAbschlussartID => manager().doPatch({ entlassungAbschlussartID })" statistics :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Kindergartenbesuch" v-if="schuleHatPrimarstufe">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Name des Kindergartens" :items="manager().kindergaertenById.values()" :item-text="i => i.bezeichnung"
					:model-value="manager().kindergaertenById.get(manager().daten.idKindergarten?? -1)"
					@update:model-value="v => manager().doPatch({ idKindergarten: v?.id ?? null})" removable />
				<svws-ui-select title="Dauer des Kindergartenbesuchs" :items="Kindergartenbesuch.values()" :item-text="textDauerKindergartenbesuch" :readonly
					:model-value="manager().getDauerKindergartenbesuch()" removeable
					@update:model-value="v => manager().doPatch({ idDauerKindergartenbesuch: v?.daten(manager().schuljahr)?.id ?? null })" />
				<svws-ui-spacing />
				<svws-ui-checkbox title="Verpflichtung f. Sprachförderkurs" :model-value="manager().daten.verpflichtungSprachfoerderkurs"
					@update:model-value="verpflichtungSprachfoerderkurs => manager().doPatch({ verpflichtungSprachfoerderkurs })">
					Verpflichtung für Sprachförderkurs
				</svws-ui-checkbox>
				<svws-ui-checkbox title="Teilnahme an Sprachförderkurs" :model-value="manager().daten.teilnahmeSprachfoerderkurs"
					@update:model-value="teilnahmeSprachfoerderkurs => manager().doPatch({ teilnahmeSprachfoerderkurs })">
					Teilnahme an Sprachförderkurs
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
			<template #actions>
				<svws-ui-checkbox :model-value="manager().daten.aufnehmendBestaetigt === true" :indeterminate="manager().daten.aufnehmendBestaetigt === null"
					@update:model-value="aufnehmendBestaetigt => manager().doPatch({ aufnehmendBestaetigt })" focus-class-content :readonly>
					Aufnahme bestätigt
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Schule" :items="manager().schulenById.values()" :item-text="textSchule" autocomplete :readonly
					:model-value="manager().schulenById.get(manager().daten.idAufnehmendeSchule ?? -1)" :item-filter="filterSchulenEintraege" removable
					@update:model-value="v => manager().patchSchule(v, 'idAufnehmendeSchule')" />
				<svws-ui-button type="transparent" @click="goToSchule(manager().daten.idAufnehmendeSchule ?? -1)" :readonly>
					<span class="icon i-ri-link" />Zur Schule
				</svws-ui-button>

				<svws-ui-text-input placeholder="Wechseldatum" :model-value="manager().daten.aufnehmendWechseldatum" :readonly
					@change="aufnehmendWechseldatum => manager().doPatch({ aufnehmendWechseldatum })" type="date" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Grundschulbesuch">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number class="contentFocusField" placeholder="Einschulung" :model-value="manager().daten.grundschuleEinschulungsjahr" :readonly
					@change="grundschuleEinschulungsjahr => manager().doPatch({ grundschuleEinschulungsjahr })" :min="1900" :max="2100" statistics />
				<svws-ui-select title="Einschulungsart" :items="Einschulungsart.values()" :model-value="manager().getEinschulungsart()" statistics removable
					@update:model-value="v => manager().doPatch({ grundschuleEinschulungsartID : v?.daten(manager().schuljahr)?.id ?? null })"
					:item-text="textEinschulungsart" :readonly />
				<svws-ui-select title="EP-Jahre" :items="PrimarstufeSchuleingangsphaseBesuchsjahre.values()" removable :item-text="textEPJahre"
					@update:model-value="v => manager().doPatch({ idGrundschuleJahreEingangsphase : v?.daten(manager().schuljahr)?.id ?? null })"
					:model-value="manager().getEPJahre()" :readonly />
				<svws-ui-select title="Übergangsempfehlung Jg. 5" :items="Uebergangsempfehlung.values()" :item-text="textUebergangsempfehlung" removable
					@update:model-value="v => manager().doPatch({kuerzelGrundschuleUebergangsempfehlung : v?.daten(manager().schuljahr)?.kuerzel ?? null})"
					:model-value="manager().getUebergangsempfehlung()" statistics :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sekundarstufe I">
			<svws-ui-input-wrapper>
				<svws-ui-input-number class="contentFocusField" placeholder="Jahr Wechsel Sek I" :model-value="manager().daten.sekIWechsel" :readonly
					@change="sekIWechsel => manager().doPatch({ sekIWechsel })" :min="1900" :max="2100" statistics />
				<svws-ui-select title="Erste Schulform Sek I" :items="Schulform.values()" :item-text="textSchulformSek1" :model-value="manager().getSchulformSek1()"
					@update:model-value="v => manager().doPatch({ sekIErsteSchulform : v?.daten(manager().schuljahr)?.kuerzel ?? null })" :readonly />
				<svws-ui-input-number placeholder="Jahr Wechsel Sek II" :model-value="manager().daten.sekIIWechsel" :readonly
					@change="sekIIWechsel => manager().doPatch({ sekIIWechsel })" :min="1900" :max="2100" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Besondere Merkmale für die Statistik">
			<svws-ui-table clickable @update:clicked="v => patchMerkmal(v)" :columns="columnsMerkmale" :items="merkmale" :readonly
				v-model="auswahlMerkmale" :selectable="hatKompetenzUpdate">
				<template #cell(merkmal)="{ rowData: s }">
					<span>{{ manager().merkmaleById.get(s.idMerkmal ?? -1)?.bezeichnung ?? " - " }}</span>
				</template>
				<template #cell(datumVon)="{ rowData: s }">
					<span>{{ formatDate(s.datumVon) }}</span>
				</template>
				<template #cell(datumBis)="{ rowData: s }">
					<span>{{ formatDate(s.datumBis) }}</span>
				</template>
				<template #actions v-if="hatKompetenzUpdate">
					<svws-ui-button @click="deleteAuswahlMerkmale" type="trash" :disabled="(auswahlMerkmale.length === 0) || !hatKompetenzUpdate" />
					<svws-ui-button @click="addMerkmal" :disabled="!hatKompetenzUpdate" type="icon" title="Merkmal hinzufügen">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</template>
			</svws-ui-table>
			<!-- Modal zum Erzeugen und Patchen eines Eintrags der besonderen Merkmale für die Statistik -->
			<svws-ui-modal :show="showModalMerkmal" @update:show="closeModalMerkmal">
				<template #modalTitle>Merkmal hinzufügen</template>
				<template #modalContent>
					<svws-ui-select title="Merkmal" class="pb-4" :items="merkmaleFilteredNotCreated" :item-text="textMerkmal" removable required
						@update:model-value="v => newEntryMerkmal.idMerkmal = v?.id ?? null" statistics :readonly
						:model-value="manager().merkmaleById.get(newEntryMerkmal.idMerkmal ?? -1)" />
					<svws-ui-input-wrapper :grid="2" style="text-align: left">
						<svws-ui-text-input placeholder="Von" type="date" :max-date="newEntryMerkmal.datumBis ?? undefined" v-model="newEntryMerkmal.datumVon"
							:readonly />
						<svws-ui-text-input placeholder="Bis" type="date" :min-date="newEntryMerkmal.datumVon ?? undefined" v-model="newEntryMerkmal.datumBis"
							:readonly />
					</svws-ui-input-wrapper>
					<div class="mt-7 flex flex-row gap-4 justify end">
						<svws-ui-button type="secondary" @click="closeModalMerkmal">Abbrechen</svws-ui-button>
						<svws-ui-button @click="sendRequestMerkmal(currentMode)" :disabled="!newEntryMerkmal.idMerkmal || !hatKompetenzUpdate">
							Speichern
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
		</svws-ui-content-card>
		<svws-ui-content-card title="Alle bisher besuchten Schulen" class="col-span-full">
			<!-- Tabelle der bisher besuchten Schulen -->
			<svws-ui-table clickable @update:clicked="v => patchBisherigeSchule(v)" :columns="columnsBisherigeSchulen" :items="bisherigeSchulen"
				v-model="auswahlBisherigeSchulen" :selectable="hatKompetenzUpdate" :readonly>
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
				<template #cell(jahrgangVon)="{ rowData }">
					<span>{{ rowData.jahrgangVon }}</span>
				</template>
				<template #cell(jahrgangBis)="{ rowData }">
					<span>{{ rowData.jahrgangBis }}</span>
				</template>
				<template #cell(schulgliederung)="{ rowData }">
					<span>{{ textSchulgliederung(rowData.schulgliederung) }}</span>
				</template>
				<template #cell(entlassart)="{ rowData }">
					<span>{{ manager().entlassgruendeById.get(rowData.entlassgrundID ?? -1)?.bezeichnung ?? '-' }}</span>
				</template>
				<template #actions v-if="hatKompetenzUpdate">
					<svws-ui-button @click="deleteAuswahlBisherigeSchulen" type="trash" :disabled="(auswahlBisherigeSchulen.length === 0) || !hatKompetenzUpdate" />
					<svws-ui-button @click="addBisherigeSchule" :disabled="!hatKompetenzUpdate" type="icon" title="Schuleintrag hinzufügen">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</template>
			</svws-ui-table>
			<!-- Modal zum Erzeugen und Patchen eines Eintrags der bisher besuchten Schulen -->
			<svws-ui-modal :show="showModalBisherigeSchule" @update:show="closeModalBisherigeSchule">
				<template #modalTitle>Schule hinzufügen</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2" style="text-align: left">
						<svws-ui-select class="col-span-full" title="Schule" :items="manager().schulenById.values()" autocomplete removable :readonly
							:item-filter="filterSchulenEintraege" @update:model-value="v => newEntryBisherigeSchule.idSchule = v?.id ?? null"
							:model-value="manager().schulenById.get(newEntryBisherigeSchule.idSchule ?? -1)" :item-text="textSchule" />
						<svws-ui-text-input span="full" placeholder="Adresse" readonly :model-value="adresseBisherigeSchule" />
						<svws-ui-text-input placeholder="Schulnummer" statistics readonly
							:model-value="manager().schulenById.get(newEntryBisherigeSchule.idSchule ?? -1)?.schulnummerStatistik ?? '-'" />
						<svws-ui-text-input placeholder="Schulform" readonly :model-value="schulformEintragSelectedSchuleNewEntryBisherigeSchule?.text" />
						<svws-ui-spacing />
						<svws-ui-text-input placeholder="Start des Schulbesuchs" type="date" :max-date="newEntryBisherigeSchuleDatumBis ?? undefined"
							v-model="newEntryBisherigeSchule.datumVon" :readonly />
						<svws-ui-text-input placeholder="Ende des Schulbesuchs" type="date" :min-date="newEntryBisherigeSchule.datumVon ?? undefined"
							v-model="newEntryBisherigeSchuleDatumBis" :readonly />
						<ui-select label="Jahrgang von" :manager="jahrgangVonManager"
							:model-value="Jahrgaenge.data().getWertBySchluessel(newEntryBisherigeSchule.jahrgangVon ?? '') ?? null"
							@update:model-value="(v : Jahrgaenge) => newEntryBisherigeSchule.jahrgangVon = v?.daten(schuljahrNewEntryBisherigeSchuleDatumVon)?.schluessel ?? null"
							:disabled="!selectedSchuleForNewEntryBisherigeSchule || !newEntryBisherigeSchule.datumVon" />
						<ui-select label="Jahrgang Bis" :manager="jahrgangBisManager"
							:model-value="Jahrgaenge.data().getWertBySchluessel(newEntryBisherigeSchule.jahrgangBis ?? '') ?? null"
							@update:model-value="(v : Jahrgaenge) => newEntryBisherigeSchule.jahrgangBis = v?.daten(schuljahrNewEntryBisherigeSchuleDatumBis)?.schluessel ?? null"
							:disabled="!selectedSchuleForNewEntryBisherigeSchule || !newEntryBisherigeSchule.datumBis" />
						<svws-ui-select class="col-span-full" title="Schulgliederung" :items="schulgliederungenBisherigeSchule" :readonly
							:disabled="(!newEntryBisherigeSchule.datumBis || !schulformEintragSelectedSchuleNewEntryBisherigeSchule)" autocomplete
							:item-filter="coreTypeDataFilter" v-model="schulgliederungBisherigeSchule"
							:item-text="v => (v as SchulgliederungKatalogEintrag).text" />
					</svws-ui-input-wrapper>
					<div class="mt-7 flex flex-row gap-4 justify end">
						<svws-ui-button type="secondary" @click="closeModalBisherigeSchule">Abbrechen</svws-ui-button>
						<svws-ui-button @click="sendRequestBisherigeSchule(currentMode)" :disabled="(!newEntryBisherigeSchule.idSchule) || !hatKompetenzUpdate">
							Speichern
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { BenutzerKompetenz, Einschulungsart, PrimarstufeSchuleingangsphaseBesuchsjahre, SchuelerSchulbesuchSchule, Schulform, Schulgliederung,
		Uebergangsempfehlung, SchulEintrag, AdressenUtils, ArrayList, SchuelerSchulbesuchMerkmal, Kindergartenbesuch, Jahrgaenge } from "@core";
	import type { Herkunftsarten, SchulformKatalogEintrag, SchulgliederungKatalogEintrag, Merkmal } from "@core";
	import type { SchuelerSchulbesuchProps } from './SSchuelerSchulbesuchProps';
	import { CoreTypeSelectManager } from "@ui";
	import type { DataTableColumn } from "@ui";
	import { coreTypeDataFilter, filterSchulenEintraege, formatDate } from "~/utils/helfer";
	import { ref, computed, watch } from "vue";

	const props = defineProps<SchuelerSchulbesuchProps>();
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);
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
		{ key: "merkmal", label: "Merkmal", statistic: true},
		{ key: "datumVon", label: "Von"},
		{ key: "datumBis", label: "Bis"},
	]

	// --- Merkmale Modal ---
	const merkmaleFilteredNotCreated = computed(() => {
		const result = [];
		for (const m of props.manager().merkmaleById.values()) {
			const alreadyExists = merkmale.value.some(v => v.idMerkmal === m.id);
			if (!alreadyExists)
				result.push(m);
		}
		return result;
	});
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
		if (auswahlMerkmale.value.length === 0)
			return;
		const ids = new ArrayList<number>();
		for (const s of auswahlMerkmale.value)
			ids.add(s.id);
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
		currentMode.value = newMode;
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

	const schulformEintragSelectedSchuleNewEntryBisherigeSchule = computed<SchulformKatalogEintrag | null>(() => {
		if (selectedSchuleForNewEntryBisherigeSchule.value.idSchulform === null)
			return null;
		return Schulform.data().getEintragByID(selectedSchuleForNewEntryBisherigeSchule.value.idSchulform);
	});

	const schulformSelectedSchuleNEwEntryBisherigeSchule = computed<Schulform | null>(() => {
		if (selectedSchuleForNewEntryBisherigeSchule.value.idSchulform === null)
			return null;
		return Schulform.data().getWertByID(selectedSchuleForNewEntryBisherigeSchule.value.idSchulform);
	})

	const schulgliederungenBisherigeSchule = computed<SchulgliederungKatalogEintrag[]>(() => {
		if ((!schulformEintragSelectedSchuleNewEntryBisherigeSchule.value) || (newEntryBisherigeSchule.value.datumBis === null))
			return [];
		const gliederungenByJahr = Schulgliederung.data().getEintraegeBySchuljahr(schuljahrNewEntryBisherigeSchuleDatumBis.value);
		const result = [];
		for (const g of gliederungenByJahr) {
			if (g.schulformen.contains(schulformEintragSelectedSchuleNewEntryBisherigeSchule.value.kuerzel))
				result.push(g);
		}
		return result;
	});

	const schuljahrNewEntryBisherigeSchuleDatumVon = computed<number>(() => {
		if (newEntryBisherigeSchule.value.datumVon === null)
			return -1;
		return Number(newEntryBisherigeSchule.value.datumVon.toString().substring(0, 4));
	});

	const schuljahrNewEntryBisherigeSchuleDatumBis = computed<number>(() => {
		if (newEntryBisherigeSchule.value.datumBis === null)
			return -1;
		return Number(newEntryBisherigeSchule.value.datumBis.toString().substring(0, 4));
	});

	const jahrgangVonManager = new CoreTypeSelectManager({ clazz: Jahrgaenge.class, schuljahr: schuljahrNewEntryBisherigeSchuleDatumVon.value,
		schulformen: schulformSelectedSchuleNEwEntryBisherigeSchule.value, selectionDisplayText: "text", optionDisplayText: "text" })

	const jahrgangBisManager = new CoreTypeSelectManager({ clazz: Jahrgaenge.class, schuljahr: schuljahrNewEntryBisherigeSchuleDatumBis.value,
		schulformen: schulformSelectedSchuleNEwEntryBisherigeSchule.value, selectionDisplayText: "text", optionDisplayText: "text" })

	watch(schuljahrNewEntryBisherigeSchuleDatumVon, () => {
		jahrgangVonManager.schuljahr = schuljahrNewEntryBisherigeSchuleDatumVon.value;
	})

	watch(schuljahrNewEntryBisherigeSchuleDatumBis, () => {
		jahrgangBisManager.schuljahr = schuljahrNewEntryBisherigeSchuleDatumBis.value;
	})

	watch(schulformSelectedSchuleNEwEntryBisherigeSchule, () => {
		jahrgangVonManager.schulformen = schulformSelectedSchuleNEwEntryBisherigeSchule.value;
		jahrgangBisManager.schulformen = schulformSelectedSchuleNEwEntryBisherigeSchule.value;
	})

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
	// ToDo: Schulform.GY entfernen
	const schuleHatPrimarstufe = computed(() => {
		const erlaubteSchulformen = [ Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V, Schulform.GY ];
		return erlaubteSchulformen.includes(props.schulform);
	});

	function textDauerKindergartenbesuch(k: Kindergartenbesuch) {
		return k.daten(props.manager().schuljahr)?.text ?? '-';
	}

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
		return e.daten(props.manager().schuljahr)?.text + ' - '+ e.daten(props.manager().schuljahr)?.beschreibung;
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
