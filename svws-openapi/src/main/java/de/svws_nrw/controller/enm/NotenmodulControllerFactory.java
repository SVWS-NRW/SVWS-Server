package de.svws_nrw.controller.enm;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.enm.NotenmodulRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.enm.NotenmodulLocalService;
import de.svws_nrw.service.enm.NotenmodulServiceFactory;
import de.svws_nrw.service.enm.NotenmodulSynchronisationService;
import de.svws_nrw.service.enm.NotenmodulVerbindungenService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Die Controller-Factory für den Bereich des Notenmoduls
 */
public final class NotenmodulControllerFactory {

	/** Die Service-Factory für das Notenmodul */
	private final NotenmodulServiceFactory notenmodulServiceFactory;

	/** Der Authentifizierte Benutzer */
	private final Benutzer authenticatedUser;


	/**
	 * Erzeugt eine neue Factory für die übergebene Datenbank-Verbindung.
	 * Der Konstruktor ist package private und sollte nur von einer Default-Methode
	 * im Interface aufgerufen werden.
	 *
	 * @param authenticatedUser   der authentifizierte Benutzer für die Vorab-Prüfung von Berechtigungen im Controller
	 */
	private NotenmodulControllerFactory(final Benutzer authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
		this.notenmodulServiceFactory = NotenmodulServiceFactory.getNewInstance(
				NotenmodulRepositoryFactory.getNewInstance(),
				LehrerRepositoryFactory.getNewInstance(),
				SchuelerRepositoryFactory.getNewInstance(),
				KatalogRepositoryFactory.getNewInstance(),
				KlassenRepositoryFactory.getNewInstance(),
				KurseRepositoryFactory.getNewInstance(),
				EigeneSchuleRepositoryFactory.getNewInstance());
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
	public static NotenmodulControllerFactory withReadAccess(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		final var conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION);
		return new NotenmodulControllerFactory(conn.getUser());
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
	public static NotenmodulControllerFactory withWriteAccess(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		final var conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN,
				BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_FUNKTION);
		return new NotenmodulControllerFactory(conn.getUser());
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
	public static NotenmodulControllerFactory withAdminAccess(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		final var conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
		return new NotenmodulControllerFactory(conn.getUser());
	}


	/**
	 * Diese statische Methode dient dem Zugriff auf die in der API-Schicht.
	 *
	 * @param request    der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 * @param idLehrer   die ID des Lehrers für welche die Anfrage gestellt wird und welche mit dem angemeldeten Benutzer abgeglichen wird
	 *
	 * @return der spezielle Servlet-Controller
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	public static NotenmodulControllerFactory withAdminAccessOrSelf(final HttpServletRequest request, final long idLehrer) {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		final var conn = DBBenutzerUtils.getDBConnectionAllowSelfLehrer(request, ServerMode.STABLE, idLehrer, BenutzerKompetenz.NOTENMODUL_ADMINISTRATION);
		return new NotenmodulControllerFactory(conn.getUser());
	}


	/**
	 * Erstellt einen Controller für die Notenmodul-Credentials
	 *
	 * @return der Controller
	 */
	public NotenmodulCredentialsController getNotenmodulCredentialsController() {
		return new NotenmodulCredentialsControllerImpl(notenmodulServiceFactory.getNotenmodulCredentialsService(),
				notenmodulServiceFactory.getNotenmodulCredentialGeneratorService());
	}


	/**
	 * Erstellt einen Controller für die Notenmodul-Verbindungen
	 *
	 * @return der Controller
	 */
	public NotenmodulVerbindungenController getNotenmodulVerbindungenController() {
		final NotenmodulVerbindungenService service = notenmodulServiceFactory.getNotenmodulVerbindungenService();
		return new NotenmodulVerbindungenControllerImpl(service);
	}


	/**
	 * Erstellt einen Controller für die Notenmodul-Synchronisation
	 *
	 * @return der Controller
	 */
	public NotenmodulSynchronisationController getNotenmodulSynchronisationController() {
		final NotenmodulSynchronisationService service = notenmodulServiceFactory.getNotenmodulSynchronisationService();
		return new NotenmodulSynchronisationControllerImpl(service);
	}


	/**
	 * Erstellt einen Controller für die Notenmodul-Synchronisation
	 *
	 * @return der Controller
	 */
	public NotenmodulLocalController getNotenmodulLocalController() {
		final NotenmodulLocalService service = notenmodulServiceFactory.getNotenmodulLocalService();
		return new NotenmodulLocalControllerImpl(service, authenticatedUser);
	}

}
