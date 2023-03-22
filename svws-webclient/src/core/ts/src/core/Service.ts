import { JavaObject, cast_java_lang_Object } from '../java/lang/JavaObject';
import { Logger, cast_de_nrw_schule_svws_core_logger_Logger } from '../core/logger/Logger';
import { LogConsumerVector, cast_de_nrw_schule_svws_core_logger_LogConsumerVector } from '../core/logger/LogConsumerVector';

export abstract class Service<T_IN, T_OUT> extends JavaObject {

	/**
	 * Die Instanz des Logger, der von diesem Service genutzt wird
	 */
	protected logger : Logger = new Logger();

	/**
	 * Die Instanz des Consumers von Log-Informationen. In diesem Fall ein einfacher Vektor
	 */
	protected log : LogConsumerVector = new LogConsumerVector();


	/**
	 * Erstellt einen neuen Service, dessen Logger automatisch in einen Vector loggt. 
	 */
	protected constructor() {
		super();
		this.logger.addConsumer(this.log);
	}

	/**
	 * Diese Methode muss von dem erbenden Service implementiert werden 
	 * und handhabt das übergebene Input-Objekt und erzeugt das zugehörige
	 * Output-Objekt.  
	 *  
	 * @param input   das Input-Objekt
	 * 
	 * @return das Output-Objekt
	 */
	public abstract handle(input : T_IN | null) : T_OUT | null;

	/**
	 * Gibt die Logger-Instanz von diesem Service zurück.  
	 * 
	 * @return die Logger-Instanz.
	 */
	public getLogger() : Logger {
		return this.logger;
	}

	/**
	 * Gibt das Log dieses Services zurück.
	 * 
	 * @return das Log dieses Services
	 */
	public getLog() : LogConsumerVector {
		return this.log;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.Service'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_Service<T_IN, T_OUT>(obj : unknown) : Service<T_IN, T_OUT> {
	return obj as Service<T_IN, T_OUT>;
}
