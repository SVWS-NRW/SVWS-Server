<template>
	<div class="page page-grid-cards">
		<schueler-schnelleingabe-anmeldedaten :manager
			:stammdaten-model
			:schulbesuch-model
			:lernabschnitt-model
			:schulen-mit-primaerstufe
			:readonly />
		<schueler-schnelleingabe-schuelerdaten :stammdaten-model
			:lernabschnitt-model
			:readonly />
		<schueler-erziehungsberechtigte-wrapper :data="getErzieher"
			:patch-erzieher
			:patch-erzieher-an-position
			:add-erzieher
			:delete-erzieher />
		<schueler-telefonnummern :id-schueler="stammdatenModel.proxy.id"
			class="col-span-full"
			:readonly />
		<schueler-schulbesuch-kindergarten v-if="schulenMitPrimaerstufe"
			class="col-span-full"
			:model="schulbesuchModel"
			:readonly />
		<schueler-schnelleingabe-vermerke :manager
			:get-vermerke
			:add-vermerk
			:patch-vermerk
			:delete-vermerke
			:readonly="!hatKompetenzVermerke" />
		<svws-ui-content-card />
		<svws-ui-content-card class="col-span-full">
			<div class="-mt-16 flex flex-row gap-4 justify-end w-full">
				<svws-ui-button type="primary" @click="schuelerAuswahlState.navigator.gotoHinzufuegen(true)">Weiteren Schüler anlegen</svws-ui-button>
				<svws-ui-button type="secondary" @click="cancel">Neuaufnahme beenden</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import { SchuelerLernabschnittsdaten } from "@core/asd/data/schueler/SchuelerLernabschnittsdaten";
	import { SchuelerSchulbesuchsdaten } from "@core/asd/data/schueler/SchuelerSchulbesuchsdaten";
	import { SchuelerStammdaten } from "@core/asd/data/schueler/SchuelerStammdaten";
	import { Schulform } from "@core/asd/types/schule/Schulform";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";

	import { SchuelerIndividualdatenModelProxy } from "../individualdaten/modelproxy/SchuelerIndividualdatenModelProxy";
	import { SchuelerLernabschnittAllgemeinModelProxy } from "../lernabschnitte/allgemein/modelproxy/SchuelerLernabschnittAllgemeinModelProxy";
	import { SchuelerSchulbesuchModelProxy } from "../schulbesuch/modelProxy/SchuelerSchulbesuchModelProxy";
	import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";

	import type { SchuelerSchnelleingabeProps } from "./SchuelerSchnelleingabeProps";

	const props = defineProps<SchuelerSchnelleingabeProps>();
	const benutzerState = useBenutzerState();
	const abschnittState = useAbschnittState();
	const schuleState = useSchuleState();
	const schuelerAuswahlState = useSchuelerAuswahlState();

	const hatKompetenzVermerke = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_VERMERKE_AENDERN));
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const readonly = computed(() => !hatKompetenzUpdate.value);
	const schuljahr = computed<number>(() => abschnittState.auswahl.schuljahr);

	/** Die Schnelleingabe zeigt nicht alle Felder der wiederverwendeten ModelProxies an. Die Validierung soll nur für die hier
	 * aufgeführten, angezeigten Felder stattfinden. Für alle übrigen Felder wird sie deaktiviert, damit sie weder Fehler anzeigen
	 * noch das Speichern blockieren - das gilt auch für Validatoren, die erst später hinzukommen. Wird ein Feld in der Schnelleingabe
	 * ergänzt, muss es hier ergänzt werden. */
	const validierteStammdaten: Array<keyof SchuelerStammdaten> = [
		"status", "anmeldedatum", "aufnahmedatum", "beginnBildungsgang", "dauerBildungsgang",
		"nachname", "vorname", "alleVornamen", "geschlecht", "strassenname", "hausnummer", "hausnummerZusatz", "wohnortID", "ortsteilID",
		"geburtsdatum", "geburtsort", "telefon", "telefonMobil", "emailPrivat", "idStaatsangehoerigkeit", "idStaatsangehoerigkeit2",
		"religionID", "religionabmeldung", "hatMigrationshintergrund", "zuzugsjahr", "idGeburtsland", "idGeburtslandMutter",
		"idGeburtslandVater", "idVerkehrspracheFamilie", "fahrschuelerArtID", "haltestelleID", "externeSchulNr", "idSchuelerausweis",
	];

	const validierteSchulbesuchsdaten: Array<keyof SchuelerSchulbesuchsdaten> = [
		"idEinschulungsartGrundschule", "idKindergarten", "idDauerKindergartenbesuch", "verpflichtungSprachfoerderkurs", "teilnahmeSprachfoerderkurs",
	];

	// Klasse und Jahrgang sind in der Schnelleingabe kein Pflichtfeld und werden daher nicht validiert
	const validierteLernabschnittsdaten: Array<keyof SchuelerLernabschnittsdaten> = [
		"hatSchwerbehinderungsNachweis",
	];

	/**
	 * Berechnet die Props, deren Validierung deaktiviert werden soll: alle Props des DTOs außer den übergebenen.
	 *
	 * @param dto        ein neu erzeugtes DTO, dessen Attribute alle initialisiert sind
	 * @param validate   die Props, die validiert werden sollen
	 *
	 * @returns die Props, bei denen die Validierung deaktiviert wird
	 */
	function getDeactivatedPropValidations<T extends object>(dto: T, validate: Array<keyof T>): Array<keyof T> {
		const validiertSet = new Set<keyof T>(validate);
		return (Object.keys(dto) as Array<keyof T>).filter(prop => !validiertSet.has(prop));
	}

	const disabledValidationStammdaten = getDeactivatedPropValidations(new SchuelerStammdaten(), validierteStammdaten);
	const disabledValidationSchulbesuchsdaten = getDeactivatedPropValidations(new SchuelerSchulbesuchsdaten(), validierteSchulbesuchsdaten);
	const disabledValidationLernabschnittsdaten = getDeactivatedPropValidations(new SchuelerLernabschnittsdaten(), validierteLernabschnittsdaten);

	const stammdatenModel = new SchuelerIndividualdatenModelProxy(
		() => schuelerAuswahlState.stammdaten.datenOrException(),
		() => schuleState.validatorKontext,
		() => schuljahr.value,
		(data) => schuelerAuswahlState.stammdaten.patch(data),
		disabledValidationStammdaten
	);

	const schulbesuchModel = new SchuelerSchulbesuchModelProxy(
		() => props.manager().schulbesuchsdaten,
		() => schuljahr.value,
		(data) => props.patchSchulbesuchsdaten(data, stammdatenModel.proxy.id),
		disabledValidationSchulbesuchsdaten
	);

	const lernabschnittModel = new SchuelerLernabschnittAllgemeinModelProxy(
		() => props.manager().lernabschnittsdaten,
		() => [],
		() => [...props.manager().klassenAktuell],
		() => [],
		() => [],
		(data) => props.patchLernabschnittsdaten(data, props.manager().lernabschnittsdaten.id),
		disabledValidationLernabschnittsdaten
	);

	const schulenMitPrimaerstufe = computed(() => {
		const erlaubteSchulformen = [Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V];
		return erlaubteSchulformen.includes(schuleState.schulform);
	});

	async function cancel() {
		await schuelerAuswahlState.navigator.select(props.manager().stammdaten.id);
	}

</script>
