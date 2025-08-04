<template>
	<div class="page page-grid-cards" v-if="data.id === 0">
		<svws-ui-content-card title="Persönliche Daten">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-text-input placeholder="Name" required v-model="data.nachname" :valid="fieldIsValid('nachname')" />
				<svws-ui-text-input placeholder="Vorname" required v-model="data.vorname" :valid="fieldIsValid('vorname')" />
				<svws-ui-text-input placeholder="Weitere Vornamen" v-model="data.alleVornamen" :valid="fieldIsValid('alleVornamen')" />
				<svws-ui-select title="Geschlecht" required :items="Geschlecht.values()" :item-text="i => i.text"
					:model-value="Geschlecht.fromValue(data.geschlecht)" @update:model-value="v => data.geschlecht = v?.id ?? -1" />
				<svws-ui-text-input placeholder="Geburtsdatum" required type="date" v-model="data.geburtsdatum" :valid="fieldIsValid('geburtsdatum')" />
				<svws-ui-select title="Status" :items="SchuelerStatus.values()" :item-text="i => i.name().toLowerCase()?? '—'"
					:model-value="SchuelerStatus.data().getWertByKuerzel('' + data.status)"
					@update:model-value="v => data.status = v?.ordinal() ?? -1" :disabled="true" />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addSchuelerStammdaten" :disabled="(!formIsValid) || (!hatKompetenzUpdate)">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>

	<div class="page page-grid-cards" v-if="data.id !== 0">
		<svws-ui-content-card title="Anmeldedaten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-select title="Status" :items="SchuelerStatus.values()" :item-text="i => i.name().toLowerCase()?? '—'"
					:model-value="SchuelerStatus.data().getWertByKuerzel('' + data.status)"
					@update:model-value="v => data.status = v?.ordinal() ?? -1" :disabled="true" />
				<svws-ui-text-input placeholder="Schuljahr" type="text" :disabled="true" />
				<svws-ui-text-input placeholder="Halbjahr" type="text" :disabled="true" />
				<svws-ui-text-input placeholder="Jahrgang" type="text" :disabled="true" />
				<svws-ui-text-input placeholder="Klasse" type="text" :disabled="true" />
				<svws-ui-spacing />
				<svws-ui-select title="Einschulungsart" :items="mapEinschulungsarten" :item-text="i => i.text ?? ''"
					:model-value="mapEinschulungsarten.get(dataSchulbesuchsdaten.grundschuleEinschulungsartID ?? -1)"
					@update:model-value="v => patchSchuelerKindergarten({grundschuleEinschulungsartID: v?.id ?? null}, data.id)"
					removable v-if="schulenMitPrimaerstufe" />
				<!--TODO Anmeldedatum darf nicht in der Zukunft liegen-->
				<svws-ui-text-input placeholder="Anmeldedatum" type="date" v-model="data.anmeldedatum" />
				<!--TODO Aufnahmedatum darf nicht vor dem Anmeldedatum liegen-->
				<svws-ui-text-input placeholder="Aufnahmedatum" type="date" v-model="data.aufnahmedatum" />
				<!--TODO Beginn Bildungsgang darf nicht vor dem Aufnahmedatum liegen-->
				<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" v-model="data.beginnBildungsgang" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Persönliche Daten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<!--TODO Leere Inputfelder unterbinden-->
				<svws-ui-text-input placeholder="Name" required :model-value="data.nachname"
					@change="nachname => patch({ nachname: nachname ?? undefined }, data.id)" />
				<svws-ui-text-input placeholder="Vorname" required :model-value="data.vorname"
					@change="vorname => patch({ vorname: vorname ?? undefined }, data.id)" :valid="fieldIsValid('vorname')" />
				<svws-ui-text-input placeholder="Weitere Vornamen" :model-value="data.alleVornamen"
					@change="alleVornamen => patch({ alleVornamen: alleVornamen ?? undefined }, data.id)" :valid="fieldIsValid('alleVornamen')" />
				<svws-ui-select title="Geschlecht" required :items="Geschlecht.values()" :item-text="i => i.text" v-model="geschlecht" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Straße" type="text" :model-value="strasseSchueler" @change="patchStrasse" :valid="fieldIsValid('strassenname')" />
				<svws-ui-select title="Wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort" :item-text="i => `${i.plz} ${i.ortsname}`"
					v-model="wohnortID" :valid="fieldIsValid('wohnortID')" removable />
				<svws-ui-spacing />
				<svws-ui-select title="Ortsteil" :items="ortsteile" :item-sort="ortsteilSort" :item-text="i => i.ortsteil ?? ''"
					v-model="ortsteilID" :valid="fieldIsValid('ortsteilID')" removable />
				<svws-ui-text-input placeholder="Geburtsdatum" required type="date" :model-value="data.geburtsdatum"
					@change="geburtsdatum => geburtsdatum && patch({geburtsdatum}, data.id)" :valid="fieldIsValid('geburtsdatum')" />
				<svws-ui-text-input placeholder="Geburtsort" :model-value="data.geburtsort" @change="geburtsort => patch({ geburtsort }, data.id)" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon" type="tel" :model-value="data.telefon" @change="telefon => patch({ telefon }, data.id)"
					:valid="fieldIsValid('telefon')" :max-len="20" />
				<svws-ui-text-input placeholder="Mobil/Fax" type="tel" :model-value="data.telefonMobil"
					@change="telefonMobil => patch({ telefonMobil }, data.id)" :valid="fieldIsValid('telefonMobil')" :max-len="20" />
				<svws-ui-text-input placeholder="E-Mail" type="email" :model-value="data.emailPrivat"
					@change="emailPrivat => patch({ emailPrivat }, data.id)" :valid="fieldIsValid('emailPrivat')" />
				<svws-ui-spacing />
				<svws-ui-select title="1. Staatsangehörigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" v-model="staatsangehoerigkeit"
					:valid="fieldIsValid('staatsangehoerigkeitID')" removable />
				<svws-ui-select title="2. Staatsangehörigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
					:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" v-model="staatsangehoerigkeit2"
					:valid="fieldIsValid('staatsangehoerigkeit2ID')" removable />
				<svws-ui-select title="Konfession" type="text" :items="mapReligionen" :item-text="i => i.bezeichnungZeugnis ?? ''" v-model="religion" removable />
				<svws-ui-spacing />
				<svws-ui-checkbox :model-value="data.hatMigrationshintergrund" @update:model-value="hatMigrationshintergrund => {
					data.hatMigrationshintergrund = hatMigrationshintergrund; patch({ hatMigrationshintergrund }, data.id) }"
					type="checkbox" title="Migrationshintergrund">
					Migrationshintergrund vorhanden
				</svws-ui-checkbox>
				<svws-ui-input-number placeholder="Zuzugsjahr" :model-value="data.zuzugsjahr" @change="zuzugsjahr => patch({ zuzugsjahr }, data.id)"
					:disabled="!data.hatMigrationshintergrund" />
				<svws-ui-select title="Geburtsland" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" v-model="geburtsland"
					:valid="fieldIsValid('geburtsland')" :disabled="!data.hatMigrationshintergrund" removable />
				<svws-ui-spacing />
				<svws-ui-select title="Geburtsland Mutter" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" v-model="geburtslandMutter"
					:valid="fieldIsValid('geburtslandMutter')" :disabled="!data.hatMigrationshintergrund" removable />
				<svws-ui-select title="Geburtsland Vater" :items="Nationalitaeten.values()" :item-text="i => `${i.historie().getLast().bezeichnung} (${i.historie().getLast().iso3})`"
					:item-sort="nationalitaetenKatalogEintragSort" :item-filter="nationalitaetenKatalogEintragFilter" v-model="geburtslandVater"
					:valid="fieldIsValid('geburtslandVater')" :disabled="!data.hatMigrationshintergrund" removable />
				<svws-ui-select title="Verkehrssprache" :items="Verkehrssprache.values()" :item-text="i => `${i.historie().getLast().text} (${i.historie().getLast().iso3})`"
					v-model="verkehrsprache" :item-sort="verkehrsspracheKatalogEintragSort" :item-filter="verkehrsspracheKatalogEintragFilter"
					:disabled="!data.hatMigrationshintergrund" removable />
				<svws-ui-spacing />
				<svws-ui-select title="Fahrschüler" :items="mapFahrschuelerarten" :item-text="i => i.text ?? ''" v-model="fahrschuelerart" removable />
				<svws-ui-select title="Haltestelle" :items="mapHaltestellen" :item-text="i => i.bezeichnung ?? ''" v-model="haltestelle" removable />
				<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht" :model-value="data.religionabmeldung"
					@change="religionabmeldung => patch({ religionabmeldung }, data.id)" type="date" />
				<svws-ui-spacing />
				<svws-ui-select title="Ext. ID-Nr." v-model="externeSchulNr" :items="mapSchulen.values()"
					:item-text="i => i.kuerzel ?? i.schulnummerStatistik ?? i.kurzbezeichnung ?? i.name" removable />
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
					<svws-ui-select title="Erzieherart" v-model="idErzieherArt" :items="mapErzieherarten" :item-sort="erzieherArtSort"
						:item-text="i => i.bezeichnung ?? ''" />
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
					<svws-ui-select title="Staatsangehörigkeit" v-model="patchStaatsangehoerigkeit" :items="Nationalitaeten.values()" :item-text="i => i.historie().getLast().staatsangehoerigkeit"
						:item-sort="staatsangehoerigkeitKatalogEintragSort" :item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete />
					<svws-ui-text-input placeholder="Straße und Hausnummer" :model-value="strasseErzieher(erzieher)" @change="patchStrasseErzieher" type="text" />
					<svws-ui-select title="Wohnort" v-model="wohnort" :items="mapOrte" :item-filter="orte_filter" :item-sort="orte_sort"
						:item-text="i => `${i.plz} ${i.ortsname}`" autocomplete />
					<svws-ui-select title="Ortsteil" v-model="ortsteil" :items="ortsteile" :item-text="i => i.ortsteil ?? ''" :item-sort="ortsteilSort"
						:item-filter="ortsteilFilter" removable />
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
						<svws-ui-select title="Staatsangehörigkeit" v-model="zweiteErzStaatsangehoerigkeit" :items="Nationalitaeten.values()"
							:item-text="i => i.historie().getLast().staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
							:item-filter="staatsangehoerigkeitKatalogEintragFilter" autocomplete />
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
					<svws-ui-notification type="warning" v-if="mapTelefonArten.size === 0">
						Die Liste der Telefonarten ist leer, es sollte mindestens eine Telefonart unter Schule/Kataloge angelegt werden, damit zusätzliche Telefonnummern eine gültige Zuordnung haben.
					</svws-ui-notification>
					<div class="mt-7 flex flex-row gap-4 justify-end">
						<svws-ui-button type="secondary" @click="closeModalTelefonnummer">Abbrechen</svws-ui-button>
						<svws-ui-button @click="sendRequestTelefonnummer"
							:disabled="(selectedTelefonArt === null) || (mapTelefonArten.size === 0) || (newEntryTelefonnummer.telefonnummer === null) ||
								(newEntryTelefonnummer.telefonnummer.length === 0) || (!hatKompetenzUpdate)">
							Speichern
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
		</svws-ui-content-card>

		<svws-ui-content-card title="Vorschulentwicklung" class="col-span-full" v-if="schulenMitPrimaerstufe">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Name des Kindergartens" :items="mapKindergaerten" :item-text="i => i.bezeichnung" :model-value="mapKindergaerten.get(dataSchulbesuchsdaten.idKindergarten ?? -1)"
					@update:model-value="v => patchSchuelerKindergarten({ idKindergarten: v?.id ?? null }, data.id)" removable />
				<svws-ui-select title="Dauer des Kindergartenbesuchs" :items="Kindergartenbesuch.values()" :item-text="i => i.daten(schuljahr)?.text ?? '-'"
					v-model="dauerKindergarten" removable />
				<svws-ui-spacing />
				<svws-ui-checkbox title="Verpflichtung f. Sprachförderkurss" :model-value="dataSchulbesuchsdaten.verpflichtungSprachfoerderkurs"
					@update:model-value="verpflichtungSprachfoerderkurs => patchSchuelerKindergarten({ verpflichtungSprachfoerderkurs }, data.id)">
					Verpflichtung f. Sprachförderkurs
				</svws-ui-checkbox>
				<svws-ui-checkbox title="Teilnahme an Sprachförderkurs" :model-value="dataSchulbesuchsdaten.teilnahmeSprachfoerderkurs"
					@update:model-value="teilnahmeSprachfoerderkurs => patchSchuelerKindergarten({ teilnahmeSprachfoerderkurs }, data.id)">
					Teilnahme an Sprachförderkurs
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addSchuelerStammdaten" :disabled="((!formIsValid) || (data.id !== 0) || (!hatKompetenzUpdate))">Speichern</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
	<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
</template>

<script setup lang="ts">

	import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";
	import type { Haltestelle, KatalogEintrag, OrtKatalogEintrag, OrtsteilKatalogEintrag, ReligionEintrag, SchulEintrag, TelefonArt, Erzieherart } from "@core";
	import { AdressenUtils, ArrayList, Geschlecht, JavaString, Kindergartenbesuch, Nationalitaeten, SchuelerStammdaten, SchuelerStatus, SchuelerTelefon,
		Schulform, Verkehrssprache, SchuelerSchulbesuchsdaten, BenutzerKompetenz, ErzieherStammdaten } from "@core";
	import {nationalitaetenKatalogEintragFilter, nationalitaetenKatalogEintragSort, orte_filter, orte_sort, ortsteilSort, verkehrsspracheKatalogEintragSort,
		staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort, verkehrsspracheKatalogEintragFilter, erzieherArtSort, ortsteilFilter } from "~/utils/helfer";
	import { computed, ref, watch } from "vue";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<SchuelerNeuProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);

	const data = ref(new SchuelerStammdaten());
	const dataSchulbesuchsdaten = ref(new SchuelerSchulbesuchsdaten());
	const isLoading = ref<boolean>(false);

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, {immediate: false, deep: true});

	//TODO Schulform.GY aus dem Array entfernen
	const schulenMitPrimaerstufe = computed(() => {
		const erlaubteSchulformen = [ Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V, Schulform.GY];
		return erlaubteSchulformen.includes(props.schulform);
	});

	const wohnortID = computed<OrtKatalogEintrag | undefined>({
		get: () => {
			const id = data.value.wohnortID;
			return id === null ? undefined : props.mapOrte.get(id)
		},
		set: (value) => void props.patch({ wohnortID: value === undefined ? null : value.id }, data.value.id),
	});

	const ortsteile = computed<Array<OrtsteilKatalogEintrag>>(() => {
		const result : Array<OrtsteilKatalogEintrag> = [];
		for (const ortsteil of props.mapOrtsteile.values())
			if (ortsteil.ort_id === data.value.wohnortID)
				result.push(ortsteil);
		return result;
	});

	const ortsteilID = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => {
			const id = data.value.ortsteilID;
			return id === null ? undefined : props.mapOrtsteile.get(id)
		},
		set: (value) => void props.patch({ ortsteilID: value === undefined ? null : value.id }, data.value.id),
	});

	const geschlecht = computed<Geschlecht>({
		get: () => Geschlecht.fromValue(data.value.geschlecht) ?? Geschlecht.X,
		set: (value) => void props.patch({ geschlecht: value.id }, data.value.id),
	});

	const strasseSchueler = computed(() => AdressenUtils.combineStrasse(data.value.strassenname ?? "", data.value.hausnummer ?? "", data.value.hausnummerZusatz ?? ""))

	function patchStrasse(value: string | null) {
		if (value !== null) {
			const vals = AdressenUtils.splitStrasse(value);
			void props.patch({ strassenname: vals[0], hausnummer: vals[1], hausnummerZusatz: vals[2] }, data.value.id);
		}
	}

	const staatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeitID) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ staatsangehoerigkeitID: value.historie().getLast().iso3 }, data.value.id),
	});

	const staatsangehoerigkeit2 = computed<Nationalitaeten | null>({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeit2ID),
		set: (value) => void props.patch({ staatsangehoerigkeit2ID: value?.historie().getLast().iso3 ?? null }, data.value.id),
	});

	const religion = computed<ReligionEintrag | undefined>({
		get: () => {
			const id = data.value.religionID;
			return id === null ? undefined : props.mapReligionen.get(id)
		},
		set: (value) => void props.patch({ religionID: value === undefined ? null : value.id }, data.value.id),
	});

	const geburtsland = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.geburtsland) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ geburtsland: value.historie().getLast().iso3 }, data.value.id),
	});

	const geburtslandMutter = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.geburtslandMutter) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ geburtslandMutter: value.historie().getLast().iso3 }, data.value.id),
	});

	const geburtslandVater = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.geburtslandVater) || Nationalitaeten.getDEU(),
		set: (value) => void props.patch({ geburtslandVater: value.historie().getLast().iso3 }, data.value.id),
	});

	const verkehrsprache = computed<Verkehrssprache>({
		get: () => Verkehrssprache.getByIsoKuerzel(data.value.verkehrspracheFamilie) || Verkehrssprache.data().getWertBySchluesselOrException("de"),
		set: (value) => void props.patch({ verkehrspracheFamilie: value.historie().getLast().iso3 }, data.value.id),
	});

	const externeSchulNr = computed<SchulEintrag | undefined>({
		get: () => (data.value.externeSchulNr === null) ? undefined : (props.mapSchulen.get(data.value.externeSchulNr) || undefined),
		set: (value) => void props.patch({ externeSchulNr: value === undefined ? null : value.schulnummerStatistik }, data.value.id),
	});

	const fahrschuelerart = computed<KatalogEintrag | undefined>({
		get: () => {
			const id = data.value.fahrschuelerArtID;
			return id === null ? undefined : props.mapFahrschuelerarten.get(id)
		},
		set: (value) => void props.patch({ fahrschuelerArtID: value === undefined ? null : value.id }, data.value.id),
	});

	const haltestelle = computed<Haltestelle | undefined>({
		get: () => {
			const id = data.value.haltestelleID;
			return id === null ? undefined : props.mapHaltestellen.get(id)
		},
		set: (value) => void props.patch({ haltestelleID: value === undefined ? null : value.id }, data.value.id),
	});

	const dauerKindergarten = computed<Kindergartenbesuch | null>({
		get(): Kindergartenbesuch | null {
			if (dataSchulbesuchsdaten.value.idDauerKindergartenbesuch === null)
				return null;
			return Kindergartenbesuch.data().getWertByID(dataSchulbesuchsdaten.value.idDauerKindergartenbesuch);
		},
		set: (value) => void props.patchSchuelerKindergarten({ idDauerKindergartenbesuch: value?.daten(schuljahr.value)?.id ?? null }, data.value.id),

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
				case 'wohnortID':
					return (data.value.wohnortID === null) || (props.mapOrte.get(data.value.wohnortID) !== undefined);
				case 'ortsteilID':
					return (data.value.ortsteilID === null) || (props.mapOrtsteile.get(data.value.ortsteilID) !== undefined);
				case 'telefon':
					return phoneNumberIsValid(data.value.telefon, false, 20);
				case 'telefonMobil':
					return phoneNumberIsValid(data.value.telefon, false, 20);
				case 'emailPrivat':
					return stringIsValid(data.value.emailPrivat, false, 20);
				case 'staatsangehoerigkeitID':
					return (data.value.staatsangehoerigkeitID === null) || (Nationalitaeten.getByISO3(data.value.staatsangehoerigkeitID) !== null);
				case 'staatsangehoerigkeit2ID':
					return (data.value.staatsangehoerigkeit2ID === null) || (Nationalitaeten.getByISO3(data.value.staatsangehoerigkeit2ID) !== null);
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

	const formIsValid = computed(() => {
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof SchuelerStammdaten);
			const fieldValue = data.value[field as keyof SchuelerStammdaten] as string | null;
			return validateField(fieldValue);
		});
	});

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
		{ key: "idErzieherArt", label: "Art"},
		{ key: "name", label: "Name"},
		{ key: "eMail", label: "E-Mail"},
		{ key: "adresse", label: "Adresse"},
		{ key: "erhaeltAnschreiben", label: "Anschreiben", tooltip: "Erhält Anschreiben", fixedWidth: 3, align: "center"},
		{ key: "actions", label: "2. Person", tooltip: "Weiteres Elternteil hinzufügen", fixedWidth: 10, align: "center" },
	];

	const idErzieherArt = computed<Erzieherart | undefined>({
		get: () => ((erzieher.value === undefined) || (erzieher.value.idErzieherArt === null)) ? undefined : props.mapErzieherarten.get(erzieher.value.idErzieherArt),
		set: (value) => (erzieher.value !== undefined) && void props.patchSchuelerErziehereintrag({ idErzieherArt: (value === undefined) ? null : value.id }, erzieher.value.id),
	});

	const wohnort = computed<OrtKatalogEintrag | undefined>({
		get: () => ((erzieher.value === undefined) || (erzieher.value.wohnortID === null)) ? undefined : props.mapOrte.get(erzieher.value.wohnortID),
		set: (value) => (erzieher.value !== undefined) && void props.patchSchuelerErziehereintrag({ wohnortID: (value === undefined) ? null : value.id }, erzieher.value.id),
	});

	const patchStaatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(erzieher.value?.staatsangehoerigkeitID ?? null) || Nationalitaeten.getDEU(),
		set: (value) => void props.patchSchuelerErziehereintrag({ staatsangehoerigkeitID: value.historie().getLast().iso3 }, erzieher.value?.id ?? 0),
	});

	const zweiteErzStaatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(zweiterErz.value.staatsangehoerigkeitID) || Nationalitaeten.getDEU(),
		set: (value) => zweiterErz.value.staatsangehoerigkeitID = value.historie().getLast().iso3,
	});

	const ortsteil = computed<OrtsteilKatalogEintrag | undefined>({
		get: () => {
			const id = ersterErz.value.ortsteilID;
			return (id === null) ? undefined : props.mapOrtsteile.get(id)
		},
		set: (value) => ersterErz.value.ortsteilID = (value === undefined) ? null : value.id,
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

	// Patch-Modal um an der zweiten Postion in einem Eintrag einen Erziehungsberechtigten anzulegen
	async function openModalForPos2(item: ErzieherStammdaten) {
		resetErzieher();
		setModeErzieher(Mode.PATCH_POS2);
		openPatchPosModalErz();
		// die ID des Eintrags für den Patch an der zweiten Position
		ersterErz.value.id = item.id;
		zweiterErz.value.idErzieherArt = item.idErzieherArt ?? 0;
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

	const columnsTelefonnummer: DataTableColumn[] = [
		{ key: "idTelefonArt", label: "Ansprechpartner" },
		{ key: "telefonnummer", label: "Telefonnummern" },
		{ key: "bemerkung", label: "Bemerkung", span: 2 },
		{ key: "istGesperrt", label: "Gesperrt", span: 1, align: "right" },
	]

	const selectedTelefonArt = computed<TelefonArt | null>({
		get: () => props.mapTelefonArten.get(newEntryTelefonnummer.value.idTelefonArt) ?? null,
		set: (telefonArt) => newEntryTelefonnummer.value.idTelefonArt = (telefonArt !== null) ? telefonArt.id : 0,
	});

	enum Mode { ADD, PATCH, PATCH_POS2, DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);
	const showModalTelefonnummer = ref<boolean>(false);

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

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		resetTelefonnummer();
		closeModalTelefonnummer();
		resetErzieher()
		closeModalErzieher()
	}

	function getBezeichnungTelefonart(idTelefonArt: number): string {
		return props.mapTelefonArten.get(idTelefonArt)?.bezeichnung ?? "";
	}

	async function addSchuelerStammdaten() {
		if (isLoading.value)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, ...partialData } = data.value;

		const result = await props.addSchueler(partialData);
		isLoading.value = false;
		data.value.id = result.id;

		erzieher.value = undefined
		props.getListSchuelerErziehereintraege().clear();
		props.getListSchuelerTelefoneintraege().clear();
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.gotoDefaultView(null);
	}

</script>
