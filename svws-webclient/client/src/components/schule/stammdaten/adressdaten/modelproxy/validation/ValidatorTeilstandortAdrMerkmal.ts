import type { Teilstandort } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern, StringPattern } from "@ui";

export class ValidatorTeilstandortAdrMerkmal extends BasicValidator {

	constructor(data: () => Teilstandort, liste: () => Iterable<Teilstandort>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorInputRequired(() => data().adrMerkmal));
		this._validatoren.add(new ValidatorStringIsUniqueInList<Teilstandort>(data, () => null, data => data.adrMerkmal, liste, true));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().adrMerkmal, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().adrMerkmal, 1, 1));
	}

	protected pruefe(): boolean {
		return true;
	}
}
