import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGesamtLehrerdatenDuplikate extends Validator {

	private readonly listStammdaten : List<LehrerStammdaten>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listStammdaten      die Liste aller Lehrerstammdaten
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(listStammdaten : List<LehrerStammdaten>, kontext : ValidatorKontext) {
		super(kontext);
		this.listStammdaten = listStammdaten;
	}

	protected pruefe() : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGesamtLehrerdatenDuplikate';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.gesamt.ValidatorGesamtLehrerdatenDuplikate', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorGesamtLehrerdatenDuplikate>('de.svws_nrw.asd.validate.gesamt.ValidatorGesamtLehrerdatenDuplikate');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGesamtLehrerdatenDuplikate(obj : unknown) : ValidatorGesamtLehrerdatenDuplikate {
	return obj as ValidatorGesamtLehrerdatenDuplikate;
}
