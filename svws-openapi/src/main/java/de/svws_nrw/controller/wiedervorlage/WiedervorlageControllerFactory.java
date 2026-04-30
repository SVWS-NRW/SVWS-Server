package de.svws_nrw.controller.wiedervorlage;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.service.wiedervorlage.WiedervorlageServiceFactory;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.wiedervorlage.WiedervorlageRepositoryFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für {@link WiedervorlageController}.
 */
public final class WiedervorlageControllerFactory {

	private final WiedervorlageServiceFactory wiedervorlageServiceFactory;

	private WiedervorlageControllerFactory(final WiedervorlageServiceFactory wiedervorlageServiceFactory) {
		this.wiedervorlageServiceFactory = wiedervorlageServiceFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Controller-Factory.
	 *
	 * @param request der {@link HttpServletRequest}
	 * @param benutzerKompetenz die Benutzerkompetenz
	 *
	 * @return neu erzeugte Controller-Factory
	 */
	private static WiedervorlageControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, benutzerKompetenz);
		final var wiedervorlageRepositoryFactory = WiedervorlageRepositoryFactory.getNewInstance();
		final var benutzerRepositoryFactory = BenutzerRepositoryFactory.getNewInstance();
		final var serviceFactory = WiedervorlageServiceFactory.getNewInstance(wiedervorlageRepositoryFactory, benutzerRepositoryFactory);
		return new WiedervorlageControllerFactory(serviceFactory);
	}

	/**
	 * Lesezugriff – {@link BenutzerKompetenz#KEINE}.
	 *
	 * @param request der {@link HttpServletRequest}
	 *
	 * @return neu erzeugte Controller-Factory
	 */
	public static WiedervorlageControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Schreibzugriff – {@link BenutzerKompetenz#KEINE}.
	 *
	 * @param request der {@link HttpServletRequest}
	 *
	 * @return neu erzeugte Controller-Factory
	 */
	public static WiedervorlageControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Löschzugriff – {@link BenutzerKompetenz#KEINE}.
	 *
	 * @param request der {@link HttpServletRequest}
	 *
	 * @return neu erzeugte Controller-Factory
	 */
	public static WiedervorlageControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Erzeugt einen neuen {@link WiedervorlageController}.
	 *
	 * @return neu erzeugter {@link WiedervorlageController}
	 */
	public WiedervorlageController getWiedervorlageController() {
		return new WiedervorlageController(wiedervorlageServiceFactory.getWiedervorlageService());
	}

}
