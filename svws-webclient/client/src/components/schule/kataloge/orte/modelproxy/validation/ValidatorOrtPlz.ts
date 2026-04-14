import type { OrtKatalogEintrag } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { StringPattern, ValidatorInputRequired, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";

export class ValidatorOrtPlz extends BasicValidator {
	constructor(data: () => OrtKatalogEintrag) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => data().plz, null, 10));
		this._validatoren.add(new ValidatorInputRequired(() => data().plz));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().plz, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
	}

	protected pruefe(): boolean {
		return true;
	}
}
