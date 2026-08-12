package de.svws_nrw.controller.lehrer;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public class LehrerPersonalabschnittsdatenControllerFactory {

	private final LehrerServiceFactory serviceFactory;


	/**
	 * @param serviceFactory {@link LehrerServiceFactory}
	 */
	public LehrerPersonalabschnittsdatenControllerFactory(final LehrerServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	/**
	 * @param request {@link HttpServletRequest}
	 * @param kompetenz {@link BenutzerKompetenz}
	 * @return {@link LehrerPersonalabschnittsdatenControllerFactory}
	 */
	public static LehrerPersonalabschnittsdatenControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz kompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, kompetenz);
		final var serviceFactory = LehrerServiceFactory.getNewInstance(
				LehrerRepositoryFactory.getNewInstance(),
				EigeneSchuleRepositoryFactory.getNewInstance(),
				KatalogRepositoryFactory.getNewInstance()
				);
		return new LehrerPersonalabschnittsdatenControllerFactory(serviceFactory);
	}

	/**
	 * create factory with write access
	 * @param request {@link HttpServletRequest}
	 * @return {@link LehrerPersonalabschnittsdatenControllerFactory}
	 */
	public static LehrerPersonalabschnittsdatenControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}

	/**
	 * create factory with read access
	 * @param request {@link HttpServletRequest}
	 * @return {@link LehrerPersonalabschnittsdatenControllerFactory}
	 */
	public static LehrerPersonalabschnittsdatenControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}

	/**
	 * @return {@link LehrerPersonalabschnittsdatenController}
	 */
	public LehrerPersonalabschnittsdatenController getController() {
		return new LehrerPersonalabschnittsdatenController(serviceFactory.getLehrerPersonalabschnittsdatenService());
	}

}
