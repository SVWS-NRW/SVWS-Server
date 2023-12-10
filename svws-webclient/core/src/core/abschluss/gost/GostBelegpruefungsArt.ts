import { JavaEnum } from '../../../java/lang/JavaEnum';

export class GostBelegpruefungsArt extends JavaEnum<GostBelegpruefungsArt> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostBelegpruefungsArt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostBelegpruefungsArt> = new Map<string, GostBelegpruefungsArt>();

	/**
	 * Prüfung nur der EF.1
	 */
	public static readonly EF1 : GostBelegpruefungsArt = new GostBelegpruefungsArt("EF1", 0, "EF.1", "nur EF.1");

	/**
	 * Gesamtprüfung über die gesamte Oberstufe
	 */
	public static readonly GESAMT : GostBelegpruefungsArt = new GostBelegpruefungsArt("GESAMT", 1, "Gesamt", "die gesamte Oberstufe");

	/**
	 * Das Kürzel für die Belegprüfungsart
	 */
	public readonly kuerzel : string;

	/**
	 * Eine textuelle Beschreibung für die Art der Belegprüfung
	 */
	public readonly beschreibung : string;

	/**
	 * Erzeugt ein neues Abitur-Belegungsart-Objekt
	 *
	 * @param kuerzel        das der Kurs-Belegungsart
	 * @param beschreibung   die textuelle Beschreibung der Kurs-Belegungsart
	 */
	private constructor(name : string, ordinal : number, kuerzel : string, beschreibung : string) {
		super(name, ordinal);
		GostBelegpruefungsArt.all_values_by_ordinal.push(this);
		GostBelegpruefungsArt.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}

	/**
	 * Gibt die Art der Belegprüfung anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Art der Belegprüfung
	 *
	 * @return die Art der Belegprüfung
	 */
	public static fromKuerzel(kuerzel : string | null) : GostBelegpruefungsArt | null {
		if (kuerzel === null)
			return null;
		switch (kuerzel) {
			case "EF.1": {
				return GostBelegpruefungsArt.EF1;
			}
			case "Gesamt": {
				return GostBelegpruefungsArt.GESAMT;
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
	public static values() : Array<GostBelegpruefungsArt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostBelegpruefungsArt | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_gost_GostBelegpruefungsArt(obj : unknown) : GostBelegpruefungsArt {
	return obj as GostBelegpruefungsArt;
}
