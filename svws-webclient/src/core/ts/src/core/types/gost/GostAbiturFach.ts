import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaInteger, cast_java_lang_Integer } from '../../../java/lang/JavaInteger';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostAbiturFach extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<GostAbiturFach> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, GostAbiturFach> = new Map<string, GostAbiturFach>();

	/**
	 * 1. Leistungskurs = LK1
	 */
	public static readonly LK1 : GostAbiturFach = new GostAbiturFach("LK1", 0, 1, "LK1", "1. Leistungskurs");

	/**
	 * 2. Leistungskurs = LK2
	 */
	public static readonly LK2 : GostAbiturFach = new GostAbiturFach("LK2", 1, 2, "LK2", "2. Leistungskurs");

	/**
	 * 3. Abiturfach (GK, schriftlich in der Abiturprüfung) = AB3
	 */
	public static readonly AB3 : GostAbiturFach = new GostAbiturFach("AB3", 2, 3, "AB3", "3. Abiturfach (GK, schriftlich)");

	/**
	 * 4. Abiturfach (GK, mündlich in der Abiturprüfung) = AB4
	 */
	public static readonly AB4 : GostAbiturFach = new GostAbiturFach("AB4", 3, 4, "AB4", "4. Abiturfach (GK, mündlich)");

	/**
	 * Die ID bzw. Nummer der Abiturfachart (1-4)
	 */
	public readonly id : number;

	/**
	 * Das Kürzel der Abiturfachart, welches auch in speziellen Kursarten genutzt wird.
	 */
	public readonly kuerzel : string;

	/**
	 * Die textuelle Beschreibung der Abiturfachart.
	 */
	public readonly beschreibung : string;

	/**
	 * Erstellt eine Abiturfachart für diese Aufzählung der Abiturfacharten.
	 * 
	 * @param id             die ID bzw. Nummer der Abiturfachart
	 * @param kuerzel        das Kürzel der Abiturfachart, welches auch in speziellen Kursarten genutzt wird
	 * @param beschreibung   die textuelle Beschreibung der Abiturfachart
	 */
	private constructor(name : string, ordinal : number, id : number, kuerzel : string, beschreibung : string) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		GostAbiturFach.all_values_by_ordinal.push(this);
		GostAbiturFach.all_values_by_name.set(name, this);
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}

	/**
	 *
	 * Gibt die Abiturfachart anhand der ID zurück. 
	 * 
	 * @param id    die ID der Abiturfachart
	 * 
	 * @return die Abiturfachart oder null falls die ID ungültig ist
	 * 
	 */
	public static fromID(id : number | null) : GostAbiturFach | null {
		if (id === null) 
			return null;
		switch (id) {
			case 1: 
				return GostAbiturFach.LK1;
			case 2: 
				return GostAbiturFach.LK2;
			case 3: 
				return GostAbiturFach.AB3;
			case 4: 
				return GostAbiturFach.AB4;
		}
		return null;
	}

	/**
	 *
	 * Gibt die Abiturfachart anhand der ID (als String) zurück. 
	 * 
	 * @param strID    die ID der Abiturfachart (als String) 
	 * 
	 * @return die Abiturfachart oder null, falls die ID ungültig ist
	 * 
	 */
	public static fromIDString(strID : string | null) : GostAbiturFach | null {
		if (strID === null) 
			return null;
		switch (strID) {
			case "1": 
				return GostAbiturFach.LK1;
			case "2": 
				return GostAbiturFach.LK2;
			case "3": 
				return GostAbiturFach.AB3;
			case "4": 
				return GostAbiturFach.AB4;
		}
		return null;
	}

	/**
	 *
	 * Gibt die Abiturfachart anhand des Kürzels zurück. 
	 * 
	 * @param kuerzel    das Kürzel der Abiturfachart
	 * 
	 * @return die Abiturfachart oder null, falls das Kürzel ungültig ist
	 * 
	 */
	public static fromKuerzel(kuerzel : string | null) : GostAbiturFach | null {
		switch (kuerzel) {
			case "LK1": 
				return GostAbiturFach.LK1;
			case "LK2": 
				return GostAbiturFach.LK2;
			case "AB3": 
				return GostAbiturFach.AB3;
			case "AB4": 
				return GostAbiturFach.AB4;
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
		if (!(other instanceof GostAbiturFach))
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
	public compareTo(other : GostAbiturFach) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostAbiturFach> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostAbiturFach | null {
		let tmp : GostAbiturFach | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.gost.GostAbiturFach'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_gost_GostAbiturFach(obj : unknown) : GostAbiturFach {
	return obj as GostAbiturFach;
}
