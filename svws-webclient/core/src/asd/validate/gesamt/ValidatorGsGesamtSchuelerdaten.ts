import { SchuelerStatistikGesamt } from '../../../asd/data/statistik/SchuelerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorGsdGesamtSchuelerdatenDuplikate } from '../../../asd/validate/gesamt/ValidatorGsdGesamtSchuelerdatenDuplikate';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGsGesamtSchuelerdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listSchueler        die Liste aller Schülerdaten
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(listSchueler: Supplier<List<SchuelerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorGsdGesamtSchuelerdatenDuplikate(listSchueler, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGsGesamtSchuelerdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.gesamt.ValidatorGsGesamtSchuelerdaten', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorGsGesamtSchuelerdaten>('de.svws_nrw.asd.validate.gesamt.ValidatorGsGesamtSchuelerdaten');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGsGesamtSchuelerdaten(obj: unknown): ValidatorGsGesamtSchuelerdaten {
	return obj as ValidatorGsGesamtSchuelerdaten;
}
