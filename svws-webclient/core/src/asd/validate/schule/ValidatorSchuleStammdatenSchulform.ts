import { SchuleStammdaten } from '../../../asd/data/schule/SchuleStammdaten';
import { Schulform } from '../../../asd/types/schule/Schulform';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSchuleStammdatenSchulform extends Validator<SchuleStammdaten> {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten : SchuleStammdaten, kontext : ValidatorKontext) {
		super(daten, kontext);
	}

	protected pruefe() : boolean {
		const schulformKrz : string | null = this.daten().schulform;
		if ((schulformKrz === null) || (JavaString.isBlank(schulformKrz))) {
			this.addFehler("Die Schulform muss gesetzt sein.");
			return false;
		}
		try {
			Schulform.data().getWertByKuerzel(schulformKrz);
			return true;
		} catch(e : any) {
			this.addFehler(e.getMessage());
			return false;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schule.ValidatorSchuleStammdatenSchulform';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.asd.validate.schule.ValidatorSchuleStammdatenSchulform', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorSchuleStammdatenSchulform>('de.svws_nrw.asd.validate.schule.ValidatorSchuleStammdatenSchulform');

}

export function cast_de_svws_nrw_asd_validate_schule_ValidatorSchuleStammdatenSchulform(obj : unknown) : ValidatorSchuleStammdatenSchulform {
	return obj as ValidatorSchuleStammdatenSchulform;
}
