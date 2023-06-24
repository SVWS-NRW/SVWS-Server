import type { JavaEnum } from '../../java/lang/JavaEnum';
import { JavaObject } from '../../java/lang/JavaObject';
import { JavaString } from '../../java/lang/JavaString';

export class ServerMode extends JavaObject implements JavaEnum<ServerMode> {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<ServerMode> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, ServerMode> = new Map<string, ServerMode>();

	/**
	 * STABLE: Es werden nur Funktionen angeboten, die als stabil und getestet angesehen werden
	 */
	public static readonly STABLE : ServerMode = new ServerMode("STABLE", 0, "stable");

	/**
	 * BETA: Es werden auch Funktionen angeboten, welche als weitgehend stabil angesegen werden, sich aber noch in der abschließenden Entwicklung befinden
	 */
	public static readonly BETA : ServerMode = new ServerMode("BETA", 1, "beta");

	/**
	 * ALPHA: Es werden auch Funktionen angeboten, welche noch nicht als stabil gelten, aber zumindest weitgehend implementiert sind
	 */
	public static readonly ALPHA : ServerMode = new ServerMode("ALPHA", 2, "alpha");

	/**
	 * DEV: Es werden alle Funktionen angeboten, auch wenn diese noch nicht stabil sind.
	 */
	public static readonly DEV : ServerMode = new ServerMode("DEV", 3, "dev");

	/**
	 * Der Server-Mode als Text, welche in textuellen Konfigurationen genutzt werden kann
	 */
	public readonly text : string;

	/**
	 * Erzeugt einen neuen Modus für die Aufzählung.
	 *
	 * @param text   der Text für den Modus
	 */
	private constructor(name : string, ordinal : number, text : string) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		ServerMode.all_values_by_ordinal.push(this);
		ServerMode.all_values_by_name.set(name, this);
		this.text = text;
	}

	public toString() : string {
		return this.text;
	}

	/**
	 * Liefert das {@link ServerMode}-Objekt anhand des übergebenen Textes.
	 * Der Vergleich des Textes erfolgt dabei case-insensitive. Ist der übergebene
	 * Wert ungültig, so wird als Default STABLE zurückgegeben, so dass ein
	 * Rückgabewert für die Methode garantiert werden kann.
	 *
	 * @param text   Der Text des Server-Modes.
	 *
	 * @return das {@link ServerMode}-Objekt anhand des übergebenen Textes.
	 */
	public static getByText(text : string | null) : ServerMode {
		for (const mode of this.values())
			if (JavaString.equalsIgnoreCase(mode.text, text))
				return mode;
		return ServerMode.STABLE;
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
		if (!(other instanceof ServerMode))
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
	public compareTo(other : ServerMode) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<ServerMode> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : ServerMode | null {
		const tmp : ServerMode | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.types.ServerMode', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_types_ServerMode(obj : unknown) : ServerMode {
	return obj as ServerMode;
}
