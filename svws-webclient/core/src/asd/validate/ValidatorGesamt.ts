import { ValidatorSchuleStammdaten } from '../../asd/validate/schule/ValidatorSchuleStammdaten';
import { Class } from '../../java/lang/Class';
import { ValidatorKontext } from '../../asd/validate/ValidatorKontext';
import { Validator, cast_de_svws_nrw_asd_validate_Validator } from '../../asd/validate/Validator';
import { SchuleStatistikdatenGesamt } from '../../asd/data/schule/SchuleStatistikdatenGesamt';

export class ValidatorGesamt extends Validator {

	private readonly daten : SchuleStatistikdatenGesamt;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : SchuleStatistikdatenGesamt, kontext : ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorSchuleStammdaten(kontext));
	}

	protected pruefe() : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.ValidatorGesamt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.ValidatorGesamt', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorGesamt>('de.svws_nrw.asd.validate.ValidatorGesamt');

}

export function cast_de_svws_nrw_asd_validate_ValidatorGesamt(obj : unknown) : ValidatorGesamt {
	return obj as ValidatorGesamt;
}
