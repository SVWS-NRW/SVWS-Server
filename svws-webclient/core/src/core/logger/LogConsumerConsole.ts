import { JavaObject } from '../../java/lang/JavaObject';
import { LogData } from '../../core/logger/LogData';
import type { Consumer } from '../../java/util/function/Consumer';
import { DateUtils } from '../../core/utils/DateUtils';

export class LogConsumerConsole extends JavaObject implements Consumer<LogData> {

	/**
	 * Gibt an, ob die Zeit beim Loggen ausgegeben wird oder nicht.
	 */
	public readonly printTime : boolean;

	/**
	 * Gibt an, ob das Log-Level beim Loggen ausgegeben wird oder nicht.
	 */
	public readonly printLevel : boolean;

	/**
	 * Gibt an, ob mit den letzten Log-Daten eine neue Zeile angefangen wurde.
	 */
	private lastLogDataHadNewLine : boolean = true;


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
		if ((__param0 === undefined) && (__param1 === undefined)) {
			this.printTime = false;
			this.printLevel = false;
		} else if (((__param0 !== undefined) && typeof __param0 === "boolean") && ((__param1 !== undefined) && typeof __param1 === "boolean")) {
			const printTime : boolean = __param0 as boolean;
			const printLevel : boolean = __param1 as boolean;
			this.printTime = printTime;
			this.printLevel = printLevel;
		} else throw new Error('invalid method overload');
	}

	/**
	 * Diese Methode implementiert das funktionale Interface java.util.function.Consumer
	 * und gibt die empfangenen Log-Informationen auf der Kommandozeile aus.
	 *
	 * @param t   die auszugebenden Log-Informationen
	 */
	public accept(t : LogData) : void {
		const s : string | null = ((this.lastLogDataHadNewLine && this.printTime) ? DateUtils.toISO8601(t.getTime())! + " " : "") + ((this.lastLogDataHadNewLine && this.printLevel) ? t.getLevel() + " " : "") + t.getText()!;
		if (t.isNewLine())
			console.log(JSON.stringify(s));
		else
			console.log(JSON.stringify(s));
		this.lastLogDataHadNewLine = t.isNewLine();
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.logger.LogConsumerConsole';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['java.util.function.Consumer', 'de.svws_nrw.core.logger.LogConsumerConsole'].includes(name);
	}

}

export function cast_de_svws_nrw_core_logger_LogConsumerConsole(obj : unknown) : LogConsumerConsole {
	return obj as LogConsumerConsole;
}
