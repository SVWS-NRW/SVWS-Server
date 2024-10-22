import { ValidatorLehrerPersonalabschnittdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLehrerPersonalabschnittdatenPflichtstundensoll';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLehrerPersonalabschnittdaten extends Validator<LehrerPersonalabschnittsdaten> {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : LehrerPersonalabschnittsdaten, kontext : ValidatorKontext) {
		super(daten, kontext);
		this._validatoren.add(new ValidatorLehrerPersonalabschnittdatenPflichtstundensoll(daten, kontext));
	}

	protected pruefe() : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerPersonalabschnittdaten>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittdaten');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerPersonalabschnittdaten(obj : unknown) : ValidatorLehrerPersonalabschnittdaten {
	return obj as ValidatorLehrerPersonalabschnittdaten;
}
