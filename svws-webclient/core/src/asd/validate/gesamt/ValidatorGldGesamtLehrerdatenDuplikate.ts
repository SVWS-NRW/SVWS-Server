import { ValidatorGld02GesamtLehrerdatenDuplikate } from '../../../asd/validate/gesamt/ValidatorGld02GesamtLehrerdatenDuplikate';
import { ValidatorGld00GesamtLehrerdatenDuplikate } from '../../../asd/validate/gesamt/ValidatorGld00GesamtLehrerdatenDuplikate';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGldGesamtLehrerdatenDuplikate extends Validator {

	private readonly listStammdaten: List<LehrerStammdaten>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listStammdaten    		die Liste aller Lehrerstammdaten
	 * @param kontext             		der Kontext des Validators
	 */
	public constructor(listStammdaten: List<LehrerStammdaten>, kontext: ValidatorKontext) {
		super(kontext);
		this.listStammdaten = listStammdaten;
		this._validatoren.add(new ValidatorGld00GesamtLehrerdatenDuplikate(listStammdaten, kontext));
		this._validatoren.add(new ValidatorGld02GesamtLehrerdatenDuplikate(listStammdaten, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGldGesamtLehrerdatenDuplikate';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.gesamt.ValidatorGldGesamtLehrerdatenDuplikate', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorGldGesamtLehrerdatenDuplikate>('de.svws_nrw.asd.validate.gesamt.ValidatorGldGesamtLehrerdatenDuplikate');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGldGesamtLehrerdatenDuplikate(obj: unknown): ValidatorGldGesamtLehrerdatenDuplikate {
	return obj as ValidatorGldGesamtLehrerdatenDuplikate;
}
