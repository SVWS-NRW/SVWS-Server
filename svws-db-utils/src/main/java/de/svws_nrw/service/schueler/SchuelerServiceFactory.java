package de.svws_nrw.service.schueler;

/**
 * Eine Factory zum Erstellen der Services für das Berufliche Gymnasium
 */
public final class SchuelerServiceFactory {

	/**
	 * Erstellt eine neue Service-Factory
	 */
	private SchuelerServiceFactory() {
		// keine Daten
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @return die Factory
	 */
	public static SchuelerServiceFactory getNewInstance() {
		return new SchuelerServiceFactory();
	}


	/**
	 * Erstellt einen neuen Service für die SchuelerSprachenfolge
	 *
	 * @return der Service
	 */
	public static SchuelerSprachenfolgeService getSchuelerSprachenfolgeService() {
		return new SchuelerSprachenfolgeService();
	}


	/**
	 * Erstellt einen neuen Service für die SchuelerSprachpruefungen
	 *
	 * @return der Service
	 */
	public static SchuelerSprachpruefungenService getSchuelerSprachpruefungenService() {
		return new SchuelerSprachpruefungenService();
	}

}
