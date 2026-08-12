package de.svws_nrw.controller.schule.merkmale;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.KatalogServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public final class MerkmalControllerFactory {

	private final KatalogServiceFactory katalogServiceFactory;

	/**
	 * Erstellt eine neue MerkmalControllerFactory mit der angegebenen Service-Factory.
	 *
	 * @param katalogServiceFactory die Factory zur Erstellung von MerkmalService-Instanzen
	 */
	public MerkmalControllerFactory(final KatalogServiceFactory katalogServiceFactory) {
		this.katalogServiceFactory = katalogServiceFactory;
	}

	private static MerkmalControllerFactory getNewInstance(final HttpServletRequest requst, final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(requst, ServerMode.STABLE, benutzerKompetenz);
		final var katalogRepositoryFactory = KatalogRepositoryFactory.getNewInstance();
		final var eigeneSchuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		final var eigeneSchuleServiceFactory = EigeneSchuleServiceFactory.getNewInstance(eigeneSchuleRepositoryFactory);
		final var merkmalServiceFactory = KatalogServiceFactory.getNewInstance(katalogRepositoryFactory, eigeneSchuleServiceFactory);

		return new MerkmalControllerFactory(merkmalServiceFactory);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Leseberechtigung.
	 * <p>
	 * Es werden keine besonderen Benutzerkompetenzen benötigt.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine MerkmalControllerFactory-Instanz mit Leseberechtigung
	 */
	public static MerkmalControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#KATALOG_EINTRAEGE_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine MerkmalControllerFactory-Instanz mit Schreibberechtigung
	 */
	public static MerkmalControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#KATALOG_EINTRAEGE_LOESCHEN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine MerkmalControllerFactory-Instanz mit Löschberechtigung
	 */
	public static MerkmalControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Erstellt eine neue MerkmalController-Instanz.
	 *
	 * @return ein neuer MerkmalController mit dem konfigurierten MerkmalService
	 */
	public MerkmalController getMerkmalController() {
		return new MerkmalController(katalogServiceFactory.getMerkmalService());
	}

}
