import { computed } from "vue";

import type { SchuleStammdaten } from "@core/asd/data/schule/SchuleStammdaten";
import { AdressenUtils } from "@core/core/utils/AdressenUtils";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStrasse } from "@ui/validation/common/ValidatorStrasse";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

/**
 * ModelProxy für die Stammdaten der Schule (Ansicht Adressdaten)
 */
export class SchuleStammdatenModelProxy extends ModelProxy<SchuleStammdaten> {

	/**
	 * ModelProxy für die Stammdaten der Schule
	 *
	 * @param data    Lambda für den Zugriff auf die Original-Daten
	 * @param patch   Methode zum Patchen der Daten
	 */
	constructor(
		data: () => SchuleStammdaten,
		patch?: (data: Partial<SchuleStammdaten>) => Promise<boolean>
	) {
		super({ data, patch });
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		// Bezeichnung 1
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.bezeichnung1), "bezeichnung1");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnung1, null, 50), "bezeichnung1");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung1, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bezeichnung1");

		// Bezeichnung 2
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnung2, null, 50), "bezeichnung2");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung2, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bezeichnung2");

		// Bezeichnung 3
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bezeichnung3, null, 50), "bezeichnung3");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bezeichnung3, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bezeichnung3");

		// Straße (Straßenname, Hausnummer und Hausnummerzusatz)
		this.addBlockingValidator(new ValidatorStrasse(() => this.strasse.value, 55, 10, 30), "strassenname", "hausnummer", "hausnummerZusatz");

		// PLZ
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.plz, null, 10), "plz");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.plz, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "plz");

		// Ort
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.ort, null, 50), "ort");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.ort, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "ort");

		// Telefon
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.telefon, null, 20), "telefon");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.telefon, StringPattern.IS_PHONE_NUMBER), "telefon");

		// Fax
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.fax, null, 20), "fax");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.fax, StringPattern.IS_PHONE_NUMBER), "fax");

		// Homepage
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.webAdresse, null, 100), "webAdresse");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.webAdresse, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "webAdresse");

		// E-Mail
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.email, null, 100), "email");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.email, StringPattern.IS_EMAIL), "email");
	}

	/** Die Straße als kombiniertes Feld aus Straßenname, Hausnummer und Hausnummerzusatz */
	strasse = computed<string | null>({
		get: () => AdressenUtils.combineStrasse(this.proxy.strassenname, this.proxy.hausnummer, this.proxy.hausnummerZusatz),
		set: (adresse) => {
			const [strassenname, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(adresse);
			if ((strassenname === "") && (hausnummer === "") && (hausnummerZusatz === "")) {
				this.proxy.strassenname = null;
				this.proxy.hausnummer = null;
				this.proxy.hausnummerZusatz = null;
			} else {
				this.proxy.strassenname = strassenname;
				this.proxy.hausnummer = hausnummer;
				this.proxy.hausnummerZusatz = hausnummerZusatz;
			}
		},
	});

}
