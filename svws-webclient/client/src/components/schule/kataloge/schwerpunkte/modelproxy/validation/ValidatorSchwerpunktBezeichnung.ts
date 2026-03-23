import type { SchuelerSchwerpunkt } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorSchwerpunktBezeichnung extends BasicValidator {

	constructor(data: () => SchuelerSchwerpunkt, alleSchuelerschwerpunkte: () => Iterable<SchuelerSchwerpunkt>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<SchuelerSchwerpunkt>(data, (data: SchuelerSchwerpunkt): number => data.id, (data: SchuelerSchwerpunkt): string => data.bezeichnung, alleSchuelerschwerpunkte, false));
		this._validatoren.add(new ValidatorInputRequired(() => data().bezeichnung));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().bezeichnung, null, 50));

	}

	protected pruefe(): boolean	{
		return true;
	}
}
