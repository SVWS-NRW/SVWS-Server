import { type Teilleistungsart, BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

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
