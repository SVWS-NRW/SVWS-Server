import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';

export class GostBesondereLernleistung extends JavaEnum<GostBesondereLernleistung> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostBesondereLernleistung> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostBesondereLernleistung> = new Map<string, GostBesondereLernleistung>();

	/**
	 * keine besondere Lernleistung = K
	 */
	public static readonly KEINE : GostBesondereLernleistung = new GostBesondereLernleistung("KEINE", 0, "K", "Keine");

	/**
	 * Projektkurs ist besondere Lernleistung = P
	 */
	public static readonly PROJEKTKURS : GostBesondereLernleistung = new GostBesondereLernleistung("PROJEKTKURS", 1, "P", "Projektkurs ist besondere Lernleistung");

	/**
	 * externe besondere Lernleistung = E
	 */
	public static readonly EXTERNE : GostBesondereLernleistung = new GostBesondereLernleistung("EXTERNE", 2, "E", "externe besondere Lernleistung");

	/**
	 * Das Kürzel für die Art der Besonderen Lernleistung
	 */
	public readonly kuerzel : string;

	/**
	 * Die textuelle Beschreibung der Art der Besonderen Lernleistung
	 */
	public readonly beschreibung : string;

	/**
	 * Erzeugt ein neues Objekt für die Aufzählung der Arten von Besonderen
	 * Lernleistungen.
	 *
	 * @param kuerzel        das Kürzel für die Art der Besonderen Lernleistung
	 * @param beschreibung   die textuelle Beschreibung der Art der Besonderen Lernleistung
	 */
	private constructor(name : string, ordinal : number, kuerzel : string, beschreibung : string) {
		super(name, ordinal);
		GostBesondereLernleistung.all_values_by_ordinal.push(this);
		GostBesondereLernleistung.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}

	/**
	 * Gibt die Art der Besonderen Lernleistung anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Art der Besonderen Lernleistung
	 *
	 * @return die Art der Besonderen Lernleistung
	 */
	public static fromKuerzel(kuerzel : string | null) : GostBesondereLernleistung {
		if (kuerzel === null)
			return GostBesondereLernleistung.KEINE;
		switch (kuerzel) {
			case "K": {
				return GostBesondereLernleistung.KEINE;
			}
			case "P": {
				return GostBesondereLernleistung.PROJEKTKURS;
			}
			case "E": {
				return GostBesondereLernleistung.EXTERNE;
			}
			default: {
				return GostBesondereLernleistung.KEINE;
			}
		}
	}

	public toString() : string {
		return this.kuerzel;
	}

	/**
	 * Prüft, ob diese Art der Besonderen Lernleistung mit der Art der c
	 * Lernleistung des übergebenen Kürzels übereinstimmt.
	 *
	 * @param kuerzel   das zu prüfende Kürzel der anderen Art der
	 *                  besonderen Lernleistung
	 *
	 * @return true, falls die Arten übereinstimmen und ansonsten false
	 */
	public is(kuerzel : string | null) : boolean {
		return JavaObject.equalsTranspiler(this.kuerzel, (kuerzel));
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostBesondereLernleistung> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostBesondereLernleistung | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.GostBesondereLernleistung', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_gost_GostBesondereLernleistung(obj : unknown) : GostBesondereLernleistung {
	return obj as GostBesondereLernleistung;
}
