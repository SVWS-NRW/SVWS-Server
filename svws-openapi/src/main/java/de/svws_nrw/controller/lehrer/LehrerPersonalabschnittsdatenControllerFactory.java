package de.svws_nrw.controller.lehrer;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.mapper.lehrer.LehrerPersonalabschnittsdatenMapper;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionServiceFactory;
import de.svws_nrw.service.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public class LehrerPersonalabschnittsdatenControllerFactory {

	private final LehrerPersonalabschnittsdatenServiceFactory serviceFactory;


	/**
	 * @param serviceFactory {@link LehrerPersonalabschnittsdatenServiceFactory}
	 */
	public LehrerPersonalabschnittsdatenControllerFactory(final LehrerPersonalabschnittsdatenServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	/**
	 * @param request {@link HttpServletRequest}
	 * @param kompetenz {@link BenutzerKompetenz}
	 * @return {@link LehrerPersonalabschnittsdatenControllerFactory}
	 */
	public static LehrerPersonalabschnittsdatenControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz kompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, kompetenz);
		final var serviceFactory = LehrerPersonalabschnittsdatenServiceFactory.getNewInstance(
				LehrerRepositoryFactory.getNewInstance(),
				KatalogRepositoryFactory.getNewInstance(),
				EigeneSchuleRepositoryFactory.getNewInstance(),
				LehrerServiceFactory.getNewInstance(),
				LehrerFunktionServiceFactory.getNewInstance(),
				LehrerPersonalabschnittsdatenMapper.INSTANCE
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
