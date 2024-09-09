import { SchuleStammdaten } from '../../../asd/data/schule/SchuleStammdaten';
import { ValidatorSchuleStammdatenSchulform } from '../../../asd/validate/schule/ValidatorSchuleStammdatenSchulform';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSchuleStammdaten extends Validator<SchuleStammdaten> {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : SchuleStammdaten, kontext : ValidatorKontext) {
		super(daten, kontext);
		this._validatoren.add(new ValidatorSchuleStammdatenSchulform(daten, kontext));
	}

	protected pruefe() : boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schule.ValidatorSchuleStammdaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.schule.ValidatorSchuleStammdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorSchuleStammdaten>('de.svws_nrw.asd.validate.schule.ValidatorSchuleStammdaten');

}

export function cast_de_svws_nrw_asd_validate_schule_ValidatorSchuleStammdaten(obj : unknown) : ValidatorSchuleStammdaten {
	return obj as ValidatorSchuleStammdaten;
}
