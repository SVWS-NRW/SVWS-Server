package de.svws_nrw.data.kataloge.teilleistungsarten;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für {@link TeilLeistungsartController}
 */
public final class TeilLeistungsartControllerFactory {

	private final TeilLeistungsartServiceFactory teilLeistungsartServiceFactory;

	private TeilLeistungsartControllerFactory(final TeilLeistungsartServiceFactory teilLeistungsartServiceFactory) {
		this.teilLeistungsartServiceFactory = teilLeistungsartServiceFactory;

	}


	/**
	 * Erzeugt eine neue Instanz der Controller-Factory
	 *
	 * @param request der {@link HttpServletRequest}
	 * @param kompetenz benötigte Benutzerkompetenzen
	 *
	 * @return neu erzeugte Controller Factory
	 */
	private static TeilLeistungsartControllerFactory getNewInstance(final HttpServletRequest request,
			final BenutzerKompetenz kompetenz) {

		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, kompetenz);
		final var katalogeRepositoryFactory = KatalogeRepositoryFactory.getNewInstance();
		final var serviceFactory = TeilLeistungsartServiceFactory.getNewInstance(katalogeRepositoryFactory);

		return new TeilLeistungsartControllerFactory(serviceFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Controller-Factory mit BenutzerKompetenz.KEINE
	 *
	 * @param request der {@link HttpServletRequest}
	 * @return neu erzeugte Controller Factory
	 */
	public static TeilLeistungsartControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Erzeugt eine neue Instanz der Controller-Factory mit BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN
	 *
	 * @param request der {@link HttpServletRequest}
	 * @return neu erzeugte Controller Factory
	 */
	public static TeilLeistungsartControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Erzeugt eine neue Instanz der Controller-Factory mit BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN
	 *
	 * @param request der {@link HttpServletRequest}
	 * @return neu erzeugte Controller Factory
	 */
	public static TeilLeistungsartControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Erstellt einen neuen TeilLeistungsartenController.
	 *
	 * @return {@link TeilLeistungsartController} - neu erzeugter Controller
	 */
	public TeilLeistungsartController getTeilLeistungsartenController() {
		return new TeilLeistungsartController(teilLeistungsartServiceFactory.getTeilLeistungsartenService());
	}
}
