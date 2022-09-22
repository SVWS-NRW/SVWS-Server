import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GELeistungsdifferenzierteKursart extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<GELeistungsdifferenzierteKursart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, GELeistungsdifferenzierteKursart> = new Map<String, GELeistungsdifferenzierteKursart>();

	public static readonly E : GELeistungsdifferenzierteKursart = new GELeistungsdifferenzierteKursart("E", 0, "E");

	public static readonly G : GELeistungsdifferenzierteKursart = new GELeistungsdifferenzierteKursart("G", 1, "G");

	public static readonly Sonstige : GELeistungsdifferenzierteKursart = new GELeistungsdifferenzierteKursart("Sonstige", 2, "");

	public readonly kuerzel : String;

	/**
	 * Erzeugt eine neue leistungsdifferenzierte Kursart.
	 * 
	 * @param kuerzel   das Kürzel für die leistungsdifferenzierte Kursart
	 */
	private constructor(name : string, ordinal : number, kuerzel : String) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
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
	public static from(kuerzel : String | null) : GELeistungsdifferenzierteKursart {
		switch (kuerzel) {
			case "E": 
				return GELeistungsdifferenzierteKursart.E;
			case "G": 
				return GELeistungsdifferenzierteKursart.G;
		}
		return GELeistungsdifferenzierteKursart.Sonstige;
	}

	/**
	 * Prüft, ob die leistungsdifferenzierte Kursart das übergeben Kürzel hat
	 * 
	 * @param kuerzel   das zu prüfende Kürzel
	 * 
	 * @return true, falls sie das Kürzel hat und ansonsten false
	 */
	public hat(kuerzel : String | null) : boolean {
		return JavaObject.equalsTranspiler(this.kuerzel, (GELeistungsdifferenzierteKursart.from(kuerzel).kuerzel));
	}

	/**
	 * Wandelt dieses Objekt in das zugehörige Kürzel um.
	 * 
	 * @return die Zeichenkette die zu dieser Kursart gehört
	 */
	public toString() : String {
		return this.kuerzel;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : String {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	private ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof GELeistungsdifferenzierteKursart))
			return false;
		return this === other;
	}

	/**
	 * Returns the ordinal value as hashcode, since the ordinal value is unique.
	 *
	 * @returns the ordinal value as hashcode
	 */
	public hashCode() : number {
		return this.__ordinal;
	}

	/**
	 * Compares this enumeration value with the other enumeration value by their ordinal value.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns a negative, zero or postive value as this enumeration value is less than, equal to
	 *          or greater than the other enumeration value
	 */
	public compareTo(other : GELeistungsdifferenzierteKursart) : number {
		return this.__ordinal - other.__ordinal;
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
	public static valueOf(name : String) : GELeistungsdifferenzierteKursart | null {
		let tmp : GELeistungsdifferenzierteKursart | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.ge.GELeistungsdifferenzierteKursart'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_ge_GELeistungsdifferenzierteKursart(obj : unknown) : GELeistungsdifferenzierteKursart {
	return obj as GELeistungsdifferenzierteKursart;
}
