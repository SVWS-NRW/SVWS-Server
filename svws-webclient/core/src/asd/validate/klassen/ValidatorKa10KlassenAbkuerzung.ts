import { JavaObject } from '../../../java/lang/JavaObject';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorKa10KlassenAbkuerzung extends Validator {

	/**
	 * das Kürzel der Klasse
	 */
	private readonly kuerzel: Supplier<string>;

	/**
	 * eine Liste der Kürzel der Klassen der Schule
	 */
	private readonly listKuerzel: Supplier<List<string> | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param kuerzel     das Kürzel der Klasse
	 * @param listKuerzel eine Liste der Kürzel der Klassen der Schule
	 * @param kontext     der Kontext des Validators
	 */
	public constructor(kuerzel: Supplier<string>, listKuerzel: Supplier<List<string> | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.kuerzel = kuerzel;
		this.listKuerzel = listKuerzel;
	}

	protected pruefe(): boolean {
		let anzahl: number = 0;
		let listeKuerzelString: List<string> | null = this.listKuerzel.get();
		let zuPruefendesKuerzel: string | null = this.kuerzel.get();
		if (listeKuerzelString !== null && !listeKuerzelString.isEmpty()) {
			for (let kuerzelAusListe of listeKuerzelString) {
				if (JavaObject.equalsTranspiler(zuPruefendesKuerzel, (kuerzelAusListe))) {
					anzahl = anzahl + 1;
				}
			}
			if (anzahl > 1) {
				this.addFehler(0, "Kürzel der Klasse: Das eingetragene Kürzel kommt mehrfach vor.");
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.klassen.ValidatorKa10KlassenAbkuerzung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.klassen.ValidatorKa10KlassenAbkuerzung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorKa10KlassenAbkuerzung>('de.svws_nrw.asd.validate.klassen.ValidatorKa10KlassenAbkuerzung');

}

export function cast_de_svws_nrw_asd_validate_klassen_ValidatorKa10KlassenAbkuerzung(obj: unknown): ValidatorKa10KlassenAbkuerzung {
	return obj as ValidatorKa10KlassenAbkuerzung;
}
