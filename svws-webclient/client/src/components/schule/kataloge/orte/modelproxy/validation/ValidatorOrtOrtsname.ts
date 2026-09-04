import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { OrtKatalogEintrag } from "@core/core/data/kataloge/OrtKatalogEintrag";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorOrtOrtsname extends BasicValidator {
	constructor(data: () => OrtKatalogEintrag) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => data().ortsname, null, 50));
		this._validatoren.add(new ValidatorInputRequired(() => data().ortsname));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().ortsname, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
	}

	protected pruefe(): boolean {
		return true;
	}

}
