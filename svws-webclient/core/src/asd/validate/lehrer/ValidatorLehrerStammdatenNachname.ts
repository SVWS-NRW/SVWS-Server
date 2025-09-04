import { ValidatorLehrerStammdatenNachnameVorhanden } from '../../../asd/validate/lehrer/ValidatorLehrerStammdatenNachnameVorhanden';
import { ValidatorLehrerStammdatenNachnameNurEinstellig } from '../../../asd/validate/lehrer/ValidatorLehrerStammdatenNachnameNurEinstellig';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLehrerStammdatenNachnameAnredeFehlerhaft } from '../../../asd/validate/lehrer/ValidatorLehrerStammdatenNachnameAnredeFehlerhaft';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLehrerStammdatenNachnamePlausibel } from '../../../asd/validate/lehrer/ValidatorLehrerStammdatenNachnamePlausibel';
import { ValidatorLehrerStammdatenNachnameOhneLeerzeichenVorNachBindestrich } from '../../../asd/validate/lehrer/ValidatorLehrerStammdatenNachnameOhneLeerzeichenVorNachBindestrich';

export class ValidatorLehrerStammdatenNachname extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : LehrerStammdaten, kontext : ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLehrerStammdatenNachnameVorhanden(daten, kontext));
		this._validatoren.add(new ValidatorLehrerStammdatenNachnamePlausibel(daten, kontext));
		this._validatoren.add(new ValidatorLehrerStammdatenNachnameOhneLeerzeichenVorNachBindestrich(daten, kontext));
		this._validatoren.add(new ValidatorLehrerStammdatenNachnameAnredeFehlerhaft(daten, kontext));
		this._validatoren.add(new ValidatorLehrerStammdatenNachnameNurEinstellig(daten, kontext));
	}

	protected pruefe() : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachname';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachname', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerStammdatenNachname>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachname');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerStammdatenNachname(obj : unknown) : ValidatorLehrerStammdatenNachname {
	return obj as ValidatorLehrerStammdatenNachname;
}
