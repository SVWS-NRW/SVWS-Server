import { ValidatorLsk10LehrerStammdatenKuerzel } from '../../../asd/validate/lehrer/ValidatorLsk10LehrerStammdatenKuerzel';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsk00LehrerStammdatenKuerzel extends Validator {

	/**
	 * Das Geburtsdatum des Lehrers
	 */
	private readonly daten: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Geburtsdatum des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
		this._validatoren.add(new ValidatorLsk10LehrerStammdatenKuerzel(this.getNotNullSupplier(daten), kontext));
	}

	protected pruefe(): boolean {
		const kuerzel: string | null = this.daten.get();
		if ((kuerzel === null) || (JavaString.isEmpty(kuerzel))) {
			this.addFehler(0, "Das Feld 'Kürzel' muss besetzt sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsk00LehrerStammdatenKuerzel';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsk00LehrerStammdatenKuerzel', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsk00LehrerStammdatenKuerzel>('de.svws_nrw.asd.validate.lehrer.ValidatorLsk00LehrerStammdatenKuerzel');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsk00LehrerStammdatenKuerzel(obj: unknown): ValidatorLsk00LehrerStammdatenKuerzel {
	return obj as ValidatorLsk00LehrerStammdatenKuerzel;
}
