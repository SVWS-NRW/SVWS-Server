import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Telefonart } from "@core/core/data/schule/Telefonart";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorTelefonartBezeichnung extends BasicValidator {

	constructor(data: () => Telefonart, alleTelefonarten: () => Iterable<Telefonart>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<Telefonart>(data, (data: Telefonart): number => data.id, (data: Telefonart): string => data.bezeichnung, alleTelefonarten, false));
		this._validatoren.add(new ValidatorInputRequired(() => data().bezeichnung));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().bezeichnung, null, 30));
	}

	protected pruefe(): boolean {
		return true;
	}
}
