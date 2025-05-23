import { JavaObject } from '../../java/lang/JavaObject';
import { LogData, cast_de_svws_nrw_core_logger_LogData } from '../../core/logger/LogData';
import type { Consumer } from '../../java/util/function/Consumer';
import { LogConsumerConsole } from '../../core/logger/LogConsumerConsole';
import { ArrayList } from '../../java/util/ArrayList';
import { Class } from '../../java/lang/Class';
import { LogLevel, cast_de_svws_nrw_core_logger_LogLevel } from '../../core/logger/LogLevel';

export class Logger extends JavaObject {

	/**
	 * Die globale Instanz des Loggers
	 */
	private static _instance : Logger | null = null;

	private readonly consumer : ArrayList<Consumer<LogData>> = new ArrayList<Consumer<LogData>>();

	private defaultLevel : LogLevel = LogLevel.INFO;

	private indent : number = 0;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public constructor() {
		super();
	}

	/**
	 * Gibt die Instanz des Loggers zurück.
	 *
	 * @return die globale Logger-Instanz
	 */
	public static global() : Logger | null {
		if (Logger._instance === null) {
			Logger._instance = new Logger();
			Logger._instance.addConsumer(new LogConsumerConsole(true, true));
		}
		return Logger._instance;
	}

	/**
	 * Fügt einen Consumer für Log-Informationen zum Logger hinzu.
	 *
	 * @param c   der hinzuzufügende Consumer von Log-Informationen
	 */
	public addConsumer(c : Consumer<LogData>) : void {
		this.consumer.add(c);
	}

	/**
	 * Fügt alle Consumer des anderen Loggers zu diesem hinzu.
	 *
	 * @param other   der andere Logger
	 */
	public copyConsumer(other : Logger) : void {
		this.consumer.addAll(other.consumer);
	}

	/**
	 * Entfernt den angegeben Consumer für Log-Informationen aus dem Logger.
	 *
	 * @param c   der zu entfernende Consumer von Log-Informationen
	 */
	public removeConsumer(c : Consumer<LogData>) : void {
		this.consumer.remove(c);
	}

	/**
	 * Gibt das aktuelle Default-Log-Level für neue Log-Informationen zurück.
	 *
	 * @return das aktuelle Default-Log-Level für neue Log-Informationen
	 */
	public getDefaultLevel() : LogLevel {
		return this.defaultLevel;
	}

	/**
	 * Setzt das Default-Log-Level für neue Log-Informationen.
	 *
	 * @param defaultLevel   das neue Default-Log-Level für neue Log-Informationen
	 */
	public setDefaultLevel(defaultLevel : LogLevel) : void {
		this.defaultLevel = defaultLevel;
	}

	/**
	 * Gibt die Anzahl der Leerzeichen zurück, die für die Einrückung bei einer Ausgabe von
	 * neuen Log-Informationen genutzt wird.
	 *
	 * @return die Anzahl der Leerzeichen
	 */
	public getIndent() : number {
		return this.indent;
	}

	/**
	 * Setzt die Anzahl der Leerzeichen, die für die Einrückung bei einer Ausgabe von
	 * neuen Log-Informationen genutzt wird.
	 *
	 * @param indent   die Anzahl der Leerzeichen
	 */
	public setIndent(indent : number) : void {
		this.indent = (indent < 0) ? 0 : indent;
	}

	/**
	 * Verändert die Anzahl der Leerzeichen, die für die Einrückung bei einer Ausgabe von
	 * neuen Log-Informationen genutzt wird.
	 *
	 * @param indent   die Veränderung bei der Anzahl der Leerzeichen
	 */
	public modifyIndent(indent : number) : void {
		this.indent = ((this.indent + indent) < 0) ? 0 : (this.indent + indent);
	}

	/**
	 * Loggt die übergebenen Log-Informationen bei diesem Debugger.
	 *
	 * @param data   die Log-Informationen
	 */
	public log(data : LogData) : void;

	/**
	 * Loggt den angegebenen Text mit dem angegebenen Log-Level und der angegebenen Einrückung.
	 *
	 * @param level    das Log-Level des zu loggenden Textes
	 * @param indent   die Einrückung, die bei dem Text verwendet werden soll
	 * @param text     der Text
	 */
	public log(level : LogLevel, indent : number, text : string) : void;

	/**
	 * Loggt den angegebenen Text mit dem angegebenen Log-Level und der Standard-Einrückung.
	 *
	 * @param level    das Log-Level des zu loggenden Textes
	 * @param text     der Text
	 */
	public log(level : LogLevel, text : string) : void;

	/**
	 * Loggt den angegebenen Text mit dem Standard-Log-Level und der angegebenen Einrückung.
	 *
	 * @param indent   die Einrückung, die bei dem Text verwendet werden soll
	 * @param text     der Text
	 */
	public log(indent : number, text : string) : void;

	/**
	 * Loggt den angegebenen Text mit dem Standard-Log-Level und der Standard-Einrückung.
	 *
	 * @param text     der Text
	 */
	public log(text : string) : void;

	/**
	 * Implementation for method overloads of 'log'
	 */
	public log(__param0 : LogData | LogLevel | number | string, __param1? : number | string, __param2? : string) : void {
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.logger.LogData')))) && (__param1 === undefined) && (__param2 === undefined)) {
			const data : LogData = cast_de_svws_nrw_core_logger_LogData(__param0);
			for (const c of this.consumer)
				c.accept(data);
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.logger.LogLevel')))) && ((__param1 !== undefined) && typeof __param1 === "number") && ((__param2 !== undefined) && (typeof __param2 === "string"))) {
			const level : LogLevel = cast_de_svws_nrw_core_logger_LogLevel(__param0);
			const indent : number = __param1 as number;
			const text : string = __param2;
			this.log(new LogData(level, indent, false, text));
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.logger.LogLevel')))) && ((__param1 !== undefined) && (typeof __param1 === "string")) && (__param2 === undefined)) {
			const level : LogLevel = cast_de_svws_nrw_core_logger_LogLevel(__param0);
			const text : string = __param1;
			this.log(level, this.indent, text);
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && (typeof __param1 === "string")) && (__param2 === undefined)) {
			const indent : number = __param0 as number;
			const text : string = __param1;
			this.log(this.defaultLevel, indent, text);
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && (__param1 === undefined) && (__param2 === undefined)) {
			const text : string = __param0;
			this.log(this.defaultLevel, text);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Loggt den angegebenen Text mit dem angegebenen Log-Level und der angegebenen Einrückung
	 * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
	 *
	 * @param level    das Log-Level des zu loggenden Textes
	 * @param indent   die Einrückung, die bei dem Text verwendet werden soll
	 * @param text     der Text
	 */
	public logLn(level : LogLevel, indent : number, text : string) : void;

	/**
	 * Loggt den angegebenen Text mit dem angegebenen Log-Level und der Standard-Einrückung
	 * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
	 *
	 *
	 * @param level    das Log-Level des zu loggenden Textes
	 * @param text     der Text
	 */
	public logLn(level : LogLevel, text : string) : void;

	/**
	 * Loggt den angegebenen Text mit dem Standard-Log-Level und der angegebenen Einrückung
	 * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
	 *
	 * @param indent   die Einrückung, die bei dem Text verwendet werden soll
	 * @param text     der Text
	 */
	public logLn(indent : number, text : string) : void;

	/**
	 * Loggt den angegebenen Text mit dem Standard-Log-Level und der Standard-Einrückung
	 * und gibt an, dass am Ende eine neue Zeile angefangen werden soll.
	 *
	 * @param text     der Text
	 */
	public logLn(text : string) : void;

	/**
	 * Implementation for method overloads of 'logLn'
	 */
	public logLn(__param0 : LogLevel | number | string, __param1? : number | string, __param2? : string) : void {
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.logger.LogLevel')))) && ((__param1 !== undefined) && typeof __param1 === "number") && ((__param2 !== undefined) && (typeof __param2 === "string"))) {
			const level : LogLevel = cast_de_svws_nrw_core_logger_LogLevel(__param0);
			const indent : number = __param1 as number;
			const text : string = __param2;
			this.log(new LogData(level, indent, true, text));
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.logger.LogLevel')))) && ((__param1 !== undefined) && (typeof __param1 === "string")) && (__param2 === undefined)) {
			const level : LogLevel = cast_de_svws_nrw_core_logger_LogLevel(__param0);
			const text : string = __param1;
			this.logLn(level, this.indent, text);
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && (typeof __param1 === "string")) && (__param2 === undefined)) {
			const indent : number = __param0 as number;
			const text : string = __param1;
			this.logLn(this.defaultLevel, indent, text);
		} else if (((__param0 !== undefined) && (typeof __param0 === "string")) && (__param1 === undefined) && (__param2 === undefined)) {
			const text : string = __param0;
			this.logLn(this.defaultLevel, text);
		} else throw new Error('invalid method overload');
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.logger.Logger';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.logger.Logger'].includes(name);
	}

	public static class = new Class<Logger>('de.svws_nrw.core.logger.Logger');

}

export function cast_de_svws_nrw_core_logger_Logger(obj : unknown) : Logger {
	return obj as Logger;
}
