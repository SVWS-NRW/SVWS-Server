import { ValidatorLsgLehrerStammdatenGeschlecht } from '../../../asd/validate/lehrer/ValidatorLsgLehrerStammdatenGeschlecht';
import { ValidatorLskLehrerStammdatenKuerzel } from '../../../asd/validate/lehrer/ValidatorLskLehrerStammdatenKuerzel';
import { ValidatorLsdLehrerStammdatenGeburtsdatum } from '../../../asd/validate/lehrer/ValidatorLsdLehrerStammdatenGeburtsdatum';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorLsnLehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLsnLehrerStammdatenNachname';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLsvLehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLsvLehrerStammdatenVorname';

export class ValidatorLehrerStammdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: LehrerStammdaten, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLsnLehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLsvLehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLsdLehrerStammdatenGeburtsdatum(daten, kontext));
		this._validatoren.add(new ValidatorLsgLehrerStammdatenGeschlecht(daten, kontext));
		this._validatoren.add(new ValidatorLskLehrerStammdatenKuerzel(daten, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdaten', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerStammdaten>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdaten');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerStammdaten(obj: unknown): ValidatorLehrerStammdaten {
	return obj as ValidatorLehrerStammdaten;
}
