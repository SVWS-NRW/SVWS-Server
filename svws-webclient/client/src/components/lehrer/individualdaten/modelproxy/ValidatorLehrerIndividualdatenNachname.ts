import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { ValidatorKontext } from "@core/asd/validate/ValidatorKontext";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorLehrerIndividualdatenNachname extends BasicValidator {

	constructor(nachname: () => string, validatorKontext: () => ValidatorKontext) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => nachname(), null, 120));
		this._validatoren.add(new ValidatorInputRequired(() => nachname()));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => nachname(), StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
	}

	protected pruefe(): boolean {
		return true;
	}
}
