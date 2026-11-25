package de.svws_nrw.controller.kurse;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.service.kurse.KursServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für den {@link KurslehrerController}. Sie übernimmt die Prüfung der Berechtigung und den Aufbau der
 * Datenbank-Verbindung.
 */
public final class KurslehrerControllerFactory {

	private final KursServiceFactory kursServiceFactory;

	private KurslehrerControllerFactory(final KursServiceFactory kursServiceFactory) {
		this.kursServiceFactory = kursServiceFactory;
	}

	private static KurslehrerControllerFactory getNewInstance(final HttpServletRequest request, final ServerMode serverMode,
			final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, serverMode, benutzerKompetenz);
		return new KurslehrerControllerFactory(KursServiceFactory.getNewInstance());
	}

	/**
	 * Erstellt eine Factory-Instanz ohne erforderliche Benutzerkompetenz.
	 *
	 * @param request   die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine KurslehrerControllerFactory-Instanz ohne zusätzliche Berechtigungsanforderung
	 */
	public static KurslehrerControllerFactory withNoAccess(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigung. Das Anlegen und Ändern ist nur im Server-Modus DEV erlaubt.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN}.
	 * </p>
	 *
	 * @param request   die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine KurslehrerControllerFactory-Instanz mit Schreibberechtigung im Modus DEV
	 */
	public static KurslehrerControllerFactory withWriteAccessDev(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.DEV, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung. Das Löschen ist im Server-Modus STABLE erlaubt.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#KATALOG_EINTRAEGE_LOESCHEN}.
	 * </p>
	 *
	 * @param request   die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine KurslehrerControllerFactory-Instanz mit Löschberechtigung im Modus STABLE
	 */
	public static KurslehrerControllerFactory withDeleteAccessStable(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Erstellt eine neue KurslehrerController-Instanz.
	 *
	 * @return ein neuer KurslehrerController mit dem konfigurierten KurslehrerService
	 */
	public KurslehrerController getKurslehrerController() {
		return new KurslehrerControllerImpl(kursServiceFactory.getKurslehrerService());
	}

}
