import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';

export class DQR extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : String;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<DQR> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<String, DQR> = new Map<String, DQR>();

	public static readonly NIVEAU_1 : DQR = new DQR("NIVEAU_1", 0, "DQR Niveau 1", "Niveau 1 des Deutschen Qualifikationsrahmens (DQR)");

	public static readonly NIVEAU_2 : DQR = new DQR("NIVEAU_2", 1, "DQR Niveau 2", "Niveau 2 des Deutschen Qualifikationsrahmens (DQR)");

	public static readonly NIVEAU_3 : DQR = new DQR("NIVEAU_3", 2, "DQR Niveau 3", "Niveau 3 des Deutschen Qualifikationsrahmens (DQR)");

	public static readonly NIVEAU_4 : DQR = new DQR("NIVEAU_4", 3, "DQR Niveau 4", "Niveau 4 des Deutschen Qualifikationsrahmens (DQR)");

	public static readonly NIVEAU_5 : DQR = new DQR("NIVEAU_5", 4, "DQR Niveau 5", "Niveau 5 des Deutschen Qualifikationsrahmens (DQR)");

	public static readonly NIVEAU_6 : DQR = new DQR("NIVEAU_6", 5, "DQR Niveau 6", "Niveau 6 des Deutschen Qualifikationsrahmens (DQR)");

	public static readonly NIVEAU_7 : DQR = new DQR("NIVEAU_7", 6, "DQR Niveau 7", "Niveau 7 des Deutschen Qualifikationsrahmens (DQR)");

	public static readonly NIVEAU_8 : DQR = new DQR("NIVEAU_8", 7, "DQR Niveau 8", "Niveau 8 des Deutschen Qualifikationsrahmens (DQR)");

	public readonly bezeichnung : String | null;

	public readonly beschreibung : String | null;

	/**
	 * Erstell ein neues DQR-Niveau.
	 * 
	 * @param bezeichnung    die Bezeichnung des Niveaus
	 * @param beschreibung   eine kurze Beschreibung des Niveaus
	 */
	private constructor(name : string, ordinal : number, bezeichnung : String, beschreibung : String) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		DQR.all_values_by_ordinal.push(this);
		DQR.all_values_by_name.set(name, this);
		this.bezeichnung = bezeichnung;
		this.beschreibung = beschreibung;
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
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : String {
		return this.__name;
	}

	/**
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof DQR))
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
	public compareTo(other : DQR) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<DQR> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : String) : DQR | null {
		let tmp : DQR | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.types.DQR'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_types_DQR(obj : unknown) : DQR {
	return obj as DQR;
}
