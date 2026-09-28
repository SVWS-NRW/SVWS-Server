import { DateManager } from '../../../asd/validate/DateManager';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpa01LehrerPersonaldatenAbgangsdatum extends Validator {

	/**
	 * Das Abgangsdatum des Lehrers
	 */
	private readonly daten: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     das Abgangsdatum des Lehrers
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		let abgangsdatum: DateManager | null = null;
		let errorMsg: string = "";
		try {
			abgangsdatum = DateManager.from(this.daten.get());
		} catch (e: any) {
			errorMsg = e.getMessage();
		}
		const finalAbgangsdatum: DateManager | null = abgangsdatum;
		if (finalAbgangsdatum === null) {
			this.addFehler(0, "Abgangsdatum des Lehrers: Das Abgangsdatum ist ungültig. " + errorMsg);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpa01LehrerPersonaldatenAbgangsdatum';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpa01LehrerPersonaldatenAbgangsdatum', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpa01LehrerPersonaldatenAbgangsdatum>('de.svws_nrw.asd.validate.lehrer.ValidatorLpa01LehrerPersonaldatenAbgangsdatum');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpa01LehrerPersonaldatenAbgangsdatum(obj: unknown): ValidatorLpa01LehrerPersonaldatenAbgangsdatum {
	return obj as ValidatorLpa01LehrerPersonaldatenAbgangsdatum;
}
