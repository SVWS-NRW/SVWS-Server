package de.svws_nrw.controller.uv;

import de.svws_nrw.controller.uv.faecher.UvFachController;
import de.svws_nrw.controller.uv.faecher.UvFachControllerImpl;
import de.svws_nrw.controller.uv.lehrer.UvLehrerAnrechnungsstundenController;
import de.svws_nrw.controller.uv.lehrer.UvLehrerAnrechnungsstundenControllerImpl;
import de.svws_nrw.controller.uv.lehrer.UvLehrerController;
import de.svws_nrw.controller.uv.lehrer.UvLehrerControllerImpl;
import de.svws_nrw.controller.uv.lehrer.UvLehrerPflichtstundensollController;
import de.svws_nrw.controller.uv.lehrer.UvLehrerPflichtstundensollControllerImpl;
import de.svws_nrw.controller.uv.lehrer.UvLehrerUnterrichtsfachController;
import de.svws_nrw.controller.uv.lehrer.UvLehrerUnterrichtsfachControllerImpl;
import de.svws_nrw.controller.uv.raeume.UvRaumController;
import de.svws_nrw.controller.uv.raeume.UvRaumControllerImpl;
import de.svws_nrw.controller.uv.raeume.UvRaumgruppeController;
import de.svws_nrw.controller.uv.raeume.UvRaumgruppeControllerImpl;
import de.svws_nrw.controller.uv.stundentafeln.UvStundentafelController;
import de.svws_nrw.controller.uv.stundentafeln.UvStundentafelControllerImpl;
import de.svws_nrw.controller.uv.stundentafeln.UvStundentafelFachController;
import de.svws_nrw.controller.uv.stundentafeln.UvStundentafelFachControllerImpl;
import de.svws_nrw.controller.uv.zeitraster.UvZeitrasterController;
import de.svws_nrw.controller.uv.zeitraster.UvZeitrasterControllerImpl;
import de.svws_nrw.controller.uv.zeitraster.UvZeitrasterEintragController;
import de.svws_nrw.controller.uv.zeitraster.UvZeitrasterEintragControllerImpl;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.uv.UvGrunddatenRepositoryFactory;
import de.svws_nrw.service.uv.UvGrunddatenServiceFactory;
import de.svws_nrw.service.uv.faecher.UvFachService;
import de.svws_nrw.service.uv.lehrer.UvLehrerAnrechnungsstundenImportService;
import de.svws_nrw.service.uv.lehrer.UvLehrerAnrechnungsstundenService;
import de.svws_nrw.service.uv.lehrer.UvLehrerPflichtstundensollImportService;
import de.svws_nrw.service.uv.lehrer.UvLehrerPflichtstundensollService;
import de.svws_nrw.service.uv.lehrer.UvLehrerService;
import de.svws_nrw.service.uv.lehrer.UvLehrerUnterrichtsfachService;
import de.svws_nrw.service.uv.raeume.UvRaumService;
import de.svws_nrw.service.uv.raeume.UvRaumgruppeService;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelFachService;
import de.svws_nrw.service.uv.stundentafeln.UvStundentafelService;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterEintragService;
import de.svws_nrw.service.uv.zeitraster.UvZeitrasterService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Die Default-Implementierung einer Controller-Factory für den Bereich der Lehrer
 */
public final class UvGrunddatenControllerFactory {

	/** Die Service-Factory für die Lehrer */
	private final UvGrunddatenServiceFactory serviceFactory;

	/**
	 * Erzeugt eine neue Factory für die übergebene Datenbank-Verbindung.
	 * Der Konstruktor ist package private und sollte nur von einer Default-Methode
	 * im Interface aufgerufen werden.
	 */
	private UvGrunddatenControllerFactory() {
		final var uvGrunddatenRepositoryFactory = UvGrunddatenRepositoryFactory.getNewInstance();
		final var lehrerRepositoryFactory = LehrerRepositoryFactory.getNewInstance();
		final var schuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		final var schuelerRepositoryFactory = SchuelerRepositoryFactory.getNewInstance();
		final var klassenRepositoryFactory = KlassenRepositoryFactory.getNewInstance();
		final var katalogRepositoryFactory = KatalogRepositoryFactory.getNewInstance();
		this.serviceFactory = UvGrunddatenServiceFactory.getNewInstance(uvGrunddatenRepositoryFactory, lehrerRepositoryFactory, schuleRepositoryFactory, schuelerRepositoryFactory, klassenRepositoryFactory);
	}

	/**
	 * Erstellt einen Controller für die UV-Grunddaten.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvGrunddatenBundleController getUvGrunddatenBundleController() throws ApiOperationException {
		return new UvGrunddatenBundleControllerImpl(serviceFactory);
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
	public static UvGrunddatenControllerFactory withReadAccess(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN);
		return new UvGrunddatenControllerFactory();
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
	public static UvGrunddatenControllerFactory withWriteAccess(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN,
				BenutzerKompetenz.UNTERRICHTSVERTEILUNG_FUNKTIONSBEZOGEN_AENDERN);
		return new UvGrunddatenControllerFactory();
	}



	/**
	 * Erstellt einen Controller für die Lehrer-Anrechnungsstunden
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvLehrerController getUvLehrerController() throws ApiOperationException {
		final UvLehrerService getService = serviceFactory.getUvLehrerService();
		return new UvLehrerControllerImpl(getService);
	}

	/**
	 * Creates and returns an instance of {@code UvLehrerPflichtstundensollController}.
	 * The controller provides access to functionalities for managing teacher mandatory teaching hours.
	 *
	 * @return an implementation of {@code UvLehrerPflichtstundensollController}
	 * @throws ApiOperationException if an error occurs during permission verification or service initialization
	 */
	public UvLehrerPflichtstundensollController getUvLehrerPflichtstundensollController() throws ApiOperationException {
		final UvLehrerPflichtstundensollService getService = serviceFactory.getUvLehrerPflichtstundensollService();
		final UvLehrerPflichtstundensollImportService importService = serviceFactory.getUvLehrerPflichtstundensollImportService();
		return new UvLehrerPflichtstundensollControllerImpl(getService, importService);
	}

	/**
	 * Creates and returns an instance of {@code UvLehrerAnrechnungsstundenController}.
	 * The controller provides access to functionalities for managing teacher credit hours.
	 *
	 * @return an implementation of {@code UvLehrerAnrechnungsstundenController}
	 *
	 * @throws ApiOperationException if an error occurs during permission verification or service initialization
	 */
	public UvLehrerAnrechnungsstundenController getUvLehrerAnrechnungsstundenController() throws ApiOperationException {
		final UvLehrerAnrechnungsstundenService getService = serviceFactory.getUvLehrerAnrechnungsstundenService();
		final UvLehrerAnrechnungsstundenImportService importService = serviceFactory.getUvLehrerAnrechnungsstundenImportService();
		return new UvLehrerAnrechnungsstundenControllerImpl(getService, importService);
	}

	/**
	 * Erstellt einen Controller für die Unterrichtsfächer von UV-Lehrkräften
	 * (Tabelle UV_LehrerUnterrichtsfaecher).
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvLehrerUnterrichtsfachController getUvLehrerUnterrichtsfachController() throws ApiOperationException {
		final UvLehrerUnterrichtsfachService getService = serviceFactory.getUvLehrerUnterrichtsfachService();
		return new UvLehrerUnterrichtsfachControllerImpl(getService);
	}

	/**
	 * Erstellt einen Controller für die UV-Fächer (Tabelle UV_Faecher).
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvFachController getUvFachController() throws ApiOperationException {
		final UvFachService getService = serviceFactory.getUvFachService();
		return new UvFachControllerImpl(getService);
	}

	/**
	 * Erstellt einen Controller für die UV-Räume (Tabelle UV_Raeume).
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvRaumController getUvRaumController() throws ApiOperationException {
		final UvRaumService getService = serviceFactory.getUvRaumService();
		return new UvRaumControllerImpl(getService);
	}

	/**
	 * Erstellt einen Controller für die UV-Raumgruppen (Tabelle UV_Raumgruppen).
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvRaumgruppeController getUvRaumgruppeController() throws ApiOperationException {
		final UvRaumgruppeService getService = serviceFactory.getUvRaumgruppeService();
		return new UvRaumgruppeControllerImpl(getService);
	}

	/**
	 * Erstellt einen Controller für die UV-Stundentafeln.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvStundentafelController getUvStundentafelController() throws ApiOperationException {
		final UvStundentafelService getService = serviceFactory.getUvStundentafelService();
		return new UvStundentafelControllerImpl(getService, serviceFactory.getUvStundentafelImportService());
	}

	/**
	 * Erstellt einen Controller für die UV-Stundentafel-Fächer.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvStundentafelFachController getUvStundentafelFachController() throws ApiOperationException {
		final UvStundentafelFachService getService = serviceFactory.getUvStundentafelFachService();
		return new UvStundentafelFachControllerImpl(getService);
	}

	/**
	 * Erstellt einen Controller für die UV-Zeitraster.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvZeitrasterController getUvZeitrasterController() throws ApiOperationException {
		final UvZeitrasterService getService = serviceFactory.getUvZeitrasterService();
		return new UvZeitrasterControllerImpl(getService);
	}

	/**
	 * Erstellt einen Controller für die UV-Zeitraster-Einträge.
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public UvZeitrasterEintragController getUvZeitrasterEintragController() throws ApiOperationException {
		final UvZeitrasterEintragService getService = serviceFactory.getUvZeitrasterEintragService();
		return new UvZeitrasterEintragControllerImpl(getService);
	}

}
