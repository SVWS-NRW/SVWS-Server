import type { Ankreuzkompetenz } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorAnkreuzfloskelText extends BasicValidator {

	constructor(data: () => Ankreuzkompetenz, liste: () => Iterable<Ankreuzkompetenz>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorInputRequired(() => data().floskelText));
		this._validatoren.add(new ValidatorStringIsUniqueInList<Ankreuzkompetenz>(data, data => data.id, data => data.floskelText, liste, false));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().floskelText, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().floskelText, 1, 255));
	}

	protected pruefe(): boolean {
		return true;
	}
}
