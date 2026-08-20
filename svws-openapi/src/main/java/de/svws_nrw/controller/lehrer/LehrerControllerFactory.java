package de.svws_nrw.controller.lehrer;

import de.svws_nrw.controller.lehrer.anrechnung.LehrerAnrechnungsstundenController;
import de.svws_nrw.controller.lehrer.anrechnung.LehrerAnrechnungsstundenControllerImpl;
import de.svws_nrw.controller.lehrer.fachrichtung.LehrerFachrichtungController;
import de.svws_nrw.controller.lehrer.fachrichtung.LehrerFachrichtungControllerImpl;
import de.svws_nrw.controller.lehrer.funktion.LehrerFunktionController;
import de.svws_nrw.controller.lehrer.funktion.LehrerFunktionControllerImpl;
import de.svws_nrw.controller.lehrer.lehrbefaehigung.LehrerLehrbefaehigungController;
import de.svws_nrw.controller.lehrer.lehrbefaehigung.LehrerLehrbefaehigungControllerImpl;
import de.svws_nrw.controller.lehrer.mehrleistung.LehrerMehrleistungController;
import de.svws_nrw.controller.lehrer.mehrleistung.LehrerMehrleistungControllerImpl;
import de.svws_nrw.controller.lehrer.minderleistung.LehrerMinderleistungController;
import de.svws_nrw.controller.lehrer.minderleistung.LehrerMinderleistungControllerImpl;
import de.svws_nrw.controller.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenController;
import de.svws_nrw.controller.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenControllerImpl;
import de.svws_nrw.controller.lehrer.unterrichtsfach.LehrerUnterrichtsfachController;
import de.svws_nrw.controller.lehrer.unterrichtsfach.LehrerUnterrichtsfachControllerImpl;
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
public final class LehrerControllerFactory {

	/** Die Service-Factory für die Lehrer */
	private final LehrerServiceFactory serviceFactory;

	private LehrerControllerFactory() {
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
	public static LehrerControllerFactory withReadAccess(final HttpServletRequest request) {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
		return new LehrerControllerFactory();
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
	public static LehrerControllerFactory withWriteAccess(final HttpServletRequest request) {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
		return new LehrerControllerFactory();
	}

	/**
	 * Erstellt einen Controller für die Unterrichtsfächer von Lehrern
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public LehrerUnterrichtsfachController getLehrerUnterrichtsfachController() {
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
	public LehrerAnrechnungsstundenController getLehrerAnrechnungsstundenController() {
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
	public LehrerMehrleistungController getLehrerMehrleistungController() {
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
	public LehrerMinderleistungController getLehrerMinderleistungController() {
		return new LehrerMinderleistungControllerImpl(serviceFactory.getLehrerMinderleistungService());
	}

	/**
	 * @return {@link LehrerPersonalabschnittsdatenController}
	 */
	public LehrerPersonalabschnittsdatenController getLehrerPersonalabschnittsdatenController() {
		return new LehrerPersonalabschnittsdatenControllerImpl(serviceFactory.getLehrerPersonalabschnittsdatenService());
	}

	/**
	 * @return {@link LehrerFunktionController}
	 */
	public LehrerFunktionController getLehrerFunktionController() {
		return new LehrerFunktionControllerImpl(serviceFactory.getLehrerFunktionService());
	}

	/**
	 * @return {@link LehrerFachrichtungController}
	 */
	public LehrerFachrichtungController getLehrerFachrichtungController() {
		return new LehrerFachrichtungControllerImpl(serviceFactory.getLehrerFachrichtungService());
	}

	/**
	 * @return {@link LehrerLehrbefaehigungController}
	 */
	public LehrerLehrbefaehigungController getLehrerLehrbefaehigungController() {
		return new LehrerLehrbefaehigungControllerImpl(serviceFactory.getLehrerLehrbefaehigungService());
	}

}
