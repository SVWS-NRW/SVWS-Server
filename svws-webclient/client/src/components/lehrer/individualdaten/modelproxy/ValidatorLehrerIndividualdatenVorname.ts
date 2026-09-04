import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorLehrerIndividualdatenVorname extends BasicValidator {

	constructor(vorname: () => string) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => vorname(), null, 60));
		this._validatoren.add(new ValidatorInputRequired(() => vorname()));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => vorname(), StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
	}

	protected pruefe(): boolean {
		return true;
	}
}
