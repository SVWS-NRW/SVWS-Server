import type { OrtKatalogEintrag } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { StringPattern, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";

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
