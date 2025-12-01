import { Schulform } from '../../../asd/types/schule/Schulform';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSss01SchuleStammdatenSchulform extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(kontext: ValidatorKontext) {
		super(kontext);
	}

	protected pruefe(): boolean {
		const schulformKrz: string | null = super.kontext().getSchuleStammdaten().schulform;
		try {
			return Schulform.data().getWertByKuerzel(schulformKrz) === null;
		} catch(e : any) {
			this.addFehler(1, "Das Kürzel für die Schulform ist ungültig.");
			return false;
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schule.ValidatorSss01SchuleStammdatenSchulform';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schule.ValidatorSss01SchuleStammdatenSchulform', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSss01SchuleStammdatenSchulform>('de.svws_nrw.asd.validate.schule.ValidatorSss01SchuleStammdatenSchulform');

}

export function cast_de_svws_nrw_asd_validate_schule_ValidatorSss01SchuleStammdatenSchulform(obj: unknown): ValidatorSss01SchuleStammdatenSchulform {
	return obj as ValidatorSss01SchuleStammdatenSchulform;
}
