import { ValidatorLsk10LehrerStammdatenKuerzel } from '../../../asd/validate/lehrer/ValidatorLsk10LehrerStammdatenKuerzel';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLskLehrerStammdatenKuerzel extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLsk10LehrerStammdatenKuerzel(daten, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLskLehrerStammdatenKuerzel';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLskLehrerStammdatenKuerzel', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLskLehrerStammdatenKuerzel>('de.svws_nrw.asd.validate.lehrer.ValidatorLskLehrerStammdatenKuerzel');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLskLehrerStammdatenKuerzel(obj: unknown): ValidatorLskLehrerStammdatenKuerzel {
	return obj as ValidatorLskLehrerStammdatenKuerzel;
}
