import type { Telefonart } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern } from "@ui";
import { StringPattern } from "../../../../../../../../ui/src/validation/common/ValidatorStringMatchesPattern";

export class ValidatorTelefonartBezeichnung extends BasicValidator {

	constructor(data: () => Telefonart, alleTelefonarten: () => Iterable<Telefonart>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorStringIsUniqueInList<Telefonart>(data, (data: Telefonart): number => data.id, (data: Telefonart): string => data.bezeichnung, alleTelefonarten, false));
		this._validatoren.add(new ValidatorInputRequired(() => data().bezeichnung));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().bezeichnung, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().bezeichnung, null, 30));
	}

	protected pruefe(): boolean {
		return true;
	}
}
