import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Floskelgruppe } from "@core/core/data/schule/Floskelgruppe";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorFloskelgruppeKuerzel extends BasicValidator {

	constructor(data: () => Floskelgruppe, alleFloskelgruppen: () => Iterable<Floskelgruppe>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<Floskelgruppe>(data, (data: Floskelgruppe): number => data.id, (data: Floskelgruppe): string => data.kuerzel, alleFloskelgruppen, false));
		this._validatoren.add(new ValidatorInputRequired(() => data().kuerzel));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().kuerzel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().kuerzel, null, 10));
	}

	protected pruefe(): boolean	{
		return true;
	}

}
