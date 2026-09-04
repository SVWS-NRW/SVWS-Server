import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Teilleistungsart } from "@core/core/data/kataloge/Teilleistungsart";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorTeilleistungsartBezeichnung extends BasicValidator {

	constructor(data: () => Teilleistungsart, liste: () => Iterable<Teilleistungsart>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorInputRequired(() => data().bezeichnung));
		this._validatoren.add(new ValidatorStringLength(() => data().bezeichnung, 1, 50));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringIsUniqueInList(data, (data: Teilleistungsart) => data.id, (data: Teilleistungsart) => data.bezeichnung,
			liste, false));
	}

	protected pruefe(): boolean {
		return true;
	}
}
