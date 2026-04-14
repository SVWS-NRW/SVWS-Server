import { SchuelerStatistikGesamt } from '../../../asd/data/statistik/SchuelerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { ValidatorGsd10GesamtSchuelerdatenDuplikate } from '../../../asd/validate/gesamt/ValidatorGsd10GesamtSchuelerdatenDuplikate';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGsdGesamtSchuelerdatenDuplikate extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listSchueler       die Liste aller Schülerstammdaten
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(listSchueler: Supplier<List<SchuelerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorGsd10GesamtSchuelerdatenDuplikate(listSchueler, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGsdGesamtSchuelerdatenDuplikate';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.gesamt.ValidatorGsdGesamtSchuelerdatenDuplikate', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorGsdGesamtSchuelerdatenDuplikate>('de.svws_nrw.asd.validate.gesamt.ValidatorGsdGesamtSchuelerdatenDuplikate');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGsdGesamtSchuelerdatenDuplikate(obj: unknown): ValidatorGsdGesamtSchuelerdatenDuplikate {
	return obj as ValidatorGsdGesamtSchuelerdatenDuplikate;
}
