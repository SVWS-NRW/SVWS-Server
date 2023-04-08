package de.svws_nrw.core;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/**
 * Diese generische Klasse dient als Basisklasse für einfache Dienste bzw. Algorithmen,
 * die im SVWS-Core definiert werden und zu einem Input-Objekt vom Typ T_IN ein
 * Output-Objekt vom Typ T_OUT erzeugen.
 *
 * @param <T_IN>    die Klasse des Input-Objektes
 * @param <T_OUT>   die Klasse des Output-Objektes
 */
public abstract class Service<@NotNull T_IN, @NotNull T_OUT> {

	/** Die Instanz des Logger, der von diesem Service genutzt wird */
	protected @NotNull Logger logger = new Logger();

	/** Die Instanz des Consumers von Log-Informationen. In diesem Fall ein einfacher Vektor */
	protected @NotNull LogConsumerList log = new LogConsumerList();

	/**
	 * Erstellt einen neuen Service, dessen Logger automatisch in einen ArrayList loggt.
	 */
	protected Service() {
		this.logger.addConsumer(log);
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
	public abstract T_OUT handle(T_IN input);


	/**
	 * Gibt die Logger-Instanz von diesem Service zurück.
	 *
	 * @return die Logger-Instanz.
	 */
	public @NotNull Logger getLogger() {
		return logger;
	}


	/**
	 * Gibt das Log dieses Services zurück.
	 *
	 * @return das Log dieses Services
	 */
	public @NotNull LogConsumerList getLog() {
		return log;
	}

}
