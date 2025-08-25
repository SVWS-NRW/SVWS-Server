import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { ValidatorLehrerPersonalabschnittsdaten } from '../../../asd/validate/lehrer/ValidatorLehrerPersonalabschnittsdaten';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerPersonaldaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     	die Daten des Validators
	 * @param stammdaten	die Stammdaten des Lehrers
	 * @param kontext   	der Kontext des Validators
	 */
	public constructor(daten : LehrerPersonaldaten, stammdaten : LehrerStammdaten, kontext : ValidatorKontext) {
		super(kontext);
		for (const abschnittsdaten of daten.abschnittsdaten)
			this._validatoren.add(new ValidatorLehrerPersonalabschnittsdaten(abschnittsdaten, stammdaten, kontext));
	}

	protected pruefe() : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonaldaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonaldaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerPersonaldaten>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonaldaten');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerPersonaldaten(obj : unknown) : ValidatorLehrerPersonaldaten {
	return obj as ValidatorLehrerPersonaldaten;
}
