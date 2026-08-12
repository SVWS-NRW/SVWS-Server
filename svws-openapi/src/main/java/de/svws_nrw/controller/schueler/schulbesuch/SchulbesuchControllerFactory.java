package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public final class SchulbesuchControllerFactory {

	private final SchuelerServiceFactory schuelerServiceFactory;

	private SchulbesuchControllerFactory(final SchuelerServiceFactory schuelerServiceFactory) {
		this.schuelerServiceFactory = schuelerServiceFactory;
	}

	private static SchulbesuchControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, benutzerKompetenz);
		final var benutzerRepoFactory = BenutzerRepositoryFactory.getNewInstance();
		final var schuelerRepoFactory = SchuelerRepositoryFactory.getNewInstance();
		final var katalogRepoFactory = KatalogRepositoryFactory.getNewInstance();
		return new SchulbesuchControllerFactory(SchuelerServiceFactory.getNewInstance(benutzerRepoFactory, schuelerRepoFactory, katalogRepoFactory));
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
		return new SchulbesuchController(schuelerServiceFactory.getSchulbesuchService());
	}

}
