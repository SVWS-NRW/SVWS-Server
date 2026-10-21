import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKa11KlassenAbkuerzung extends Validator {

	/**
	 * das Kürzel der Klasse
	 */
	private readonly kuerzel: Supplier<string>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kuerzel     das Kürzel der Klasse
	 * @param kontext     der Kontext des Validators
	 */
	public constructor(kuerzel: Supplier<string>, kontext: ValidatorKontext) {
		super(kontext);
		this.kuerzel = kuerzel;
	}

	protected pruefe(): boolean {
		if (this.kuerzel.get().length > 15) {
			this.addFehler(0, "Kürzel der Klasse: Das eingetragene Kürzel darf maximal 15 Stellen besitzen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKa11KlassenAbkuerzung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKa11KlassenAbkuerzung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKa11KlassenAbkuerzung>('de.svws_nrw.asd.validate.klassen.ValidatorKa11KlassenAbkuerzung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKa11KlassenAbkuerzung(obj: unknown): ValidatorKa11KlassenAbkuerzung {
	return obj as ValidatorKa11KlassenAbkuerzung;
}
