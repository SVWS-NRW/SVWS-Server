package de.svws_nrw.controller.gost.klausurplan;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.gost.GostRepositoryFactory;
import de.svws_nrw.repo.gost.klausurplan.GostKlausurenRepositoryFactory;
import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;
import de.svws_nrw.service.gost.klausurplan.GostKlausurenServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Eine Controller-Factory für die GOSt-Klausurplanung.
 */
public final class GostKlausurenControllerFactory {

	private final GostKlausurenServiceFactory gostKlausurenServiceFactory;

	private GostKlausurenControllerFactory() {
		final var repositoryFactory = GostKlausurenRepositoryFactory.getNewInstance();
		this.gostKlausurenServiceFactory = GostKlausurenServiceFactory.getNewInstance(repositoryFactory, GostRepositoryFactory.getNewInstance(),
				KatalogeRepositoryFactory.getNewInstance());
	}

	/**
	 * Erzeugt eine Controller-Factory mit Leseberechtigung.
	 *
	 * @param request der HTTP-Request
	 *
	 * @return die Controller-Factory
	 */
	public static GostKlausurenControllerFactory withReadAccess(final HttpServletRequest request) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION);
		return new GostKlausurenControllerFactory();
	}

	/**
	 * Erzeugt eine Controller-Factory mit Schreibberechtigung.
	 *
	 * @param request der HTTP-Request
	 *
	 * @return die Controller-Factory
	 */
	public static GostKlausurenControllerFactory withWriteAccess(final HttpServletRequest request) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_AENDERN);
		return new GostKlausurenControllerFactory();
	}

	/**
	 * Erstellt einen Controller für die GOSt-Klausurvorgaben.
	 *
	 * @return der Controller
	 */
	public GostKlausurenVorgabeController getGostKlausurenVorgabeController() {
		return new GostKlausurenVorgabeControllerImpl(gostKlausurenServiceFactory.getGostKlausurenVorgabeService());
	}

}
