import { JavaObject } from '../../java/lang/JavaObject';
import { LogData } from '../../core/logger/LogData';
import type { Consumer } from '../../java/util/function/Consumer';
import { StringBuilder } from '../../java/lang/StringBuilder';
import { ArrayList } from '../../java/util/ArrayList';
import type { List } from '../../java/util/List';
import { LogLevel, cast_de_svws_nrw_core_logger_LogLevel } from '../../core/logger/LogLevel';

export class LogConsumerList extends JavaObject implements Consumer<LogData> {

	/**
	 * Gibt an, ob die Zeit beim Loggen ausgegeben wird oder nicht.
	 */
	public readonly printTime : boolean;

	/**
	 * Gibt an, ob das Log-Level beim Loggen ausgegeben wird oder nicht.
	 */
	public readonly printLevel : boolean;

	/**
	 * Der Vektor mit den gesammelten Log-Informationen.
	 */
	private readonly logData : ArrayList<LogData> = new ArrayList();


	/**
	 * Erzeugt einen neuen Consumer für Log-Informationen, mit den Standardeinstellungen,
	 * das weder Zeit noch Log-Level mit ausgegeben werden.
	 */
	public constructor();

	/**
	 * Erzeugt einen neuen Consumer für Log-Informationen.
	 *
	 * @param printTime     gibt an, ob die Zeit beim Loggen ausgegeben wird oder nicht
	 * @param printLevel    gibt an, ob das Log-Level beim Loggen ausgegeben wird oder nicht
	 */
	public constructor(printTime : boolean, printLevel : boolean);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0? : boolean, __param1? : boolean) {
		super();
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined")) {
			this.printTime = false;
			this.printLevel = false;
		} else if (((typeof __param0 !== "undefined") && typeof __param0 === "boolean") && ((typeof __param1 !== "undefined") && typeof __param1 === "boolean")) {
			const printTime : boolean = __param0 as boolean;
			const printLevel : boolean = __param1 as boolean;
			this.printTime = printTime;
			this.printLevel = printLevel;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Hängt einen anderen Log vom gleichen Typ an diesen an.
	 *
	 * @param log   der anzuhängende Log
	 */
	public append(log : LogConsumerList) : void {
		this.logData.addAll(log.logData);
	}

	/**
	 * Diese Methode implementiert das funktionale Interface java.util.function.Consumer
	 * und hängt die empfangenen Log-Informationen an den Vektor an.
	 *
	 * @param t   die anzuhängenden Log-Informationen
	 */
	public accept(t : LogData | null) : void {
		if (t === null)
			return;
		this.logData.add(t);
	}

	/**
	 * Gibt den Vektor mit den gesammelten Log-Informationen zurück.
	 *
	 * @return der Vektor mit den gesammelten Log-Informationen
	 */
	public getLogData() : List<LogData> {
		return this.logData;
	}

	/**
	 * Gibt die gesammelten Log-Informationen als Liste von Strings zurück.
	 *
	 * @return die gesammelten Log-Informationen als Liste von Strings
	 */
	public getStrings() : List<string> | null;

	/**
	 * Gibt die gesammelten Log-Informationen als Liste von Strings zurück, die alle
	 * als Präfix indent erhalten. Dies dient z.B. dem Einrücken der Log-Informationen.
	 * Außerdem werden Log-Einträge, die kein newline am Ende haben mit den jeweils nachfolgenden
	 * Einträgen zusammengefasst.
	 *
	 * @param indent   das Präfix, welches zum Einrücken der Log-Informationen genutzt wird
	 *
	 * @return die gesammelten Log-Informationen als Liste von Strings
	 */
	public getStrings(indent : string) : List<string> | null;

	/**
	 * Implementation for method overloads of 'getStrings'
	 */
	public getStrings(__param0? : string) : List<string> | null {
		if ((typeof __param0 === "undefined")) {
			return this.getStrings("");
		} else if (((typeof __param0 !== "undefined") && (typeof __param0 === "string"))) {
			const indent : string = __param0;
			const result : ArrayList<string> | null = new ArrayList();
			let temp : string = indent;
			for (let i : number = 0; i < this.logData.size(); i++) {
				const data : LogData = this.logData.get(i);
				if (data === null)
					continue;
				temp += data.getText();
				if (data.isNewLine()) {
					result.add(temp);
					temp = indent;
				}
			}
			if (!JavaObject.equalsTranspiler(indent, (temp)))
				result.add(temp);
			return result;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt die gesammelten Log-Informationen als Text zurück, bei dem
	 * die einzelnen Log-Informationen durch Zeilenumbrüche voneinander
	 * getrennt werden. Dabei werden Informationen ausgelassen,
	 * die aufgrund des hier vorgegebenen Log-Levels LogLevel.INFO nicht
	 * berücksichtigt werden sollen.
	 *
	 * @return der Text der Log-Informationen für das Log-Level LogLevel.INFO
	 */
	public getText() : string;

	/**
	 * Gibt die gesammelten Log-Informationen als Text zurück, bei dem
	 * die einzelnen Log-Informationen durch Zeilenumbrüche voneinander
	 * getrennt werden. Dabei werden Informationen ausgelassen,
	 * die aufgrund des angebenen Log-Levels nicht berücksichtigt werden
	 * sollen.
	 *
	 * @param level    das Log-Level, welches mindestens geben sein muss, damit die
	 *                 Log-Informationen berücksichtigt werden.
	 *
	 * @return der Text der Log-Informationen für das angegebene Log-Level
	 */
	public getText(level : LogLevel) : string;

	/**
	 * Gibt die gesammelten Log-Informationen als Text zurück, bei dem
	 * die einzelnen Log-Informationen durch Zeilenumbrüche voneinander
	 * getrennt werden. Dabei werden Informationen ausgelassen,
	 * die aufgrund des angebenen Log-Levels nicht berücksichtigt werden
	 * sollen.
	 *
	 * @param level    das Log-Level, welches mindestens geben sein muss, damit die
	 *                 Log-Informationen berücksichtigt werden.
	 * @param indent   das Präfix, welches zum Einrücken der Log-Informationen genutzt wird
	 *
	 * @return der Text der Log-Informationen für das angegebene Log-Level
	 */
	public getText(level : LogLevel, indent : string) : string;

	/**
	 * Implementation for method overloads of 'getText'
	 */
	public getText(__param0? : LogLevel, __param1? : string) : string {
		if ((typeof __param0 === "undefined") && (typeof __param1 === "undefined")) {
			return this.getText(LogLevel.INFO, "");
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.logger.LogLevel')))) && (typeof __param1 === "undefined")) {
			const level : LogLevel = cast_de_svws_nrw_core_logger_LogLevel(__param0);
			return this.getText(level, "");
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && ((__param0 as JavaObject).isTranspiledInstanceOf('de.svws_nrw.core.logger.LogLevel')))) && ((typeof __param1 !== "undefined") && (typeof __param1 === "string"))) {
			const level : LogLevel = cast_de_svws_nrw_core_logger_LogLevel(__param0);
			const indent : string = __param1;
			const sb : StringBuilder | null = new StringBuilder();
			for (let i : number = 0; i < this.logData.size(); i++) {
				const data : LogData | null = this.logData.get(i);
				if (data === null)
					continue;
				if (data.getLevel().toInteger() > level.toInteger())
					continue;
				sb.append(indent);
				sb.append(data.getText());
				sb.append("\n");
			}
			return sb.toString();
		} else throw new Error('invalid method overload');
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.function.Consumer', 'de.svws_nrw.core.logger.LogConsumerList'].includes(name);
	}

}

export function cast_de_svws_nrw_core_logger_LogConsumerList(obj : unknown) : LogConsumerList {
	return obj as LogConsumerList;
}
