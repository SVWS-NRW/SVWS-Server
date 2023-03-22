import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostBelegungsfehlerArt extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<GostBelegungsfehlerArt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, GostBelegungsfehlerArt> = new Map<string, GostBelegungsfehlerArt>();

	/**
	 * Belegungsfehler
	 */
	public static readonly BELEGUNG : GostBelegungsfehlerArt = new GostBelegungsfehlerArt("BELEGUNG", 0, "BELEGUNG");

	/**
	 * Fehler bei der Schriftlichkeit
	 */
	public static readonly SCHRIFTLICHKEIT : GostBelegungsfehlerArt = new GostBelegungsfehlerArt("SCHRIFTLICHKEIT", 1, "SCHRIFTLICHKEIT");

	/**
	 * Information, aber kein Fehler
	 */
	public static readonly HINWEIS : GostBelegungsfehlerArt = new GostBelegungsfehlerArt("HINWEIS", 2, "HINWEIS");

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
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		GostBelegungsfehlerArt.all_values_by_ordinal.push(this);
		GostBelegungsfehlerArt.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
	}

	/**
	 * Gibt die Belegungsfehler-Art anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Belegungsfehler-Art
	 *  
	 * @return die Belegungsfehler-Art
	 */
	public static fromKuerzel(kuerzel : string | null) : GostBelegungsfehlerArt | null {
		if (kuerzel === null) 
			return null;
		switch (kuerzel) {
			case "BELEGUNG": 
				return GostBelegungsfehlerArt.BELEGUNG;
			case "SCHRIFTLICHKEIT": 
				return GostBelegungsfehlerArt.SCHRIFTLICHKEIT;
			case "HINWEIS": 
				return GostBelegungsfehlerArt.HINWEIS;
		}
		return null;
	}

	public toString() : string {
		return this.kuerzel;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	private name() : string {
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
		if (!(other instanceof GostBelegungsfehlerArt))
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
	public compareTo(other : GostBelegungsfehlerArt) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostBelegungsfehlerArt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostBelegungsfehlerArt | null {
		let tmp : GostBelegungsfehlerArt | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.GostBelegungsfehlerArt'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegungsfehlerArt(obj : unknown) : GostBelegungsfehlerArt {
	return obj as GostBelegungsfehlerArt;
}
