<template>
	<div v-if="!hatKompetenzUpdate" class="pt-8 px-6 lg:px-9 3xl:px-12 4xl:px-20">
		Für die Nutzung der Gruppenprozesse fehlen Benutzerkompetenzen.
	</div>
	<template v-else>
		<gruppenprozesse-aktionen :gruppenprozesse="schuelerAuswahlState.gruppenprozesse" />
		<div class="page page-grid-cards">
			<svws-ui-content-card title="Statusdaten" v-if="hatKompetenzAnsehen">
				<template #actions v-if="schulform === Schulform.BK || schulform === Schulform.SB">
					<ui-gruppenprozesse-wrapper :model
						attribute-name="istDuplikat" :nullable="false" class="self-center">
						<svws-ui-checkbox v-model="istDuplikat" :disabled="!hatKompetenzUpdate"
							:indeterminate="!model.isPending('istDuplikat')">
							Ist Duplikat
						</svws-ui-checkbox>
					</ui-gruppenprozesse-wrapper>
				</template>
				<svws-ui-input-wrapper :grid="2">
					<ui-gruppenprozesse-wrapper :model attribute-name="status" :nullable="false">
						<ui-select v-model="status" label="Status" :manager="statusSelectManager" statistics :removable="false" />
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper v-if="alleExtern" :model attribute-name="externeSchulNr">
						<ui-select v-model="stammschuleExtern" label="Stammschule" :manager="stammschuleSelectManager" :readonly="!hatKompetenzUpdate" />
					</ui-gruppenprozesse-wrapper>
					<div v-else />
					<ui-gruppenprozesse-wrapper :model attribute-name="fahrschuelerArtID">
						<ui-select v-model="fahrschuelerArtID" label="Fahrschüler" :manager="fahrschuelerSelectManager" :readonly="!hatKompetenzUpdate" />
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper :model attribute-name="haltestelleID">
						<ui-select v-model="haltestelleID" label="Haltestelle" :manager="haltestelleSelectManager" :readonly="!hatKompetenzUpdate"
							:removable="false" />
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper :model attribute-name="aufnahmedatum">
						<svws-ui-text-input placeholder="Aufnahmedatum" :readonly="!hatKompetenzUpdate" :model-value="aufnahmedatum"
							@update:model-value="setAufnahmedatum" type="date" statistics />
					</ui-gruppenprozesse-wrapper>
					<svws-ui-spacing />
					<svws-ui-input-wrapper :grid="2" class="input-wrapper--checkboxes gap-y-2!">
						<ui-gruppenprozesse-wrapper :model attribute-name="istVolljaehrig"
							:nullable="false">
							<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="istVolljaehrig"
								:indeterminate="!model.isPending('istVolljaehrig')">
								Volljährig
							</svws-ui-checkbox>
						</ui-gruppenprozesse-wrapper>
						<ui-gruppenprozesse-wrapper :model attribute-name="keineAuskunftAnDritte"
							:nullable="false" class="self-center">
							<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="keineAuskunftAnDritte"
								:indeterminate="!model.isPending('keineAuskunftAnDritte')">
								Keine Auskunft an Dritte
							</svws-ui-checkbox>
						</ui-gruppenprozesse-wrapper>
						<ui-gruppenprozesse-wrapper :model attribute-name="istSchulpflichtErfuellt"
							:nullable="false" class="self-center">
							<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="istSchulpflichtErfuellt"
								:indeterminate="!model.isPending('istSchulpflichtErfuellt')">
								Schulpflicht erfüllt
							</svws-ui-checkbox>
						</ui-gruppenprozesse-wrapper>
						<ui-gruppenprozesse-wrapper :model attribute-name="istBerufsschulpflichtErfuellt"
							:nullable="false" class="self-center">
							<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="istBerufsschulpflichtErfuellt"
								:indeterminate="!model.isPending('istBerufsschulpflichtErfuellt')">
								Schulpflicht SII erfüllt
							</svws-ui-checkbox>
						</ui-gruppenprozesse-wrapper>
						<ui-gruppenprozesse-wrapper :model attribute-name="hatMasernimpfnachweis"
							:nullable="false" class="self-center">
							<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="hatMasernimpfnachweis"
								:indeterminate="!model.isPending('hatMasernimpfnachweis')">
								Masern-Schutznachweis
							</svws-ui-checkbox>
						</ui-gruppenprozesse-wrapper>
						<ui-gruppenprozesse-wrapper :model attribute-name="erhaeltSchuelerBAFOEG"
							:nullable="false" class="self-center">
							<svws-ui-checkbox :disabled="!hatKompetenzUpdate" v-model="erhaeltSchuelerBAFOEG"
								:indeterminate="!model.isPending('erhaeltSchuelerBAFOEG')">
								BAFöG
							</svws-ui-checkbox>
						</ui-gruppenprozesse-wrapper>
					</svws-ui-input-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Staatsangehörigkeit und Konfession" v-if="hatKompetenzAnsehen">
				<svws-ui-input-wrapper :grid="2">
					<ui-gruppenprozesse-wrapper :model attribute-name="idStaatsangehoerigkeit">
						<ui-select v-model="staatsangehoerigkeitID" label="1. Staatsangehörigkeit" :manager="ersteStaatsAngehoerigkeitSelectManager" statistics
							:removable="false" />
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper :model attribute-name="idStaatsangehoerigkeit2">
						<ui-select v-model="staatsangehoerigkeit2ID" label="2. Staatsangehörigkeit"	:manager="zweiteStaatsAngehoerigkeitSelectManager" statistics
							:removable="false" />
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper :model attribute-name="religionID">
						<ui-select v-model="konfession" label="Konfession" :manager="konfessionSelectManager" :readonly="!hatKompetenzUpdate"
							statistics :removable="false" />
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper :model attribute-name="druckeKonfessionAufZeugnisse"
						:nullable="false" class="self-center">
						<svws-ui-checkbox v-model="druckeKonfessionAufZeugnisse" :disabled="!hatKompetenzUpdate"
							:indeterminate="!model.isPending('druckeKonfessionAufZeugnisse')">
							Konfession aufs Zeugnis
						</svws-ui-checkbox>
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper :model attribute-name="religionabmeldung">
						<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht" :readonly="!hatKompetenzUpdate" :model-value="religionabmeldung"
							@update:model-value="setReligionabmeldung" type="date" statistics />
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper :model attribute-name="religionanmeldung">
						<svws-ui-text-input placeholder="Wiederanmeldung" :readonly="!hatKompetenzUpdate" :model-value="religionanmeldung"
							@update:model-value="setReligionanmeldung" type="date" statistics />
					</ui-gruppenprozesse-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Migrationshintergrund" v-if="hatKompetenzAnsehen" id="Card-Migrationshintergrund">
				<template #actions>
					<ui-gruppenprozesse-wrapper :model attribute-name="hatMigrationshintergrund"
						:nullable="false" class="self-center">
						<svws-ui-checkbox @update:model-value="(hatMigrationsHintergrund) => {!hatMigrationsHintergrund && bereich.resetMigrationshintergrund()}"
							:disabled="!hatKompetenzUpdate" v-model="hatMigrationshintergrund" statistics focus-class-content
							:indeterminate="!model.isPending('hatMigrationshintergrund')">
							Migrationshintergrund vorhanden
						</svws-ui-checkbox>
					</ui-gruppenprozesse-wrapper>
				</template>
				<svws-ui-input-wrapper :grid="2">
					<ui-gruppenprozesse-wrapper :model attribute-name="zuzugsjahr" :nullable="hatMigrationshintergrund">
						<svws-ui-input-number placeholder="Zuzugsjahr" :model-value="zuzugsjahr" @update:model-value="(value: number | null) => bereich.setZuzugsjahr(value)"
							:disabled="!hatMigrationshintergrund" statistics :steps="false" :readonly="hatMigrationshintergrund && !hatKompetenzUpdate"
							:min="bereich.minZuzugsjahr" :max="bereich.maxZuzugsjahr" />
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper :model attribute-name="idGeburtsland" :nullable="hatMigrationshintergrund">
						<ui-select v-model="idGeburtsland" label="Geburtsland" :manager="geburtslandSelectManager" :disabled="!hatMigrationshintergrund"
							:readonly="hatMigrationshintergrund && !hatKompetenzUpdate" :removable="false" statistics />
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper :model attribute-name="idVerkehrspracheFamilie"
						:nullable="hatMigrationshintergrund">
						<ui-select v-model="verkehrssprache" label="Verkehrssprache" :manager="verkehrsspracheSelectManager"
							:disabled="!hatMigrationshintergrund" :readonly="hatMigrationshintergrund && !hatKompetenzUpdate" :removable="false"
							searchable statistics />
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper :model attribute-name="idGeburtslandMutter"
						:nullable="hatMigrationshintergrund">
						<ui-select v-model="geburtslandMutter" label="Geburtsland Mutter" :manager="geburtslandMutterSelectManager"
							:disabled="!hatMigrationshintergrund" :readonly="hatMigrationshintergrund && !hatKompetenzUpdate" :removable="false" searchable
							statistics />
					</ui-gruppenprozesse-wrapper>
					<ui-gruppenprozesse-wrapper :model attribute-name="idGeburtslandVater" :nullable="hatMigrationshintergrund">
						<ui-select v-model="geburtslandVater" label="Geburtsland Vater" :manager="geburtslandVaterSelectManager"
							:disabled="!hatMigrationshintergrund" :readonly="hatMigrationshintergrund && !hatKompetenzUpdate" :removable="false"
							searchable statistics />
					</ui-gruppenprozesse-wrapper>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
		</div>
	</template>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
	import { Nationalitaeten } from "@core/asd/types/schule/Nationalitaeten";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { Verkehrssprache } from "@core/asd/types/schule/Verkehrssprache";
	import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useFahrschuelerartenState } from "@ui/states/kataloge/FahrschuelerartenState";
	import { useHaltestellenState } from "@ui/states/kataloge/HaltestellenState";
	import { useHerkunftschulenState } from "@ui/states/kataloge/HerkunftschulenState";
	import { useReligionenState } from "@ui/states/kataloge/ReligionenState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	import GruppenprozesseAktionen from "~/components/gruppenprozesse/GruppenprozesseAktionen.vue";
	import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";
	import { SchuelerStammdatenBereich } from "~/states/schueler/SchuelerStammdatenBereich";

	const schuleState = useSchuleState();
	const fahrschuelerartenState = useFahrschuelerartenState();
	const benutzerState = useBenutzerState();
	const schuelerAuswahlState = useSchuelerAuswahlState();
	const religionenState = useReligionenState();
	const haltestellenState = useHaltestellenState();
	const herkunftschulenState = useHerkunftschulenState();

	/** Der Bereich der Stammdaten wird von der Route beim Betreten des Tabs angemeldet, bevor die Komponente aufgebaut wird */
	const bereich = schuelerAuswahlState.gruppenprozesse.bereichOrException(SchuelerStammdatenBereich.SCHLUESSEL);
	const model = bereich.model;

	// Die Bindungen werden auf oberster Ebene benötigt, damit sie im Template automatisch entpackt werden
	const { status, staatsangehoerigkeitID, staatsangehoerigkeit2ID, konfession, druckeKonfessionAufZeugnisse, istDuplikat, alleExtern, stammschuleExtern,
		aufnahmedatum, religionanmeldung, religionabmeldung, fahrschuelerArtID, haltestelleID, hatMigrationshintergrund, idGeburtsland, geburtslandMutter,
		geburtslandVater, verkehrssprache, zuzugsjahr, erhaeltSchuelerBAFOEG, hatMasernimpfnachweis, istBerufsschulpflichtErfuellt, istSchulpflichtErfuellt,
		istVolljaehrig, keineAuskunftAnDritte } = model;

	const hatKompetenzAnsehen = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN));
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const schuljahr = computed(() => schuleState.schuljahr);
	const schulform = computed(() => schuleState.schulform);
	const schulen = computed(() => herkunftschulenState.herkunftschulen.bySchulnummerStatistikFilteredByEigeneSchulform.values());


	/** Erzeugt einen Select-Manager für die Nationalitäten, jedes Feld benötigt einen eigenen Manager */
	const createNationalitaetenSelectManager = () => new CoreTypeSelectManager({
		clazz: Nationalitaeten.class, schuljahr: schuljahr, schulformen: schulform,
	});

	const ersteStaatsAngehoerigkeitSelectManager = createNationalitaetenSelectManager();

	const zweiteStaatsAngehoerigkeitSelectManager = createNationalitaetenSelectManager();

	const statusSelectManager = new CoreTypeSelectManager({
		clazz: SchuelerStatus.class, schuljahr: schuljahr, schulformen: schulform,
	});

	const konfessionSelectManager = new SelectManager({
		options: computed(() => religionenState.religionen.list),
		optionDisplayText: selected => selected.bezeichnung,
		selectionDisplayText: selected => selected.bezeichnung,
	});

	const fahrschuelerSelectManager = new SelectManager({
		options: computed(() => fahrschuelerartenState.fahrschuelerarten.byId.values()),
		optionDisplayText: selected => selected.bezeichnung ?? '',
		selectionDisplayText: selected => selected.bezeichnung ?? '',
	});

	const haltestelleSelectManager = new SelectManager({
		options: computed(() => haltestellenState.haltestellen.list),
		optionDisplayText: selected => selected.bezeichnung ?? '',
		selectionDisplayText: selected => selected.bezeichnung ?? '',
	});

	const stammschuleSelectManager = new SelectManager({
		options: schulen,
		selectionDisplayText: getSchulnummerText,
		optionDisplayText: getSchulnummerText,
	});

	function getSchulnummerText(eintrag: SchulEintrag | null): string {
		const text = `${eintrag?.schulnummerStatistik ?? ''} ${eintrag?.kuerzel ?? eintrag?.kurzbezeichnung ?? ''}`;
		return text.length > 0 ? text : 'Fehlende Angaben';
	}

	const geburtslandSelectManager = createNationalitaetenSelectManager();

	const geburtslandMutterSelectManager = createNationalitaetenSelectManager();

	const geburtslandVaterSelectManager = createNationalitaetenSelectManager();

	const verkehrsspracheSelectManager = new CoreTypeSelectManager({
		clazz: Verkehrssprache.class, schuljahr: schuljahr, schulformen: schulform,
	});

	/** Erzeugt die Behandlung der Eingabe eines Datumsfeldes, bei der das Löschen des Datums ignoriert wird */
	const ignoriereNull = (attribut: WritableComputedRef<string | null>) => (value: string | null) => {
		if (value !== null) {
			attribut.value = value;
		}
	};

	const setAufnahmedatum = ignoriereNull(aufnahmedatum);
	const setReligionanmeldung = ignoriereNull(religionanmeldung);
	const setReligionabmeldung = ignoriereNull(religionabmeldung);

</script>
