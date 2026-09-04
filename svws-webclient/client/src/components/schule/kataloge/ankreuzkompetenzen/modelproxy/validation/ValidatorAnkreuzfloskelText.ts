import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import type { Ankreuzkompetenz } from "@core/core/data/schule/Ankreuzkompetenz";
import { ValidatorInputRequired } from "@ui/validation/common/ValidatorInputRequired";
import { ValidatorStringIsUniqueInList } from "@ui/validation/common/ValidatorStringIsUniqueInList";
import { ValidatorStringLength } from "@ui/validation/common/ValidatorStringLength";
import { ValidatorStringMatchesPattern, StringPattern } from "@ui/validation/common/ValidatorStringMatchesPattern";

export class ValidatorAnkreuzfloskelText extends BasicValidator {

	constructor(data: () => Ankreuzkompetenz, liste: () => Iterable<Ankreuzkompetenz>) {
		super(ValidatorFehlerart.MUSS);
		this._validatoren.add(new ValidatorInputRequired(() => data().floskelText));
		// Der FloskelText muss nur pro Fach unique sein, daher werden hier nur die Ankreuzkompetenzen zum gleichen Fach verglichen
		this._validatoren.add(new ValidatorStringIsUniqueInList<Ankreuzkompetenz>(
			data,
			data => data.id,
			data => data.floskelText,
			() => [...liste()].filter(ak => ak.idFach === data().idFach),
			false
		));
		this._validatoren.add(new ValidatorStringMatchesPattern(() => data().floskelText, StringPattern.NO_LEADING_OR_TRAILING_WHITESPACES));
		this._validatoren.add(new ValidatorStringLength(() => data().floskelText, 1, 255));
	}

	protected pruefe(): boolean {
		return true;
	}
}
