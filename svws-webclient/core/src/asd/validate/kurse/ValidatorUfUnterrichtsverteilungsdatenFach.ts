import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorUf00UnterrichtsverteilungsdatenFach } from '../../../asd/validate/kurse/ValidatorUf00UnterrichtsverteilungsdatenFach';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorUfUnterrichtsverteilungsdatenFach extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idFach   FachID
	 * @param kontext  der Kontext des Validators
	 */
	public constructor(idFach: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorUf00UnterrichtsverteilungsdatenFach(idFach, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.kurse.ValidatorUfUnterrichtsverteilungsdatenFach';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.kurse.ValidatorUfUnterrichtsverteilungsdatenFach', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorUfUnterrichtsverteilungsdatenFach>('de.svws_nrw.asd.validate.kurse.ValidatorUfUnterrichtsverteilungsdatenFach');

}

export function cast_de_svws_nrw_asd_validate_kurse_ValidatorUfUnterrichtsverteilungsdatenFach(obj: unknown): ValidatorUfUnterrichtsverteilungsdatenFach {
	return obj as ValidatorUfUnterrichtsverteilungsdatenFach;
}
