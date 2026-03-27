import type { Floskel } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorFloskelKuerzel extends BasicValidator {

	constructor(data: () => Floskel, alleFloskeln: () => Iterable<Floskel>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<Floskel>(data, (data: Floskel): number => data.id, (data: Floskel): string => data.kuerzel, alleFloskeln, false));
		this._validatoren.add(new ValidatorInputRequired(() => data().kuerzel));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().kuerzel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().kuerzel, null, 10));
	}

	protected pruefe(): boolean {
		return true;
	}

}
