import type { Teilstandort } from "@core/core/data/schule/Teilstandort";
import { ModelProxy } from "@ui/model/ModelProxy";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

/**
 * ModelProxy für Teilstandorte
 */
export class TeilstandortModelProxy extends ModelProxy<Teilstandort> {

	/**
	 * ModelProxy für Teilstandorte
	 *
	 * @param data    Lambda für den Zugriff auf die Original-Daten
	 * @param patch   Methode zum Patchen der Daten
	 */
	constructor(
		data: () => Teilstandort,
		patch?: (data: Partial<Teilstandort>) => Promise<boolean>
	) {
		super({ data, patch });
		this.addValidatoren();
		this.validate();
	}

	private addValidatoren() {
		// Merkmal
		this.addBlockingValidator(new ValidatorInputRequired(() => this.proxy.adrMerkmal), 'adrMerkmal');

		// Kürzel
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.kuerzel, null, 30), 'kuerzel');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.kuerzel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'kuerzel');

		// Straße
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.strassenname, null, 55), 'strassenname');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.strassenname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'strassenname');

		// Hausnummer
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.hausNr, null, 10), 'hausNr');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.hausNr, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'hausNr');

		// Hausnummer-Zusatz
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.hausNrZusatz, null, 30), 'hausNrZusatz');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.hausNrZusatz, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'hausNrZusatz');

		// PLZ
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.plz, null, 10), 'plz');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.plz, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'plz');

		// Ort
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.ort, null, 50), 'ort');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.ort, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'ort');

		// Bemerkung
		this.addBlockingValidator(new ValidatorStringLength(() => this.proxy.bemerkung, null, 50), 'bemerkung');
		this.addBlockingValidator(new ValidatorStringMatchesPattern(() => this.proxy.bemerkung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES), 'bemerkung');
	}
}
