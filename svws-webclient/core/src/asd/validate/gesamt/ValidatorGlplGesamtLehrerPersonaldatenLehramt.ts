import { ValidatorGlpl01GesamtLehrerPersonaldatenLehramt } from '../../../asd/validate/gesamt/ValidatorGlpl01GesamtLehrerPersonaldatenLehramt';
import { LehrerStatistikGesamt } from '../../../asd/data/statistik/LehrerStatistikGesamt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { ValidatorGlpl00GesamtLehrerPersonaldatenLehramt } from '../../../asd/validate/gesamt/ValidatorGlpl00GesamtLehrerPersonaldatenLehramt';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorGlplGesamtLehrerPersonaldatenLehramt extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param listLehrer          die Liste der Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext             der Kontext des Validators
	 */
	public constructor(listLehrer: Supplier<List<LehrerStatistikGesamt>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorGlpl00GesamtLehrerPersonaldatenLehramt(listLehrer, kontext));
		this._validatoren.add(new ValidatorGlpl01GesamtLehrerPersonaldatenLehramt(listLehrer, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.gesamt.ValidatorGlplGesamtLehrerPersonaldatenLehramt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.gesamt.ValidatorGlplGesamtLehrerPersonaldatenLehramt', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorGlplGesamtLehrerPersonaldatenLehramt>('de.svws_nrw.asd.validate.gesamt.ValidatorGlplGesamtLehrerPersonaldatenLehramt');

}

export function cast_de_svws_nrw_asd_validate_gesamt_ValidatorGlplGesamtLehrerPersonaldatenLehramt(obj: unknown): ValidatorGlplGesamtLehrerPersonaldatenLehramt {
	return obj as ValidatorGlplGesamtLehrerPersonaldatenLehramt;
}
