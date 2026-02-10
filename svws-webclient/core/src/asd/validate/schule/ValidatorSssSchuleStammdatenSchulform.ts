import { ValidatorSss00SchuleStammdatenSchulform } from '../../../asd/validate/schule/ValidatorSss00SchuleStammdatenSchulform';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSssSchuleStammdatenSchulform extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Schulform
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSss00SchuleStammdatenSchulform(daten, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schule.ValidatorSssSchuleStammdatenSchulform';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schule.ValidatorSssSchuleStammdatenSchulform', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSssSchuleStammdatenSchulform>('de.svws_nrw.asd.validate.schule.ValidatorSssSchuleStammdatenSchulform');

}

export function cast_de_svws_nrw_asd_validate_schule_ValidatorSssSchuleStammdatenSchulform(obj: unknown): ValidatorSssSchuleStammdatenSchulform {
	return obj as ValidatorSssSchuleStammdatenSchulform;
}
