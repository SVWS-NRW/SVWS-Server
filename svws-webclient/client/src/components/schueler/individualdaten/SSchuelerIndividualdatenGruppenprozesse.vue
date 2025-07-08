<template>
	<div class="flex gap-4 pt-8 px-6 lg:px-9 3xl:px-12 4xl:px-20">
		<SvwsUiTooltip color="light" :show-arrow="false">
			<template #content>
				Änderungen übernehmen
			</template>
			<svws-ui-button :disabled="!props.pendingStateManager().pendingStateExists() || disableSave" @click="patchPendingStates" type="primary" size="big">
				Speichern
				<svws-ui-spinner :spinning="loading" />
			</svws-ui-button>
		</SvwsUiTooltip>
		<SvwsUiTooltip color="light" :show-arrow="false">
			<template #content>
				Änderungen zurücksetzen
			</template>
			<svws-ui-button :disabled="!props.pendingStateManager().pendingStateExists()" @click="pendingStateManager().resetPendingState()" type="danger"
				size="big">
				Zurücksetzen
			</svws-ui-button>
		</SvwsUiTooltip>
	</div>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Statusdaten" v-if="hatKompetenzAnsehen">
			<template #actions v-if="schulform === Schulform.BK || schulform === Schulform.SB">
				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager"
					attribute-name="istDuplikat" :nullable="false" class="self-center">
					<svws-ui-checkbox v-model="istDuplikat" :disabled="!hatKompetenzUpdate"
						:indeterminate="!pendingStateManager().isAttributePending('istDuplikat')">
						Ist Duplikat
					</svws-ui-checkbox>
				</ui-gruppenprozesse-wrapper>
			</template>

			<svws-ui-input-wrapper :grid="2">
				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="status" :nullable="false">
					<ui-select v-model="status" label="Status" :manager="statusSelectManager" statistics />
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper v-if="alleExtern" :pending-state-manager="pendingStateManager" attribute-name="externeSchulNr">
					<ui-select v-model="stammschuleExtern" label="Stammschule" :manager="stammschuleSelectManager" :readonly="!hatKompetenzUpdate" />
				</ui-gruppenprozesse-wrapper>
				<div v-else />

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="fahrschuelerArtID">
					<ui-select v-model="fahrschuelerArtID" label="Fahrschüler" :manager="fahrschuelerSelectManager" :readonly="!hatKompetenzUpdate" />
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="haltestelleID">
					<ui-select v-model="haltestelleID" label="Haltestelle" :manager="haltestelleSelectManager" :readonly="!hatKompetenzUpdate" />
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="aufnahmedatum">
					<svws-ui-text-input placeholder="Aufnahmedatum" :readonly="!hatKompetenzUpdate" :model-value="aufnahmedatum"
						@update:model-value="setAufnahmedatum" type="date" :removable="false" statistics />
				</ui-gruppenprozesse-wrapper>

				<svws-ui-spacing />

				<svws-ui-input-wrapper :grid="2" class="input-wrapper--checkboxes !gap-y-2">
					<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="istVolljaehrig"
						:nullable="false">
						<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="istVolljaehrig"
							:indeterminate="!pendingStateManager().isAttributePending('istVolljaehrig')">
							Volljährig
						</svws-ui-checkbox>
					</ui-gruppenprozesse-wrapper>

					<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="keineAuskunftAnDritte"
						:nullable="false" class="self-center">
						<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="keineAuskunftAnDritte"
							:indeterminate="!pendingStateManager().isAttributePending('keineAuskunftAnDritte')">
							Keine Auskunft an Dritte
						</svws-ui-checkbox>
					</ui-gruppenprozesse-wrapper>

					<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="istSchulpflichtErfuellt"
						:nullable="false" class="self-center">
						<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="istSchulpflichtErfuellt"
							:indeterminate="!pendingStateManager().isAttributePending('istSchulpflichtErfuellt')">
							Schulpflicht erfüllt
						</svws-ui-checkbox>
					</ui-gruppenprozesse-wrapper>

					<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="istBerufsschulpflichtErfuellt"
						:nullable="false" class="self-center">
						<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="istBerufsschulpflichtErfuellt"
							:indeterminate="!pendingStateManager().isAttributePending('istBerufsschulpflichtErfuellt')">
							Schulpflicht SII erfüllt
						</svws-ui-checkbox>
					</ui-gruppenprozesse-wrapper>

					<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="hatMasernimpfnachweis"
						:nullable="false" class="self-center">
						<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="hatMasernimpfnachweis"
							:indeterminate="!pendingStateManager().isAttributePending('hatMasernimpfnachweis')">
							Masern Impfnachweis
						</svws-ui-checkbox>
					</ui-gruppenprozesse-wrapper>

					<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="erhaeltSchuelerBAFOEG"
						:nullable="false" class="self-center">
						<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="erhaeltSchuelerBAFOEG"
							:indeterminate="!pendingStateManager().isAttributePending('erhaeltSchuelerBAFOEG')">
							BAFöG
						</svws-ui-checkbox>
					</ui-gruppenprozesse-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card title="Staatsangehörigkeit und Konfession" v-if="hatKompetenzAnsehen">
			<svws-ui-input-wrapper :grid="2">
				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="staatsangehoerigkeitID">
					<ui-select v-model="staatsangehoerigkeitID" label="1. Staatsangehörigkeit" :manager="ersteStaatsAngehoerigkeitSelectManager" statistics />
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="staatsangehoerigkeit2ID">
					<ui-select v-model="staatsangehoerigkeit2ID" label="2. Staatsangehörigkeit"	:manager="zweiteStaatsAngehoerigkeitSelectManager" statistics />
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="religionID">
					<ui-select v-model="konfession" label="Konfession" :manager="konfessionSelectManager" :readonly="!hatKompetenzUpdate"
						statistics />
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="druckeKonfessionAufZeugnisse"
					:nullable="false" class="self-center">
					<svws-ui-checkbox v-model="druckeKonfessionAufZeugnisse" :disabled="!hatKompetenzUpdate"
						:indeterminate="!pendingStateManager().isAttributePending('druckeKonfessionAufZeugnisse')">
						Konfession aufs Zeugnis
					</svws-ui-checkbox>
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="religionabmeldung">
					<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht" :readonly="!hatKompetenzUpdate" :model-value="religionabmeldung"
						@update:model-value="setReligionabmeldung" type="date" :removable="false" statistics />
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="religionanmeldung">
					<svws-ui-text-input placeholder="Wiederanmeldung" :readonly="!hatKompetenzUpdate" :model-value="religionanmeldung"
						@update:model-value="setReligionanmeldung" type="date" :removable="false" statistics />
				</ui-gruppenprozesse-wrapper>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>

		<svws-ui-content-card title="Migrationshintergrund" v-if="hatKompetenzAnsehen" id="Card-Migrationshintergrund">
			<template #actions>
				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="hatMigrationshintergrund"
					:nullable="false" class="self-center">
					<svws-ui-checkbox @update:model-value="(hatMigrationsHintergrund) => {!hatMigrationsHintergrund && resetMigrationsHintergrundInputs()}"
						:disabled="!hatKompetenzUpdate" v-model="hatMigrationshintergrund" statistics focus-class-content
						:indeterminate="!pendingStateManager().isAttributePending('hatMigrationshintergrund')">
						Migrationshintergrund vorhanden
					</svws-ui-checkbox>
				</ui-gruppenprozesse-wrapper>
			</template>

			<svws-ui-input-wrapper :grid="2">
				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="zuzugsjahr" :nullable="hatMigrationshintergrund">
					<svws-ui-input-number placeholder="Zuzugsjahr" :model-value="zuzugsjahr" @update:model-value="setZuzugsjahr"
						:disabled="!hatMigrationshintergrund" statistics hide-stepper :readonly="hatMigrationshintergrund && !hatKompetenzUpdate"
						:min="minZuzugsjahr" :max="maxZuzugsjahr" />
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="geburtsland" :nullable="hatMigrationshintergrund">
					<ui-select v-model="geburtsland" label="Geburtsland" :manager="geburtslandSelectManager" :disabled="!hatMigrationshintergrund"
						:readonly="hatMigrationshintergrund && !hatKompetenzUpdate" searchable statistics />
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="verkehrspracheFamilie"
					:nullable="hatMigrationshintergrund">
					<ui-select v-model="verkehrssprache" label="Verkehrssprache" :manager="verkehrsspracheSelectManager"
						:disabled="!hatMigrationshintergrund" :readonly="hatMigrationshintergrund && !hatKompetenzUpdate"
						searchable statistics />
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="geburtslandMutter"
					:nullable="hatMigrationshintergrund">
					<ui-select v-model="geburtslandMutter" label="Geburtsland Mutter" :manager="geburtslandMutterSelectManager"
						:disabled="!hatMigrationshintergrund" :readonly="hatMigrationshintergrund && !hatKompetenzUpdate" searchable
						statistics />
				</ui-gruppenprozesse-wrapper>

				<ui-gruppenprozesse-wrapper :pending-state-manager="pendingStateManager" attribute-name="geburtslandVater" :nullable="hatMigrationshintergrund">
					<ui-select v-model="geburtslandVater" label="Geburtsland Vater" :manager="geburtslandVaterSelectManager"
						:disabled="!hatMigrationshintergrund" :readonly="hatMigrationshintergrund && !hatKompetenzUpdate" searchable statistics />
				</ui-gruppenprozesse-wrapper>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
	<svws-ui-checkpoint-modal :checkpoint :continue-routing="continueRoutingAfterCheckpointCustom" />
</template>

<script setup lang="ts">

	import { BenutzerKompetenz, Fahrschuelerart, Nationalitaeten, SchuelerStatus, Schulform, Verkehrssprache } from "@core";
	import type { SchuelerIndividualdatenGruppenprozesseProps } from "~/components/schueler/individualdaten/SSchuelerIndividualdatenGruppenprozesseProps";
	import { computed, ref, watch, toRefs } from "vue";
	import { CoreTypeSelectManager } from "@ui";
	import { BaseSelectManager } from "@ui";

	const props = defineProps<SchuelerIndividualdatenGruppenprozesseProps>();

	const { pendingStateManager } = toRefs(props);

	const status = pendingStateManager.value().status;
	const staatsangehoerigkeitID = pendingStateManager.value().staatsangehoerigkeitID;
	const staatsangehoerigkeit2ID = pendingStateManager.value().staatsangehoerigkeit2ID;
	const konfession = pendingStateManager.value().konfession;
	const druckeKonfessionAufZeugnisse = pendingStateManager.value().druckeKonfessionAufZeugnisse;
	const istDuplikat = pendingStateManager.value().istDuplikat;
	const alleExtern = pendingStateManager.value().alleExtern;
	const stammschuleExtern = pendingStateManager.value().stammschuleExtern;
	const aufnahmedatum = pendingStateManager.value().aufnahmedatum;
	const religionanmeldung = pendingStateManager.value().religionanmeldung;
	const religionabmeldung = pendingStateManager.value().religionabmeldung;
	const fahrschuelerArtID = pendingStateManager.value().fahrschuelerArtID;
	const haltestelleID = pendingStateManager.value().haltestelleID;
	const hatMigrationshintergrund = pendingStateManager.value().hatMigrationshintergrund;
	const geburtsland = pendingStateManager.value().geburtsland;
	const geburtslandMutter = pendingStateManager.value().geburtslandMutter;
	const geburtslandVater = pendingStateManager.value().geburtslandVater;
	const verkehrssprache = pendingStateManager.value().verkehrssprache;
	const zuzugsjahr = pendingStateManager.value().zuzugsjahr;
	const erhaeltSchuelerBAFOEG = pendingStateManager.value().erhaeltSchuelerBAFOEG;
	const hatMasernimpfnachweis = pendingStateManager.value().hatMasernimpfnachweis;
	const istBerufsschulpflichtErfuellt = pendingStateManager.value().istBerufsschulpflichtErfuellt;
	const istSchulpflichtErfuellt = pendingStateManager.value().istSchulpflichtErfuellt;
	const istVolljaehrig = pendingStateManager.value().istVolljaehrig;
	const keineAuskunftAnDritte = pendingStateManager.value().keineAuskunftAnDritte;

	const loading = ref<boolean>(false);

	const hatKompetenzAnsehen = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN));
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const schuljahr = computed(() => props.schuelerListeManager().getSchuljahr());
	const schulform = computed(() => props.schuelerListeManager().schulform());
	const religionen = computed(() => props.mapReligionen.values());
	const fahrschuelerArten = computed(() => props.mapFahrschuelerarten.values());
	const haltestellen = computed(() => props.mapHaltestellen.values());
	const schulen = computed(() => props.mapSchulen.values());

	watch(() => props.pendingStateManager().pendingStateExists(), (somethingPending: boolean) => {
		props.checkpoint.active = somethingPending;
	}, { immediate: false, deep: true });

	watch(
		() => schulform.value,
		(newValue) => {
			ersteStaatsAngehoerigkeitSelectManager.schulformen = newValue;
			zweiteStaatsAngehoerigkeitSelectManager.schulformen = newValue;
			statusSelectManager.schulformen = newValue;
			geburtslandSelectManager.schulformen = newValue;
			geburtslandMutterSelectManager.schulformen = newValue;
			geburtslandVaterSelectManager.schulformen = newValue;
			verkehrsspracheSelectManager.schulformen = newValue;
		}
	);

	watch(
		() => schuljahr.value,
		(newValue) => {
			ersteStaatsAngehoerigkeitSelectManager.schuljahr = newValue;
			zweiteStaatsAngehoerigkeitSelectManager.schuljahr = newValue;
			statusSelectManager.schuljahr = newValue;
			geburtslandSelectManager.schuljahr = newValue;
			geburtslandMutterSelectManager.schuljahr = newValue;
			geburtslandVaterSelectManager.schuljahr = newValue;
			verkehrsspracheSelectManager.schuljahr = newValue;
		}
	);

	watch(
		() => religionen.value,
		(newValue) => {
			konfessionSelectManager.options = newValue;
		}
	);

	watch(
		() => fahrschuelerArten.value,
		(newValue) => {
			fahrschuelerSelectManager.options = newValue;
		}
	);

	watch(
		() => haltestellen.value,
		(newValue) => {
			haltestelleSelectManager.options = newValue;
		}
	);

	watch(
		() => schulen.value,
		(newValue) => {
			stammschuleSelectManager.options = newValue;
		}
	);

	const ersteStaatsAngehoerigkeitSelectManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class, schuljahr: schuljahr.value,
		schulformen: schulform.value, removable: false,
	});
	const zweiteStaatsAngehoerigkeitSelectManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class, schuljahr: schuljahr.value,
		schulformen: schulform.value, removable: false,
	});
	const statusSelectManager = new CoreTypeSelectManager({
		clazz: SchuelerStatus.class, schuljahr: schuljahr.value, schulformen: schulform.value,
		removable: false,
	});
	const konfessionSelectManager = new BaseSelectManager({
		options: religionen.value, removable: false,
		optionDisplayText: selected => selected.bezeichnung ?? '', selectionDisplayText: selected => selected.bezeichnung ?? '',
	});
	const fahrschuelerSelectManager = new BaseSelectManager({
		options: fahrschuelerArten.value, removable: false,
		optionDisplayText: (selected: Fahrschuelerart) => selected.bezeichnung ?? '', selectionDisplayText: selected => selected.bezeichnung ?? '',
	});
	const haltestelleSelectManager = new BaseSelectManager({
		options: haltestellen.value, removable: false, optionDisplayText: selected => selected.bezeichnung ?? '',
		selectionDisplayText: selected => selected.bezeichnung ?? '',
	});
	const stammschuleSelectManager = new BaseSelectManager({
		options: schulen.value,
		optionDisplayText: selected => selected.kuerzel ?? selected.schulnummerStatistik ?? selected.kurzbezeichnung ?? selected.name,
		selectionDisplayText: selected => selected.kuerzel ?? selected.schulnummerStatistik ?? selected.kurzbezeichnung ?? selected.name,
	});
	const geburtslandSelectManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class, schuljahr: schuljahr.value, schulformen: schulform.value,
		removable: false,
	});
	const geburtslandMutterSelectManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class, schuljahr: schuljahr.value, schulformen: schulform.value,
		removable: false,
	});
	const geburtslandVaterSelectManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class, schuljahr: schuljahr.value, schulformen: schulform.value,
		removable: false,
	});
	const verkehrsspracheSelectManager = new CoreTypeSelectManager({
		clazz: Verkehrssprache.class, schuljahr: schuljahr.value, schulformen: schulform.value,
		removable: false,
	});

	function setReligionabmeldung(value: string | null) {
		if (value !== null)
			religionabmeldung.value = value;
	}

	function setReligionanmeldung(value: string | null) {
		if (value !== null)
			religionanmeldung.value = value;
	}

	function setAufnahmedatum(value: string | null) {
		if (value !== null)
			aufnahmedatum.value = value;
	}

	const minZuzugsjahr = new Date().getFullYear() + 1 - 100;
	const maxZuzugsjahr = new Date().getFullYear() + 1;

	const disableSave = ref<boolean>(false);

	const setZuzugsjahr = (value: number | null) => {
		disableSave.value = ((value !== null) && ((value < Number(minZuzugsjahr)) || (value > Number(maxZuzugsjahr))));

		if ((value !== null) && !disableSave.value)
			zuzugsjahr.value = value;
		else
			props.pendingStateManager().removePendingState("zuzugsjahr");
	};

	async function continueRoutingAfterCheckpointCustom() {
		props.pendingStateManager().resetPendingState();
		await props.continueRoutingAfterCheckpoint();
	}

	async function patchPendingStates() {
		loading.value = true;
		await props.patchMultiple();
		loading.value = false;
	}

	function resetMigrationsHintergrundInputs() {
		geburtsland.value = null;
		props.pendingStateManager().removePendingState('zuzugsjahr');
		geburtsland.value = null;
		props.pendingStateManager().removePendingState('geburtsland');
		verkehrssprache.value = null;
		props.pendingStateManager().removePendingState('verkehrspracheFamilie');
		geburtslandMutter.value = null;
		props.pendingStateManager().removePendingState('geburtslandMutter');
		geburtslandVater.value = null;
		props.pendingStateManager().removePendingState('geburtslandVater');
	}

</script>
