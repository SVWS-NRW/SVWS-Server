package de.svws_nrw.controller.kurse;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.service.kurse.KursServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für den {@link KursController}. Sie übernimmt die Prüfung der Berechtigung und den Aufbau der
 * Datenbank-Verbindung.
 */
public final class KursControllerFactory {

	private final KursServiceFactory kursServiceFactory;

	private KursControllerFactory(final KursServiceFactory kursServiceFactory) {
		this.kursServiceFactory = kursServiceFactory;
	}

	private static KursControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, benutzerKompetenz);
		return new KursControllerFactory(KursServiceFactory.getNewInstance());
	}

	/**
	 * Erstellt eine Factory-Instanz mit Leseberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#UNTERRICHTSVERTEILUNG_ANSEHEN}.
	 * </p>
	 *
	 * @param request   die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine KursControllerFactory-Instanz mit Leseberechtigung
	 */
	public static KursControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN);
	}

	/**
	 * Erstellt eine Factory-Instanz ohne erforderliche Benutzerkompetenz.
	 *
	 * @param request   die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine KursControllerFactory-Instanz ohne zusätzliche Berechtigungsanforderung
	 */
	public static KursControllerFactory withNoAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Erstellt eine neue KursController-Instanz.
	 *
	 * @return ein neuer KursController mit dem konfigurierten KursService
	 */
	public KursController getKursController() {
		return new KursControllerImpl(kursServiceFactory.getKursService());
	}

}
