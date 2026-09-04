import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Floskel } from "@core/core/data/schule/Floskel";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorFloskelKuerzel extends BasicValidator {

	constructor(data: () => Floskel, alleFloskeln: () => Iterable<Floskel>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<Floskel>(data, (data: Floskel): number => data.id, (data: Floskel): string => data.kuerzel, alleFloskeln, false));
		this._validatoren.add(new ValidatorInputRequired(() => data().kuerzel));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().kuerzel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().kuerzel, null, 10));
	}

	protected pruefe(): boolean {
		return true;
	}

}
