package de.svws_nrw.controller.schueler;

import de.svws_nrw.controller.schueler.schulbesuch.SchuelerBisherigeSchuleController;
import de.svws_nrw.controller.schueler.schulbesuch.SchuelerBisherigeSchuleControllerImpl;
import de.svws_nrw.controller.schueler.schulbesuch.SchuelerMerkmalController;
import de.svws_nrw.controller.schueler.schulbesuch.SchuelerMerkmalControllerImpl;
import de.svws_nrw.controller.schueler.schulbesuch.SchuelerSchulbesuchController;
import de.svws_nrw.controller.schueler.schulbesuch.SchuelerSchulbesuchControllerImpl;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public final class SchuelerControllerFactory {

	private final SchuelerServiceFactory schuelerServiceFactory;

	private SchuelerControllerFactory(final SchuelerServiceFactory schuelerServiceFactory) {
		this.schuelerServiceFactory = schuelerServiceFactory;
	}

	private static SchuelerControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, benutzerKompetenz);
		final var schuelerRepoFactory = SchuelerRepositoryFactory.getNewInstance();
		final var katalogRepositoryFactory = KatalogRepositoryFactory.getNewInstance();
		final var benutzerRepositoryFactory = BenutzerRepositoryFactory.getNewInstance();
		final var serviceFactory = SchuelerServiceFactory.getNewInstance(benutzerRepositoryFactory, schuelerRepoFactory, katalogRepositoryFactory);

		return new SchuelerControllerFactory(serviceFactory);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#SCHUELER_INDIVIDUALDATEN_ANSEHEN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine SchuelerControllerFactory-Instanz mit Leseberechtigung
	 */
	public static SchuelerControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigungen.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#SCHUELER_INDIVIDUALDATEN_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine SchuelerControllerFactory-Instanz mit Schreibberechtigungen
	 */
	public static SchuelerControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#SCHUELER_INDIVIDUALDATEN_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine SchuelerControllerFactory-Instanz mit Löschberechtigung
	 */
	public static SchuelerControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
	}

	/**
	 * Erstellt eine neue BisherigeSchulenController-Instanz.
	 *
	 * @return ein neuer BisherigeSchulenController mit dem konfigurierten BisherigeSchulenService
	 */
	public SchuelerBisherigeSchuleController getSchuelerBisherigeSchuleController() {
		return new SchuelerBisherigeSchuleControllerImpl(schuelerServiceFactory.getSchuelerBisherigeSchuleService());
	}

	/**
	 * Erstellt eine neue SchuelerMerkmalController-Instanz.
	 *
	 * @return ein neuer SchuelerMerkmalController mit dem konfigurierten SchuelerMerkmalService
	 */
	public SchuelerMerkmalController getSchuelerMerkmalController() {
		return new SchuelerMerkmalControllerImpl(schuelerServiceFactory.getSchuelerMerkmalService());
	}

	/**
	 * Erstellt eine neue SchuelerSchulbesuchController-Instanz.
	 *
	 * @return ein neuer SchuelerSchulbesuchController mit dem konfigurierten SchulbesuchService
	 */
	public SchuelerSchulbesuchController getSchuelerSchulbesuchController() {
		return new SchuelerSchulbesuchControllerImpl(schuelerServiceFactory.getSchulbesuchService());
	}

}
