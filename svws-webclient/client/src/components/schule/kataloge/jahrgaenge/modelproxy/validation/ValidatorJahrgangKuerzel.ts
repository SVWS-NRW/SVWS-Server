import { BasicValidator, type JahrgangsDaten, ValidatorFehlerart } from "@core";
import { ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern, ValidatorStringNotBlank } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorJahrgangKuerzel extends BasicValidator {

	constructor(data: () => JahrgangsDaten, liste: () => Iterable<JahrgangsDaten>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<JahrgangsDaten>(data, (data) => data.id, (data) => data.kuerzel, liste, false));
		this._validatoren.add(new ValidatorStringNotBlank(() => data().kuerzel));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().kuerzel, StringPattern.NO_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().kuerzel, 20));
	}


	protected pruefe(): boolean {
		return true;
	}
}
