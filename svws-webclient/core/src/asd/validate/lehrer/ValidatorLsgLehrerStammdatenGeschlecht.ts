import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorLsg01LehrerStammdatenGeschlecht } from '../../../asd/validate/lehrer/ValidatorLsg01LehrerStammdatenGeschlecht';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsgLehrerStammdatenGeschlecht extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Geschlecht des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLsg01LehrerStammdatenGeschlecht(daten, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsgLehrerStammdatenGeschlecht';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLsgLehrerStammdatenGeschlecht', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsgLehrerStammdatenGeschlecht>('de.svws_nrw.asd.validate.lehrer.ValidatorLsgLehrerStammdatenGeschlecht');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsgLehrerStammdatenGeschlecht(obj: unknown): ValidatorLsgLehrerStammdatenGeschlecht {
	return obj as ValidatorLsgLehrerStammdatenGeschlecht;
}
