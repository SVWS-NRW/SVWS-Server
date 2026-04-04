package de.svws_nrw.controller.enm;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.enm.NotenmodulRepositoryFactory;
import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;
import de.svws_nrw.service.enm.EnmV2ServiceFactory;
import de.svws_nrw.service.enm.NotenmodulCredentialsService;
import de.svws_nrw.service.enm.NotenmodulLocalService;
import de.svws_nrw.service.enm.NotenmodulServiceFactory;
import de.svws_nrw.service.enm.NotenmodulSynchronisationService;
import de.svws_nrw.service.enm.NotenmodulVerbindungenService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Die Controller-Factory für den Bereich des Notenmoduls
 */
public final class NotenmodulControllerFactory {

	/** Die Service-Factory für die ENM-Daten in Version 1 */
	private final EnmV2ServiceFactory enmV2ServiceFactory;

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
		this.enmV2ServiceFactory = EnmV2ServiceFactory.getNewInstance(
				KatalogeRepositoryFactory.getNewInstance(),
				KlassenRepositoryFactory.getNewInstance(),
				KurseRepositoryFactory.getNewInstance(),
				LehrerRepositoryFactory.getNewInstance(),
				NotenmodulRepositoryFactory.getNewInstance(),
				SchuelerRepositoryFactory.getNewInstance(),
				SchuleRepositoryFactory.getNewInstance());
		final var notenmodulRepositoryFactory = NotenmodulRepositoryFactory.getNewInstance();
		final var lehrerRepositoryFactory = LehrerRepositoryFactory.getNewInstance();
		final var schuelerRepositoryFactory = SchuelerRepositoryFactory.getNewInstance();
		final var katalogeRepositoryFactory = KatalogeRepositoryFactory.getNewInstance();
		this.notenmodulServiceFactory = NotenmodulServiceFactory.getNewInstance(notenmodulRepositoryFactory, this.enmV2ServiceFactory, lehrerRepositoryFactory,
				schuelerRepositoryFactory, katalogeRepositoryFactory);
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
		final NotenmodulCredentialsService service = notenmodulServiceFactory.getNotenmodulCredentialsService();
		return new NotenmodulCredentialsControllerImpl(service);
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
