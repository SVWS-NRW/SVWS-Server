import { JavaObject } from '../java/lang/JavaObject';
import { LogConsumerList } from '../core/logger/LogConsumerList';
import { Logger } from '../core/logger/Logger';

export abstract class Service<T_IN, T_OUT> extends JavaObject {

	/**
	 * Die Instanz des Logger, der von diesem Service genutzt wird
	 */
	protected logger : Logger = new Logger();

	/**
	 * Die Instanz des Consumers von Log-Informationen. In diesem Fall ein einfacher Vektor
	 */
	protected log : LogConsumerList = new LogConsumerList();


	/**
	 * Erstellt einen neuen Service, dessen Logger automatisch in einen ArrayList loggt.
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
	public getLog() : LogConsumerList {
		return this.log;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.Service';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.Service'].includes(name);
	}

}

export function cast_de_svws_nrw_core_Service<T_IN, T_OUT>(obj : unknown) : Service<T_IN, T_OUT> {
	return obj as Service<T_IN, T_OUT>;
}
