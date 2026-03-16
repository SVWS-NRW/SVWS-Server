import type { Betrieb } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorBetriebName extends BasicValidator {

	constructor(betrieb: () => Betrieb, liste: () => Iterable<Betrieb>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorInputRequired<string>(() => betrieb().name));
		this._validatoren.add(new ValidatorStringLength(() => betrieb().name, null, 50));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => betrieb().name, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringIsUniqueInList(betrieb, (betrieb) => betrieb.id, (betrieb) => betrieb.name, liste, false));
	}

	protected pruefe(): boolean {
		return true;
	}

}
