import { JavaEnum } from '../../../java/lang/JavaEnum';

export class GostAbiturFach extends JavaEnum<GostAbiturFach> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostAbiturFach> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostAbiturFach> = new Map<string, GostAbiturFach>();

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
		super(name, ordinal);
		GostAbiturFach.all_values_by_ordinal.push(this);
		GostAbiturFach.all_values_by_name.set(name, this);
		this.id = id;
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}

	/**
	 * Gibt die Abiturfachart anhand der ID zurück.
	 *
	 * @param id    die ID der Abiturfachart
	 *
	 * @return die Abiturfachart oder null falls die ID ungültig ist
	 */
	public static fromID(id : number | null) : GostAbiturFach | null {
		if (id === null)
			return null;
		switch (id) {
			case 1: {
				return GostAbiturFach.LK1;
			}
			case 2: {
				return GostAbiturFach.LK2;
			}
			case 3: {
				return GostAbiturFach.AB3;
			}
			case 4: {
				return GostAbiturFach.AB4;
			}
			default: {
				return null;
			}
		}
	}

	/**
	 * Gibt die Abiturfachart anhand der ID (als String) zurück.
	 *
	 * @param strID    die ID der Abiturfachart (als String)
	 *
	 * @return die Abiturfachart oder null, falls die ID ungültig ist
	 */
	public static fromIDString(strID : string | null) : GostAbiturFach | null {
		if (strID === null)
			return null;
		switch (strID) {
			case "1": {
				return GostAbiturFach.LK1;
			}
			case "2": {
				return GostAbiturFach.LK2;
			}
			case "3": {
				return GostAbiturFach.AB3;
			}
			case "4": {
				return GostAbiturFach.AB4;
			}
			default: {
				return null;
			}
		}
	}

	/**
	 * Gibt die Abiturfachart anhand des Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Abiturfachart
	 *
	 * @return die Abiturfachart oder null, falls das Kürzel ungültig ist
	 */
	public static fromKuerzel(kuerzel : string | null) : GostAbiturFach | null {
		switch (kuerzel) {
			case "LK1": {
				return GostAbiturFach.LK1;
			}
			case "LK2": {
				return GostAbiturFach.LK2;
			}
			case "AB3": {
				return GostAbiturFach.AB3;
			}
			case "AB4": {
				return GostAbiturFach.AB4;
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.GostAbiturFach', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_gost_GostAbiturFach(obj : unknown) : GostAbiturFach {
	return obj as GostAbiturFach;
}
