<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Anmeldedaten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<ui-select label="Status" v-model="selectedStatus" :manager="statusManager" :removable="false" searchable />
				<svws-ui-text-input placeholder="Schuljahr" type="text" :disabled="true" />
				<svws-ui-text-input placeholder="Halbjahr" type="text" :disabled="true" />
				<svws-ui-text-input placeholder="Jahrgang" type="text" :disabled="true" />
				<svws-ui-text-input placeholder="Klasse" type="text" :disabled="true" />
				<svws-ui-spacing />
				<ui-select label="Einschulungsart" v-model="einschulungsart" :manager="einschulungsartManager" :removable="false" v-if="schulenMitPrimaerstufe" />
				<!--TODO Anmeldedatum darf nicht in der Zukunft liegen-->
				<svws-ui-text-input placeholder="Anmeldedatum" type="date" :model-value="data.anmeldedatum"
					@change="anmeldedatum => patch({ anmeldedatum }, data.id)" />
				<!--TODO Aufnahmedatum darf nicht vor dem Anmeldedatum liegen-->
				<svws-ui-text-input placeholder="Aufnahmedatum" type="date" :model-value="data.aufnahmedatum"
					@change="aufnahmedatum => patch({ aufnahmedatum }, data.id)" />
				<!--TODO Beginn Bildungsgang darf nicht vor dem Aufnahmedatum liegen-->
				<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" :model-value="data.beginnBildungsgang"
					@change="beginnBildungsgang => patch({ beginnBildungsgang }, data.id)" />
				<svws-ui-text-input placeholder="Dauer Bildungsgang" type="date" readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card title="Persönliche Daten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<!--TODO Leere Inputfelder unterbinden-->
				<svws-ui-text-input placeholder="Name" required :model-value="data.nachname"
					@change="nachname => patch({ nachname: nachname ?? undefined }, data.id)" :valid="fieldIsValid('vorname')" />
				<svws-ui-text-input placeholder="Vorname" required :model-value="data.vorname"
					@change="vorname => patch({ vorname: vorname ?? undefined }, data.id)" :valid="fieldIsValid('vorname')" />
				<svws-ui-text-input placeholder="Weitere Vornamen" :model-value="data.alleVornamen"
					@change="alleVornamen => patch({ alleVornamen: alleVornamen ?? undefined }, data.id)" :valid="fieldIsValid('alleVornamen')" />
				<ui-select label="Geschlecht" :model-value="geschlecht" @update:model-value="setGeschlecht" :manager="geschlechtManager" :removable="false" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Straße" type="text" :model-value="strasseSchueler" @change="patchStrasse" :valid="fieldIsValid('strassenname')" />
				<ui-select label="Wohnort" v-model="wohnortID" :manager="wohnortManager" searchable />
				<svws-ui-spacing />
				<ui-select label="Ortsteil" v-model="ortsteilSelected" :manager="ortsteilManager" searchable />
				<svws-ui-text-input placeholder="Geburtsdatum" required type="date" :model-value="data.geburtsdatum"
					@change="geburtsdatum => geburtsdatum && patch({ geburtsdatum }, data.id)" :valid="fieldIsValid('geburtsdatum')" />
				<svws-ui-text-input placeholder="Geburtsort" :model-value="data.geburtsort" @change="geburtsort => patch({ geburtsort }, data.id)" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" type="tel" :model-value="data.telefon" @change="telefon => patch({ telefon }, data.id)"
					:valid="fieldIsValid('telefon')" :max-len="20" />
				<svws-ui-text-input placeholder="Mobil/Fax" type="tel" :model-value="data.telefonMobil"
					@change="telefonMobil => patch({ telefonMobil }, data.id)" :valid="fieldIsValid('telefonMobil')" :max-len="20" />
				<svws-ui-text-input placeholder="E-Mail" type="email" :model-value="data.emailPrivat"
					@change="emailPrivat => patch({ emailPrivat }, data.id)" :valid="fieldIsValid('emailPrivat')" />
				<svws-ui-spacing />
				<ui-select label="1. Staatsangehörigkeit" v-model="staatsangehoerigkeit" :manager="staatsangehoerigkeitenManager" searchable />
				<ui-select label="2. Staatsangehörigkeit" v-model="staatsangehoerigkeit2" :manager="staatsangehoerigkeitenManager" searchable />
				<ui-select label="Konfession" v-model="religion" :manager="religionManager" :removable="false" searchable />
				<svws-ui-spacing />
				<svws-ui-checkbox v-model="hatMigrationshintergrund" type="checkbox" title="Migrationshintergrund">
					Migrationshintergrund vorhanden
				</svws-ui-checkbox>
				<svws-ui-input-number placeholder="Zuzugsjahr" :model-value="data.zuzugsjahr" @change="zuzugsjahr => patch({ zuzugsjahr }, data.id)"
					:readonly="!auswahlMigrationsHintergrund" />
				<ui-select label="Geburtsland" v-model="geburtsland" :manager="geburtslandManager" :readonly="!auswahlMigrationsHintergrund" :removable="false" searchable />
				<svws-ui-spacing />
				<ui-select label="Geburtsland Mutter" v-model="geburtslandMutter" :manager="geburtslandManager" :readonly="!auswahlMigrationsHintergrund" :removable="false" searchable />
				<ui-select label="Geburtsland Vater" v-model="geburtslandVater" :manager="geburtslandManager" :readonly="!auswahlMigrationsHintergrund" :removable="false" searchable />
				<ui-select label="Verkehrssprache" v-model="verkehrssprache" :manager="verkehrsspracheManager" :readonly="!auswahlMigrationsHintergrund" :removable="false" searchable />
				<svws-ui-spacing />
				<ui-select label="Fahrschüler" v-model="fahrschuelerart" :manager="fahrschuelerartManager" :removable="false" searchable />
				<ui-select label="Haltestelle" v-model="haltestelle" :manager="haltestellenManager" :removable="false" searchable />
				<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht" :model-value="data.religionabmeldung"
					@change="religionabmeldung => patch({ religionabmeldung }, data.id)" type="date" />
				<svws-ui-spacing />
				<ui-select label="Ext. ID-Nr." v-model="externeSchulNr" :manager="externeIDNrManager" :removable="false" searchable />
				<!--TODO Ausweisnummer, Schwerbehindertenausweis, Bemerkumng zu SchuelerStammdaten hinzufügen-->
				<svws-ui-text-input placeholder="NR. Schülerausweis" :disabled="true" />
				<svws-ui-text-input placeholder="Schwerbehindertenausweis" type="text" :disabled="true" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Bemerkung" type="text" :disabled="true" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card title="Erziehungsberechtigte" class="col-span-full">
			<svws-ui-table class="contentFocusField" :items="sortedData" :columns="columnsErzieher" :no-data="getListSchuelerErziehereintraege().size() === 0" clickable :clicked="erzieher"
				@update:clicked="value => erzieher = value" v-model="selectedErz" :selectable="true" focus-first-element>
				<template #header(erhaeltAnschreiben)>
					<svws-ui-tooltip>
						<span class="icon i-ri-mail-send-line" />
						<template #content>
							Erhält Anschreiben
						</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(idErzieherArt)="{ value }">
					{{ getBezeichnungErzieherart(value) }}
				</template>
				<template #cell(name)="{ rowData }">
					{{ rowData.vorname }} {{ rowData.nachname }}
				</template>
				<template #cell(email)="{ value: eMail }">
					{{ eMail ? eMail : '—' }}
				</template>
				<template #cell(adresse)="{ rowData }">
					{{ strasseErzieher(rowData) }}{{ rowData.wohnortID && mapOrte?.get(rowData.wohnortID) ? `, ${mapOrte.get(rowData.wohnortID)?.plz} ${mapOrte?.get(rowData.wohnortID)?.ortsname}` : '' }}
				</template>
				<template #cell(erhaeltAnschreiben)="{ value: erhaeltAnschreiben }">
					{{ erhaeltAnschreiben ? '&check;' : '&times;' }}
				</template>
				<template #cell(actions)="{ rowData }">
					<!-- Button zum Hinzufügen eines Erziehers an der zweiten Position, wird nur angezeigt wenn noch keine zweite Position in einem Eintrag existiert -->
					<svws-ui-button v-if="isSuffix1(rowData.id) && !hasSuffix2(rowData.id)"
						@click.stop="openModalForPos2(rowData)">
						+
					</svws-ui-button>
				</template>
				<template #actions>
					<svws-ui-button @click="deleteErzieherRequest" type="trash" :disabled="(selectedErz.length === 0) || (!hatKompetenzUpdate)" />
					<svws-ui-button @click="addErzieher" type="icon" title="Erziehungsberechtigten hinzufügen" :disabled="!hatKompetenzUpdate">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</template>
			</svws-ui-table>
			<svws-ui-content-card v-if="erzieher !== undefined" :title="(erzieher.vorname !== null) || (erzieher.nachname !== null) ?
				`Daten zu ${erzieher.vorname ? erzieher.vorname + ' ' : '' }${erzieher.nachname}` : 'Daten zur Person'" class="col-span-full mt-16 lg:mt-20">
				<template #actions>
					<svws-ui-checkbox class="mr-2" :model-value="erzieher.erhaeltAnschreiben === true" @update:model-value="erhaeltAnschreiben => (erzieher !== undefined) &&
						patchSchuelerErziehereintrag({ erhaeltAnschreiben }, erzieher.id)">
						Erhält Anschreiben
					</svws-ui-checkbox>
				</template>
				<!-- Felder zum Patchen der Erzieherdaten -->
				<svws-ui-input-wrapper :grid="4">
					<ui-select label="Erzieherart" v-model="erzieherart" :manager="erzieherartenManager" :removable="false" searchable />
					<svws-ui-text-input placeholder="Anrede" :model-value="erzieher?.anrede" @change="anrede=>(erzieher !== undefined) &&
						patchSchuelerErziehereintrag({ anrede }, erzieher.id)" type="text" />
					<svws-ui-text-input placeholder="Titel" :model-value="erzieher?.titel" @change="titel=>(erzieher !== undefined) &&
						patchSchuelerErziehereintrag({ titel }, erzieher.id)" type="text" />
					<svws-ui-spacing />
					<svws-ui-text-input placeholder="Name" :model-value="erzieher?.nachname" @change="nachname=>(erzieher !== undefined) &&
						patchSchuelerErziehereintrag({ nachname }, erzieher.id)" type="text" />
					<svws-ui-text-input placeholder="Vorname" :model-value="erzieher?.vorname" @change="vorname=>(erzieher !== undefined) &&
						patchSchuelerErziehereintrag({ vorname }, erzieher.id)" type="text" />
					<svws-ui-text-input placeholder="E-Mail Adresse" :model-value="erzieher?.eMail" @change="eMail=>(erzieher !== undefined) &&
						patchSchuelerErziehereintrag({ eMail }, erzieher.id)" type="email" verify-email />
					<svws-ui-spacing />
					<ui-select label="Staatsangehörigkeit" v-model="ersterErzStaatsangehoerigkeit" :manager="staatsangehoerigkeitenManager" searchable />
					<svws-ui-text-input placeholder="Straße und Hausnummer" :model-value="strasseErzieher(erzieher)" @change="patchStrasseErzieher" type="text" />
					<ui-select label="Wohnort" v-model="erzWohnort" :manager="wohnortManager" searchable />
					<ui-select label="Ortsteil" v-model="erzOrtsteil" :manager="erzOrtsteilManager" :readonly="!erzieher.wohnortID" searchable />
					<svws-ui-spacing />
					<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="erzieher?.bemerkungen" span="full" autoresize
						@change="bemerkungen => (erzieher !== undefined) && patchSchuelerErziehereintrag({ bemerkungen: bemerkungen === null ? '' : bemerkungen }, erzieher.id)"
						:readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<!-- Modal zum Hinzufügen eines zweiten Erziehungsberechtigten (Position 2) über den "+"-Button -->
			<svws-ui-modal :show="showPatchPosModalErz" @update:show="closeModalErzieher">
				<template #modalTitle>Einen zweiten Erziehungsberechtigten hinzufügen</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2" class="text-left">
						<svws-ui-text-input placeholder="Anrede" v-model="zweiterErz.anrede" type="text" />
						<svws-ui-text-input placeholder="Titel" v-model="zweiterErz.titel" type="text" />
						<svws-ui-text-input placeholder="Vorname" v-model="zweiterErz.vorname" type="text" required />
						<svws-ui-text-input placeholder="Nachname" v-model="zweiterErz.nachname" type="text" required />
						<svws-ui-text-input placeholder="E-Mail Adresse" v-model="zweiterErz.eMail" type="email" verify-email />
						<ui-select label="Staatsangehörigkeit" v-model="zweiteErzStaatsangehoerigkeit" :manager="staatsangehoerigkeitenManager" :removable="false" searchable />
					</svws-ui-input-wrapper>
					<div class="mt-7 flex flex-row gap-4 justify-end">
						<svws-ui-button type="secondary" @click="showPatchPosModalErz = false">Abbrechen</svws-ui-button>
						<svws-ui-button @click="saveSecondErzieher" :disabled="(!stringIsValid(zweiterErz.vorname, true, 120))
							|| (!stringIsValid(zweiterErz.nachname, true, 120)) || (!hatKompetenzUpdate)">
							Zweiten Erzieher speichern
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
			<SSchuelerErziehungsberechtigteModal v-model:erster-erz="ersterErz"
				v-model:zweiter-erz="zweiterErz"
				:show-modal="showModalErzieher"
				:map-erzieherarten="mapErzieherarten"
				:hat-kompetenz-update="hatKompetenzUpdate"
				:ist-erster-erz-gespeichert="istErsterErzGespeichert"
				:map-orte="mapOrte"
				:map-ortsteile="mapOrtsteile"
				:schuljahr="schuljahr"
				@close-modal="closeModalErzieher"
				@send-request="sendRequestErzieher"
				@save-and-show-second="saveAndShowSecondForm"
				@save-second-erzieher="saveSecondErzieher" />
		</svws-ui-content-card>

		<svws-ui-content-card title="Weitere Telefonnummern" class="col-span-full">
			<svws-ui-table clickable
				@update:clicked="v => patchTelefonnummer(v)" :items="getListSchuelerTelefoneintraege()" :no-data="getListSchuelerTelefoneintraege().size() === 0"
				:columns="columnsTelefonnummer" :selectable="true" v-model="selected">
				<template #cell(idTelefonArt)="{ value }">
					{{ getBezeichnungTelefonart(value) }}
				</template>
				<template #cell(telefonnummer)="{ value }">
					{{ value }}
				</template>
				<template #cell(bemerkung)="{ value }">
					{{ value }}
				</template>
				<template #cell(istGesperrt)="{ value }">
					{{ value ? 'Gesperrt' : 'Nicht gesperrt' }}
				</template>
				<template #actions>
					<div class="inline-flex gap-4">
						<svws-ui-button @click="deleteTelefonnummern" type="trash" :disabled="(selected.length === 0) || (!hatKompetenzUpdate)" />
						<svws-ui-button @click="addTelefonnummer" type="icon" title="Telefonnummer hinzufügen" :disabled="!hatKompetenzUpdate">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-table>

			<svws-ui-modal :show="showModalTelefonnummer" @update:show="closeModalTelefonnummer">
				<template #modalTitle>Telefonnummer hinzufügen</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2" class="text-left">
						<ui-select label="Telefonart" v-model="telefonArt" :manager="telefonArtManager" :removable="false" searchable />
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
					<svws-ui-notification type="warning" v-if="mapTelefonArten.size === 0">
						Die Liste der Telefonarten ist leer, es sollte mindestens eine Telefonart unter Schule/Kataloge angelegt werden, damit zusätzliche Telefonnummern eine gültige Zuordnung haben.
					</svws-ui-notification>
					<div class="mt-7 flex flex-row gap-4 justify-end">
						<svws-ui-button type="secondary" @click="closeModalTelefonnummer">Abbrechen</svws-ui-button>
						<svws-ui-button @click="sendRequestTelefonnummer"
							:disabled="(telefonArt === null) || (mapTelefonArten.size === 0) || (newEntryTelefonnummer.telefonnummer === null) ||
								(newEntryTelefonnummer.telefonnummer.length === 0) || (!hatKompetenzUpdate)">
							Speichern
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
		</svws-ui-content-card>

		<svws-ui-content-card title="Vorschulentwicklung" class="col-span-full" v-if="schulenMitPrimaerstufe">
			<svws-ui-input-wrapper :grid="2">
				<ui-select label="Name des Kindergartens" v-model="kindergarten" :manager="nameKindergartenManager" />
				<ui-select label="Dauer des Kindergartenbesuchs" v-model="dauerKindergarten" :manager="dauerKindergartenManager" />
				<svws-ui-spacing />
				<svws-ui-checkbox title="Verpflichtung f. Sprachförderkurss" :model-value="dataSchulbesuchsdaten.verpflichtungSprachfoerderkurs"
					@update:model-value="verpflichtungSprachfoerderkurs => patchSchuelerSchulbesuchsdaten({ verpflichtungSprachfoerderkurs }, data.id)">
					Verpflichtung f. Sprachförderkurs
				</svws-ui-checkbox>
				<svws-ui-checkbox title="Teilnahme an Sprachförderkurs" :model-value="dataSchulbesuchsdaten.teilnahmeSprachfoerderkurs"
					@update:model-value="teilnahmeSprachfoerderkurs => patchSchuelerSchulbesuchsdaten({ teilnahmeSprachfoerderkurs }, data.id)">
					Teilnahme an Sprachförderkurs
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Neuaufnahme beenden</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerStammdaten, TelefonArt, OrtsteilKatalogEintrag, EinschulungsartKatalogEintrag, NationalitaetenKatalogEintrag, SchuelerStatusKatalogEintrag, VerkehrsspracheKatalogEintrag} from "@core";
	import { BenutzerKompetenz, SchuelerTelefon, ArrayList, ErzieherStammdaten, AdressenUtils, Geschlecht, JavaString, Kindergartenbesuch, Nationalitaeten, SchuelerStatus, Schulform, Verkehrssprache } from "@core";
	import { computed, ref, watch } from "vue";
	import type { SchuelerNeuSchnelleingabeProps } from "~/components/schueler/SSchuelerNeuSchnelleingabeProps";
	import { erzieherArtSort,orte_sort, ortsteilSort } from "~/utils/helfer";
	import type { DataTableColumn } from "@ui";
	import { CoreTypeSelectManager, SelectManager } from "@ui";

	const props = defineProps<SchuelerNeuSchnelleingabeProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);

	const data = computed(() => props.schuelerListeManager().daten());

	const dataSchulbesuchsdaten = computed(() => props.schuelerSchulbesuchsManager().daten);

	//TODO Schulform.GY aus dem Array entfernen
	const schulenMitPrimaerstufe = computed(() => {
		const erlaubteSchulformen = [ Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V, Schulform.GY];
		return erlaubteSchulformen.includes(props.schulform);
	});

	const kindergaerten = computed(() => props.mapKindergaerten.values());

	const nameKindergartenManager = new SelectManager({ options: kindergaerten.value, optionDisplayText: i => i.bezeichnung, selectionDisplayText: i => i.bezeichnung })

	const auswahlKindergartenID = ref(dataSchulbesuchsdaten.value.idKindergarten);

	const kindergarten = computed({
		get: () => {
			const id = auswahlKindergartenID.value ?? -1;
			return props.mapKindergaerten.get(id);
		},
		set: (value) => {
			auswahlKindergartenID.value = value?.id ?? -1;
			void props.patchSchuelerSchulbesuchsdaten({ idKindergarten: value?.id ?? null }, data.value.id);
		},
	})

	const orte = computed(() => props.mapOrte.values());

	const wohnortManager = new SelectManager({ options: orte.value, optionDisplayText: i => `${i.plz} ${i.ortsname}`, sort: orte_sort, selectionDisplayText: i => `${i.plz} ${i.ortsname}` })

	const auswahlWohnortID = ref(data.value.wohnortID ?? null);

	const wohnortID = computed({
		get: () => {
			const id = auswahlWohnortID.value ?? -1;
			return props.mapOrte.get(id) ?? null;
		},
		set: (value) => {

			auswahlWohnortID.value = value?.id ?? -1;
			void props.patch({ wohnortID: value?.id ?? null}, data.value.id);
		},
	});

	const ortsteile = computed(() => Array.from(props.mapOrtsteile.values()));

	const ortsteileFiltered = computed(() => {
		const wohnortID = auswahlWohnortID.value;
		if (wohnortID === null)
			return ortsteile.value;
		return ortsteile.value.filter(o => o.ort_id === wohnortID);
	});

	const items = computed(() => ortsteileFiltered.value);

	const auswahlOrtsteilID = ref<number | null>(data.value.ortsteilID ?? null);

	const ortsteilManager = new SelectManager({ options: items, sort: ortsteilSort, optionDisplayText: i => i.ortsteil ?? '',
		selectionDisplayText: i => i.ortsteil ?? '' });

	const ortsteilSelected = computed<OrtsteilKatalogEintrag | null>({
		get: () => {
			const id = auswahlOrtsteilID.value ?? -1;
			return props.mapOrtsteile.get(id) ?? null;
		},
		set: (value: OrtsteilKatalogEintrag | null | undefined) => {
			if (value === null || value === undefined) {
				auswahlOrtsteilID.value = null;
				void props.patch({ ortsteilID: null }, data.value.id);
			}

			auswahlOrtsteilID.value = value?.id ?? -1;
			void props.patch({ ortsteilID: value?.id ?? null }, data.value.id);
		},
	});

	const geschlecht = ref(Geschlecht.fromValue(data.value.geschlecht));

	const geschlechtManager = new SelectManager({ options: Geschlecht.values(), optionDisplayText: i => i.text, selectionDisplayText: i => i.text })

	async function setGeschlecht(value: Geschlecht | null | undefined): Promise<void> {
		geschlecht.value = value ?? null;
		ortsteilManager.setConfig()
		data.value.geschlecht = value?.id ?? -1;
		await props.patch({ geschlecht: value?.id }, data.value.id);
	}

	const einschulungsarten = computed(() => props.mapEinschulungsarten);

	const einschulungsartManager = new SelectManager({ options: einschulungsarten.value.values(), optionDisplayText: i => i.text, selectionDisplayText: i => i.text })

	const auswahlEinschulungsart = ref(dataSchulbesuchsdaten.value.grundschuleEinschulungsartID);

	const einschulungsart = computed({
		get: () => {
			const id = auswahlEinschulungsart.value ?? -1;
			return props.mapEinschulungsarten.get(id) ?? null;
		},
		set: (value: EinschulungsartKatalogEintrag) => {
			auswahlEinschulungsart.value = value.id;
			void props.patchSchuelerSchulbesuchsdaten({ grundschuleEinschulungsartID: value.id }, data.value.id);
		},
	});

	const strasseSchueler = computed(() => AdressenUtils.combineStrasse(data.value.strassenname ?? "", data.value.hausnummer ?? "", data.value.hausnummerZusatz ?? ""));

	function patchStrasse(value: string | null) {
		if (value !== null) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname: vals[0], hausnummer: vals[1], hausnummerZusatz: vals[2] }, data.value.id);
		}
	}

	const auswahlStatusID = ref<number | null>(data.value.status);

	const statusManager = new CoreTypeSelectManager({ clazz: SchuelerStatus.class, schuljahr: schuljahr, schulformen: props.schulform, optionDisplayText: "text", selectionDisplayText: "text" });

	const selectedStatus = computed<SchuelerStatusKatalogEintrag | null>({
		get: (): SchuelerStatusKatalogEintrag | null => {
			return SchuelerStatus.data().getWertByKuerzel('' + auswahlStatusID.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			auswahlStatusID.value = value?.id ?? null;
			void props.patch({ status: value?.id }, data.value.id);
		},
	});

	const auswahlMigrationsHintergrund = ref<boolean>(data.value.hatMigrationshintergrund);

	const hatMigrationshintergrund = computed<boolean>({
		get: () => data.value.hatMigrationshintergrund,
		set: (val) => {
			auswahlMigrationsHintergrund.value = val;
			return void props.patch({ hatMigrationshintergrund: val }, data.value.id);
		},
	});

	const staatsangehoerigkeitenManager = new CoreTypeSelectManager({ clazz: Nationalitaeten.class, schuljahr: schuljahr, optionDisplayText: "text", selectionDisplayText: "text" });

	const staatsangehoerigkeitID = ref(data.value.staatsangehoerigkeitID ?? null);

	const staatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			return Nationalitaeten.getByISO3(staatsangehoerigkeitID.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			staatsangehoerigkeitID.value = value?.iso3 ?? null;
			void props.patch({ staatsangehoerigkeitID: value?.iso3 ?? null}, data.value.id)
		},
	});

	const staatsangehoerigkeit2ID = ref(data.value.staatsangehoerigkeit2ID ?? null);

	const staatsangehoerigkeit2 = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			return Nationalitaeten.getByISO3(staatsangehoerigkeit2ID.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			staatsangehoerigkeit2ID.value = value?.iso3 ?? null;
			void props.patch({ staatsangehoerigkeit2ID: value?.iso3 ?? null}, data.value.id)
		},
	});

	const religionen = computed(() => props.mapReligionen.values());

	const religionManager = new SelectManager({ options: religionen.value, optionDisplayText: i => i.bezeichnungZeugnis ?? '', selectionDisplayText: i => i.bezeichnungZeugnis ?? '' })

	const auswahlReligionID = ref(data.value.religionID);

	const religion = computed({
		get: () => {
			const id = auswahlReligionID.value ?? -1;
			return props.mapReligionen.get(id);
		},
		set: (value) => {

			auswahlReligionID.value = value?.id ?? -1;
			void props.patch({ religionID: value?.id ?? null }, data.value.id);
		},
	})

	const geburtslandManager = new CoreTypeSelectManager({ clazz: Nationalitaeten.class, schuljahr: schuljahr, optionDisplayText: "text", selectionDisplayText: "text" })

	const auswahlGeburtsland = ref(data.value.geburtsland ?? null);

	const geburtsland = computed<NationalitaetenKatalogEintrag | null>({
		get: () :NationalitaetenKatalogEintrag | null => {
			return Nationalitaeten.getByISO3(auswahlGeburtsland.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			auswahlGeburtsland.value = value?.iso3 ?? null;
			void props.patch({ geburtsland: value?.iso3 }, data.value.id)
		},
	});

	const auswahlGeburtslandMutter = ref(data.value.geburtslandMutter ?? null);

	const geburtslandMutter = computed<NationalitaetenKatalogEintrag | null>({
		get: () :NationalitaetenKatalogEintrag | null => {
			return Nationalitaeten.getByISO3(auswahlGeburtslandMutter.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			auswahlGeburtslandMutter.value = value?.iso3 ?? null;
			void props.patch({ geburtslandMutter: value?.iso3 }, data.value.id)
		},
	});

	const auswahlGeburtslandVater = ref(data.value.geburtslandVater ?? null);

	const geburtslandVater = computed<NationalitaetenKatalogEintrag | null>({
		get: () :NationalitaetenKatalogEintrag | null => {
			return Nationalitaeten.getByISO3(auswahlGeburtslandVater.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			auswahlGeburtslandVater.value = value?.iso3 ?? null;
			void props.patch({ geburtslandVater: value?.iso3 }, data.value.id)
		},
	});

	const verkehrsspracheManager = new CoreTypeSelectManager({ clazz: Verkehrssprache.class, schuljahr: schuljahr, optionDisplayText: "text", selectionDisplayText: "text" });

	const auswahlVerkehrssprache = ref(data.value.verkehrspracheFamilie ?? null);

	const verkehrssprache = computed<VerkehrsspracheKatalogEintrag | null>({
		get: () :VerkehrsspracheKatalogEintrag | null => {
			return Verkehrssprache.getByIsoKuerzel(auswahlVerkehrssprache.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			auswahlVerkehrssprache.value = value?.iso3 ?? null;
			void props.patch({ verkehrspracheFamilie: value?.iso3 }, data.value.id)
		},
	});

	const externeIDNummern = computed(() => props.mapSchulen.values());

	const externeIDNrManager = new SelectManager({ options: externeIDNummern.value, optionDisplayText: i => i.kuerzel ?? i.schulnummerStatistik ?? i.kurzbezeichnung ?? i.name,
		selectionDisplayText: i => i.kuerzel ?? i.schulnummerStatistik ?? i.kurzbezeichnung ?? i.name });

	const auswahlExterneIDNr = ref(data.value.externeSchulNr ?? null);

	const externeSchulNr = computed({
		get: () => props.mapSchulen.get(auswahlExterneIDNr.value ?? "") ?? null,
		set: (value) => {
			auswahlExterneIDNr.value = value?.schulnummerStatistik ?? null;
			void props.patch({ externeSchulNr: value?.schulnummerStatistik }, data.value.id);
		},
	});

	const fahrschuelerarten = computed(() => props.mapFahrschuelerarten.values());

	const fahrschuelerartManager = new SelectManager({ options: fahrschuelerarten.value, optionDisplayText: i => i.bezeichnung ?? '', selectionDisplayText: i => i.bezeichnung ?? '' });

	const auswahlfahrschuelerartID = ref(data.value.fahrschuelerArtID ?? null);

	const fahrschuelerart = computed({
		get: () => props.mapFahrschuelerarten.get(auswahlfahrschuelerartID.value ?? -1) ?? null,
		set: (value) => {
			const id = value?.id ?? null;
			auswahlfahrschuelerartID.value = id;
			void props.patch({ fahrschuelerArtID: id }, data.value.id);
		},
	});

	const haltestellen = computed(() => props.mapHaltestellen.values());

	const haltestellenManager = new SelectManager({ options: haltestellen.value, optionDisplayText: i => i.bezeichnung ?? '', selectionDisplayText: i => i.bezeichnung ?? '' });

	const auswahlHaltestellenID = ref(data.value.haltestelleID ?? null);

	const haltestelle = computed({
		get: () => props.mapHaltestellen.get(auswahlHaltestellenID.value ?? -1) ?? null,
		set: (value) => {
			const id = value?.id ?? null;
			auswahlHaltestellenID.value = id;
			void props.patch({ haltestelleID: id }, data.value.id);
		},
	});

	const dauerKindergartenManager = new CoreTypeSelectManager({clazz: Kindergartenbesuch.class, schuljahr: schuljahr, optionDisplayText: "text", selectionDisplayText: "text" })

	const dauerKindergartenbesuchID = ref(dataSchulbesuchsdaten.value.idDauerKindergartenbesuch ?? null);

	const dauerKindergarten = computed({
		get: () => {
			if (dauerKindergartenbesuchID.value === null)
				return null;
			return Kindergartenbesuch.data().getEintragByID(dauerKindergartenbesuchID.value) ?? null;
		},
		set: (value) => {
			dauerKindergartenbesuchID.value = value?.id ?? null;
			void props.patchSchuelerSchulbesuchsdaten({ idDauerKindergartenbesuch: value?.id ?? null }, data.value.id);
		},
	});

	//validation logic
	function fieldIsValid(field: keyof SchuelerStammdaten | null):(v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'nachname':
					return stringIsValid(data.value.nachname, true, 120);
				case 'vorname':
					return stringIsValid(data.value.vorname, true, 120);
				case 'alleVornamen':
					return stringIsValid(data.value.alleVornamen, false, 120);
				case 'geburtsdatum':
					return data.value.geburtsdatum !== null;
				case 'geschlecht':
					return Geschlecht.fromValue(data.value.geschlecht) !== null;
				case 'strassenname':
					return adresseIsValid();
				case 'telefon':
					return phoneNumberIsValid(data.value.telefon, false, 20);
				case 'telefonMobil':
					return phoneNumberIsValid(data.value.telefon, false, 20);
				case 'emailPrivat':
					return stringIsValid(data.value.emailPrivat, false, 20);
				case 'geburtsland':
					return (data.value.geburtsland === null) || (Nationalitaeten.getByISO3(data.value.geburtsland) !== null);
				case 'geburtslandMutter':
					return (data.value.geburtslandMutter === null) || (Nationalitaeten.getByISO3(data.value.geburtslandMutter) !== null);
				case 'geburtslandVater':
					return (data.value.geburtslandVater === null) || (Nationalitaeten.getByISO3(data.value.geburtslandVater) !== null);
				default:
					return true;
			}
		}
	}

	function adresseIsValid() {
		return stringIsValid(data.value.strassenname, false, 55) &&
			stringIsValid(data.value.hausnummer, false, 10) &&
			stringIsValid(data.value.hausnummerZusatz, false, 30);
	}

	function phoneNumberIsValid(input: string | null, mandatory: boolean, maxLength: number) {
		if ((input === null) || (JavaString.isBlank(input)))
			return !mandatory;
		// folgende Formate sind erlaubt: 0151123456, 0151/123456, 0151-123456, +49/176-456456 -> Buchstaben sind nicht erlaubt
		return /^\+?\d+([-/]?\d+)*$/.test(input) && input.length <= maxLength;
	}

	function stringIsValid(input: string | null, mandatory: boolean, maxLength: number) {
		if (mandatory)
			return (input !== null) && (!JavaString.isBlank(input)) && (input.length <= maxLength);
		return (input === null) || (input.length <= maxLength);
	}

	// Anlegen von Erziehungsberechtigten
	const erzieher = ref<ErzieherStammdaten | undefined>();

	const selectedErz = ref<ErzieherStammdaten[]>([]);

	const ersterErz = ref<ErzieherStammdaten>(new ErzieherStammdaten())
	const zweiterErz = ref<ErzieherStammdaten>(new ErzieherStammdaten());

	const showModalErzieher = ref<boolean>(false);
	const showPatchPosModalErz = ref<boolean>(false);
	const istErsterErzGespeichert = ref(false);

	enum Mode { ADD, PATCH, PATCH_POS2, DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);

	const sortedData = computed(() => {
		const list = Array.from(props.getListSchuelerErziehereintraege());
		return list.sort((a, b) => {
			const ersteErzId = Math.floor(a.id / 10);
			const zweiteErzId = Math.floor(b.id / 10);
			if (ersteErzId !== zweiteErzId)
				return ersteErzId - zweiteErzId;
			return a.id - b.id;
		});
	});

	const columnsErzieher: DataTableColumn[] = [
		{ key: "idErzieherArt", label: "Art" },
		{ key: "name", label: "Name"},
		{ key: "eMail", label: "E-Mail"},
		{ key: "adresse", label: "Adresse"},
		{ key: "erhaeltAnschreiben", label: "Anschreiben", tooltip: "Erhält Anschreiben", fixedWidth: 3, align: "center" },
		{ key: "actions", label: "2. Person", tooltip: "Weiteres Elternteil hinzufügen", fixedWidth: 10, align: "center" },
	];

	const erzieherarten = computed(() => props.mapErzieherarten.values());

	const erzieherartenManager = new SelectManager({ options: erzieherarten.value, sort: erzieherArtSort, optionDisplayText: i => i.bezeichnung, selectionDisplayText: i => i.bezeichnung });

	const erzieherart = computed({
		get: () => props.mapErzieherarten.get(erzieher.value?.idErzieherArt ?? -1) ?? null,
		set: (value) => {
			const id = value?.id ?? undefined;
			if (erzieher.value === undefined)
				return;
			erzieher.value.idErzieherArt = id ?? null;
			void props.patchSchuelerErziehereintrag({ idErzieherArt: id ?? null }, erzieher.value.id);
		},
	});

	const erzWohnort = computed({
		get: () => props.mapOrte.get(erzieher.value?.wohnortID ?? -1) ?? null,
		set: (value) => {
			if (erzieher.value === undefined)
				return;
			erzieher.value.wohnortID = value?.id ?? -1;
			void props.patchSchuelerErziehereintrag({ wohnortID: value?.id }, erzieher.value.id);
		},
	});

	const ersterErzStaatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			const iso3 = erzieher.value?.staatsangehoerigkeitID ?? null;
			return Nationalitaeten.getByISO3(iso3)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			if (erzieher.value === undefined)
				return;
			const iso3 = value?.iso3 ?? null;
			erzieher.value.staatsangehoerigkeitID = iso3;
			void props.patchSchuelerErziehereintrag({ staatsangehoerigkeitID: iso3 }, erzieher.value.id);
		},
	});

	const zweiteErzStaatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			const iso3 = zweiterErz.value.staatsangehoerigkeitID ?? null;
			return Nationalitaeten.getByISO3(iso3)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			if (erzieher.value === undefined)
				return;
			zweiterErz.value.staatsangehoerigkeitID = value?.iso3 ?? null
		},
	});

	const erzOrtsteileFiltered = computed(() => {
		const wohnortID = erzieher.value?.wohnortID;
		if (wohnortID === null)
			return ortsteile.value;
		return ortsteile.value.filter(o => o.ort_id === wohnortID);
	});

	const erzItems = computed(() => erzOrtsteileFiltered.value);

	const erzOrtsteilManager = new SelectManager({ options: erzItems, sort: ortsteilSort, optionDisplayText: i => i.ortsteil ?? '',
		selectionDisplayText: i => i.ortsteil ?? '' });

	const erzOrtsteil = computed<OrtsteilKatalogEintrag | null>({
		get: () => {
			const id = erzieher.value?.ortsteilID ?? -1;
			return props.mapOrtsteile.get(id) ?? null;
		},
		set: (value: OrtsteilKatalogEintrag | null) => {
			if (erzieher.value === undefined)
				return;
			erzieher.value.ortsteilID = value?.id ?? null;
			void props.patchSchuelerErziehereintrag({ ortsteilID: value?.id ?? null }, erzieher.value.id);
		},
	});

	function getBezeichnungErzieherart(idErzieherart: number): string {
		return props.mapErzieherarten.get(idErzieherart)?.bezeichnung ?? "";
	}

	function strasseErzieher(erzieher: ErzieherStammdaten) {
		return AdressenUtils.combineStrasse(erzieher.strassenname ?? "", erzieher.hausnummer ?? "", erzieher.hausnummerZusatz ?? "");
	}

	function patchStrasseErzieher(value: string | null) {
		if ((value !== null) && (erzieher.value !== undefined)) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patchSchuelerErziehereintrag({ strassenname: vals[0], hausnummer: vals[1], hausnummerZusatz: vals[2] }, erzieher.value.id);
		}
	}

	function setModeErzieher(newMode: Mode) {
		return currentMode.value = newMode;
	}

	function addErzieher() {
		resetErzieher();
		setModeErzieher(Mode.ADD);
		openModalErzieher()
		ersterErz.value.id = 0;
	}

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		resetTelefonnummer();
		closeModalTelefonnummer();
		resetErzieher()
		closeModalErzieher()
	}

	// Patch-Modal um an der zweiten Postion in einem Eintrag einen Erziehungsberechtigten anzulegen
	async function openModalForPos2(item: ErzieherStammdaten) {
		resetErzieher();
		setModeErzieher(Mode.PATCH_POS2);
		openPatchPosModalErz();
		// die ID des Eintrags für den Patch an der zweiten Position
		ersterErz.value.id = item.id;
		zweiterErz.value.idErzieherArt = item.idErzieherArt ?? 0;
		zweiterErz.value.wohnortID = item.wohnortID;
		zweiterErz.value.ortsteilID = item.ortsteilID;
		zweiterErz.value.bemerkungen = item.bemerkungen;
		zweiterErz.value.erhaeltAnschreiben = item.erhaeltAnschreiben;
	}

	function openModalErzieher() {
		showModalErzieher.value = true;
	}

	function openPatchPosModalErz() {
		showPatchPosModalErz.value = true;
	}

	function closeModalErzieher() {
		resetErzieher();
		setModeErzieher(Mode.DEFAULT);
		showModalErzieher.value = false;
		showPatchPosModalErz.value = false;
	}

	function resetErzieher() {
		const defaultErzieherStammdaten = new ErzieherStammdaten();
		const ersteErzieherart = props.mapErzieherarten.values().next().value;
		defaultErzieherStammdaten.idErzieherArt = ersteErzieherart?.id ?? 0;
		ersterErz.value = defaultErzieherStammdaten;
		ersterErz.value.erhaeltAnschreiben = false;

		istErsterErzGespeichert.value = false;
		zweiterErz.value = new ErzieherStammdaten();
	}

	async function sendRequestErzieher() {
		const { id, idSchueler, ...partialDataWithoutId } = ersterErz.value;
		const schuelerId = data.value.id;
		if (currentMode.value === Mode.ADD) {
			await props.addSchuelerErziehereintrag(partialDataWithoutId, schuelerId, 1);
		}
		// Normale Patch für beide Positionen
		if (currentMode.value === Mode.PATCH) {
			await props.patchSchuelerErziehereintrag(partialDataWithoutId, ersterErz.value.id);
		}
		// Zweite Position zum bestehenden Eintrag hinzufügen
		if (currentMode.value === Mode.PATCH_POS2) {
			await props.patchSchuelerErzieherAnPosition(partialDataWithoutId, ersterErz.value.id, schuelerId, 2);
		}
		enterDefaultMode();
	}

	// Speichert den ersten Erziehungsberechtigten (Position 1) und bereitet das Formular für den zweiten Erziehungsberechtigten vor.
	async function saveAndShowSecondForm() {
		const { id, idSchueler, ...partialDataWithoutId } = ersterErz.value;
		const schuelerId = data.value.id;
		const savedEntry = await props.addSchuelerErziehereintrag(partialDataWithoutId, schuelerId, 1);
		ersterErz.value.id = savedEntry.id;
		zweiterErz.value.idErzieherArt = ersterErz.value.idErzieherArt;
		zweiterErz.value.wohnortID = ersterErz.value.wohnortID;
		zweiterErz.value.ortsteilID = ersterErz.value.ortsteilID;
		zweiterErz.value.bemerkungen = ersterErz.value.bemerkungen;
		istErsterErzGespeichert.value = true;
	}

	// Speichert den zweiten Erziehungsberechtigten (Position 2) und beendet anschließend den Bearbeitungsmodus.
	async function saveSecondErzieher() {
		const { id, idSchueler, erhaeltAnschreiben, ...partialDataWithoutId } = zweiterErz.value;
		const schuelerId = data.value.id;
		await props.patchSchuelerErzieherAnPosition(partialDataWithoutId, ersterErz.value.id, schuelerId, 2);
		enterDefaultMode();
	}

	/**
	 * Prüft, ob eine ID auf die erste Position (Suffix 1) endet.
	 * @param id Die künstliche ID mit Suffix.
	 * @returns true, wenn Suffix === 1.
	 */
	function isSuffix1(id: number): boolean {
		return id % 10 === 1;
	}

	/**
	 * Prüft, ob bereits eine zweite Position (Suffix 2) für einen Eintrag existiert.
	 * @param id Die künstliche ID mit Suffix.
	 * @returns true, wenn ein Eintrag mit id+1 existiert.
	 */
	function hasSuffix2(id: number): boolean {
		return Array.from(props.getListSchuelerErziehereintraege()).some(e => e.id === id + 1);
	}

	async function deleteErzieherRequest() {
		if (selectedErz.value.length === 0)
			return;
		const ids = new ArrayList<number>();
		for (const s of selectedErz.value)
			ids.add(s.id);
		await props.deleteSchuelerErziehereintrage(ids);
		selectedErz.value = [];
		erzieher.value = undefined;
	}

	watch(() => props.getListSchuelerErziehereintraege(), (neu) => {
		if (neu.isEmpty())
			erzieher.value = undefined;
		else
			erzieher.value = neu.getFirst();
	}, { immediate: true });

	// Anlegen von Telefonnummern
	const selected = ref<SchuelerTelefon[]>([]);
	const newEntryTelefonnummer = ref<SchuelerTelefon>(new SchuelerTelefon());
	const showModalTelefonnummer = ref<boolean>(false);

	const columnsTelefonnummer: DataTableColumn[] = [
		{ key: "idTelefonArt", label: "Ansprechpartner" },
		{ key: "telefonnummer", label: "Telefonnummern" },
		{ key: "bemerkung", label: "Bemerkung", span: 2 },
		{ key: "istGesperrt", label: "Gesperrt", span: 1, align: "right" },
	]

	const telefonArten = computed(() => props.mapTelefonArten.values());

	const telefonArtManager = new SelectManager({ options: telefonArten.value, optionDisplayText: i => i.bezeichnung, selectionDisplayText: i => i.bezeichnung });

	const telefonArt = computed<TelefonArt | null>({
		get: () => props.mapTelefonArten.get(newEntryTelefonnummer.value.idTelefonArt) ?? null,
		set: (value) => newEntryTelefonnummer.value.idTelefonArt = value !== null ? value.id : -1,
	});

	function addTelefonnummer() {
		resetTelefonnummer();
		setMode(Mode.ADD);
		openModalTelefonnummer();
	}

	async function sendRequestTelefonnummer() {
		const { id, idSchueler, ...partialDataWithoutId } = newEntryTelefonnummer.value;
		const schuelerId = data.value.id;
		if (currentMode.value === Mode.ADD) {
			await props.addSchuelerTelefoneintrag(partialDataWithoutId, schuelerId);
		}
		if (currentMode.value === Mode.PATCH) {
			await props.patchSchuelerTelefoneintrag(partialDataWithoutId, newEntryTelefonnummer.value.id);
		}
		enterDefaultMode();
	}

	function patchTelefonnummer(telefonEintrag: SchuelerTelefon) {
		resetTelefonnummer();
		setMode(Mode.PATCH);
		newEntryTelefonnummer.value.id = telefonEintrag.id;
		newEntryTelefonnummer.value.idTelefonArt = telefonEintrag.idTelefonArt;
		newEntryTelefonnummer.value.telefonnummer = telefonEintrag.telefonnummer;
		newEntryTelefonnummer.value.bemerkung = telefonEintrag.bemerkung;
		newEntryTelefonnummer.value.istGesperrt = telefonEintrag.istGesperrt;
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
		setMode(Mode.DEFAULT);
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

	function getBezeichnungTelefonart(idTelefonArt: number): string {
		return props.mapTelefonArten.get(idTelefonArt)?.bezeichnung ?? "";
	}

	function cancel() {
		props.schuelerListeManager().schuelerstatus.auswahlClear();
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.NEUAUFNAHME);
		void props.gotoDefaultView(props.schuelerListeManager().auswahl().id);
	}

</script>
