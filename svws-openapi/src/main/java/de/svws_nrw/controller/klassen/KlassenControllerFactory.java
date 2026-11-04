package de.svws_nrw.controller.klassen;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.service.klassen.KlassenServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Eine Factory zum Erstellen der Klassen-Controller. Die Prüfung der Benutzerkompetenz und der Aufbau der
 * Datenbankverbindung erfolgen ausschließlich beim Erstellen der Factory.
 */
public final class KlassenControllerFactory {

	private final KlassenServiceFactory klassenServiceFactory;

	/**
	 * Erstellt eine neue Factory.
	 *
	 * @param klassenServiceFactory   die Factory für die Klassen-Services
	 */
	private KlassenControllerFactory(final KlassenServiceFactory klassenServiceFactory) {
		this.klassenServiceFactory = klassenServiceFactory;
	}

	/**
	 * Baut die Datenbankverbindung für den Benutzer auf, prüft die Kompetenzen und erstellt die Factory.
	 *
	 * @param request             die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @param benutzerKompetenz   die erforderlichen Benutzerkompetenzen
	 *
	 * @return die neue Factory
	 */
	private static KlassenControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz... benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, benutzerKompetenz);
		return new KlassenControllerFactory(KlassenServiceFactory.getNewInstance());
	}

	/**
	 * Erstellt eine Factory-Instanz mit Leseberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#UNTERRICHTSVERTEILUNG_ANSEHEN}.
	 * </p>
	 *
	 * @param request   die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine KlassenControllerFactory-Instanz mit Leseberechtigung
	 */
	public static KlassenControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN);
	}

	/**
	 * Erstellt eine Factory-Instanz ohne erforderliche Benutzerkompetenz.
	 *
	 * @param request   die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine KlassenControllerFactory-Instanz ohne zusätzliche Berechtigungsanforderung
	 */
	public static KlassenControllerFactory withNoAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Erstellt eine neue KlassenController-Instanz.
	 *
	 * @return ein neuer KlassenController mit dem konfigurierten KlassenService
	 */
	public KlassenController getKlassenController() {
		return new KlassenControllerImpl(klassenServiceFactory.getKlassenService());
	}

}
