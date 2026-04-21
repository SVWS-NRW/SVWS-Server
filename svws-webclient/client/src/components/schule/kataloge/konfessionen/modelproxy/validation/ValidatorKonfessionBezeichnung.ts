import type { ReligionEintrag } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { StringPattern, ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";

export class ValidatorKonfessionBezeichnung extends BasicValidator {
	constructor(data: () => ReligionEintrag, alleKonfessionen: () => Iterable<ReligionEintrag>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<ReligionEintrag>(data, (data) => data.id, (data) => data.bezeichnung, alleKonfessionen, false));
		this._validatoren.add(new ValidatorInputRequired(() => data().bezeichnung));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().bezeichnung, null, 30));
	}

	protected pruefe(): boolean	{
		return true;
	}

}
