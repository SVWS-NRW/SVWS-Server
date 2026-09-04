import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Teilstandort } from "@core/core/data/schule/Teilstandort";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorTeilstandortAdrMerkmal extends BasicValidator {

	constructor(data: () => Teilstandort, liste: () => Iterable<Teilstandort>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorInputRequired(() => data().adrMerkmal));
		this._validatoren.add(new ValidatorStringIsUniqueInList<Teilstandort>(data, () => null, data => data.adrMerkmal, liste, true));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().adrMerkmal, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().adrMerkmal, 1, 1));
	}

	protected pruefe(): boolean {
		return true;
	}
}
