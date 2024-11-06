import { ValidatorLehrerStammdatenNachname } from '../../../asd/validate/lehrer/ValidatorLehrerStammdatenNachname';
import { ValidatorLehrerStammdatenGeburtsdatum } from '../../../asd/validate/lehrer/ValidatorLehrerStammdatenGeburtsdatum';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorLehrerStammdatenVorname } from '../../../asd/validate/lehrer/ValidatorLehrerStammdatenVorname';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerStammdaten extends Validator<LehrerStammdaten> {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : LehrerStammdaten, kontext : ValidatorKontext) {
		super(daten, kontext);
		this._validatoren.add(new ValidatorLehrerStammdatenNachname(daten, kontext));
		this._validatoren.add(new ValidatorLehrerStammdatenVorname(daten, kontext));
		this._validatoren.add(new ValidatorLehrerStammdatenGeburtsdatum(daten, kontext));
	}

	protected pruefe() : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerStammdaten>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdaten');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerStammdaten(obj : unknown) : ValidatorLehrerStammdaten {
	return obj as ValidatorLehrerStammdaten;
}
