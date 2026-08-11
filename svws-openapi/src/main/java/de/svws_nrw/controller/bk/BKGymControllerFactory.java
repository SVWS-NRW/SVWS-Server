package de.svws_nrw.controller.bk;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.bk.BKGymAbiturdatenService;
import de.svws_nrw.service.bk.BKGymLeistungsdatenService;
import de.svws_nrw.service.bk.BKGymServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Die Controller-Factory für die ENM-Daten in Version 1
 */
public final class BKGymControllerFactory {

	/** Die Service-Factory für das Berufliche Gymnasium */
	private final BKGymServiceFactory serviceFactory;


	/**
	 * Erzeugt eine neue Factory für die übergebene Datenbank-Verbindung.
	 * Der Konstruktor ist package private und sollte nur von einer Default-Methode
	 * im Interface aufgerufen werden.
	 */
	private BKGymControllerFactory() {
		this.serviceFactory = BKGymServiceFactory.getNewInstance(
				SchuelerRepositoryFactory.getNewInstance(),
				EigeneSchuleRepositoryFactory.getNewInstance(),
				KatalogRepositoryFactory.getNewInstance());
	}


	/**
	 * Diese statische Methode dient dem Zugriff auf die Datenbank in der API-Schicht.
	 *
	 * @param request  der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return der spezielle Servlet-Controller
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	public static BKGymControllerFactory withReadAccess(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Repositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN);
		return new BKGymControllerFactory();
	}


	/**
	 * Diese statische Methode dient dem Zugriff auf die in der API-Schicht.
	 *
	 * @param request  der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return der spezielle Servlet-Controller
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	public static BKGymControllerFactory withReadAccessFunktionsbezogen(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Repositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN, BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN);
		return new BKGymControllerFactory();
	}


	/**
	 * Erstellt einen Controller für die Endpunkte des Beruflichen Gymnasiums
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public BKGymController getBKGymController() throws ApiOperationException {
		final BKGymAbiturdatenService abidatenService = serviceFactory.getAbiturdatenService();
		final BKGymLeistungsdatenService leistungsdatenService = serviceFactory.getLeistungsdatenService();
		return new BKGymControllerImpl(abidatenService, leistungsdatenService);
	}

}
