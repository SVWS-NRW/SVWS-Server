import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorJahrgangBezeichnung extends BasicValidator {

	constructor(data: () => JahrgangsDaten, liste: () => Iterable<JahrgangsDaten>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<JahrgangsDaten>(data, (data) => data.id, (data) => data.bezeichnung, liste, false));
		this._validatoren.add(new ValidatorInputRequired(() => data().bezeichnung));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().bezeichnung, null, 100));
	}


	protected pruefe(): boolean {
		return true;
	}
}
