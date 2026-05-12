package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.service.schueler.schulbesuch.SchulbesuchServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public final class SchulbesuchControllerFactory {

	private final SchulbesuchServiceFactory schulbesuchServiceFactory;

	private SchulbesuchControllerFactory(final SchulbesuchServiceFactory schulbesuchServiceFactory) {
		this.schulbesuchServiceFactory = schulbesuchServiceFactory;
	}

	private static SchulbesuchControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, benutzerKompetenz);
		return new SchulbesuchControllerFactory(SchulbesuchServiceFactory.getNewInstance());
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#SCHUELER_INDIVIDUALDATEN_ANSEHEN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine SchulbesuchControllerFactory-Instanz mit Leseberechtigung
	 */
	public static SchulbesuchControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigungen.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#SCHUELER_INDIVIDUALDATEN_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine SchulbesuchControllerFactory-Instanz mit Schreibberechtigungen
	 */
	public static SchulbesuchControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

	/**
	 * Erstellt eine neue SchulbesuchController-Instanz.
	 *
	 * @return ein neuer SchulbesuchController mit dem konfigurierten SchulbesuchService
	 */
	public SchulbesuchController getSchulbesuchController() {
		return new SchulbesuchController(schulbesuchServiceFactory.getSchulbesuchService());
	}

}
