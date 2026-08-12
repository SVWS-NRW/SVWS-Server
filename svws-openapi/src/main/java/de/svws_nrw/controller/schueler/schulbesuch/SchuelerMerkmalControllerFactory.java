package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public class SchuelerMerkmalControllerFactory {

	private final SchuelerServiceFactory factory;


	/**
	 * Erstellt eine neue SchuelerMerkmalControllerFactory mit der angegebenen Service-Factory.
	 *
	 * @param factory die Factory zur Erstellung von SchuelerMerkmalService-Instanzen
	 */
	public SchuelerMerkmalControllerFactory(final SchuelerServiceFactory factory) {
		this.factory = factory;
	}

	private static SchuelerMerkmalControllerFactory getNewInstance(final HttpServletRequest request) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
		final var schuelerRepoFactory = SchuelerRepositoryFactory.getNewInstance();
		final var katalogRepositoryFactory = KatalogRepositoryFactory.getNewInstance();
		final var benutzerRepositoryFactory = BenutzerRepositoryFactory.getNewInstance();
		final var serviceFactory = SchuelerServiceFactory.getNewInstance(benutzerRepositoryFactory, schuelerRepoFactory, katalogRepositoryFactory);

		return new SchuelerMerkmalControllerFactory(serviceFactory);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#SCHUELER_INDIVIDUALDATEN_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine SchuelerMerkmalControllerFactory-Instanz mit Schreibberechtigung
	 */
	public static SchuelerMerkmalControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#SCHUELER_INDIVIDUALDATEN_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine SchuelerMerkmalControllerFactory-Instanz mit Löschberechtigung
	 */
	public static SchuelerMerkmalControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request);
	}


	/**
	 * Erstellt eine neue SchuelerMerkmalController-Instanz.
	 *
	 * @return ein neuer SchuelerMerkmalController mit dem konfigurierten SchuelerMerkmalService
	 */
	public SchuelerMerkmalController getBisherigeSchulenController() {
		return new SchuelerMerkmalController(factory.getSchuelerMerkmalService());
	}

}
