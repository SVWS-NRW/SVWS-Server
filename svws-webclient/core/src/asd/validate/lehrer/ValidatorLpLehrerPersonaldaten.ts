import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { ValidatorLplLehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLplLehrerPersonaldatenLehramt';
import { DateManager } from '../../../asd/validate/DateManager';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten } from '../../../asd/validate/lehrer/ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpLehrerPersonaldaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     	die Daten des Validators
	 * @param stammdaten	die Stammdaten des Lehrers
	 * @param kontext   	der Kontext des Validators
	 */
	public constructor(daten: LehrerPersonaldaten, stammdaten: LehrerStammdaten, kontext: ValidatorKontext) {
		super(kontext);
		for (const abschnittsdaten of daten.abschnittsdaten)
			this._validatoren.add(new ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(abschnittsdaten, stammdaten, kontext));
		try {
			this._validatoren.add(new ValidatorLplLehrerPersonaldatenLehramt(daten, DateManager.from(stammdaten.geburtsdatum), kontext));
		} catch(e : any) {
			e.printStackTrace();
		}
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpLehrerPersonaldaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpLehrerPersonaldaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLpLehrerPersonaldaten>('de.svws_nrw.asd.validate.lehrer.ValidatorLpLehrerPersonaldaten');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpLehrerPersonaldaten(obj: unknown): ValidatorLpLehrerPersonaldaten {
	return obj as ValidatorLpLehrerPersonaldaten;
}
