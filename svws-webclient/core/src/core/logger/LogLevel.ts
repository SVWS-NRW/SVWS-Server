import { JavaEnum } from '../../java/lang/JavaEnum';

export class LogLevel extends JavaEnum<LogLevel> {

	/** an array containing all values of this enumeration */
	static readonly all_values_by_ordinal : Array<LogLevel> = [];

	/** an array containing all values of this enumeration indexed by their name*/
	static readonly all_values_by_name : Map<string, LogLevel> = new Map<string, LogLevel>();

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
		super(name, ordinal);
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
		const tmp = this.all_values_by_name.get(name);
		return (!tmp) ? null : tmp;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.logger.LogLevel', 'java.lang.Enum'].includes(name);
	}

}

export function cast_de_svws_nrw_core_logger_LogLevel(obj : unknown) : LogLevel {
	return obj as LogLevel;
}
