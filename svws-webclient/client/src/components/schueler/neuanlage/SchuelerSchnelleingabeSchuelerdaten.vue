<template>
	<svws-ui-content-card title="Persönliche Daten" class="col-span-full">
		<svws-ui-input-wrapper :grid="4">
			<svws-ui-text-input placeholder="Nachname"
				:model-value="manager().stammdaten.nachname"
				@change="patchNachname"
				:valid="nachnameIsValid"
				:min-len="1" :max-len="120" :readonly required />
			<svws-ui-text-input placeholder="Rufname"
				:model-value="manager().stammdaten.vorname"
				@change="patchVorname"
				:valid="vornameIsValid"
				:min-len="1" :max-len="80" :readonly required />
			<svws-ui-text-input placeholder="Alle Vornamen"
				:model-value="manager().stammdaten.alleVornamen"
				@change="patchAlleVornamen"
				:valid="alleVornamenIsValid"
				:max-len="255" :readonly />
			<ui-select label="Geschlecht"
				v-model="geschlecht"
				:manager="geschlechtManager"
				:removable="false" :readonly required />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Straße"
				:model-value="strasse"
				@change="patchStrasse"
				:valid="adresseIsValid"
				:max-len="50" :readonly />
			<ui-select label="Wohnort"
				v-model="wohnort"
				:manager="wohnortManager"
				searchable :readonly />
			<ui-select label="Ortsteil"
				v-model="ortsteil"
				:manager="ortsteilManager"
				searchable :readonly />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Geburtsdatum" type="date"
				:model-value="manager().stammdaten.geburtsdatum"
				@change="patchGeburtsdatum"
				:valid="geburtsdatumIsValid"
				required :readonly />
			<svws-ui-text-input placeholder="Geburtsort"
				:model-value="manager().stammdaten.geburtsort"
				@change="patchGeburtsort"
				:valid="geburtsortIsValid"
				:max-len="100" :readonly />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Telefon" type="tel"
				:model-value="manager().stammdaten.telefon"
				@change="patchTelefon"
				:valid="telefonIsValid"
				:max-len="20" :readonly />
			<svws-ui-text-input placeholder="Mobil/Fax" type="tel"
				:model-value="manager().stammdaten.telefonMobil"
				@change="patchTelefonMobil"
				:valid="telefonIsValid"
				:max-len="20" :readonly />
			<svws-ui-text-input placeholder="E-Mail" type="email"
				:model-value="manager().stammdaten.emailPrivat"
				@change="patchEmailPrivat"
				:valid="emailPrivatIsValid"
				:max-len="100" :readonly />
			<svws-ui-spacing />
			<ui-select label="1. Staatsangehörigkeit"
				v-model="staatsangehoerigkeit"
				:manager="staatsangehoerigkeitenManager"
				searchable :readonly />
			<ui-select label="2. Staatsangehörigkeit"
				v-model="staatsangehoerigkeit2"
				:manager="staatsangehoerigkeitenManager"
				searchable :readonly />
			<ui-select label="Konfession"
				v-model="religion"
				:manager="religionManager"
				:removable="false" :readonly />
			<svws-ui-text-input placeholder="Abmeldung vom Religionsunterricht"
				:model-value="manager().stammdaten.religionabmeldung"
				@change="patchReligionAbmeldung"
				type="date" :readonly />
			<svws-ui-checkbox v-model="hatMigrationshintergrund" :readonly>
				Migrationshintergrund vorhanden
			</svws-ui-checkbox>
			<svws-ui-input-number placeholder="Zuzugsjahr"
				v-model="zuzugsjahr"
				:valid="zuzugsjahrIsValid"
				:disabled="(!manager().stammdaten.hatMigrationshintergrund || readonly)" />
			<ui-select label="Geburtsland"
				v-model="geburtsland"
				:manager="geburtslandManager"
				:disabled="(!manager().stammdaten.hatMigrationshintergrund || readonly)" :removable="false" />
			<svws-ui-spacing />
			<ui-select label="Geburtsland Mutter"
				v-model="geburtslandMutter"
				:manager="geburtslandManager"
				:disabled="(!manager().stammdaten.hatMigrationshintergrund || readonly)" :removable="false" />
			<ui-select label="Geburtsland Vater"
				v-model="geburtslandVater"
				:manager="geburtslandManager"
				:disabled="(!manager().stammdaten.hatMigrationshintergrund || readonly)" :removable="false" />
			<ui-select label="Verkehrssprache"
				v-model="verkehrssprache"
				:manager="verkehrsspracheManager"
				:disabled="(!manager().stammdaten.hatMigrationshintergrund || readonly)" :removable="false" />
			<svws-ui-spacing />
			<ui-select label="Fahrschüler"
				v-model="fahrschuelerart"
				:manager="fahrschuelerartManager"
				:removable="false" :readonly />
			<ui-select label="Haltestelle"
				v-model="haltestelle"
				:manager="haltestellenManager"
				:removable="false" :readonly />
			<svws-ui-spacing />
			<ui-select label="Ext. ID-Nr."
				v-model="externeSchulNr"
				:manager="externeIDNrManager"
				:removable="false" :readonly />
			<svws-ui-text-input placeholder="Schülerausweis-Nummer"
				:model-value="manager().stammdaten.idSchuelerausweis"
				@change="patchIdSchuelerausweis"
				:max-len="30" :readonly />
			<svws-ui-checkbox v-model="schwerbehinderung">
				Schwerstbehinderung
			</svws-ui-checkbox>
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import type { NationalitaetenKatalogEintrag, SchuelerLernabschnittsdaten, OrtsteilKatalogEintrag, SchuelerStammdaten, VerkehrsspracheKatalogEintrag } from "@core";
	import { AdressenUtils, DateUtils, Geschlecht, Nationalitaeten, Verkehrssprache } from "@core";
	import { CoreTypeSelectManager, SelectManager, useAbschnittState, useOrteState } from "@ui";
	import type { SchuelerSchnelleingabeManager } from "@ui";
	import { orte_sort, ortsteilSort } from "~/utils/helfer";
	import { emailIsValid, mandatoryInputIsValid, numberIsValid, optionalInputIsValid, phoneNumberIsValid } from "~/util/validation/Validation";

	const props = defineProps<{
		manager: () => SchuelerSchnelleingabeManager;
		patchSchueler: (patchObject: Partial<SchuelerStammdaten>, id: number) => Promise<void>;
		patchLernabschnittsdaten: (data: Partial<SchuelerLernabschnittsdaten>, idEintrag: number) => Promise<void>;
		readonly: boolean;
	}>();

	const abschnittState = useAbschnittState();
	const orteState = useOrteState();

	const manager = () => props.manager();
	const religionen = computed(() => props.manager().religionenById.values());
	const externeSchulnummern = computed(() => props.manager().schulenById.values());
	const haltestellen = computed(() => props.manager().haltestellenById.values());
	const fahrschuelerarten = computed(() => props.manager().fahrschuelerartenById.values());

	const geschlecht = computed<Geschlecht | null>({
		get: () => Geschlecht.fromValue(manager().stammdaten.geschlecht),
		set: (value: Geschlecht | null) => {
			manager().stammdaten.geschlecht = value?.id ?? -1;
			void props.patchSchueler({ geschlecht: value?.id }, manager().stammdaten.id);
		},
	});

	const strasse = computed(() => AdressenUtils.combineStrasse(manager().stammdaten.strassenname ?? "",
		manager().stammdaten.hausnummer ?? "", manager().stammdaten.hausnummerZusatz ?? ""));

	const wohnort = computed({
		get: () => orteState.orte.byId.get(props.manager().stammdaten.wohnortID ?? -1) ?? null,
		set: (value) => {
			props.manager().stammdaten.wohnortID = value?.id ?? -1;
			void props.patchSchueler({ wohnortID: value?.id ?? null }, manager().stammdaten.id);
		},
	});

	const ortsteil = computed<OrtsteilKatalogEintrag | null>({
		get: () => orteState.ortsteile.byId.get(props.manager().stammdaten.ortsteilID ?? -1) ?? null,
		set: (value: OrtsteilKatalogEintrag | null) => {
			props.manager().stammdaten.ortsteilID = value?.id ?? null;
			void props.patchSchueler({ ortsteilID: value?.id ?? null }, manager().stammdaten.id);
		},
	});

	const staatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.data().getWertByIDOrNull(props.manager().stammdaten.idStaatsangehoerigkeit)?.daten(abschnittState.auswahl.schuljahr) ?? null,
		set: (value) => {
			props.manager().stammdaten.idStaatsangehoerigkeit = value?.id ?? null;
			void props.patchSchueler({ idStaatsangehoerigkeit: value?.id ?? null }, manager().stammdaten.id);
		},
	});

	const staatsangehoerigkeit2 = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.data().getWertByIDOrNull(props.manager().stammdaten.idStaatsangehoerigkeit2)?.daten(abschnittState.auswahl.schuljahr) ?? null,
		set: (value) => {
			props.manager().stammdaten.idStaatsangehoerigkeit2 = value?.id ?? null;
			void props.patchSchueler({ idStaatsangehoerigkeit2: value?.id ?? null }, manager().stammdaten.id);
		},
	});

	const religion = computed({
		get: () => props.manager().religionenById.get(props.manager().stammdaten.religionID ?? -1),
		set: (value) => {
			props.manager().stammdaten.religionID = value?.id ?? -1;
			void props.patchSchueler({ religionID: value?.id ?? null }, manager().stammdaten.id);
		},
	});

	const hatMigrationshintergrund = computed<boolean>({
		get: () => manager().stammdaten.hatMigrationshintergrund,
		set: (hatMigrationshintergrund: boolean) => {
			manager().stammdaten.hatMigrationshintergrund = hatMigrationshintergrund;
			return void props.patchSchueler({ hatMigrationshintergrund }, manager().stammdaten.id);
		},
	});

	const zuzugsjahr = computed<number | null>({
		get: () => manager().stammdaten.zuzugsjahr,
		set: (value: number | null) => patchZuzugsjahr(value),
	});

	const geburtsland = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.data().getWertByIDOrNull(manager().stammdaten.idGeburtsland)?.daten(abschnittState.auswahl.schuljahr) ?? null,
		set: (value) => {
			manager().stammdaten.idGeburtsland = value?.id ?? null;
			void props.patchSchueler({ idGeburtsland: value?.id }, manager().stammdaten.id);
		},
	});

	const geburtslandMutter = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.data().getWertByIDOrNull(props.manager().stammdaten.idGeburtslandMutter)?.daten(abschnittState.auswahl.schuljahr) ?? null,
		set: (value) => {
			props.manager().stammdaten.idGeburtslandMutter = value?.id ?? null;
			void props.patchSchueler({ idGeburtslandMutter: value?.id }, manager().stammdaten.id);
		},
	});

	const geburtslandVater = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.data().getWertByIDOrNull(props.manager().stammdaten.idGeburtslandVater)?.daten(abschnittState.auswahl.schuljahr) ?? null,
		set: (value) => {
			props.manager().stammdaten.idGeburtslandVater = value?.id ?? null;
			void props.patchSchueler({ idGeburtslandVater: value?.id }, manager().stammdaten.id);
		},
	});

	const verkehrssprache = computed<VerkehrsspracheKatalogEintrag | null>({
		get: () => Verkehrssprache.data().getWertByIDOrNull(props.manager().stammdaten.idVerkehrspracheFamilie)?.daten(abschnittState.auswahl.schuljahr) ?? null,
		set: (value) => {
			props.manager().stammdaten.idVerkehrspracheFamilie = value?.id ?? null;
			void props.patchSchueler({ idVerkehrspracheFamilie: value?.id }, manager().stammdaten.id);
		},
	});

	const fahrschuelerart = computed({
		get: () => props.manager().fahrschuelerartenById.get(props.manager().stammdaten.fahrschuelerArtID ?? -1) ?? null,
		set: (value) => {
			const id = value?.id ?? null;
			props.manager().stammdaten.fahrschuelerArtID = id;
			void props.patchSchueler({ fahrschuelerArtID: id }, manager().stammdaten.id);
		},
	});

	const haltestelle = computed({
		get: () => props.manager().haltestellenById.get(props.manager().stammdaten.haltestelleID ?? -1) ?? null,
		set: (value) => {
			const id = value?.id ?? null;
			props.manager().stammdaten.haltestelleID = id;
			void props.patchSchueler({ haltestelleID: id }, manager().stammdaten.id);
		},
	});
	const externeSchulNr = computed({
		get: () => props.manager().schulenByExterneSchulnummer.get(props.manager().stammdaten.externeSchulNr ?? "") ?? null,
		set: (value) => {
			props.manager().stammdaten.externeSchulNr = value?.schulnummerStatistik ?? null;
			void props.patchSchueler({ externeSchulNr: value?.schulnummerStatistik }, manager().stammdaten.id);
		},
	});

	const schwerbehinderung = computed<boolean>({
		get: () => manager().lernabschnittsdaten.hatSchwerbehinderungsNachweis,
		set: (hatSchwerbehinderungsNachweis) => void props.patchLernabschnittsdaten({ hatSchwerbehinderungsNachweis }, manager().lernabschnittsdaten.id),
	});

	// --- manager ---

	const geschlechtManager = new SelectManager({
		options: Geschlecht.values(),
		optionDisplayText: i => i.text,
		selectionDisplayText: i => i.text,
	});

	const wohnortManager = new SelectManager({
		options: computed(() => orteState.orte.list),
		optionDisplayText: i => `${i.plz} ${i.ortsname}`,
		sort: orte_sort,
		selectionDisplayText: i => `${i.plz} ${i.ortsname}`,
	});

	const ortsteilManager = new SelectManager({
		options: computed(() => orteState.ortsteile.listByOrtId(props.manager().stammdaten.wohnortID)),
		sort: ortsteilSort,
		optionDisplayText: i => i.ortsteil ?? '',
		selectionDisplayText: i => i.ortsteil ?? '',
	});

	const staatsangehoerigkeitenManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const religionManager = new SelectManager({
		options: religionen,
		optionDisplayText: i => i.bezeichnungZeugnis ?? '',
		selectionDisplayText: i => i.bezeichnungZeugnis ?? '',
	});

	const geburtslandManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const verkehrsspracheManager = new CoreTypeSelectManager({
		clazz: Verkehrssprache.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const fahrschuelerartManager = new SelectManager({
		options: fahrschuelerarten,
		optionDisplayText: i => i.bezeichnung ?? '',
		selectionDisplayText: i => i.bezeichnung ?? '',
	});

	const haltestellenManager = new SelectManager({
		options: haltestellen,
		optionDisplayText: i => i.bezeichnung ?? '',
		selectionDisplayText: i => i.bezeichnung ?? '',
	});

	const externeIDNrManager = new SelectManager({
		options: externeSchulnummern, optionDisplayText: i => i.kuerzel ?? i.schulnummerStatistik ?? i.kurzbezeichnung ?? i.name,
		selectionDisplayText: i => i.kuerzel ?? i.schulnummerStatistik ?? i.kurzbezeichnung ?? i.name,
	});

	// --- validate ---

	function nachnameIsValid(nachname: string | null): nachname is string {
		return mandatoryInputIsValid(nachname, 120);
	}

	function vornameIsValid(vorname: string | null): vorname is string {
		return mandatoryInputIsValid(vorname, 80);
	}

	function alleVornamenIsValid(alleVornamen: string | null) {
		return optionalInputIsValid(alleVornamen, 255);
	}

	function adresseIsValid(v: string | null) {
		const [strasse, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(v);
		return optionalInputIsValid(strasse, 50)
			&& optionalInputIsValid(hausnummer, 10)
			&& optionalInputIsValid(hausnummerZusatz, 30);
	}

	function geburtsdatumIsValid(strDate: string | null) {
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

	function geburtsortIsValid(value: string | null) {
		return optionalInputIsValid(value, 100);
	}

	function telefonIsValid(value: string | null) {
		return phoneNumberIsValid(value, 20);
	}

	function emailPrivatIsValid(value: string | null) {
		return emailIsValid(value, 100);
	}

	function idSchuelerausweisIsValid(value: string | null) {
		return optionalInputIsValid(value, 30);
	}

	function zuzugsjahrIsValid(value: number | null) {
		return numberIsValid(value, false, 1900, 2999);
	}

	// --- patch ---

	function patchNachname(nachname: string | null) {
		if (nachnameIsValid(nachname)) {
			void props.patchSchueler({ nachname }, manager().stammdaten.id);
		}
	}

	function patchVorname(vorname: string | null) {
		if (vornameIsValid(vorname)) {
			void props.patchSchueler({ vorname }, manager().stammdaten.id);
		}
	}

	function patchAlleVornamen(alleVornamen: string | null) {
		if (alleVornamenIsValid(alleVornamen)) {
			void props.patchSchueler({ alleVornamen: alleVornamen ?? "" }, manager().stammdaten.id);
		}
	}

	async function patchStrasse(v: string | null) {
		if (adresseIsValid(v)) {
			const [strassenname, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(v);
			await props.patchSchueler({ strassenname, hausnummer, hausnummerZusatz }, manager().stammdaten.id);
		}
	}

	function patchGeburtsdatum(geburtsdatum: string | null) {
		if (geburtsdatumIsValid(geburtsdatum)) {
			void props.patchSchueler({ geburtsdatum }, manager().stammdaten.id);
		}
	}

	function patchGeburtsort(geburtsort: string | null) {
		if (geburtsortIsValid(geburtsort)) {
			void props.patchSchueler({ geburtsort }, manager().stammdaten.id);
		}
	}

	function patchTelefon(telefon: string | null) {
		if (telefonIsValid(telefon)) {
			void props.patchSchueler({ telefon }, manager().stammdaten.id);
		}
	}

	function patchTelefonMobil(telefonMobil: string | null) {
		if (telefonIsValid(telefonMobil)) {
			void props.patchSchueler({ telefonMobil }, manager().stammdaten.id);
		}
	}

	function patchEmailPrivat(emailPrivat: string | null) {
		if (emailPrivatIsValid(emailPrivat)) {
			void props.patchSchueler({ emailPrivat }, manager().stammdaten.id);
		}
	}

	function patchReligionAbmeldung(religionabmeldung: string | null) {
		return void props.patchSchueler({ religionabmeldung }, manager().stammdaten.id);
	}

	function patchZuzugsjahr(zuzugsjahr: number | null) {
		if (zuzugsjahrIsValid(zuzugsjahr)) {
			void props.patchSchueler({ zuzugsjahr }, manager().stammdaten.id);
		}
	}

	function patchIdSchuelerausweis(idSchuelerausweis: string | null) {
		if (idSchuelerausweisIsValid(idSchuelerausweis)) {
			void props.patchSchueler({ idSchuelerausweis }, manager().stammdaten.id);
		}
	}

</script>
