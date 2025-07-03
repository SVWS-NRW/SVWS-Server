import { JavaEnum } from '../../../../java/lang/JavaEnum';
import { Class } from '../../../../java/lang/Class';

export class BKGymBelegungsfehlerArt extends JavaEnum<BKGymBelegungsfehlerArt> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<BKGymBelegungsfehlerArt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, BKGymBelegungsfehlerArt> = new Map<string, BKGymBelegungsfehlerArt>();

	/**
	 * Belegungsfehler
	 */
	public static readonly BELEGUNG : BKGymBelegungsfehlerArt = new BKGymBelegungsfehlerArt("BELEGUNG", 0, "BELEGUNG");

	/**
	 * Fehler bei der Schriftlichkeit
	 */
	public static readonly SCHRIFTLICHKEIT : BKGymBelegungsfehlerArt = new BKGymBelegungsfehlerArt("SCHRIFTLICHKEIT", 1, "SCHRIFTLICHKEIT");

	/**
	 * Fehler in Bezug auf schulische Rahmenbedingungen - Zusatzkursbeginn oder nicht erlaubte oder geforderte Fachkombinationen
	 */
	public static readonly SCHULSPEZIFISCH : BKGymBelegungsfehlerArt = new BKGymBelegungsfehlerArt("SCHULSPEZIFISCH", 2, "SCHULSPEZIFISCH");

	/**
	 * Information, aber kein Fehler
	 */
	public static readonly HINWEIS : BKGymBelegungsfehlerArt = new BKGymBelegungsfehlerArt("HINWEIS", 3, "HINWEIS");

	/**
	 * Das Kürzel für die Belegungsfehlerart
	 */
	public readonly kuerzel : string;

	/**
	 * Erzeugt ein neues Abitur-Belegungsfehler-Objekt
	 *
	 * @param kuerzel        das Kürzel der Fehler-Art
	 */
	private constructor(name : string, ordinal : number, kuerzel : string) {
		super(name, ordinal);
		BKGymBelegungsfehlerArt.all_values_by_ordinal.push(this);
		BKGymBelegungsfehlerArt.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
	}

	/**
	 * Gibt die Belegungsfehler-Art anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Belegungsfehler-Art
	 *
	 * @return die Belegungsfehler-Art
	 */
	public static fromKuerzel(kuerzel : string | null) : BKGymBelegungsfehlerArt | null {
		if (kuerzel === null)
			return null;
		switch (kuerzel) {
			case "BELEGUNG": {
				return BKGymBelegungsfehlerArt.BELEGUNG;
			}
			case "SCHRIFTLICHKEIT": {
				return BKGymBelegungsfehlerArt.SCHRIFTLICHKEIT;
			}
			case "SCHULSPEZIFISCH": {
				return BKGymBelegungsfehlerArt.SCHULSPEZIFISCH;
			}
			case "HINWEIS": {
				return BKGymBelegungsfehlerArt.HINWEIS;
			}
			default: {
				return null;
			}
		}
	}

	public toString() : string {
		return this.kuerzel;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<BKGymBelegungsfehlerArt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : BKGymBelegungsfehlerArt | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegungsfehlerArt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymBelegungsfehlerArt', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

	public static class = new Class<BKGymBelegungsfehlerArt>('de.svws_nrw.core.abschluss.bk.d.BKGymBelegungsfehlerArt');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegungsfehlerArt(obj : unknown) : BKGymBelegungsfehlerArt {
	return obj as BKGymBelegungsfehlerArt;
}
