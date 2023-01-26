import { JavaObject, cast_java_lang_Object } from '../../java/lang/JavaObject';
import { LogData, cast_de_nrw_schule_svws_core_logger_LogData } from '../../core/logger/LogData';
import { Consumer, cast_java_util_function_Consumer } from '../../java/util/function/Consumer';
import { StringBuilder, cast_java_lang_StringBuilder } from '../../java/lang/StringBuilder';
import { List, cast_java_util_List } from '../../java/util/List';
import { JavaString, cast_java_lang_String } from '../../java/lang/JavaString';
import { Vector, cast_java_util_Vector } from '../../java/util/Vector';
import { LogLevel, cast_de_nrw_schule_svws_core_logger_LogLevel } from '../../core/logger/LogLevel';

export class LogConsumerVector extends JavaObject implements Consumer<LogData> {

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
	private readonly logData : Vector<LogData> = new Vector();


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
			let printTime : boolean = __param0 as boolean;
			let printLevel : boolean = __param1 as boolean;
			this.printTime = printTime;
			this.printLevel = printLevel;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Hängt einen anderen Log vom gleichen Typ an diesen an.
	 * 
	 * @param log   der anzuhängende Log
	 */
	public append(log : LogConsumerVector) : void {
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
	 * als Präfix indet erhalten. Dies dient z.B. dem Einrücken der Log-Informationen.
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
			let indent : string = __param0;
			let result : Vector<string> | null = new Vector();
			for (let i : number = 0; i < this.logData.size(); i++){
				let data : LogData = this.logData.get(i);
				if (data === null) 
					continue;
				result.add(indent! + data.getText()!);
			}
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
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.logger.LogLevel')))) && (typeof __param1 === "undefined")) {
			let level : LogLevel = cast_de_nrw_schule_svws_core_logger_LogLevel(__param0);
			return this.getText(level, "");
		} else if (((typeof __param0 !== "undefined") && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.nrw.schule.svws.core.logger.LogLevel')))) && ((typeof __param1 !== "undefined") && (typeof __param1 === "string"))) {
			let level : LogLevel = cast_de_nrw_schule_svws_core_logger_LogLevel(__param0);
			let indent : string = __param1;
			let sb : StringBuilder | null = new StringBuilder();
			for (let i : number = 0; i < this.logData.size(); i++){
				let data : LogData | null = this.logData.get(i);
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
		return ['java.util.function.Consumer', 'de.nrw.schule.svws.core.logger.LogConsumerVector'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_logger_LogConsumerVector(obj : unknown) : LogConsumerVector {
	return obj as LogConsumerVector;
}
