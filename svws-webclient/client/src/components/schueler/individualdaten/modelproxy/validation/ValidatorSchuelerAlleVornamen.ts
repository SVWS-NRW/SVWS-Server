import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { StringPattern, ValidatorStringMatchesPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorSchuelerAlleVornamen extends BasicValidator {

	constructor(data: () => { alleVornamen: string | null }) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => data().alleVornamen, null, 255));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().alleVornamen, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
	}

	protected pruefe(): boolean {
		return true;
	}
}
