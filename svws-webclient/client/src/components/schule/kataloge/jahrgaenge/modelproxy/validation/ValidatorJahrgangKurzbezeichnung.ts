import { BasicValidator, type JahrgangsDaten, ValidatorFehlerart } from "@core";
import { ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorJahrgangKurzbezeichnung extends BasicValidator {

	constructor(data: () => JahrgangsDaten, liste: () => Iterable<JahrgangsDaten>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<JahrgangsDaten>(data, (data) => data.id, (data) => data.kurzbezeichnung, liste, false));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().kurzbezeichnung, StringPattern.NO_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().kurzbezeichnung, null, 2));
	}

	protected pruefe(): boolean {
		return true;
	}
}
