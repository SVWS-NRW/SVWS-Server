import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorSchuleSchulname extends BasicValidator {

	constructor(data: () => SchulEintrag) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => data().name, null, 120));
		this._validatoren.add(new ValidatorInputRequired(() => data().name));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().name, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
	}

	protected pruefe(): boolean	{
		return true;
	}
}
