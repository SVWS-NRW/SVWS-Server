import { StringPattern, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { ValidatorKontext } from "@core";
import { BasicValidator, ValidatorFehlerart, ValidatorLsvLehrerStammdatenVorname } from "@core";

export class ValidatorLehrerIndividualdatenVorname extends BasicValidator {

	constructor(vorname: () => string, validatorKontext: () => ValidatorKontext) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => vorname(), null, 60));
		this._validatoren.add(new ValidatorInputRequired(() => vorname()));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => vorname(), StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorLsvLehrerStammdatenVorname({ get: () => vorname() }, validatorKontext()));
	}

	protected pruefe(): boolean {
		return true;
	}
}
