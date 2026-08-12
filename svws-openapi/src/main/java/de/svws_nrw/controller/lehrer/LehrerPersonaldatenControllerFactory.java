package de.svws_nrw.controller.lehrer;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeService;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungService;
import de.svws_nrw.service.lehrer.unterrichtsfach.LehrerUnterrichtsfachService;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Die Default-Implementierung einer Controller-Factory für den Bereich der Lehrer
 */
public final class LehrerPersonaldatenControllerFactory {

	/** Die Service-Factory für die Lehrer */
	private final LehrerServiceFactory serviceFactory;


	/**
	 * Erzeugt eine neue Factory für die übergebene Datenbank-Verbindung.
	 * Der Konstruktor ist package private und sollte nur von einer Default-Methode
	 * im Interface aufgerufen werden.
	 */
	private LehrerPersonaldatenControllerFactory() {
		final var lehrerRepositoryFactory = LehrerRepositoryFactory.getNewInstance();
		final var schuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		final var katalogRepoFactory = KatalogRepositoryFactory.getNewInstance();
		this.serviceFactory = LehrerServiceFactory.getNewInstance(lehrerRepositoryFactory, schuleRepositoryFactory, katalogRepoFactory);
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
	public static LehrerPersonaldatenControllerFactory withReadAccess(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
		return new LehrerPersonaldatenControllerFactory();
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
	public static LehrerPersonaldatenControllerFactory withWriteAccess(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
		return new LehrerPersonaldatenControllerFactory();
	}



	/**
	 * Erstellt einen Controller für die Unterrichtsfächer von Lehrern
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public LehrerUnterrichtsfachController getLehrerUnterrichtsfachController() throws ApiOperationException {
		final LehrerUnterrichtsfachService service = serviceFactory.getLehrerUnterrichtsfachService();
		return new LehrerUnterrichtsfachControllerImpl(service);
	}


	/**
	 * Erstellt einen Controller für die Lehrer-Anrechnungsstunden
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public LehrerAnrechnungsstundenController getLehrerAnrechnungsstundenController() throws ApiOperationException {
		final LehrerAnrechnungsstundeService getService = serviceFactory.getLehrerAnrechnungsstundenService();
		return new LehrerAnrechnungsstundenControllerImpl(getService);
	}

	/**
	 * Erstellt einen Controller für die Lehrer-Mehrleistungen
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public LehrerMehrleistungController getLehrerMehrleistungController() throws ApiOperationException {
		final LehrerMehrleistungService getService = serviceFactory.getLehrerMehrleistungService();
		return new LehrerMehrleistungControllerImpl(getService);
	}

	/**
	 * Erstellt einen Controller für die Lehrer-Minderleistungen
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public LehrerMinderleistungController getLehrerMinderleistungController() throws ApiOperationException {
		return new LehrerMinderleistungControllerImpl(serviceFactory.getLehrerMinderleistungService());
	}

}
