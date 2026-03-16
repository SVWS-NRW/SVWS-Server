import { BasicValidator, type JahrgangsDaten, ValidatorFehlerart } from "@core";
import { ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern, ValidatorInputRequired } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorJahrgangKuerzel extends BasicValidator {

	constructor(data: () => JahrgangsDaten, liste: () => Iterable<JahrgangsDaten>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<JahrgangsDaten>(data, (data: JahrgangsDaten) => data.id, (data: JahrgangsDaten) => data.kuerzel, liste, false));
		this._validatoren.add(new ValidatorInputRequired<string>(() => data().kuerzel));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().kuerzel, StringPattern.NO_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().kuerzel, null, 20));
	}


	protected pruefe(): boolean {
		return true;
	}
}
