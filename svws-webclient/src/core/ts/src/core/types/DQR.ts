import { JavaEnum } from '../../java/lang/JavaEnum';
import { JavaObject } from '../../java/lang/JavaObject';

export class DQR extends JavaObject implements JavaEnum<DQR> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<DQR> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, DQR> = new Map<string, DQR>();

	/**
	 * DQR Niveau 1
	 */
	public static readonly NIVEAU_1 : DQR = new DQR("NIVEAU_1", 0, "DQR Niveau 1", "Niveau 1 des Deutschen Qualifikationsrahmens (DQR)");

	/**
	 * DQR Niveau 2
	 */
	public static readonly NIVEAU_2 : DQR = new DQR("NIVEAU_2", 1, "DQR Niveau 2", "Niveau 2 des Deutschen Qualifikationsrahmens (DQR)");

	/**
	 * DQR Niveau 3
	 */
	public static readonly NIVEAU_3 : DQR = new DQR("NIVEAU_3", 2, "DQR Niveau 3", "Niveau 3 des Deutschen Qualifikationsrahmens (DQR)");

	/**
	 * DQR Niveau 4
	 */
	public static readonly NIVEAU_4 : DQR = new DQR("NIVEAU_4", 3, "DQR Niveau 4", "Niveau 4 des Deutschen Qualifikationsrahmens (DQR)");

	/**
	 * DQR Niveau 5
	 */
	public static readonly NIVEAU_5 : DQR = new DQR("NIVEAU_5", 4, "DQR Niveau 5", "Niveau 5 des Deutschen Qualifikationsrahmens (DQR)");

	/**
	 * DQR Niveau 6
	 */
	public static readonly NIVEAU_6 : DQR = new DQR("NIVEAU_6", 5, "DQR Niveau 6", "Niveau 6 des Deutschen Qualifikationsrahmens (DQR)");

	/**
	 * DQR Niveau 7
	 */
	public static readonly NIVEAU_7 : DQR = new DQR("NIVEAU_7", 6, "DQR Niveau 7", "Niveau 7 des Deutschen Qualifikationsrahmens (DQR)");

	/**
	 * DQR Niveau 8
	 */
	public static readonly NIVEAU_8 : DQR = new DQR("NIVEAU_8", 7, "DQR Niveau 8", "Niveau 8 des Deutschen Qualifikationsrahmens (DQR)");

	/**
	 * Die Bezeichnung des Niveaus
	 */
	public readonly bezeichnung : string | null;

	/**
	 * Eine Beschreibung des DQR-Niveaus
	 */
	public readonly beschreibung : string | null;

	/**
	 * Erstell ein neues DQR-Niveau.
	 *
	 * @param bezeichnung    die Bezeichnung des Niveaus
	 * @param beschreibung   eine kurze Beschreibung des Niveaus
	 */
	private constructor(name : string, ordinal : number, bezeichnung : string, beschreibung : string) {
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
	public name() : string {
		return this.__name;
	}

	/**
	 * Returns the ordinal value of this enumeration value.
	 *
	 * @returns the ordinal value
	 */
	public ordinal() : number {
		return this.__ordinal;
	}

	/**
	 * Returns the name of this enumeration value.
	 *
	 * @returns the name
	 */
	public toString() : string {
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
	public static valueOf(name : string) : DQR | null {
		const tmp : DQR | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.DQR', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_DQR(obj : unknown) : DQR {
	return obj as DQR;
}
