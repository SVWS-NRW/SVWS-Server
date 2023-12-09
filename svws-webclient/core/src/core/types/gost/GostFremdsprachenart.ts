import { JavaEnum } from '../../../java/lang/JavaEnum';

export class GostFremdsprachenart extends JavaEnum<GostFremdsprachenart> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<GostFremdsprachenart> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, GostFremdsprachenart> = new Map<string, GostFremdsprachenart>();

	/**
	 * Ist eine fortgeführte Fremdsprache.
	 */
	public static readonly FORTGEFUEHRT : GostFremdsprachenart = new GostFremdsprachenart("FORTGEFUEHRT", 0, 0, "fortgeführt");

	/**
	 * Ist eine neueinsetzende Fremdsprache.
	 */
	public static readonly NEU : GostFremdsprachenart = new GostFremdsprachenart("NEU", 1, 1, "neu einsetzend");

	/**
	 * Kann neueinsetzende oder fortgeführte Fremdsprache sein.
	 */
	public static readonly BELIEBIG : GostFremdsprachenart = new GostFremdsprachenart("BELIEBIG", 2, 2, "beliebig");

	/**
	 * eine eindeutige ID für die Fremdsprachenart
	 */
	public readonly id : number;

	/**
	 * Die Bezeichnung der Fremdsprachenart als Text
	 */
	public readonly bezeichnung : string;

	/**
	 * Erzeugt eine neue Fremdsprachenart für die Aufzählung.
	 *
	 * @param id            die eindeutige ID für die Fremdsprachenart
	 * @param bezeichnung   die Bezeichnung der Fremdsprachenart als Text
	 */
	private constructor(name : string, ordinal : number, id : number, bezeichnung : string) {
		super(name, ordinal);
		GostFremdsprachenart.all_values_by_ordinal.push(this);
		GostFremdsprachenart.all_values_by_name.set(name, this);
		this.id = id;
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<GostFremdsprachenart> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : GostFremdsprachenart | null {
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.gost.GostFremdsprachenart', 'java.lang.Enum', 'java.lang.Comparable'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_gost_GostFremdsprachenart(obj : unknown) : GostFremdsprachenart {
	return obj as GostFremdsprachenart;
}
