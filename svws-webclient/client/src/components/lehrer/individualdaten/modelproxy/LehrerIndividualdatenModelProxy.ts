import type { LehrerStammdaten, NationalitaetenKatalogEintrag, OrtKatalogEintrag, OrtsteilKatalogEintrag, ValidatorKontext } from "@core";
import { AdressenUtils, Geschlecht, Nationalitaeten, PersonalTyp, ValidatorLsdLehrerStammdatenGeburtsdatum } from "@core";
import type { LehrerListeManager } from "@ui";
import { ModelProxy, StringPattern, ValidatorInputRequired, ValidatorStrasse, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { computed } from "vue";
import { ValidatorLehrerIndividualdatenKuerzel } from "~/components/lehrer/individualdaten/modelproxy/ValidatorLehrerIndividualdatenKuerzel";
import { ValidatorLehrerIndividualdatenNachname } from "~/components/lehrer/individualdaten/modelproxy/ValidatorLehrerIndividualdatenNachname";
import { ValidatorLehrerIndividualdatenVorname } from "~/components/lehrer/individualdaten/modelproxy/ValidatorLehrerIndividualdatenVorname";

/**
 * Der spezielle ModelProxy für die Lehrerstammdaten
 */
export class LehrerIndividualdatenModelProxy extends ModelProxy<LehrerStammdaten> {

	protected readonly schuljahr: number;
	protected readonly manager: () => LehrerListeManager;

	protected readonly orteById: Map<number, OrtKatalogEintrag>;
	protected readonly ortsteileById: Map<number, OrtsteilKatalogEintrag>;

	/**
	 * Erstellt einen ModelProxy für das Core-DTO LehrerIndividualdaten.
	 *
	 * @param data               ein Lambda für den Zugriff auf die "Original"-Daten
	 * @param validatorKontext   der Validator-Kontext für die Nutzung in den ASD-Validatoren
	 * @param manager
	 * @param orteById
	 * @param ortsteileById
	 * @param patch              ggf. die Methode zum Patchen der einzelnen Attribute, sofern das automatische Patchen
	 *                           bei Änderungen gewünscht ist
	 */
	constructor(data: () => LehrerStammdaten, validatorKontext: () => ValidatorKontext, manager: () => LehrerListeManager, orteById: Map<number, OrtKatalogEintrag>, ortsteileById: Map<number, OrtsteilKatalogEintrag>, patch?: (data: Partial<LehrerStammdaten>) => Promise<boolean>) {
		const listOfAutopatchProps: Iterable<keyof LehrerStammdaten> = ["istSichtbar", "istRelevantFuerStatistik", "personalTyp", "geschlecht",
			"idStaatsangehoerigkeit", "wohnortID", "ortsteilID"];
		super({ data, patch, checkValidBeforePatch: true, listOfAutopatchProps });
		this.schuljahr = validatorKontext().getSchuljahr();
		this.manager = manager;
		this.orteById = orteById;
		this.ortsteileById = ortsteileById;


		// Kürzel
		this.addValidator(new ValidatorLehrerIndividualdatenKuerzel(() => this.proxy, () => this.manager().liste.list()), "kuerzel");

		// Personal-Typ
		this.addValidator(new ValidatorInputRequired(() => this.proxy.personalTyp), "personalTyp");

		// Nachname
		this.addValidator(new ValidatorLehrerIndividualdatenNachname(() => this.proxy.nachname, validatorKontext), "nachname");

		// Vorname
		this.addValidator(new ValidatorLehrerIndividualdatenVorname(() => this.proxy.vorname, validatorKontext), "vorname");

		// Geschlecht
		this.addValidator(new ValidatorInputRequired(() => this.proxy.geschlecht), "geschlecht");

		// Geburtsdatum
		this.addValidator(new ValidatorLsdLehrerStammdatenGeburtsdatum({ get: () => this.proxy.geburtsdatum }, validatorKontext()), "geburtsdatum");

		// Staatsangehörigkeit
		this.addValidator(new ValidatorInputRequired(() => this.proxy.idStaatsangehoerigkeit), "idStaatsangehoerigkeit");

		// Akademischer Grad
		this.addValidator(new ValidatorStringLength(() => this.proxy.titel, null, 20), "titel");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.titel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "titel");

		// Amtsbezeichnung
		this.addValidator(new ValidatorStringLength(() => this.proxy.amtsbezeichnung, null, 15), "amtsbezeichnung");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.amtsbezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "amtsbezeichnung");

		// Straße
		this.addValidator(new ValidatorStrasse(() => this.adresse.value, 55, 10, 30), "strassenname", "hausnummer", "hausnummerZusatz");

		// Telefonnummer
		this.addValidator(new ValidatorStringLength(() => this.proxy.telefon, null, 20), "telefon");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefon, StringPattern.IS_PHONE_NUMBER), "telefon");

		// Mobil oder Fax
		this.addValidator(new ValidatorStringLength(() => this.proxy.telefonMobil, null, 20), "telefonMobil");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefonMobil, StringPattern.IS_PHONE_NUMBER), "telefonMobil");

		// Private E-Mail-Adresse
		this.addValidator(new ValidatorStringLength(() => this.proxy.emailPrivat, null, 100), "emailPrivat");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.emailPrivat, StringPattern.IS_EMAIL), "emailPrivat");

		// Dienstliche E-Mail-Adresse
		this.addValidator(new ValidatorStringLength(() => this.proxy.emailDienstlich, null, 100), "emailDienstlich");
		this.addValidator(new ValidatorStringMatchesPattern(() => this.proxy.emailDienstlich, StringPattern.IS_EMAIL), "emailDienstlich");

		this.validate();
	}

	selectedPersonalTyp = computed<PersonalTyp>({
		get: () => PersonalTyp.fromKuerzel(this.proxy.personalTyp) ?? PersonalTyp.SONSTIGE,
		set: (value) => this.proxy.personalTyp = value.kuerzel,
	});

	selectedGeschlecht = computed<Geschlecht>({
		get: () => Geschlecht.fromValue(this.proxy.geschlecht) ?? Geschlecht.X,
		set: (value) => this.proxy.geschlecht = value.id,
	});

	selectedStaatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: () => {
			const wert = Nationalitaeten.data().getWertByIDOrNull(this.proxy.idStaatsangehoerigkeit) ?? Nationalitaeten.getDEU();
			return Nationalitaeten.data().getEintragBySchuljahrUndWert(this.schuljahr, wert);
		},
		set: (value: NationalitaetenKatalogEintrag | null) => this.proxy.idStaatsangehoerigkeit = value?.id ?? null,
	});

	selectedWohnort = computed<OrtKatalogEintrag | null>({
		get: () => this.orteById.get(this.proxy.wohnortID ?? -1) ?? null,
		set: (val) => this.proxy.wohnortID = val?.id ?? null,
	});

	selectedOrtsteil = computed<OrtsteilKatalogEintrag | null>({
		get: () => this.ortsteileById.get(this.proxy.ortsteilID ?? -1) ?? null,
		set: (val) => this.proxy.ortsteilID = val?.id ?? null,
	});

	adresse = computed({
		get: () => AdressenUtils.combineStrasse(this.proxy.strassenname, this.proxy.hausnummer, this.proxy.hausnummerZusatz),
		set: (adresse: string | null) => {
			const [strassenname, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(adresse);
			this.proxy.strassenname = strassenname;
			this.proxy.hausnummer = hausnummer;
			this.proxy.hausnummerZusatz = hausnummerZusatz;
		},
	});
}
