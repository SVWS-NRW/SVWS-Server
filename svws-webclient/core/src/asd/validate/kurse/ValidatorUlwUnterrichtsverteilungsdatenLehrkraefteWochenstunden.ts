import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden } from '../../../asd/validate/kurse/ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden';

export class ValidatorUlwUnterrichtsverteilungsdatenLehrkraefteWochenstunden extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenLehrer     die Wochenstunden der Lehrkraft
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(wochenstundenLehrer: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorUlw00UnterrichtsverteilungsdatenLehrkraefteWochenstunden(wochenstundenLehrer, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUlwUnterrichtsverteilungsdatenLehrkraefteWochenstunden';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.kurse.ValidatorUlwUnterrichtsverteilungsdatenLehrkraefteWochenstunden', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUlwUnterrichtsverteilungsdatenLehrkraefteWochenstunden>('de.svws_nrw.asd.validate.kurse.ValidatorUlwUnterrichtsverteilungsdatenLehrkraefteWochenstunden');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUlwUnterrichtsverteilungsdatenLehrkraefteWochenstunden(obj: unknown): ValidatorUlwUnterrichtsverteilungsdatenLehrkraefteWochenstunden {
	return obj as ValidatorUlwUnterrichtsverteilungsdatenLehrkraefteWochenstunden;
}
