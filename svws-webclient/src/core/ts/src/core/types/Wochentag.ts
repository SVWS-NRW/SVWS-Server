import type { JavaEnum } from '../../java/lang/JavaEnum';
import { JavaObject } from '../../java/lang/JavaObject';
import { DeveloperNotificationException } from '../../core/exceptions/DeveloperNotificationException';

export class Wochentag extends JavaObject implements JavaEnum<Wochentag> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<Wochentag> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, Wochentag> = new Map<string, Wochentag>();

	/**
	 * Montag
	 */
	public static readonly MONTAG : Wochentag = new Wochentag("MONTAG", 0, 1, "Montag", "Mo");

	/**
	 * Dienstag
	 */
	public static readonly DIENSTAG : Wochentag = new Wochentag("DIENSTAG", 1, 2, "Dienstag", "Di");

	/**
	 * Mittwoch
	 */
	public static readonly MITTWOCH : Wochentag = new Wochentag("MITTWOCH", 2, 3, "Mittwoch", "Mi");

	/**
	 * Donnerstag
	 */
	public static readonly DONNERSTAG : Wochentag = new Wochentag("DONNERSTAG", 3, 4, "Donnerstag", "Do");

	/**
	 * Freitag
	 */
	public static readonly FREITAG : Wochentag = new Wochentag("FREITAG", 4, 5, "Freitag", "Fr");

	/**
	 * Samstag
	 */
	public static readonly SAMSTAG : Wochentag = new Wochentag("SAMSTAG", 5, 6, "Samstag", "Sa");

	/**
	 * Sonntag
	 */
	public static readonly SONNTAG : Wochentag = new Wochentag("SONNTAG", 6, 7, "Sonntag", "So");

	/**
	 * Die eindeutige ID des Wochentags
	 */
	public readonly id : number;

	/**
	 * Der voll ausgeschriebene Wochentag, z.B. Montag
	 */
	public readonly beschreibung : string;

	/**
	 * Das Kürzel des Wochentags, z.B. Mo
	 */
	public readonly kuerzel : string;

	/**
	 * Erzeugt einen neuen Wochentag für die Aufzählung.
	 *
	 * @param id             die eindeutige ID des Wochentags
	 * @param kuerzel        das Kürzel des Wochentags
	 * @param beschreibung   der ausgeschriebene Wochentag
	 */
	private constructor(name : string, ordinal : number, id : number, beschreibung : string, kuerzel : string) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		Wochentag.all_values_by_ordinal.push(this);
		Wochentag.all_values_by_name.set(name, this);
		this.id = id;
		this.beschreibung = beschreibung;
		this.kuerzel = kuerzel;
	}

	public toString() : string {
		return this.beschreibung;
	}

	/**
	 * Liefert das {@link Wochentag}-Objekt anhand seiner ID.
	 *
	 * @param id  Die ID des Wochentages.
	 *
	 * @return das {@link Wochentag}-Objekt anhand seiner ID.
	 * @throws DeveloperNotificationException falls die ID ungültig ist
	 */
	public static fromIDorException(id : number) : Wochentag {
		DeveloperNotificationException.ifTrue("Der Wochentag(" + id + ") muss zwischen 1 (Montag) und 7 (Sonntag) liegen!", id < 1 || id > 7);
		const wochentage : Array<Wochentag> = this.values();
		return wochentage[id - 1];
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
	 * Returns true if this and the other enumeration values are equal.
	 *
	 * @param other   the other enumeration value
	 *
	 * @returns true if they are equal and false otherwise
	 */
	public equals(other : JavaObject) : boolean {
		if (!(other instanceof Wochentag))
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
	public compareTo(other : Wochentag) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<Wochentag> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : Wochentag | null {
		const tmp : Wochentag | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.Wochentag', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_Wochentag(obj : unknown) : Wochentag {
	return obj as Wochentag;
}
