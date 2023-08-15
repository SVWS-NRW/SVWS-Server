import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaString } from '../../../java/lang/JavaString';

export class SprachBelegungSekI extends JavaObject implements JavaEnum<SprachBelegungSekI> {

	/** the name of the enumeration value */
	readonly __name : string;

	/** the ordinal value for the enumeration value */
	readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<SprachBelegungSekI> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, SprachBelegungSekI> = new Map<string, SprachBelegungSekI>();

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I nicht oder weniger als 2 Jahre belegt wurde
	 */
	public static readonly NICHT_BELEGT : SprachBelegungSekI = new SprachBelegungSekI("NICHT_BELEGT", 0, 0);

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I mindestens 2 Jahre - aber nicht 4 oder mehr Jahre - belegt wurde
	 */
	public static readonly MIND_2_JAHRE : SprachBelegungSekI = new SprachBelegungSekI("MIND_2_JAHRE", 1, 2);

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I mindestens 4 Jahre - aber nicht ab Klasse 5 - belegt wurde
	 */
	public static readonly MIND_4_JAHRE : SprachBelegungSekI = new SprachBelegungSekI("MIND_4_JAHRE", 2, 4);

	/**
	 * Gibt an, dass eine Sprache in der Sekundarstufe I ab Klasse 5, d.h. 5 (in G8) oder 6 Jahre belegt wurde.
	 */
	public static readonly AB_JAHRGANG_5 : SprachBelegungSekI = new SprachBelegungSekI("AB_JAHRGANG_5", 3, 6);

	/**
	 * Die Dauer der Sprachbelegung in der SekI - der Wert kann von der realen Belegung abweichen, da nur die relevante Dauer angeben ist und im Falle des Jahrgangs 5 abweichen kann, falls der G8-Bildungsgang vorliegt
	 */
	public readonly dauer : number;

	/**
	 * Erstellt einen neuen enum-Wert mit der angegebenen Dauer der Sprachbelegung.
	 *
	 * @param dauer   die Dauer der Sprachbelegung in der Sek I
	 */
	private constructor(name : string, ordinal : number, dauer : number) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		SprachBelegungSekI.all_values_by_ordinal.push(this);
		SprachBelegungSekI.all_values_by_name.set(name, this);
		this.dauer = dauer;
	}

	/**
	 * Ermittelt die Sprachbelegung in der Sek I anhand des übergebenen Jahrgangs.
	 * WICHTIG: Sollte ein Schüler sich im G8-Bildungsgang bewegen, so wird die Dauer
	 * mit 6 Jahren hier nicht korrekt zugeordnet.
	 *
	 * @param kuerzel   der Statistik-Jahrgang in welchem mit der Sprache begonnen wurde
	 *
	 * @return die Sprachbelegung in der Sek I
	 */
	public static getByASDJahrgang(kuerzel : string | null) : SprachBelegungSekI {
		if (kuerzel === null)
			return SprachBelegungSekI.NICHT_BELEGT;
		if (JavaString.compareTo(kuerzel, "05") <= 0)
			return SprachBelegungSekI.AB_JAHRGANG_5;
		if (JavaString.compareTo(kuerzel, "07") <= 0)
			return SprachBelegungSekI.MIND_4_JAHRE;
		if (JavaString.compareTo(kuerzel, "09") <= 0)
			return SprachBelegungSekI.MIND_2_JAHRE;
		return SprachBelegungSekI.NICHT_BELEGT;
	}

	/**
	 * Ermittelt die Sprachbelegung in der Sek I anhand der übergebenen Dauer der Belegung in der Sek I.
	 * WICHTIG: Sollte ein Schüler sich im G8-Bildungsgang bewegen, so wird die Dauer
	 * mit 6 Jahren hier nicht korrekt zugeordnet.
	 *
	 * @param dauer   die Dauer in vollen Jahren bei der Sprachbelegung in der Sek I
	 *
	 * @return die Sprachbelegung in der Sek I
	 */
	public static getByDauer(dauer : number) : SprachBelegungSekI {
		if (dauer <= 0)
			return SprachBelegungSekI.NICHT_BELEGT;
		if (dauer <= 3)
			return SprachBelegungSekI.MIND_2_JAHRE;
		if (dauer <= 4)
			return SprachBelegungSekI.MIND_4_JAHRE;
		return SprachBelegungSekI.AB_JAHRGANG_5;
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
		if (!(other instanceof SprachBelegungSekI))
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
	public compareTo(other : SprachBelegungSekI) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<SprachBelegungSekI> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : SprachBelegungSekI | null {
		const tmp : SprachBelegungSekI | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.fach.SprachBelegungSekI', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_fach_SprachBelegungSekI(obj : unknown) : SprachBelegungSekI {
	return obj as SprachBelegungSekI;
}
