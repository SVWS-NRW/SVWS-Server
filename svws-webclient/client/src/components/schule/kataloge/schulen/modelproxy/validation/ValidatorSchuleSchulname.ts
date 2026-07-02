import type { SchulEintrag } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { StringPattern, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";

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
