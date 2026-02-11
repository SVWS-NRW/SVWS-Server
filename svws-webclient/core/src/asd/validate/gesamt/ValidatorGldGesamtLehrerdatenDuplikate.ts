import { ValidatorGld11GesamtLehrerdatenDuplikate } from '../../../asd/validate/gesamt/ValidatorGld11GesamtLehrerdatenDuplikate';
import { ValidatorGld10GesamtLehrerdatenDuplikate } from '../../../asd/validate/gesamt/ValidatorGld10GesamtLehrerdatenDuplikate';
import { LehrerStatistikGesamt } from '../../../asd/data/statistik/LehrerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGldGesamtLehrerdatenDuplikate extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrer       die Liste aller Lehrerstammdaten
	 * @param kontext          der Kontext des Validators
	 */
	public constructor(listLehrer: Supplier<List<LehrerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorGld10GesamtLehrerdatenDuplikate(listLehrer, kontext));
		this._validatoren.add(new ValidatorGld11GesamtLehrerdatenDuplikate(listLehrer, kontext));
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

	public static readonly class = new Class<ValidatorGldGesamtLehrerdatenDuplikate>('de.svws_nrw.asd.validate.gesamt.ValidatorGldGesamtLehrerdatenDuplikate');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGldGesamtLehrerdatenDuplikate(obj: unknown): ValidatorGldGesamtLehrerdatenDuplikate {
	return obj as ValidatorGldGesamtLehrerdatenDuplikate;
}
