package de.svws_nrw.controller.gost;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.gost.GostRepositoryFactory;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.service.benutzer.BenutzerServiceFactory;
import de.svws_nrw.service.crypto.CryptoServiceFactory;
import de.svws_nrw.service.gost.GostServiceFactory;
import de.svws_nrw.service.schueler.SchuelerServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Die Controller-Factory für die Gymnasiale Oberstufe
 */
public final class GostLaufbahnplanungControllerFactory {

	/** Die Service Factory für den Zugriff auf Benutzerinformationen */
	private final BenutzerServiceFactory benutzerServiceFactory;

	/** Die Service-Factory für die Gymnasiale Oberstufe */
	private final GostServiceFactory gostServiceFactory;

	/**
	 * Erzeugt eine neue Factory.
	 * Der Konstruktor ist package private und sollte nur von einer Default-Methode
	 * im Interface aufgerufen werden.
	 */
	private GostLaufbahnplanungControllerFactory() {
		final BenutzerRepositoryFactory benutzerRepositoryFactory = BenutzerRepositoryFactory.getNewInstance();
		final SchuelerRepositoryFactory schuelerRepositoryFactory = SchuelerRepositoryFactory.getNewInstance();
		final GostRepositoryFactory gostRepositoryFactory = GostRepositoryFactory.getNewInstance();
		final KatalogRepositoryFactory katalogRepositoryFactory = KatalogRepositoryFactory.getNewInstance();

		this.benutzerServiceFactory = BenutzerServiceFactory.getNewInstance(benutzerRepositoryFactory);

		this.gostServiceFactory = GostServiceFactory.getNewInstance(
				gostRepositoryFactory,
				schuelerRepositoryFactory,
				LehrerRepositoryFactory.getNewInstance(),
				benutzerRepositoryFactory,
				katalogRepositoryFactory,
				EigeneSchuleRepositoryFactory.getNewInstance(),
				benutzerServiceFactory,
				CryptoServiceFactory.getNewInstance(benutzerRepositoryFactory, schuelerRepositoryFactory),
				SchuelerServiceFactory.getNewInstance(benutzerRepositoryFactory, schuelerRepositoryFactory),
				GostKlausurenRepositoryFactory.getNewInstance());
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
	public static GostLaufbahnplanungControllerFactory withAccessForKlausurplanungOrKursplanungOrLaufbahnplanung(final HttpServletRequest request)
			throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KLAUSURPLANUNG_ANSEHEN_FUNKTION,
				BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
				BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN);
		return new GostLaufbahnplanungControllerFactory();
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
	public static GostLaufbahnplanungControllerFactory withAccessForKursplanungOrLaufbahnplanung(final HttpServletRequest request)
			throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_KURSPLANUNG_FUNKTIONSBEZOGEN,
				BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN);
		return new GostLaufbahnplanungControllerFactory();
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
	public static GostLaufbahnplanungControllerFactory withAccessForLaufbahnplanung(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
				BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN);
		return new GostLaufbahnplanungControllerFactory();
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
	public static GostLaufbahnplanungControllerFactory withAccessForLaufbahnplanungAllgemein(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN);
		return new GostLaufbahnplanungControllerFactory();
	}


	/**
	 * Erstellt einen Controller für die Laufbahnplanung der gymnasialen Oberstufe
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public GostLaufbahnplanungController getGostLaufbahnplanungController() throws ApiOperationException {
		return new GostLaufbahnplanungControllerImpl(
				benutzerServiceFactory.getBenutzerKompetenzService(),
				gostServiceFactory.getGostAbiturdatenService(),
				gostServiceFactory.getGostFachwahlService(),
				gostServiceFactory.getGostSchuelerGKLWahlService(),
				gostServiceFactory.getGostJahrgangFachwahlService(),
				gostServiceFactory.getGostLaufbahnplanungImportV1Service(),
				gostServiceFactory.getGostLaufbahnplanungExportV2Service(),
				gostServiceFactory.getGostLaufbahnplanungImportV2Service());
	}

}
