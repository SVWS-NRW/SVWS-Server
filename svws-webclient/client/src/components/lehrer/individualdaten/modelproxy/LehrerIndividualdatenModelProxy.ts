import { computed } from "vue";
import type { LehrerStammdaten, NationalitaetenKatalogEintrag, OrtKatalogEintrag, OrtsteilKatalogEintrag, ValidatorKontext } from "@core";
import { AdressenUtils,	Geschlecht,	Nationalitaeten, PersonalTyp, ValidatorLsdLehrerStammdatenGeburtsdatum, ValidatorLsgLehrerStammdatenGeschlecht,
	ValidatorLskLehrerStammdatenKuerzel, ValidatorLsnLehrerStammdatenNachname, ValidatorLssLehrerStammdatenStaatsangehoerigkeitID,
	ValidatorLsvLehrerStammdatenVorname } from "@core";
import type { LehrerListeManager, OrteState } from "@ui";
import { ModelProxy, StringPattern, ValidatorInputRequired, ValidatorStrasse, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { ValidatorLehrerIndividualdatenKuerzel } from "./ValidatorLehrerIndividualdatenKuerzel";
import { ValidatorLehrerIndividualdatenNachname } from "./ValidatorLehrerIndividualdatenNachname";
import { ValidatorLehrerIndividualdatenVorname } from "./ValidatorLehrerIndividualdatenVorname";
import { orteStateImpl } from "~/states/kataloge/OrteStateImpl";

/**
 * Der spezielle ModelProxy für die Lehrerstammdaten
 */
export class LehrerIndividualdatenModelProxy extends ModelProxy<LehrerStammdaten> {

	private readonly orteState: OrteState = orteStateImpl;

	protected readonly schuljahr: number;
	protected readonly manager: () => LehrerListeManager;

	/**
	 * Erstellt einen ModelProxy für das Core-DTO LehrerStammdaten.
	 *
	 * @param data               ein Lambda für den Zugriff auf die "Original"-Daten
	 * @param validatorKontext   der Validator-Kontext für die Nutzung in den ASD-Validatoren
	 * @param manager
	 * @param patch              ggf. die Methode zum Patchen der einzelnen Attribute, sofern das automatische Patchen
	 *                           bei Änderungen gewünscht ist
	 */
	constructor(data: () => LehrerStammdaten,
		validatorKontext: () => ValidatorKontext,
		manager: () => LehrerListeManager,
		patch?: (data: Partial<LehrerStammdaten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof LehrerStammdaten> =
			["istSichtbar", "istRelevantFuerStatistik", "personalTyp", "geschlecht", "idStaatsangehoerigkeit", "wohnortID", "ortsteilID"];
		super({ data, patch, listOfAutopatchProps });
		this.schuljahr = validatorKontext().getSchuljahr();
		this.manager = manager;

		// Kürzel
		this.addBlockingValidator(new ValidatorLehrerIndividualdatenKuerzel(() => this.proxy, () => this.manager().liste.list()), "kuerzel");
		this.addValidator(new ValidatorLskLehrerStammdatenKuerzel({ get: () => this.proxy.kuerzel }, validatorKontext()), "kuerzel");

		// Personal-Typ
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.personalTyp), "personalTyp");

		// Nachname
		this.addBlockingValidator(new ValidatorLehrerIndividualdatenNachname(() => this.proxy.nachname, validatorKontext), "nachname");
		this.addValidator(new ValidatorLsnLehrerStammdatenNachname({ get: () => this.proxy.nachname }, validatorKontext()), "nachname");

		// Vorname
		this.addBlockingValidator(new ValidatorLehrerIndividualdatenVorname(() => this.proxy.vorname), "vorname");
		this.addValidator(new ValidatorLsvLehrerStammdatenVorname({ get: () => this.proxy.vorname }, validatorKontext()), "vorname");

		// Geschlecht
		this.addBlockingValidator(new ValidatorInputRequired(() => this.geschlecht.value), "geschlecht");
		this.addValidator(new ValidatorLsgLehrerStammdatenGeschlecht({ get: () => this.proxy.geschlecht }, validatorKontext()), "geschlecht");

		// Geburtsdatum
		this.addValidator(new ValidatorLsdLehrerStammdatenGeburtsdatum({ get: () => this.proxy.geburtsdatum }, validatorKontext()), "geburtsdatum");

		// Staatsangehörigkeit
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.idStaatsangehoerigkeit), "idStaatsangehoerigkeit");
		this.addValidator(new ValidatorLssLehrerStammdatenStaatsangehoerigkeitID(
			{ get: () => this.proxy.idStaatsangehoerigkeit }, validatorKontext()), "idStaatsangehoerigkeit");

		// Akademischer Grad
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.titel, null, 20), "titel");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.titel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "titel");

		// Amtsbezeichnung
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.amtsbezeichnung, null, 15), "amtsbezeichnung");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.amtsbezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "amtsbezeichnung");

		// Straße
		this.addBlockingValidator(new ValidatorStrasse(() => this.adresse.value, 55, 10, 30), "strassenname", "hausnummer", "hausnummerZusatz");

		// Telefonnummer
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.telefon, null, 20), "telefon");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefon, StringPattern.IS_PHONE_NUMBER), "telefon");

		// Mobil oder Fax
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.telefonMobil, null, 20), "telefonMobil");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefonMobil, StringPattern.IS_PHONE_NUMBER), "telefonMobil");

		// Private E-Mail-Adresse
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.emailPrivat, null, 100), "emailPrivat");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.emailPrivat, StringPattern.IS_EMAIL), "emailPrivat");

		// Dienstliche E-Mail-Adresse
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.emailDienstlich, null, 100), "emailDienstlich");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.emailDienstlich, StringPattern.IS_EMAIL), "emailDienstlich");

		this.validate();
	}

	personalTyp = computed<PersonalTyp | null>({
		get: () => PersonalTyp.fromKuerzel(this.proxy.personalTyp) ?? null,
		set: (value) => this.proxy.personalTyp = value?.kuerzel ?? '',
	});

	geschlecht = computed<Geschlecht | null>({
		get: () => Geschlecht.fromValue(this.proxy.geschlecht) ?? null,
		set: (value) => this.proxy.geschlecht = value?.id ?? -1,
	});

	staatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: () => {
			const nationalitaet = Nationalitaeten.data().getWertByIDOrNull(this.proxy.idStaatsangehoerigkeit);
			return (nationalitaet === null) ? null : Nationalitaeten.data().getEintragBySchuljahrUndWert(this.schuljahr, nationalitaet);
		},
		set: (value: NationalitaetenKatalogEintrag | null) => this.proxy.idStaatsangehoerigkeit = value?.id ?? null,
	});

	wohnort = computed<OrtKatalogEintrag | null>({
		get: () => this.orteState.orte.byId.get(this.proxy.wohnortID ?? -1) ?? null,
		set: (val) => this.proxy.wohnortID = val?.id ?? null,
	});

	ortsteil = computed<OrtsteilKatalogEintrag | null>({
		get: () => this.orteState.ortsteile.byId.get(this.proxy.ortsteilID ?? -1) ?? null,
		set: (val) => this.proxy.ortsteilID = val?.id ?? null,
	});

	adresse = computed<string | null>({
		get: () => AdressenUtils.combineStrasse(this.proxy.strassenname, this.proxy.hausnummer, this.proxy.hausnummerZusatz),
		set: (adresse: string | null) => {
			const [strassenname, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(adresse);
			this.proxy.strassenname = strassenname;
			this.proxy.hausnummer = hausnummer;
			this.proxy.hausnummerZusatz = hausnummerZusatz;
		},
	});
}
