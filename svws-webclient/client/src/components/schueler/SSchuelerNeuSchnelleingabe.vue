<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Anmeldedaten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<ui-select label="Status" v-model="selectedStatus" :manager="statusManager" :removable="false" searchable />
				<ui-select label="Schuljahresabschnitt" v-model="schuljahresabschnitt" :manager="schuljahresabschnittsManager" readonly required />
				<ui-select label="Jahrgang" v-model="jahrgang" :manager="jahrgangManager" readonly required />
				<ui-select label="Klasse" v-model="klasse" :manager="klassenManager" readonly required />
				<svws-ui-spacing />
				<ui-select label="Einschulungsart" v-model="einschulungsart" :manager="einschulungsartManager" :removable="false" v-if="schulenMitPrimaerstufe" />
				<svws-ui-text-input placeholder="Anmeldedatum" type="date" :model-value="data().anmeldedatum" :valid="istAnmeldedatumGueltig"
					@change="anmeldedatum => patchAnmeldedatum(anmeldedatum)" :readonly />
				<div v-if="anmeldedatumError" class="flex mt-1">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p>{{ anmeldedatumError }}</p>
				</div>
				<svws-ui-text-input placeholder="Aufnahmedatum" type="date" :model-value="data().aufnahmedatum" :valid="istAufnahmedatumGueltig"
					@change="aufnahmedatum => patchAufnahmedatum(aufnahmedatum)" :readonly />
				<div v-if="aufnahmedatumError" class="flex mt-1">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p>{{ aufnahmedatumError }}</p>
				</div>
				<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" :model-value="data().beginnBildungsgang" :valid="istBeginnBildungsgangGueltig" v-if="schulenMitBKoderSK"
					@change="beginnBildungsgang => patchBeginnBildungsgang(beginnBildungsgang)" :readonly />
				<div v-if="(beginnBildungsgangError && schulenMitBKoderSK)" class="flex mt-1">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p>{{ beginnBildungsgangError }}</p>
				</div>
				<svws-ui-input-number placeholder="Dauer Bildungsgang" :model-value="data().dauerBildungsgang" @change="dauerBildungsgang => patch({ dauerBildungsgang }, data().id)" v-if="schulenMitBKoderSK" :readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card title="Persönliche Daten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-text-input placeholder="Nachname"
					:model-value="data().nachname"
					@change="nachname => patchIfValid('nachname', nachname)"
					:valid="fieldIsValid('nachname')" :min-len="1" :max-len="120" :readonly required />
				<svws-ui-text-input placeholder="Rufname"
					:model-value="data().vorname"
					@change="vorname => patchIfValid('vorname', vorname)"
					:valid="fieldIsValid('vorname')" :min-len="1" :max-len="80" :readonly required />
				<svws-ui-text-input placeholder="Alle Vornamen"
					:model-value="data().alleVornamen"
					@change="alleVornamen => patchIfValid('alleVornamen', alleVornamen)"
					:valid="fieldIsValid('alleVornamen')" :max-len="255" :readonly />
				<ui-select label="Geschlecht" :model-value="geschlecht" @update:model-value="setGeschlecht" :manager="geschlechtManager" :removable="false" :readonly />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Straße" type="text" :model-value="strasseSchueler" @change="patchStrasse" :valid="fieldIsValid('strassenname')" :max-len="55" :readonly />
				<ui-select label="Wohnort" v-model="wohnortID" :manager="wohnortManager" searchable :readonly />
				<svws-ui-spacing />
				<ui-select label="Ortsteil" v-model="ortsteilSelected" :manager="ortsteilManager" searchable :readonly />
				<svws-ui-text-input placeholder="Geburtsdatum" required type="date" :model-value="data().geburtsdatum" :valid="istGeburtsdatumGueltig"
					@change="geburtsdatum => (istGeburtsdatumGueltig(geburtsdatum) && patch({ geburtsdatum }, data().id))" :readonly />
				<div v-if="!istGeburtsdatumGueltig(data().geburtsdatum)" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p>Das Alter muss zwischen 4 und 50 Jahren liegen.</p>
				</div>
				<svws-ui-text-input placeholder="Geburtsort" :model-value="data().geburtsort" @change="geburtsort => patch({ geburtsort }, data().id)" :readonly />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" type="tel" :model-value="data().telefon" @change="telefon => patchIfValid('telefon', telefon)" :valid="fieldIsValid('telefon')" :max-len="20" :readonly />
				<svws-ui-text-input placeholder="Mobil/Fax" type="tel" :model-value="data().telefonMobil"
					@change="telefonMobil => patchIfValid('telefonMobil', telefonMobil)" :valid="fieldIsValid('telefonMobil')" :max-len="20" :readonly />
				<svws-ui-text-input placeholder="E-Mail" type="email" :model-value="data().emailPrivat"
					@change="emailPrivat => patchIfValid('emailPrivat', emailPrivat)" :readonly />
				<svws-ui-spacing />
				<ui-select label="1. Staatsangehörigkeit" v-model="staatsangehoerigkeit" :manager="staatsangehoerigkeitenManager" searchable :readonly />
				<ui-select label="2. Staatsangehörigkeit" v-model="staatsangehoerigkeit2" :manager="staatsangehoerigkeitenManager" searchable :readonly />
				<ui-select label="Konfession" v-model="religion" :manager="religionManager" :removable="false" searchable :readonly />
				<svws-ui-spacing />
				<svws-ui-checkbox v-model="hatMigrationshintergrund" type="checkbox" title="Migrationshintergrund" :readonly>
					Migrationshintergrund vorhanden
				</svws-ui-checkbox>
				<svws-ui-input-number placeholder="Zuzugsjahr" :model-value="data().zuzugsjahr" @change="zuzugsjahr => patch({ zuzugsjahr }, data().id)"
					:readonly="(!auswahlMigrationsHintergrund || readonly)" />
				<ui-select label="Geburtsland" v-model="geburtsland" :manager="geburtslandManager" :readonly="(!auswahlMigrationsHintergrund || readonly)" :removable="false" searchable />
				<svws-ui-spacing />
				<ui-select label="Geburtsland Mutter" v-model="geburtslandMutter" :manager="geburtslandManager" :readonly="(!auswahlMigrationsHintergrund || readonly)" :removable="false" searchable />
				<ui-select label="Geburtsland Vater" v-model="geburtslandVater" :manager="geburtslandManager" :readonly="(!auswahlMigrationsHintergrund || readonly)" :removable="false" searchable />
				<ui-select label="Verkehrssprache" v-model="verkehrssprache" :manager="verkehrsspracheManager" :readonly="(!auswahlMigrationsHintergrund || readonly)" :removable="false" searchable />
				<svws-ui-spacing />
				<ui-select label="Fahrschüler" v-model="fahrschuelerart" :manager="fahrschuelerartManager" :removable="false" searchable :readonly />
				<ui-select label="Haltestelle" v-model="haltestelle" :manager="haltestellenManager" :removable="false" searchable :readonly />
				<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht" :model-value="data().religionabmeldung"
					@change="religionabmeldung => patch({ religionabmeldung }, data().id)" type="date" :readonly />
				<svws-ui-spacing />
				<ui-select label="Ext. ID-Nr." v-model="externeSchulNr" :manager="externeIDNrManager" :removable="false" searchable :readonly />
				<template v-if="props.serverMode === ServerMode.DEV">
					<svws-ui-text-input placeholder="Schülerausweis-Nummer" :model-value="data().idSchuelerausweis"
						@change="value => patch({ idSchuelerausweis : value ?? null }, data().id)" removable :readonly />
				</template>
				<svws-ui-checkbox v-model="schwerbehinderung" span="full">Schwerstbehinderung</svws-ui-checkbox>
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
					<svws-ui-button @click="deleteErzieherRequest" type="trash" :disabled="(selectedErz.length === 0) || (readonly)" />
					<svws-ui-button @click="addErzieher" type="icon" title="Erziehungsberechtigten hinzufügen" :disabled="readonly">
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
					<ui-select label="Wohnort" v-model="erzWohnort" :manager="wohnortManager" searchable :readonly />
					<ui-select label="Ortsteil" v-model="erzOrtsteil" :manager="erzOrtsteilManager" :readonly="(!erzieher.wohnortID || readonly)" searchable />
					<svws-ui-spacing />
					<svws-ui-textarea-input placeholder="Bemerkungen" :model-value="erzieher?.bemerkungen" span="full" autoresize
						@change="bemerkungen => (erzieher !== undefined) && patchSchuelerErziehereintrag({ bemerkungen: bemerkungen === null ? '' : bemerkungen }, erzieher.id)"
						:readonly />
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
						<svws-ui-button @click="saveSecondErzieher" :disabled="(!mandatoryInputIsValid(zweiterErz.vorname, 120))
							|| (!mandatoryInputIsValid(zweiterErz.nachname, 120)) || (!hatKompetenzUpdate)">
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
					@update:model-value="verpflichtungSprachfoerderkurs => patchSchuelerSchulbesuchsdaten({ verpflichtungSprachfoerderkurs }, data().id)">
					Verpflichtung f. Sprachförderkurs
				</svws-ui-checkbox>
				<svws-ui-checkbox title="Teilnahme an Sprachförderkurs" :model-value="dataSchulbesuchsdaten.teilnahmeSprachfoerderkurs"
					@update:model-value="teilnahmeSprachfoerderkurs => patchSchuelerSchulbesuchsdaten({ teilnahmeSprachfoerderkurs }, data().id)">
					Teilnahme an Sprachförderkurs
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card title="Vermerke anlegen" class="col-span-full">
			<svws-ui-table clickable
				@update:clicked="patchVermerk" :items="getListSchuelerVermerkeintraege()" :no-data="getListSchuelerVermerkeintraege().size() === 0"
				:columns="columnsVermerke" :selectable="true" v-model="selectedVermerk">
				<template #cell(idVermerkart)="{ value }">
					{{ getBezeichnungVermerkArt(value) }}
				</template>
				<template #actions>
					<div class="inline-flex gap-4">
						<svws-ui-button @click="deleteVermerke" type="trash" :disabled="(selectedVermerk.length === 0) || (!hatKompetenzUpdate)" />
						<svws-ui-button @click="addVermerk" type="icon" title="Vermerk hinzufügen" :disabled="!hatKompetenzUpdate">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-table>

			<svws-ui-modal :show="showModalVermerke" @update:show="closeModalVermerke">
				<template #modalTitle>Vermerk hinzufügen</template>
				<template #modalContent>
					<ui-select label="Vermerkart" v-model="vermerkArt" :manager="VermerkArtManager" :removable="false" searchable />
					<svws-ui-textarea-input class="col-span-full" :model-value="newEntryVermerk.bemerkung" @input="v => newEntryVermerk.bemerkung = v ?? ''" placeholder="Bemerkung" :autoresize="true" />
					<svws-ui-notification type="warning" v-if="mapVermerkArten.size === 0">
						Die Liste der Vermerkarten ist leer. Es sollte mindestens eine Vermerkart unter Schule/Kataloge angelegt werden, damit zusätzliche Vermerke eine gültige Zuordnung haben.
					</svws-ui-notification>
					<div class="mt-7 flex flex-row gap-4 justify-end">
						<svws-ui-button type="secondary" @click="closeModalVermerke">Abbrechen</svws-ui-button>
						<svws-ui-button @click="sendRequestVermerk"
							:disabled="(vermerkArt === null) || (mapVermerkArten.size === 0) || (newEntryVermerk.bemerkung === '') || (!hatKompetenzUpdate)">
							Speichern
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
		</svws-ui-content-card>

		<svws-ui-content-card />
		<svws-ui-content-card class="col-span-full">
			<div class="-mt-16 flex flex-row gap-4 justify-end w-full">
				<svws-ui-button type="primary" @click="gotoSchuelerNeuView">Weiteren Schüler anlegen</svws-ui-button>
				<svws-ui-button type="secondary" @click="cancel">Neuaufnahme beenden</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerStammdaten, Telefonart, OrtsteilKatalogEintrag, EinschulungsartKatalogEintrag, NationalitaetenKatalogEintrag, SchuelerStatusKatalogEintrag, VerkehrsspracheKatalogEintrag, KlassenDaten, VermerkartEintrag } from "@core";
	import { BenutzerKompetenz, SchuelerTelefon, ArrayList, ErzieherStammdaten, AdressenUtils, Geschlecht, Kindergartenbesuch, Nationalitaeten, SchuelerStatus, Schulform, Verkehrssprache, ServerMode, DateUtils, SchuelerVermerke } from "@core";
	import { computed, ref, watch } from "vue";
	import type { SchuelerNeuSchnelleingabeProps } from "~/components/schueler/SSchuelerNeuSchnelleingabeProps";
	import { erzieherArtSort, orte_sort, ortsteilSort } from "~/utils/helfer";
	import type { DataTableColumn } from "@ui";
	import { CoreTypeSelectManager, SelectManager } from "@ui";
	import { emailIsValid, mandatoryInputIsValid, optionalInputIsValid, phoneNumberIsValid } from "~/util/validation/Validation";

	const props = defineProps<SchuelerNeuSchnelleingabeProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed<boolean>(() => !props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);

	const data = () => props.schuelerListeManager().daten();

	const dataSchulbesuchsdaten = computed(() => props.schuelerSchulbesuchsManager().daten);

	const dataSchuelerLernabschnittdaten = computed(() => props.schuelerLernabschnittManager().lernabschnittGet());

	const schulenMitPrimaerstufe = computed(() => {
		const erlaubteSchulformen = [Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V];
		return erlaubteSchulformen.includes(props.schulform);
	});

	const schulenMitBKoderSK = computed(() => props.schulform === Schulform.BK || props.schulform === Schulform.SK);

	const schuljahresabschnitte = computed(() => Array.from(props.schuelerListeManager().schuljahresabschnitte.list()));

	const schuljahresabschnittsManager = new SelectManager({ options: schuljahresabschnitte, optionDisplayText: i => `${i.schuljahr}/${(i.schuljahr + 1) % 100}.${i.abschnitt}`, selectionDisplayText: i => `${i.schuljahr}/${(i.schuljahr + 1) % 100}.${i.abschnitt}` });

	const schuljahresabschnitt = computed({
		get: () => {
			const id = dataSchuelerLernabschnittdaten.value.schuljahresabschnitt;
			return schuljahresabschnitte.value.find(i => i.id === id) ?? null;
		},
		set: (value) => {
			dataSchuelerLernabschnittdaten.value.schuljahresabschnitt = value?.id ?? -1;
			void loadKlassenFuerAbschnitt(value?.id ?? -1);
		},
	});

	const jahrgaenge = computed(() => Array.from(props.schuelerListeManager().jahrgaenge.list()));

	const jahrgangManager = new SelectManager({ options: jahrgaenge, optionDisplayText: i => i.kuerzel ?? '', selectionDisplayText: i => i.kuerzel ?? '' });

	const jahrgang = computed({
		get: () => {
			const id = dataSchuelerLernabschnittdaten.value.jahrgangID ?? -1;
			return jahrgaenge.value.find(i => i.id === id) ?? null;
		},
		set: (value) => dataSchuelerLernabschnittdaten.value.jahrgangID = value?.id ?? -1,
	});

	const klassenFuerAbschnitt = ref<KlassenDaten[]>([]);

	async function loadKlassenFuerAbschnitt(idAbschnitt: number) {
		if (idAbschnitt <= 0) {
			klassenFuerAbschnitt.value = [];
			return;
		}
		klassenFuerAbschnitt.value = Array.from(await props.getSchuelerKlassenFuerAbschnitt(idAbschnitt));
	}

	const klassen = computed(() => {
		const global = Array.from(props.schuelerListeManager().klassen.list());
		return klassenFuerAbschnitt.value.length > 0 ? klassenFuerAbschnitt.value : global;
	});

	const klassenManager = new SelectManager({ options: klassen, optionDisplayText: i => i.kuerzel ?? '', selectionDisplayText: i => i.kuerzel ?? '' });

	const klasse = computed({
		get: () => {
			const id = dataSchuelerLernabschnittdaten.value.klassenID ?? -1;
			return klassen.value.find(i => i.id === id) ?? null;
		},
		set: (value) => dataSchuelerLernabschnittdaten.value.klassenID = value?.id ?? -1,
	});


	const schwerbehinderung = computed<boolean>({
		get: () => dataSchuelerLernabschnittdaten.value.hatSchwerbehinderungsNachweis,
		set: (value) => void props.patchSchuelerLernabschnittsdaten({ hatSchwerbehinderungsNachweis: value }, dataSchuelerLernabschnittdaten.value.id),
	});

	const kindergaerten = computed(() => props.mapKindergaerten.values());

	const nameKindergartenManager = new SelectManager({ options: kindergaerten, optionDisplayText: i => i.bezeichnung, selectionDisplayText: i => i.bezeichnung });

	const auswahlKindergartenID = ref(dataSchulbesuchsdaten.value.idKindergarten);

	const kindergarten = computed({
		get: () => {
			const id = auswahlKindergartenID.value ?? -1;
			return props.mapKindergaerten.get(id);
		},
		set: (value) => {
			auswahlKindergartenID.value = value?.id ?? -1;
			void props.patchSchuelerSchulbesuchsdaten({ idKindergarten: value?.id ?? null }, data().id);
		},
	});

	const orte = computed(() => props.mapOrte.values());

	const wohnortManager = new SelectManager({ options: orte, optionDisplayText: i => `${i.plz} ${i.ortsname}`, sort: orte_sort, selectionDisplayText: i => `${i.plz} ${i.ortsname}` });

	const auswahlWohnortID = ref(data().wohnortID ?? null);

	const wohnortID = computed({
		get: () => {
			const id = auswahlWohnortID.value ?? -1;
			return props.mapOrte.get(id) ?? null;
		},
		set: (value) => {

			auswahlWohnortID.value = value?.id ?? -1;
			void props.patch({ wohnortID: value?.id ?? null }, data().id);
		},
	});

	const ortsteile = computed(() => Array.from(props.mapOrtsteile.values()));

	const ortsteileFiltered = computed(() => {
		const wohnortID = auswahlWohnortID.value;
		if (wohnortID === null) {
			return ortsteile.value;
		}
		return ortsteile.value.filter(o => o.ort_id === wohnortID);
	});

	const items = computed(() => ortsteileFiltered.value);

	const auswahlOrtsteilID = ref<number | null>(data().ortsteilID ?? null);

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
				void props.patch({ ortsteilID: null }, data().id);
			}

			auswahlOrtsteilID.value = value?.id ?? -1;
			void props.patch({ ortsteilID: value?.id ?? null }, data().id);
		},
	});

	const geschlecht = ref(Geschlecht.fromValue(data().geschlecht));

	const geschlechtManager = new SelectManager({ options: Geschlecht.values(), optionDisplayText: i => i.text, selectionDisplayText: i => i.text });

	async function setGeschlecht(value: Geschlecht | null | undefined): Promise<void> {
		geschlecht.value = value ?? null;
		ortsteilManager.setConfig();
		data().geschlecht = value?.id ?? -1;
		await props.patch({ geschlecht: value?.id }, data().id);
	}

	const einschulungsarten = computed(() => props.mapEinschulungsarten.values());

	const einschulungsartManager = new SelectManager({ options: einschulungsarten, optionDisplayText: i => i.text, selectionDisplayText: i => i.text });

	const auswahlEinschulungsart = ref(dataSchulbesuchsdaten.value.grundschuleEinschulungsartID);

	const einschulungsart = computed({
		get: () => {
			const id = auswahlEinschulungsart.value ?? -1;
			return props.mapEinschulungsarten.get(id) ?? null;
		},
		set: (value: EinschulungsartKatalogEintrag) => {
			auswahlEinschulungsart.value = value.id;
			void props.patchSchuelerSchulbesuchsdaten({ grundschuleEinschulungsartID: value.id }, data().id);
		},
	});

	const strasseSchueler = computed(() => AdressenUtils.combineStrasse(data().strassenname ?? "", data().hausnummer ?? "", data().hausnummerZusatz ?? ""));

	function patchStrasse(value: string | null) {
		if (value !== null) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname: vals[0], hausnummer: vals[1], hausnummerZusatz: vals[2] }, data().id);
		}
	}

	const auswahlStatusID = ref<number | null>(data().status);

	const statusManager = new CoreTypeSelectManager({ clazz: SchuelerStatus.class, schuljahr: schuljahr, schulformen: props.schulform, optionDisplayText: "text", selectionDisplayText: "text" });

	const selectedStatus = computed<SchuelerStatusKatalogEintrag | null>({
		get: (): SchuelerStatusKatalogEintrag | null => {
			return SchuelerStatus.data().getWertByKuerzel('' + auswahlStatusID.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			auswahlStatusID.value = value?.id ?? null;
			void props.patch({ status: value?.id }, data().id);
		},
	});

	const auswahlMigrationsHintergrund = ref<boolean>(data().hatMigrationshintergrund);

	const hatMigrationshintergrund = computed<boolean>({
		get: () => data().hatMigrationshintergrund,
		set: (val) => {
			auswahlMigrationsHintergrund.value = val;
			return void props.patch({ hatMigrationshintergrund: val }, data().id);
		},
	});

	const staatsangehoerigkeitenManager = new CoreTypeSelectManager({ clazz: Nationalitaeten.class, schuljahr: schuljahr, optionDisplayText: "text", selectionDisplayText: "text" });

	const staatsangehoerigkeitID = ref(data().staatsangehoerigkeitID ?? null);

	const staatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			return Nationalitaeten.getByISO3(staatsangehoerigkeitID.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			staatsangehoerigkeitID.value = value?.iso3 ?? null;
			void props.patch({ staatsangehoerigkeitID: value?.iso3 ?? null }, data().id);
		},
	});

	const staatsangehoerigkeit2ID = ref(data().staatsangehoerigkeit2ID ?? null);

	const staatsangehoerigkeit2 = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			return Nationalitaeten.getByISO3(staatsangehoerigkeit2ID.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			staatsangehoerigkeit2ID.value = value?.iso3 ?? null;
			void props.patch({ staatsangehoerigkeit2ID: value?.iso3 ?? null }, data().id);
		},
	});

	const religionen = computed(() => props.mapReligionen.values());

	const religionManager = new SelectManager({ options: religionen, optionDisplayText: i => i.bezeichnungZeugnis ?? '', selectionDisplayText: i => i.bezeichnungZeugnis ?? '' });

	const auswahlReligionID = ref(data().religionID);

	const religion = computed({
		get: () => {
			const id = auswahlReligionID.value ?? -1;
			return props.mapReligionen.get(id);
		},
		set: (value) => {

			auswahlReligionID.value = value?.id ?? -1;
			void props.patch({ religionID: value?.id ?? null }, data().id);
		},
	});

	const geburtslandManager = new CoreTypeSelectManager({ clazz: Nationalitaeten.class, schuljahr: schuljahr, optionDisplayText: "text", selectionDisplayText: "text" });

	const auswahlGeburtsland = ref(data().geburtsland ?? null);

	const geburtsland = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			return Nationalitaeten.getByISO3(auswahlGeburtsland.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			auswahlGeburtsland.value = value?.iso3 ?? null;
			void props.patch({ geburtsland: value?.iso3 }, data().id);
		},
	});

	const auswahlGeburtslandMutter = ref(data().geburtslandMutter ?? null);

	const geburtslandMutter = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			return Nationalitaeten.getByISO3(auswahlGeburtslandMutter.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			auswahlGeburtslandMutter.value = value?.iso3 ?? null;
			void props.patch({ geburtslandMutter: value?.iso3 }, data().id);
		},
	});

	const auswahlGeburtslandVater = ref(data().geburtslandVater ?? null);

	const geburtslandVater = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			return Nationalitaeten.getByISO3(auswahlGeburtslandVater.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			auswahlGeburtslandVater.value = value?.iso3 ?? null;
			void props.patch({ geburtslandVater: value?.iso3 }, data().id);
		},
	});

	const verkehrsspracheManager = new CoreTypeSelectManager({ clazz: Verkehrssprache.class, schuljahr: schuljahr, optionDisplayText: "text", selectionDisplayText: "text" });

	const auswahlVerkehrssprache = ref(data().verkehrspracheFamilie ?? null);

	const verkehrssprache = computed<VerkehrsspracheKatalogEintrag | null>({
		get: (): VerkehrsspracheKatalogEintrag | null => {
			return Verkehrssprache.getByIsoKuerzel(auswahlVerkehrssprache.value)?.daten(schuljahr.value) ?? null;
		},
		set: (value) => {
			auswahlVerkehrssprache.value = value?.iso3 ?? null;
			void props.patch({ verkehrspracheFamilie: value?.iso3 }, data().id);
		},
	});

	const externeIDNummern = computed(() => props.mapSchulen.values());

	const externeIDNrManager = new SelectManager({ options: externeIDNummern, optionDisplayText: i => i.kuerzel ?? i.schulnummerStatistik ?? i.kurzbezeichnung ?? i.name,
		selectionDisplayText: i => i.kuerzel ?? i.schulnummerStatistik ?? i.kurzbezeichnung ?? i.name });

	const auswahlExterneIDNr = ref(data().externeSchulNr ?? null);

	const externeSchulNr = computed({
		get: () => props.mapSchulen.get(auswahlExterneIDNr.value ?? "") ?? null,
		set: (value) => {
			auswahlExterneIDNr.value = value?.schulnummerStatistik ?? null;
			void props.patch({ externeSchulNr: value?.schulnummerStatistik }, data().id);
		},
	});

	const fahrschuelerarten = computed(() => props.mapFahrschuelerarten.values());

	const fahrschuelerartManager = new SelectManager({ options: fahrschuelerarten, optionDisplayText: i => i.bezeichnung ?? '', selectionDisplayText: i => i.bezeichnung ?? '' });

	const auswahlfahrschuelerartID = ref(data().fahrschuelerArtID ?? null);

	const fahrschuelerart = computed({
		get: () => props.mapFahrschuelerarten.get(auswahlfahrschuelerartID.value ?? -1) ?? null,
		set: (value) => {
			const id = value?.id ?? null;
			auswahlfahrschuelerartID.value = id;
			void props.patch({ fahrschuelerArtID: id }, data().id);
		},
	});

	const haltestellen = computed(() => props.mapHaltestellen.values());

	const haltestellenManager = new SelectManager({ options: haltestellen, optionDisplayText: i => i.bezeichnung ?? '', selectionDisplayText: i => i.bezeichnung ?? '' });

	const auswahlHaltestellenID = ref(data().haltestelleID ?? null);

	const haltestelle = computed({
		get: () => props.mapHaltestellen.get(auswahlHaltestellenID.value ?? -1) ?? null,
		set: (value) => {
			const id = value?.id ?? null;
			auswahlHaltestellenID.value = id;
			void props.patch({ haltestelleID: id }, data().id);
		},
	});

	const dauerKindergartenManager = new CoreTypeSelectManager({ clazz: Kindergartenbesuch.class, schuljahr: schuljahr, optionDisplayText: "text", selectionDisplayText: "text" });

	const dauerKindergartenbesuchID = ref(dataSchulbesuchsdaten.value.idDauerKindergartenbesuch ?? null);

	const dauerKindergarten = computed({
		get: () => {
			if (dauerKindergartenbesuchID.value === null) {
				return null;
			}
			return Kindergartenbesuch.data().getEintragByID(dauerKindergartenbesuchID.value) ?? null;
		},
		set: (value) => {
			dauerKindergartenbesuchID.value = value?.id ?? null;
			void props.patchSchuelerSchulbesuchsdaten({ idDauerKindergartenbesuch: value?.id ?? null }, data().id);
		},
	});

	const anmeldedatumError = ref<string | null>();
	const aufnahmedatumError = ref<string | null>();
	const beginnBildungsgangError = ref<string | null>();

	// validation logic
	function fieldIsValid(field: keyof SchuelerStammdaten | null): (v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'nachname':
					return mandatoryInputIsValid(data().nachname, 120);
				case 'vorname':
					return mandatoryInputIsValid(data().vorname, 80);
				case 'alleVornamen':
					return optionalInputIsValid(data().alleVornamen, 255);
				case 'geschlecht':
					return Geschlecht.fromValue(data().geschlecht) !== null;
				case 'strassenname':
					return adresseIsValid();
				case 'telefon':
					return phoneNumberIsValid(data().telefon, 20);
				case 'telefonMobil':
					return phoneNumberIsValid(data().telefon, 20);
				case 'emailPrivat':
					return emailIsValid(data().emailPrivat, 20);
				case 'geburtsland':
					return (data().geburtsland === null) || (Nationalitaeten.getByISO3(data().geburtsland) !== null);
				case 'geburtslandMutter':
					return (data().geburtslandMutter === null) || (Nationalitaeten.getByISO3(data().geburtslandMutter) !== null);
				case 'geburtslandVater':
					return (data().geburtslandVater === null) || (Nationalitaeten.getByISO3(data().geburtslandVater) !== null);
				default:
					return true;
			}
		};
	}

	async function patchIfValid(field: string, value: string | null | undefined) {
		const v = (value === undefined) ? null : value;
		switch (field) {
			case 'nachname':
				if (!mandatoryInputIsValid(v ?? null, 120)) {
					return;
				}
				await props.patch({ nachname: v ?? undefined }, data().id);
				return;
			case 'vorname':
				if (!mandatoryInputIsValid(v ?? null, 120)) {
					return;
				}
				await props.patch({ vorname: v ?? undefined }, data().id);
				return;
			case 'alleVornamen': {
				if (optionalInputIsValid(v, 255)) {
					await props.patch({ alleVornamen: v ?? undefined }, data().id);
				}
				return;
			}
			case 'telefon':
				if ((v !== null) && (v.length > 0) && !phoneNumberIsValid(v, 20)) {
					return;
				}
				await props.patch({ telefon: v ?? null }, data().id);
				return;
			case 'telefonMobil':
				if ((v !== null) && (v.length > 0) && !phoneNumberIsValid(v, 20)) {
					return;
				}
				await props.patch({ telefonMobil: v ?? null }, data().id);
				return;
			case 'emailPrivat':
				if ((v !== null) && (v.length > 0) && !emailIsValid(v, 100)) {
					return;
				}
				await props.patch({ emailPrivat: v ?? null }, data().id);
				return;
			default:
			{
				const obj: any = {};
				obj[field] = v ?? null;
				await props.patch(obj, data().id);
			}
		}
	}

	function adresseIsValid() {
		return optionalInputIsValid(data().strassenname, 55) &&
			optionalInputIsValid(data().hausnummer, 10) &&
			optionalInputIsValid(data().hausnummerZusatz, 30);
	}

	function parseISOToDate(strDate: string | null) {
		if (strDate === null) {
			return null;
		}
		try {
			const d = DateUtils.extractFromDateISO8601(strDate);
			return new Date(d[0], d[1] - 1, d[2]);
		} catch {
			return null;
		}
	}

	function istAnmeldedatumGueltig(strDate: string | null) {
		if (strDate === null) {
			return true;
		}
		const d = parseISOToDate(strDate);
		if (d === null) {
			return false;
		}
		const today = new Date();
		// Datum darf nicht in der Zukunft liegen (heutige Datum ist erlaubt)
		return d.getTime() <= new Date(today.getFullYear(), today.getMonth(), today.getDate()).getTime();
	}

	async function patchAnmeldedatum(value: string | null) {
		if (!istAnmeldedatumGueltig(value)) {
			anmeldedatumError.value = "Das Anmeldedatum darf nicht in der Zukunft liegen";
			return;
		}
		anmeldedatumError.value = null;
		await props.patch({ anmeldedatum: value ?? null }, data().id);
	}

	function istAufnahmedatumGueltig(strDate: string | null) {
		if (strDate === null) {
			return true;
		}
		const aufnahme = parseISOToDate(strDate);
		if (aufnahme === null) {
			return false;
		}
		// Aufnahmedatum darf nicht vor Anmeldedatum liegen
		const anmeld = parseISOToDate(data().anmeldedatum);
		if (anmeld !== null) {
			return aufnahme.getTime() >= anmeld.getTime();
		}
		return true;
	}

	async function patchAufnahmedatum(value: string | null) {
		if (!istAufnahmedatumGueltig(value)) {
			aufnahmedatumError.value = "Das Aufnahmedatum darf nicht vor dem Anmeldedatum liegen";
			return;
		}
		aufnahmedatumError.value = null;
		await props.patch({ aufnahmedatum: value ?? null }, data().id);
	}

	function istBeginnBildungsgangGueltig(strDate: string | null) {
		if (strDate === null) {
			return true;
		}
		const beginn = parseISOToDate(strDate);
		if (beginn === null) {
			return false;
		}
		// Beginn des Bildungsgangs darf nicht vor Aufnahmedatum liegen
		const aufnahme = parseISOToDate(data().aufnahmedatum);
		if (aufnahme !== null) {
			return beginn.getTime() >= aufnahme.getTime();
		}
		return true;
	}

	async function patchBeginnBildungsgang(value: string | null) {
		if (!istBeginnBildungsgangGueltig(value)) {
			beginnBildungsgangError.value = "Der Beginn des Bildungsgangs darf nicht vor dem Aufnahmedatum liegen";
			return;
		}
		beginnBildungsgangError.value = null;
		await props.patch({ beginnBildungsgang: value ?? null }, data().id);
	}

	function istGeburtsdatumGueltig(strDate: string | null) {
		if (strDate === null) {
			return true;
		}
		try {
			const date = DateUtils.extractFromDateISO8601(strDate);
			const curDate = new Date();
			const diffYear = curDate.getFullYear() - date[0];
			return (diffYear > 3) && (diffYear < 51);
		} catch {
			return false;
		}
	}

	// Anlegen von Erziehungsberechtigten
	const erzieher = ref<ErzieherStammdaten | undefined>();

	const selectedErz = ref<ErzieherStammdaten[]>([]);

	const ersterErz = ref<ErzieherStammdaten>(new ErzieherStammdaten());
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
			if (ersteErzId !== zweiteErzId) {
				return ersteErzId - zweiteErzId;
			}
			return a.id - b.id;
		});
	});

	const columnsErzieher: DataTableColumn[] = [
		{ key: "idErzieherArt", label: "Art" },
		{ key: "name", label: "Name" },
		{ key: "eMail", label: "E-Mail" },
		{ key: "adresse", label: "Adresse" },
		{ key: "erhaeltAnschreiben", label: "Anschreiben", tooltip: "Erhält Anschreiben", fixedWidth: 3, align: "center" },
		{ key: "actions", label: "2. Person", tooltip: "Weiteres Elternteil hinzufügen", fixedWidth: 10, align: "center" },
	];

	const erzieherarten = computed(() => props.mapErzieherarten.values());

	const erzieherartenManager = new SelectManager({ options: erzieherarten, sort: erzieherArtSort, optionDisplayText: i => i.bezeichnung, selectionDisplayText: i => i.bezeichnung });

	const erzieherart = computed({
		get: () => props.mapErzieherarten.get(erzieher.value?.idErzieherArt ?? -1) ?? null,
		set: (value) => {
			const id = value?.id ?? undefined;
			if (erzieher.value === undefined) {
				return;
			}
			erzieher.value.idErzieherArt = id ?? null;
			void props.patchSchuelerErziehereintrag({ idErzieherArt: id ?? null }, erzieher.value.id);
		},
	});

	const erzWohnort = computed({
		get: () => props.mapOrte.get(erzieher.value?.wohnortID ?? -1) ?? null,
		set: (value) => {
			if (erzieher.value === undefined) {
				return;
			}
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
			if (erzieher.value === undefined) {
				return;
			}
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
			if (erzieher.value === undefined) {
				return;
			}
			zweiterErz.value.staatsangehoerigkeitID = value?.iso3 ?? null;
		},
	});

	const erzOrtsteileFiltered = computed(() => {
		const wohnortID = erzieher.value?.wohnortID;
		if (wohnortID === null) {
			return ortsteile.value;
		}
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
			if (erzieher.value === undefined) {
				return;
			}
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
		openModalErzieher();
		ersterErz.value.id = 0;
	}

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		resetTelefonnummer();
		closeModalTelefonnummer();
		resetErzieher();
		closeModalErzieher();
		resetVermerk();
		closeModalVermerke();
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
		zweiterErz.value.strassenname = item.strassenname;
		zweiterErz.value.hausnummer = item.hausnummer;
		zweiterErz.value.hausnummerZusatz = item.hausnummerZusatz;
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
		const schuelerId = data().id;
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
		const schuelerId = data().id;
		const savedEntry = await props.addSchuelerErziehereintrag(partialDataWithoutId, schuelerId, 1);
		ersterErz.value.id = savedEntry.id;
		zweiterErz.value.idErzieherArt = ersterErz.value.idErzieherArt;
		zweiterErz.value.wohnortID = ersterErz.value.wohnortID;
		zweiterErz.value.ortsteilID = ersterErz.value.ortsteilID;
		zweiterErz.value.bemerkungen = ersterErz.value.bemerkungen;
		zweiterErz.value.strassenname = ersterErz.value.strassenname;
		zweiterErz.value.hausnummer = ersterErz.value.hausnummer;
		zweiterErz.value.hausnummerZusatz = ersterErz.value.hausnummerZusatz;
		istErsterErzGespeichert.value = true;
	}

	// Speichert den zweiten Erziehungsberechtigten (Position 2) und beendet anschließend den Bearbeitungsmodus.
	async function saveSecondErzieher() {
		const { id, idSchueler, erhaeltAnschreiben, ...partialDataWithoutId } = zweiterErz.value;
		const schuelerId = data().id;
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
		if (selectedErz.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of selectedErz.value) {
			ids.add(s.id);
		}
		await props.deleteSchuelerErziehereintrage(ids);
		selectedErz.value = [];
		erzieher.value = undefined;
	}

	watch(() => props.getListSchuelerErziehereintraege(), (neu) => {
		if (neu.isEmpty()) {
			erzieher.value = undefined;
		} else {
			erzieher.value = neu.getFirst();
		}
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
	];

	const telefonArten = computed(() => props.mapTelefonArten.values());

	const telefonArtManager = new SelectManager({ options: telefonArten, optionDisplayText: i => i.bezeichnung, selectionDisplayText: i => i.bezeichnung });

	const telefonArt = computed<Telefonart | null>({
		get: () => props.mapTelefonArten.get(newEntryTelefonnummer.value.idTelefonArt) ?? null,
		set: (value) => newEntryTelefonnummer.value.idTelefonArt = value === null ? -1 : value.id,
	});

	function addTelefonnummer() {
		resetTelefonnummer();
		setMode(Mode.ADD);
		openModalTelefonnummer();
	}

	async function sendRequestTelefonnummer() {
		const { id, idSchueler, ...partialDataWithoutId } = newEntryTelefonnummer.value;
		const schuelerId = data().id;
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
		if (selected.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of selected.value) {
			ids.add(s.id);
		}
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

	// Anlegen von Vermerken
	const selectedVermerk = ref<SchuelerVermerke[]>([]);
	const newEntryVermerk = ref<SchuelerVermerke>(new SchuelerVermerke());
	const showModalVermerke = ref<boolean>(false);

	const columnsVermerke: DataTableColumn[] = [
		{ key: "idVermerkart", label: "Vermerkart" },
		{ key: "bemerkung", label: "Bemerkung", span: 2 },
		{ key: "angelegtVon", label: "Angelegt von" },
		{ key: "geaendertVon", label: "Geändert von" },
		{ key: "datum", label: "Erstellt am", span: 1, align: "right" },
	];

	const vermerkArten = computed(() => props.mapVermerkArten.values());

	const VermerkArtManager = new SelectManager({
		options: vermerkArten,
		optionDisplayText: i => i.bezeichnung ?? "",
		selectionDisplayText: i => i.bezeichnung ?? "" });

	const vermerkArt = computed<VermerkartEintrag | undefined>({
		get: () => props.mapVermerkArten.get(newEntryVermerk.value.idVermerkart ?? -1),
		set: (value) => newEntryVermerk.value.idVermerkart = value?.id ?? -1,
	});

	function addVermerk() {
		resetVermerk();
		setMode(Mode.ADD);
		openModalVermerke();
	}

	async function sendRequestVermerk() {
		const { id, datum, angelegtVon, geaendertVon, ...partialDataWithoutId } = newEntryVermerk.value;
		partialDataWithoutId.idSchueler = data().id;
		showModalVermerke.value = false;
		if (currentMode.value === Mode.ADD) {
			await props.addSchuelerVermerkeintrag(partialDataWithoutId);
		}
		if (currentMode.value === Mode.PATCH) {
			await props.patchSchuelerVermerkeintrag(partialDataWithoutId, newEntryVermerk.value.id);
		}
		enterDefaultMode();
	}

	function patchVermerk(vermerkEintrag: SchuelerVermerke) {
		resetVermerk();
		setMode(Mode.PATCH);
		newEntryVermerk.value.id = vermerkEintrag.id;
		newEntryVermerk.value.idVermerkart = vermerkEintrag.idVermerkart;
		newEntryVermerk.value.bemerkung = vermerkEintrag.bemerkung;
		openModalVermerke();
	}

	async function deleteVermerke() {
		if (selectedVermerk.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of selectedVermerk.value) {
			ids.add(s.id);
		}
		await props.deleteSchuelerVermerkeintraege(ids);
		selectedVermerk.value = [];
	}

	function openModalVermerke() {
		showModalVermerke.value = true;
	}

	function closeModalVermerke() {
		resetVermerk();
		setMode(Mode.DEFAULT);
		showModalVermerke.value = false;
	}

	function resetVermerk() {
		const defaultVermerk = new SchuelerVermerke();
		const ersteVermerkArt = props.mapVermerkArten.values().next().value;
		defaultVermerk.idVermerkart = ersteVermerkArt?.id ?? 0;
		defaultVermerk.bemerkung = '';
		newEntryVermerk.value = defaultVermerk;
	}

	function getBezeichnungVermerkArt(idVermerkArt: number): string {
		return props.mapVermerkArten.get(idVermerkArt)?.bezeichnung ?? "";
	}


	function cancel() {
		props.schuelerListeManager().schuelerstatus.auswahlClear();
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.NEUAUFNAHME);
		void props.gotoDefaultView(props.schuelerListeManager().auswahl().id);
	}

	await loadKlassenFuerAbschnitt(schuljahresabschnitt.value?.id ?? -1);

</script>
