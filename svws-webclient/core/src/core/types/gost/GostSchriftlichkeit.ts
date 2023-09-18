import type { JavaEnum } from '../../../java/lang/JavaEnum';
import { JavaObject } from '../../../java/lang/JavaObject';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';

export class GostSchriftlichkeit extends JavaObject implements JavaEnum<GostSchriftlichkeit> {

	/** the name of the enumeration value */
	readonly __name : string;

	/** the ordinal value for the enumeration value */
	readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostSchriftlichkeit> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostSchriftlichkeit> = new Map<string, GostSchriftlichkeit>();

	/**
	 * Ist mündlich.
	 */
	public static readonly MUENDLICH : GostSchriftlichkeit = new GostSchriftlichkeit("MUENDLICH", 0, false);

	/**
	 * Ist schriftlich.
	 */
	public static readonly SCHRIFTLICH : GostSchriftlichkeit = new GostSchriftlichkeit("SCHRIFTLICH", 1, true);

	/**
	 * Kann mündlich oder schriftlich sein.
	 */
	public static readonly BELIEBIG : GostSchriftlichkeit = new GostSchriftlichkeit("BELIEBIG", 2, null);

	/**
	 * Gibt an, ob eine Schriftlichkeit vorliegt (true), nicht vorliegt (false), oder beliebig sein kann (null)
	 */
	public readonly istSchriftlich : boolean | null;

	private constructor(name : string, ordinal : number, istSchriftlich : boolean | null) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		GostSchriftlichkeit.all_values_by_ordinal.push(this);
		GostSchriftlichkeit.all_values_by_name.set(name, this);
		this.istSchriftlich = istSchriftlich;
	}

	/**
	 * Liefert TRUE, falls schriftlich, FALLS falls mündlich, andernfalls eine Exception.
	 *
	 * @return TRUE, falls schriftlich, FALLS falls mündlich, andernfalls eine Exception.
	 */
	public getIstSchriftlichOrException() : boolean {
		return DeveloperNotificationException.ifNull("Schriftlichkeit sollte nicht NULL sein!", this.istSchriftlich)!;
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
		if (!(other instanceof GostSchriftlichkeit))
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
	public compareTo(other : GostSchriftlichkeit) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostSchriftlichkeit> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostSchriftlichkeit | null {
		const tmp : GostSchriftlichkeit | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.GostSchriftlichkeit', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_gost_GostSchriftlichkeit(obj : unknown) : GostSchriftlichkeit {
	return obj as GostSchriftlichkeit;
}
