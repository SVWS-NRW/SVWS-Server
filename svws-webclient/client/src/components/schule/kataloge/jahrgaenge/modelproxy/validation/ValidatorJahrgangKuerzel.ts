import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorJahrgangKuerzel extends BasicValidator {

	constructor(data: () => JahrgangsDaten, liste: () => Iterable<JahrgangsDaten>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<JahrgangsDaten>(data, (data: JahrgangsDaten) => data.id, (data: JahrgangsDaten) => data.kuerzel, liste, false));
		this._validatoren.add(new ValidatorInputRequired<string>(() => data().kuerzel));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().kuerzel, StringPattern.NO_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().kuerzel, null, 20));
	}


	protected pruefe(): boolean {
		return true;
	}
}
