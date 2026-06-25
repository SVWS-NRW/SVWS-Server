import {
	ModelProxy,
	StringPattern,
	ValidatorInputRequired,
	ValidatorNumberRange,
	ValidatorStringLength,
	ValidatorStringMatchesPattern,
} from "@ui";
import type { Kindergarten } from "@core";
import { AdressenUtils } from "@core";
import { ValidatorKindergaertenBezeichnung } from "~/components/schule/kataloge/kindergaerten/modelproxy/validation/ValidatorKindergaertenBezeichnung";
import { ValidatorStrasse } from "../../../../../../../ui/src/validation/common/ValidatorStrasse";
import { computed } from "vue";

/**
 * ModelProxy für Kindergärten.
 */
export class KindergaertenModelProxy extends ModelProxy<Kindergarten> {

	/**
	 * ModelProxy für Kindergärten.
	 *
	 * @param data    Lambda für den Zugriff auf die Original-Daten
	 * @param kindergaerten    Liste aller Kindergärten
	 * @param patch   Methode zum Patchen einzelner Attribute
	 */
	constructor(
		data: () => Kindergarten,
		kindergaerten: () => Iterable<Kindergarten>,
		patch?: (data: Partial<Kindergarten>) => Promise<boolean>
	) {
		const listOfAutopatchProps: Iterable<keyof Kindergarten> =
			["istSichtbar"];
		super({ data, patch, listOfAutopatchProps });
		this.addValidatoren(kindergaerten);
		this.validate();
	}

	private addValidatoren(liste: () => Iterable<Kindergarten>) {
		// Bezeichnung
		this.addBlockingValidator(new ValidatorKindergaertenBezeichnung(() => this.proxy, liste), "bezeichnung");
		// Bemerkung
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bemerkung, null, 50), "bemerkung");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bemerkung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "bemerkung");
		// Telefon
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.tel, StringPattern.IS_PHONE_NUMBER), "tel");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.tel, null, 20), "tel");
		// Email
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.email, StringPattern.IS_EMAIL), "email");
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.email, null, 40), "email");
		// Straße
		this.addBlockingValidator(new ValidatorStrasse(() => this.adresse.value, 55, 10, 30),
			"strassenname", "hausNr", "hausNrZusatz");
		// PLZ
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.plz, null, 10), "plz");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.plz, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "plz");
		// Ort
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.ort, null, 30), "ort");
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.ort, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), "ort");
		// Sortierung
		this.addBlockingValidator(new ValidatorNumberRange(() => this.proxy.sortierung, 0, 32000), "sortierung");
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.sortierung), "sortierung");
	}

	adresse = computed({
		get: () => AdressenUtils.combineStrasse(this.proxy.strassenname, this.proxy.hausNr, this.proxy.hausNrZusatz),
		set: (adresse: string | null) => {
			const [strasse, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(adresse);
			this.proxy.strassenname = strasse;
			this.proxy.hausNr = hausnummer;
			this.proxy.hausNrZusatz = hausnummerZusatz;
		},
	});
}
