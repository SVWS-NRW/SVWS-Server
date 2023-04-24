import { Comparable } from '../../java/lang/Comparable';
import { JavaObject } from '../../java/lang/JavaObject';
import { Arrays } from '../../java/util/Arrays';
import { LogLevel } from '../../core/logger/LogLevel';
import { System } from '../../java/lang/System';

export class LogData extends JavaObject implements Comparable<LogData | null> {

	private readonly time : number;

	private readonly level : LogLevel;

	private readonly text : string;

	private readonly newLine : boolean;

	private indent : number = 0;


	/**
	 * Erzeugt eine neue Log-Information.
	 *
	 * @param level     das zugehörige Log-Level
	 * @param indent    die Anzahl der Leerzeichen, die bei der Ausgabe zur Einrückung genutzt werden sollen
	 * @param newLine   gibt an, ob die Log-Informationen beim Ausgeben mit einer neuen Zeile beendet werden sollen oder nicht
	 * @param text      der Text der Log-Information
	 */
	public constructor(level : LogLevel, indent : number, newLine : boolean, text : string) {
		super();
		this.time = System.currentTimeMillis();
		this.level = level;
		this.indent = (indent < 0) ? 0 : indent;
		this.newLine = newLine;
		this.text = (text === null) ? "" : text;
	}

	/**
	 * Vergleicht zwei Log-Informationen anhand der Zeit.
	 *
	 * @param other   die zu vergleichenden Log-Informationen
	 *
	 * @return ein negativer Wert, falls diese Log-Information früher geloggt wurde, 0, falls
	 *         sie zur gleichen Zeit geloggt wurden oder ein positiver Wert, falls diese
	 *         Log-Information später geloggt wurde
	 */
	public compareTo(other : LogData) : number {
		if (this.time < other.time)
			return -1;
		if (this.time > other.time)
			return 1;
		return 0;
	}

	/**
	 * Gibt diese Log-Information als JSON-String mit Zeit und Log-Level zurück.
	 *
	 * @return die Log-Informationen als JSON-String
	 */
	public toString() : string {
		return "{ \"time\":" + this.time + ", \"level\":" + this.level.toInteger() + ", \"text\":\"" + this.getText()! + "\"}";
	}

	/**
	 * Erhöht die Einrückung bei der Ausgabe, um den angegebenen Wert.
	 *
	 * @param indent   der Wert, um den die Einrückung erhöht wird.
	 */
	public addIndent(indent : number) : void {
		this.indent += indent;
	}

	/**
	 * Gibt den Zeitstempel der Log-Information zurück.
	 *
	 * @return der Zeitstempel der Log-Information
	 */
	public getTime() : number {
		return this.time;
	}

	/**
	 * Gibt das Log-Level der Log-Information zurück.
	 *
	 * @return das Log-Level der Log-Information
	 */
	public getLevel() : LogLevel {
		return this.level;
	}

	/**
	 * Gibt an, ob bei der Ausgabe mit einer neue Zeile beendet werden soll oder nicht.
	 *
	 * @return true, falls die Ausgabe mit einer neuen Zeile beendet werden soll, ansonsten false
	 */
	public isNewLine() : boolean {
		return this.newLine;
	}

	/**
	 * Gibt den Text dieser Log-Information mit Einrückung, aber ohne Zeitangabe und
	 * Log-Level als String zurück.
	 *
	 * @return der Text dieser Log-Information
	 */
	public getText() : string {
		if (this.indent <= 0)
			return this.text;
		const indentChars : Array<string> | null = Array(this.indent).fill("");
		Arrays.fill(indentChars, ' ');
		return indentChars.join("") + this.text!;
	}

	public hashCode() : number {
		return super.hashCode();
	}

	public equals(obj : unknown | null) : boolean {
		if (obj === null)
			return false;
		if (((obj instanceof JavaObject) && ((obj as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.logger.LogData'))))
			return this.compareTo(cast_de_svws_nrw_core_logger_LogData(obj)) === 0;
		return super.equals((obj));
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.lang.Comparable', 'de.svws_nrw.core.logger.LogData'].includes(name);
	}

}

export function cast_de_svws_nrw_core_logger_LogData(obj : unknown) : LogData {
	return obj as LogData;
}
