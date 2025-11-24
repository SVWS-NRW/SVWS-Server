import { ValidatorSss01SchuleStammdatenSchulform } from '../../../asd/validate/schule/ValidatorSss01SchuleStammdatenSchulform';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSss00SchuleStammdatenSchulform extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSss01SchuleStammdatenSchulform(kontext));
	}

	protected pruefe(): boolean {
		const schulformKrz: string | null = super.kontext().getSchuleStammdaten().schulform;
		if (schulformKrz === null || JavaString.isBlank(schulformKrz)) {
			this.addFehler(0, "Die Schulform muss gesetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schule.ValidatorSss00SchuleStammdatenSchulform';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schule.ValidatorSss00SchuleStammdatenSchulform', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorSss00SchuleStammdatenSchulform>('de.svws_nrw.asd.validate.schule.ValidatorSss00SchuleStammdatenSchulform');

}

export function cast_de_svws_nrw_asd_validate_schule_ValidatorSss00SchuleStammdatenSchulform(obj: unknown): ValidatorSss00SchuleStammdatenSchulform {
	return obj as ValidatorSss00SchuleStammdatenSchulform;
}
