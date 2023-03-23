import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';

export class GostBelegpruefungsArt extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<GostBelegpruefungsArt> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, GostBelegpruefungsArt> = new Map<string, GostBelegpruefungsArt>();

	/**
	 * Prüfung nur der EF.1
	 */
	public static readonly EF1 : GostBelegpruefungsArt = new GostBelegpruefungsArt("EF1", 0, "EF.1", "nur EF.1");

	/**
	 * Gesamtprüfung über die gesamte Oberstufe
	 */
	public static readonly GESAMT : GostBelegpruefungsArt = new GostBelegpruefungsArt("GESAMT", 1, "Gesamt", "die gesamte Oberstufe");

	/**
	 * Das Kürzel für die Belegprüfungsart
	 */
	public readonly kuerzel : string;

	/**
	 * Eine textuelle Beschreibung für die Art der Belegprüfung
	 */
	public readonly beschreibung : string;

	/**
	 * Erzeugt ein neues Abitur-Belegungsart-Objekt
	 *
	 * @param kuerzel        das der Kurs-Belegungsart
	 * @param beschreibung   die textuelle Beschreibung der Kurs-Belegungsart
	 */
	private constructor(name : string, ordinal : number, kuerzel : string, beschreibung : string) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		GostBelegpruefungsArt.all_values_by_ordinal.push(this);
		GostBelegpruefungsArt.all_values_by_name.set(name, this);
		this.kuerzel = kuerzel;
		this.beschreibung = beschreibung;
	}

	/**
	 * Gibt die Art der Belegprüfung anhand des übergebenen Kürzels zurück.
	 *
	 * @param kuerzel    das Kürzel der Art der Belegprüfung
	 *
	 * @return die Art der Belegprüfung
	 */
	public static fromKuerzel(kuerzel : string | null) : GostBelegpruefungsArt | null {
		if (kuerzel === null)
			return null;
		switch (kuerzel) {
			case "EF.1": {
				return GostBelegpruefungsArt.EF1;
			}
			case "Gesamt": {
				return GostBelegpruefungsArt.GESAMT;
			}
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
		if (!(other instanceof GostBelegpruefungsArt))
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
	public compareTo(other : GostBelegpruefungsArt) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostBelegpruefungsArt> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostBelegpruefungsArt | null {
		const tmp : GostBelegpruefungsArt | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungsArt'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_gost_GostBelegpruefungsArt(obj : unknown) : GostBelegpruefungsArt {
	return obj as GostBelegpruefungsArt;
}
