import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { FachDaten } from "@core/core/data/fach/FachDaten";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorFachKuerzel extends BasicValidator {
	constructor(data: () => FachDaten, alleFaecher: () => Iterable<FachDaten>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<FachDaten>(data, (data: FachDaten): number => data.id, (data: FachDaten): string => data.kuerzel, alleFaecher, false));
		this._validatoren.add(new ValidatorInputRequired(() => data().kuerzel));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().kuerzel, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().kuerzel, null, 20));
	}

	protected pruefe(): boolean	{
		return true;
	}

}
