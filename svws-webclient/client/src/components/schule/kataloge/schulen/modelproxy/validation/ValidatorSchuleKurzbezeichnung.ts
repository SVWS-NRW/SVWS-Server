import type { SchulEintrag } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { StringPattern, ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";

export class ValidatorSchuleKurzbezeichnung extends BasicValidator {

	constructor(data: () => SchulEintrag, alleSchulen: () => Iterable<SchulEintrag>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringLength(() => data().kurzbezeichnung, null, 40));
		this._validatoren.add(new ValidatorInputRequired(() => data().kurzbezeichnung));
		this._validatoren.add(new ValidatorStringIsUniqueInList<SchulEintrag>(data, (data: SchulEintrag): number => data.id, (data: SchulEintrag) => data.kurzbezeichnung, alleSchulen, false));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().kurzbezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
	}

	protected pruefe(): boolean	{
		return true;
	}
}
