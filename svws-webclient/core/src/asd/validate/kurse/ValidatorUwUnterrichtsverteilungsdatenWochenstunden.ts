import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorUw00UnterrichtsverteilungsdatenWochenstunden } from '../../../asd/validate/kurse/ValidatorUw00UnterrichtsverteilungsdatenWochenstunden';

export class ValidatorUwUnterrichtsverteilungsdatenWochenstunden extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param wochenstundenKurs   die Wochenstunden des Kurses
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(wochenstundenKurs: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorUw00UnterrichtsverteilungsdatenWochenstunden(wochenstundenKurs, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUwUnterrichtsverteilungsdatenWochenstunden';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUwUnterrichtsverteilungsdatenWochenstunden', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUwUnterrichtsverteilungsdatenWochenstunden>('de.svws_nrw.asd.validate.kurse.ValidatorUwUnterrichtsverteilungsdatenWochenstunden');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUwUnterrichtsverteilungsdatenWochenstunden(obj: unknown): ValidatorUwUnterrichtsverteilungsdatenWochenstunden {
	return obj as ValidatorUwUnterrichtsverteilungsdatenWochenstunden;
}
