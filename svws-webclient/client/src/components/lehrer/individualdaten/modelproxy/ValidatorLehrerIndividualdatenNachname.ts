import { StringPattern, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import type { ValidatorKontext } from "@core";
import { BasicValidator, ValidatorFehlerart, ValidatorLsnLehrerStammdatenNachname } from "@core";

export class ValidatorLehrerIndividualdatenNachname extends BasicValidator {

	constructor(nachname: () => string, validatorKontext: () => ValidatorKontext) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => nachname(), null, 120));
		this._validatoren.add(new ValidatorInputRequired(() => nachname()));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => nachname(), StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorLsnLehrerStammdatenNachname({ get: () => nachname() }, validatorKontext()));
	}

	protected pruefe(): boolean {
		return true;
	}
}
