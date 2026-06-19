import type { Ankreuzkompetenz } from "@core";
import { BasicValidator, ValidatorFehlerart } from "@core";
import { ValidatorInputRequired, ValidatorStringIsUniqueInList, ValidatorStringLength, ValidatorStringMatchesPattern, StringPattern } from "@ui";

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
