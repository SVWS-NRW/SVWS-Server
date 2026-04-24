import type { SchulEintrag } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { StringPattern, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";

export class ValidatorSchuleKuerzel extends BasicValidator {

	constructor(data: () => SchulEintrag, alleSchulen: () => Iterable<SchulEintrag>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => data().kuerzel, null, 10));
		this._validatoren.add(new ValidatorStringIsUniqueInList<SchulEintrag>(data, (data: SchulEintrag): number => data.id, (data: SchulEintrag) => data.kuerzel, alleSchulen, false));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().kuerzel, StringPattern.NO_WHITESPACES));
	}

	protected pruefe(): boolean	{
		return true;
	}
}
