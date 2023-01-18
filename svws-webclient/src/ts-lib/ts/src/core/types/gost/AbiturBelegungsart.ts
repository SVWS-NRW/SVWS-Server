import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class AbiturBelegungsart extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<AbiturBelegungsart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, AbiturBelegungsart> = new Map<String, AbiturBelegungsart>();

	/**
	 * AbiturBelegungsart "nicht belegt" 
	 */
	public static readonly NICHT_BELEGT : AbiturBelegungsart = new AbiturBelegungsart("NICHT_BELEGT", 0, "-", "nicht belegt");

	/**
	 * AbiturBelegungsart "mündlich" 
	 */
	public static readonly MUENDLICH : AbiturBelegungsart = new AbiturBelegungsart("MUENDLICH", 1, "M", "mündlich");

	/**
	 * AbiturBelegungsart "schriftlich" 
	 */
	public static readonly SCHRIFTLICH : AbiturBelegungsart = new AbiturBelegungsart("SCHRIFTLICH", 2, "S", "schriftlich");

	/**
	 * Das Kürzel für die Kurs-Belegungsart (-,M oder S)
	 */
	public kuerzel : String;

	/**
	 * Die textuelle Beschreibung der Kurs-Belegungsart (nicht belegt, mündlich oder schriftlich) 
	 */
	public beschreibung : String;

	/**
	 * Erzeugt ein neues Abitur-Belegungsart-Objekt
	 * 
	 * @param kuerzel        das der Kurs-Belegungsart
	 * @param beschreibung   die textuelle Beschreibung der Kurs-Belegungsart
	 */
	private constructor(name : string, ordinal : number, kuerzel : String, beschreibung : String) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		AbiturBelegungsart.all_values_by_ordinal.push(this);
		AbiturBelegungsart.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}

	/**
	 * Gibt die Kurs-Belegungsart anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Kurs-Belegungsart
	 *  
	 * @return die Kurs-Belegungsart oder null, falls das kuerzel fehlerhaft ist
	 */
	public static fromKuerzel(kuerzel : String | null) : AbiturBelegungsart | null {
		if (kuerzel === null) 
			return AbiturBelegungsart.NICHT_BELEGT;
		switch (kuerzel) {
			case "-": 
				return AbiturBelegungsart.NICHT_BELEGT;
			case "M": 
				return AbiturBelegungsart.MUENDLICH;
			case "S": 
				return AbiturBelegungsart.SCHRIFTLICH;
		}
		return null;
	}

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
		if (!(other instanceof AbiturBelegungsart))
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
	public compareTo(other : AbiturBelegungsart) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<AbiturBelegungsart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : AbiturBelegungsart | null {
		let tmp : AbiturBelegungsart | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.gost.AbiturBelegungsart'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_gost_AbiturBelegungsart(obj : unknown) : AbiturBelegungsart {
	return obj as AbiturBelegungsart;
}
