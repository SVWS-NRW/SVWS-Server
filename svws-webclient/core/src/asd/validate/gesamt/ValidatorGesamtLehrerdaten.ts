import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGesamtLehrerdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listStammdaten      die Liste aller Lehrerstammdaten
	 * @param listPersonaldaten   die Liste aller Lehrerpersonaldaten
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(listStammdaten : List<LehrerStammdaten>, listPersonaldaten : List<LehrerPersonaldaten>, kontext : ValidatorKontext) {
		super(kontext);
	}

	protected pruefe() : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGesamtLehrerdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.gesamt.ValidatorGesamtLehrerdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorGesamtLehrerdaten>('de.svws_nrw.asd.validate.gesamt.ValidatorGesamtLehrerdaten');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGesamtLehrerdaten(obj : unknown) : ValidatorGesamtLehrerdaten {
	return obj as ValidatorGesamtLehrerdaten;
}
