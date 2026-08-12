package de.svws_nrw.controller.katalog.teilleistungsart;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.KatalogServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für {@link TeilleistungsartController}
 */
public final class TeilleistungsartControllerFactory {

	private final KatalogServiceFactory katalogServiceFactory;

	private TeilleistungsartControllerFactory(final KatalogServiceFactory katalogServiceFactory) {
		this.katalogServiceFactory = katalogServiceFactory;

	}


	/**
	 * Erzeugt eine neue Instanz der Controller-Factory
	 *
	 * @param request der {@link HttpServletRequest}
	 * @param kompetenz benötigte Benutzerkompetenzen
	 *
	 * @return neu erzeugte Controller Factory
	 */
	private static TeilleistungsartControllerFactory getNewInstance(final HttpServletRequest request,
			final BenutzerKompetenz kompetenz) {

		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, kompetenz);
		final var katalogeRepositoryFactory = KatalogRepositoryFactory.getNewInstance();
		final var eigeneSchuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		final var eigeneSchuleServiceFactory = EigeneSchuleServiceFactory.getNewInstance(eigeneSchuleRepositoryFactory);
		final var serviceFactory = KatalogServiceFactory.getNewInstance(katalogeRepositoryFactory, eigeneSchuleServiceFactory);

		return new TeilleistungsartControllerFactory(serviceFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Controller-Factory mit BenutzerKompetenz.KEINE
	 *
	 * @param request der {@link HttpServletRequest}
	 * @return neu erzeugte Controller Factory
	 */
	public static TeilleistungsartControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Erzeugt eine neue Instanz der Controller-Factory mit BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN
	 *
	 * @param request der {@link HttpServletRequest}
	 * @return neu erzeugte Controller Factory
	 */
	public static TeilleistungsartControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Erzeugt eine neue Instanz der Controller-Factory mit BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN
	 *
	 * @param request der {@link HttpServletRequest}
	 * @return neu erzeugte Controller Factory
	 */
	public static TeilleistungsartControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Erstellt einen neuen TeilLeistungsartenController.
	 *
	 * @return {@link TeilleistungsartController} - neu erzeugter Controller
	 */
	public TeilleistungsartController getTeilLeistungsartenController() {
		return new TeilleistungsartController(katalogServiceFactory.getTeilLeistungsartenService());
	}
}
