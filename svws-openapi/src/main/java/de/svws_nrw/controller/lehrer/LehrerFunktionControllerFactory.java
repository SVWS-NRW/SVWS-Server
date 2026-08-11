package de.svws_nrw.controller.lehrer;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.mapper.lehrer.LehrerFunktionMapper;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public class LehrerFunktionControllerFactory {

	private final LehrerFunktionServiceFactory serviceFactory;

	/**
	 * @param serviceFactory {@link LehrerFunktionServiceFactory}
	 */
	public LehrerFunktionControllerFactory(final LehrerFunktionServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	/**
	 * @param request {@link HttpServletRequest}
	 * @param kompetenz {@link BenutzerKompetenz}
	 * @return {@link LehrerFunktionControllerFactory}
	 */
	public static LehrerFunktionControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz kompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, kompetenz);
		final var repoFactory = LehrerRepositoryFactory.getNewInstance();
		final var mapper = LehrerFunktionMapper.INSTANCE;
		final var serviceFactory = LehrerFunktionServiceFactory.getNewInstance(
				repoFactory, mapper);
		return new LehrerFunktionControllerFactory(serviceFactory);
	}

	/**
	 * create factory with write access
	 * @param request {@link HttpServletRequest}
	 * @return {@link LehrerFunktionControllerFactory}
	 */
	public static LehrerFunktionControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}

	/**
	 * create factory with read access
	 * @param request {@link HttpServletRequest}
	 * @return {@link LehrerFunktionControllerFactory}
	 */
	public static LehrerFunktionControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}

	/**
	 * @return {@link LehrerFunktionController}
	 */
	public LehrerFunktionController getController() {
		return new LehrerFunktionController(serviceFactory.getLehrerFunktionService());
	}

}
