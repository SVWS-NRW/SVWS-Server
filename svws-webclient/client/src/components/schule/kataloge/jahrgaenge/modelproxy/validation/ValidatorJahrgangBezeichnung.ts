import { BasicValidator, type JahrgangsDaten, ValidatorFehlerart } from "@core";
import { ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern, ValidatorStringNotBlank } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorJahrgangBezeichnung extends BasicValidator {

	constructor(data: () => JahrgangsDaten, liste: () => Iterable<JahrgangsDaten>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<JahrgangsDaten>(data, (data) => data.id, (data) => data.bezeichnung, liste, false));
		this._validatoren.add(new ValidatorStringNotBlank(() => data().bezeichnung));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().bezeichnung, 100));
	}


	protected pruefe(): boolean {
		return true;
	}
}
