import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLsk10LehrerStammdatenKuerzel extends Validator {

	/**
	 * Die Lehrer-Stammdaten
	 */
	private readonly daten: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: Supplier<string | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		const kuerzel: string | null = this.daten.get();
		if ((kuerzel === null) || JavaString.isBlank(kuerzel.trim()))
			return true;
		let fehlertext0: string | null = "Der Eintrag " + kuerzel + " ist als Lehrerkürzel unzulässig. Zulässig sind: 1. Stelle: A-Z, Ä, Ö, Ü; 2.-4. Stelle: A-Z, Ä, Ö, Ü, -, 'kein Eintrag'. Buchstaben müssen großgeschrieben werden.";
		if (!JavaString.matches(kuerzel, "^[A-ZÄÖÜ][A-ZÄÖÜ0-9\\-\\ ]{0,3}$")) {
			this.addFehler(0, fehlertext0);
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLsk10LehrerStammdatenKuerzel';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLsk10LehrerStammdatenKuerzel', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLsk10LehrerStammdatenKuerzel>('de.svws_nrw.asd.validate.lehrer.ValidatorLsk10LehrerStammdatenKuerzel');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLsk10LehrerStammdatenKuerzel(obj: unknown): ValidatorLsk10LehrerStammdatenKuerzel {
	return obj as ValidatorLsk10LehrerStammdatenKuerzel;
}
