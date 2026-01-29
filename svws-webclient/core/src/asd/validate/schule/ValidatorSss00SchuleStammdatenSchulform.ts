import { ValidatorSss01SchuleStammdatenSchulform } from '../../../asd/validate/schule/ValidatorSss01SchuleStammdatenSchulform';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSss00SchuleStammdatenSchulform extends Validator {

	private readonly daten: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Schulform
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorSss01SchuleStammdatenSchulform(this.getNotNullSupplier(daten), kontext));
	}

	protected pruefe(): boolean {
		const schulformKrz: string | null = this.daten.get();
		if ((schulformKrz === null) || JavaString.isBlank(schulformKrz)) {
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

	public static readonly class = new Class<ValidatorSss00SchuleStammdatenSchulform>('de.svws_nrw.asd.validate.schule.ValidatorSss00SchuleStammdatenSchulform');

}

export function cast_de_svws_nrw_asd_validate_schule_ValidatorSss00SchuleStammdatenSchulform(obj: unknown): ValidatorSss00SchuleStammdatenSchulform {
	return obj as ValidatorSss00SchuleStammdatenSchulform;
}
