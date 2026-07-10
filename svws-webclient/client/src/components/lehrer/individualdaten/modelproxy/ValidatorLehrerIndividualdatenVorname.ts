import { StringPattern, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { BasicValidator, ValidatorFehlerart } from "@core";

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
