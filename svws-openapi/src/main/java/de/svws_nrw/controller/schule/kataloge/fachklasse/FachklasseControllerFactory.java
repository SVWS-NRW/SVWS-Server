package de.svws_nrw.controller.schule.kataloge.fachklasse;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.KatalogServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public final class FachklasseControllerFactory {

	private final KatalogServiceFactory serviceFactory;

	/**
	 * @param serviceFactory {@link KatalogServiceFactory}
	 */
	public FachklasseControllerFactory(final KatalogServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	private static FachklasseControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, benutzerKompetenz);
		final var serviceFactory = KatalogServiceFactory.getNewInstance(
				KatalogRepositoryFactory.getNewInstance(),
				EigeneSchuleServiceFactory.getNewInstance(EigeneSchuleRepositoryFactory.getNewInstance())
		);
		return new FachklasseControllerFactory(serviceFactory);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Leseberechtigung.
	 * <p>
	 * Es werden keine besonderen Benutzerkompetenzen benötigt.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine FachklasseControllerFactory-Instanz mit Leseberechtigung
	 */
	public static FachklasseControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#KATALOG_EINTRAEGE_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine FachklasseControllerFactory-Instanz mit Schreibberechtigung
	 */
	public static FachklasseControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#KATALOG_EINTRAEGE_LOESCHEN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine FachklasseControllerFactory-Instanz mit Löschberechtigung
	 */
	public static FachklasseControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * @return ein neuer FachklasseController mit dem konfigurierten FachklasseSerice
	 */
	public FachklasseController getController() {
		return new FachklasseController(serviceFactory.getFachklasseService());
	}

}
