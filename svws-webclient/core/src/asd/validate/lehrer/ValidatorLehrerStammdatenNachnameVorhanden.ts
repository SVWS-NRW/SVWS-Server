import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerStammdatenNachnameVorhanden extends Validator<LehrerStammdaten> {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : LehrerStammdaten, kontext : ValidatorKontext) {
		super(daten, kontext);
	}

	protected pruefe() : boolean {
		const nachname : string | null = this.daten().nachname;
		if ((nachname === null) || (nachname.length === 0)) {
			this.addFehler("Kein Wert im Feld 'nachname'.");
			return false;
		}
		if (JavaString.isBlank(nachname.trim())) {
			this.addFehler("Das Feld 'nachname' darf nicht nur aus Leerzeichen bestehen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameVorhanden';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameVorhanden', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerStammdatenNachnameVorhanden>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerStammdatenNachnameVorhanden');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerStammdatenNachnameVorhanden(obj : unknown) : ValidatorLehrerStammdatenNachnameVorhanden {
	return obj as ValidatorLehrerStammdatenNachnameVorhanden;
}
