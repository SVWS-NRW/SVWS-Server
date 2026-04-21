import { BasicValidator, ValidatorFehlerart, type Kindergarten } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorKindergaertenBezeichnung extends BasicValidator {

	constructor(kindergaerten: () => Kindergarten, liste: () => Iterable<Kindergarten>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorInputRequired(() => kindergaerten().bezeichnung));
		this._validatoren.add(new ValidatorStringIsUniqueInList<Kindergarten>(kindergaerten, data => data.id, data => data.bezeichnung, liste, false));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => kindergaerten().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => kindergaerten().bezeichnung, null, 100));
	}

	protected pruefe(): boolean {
		return true;
	}
}
