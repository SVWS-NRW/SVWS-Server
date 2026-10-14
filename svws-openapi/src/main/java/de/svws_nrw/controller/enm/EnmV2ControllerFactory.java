package de.svws_nrw.controller.enm;

import de.svws_nrw.core.data.enm.ENMLehrerFunktion;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.enm.NotenmodulRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.enm.EnmV2GetService;
import de.svws_nrw.service.enm.EnmV2ImportService;
import de.svws_nrw.service.enm.NotenmodulServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response.Status;

/**
 * Die Controller-Factory für die ENM-Daten in Version 2
 */
public final class EnmV2ControllerFactory {

	/** Die Service-Factory für die ENM-Daten in Version 2 */
	private final NotenmodulServiceFactory serviceFactory;


	/**
	 * Erzeugt eine neue Factory für die übergebene Datenbank-Verbindung.
	 * Der Konstruktor ist package private und sollte nur von einer Default-Methode
	 * im Interface aufgerufen werden.
	 */
	private EnmV2ControllerFactory() {
		this.serviceFactory = NotenmodulServiceFactory.getNewInstance(
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
	public static EnmV2ControllerFactory withReadAccess(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN);
		return new EnmV2ControllerFactory();
	}


	/**
	 * Diese statische Methode dient dem Zugriff auf die in der API-Schicht.
	 *
	 * Die Funktion NOTENMODULADMIN liefert schulweite Daten samt Zugangsdaten aller Lehrer und
	 * verlangt daher die allgemeine Kompetenz.
	 *
	 * @param request   der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 * @param id        die ID des Lehrers
	 * @param funktion  die ID der Lehrerfunktion, in welcher die Abfrage ausgeführt wird
	 *
	 * @return der spezielle Servlet-Controller
	 *
	 * @throws ApiOperationException   falls die Lehrerfunktion ungültig oder die Berechtigung nicht gegeben ist
	 */
	public static EnmV2ControllerFactory withReadAccessFunktionsbezogen(final HttpServletRequest request, final long id, final int funktion)
			throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		final DBEntityManager conn = DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_FUNKTION);
		final ENMLehrerFunktion lehrerFunktion = ENMLehrerFunktion.fromID(funktion);
		if (lehrerFunktion == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die übergebene Lehrerfunktion wird nicht unterstützt.");
		}
		final Benutzer user = conn.getUser();
		final boolean hatAllgemeineKompetenz = user.pruefeKompetenz(BenutzerKompetenz.NOTENMODUL_NOTEN_ANSEHEN_ALLGEMEIN);
		if (!hatAllgemeineKompetenz && ((user.getIdLehrer() == null) || (user.getIdLehrer() != id)
				|| (lehrerFunktion == ENMLehrerFunktion.NOTENMODULADMIN))) {
			throw new ApiOperationException(Status.FORBIDDEN);
		}
		return new EnmV2ControllerFactory();
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
	public static EnmV2ControllerFactory withWriteAccess(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN);
		return new EnmV2ControllerFactory();
	}


	/**
	 * Erstellt einen Controller für die ENM-Daten in Version 2
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public EnmV2Controller getEnmV2Controller() throws ApiOperationException {
		final EnmV2GetService getService = serviceFactory.getEnmV2GetService();
		final EnmV2ImportService importService = serviceFactory.getEnmV2ImportService();
		return new EnmV2ControllerImpl(getService, importService);
	}

}
