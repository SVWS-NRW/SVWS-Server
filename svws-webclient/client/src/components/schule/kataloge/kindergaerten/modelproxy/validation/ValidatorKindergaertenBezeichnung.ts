import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Kindergarten } from "@core/core/data/schule/Kindergarten";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorKindergaertenBezeichnung extends BasicValidator {

	constructor(kindergaerten: () => Kindergarten, liste: () => Iterable<Kindergarten>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorInputRequired(() => kindergaerten().bezeichnung));
		this._validatoren.add(new ValidatorStringIsUniqueInList<Kindergarten>(kindergaerten, data => data.id, data => data.bezeichnung, liste, false));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => kindergaerten().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => kindergaerten().bezeichnung, null, 100));
	}

	protected pruefe(): boolean {
		return true;
	}
}
