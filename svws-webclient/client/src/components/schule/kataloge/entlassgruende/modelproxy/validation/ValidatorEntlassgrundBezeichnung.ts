import type { KatalogEntlassgrund } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorEntlassgrundBezeichnung extends BasicValidator {

	constructor(data: () => KatalogEntlassgrund, alleEntlassgruende: () => Iterable<KatalogEntlassgrund>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<KatalogEntlassgrund>(data, (data: KatalogEntlassgrund): number => data.id, (data: KatalogEntlassgrund): string => data.bezeichnung, alleEntlassgruende, false));
		this._validatoren.add(new ValidatorInputRequired(() => data().bezeichnung));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().bezeichnung, null, 30));
	}

	protected pruefe(): boolean	{
		return true;
	}

}
