import { ValidatorLsg00LehrerStammdatenGeschlecht } from '../../../asd/validate/lehrer/ValidatorLsg00LehrerStammdatenGeschlecht';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsgLehrerStammdatenGeschlecht extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: LehrerStammdaten, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLsg00LehrerStammdatenGeschlecht(daten, kontext));
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

	public static class = new Class<ValidatorLsgLehrerStammdatenGeschlecht>('de.svws_nrw.asd.validate.lehrer.ValidatorLsgLehrerStammdatenGeschlecht');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsgLehrerStammdatenGeschlecht(obj: unknown): ValidatorLsgLehrerStammdatenGeschlecht {
	return obj as ValidatorLsgLehrerStammdatenGeschlecht;
}
