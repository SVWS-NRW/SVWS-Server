import type { Floskelgruppe } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorFloskelgruppeBezeichnung extends BasicValidator {

	constructor(data: () => Floskelgruppe, alleFloskelgruppen: () => Iterable<Floskelgruppe>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<Floskelgruppe>(data, (data: Floskelgruppe): number => data.id, (data: Floskelgruppe): string => data.bezeichnung, alleFloskelgruppen, false));
		this._validatoren.add(new ValidatorInputRequired(() => data().bezeichnung));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().bezeichnung, null, 50));
	}

	protected pruefe(): boolean	{
		return true;
	}

}
