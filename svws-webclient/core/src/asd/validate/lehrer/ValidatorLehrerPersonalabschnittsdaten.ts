import { DateManager } from '../../../asd/validate/DateManager';
import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll';

export class ValidatorLehrerPersonalabschnittsdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten        die Daten des Validators, hier die Personalabschnittsdaten des Lehrers
	 * @param stammdaten   die Stammdaten des Lehrers
	 * @param kontext      der Kontext des Validators
	 */
	public constructor(daten : LehrerPersonalabschnittsdaten, stammdaten : LehrerStammdaten, kontext : ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLehrerPersonalabschnittsdatenPflichtstundensoll(daten, kontext));
		try {
			const geburtsdatum : DateManager = DateManager.from(stammdaten.geburtsdatum);
			this._validatoren.add(new ValidatorLehrerPersonalabschnittsdatenRechtsverhaeltnis(daten, geburtsdatum, kontext));
		} catch(e : any) {
			// empty block
		}
	}

	protected pruefe() : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittsdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittsdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLehrerPersonalabschnittsdaten>('de.svws_nrw.asd.validate.lehrer.ValidatorLehrerPersonalabschnittsdaten');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLehrerPersonalabschnittsdaten(obj : unknown) : ValidatorLehrerPersonalabschnittsdaten {
	return obj as ValidatorLehrerPersonalabschnittsdaten;
}
