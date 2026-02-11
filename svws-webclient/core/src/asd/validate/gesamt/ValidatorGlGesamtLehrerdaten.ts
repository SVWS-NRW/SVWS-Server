import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { LehrerStammdaten } from '../../../asd/data/lehrer/LehrerStammdaten';
import { ValidatorGldGesamtLehrerdatenDuplikate } from '../../../asd/validate/gesamt/ValidatorGldGesamtLehrerdatenDuplikate';
import type { List } from '../../../java/util/List';
import { ValidatorGlplGesamtLehrerPersonaldatenLehramt } from '../../../asd/validate/gesamt/ValidatorGlplGesamtLehrerPersonaldatenLehramt';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGlGesamtLehrerdaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listStammdaten      die Liste aller Lehrerstammdaten
	 * @param listPersonaldaten   die Liste aller Lehrerpersonaldaten
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(listStammdaten: List<LehrerStammdaten>, listPersonaldaten: List<LehrerPersonaldaten>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorGldGesamtLehrerdatenDuplikate(listStammdaten, kontext));
		this._validatoren.add(new ValidatorGlplGesamtLehrerPersonaldatenLehramt(listPersonaldaten, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGlGesamtLehrerdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.gesamt.ValidatorGlGesamtLehrerdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorGlGesamtLehrerdaten>('de.svws_nrw.asd.validate.gesamt.ValidatorGlGesamtLehrerdaten');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGlGesamtLehrerdaten(obj: unknown): ValidatorGlGesamtLehrerdaten {
	return obj as ValidatorGlGesamtLehrerdaten;
}
