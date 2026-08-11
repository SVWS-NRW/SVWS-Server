package de.svws_nrw.controller.kataloge;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.mapper.Schild3FachklasseDQRNiveauZuordnungMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.schild3.Schild3FachklasseDQRNiveauZuordnungServiceFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für {@link Schild3FachklasseSchild3FachklasseDQRNiveauZuordnungZuordnungImpl}
 */
public final class Schild3FachklasseDQRNiveauZuordnungControllerFactory {

	private final Schild3FachklasseDQRNiveauZuordnungServiceFactory serviceFactory;

	private Schild3FachklasseDQRNiveauZuordnungControllerFactory(final Schild3FachklasseDQRNiveauZuordnungServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Controller-Factory
	 *
	 * @param request der {@link HttpServletRequest}
	 * @param kompetenz benötigte Benutzerkompetenzen
	 *
	 * @return neu erzeugte Controller Factory
	 */
	private static Schild3FachklasseDQRNiveauZuordnungControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz kompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, kompetenz);

		final var mapper = Schild3FachklasseDQRNiveauZuordnungMapper.INSTANCE;
		final var schuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		final var schuleServiceFactory = SchuleServiceFactory.getNewInstance(schuleRepositoryFactory);
		final var serviceFactory = Schild3FachklasseDQRNiveauZuordnungServiceFactory.getNewInstance(mapper, schuleRepositoryFactory, schuleServiceFactory);

		return new Schild3FachklasseDQRNiveauZuordnungControllerFactory(serviceFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Controller-Factory mit BenutzerKompetenz.KEINE
	 *
	 * @param request der {@link HttpServletRequest}
	 *
	 * @return neu erzeugte Controller Factory
	 */
	public static Schild3FachklasseDQRNiveauZuordnungControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Erstellt einen neuen {@link Schild3FachklasseSchild3FachklasseDQRNiveauZuordnungZuordnungImpl}.
	 *
	 * @return {@link Schild3FachklasseSchild3FachklasseDQRNiveauZuordnungZuordnungImpl} - neu erzeugter Controller
	 */
	public Schild3FachklasseSchild3FachklasseDQRNiveauZuordnungZuordnungImpl getDQRNiveauController() {
		return new Schild3FachklasseSchild3FachklasseDQRNiveauZuordnungZuordnungImpl(serviceFactory.getSchild3FachklasseDQRNiveauZuordnungService());
	}
}
