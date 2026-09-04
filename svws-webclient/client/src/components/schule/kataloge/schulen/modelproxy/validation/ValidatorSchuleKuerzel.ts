import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

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
