import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';

export class LogLevel extends JavaObject {

	/** the name of the enumeration value */
	private readonly __name : string;

	/** the ordinal value for the enumeration value */
	private readonly __ordinal : number;

	/** an array containing all values of this enumeration */
	private static readonly all_values_by_ordinal : Array<LogLevel> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	private static readonly all_values_by_name : Map<string, LogLevel> = new Map<string, LogLevel>();

	/**
	 * Das Log-Level, bei dem keine Fehler, etc. ausgegeben werden, sondern nur Informationen der Anwendung
	 */
	public static readonly APP : LogLevel = new LogLevel("APP", 0, 0);

	/**
	 * Das Log-Level, welches nur Fehler ausgibt.
	 */
	public static readonly ERROR : LogLevel = new LogLevel("ERROR", 1, 10);

	/**
	 * Das Log-Level, welches auch Warnungen ausgibt.
	 */
	public static readonly WARNING : LogLevel = new LogLevel("WARNING", 2, 100);

	/**
	 * Das Log-Level, welches auch Informationen, die keine Warnungen sind, ausgibt.
	 */
	public static readonly INFO : LogLevel = new LogLevel("INFO", 3, 1000);

	/**
	 * Das Log-Level, welches zum Debuggen alles ausgibt.
	 */
	public static readonly DEBUG : LogLevel = new LogLevel("DEBUG", 4, 10000);

	private readonly level : number;

	/**
	 * Der von der Aufzählung intern genutzte Konstruktor der Aufzählung
	 *
	 * @param level   der Integer-Wert des Log-Levels.
	 */
	private constructor(name : string, ordinal : number, level : number) {
		super();
		this.__name = name;
		this.__ordinal = ordinal;
		LogLevel.all_values_by_ordinal.push(this);
		LogLevel.all_values_by_name.set(name, this);
		this.level = level;
	}

	/**
	 * Gibt den Integer-Wert des Log-Levels zurück.
	 *
	 * @return der Integer-Wert des Log-Levels
	 */
	public toInteger() : number {
		return this.level;
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
		if (!(other instanceof LogLevel))
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
	public compareTo(other : LogLevel) : number {
		return this.__ordinal - other.__ordinal;
	}

	/**
	 * Returns an array with enumeration values.
	 *
	 * @returns the array with enumeration values
	 */
	public static values() : Array<LogLevel> {
		return [...this.all_values_by_ordinal];
	}

	/**
	 * Returns the enumeration value with the specified name.
	 *
	 * @param name   the name of the enumeration value
	 *
	 * @returns the enumeration values or null
	 */
	public static valueOf(name : string) : LogLevel | null {
		let tmp : LogLevel | undefined = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.logger.LogLevel'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_logger_LogLevel(obj : unknown) : LogLevel {
	return obj as LogLevel;
}
