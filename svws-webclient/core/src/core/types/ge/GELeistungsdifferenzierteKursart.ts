import { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';

export class GELeistungsdifferenzierteKursart extends JavaEnum<GELeistungsdifferenzierteKursart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GELeistungsdifferenzierteKursart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GELeistungsdifferenzierteKursart> = new Map<string, GELeistungsdifferenzierteKursart>();

	/**
	 * Es handelt sich um einen leistungsdifferenzierten Kurs auf Erweiterungsebene (E-Kurs)
	 */
	public static readonly E : GELeistungsdifferenzierteKursart = new GELeistungsdifferenzierteKursart("E", 0, "E");

	/**
	 * Es handelt sich um einen leistungsdifferenzierten Kurs auf Grundebene (G-Kurs)
	 */
	public static readonly G : GELeistungsdifferenzierteKursart = new GELeistungsdifferenzierteKursart("G", 1, "G");

	/**
	 * Es handelt sich um einen sonstigen Kurs ohne Leistungsdifferenzierung
	 */
	public static readonly Sonstige : GELeistungsdifferenzierteKursart = new GELeistungsdifferenzierteKursart("Sonstige", 2, "");

	/**
	 * Das Kürzel der leistungsdifferenzierten Kursart
	 */
	public readonly kuerzel : string;

	/**
	 * Erzeugt eine neue leistungsdifferenzierte Kursart.
	 *
	 * @param kuerzel   das Kürzel für die leistungsdifferenzierte Kursart
	 */
	private constructor(name : string, ordinal : number, kuerzel : string) {
		super(name, ordinal);
		GELeistungsdifferenzierteKursart.all_values_by_ordinal.push(this);
		GELeistungsdifferenzierteKursart.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
	}

	/**
	 * Wandelt den übergebenen String, in einen Objekt dieser Aufzählung um.
	 * Dabei wird "E" als E-Kurs und "G" als G-Kurs interpretiert. Alles andere
	 * wird als sonstiger Kurs interpretiert.
	 *
	 * @param kuerzel   die Zeichenkette, die als Kursart interpretiert werden soll
	 *
	 * @return das resultierende Objekt dieser Aufzählung
	 */
	public static from(kuerzel : string | null) : GELeistungsdifferenzierteKursart {
		switch (kuerzel) {
			case "E": {
				return GELeistungsdifferenzierteKursart.E;
			}
			case "G": {
				return GELeistungsdifferenzierteKursart.G;
			}
			default: {
				return GELeistungsdifferenzierteKursart.Sonstige;
			}
		}
	}

	/**
	 * Prüft, ob die leistungsdifferenzierte Kursart das übergeben Kürzel hat
	 *
	 * @param kuerzel   das zu prüfende Kürzel
	 *
	 * @return true, falls sie das Kürzel hat und ansonsten false
	 */
	public hat(kuerzel : string | null) : boolean {
		return JavaObject.equalsTranspiler(this.kuerzel, (GELeistungsdifferenzierteKursart.from(kuerzel).kuerzel));
	}

	/**
	 * Wandelt dieses Objekt in das zugehörige Kürzel um.
	 *
	 * @return die Zeichenkette die zu dieser Kursart gehört
	 */
	public toString() : string {
		return this.kuerzel;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GELeistungsdifferenzierteKursart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GELeistungsdifferenzierteKursart | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.ge.GELeistungsdifferenzierteKursart';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.ge.GELeistungsdifferenzierteKursart', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_ge_GELeistungsdifferenzierteKursart(obj : unknown) : GELeistungsdifferenzierteKursart {
	return obj as GELeistungsdifferenzierteKursart;
}
