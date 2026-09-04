import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { KatalogEntlassgrund } from "@core/core/data/kataloge/KatalogEntlassgrund";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

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
