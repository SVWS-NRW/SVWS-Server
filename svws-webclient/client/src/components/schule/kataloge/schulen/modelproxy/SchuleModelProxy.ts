import { ModelProxy, StringPattern, ValidatorInputRequired, ValidatorNumberRange, ValidatorStringLength, ValidatorStringMatchesPattern,
	ValidatorStrasse } from "@ui";
import type { HerkunftSchulformKatalogEintrag, SchulEintrag } from "@core";
import { HerkunftSchulform } from "@core";
import { AdressenUtils, Schulform } from "@core";
import { ValidatorSchuleKuerzel } from "~/components/schule/kataloge/schulen/modelproxy/validation/ValidatorSchuleKuerzel";
import { ValidatorSchuleKurzbezeichnung } from "~/components/schule/kataloge/schulen/modelproxy/validation/ValidatorSchuleKurzbezeichnung";
import { ValidatorSchuleSchulname } from "~/components/schule/kataloge/schulen/modelproxy/validation/ValidatorSchuleSchulname";
import { computed } from "vue";

export class SchuleModelProxy extends ModelProxy<SchulEintrag> {

	/**
	 * ModelProxy für Schule
	 *
	 * @param data Lambda für den Zugriff auf die Original-Daten
	 * @param alleSchulen Lambda zur Liste aller Schulen
	 * @param patch Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => SchulEintrag,
		alleSchulen: () => Iterable<SchulEintrag>,
		patch?: (data: Partial<SchulEintrag>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof SchulEintrag> = ["schulnummerStatistik", "idSchulform", "istSichtbar"];
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren(alleSchulen);
		this.validate();
	}

	private addValidatoren(alleSchulen: () => Iterable<SchulEintrag>) {
		this.addBlockingValidator(new ValidatorSchuleKuerzel((): SchulEintrag => this.proxy, alleSchulen), 'kuerzel');

		this.addBlockingValidator(new ValidatorSchuleKurzbezeichnung((): SchulEintrag => this.proxy), 'kurzbezeichnung');

		this.addBlockingValidator(new ValidatorSchuleSchulname((): SchulEintrag => this.proxy), 'name');

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.schulleiter, null, 40), 'schulleiter');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.schulleiter, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'schulleiter');

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.plz, null, 10), 'plz');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.plz, StringPattern.NO_WHITESPACES), 'plz');

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.ort, null, 50), 'ort');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.ort, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'ort');

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.telefon, null, 20), 'telefon');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefon, StringPattern.IS_PHONE_NUMBER), 'telefon');

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.fax, null, 20), 'fax');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.fax, StringPattern.IS_PHONE_NUMBER), 'fax');

		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.email, null, 40), 'email');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.email, StringPattern.IS_EMAIL), 'email');

		this.addBlockingValidator(new ValidatorInputRequired((): number => this.proxy.sortierung), 'sortierung');
		this.addBlockingValidator(new ValidatorNumberRange((): number => this.proxy.sortierung, 0, 32000), "sortierung");

		this.addBlockingValidator(new ValidatorInputRequired((): number | null => this.proxy.idSchulform), 'idSchulform');

		this.addBlockingValidator(new ValidatorStrasse(() => this.adresse.value, 55, 10, 30),
			"strassenname", "hausnummer", "zusatzHausnummer");

		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.schulnummerStatistik), 'schulnummerStatistik');
	}

	adresse = computed({
		get: () => AdressenUtils.combineStrasse(this.proxy.strassenname, this.proxy.hausnummer, this.proxy.zusatzHausnummer),
		set: (adresse: string | null) => {
			const [strasse, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(adresse);
			this.proxy.strassenname = strasse;
			this.proxy.hausnummer = hausnummer;
			this.proxy.zusatzHausnummer = hausnummerZusatz;
		},
	});

	selectedSchulformSonstigeSchule = computed<HerkunftSchulformKatalogEintrag | null>({
		get: () => HerkunftSchulform.data().getEintragByID(this.proxy.idSchulform ?? -1),
		set: (value) => this.proxy.idSchulform = value?.id ?? null,
	});

	schulformInternal = computed<string | null>(() => Schulform.data().getEintragByID(this.proxy.idSchulform ?? -1)?.text ?? '');
}
