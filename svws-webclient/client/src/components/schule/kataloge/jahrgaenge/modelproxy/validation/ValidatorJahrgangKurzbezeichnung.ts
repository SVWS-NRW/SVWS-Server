import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorJahrgangKurzbezeichnung extends BasicValidator {

	constructor(data: () => JahrgangsDaten, liste: () => Iterable<JahrgangsDaten>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<JahrgangsDaten>(data, (data) => data.id, (data) => data.kurzbezeichnung, liste, false));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().kurzbezeichnung, StringPattern.NO_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().kurzbezeichnung, null, 2));
	}

	protected pruefe(): boolean {
		return true;
	}
}
