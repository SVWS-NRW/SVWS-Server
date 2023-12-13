import { JavaEnum } from '../../../java/lang/JavaEnum';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';

export class GostSchriftlichkeit extends JavaEnum<GostSchriftlichkeit> {

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
		super(name, ordinal);
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.types.gost.GostSchriftlichkeit';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.GostSchriftlichkeit', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_gost_GostSchriftlichkeit(obj : unknown) : GostSchriftlichkeit {
	return obj as GostSchriftlichkeit;
}
