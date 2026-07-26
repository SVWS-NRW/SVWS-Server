package de.svws_nrw.controller.gost.klausuren;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenRepositoryFactory;
import de.svws_nrw.service.gost.GostServiceFactoryBuilder;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Eine Controller-Factory für die GOSt-Klausurplanung.
 */
public final class GostKlausurenControllerFactory {

	private final GostKlausurenServiceFactory gostKlausurenServiceFactory;

	private GostKlausurenControllerFactory() {
		final var repositoryFactory = GostKlausurenRepositoryFactory.getNewInstance();
		this.gostKlausurenServiceFactory = GostKlausurenServiceFactory.getNewInstance(repositoryFactory, GostServiceFactoryBuilder.getGostServiceFactory());
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

	/**
	 * Erstellt einen Controller für Vorlagenoperationen auf GOSt-Klausurvorgaben.
	 *
	 * @return der Controller
	 */
	public GostKlausurenVorgabeVorlagenController getGostKlausurenVorgabeVorlagenController() {
		return new GostKlausurenVorgabeVorlagenControllerImpl(gostKlausurenServiceFactory.getGostKlausurenVorgabeVorlagenService());
	}

	/**
	 * Erstellt einen Controller für die GOSt-Klausurtermine.
	 *
	 * @return der Controller
	 */
	public GostKlausurenTerminController getGostKlausurenTerminController() {
		return new GostKlausurenTerminControllerImpl(gostKlausurenServiceFactory.getGostKlausurenTerminService());
	}

	/**
	 * Erstellt einen Controller für die GOSt-Kursklausuren.
	 *
	 * @return der Controller
	 */
	public GostKlausurenKursklausurController getGostKlausurenKursklausurController() {
		return new GostKlausurenKursklausurControllerImpl(gostKlausurenServiceFactory.getGostKlausurenKursklausurService());
	}

	/**
	 * Erstellt einen Controller für die GOSt-Schülerklausuren.
	 *
	 * @return der Controller
	 */
	public GostKlausurenSchuelerklausurController getGostKlausurenSchuelerklausurController() {
		return new GostKlausurenSchuelerklausurControllerImpl(gostKlausurenServiceFactory.getGostKlausurenSchuelerklausurService());
	}

	/**
	 * Erstellt einen Controller für die GOSt-Schülerklausurtermine.
	 *
	 * @return der Controller
	 */
	public GostKlausurenSchuelerklausurterminController getGostKlausurenSchuelerklausurterminController() {
		return new GostKlausurenSchuelerklausurterminControllerImpl(gostKlausurenServiceFactory.getGostKlausurenSchuelerklausurterminService());
	}

	/**
	 * Erstellt einen Controller für die GOSt-Klausurräume.
	 *
	 * @return der Controller
	 */
	public GostKlausurenRaumController getGostKlausurenRaumController() {
		return new GostKlausurenRaumControllerImpl(gostKlausurenServiceFactory.getGostKlausurenRaumService());
	}

	/**
	 * Erstellt einen Controller für die GOSt-Klausurraumstunden.
	 *
	 * @return der Controller
	 */
	public GostKlausurenRaumstundeController getGostKlausurenRaumstundeController() {
		return new GostKlausurenRaumstundeControllerImpl(gostKlausurenServiceFactory.getGostKlausurenRaumstundeService());
	}

	/**
	 * Erstellt einen Controller für die GOSt-Schülerklausurtermin-Raumstunden-Zuordnungen.
	 *
	 * @return der Controller
	 */
	public GostKlausurenSchuelerklausurterminraumstundeController getGostKlausurenSchuelerklausurterminraumstundeController() {
		return new GostKlausurenSchuelerklausurterminraumstundeControllerImpl(
				gostKlausurenServiceFactory.getGostKlausurenSchuelerklausurterminraumstundeService());
	}

	/**
	 * Erstellt einen Controller für aggregierte GOSt-Klausurdaten.
	 *
	 * @return der Controller
	 */
	public GostKlausurenKlausurdatenController getGostKlausurenKlausurdatenController() {
		return new GostKlausurenKlausurdatenControllerImpl(
				gostKlausurenServiceFactory.getGostKlausurenAllDataService(),
				gostKlausurenServiceFactory.getGostKlausurenKlausurdatenIssuesService(),
				gostKlausurenServiceFactory.getGostKlausurenSchuelerKlausurdatenService());
	}

	/**
	 * Erstellt einen Controller für höherwertige Kursklausur-Workflows.
	 *
	 * @return der Controller
	 */
	public GostKlausurenKursklausurWorkflowController getGostKlausurenKursklausurWorkflowController() {
		return new GostKlausurenKursklausurWorkflowControllerImpl(
				gostKlausurenServiceFactory.getGostKlausurenKursklausurPatchService(),
				gostKlausurenServiceFactory.getGostKlausurenKursklausurCreationService(),
				gostKlausurenServiceFactory.getGostKlausurenKursklausurBlockungService());
	}

	/**
	 * Erstellt einen Controller für höherwertige Schülerklausur-Workflows.
	 *
	 * @return der Controller
	 */
	public GostKlausurenSchuelerklausurWorkflowController getGostKlausurenSchuelerklausurWorkflowController() {
		return new GostKlausurenSchuelerklausurWorkflowControllerImpl(
				gostKlausurenServiceFactory.getGostKlausurenSchuelerklausurCreationService());
	}

	/**
	 * Erstellt einen Controller für höherwertige Klausurtermin-Workflows.
	 *
	 * @return der Controller
	 */
	public GostKlausurenTerminWorkflowController getGostKlausurenTerminWorkflowController() {
		return new GostKlausurenTerminWorkflowControllerImpl(gostKlausurenServiceFactory.getGostKlausurenTerminPatchService());
	}

	/**
	 * Erstellt einen Controller für höherwertige Schülerklausurtermin-Workflows.
	 *
	 * @return der Controller
	 */
	public GostKlausurenSchuelerklausurterminWorkflowController getGostKlausurenSchuelerklausurterminWorkflowController() {
		return new GostKlausurenSchuelerklausurterminWorkflowControllerImpl(
				gostKlausurenServiceFactory.getGostKlausurenSchuelerklausurterminCreationService(),
				gostKlausurenServiceFactory.getGostKlausurenSchuelerklausurterminPatchService(),
				gostKlausurenServiceFactory.getGostKlausurenNachschreibterminBlockungService());
	}

	/**
	 * Erstellt einen Controller für Raumzuweisungen.
	 *
	 * @return der Controller
	 */
	public GostKlausurenRaumzuweisungController getGostKlausurenRaumzuweisungController() {
		return new GostKlausurenRaumzuweisungControllerImpl(gostKlausurenServiceFactory.getGostKlausurenRaumzuweisungService());
	}

}
