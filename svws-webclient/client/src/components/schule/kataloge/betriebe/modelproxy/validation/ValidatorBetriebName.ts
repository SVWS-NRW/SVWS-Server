import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Betrieb } from "@core/core/data/schule/Betrieb";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

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
