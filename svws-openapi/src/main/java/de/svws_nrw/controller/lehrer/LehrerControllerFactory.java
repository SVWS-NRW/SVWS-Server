package de.svws_nrw.controller.lehrer;

import de.svws_nrw.controller.lehrer.anrechnung.LehrerAnrechnungsstundenController;
import de.svws_nrw.controller.lehrer.anrechnung.LehrerAnrechnungsstundenControllerImpl;
import de.svws_nrw.controller.lehrer.einwilligung.LehrerEinwilligungController;
import de.svws_nrw.controller.lehrer.einwilligung.LehrerEinwilligungControllerImpl;
import de.svws_nrw.controller.lehrer.fachrichtung.LehrerFachrichtungController;
import de.svws_nrw.controller.lehrer.fachrichtung.LehrerFachrichtungControllerImpl;
import de.svws_nrw.controller.lehrer.funktion.LehrerFunktionController;
import de.svws_nrw.controller.lehrer.funktion.LehrerFunktionControllerImpl;
import de.svws_nrw.controller.lehrer.lehramt.LehrerLehramtController;
import de.svws_nrw.controller.lehrer.lehramt.LehrerLehramtControllerImpl;
import de.svws_nrw.controller.lehrer.lehrbefaehigung.LehrerLehrbefaehigungController;
import de.svws_nrw.controller.lehrer.lehrbefaehigung.LehrerLehrbefaehigungControllerImpl;
import de.svws_nrw.controller.lehrer.lernplattform.LehrerLernplattformController;
import de.svws_nrw.controller.lehrer.lernplattform.LehrerLernplattformControllerImpl;
import de.svws_nrw.controller.lehrer.liste.LehrerListeController;
import de.svws_nrw.controller.lehrer.liste.LehrerListeControllerImpl;
import de.svws_nrw.controller.lehrer.mehrleistung.LehrerMehrleistungController;
import de.svws_nrw.controller.lehrer.mehrleistung.LehrerMehrleistungControllerImpl;
import de.svws_nrw.controller.lehrer.minderleistung.LehrerMinderleistungController;
import de.svws_nrw.controller.lehrer.minderleistung.LehrerMinderleistungControllerImpl;
import de.svws_nrw.controller.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenController;
import de.svws_nrw.controller.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenControllerImpl;
import de.svws_nrw.controller.lehrer.personaldaten.LehrerPersonaldatenController;
import de.svws_nrw.controller.lehrer.personaldaten.LehrerPersonaldatenControllerImpl;
import de.svws_nrw.controller.lehrer.stammdaten.LehrerStammdatenController;
import de.svws_nrw.controller.lehrer.stammdaten.LehrerStammdatenControllerImpl;
import de.svws_nrw.controller.lehrer.unterrichtsfach.LehrerUnterrichtsfachController;
import de.svws_nrw.controller.lehrer.unterrichtsfach.LehrerUnterrichtsfachControllerImpl;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeService;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungService;
import de.svws_nrw.service.lehrer.unterrichtsfach.LehrerUnterrichtsfachService;
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

	private static LehrerControllerFactory getNewInstance(final HttpServletRequest request, final ServerMode serverMode,
			final BenutzerKompetenz... kompetenzen) {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, serverMode, kompetenzen);
		return new LehrerControllerFactory();
	}

	/**
	 * Diese statische Methode dient dem lesenden Zugriff auf die Personaldaten der Lehrer in der API-Schicht
	 * im Stable-Modus. Sie erfordert die Kompetenz {@link BenutzerKompetenz#LEHRER_PERSONALDATEN_ANSEHEN}.
	 *
	 * @param request  der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return der spezielle Servlet-Controller
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	public static LehrerControllerFactory withPersonaldatenReadAccessStable(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_ANSEHEN);
	}


	/**
	 * Diese statische Methode dient dem schreibenden Zugriff auf die Personaldaten der Lehrer in der API-Schicht
	 * im Stable-Modus. Sie erfordert die Kompetenz {@link BenutzerKompetenz#LEHRER_PERSONALDATEN_AENDERN}.
	 *
	 * @param request  der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return der spezielle Servlet-Controller
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	public static LehrerControllerFactory withPersonaldatenWriteAccessStable(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.STABLE, BenutzerKompetenz.LEHRER_PERSONALDATEN_AENDERN);
	}

	/**
	 * Diese statische Methode dient dem lesenden Zugriff auf die Lehrerdaten (z.B. Stammdaten) in der API-Schicht
	 * im Stable-Modus. Sie erfordert die Kompetenz {@link BenutzerKompetenz#LEHRERDATEN_ANSEHEN}.
	 *
	 * @param request  der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return der spezielle Servlet-Controller
	 */
	public static LehrerControllerFactory withReadAccessStable(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.STABLE, BenutzerKompetenz.LEHRERDATEN_ANSEHEN);
	}

	/**
	 * Diese statische Methode dient dem schreibenden Zugriff auf die Lehrerdaten (z.B. Stammdaten) in der API-Schicht
	 * im Stable-Modus. Sie erfordert die Kompetenz {@link BenutzerKompetenz#LEHRERDATEN_AENDERN}.
	 *
	 * @param request  der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return der spezielle Servlet-Controller
	 */
	public static LehrerControllerFactory withWriteAccessStable(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.STABLE, BenutzerKompetenz.LEHRERDATEN_AENDERN);
	}

	/**
	 * Diese statische Methode dient dem Zugriff auf Lehrerdaten in der API-Schicht im Stable-Modus, für den
	 * bewusst keine Benutzerkompetenz ({@link BenutzerKompetenz#KEINE}) erforderlich ist. Dies gilt für die
	 * Auswahlliste der Lehrer, die im Client für die Auswahl von Lehrern in allen Bereichen benötigt wird.
	 *
	 * @param request  der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return der spezielle Servlet-Controller
	 */
	public static LehrerControllerFactory withNoAccessStable(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Diese statische Methode dient dem Löschen von Lehrern in der API-Schicht im Stable-Modus.
	 * Sie erfordert die Kompetenz {@link BenutzerKompetenz#LEHRERDATEN_LOESCHEN}.
	 *
	 * @param request  der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return der spezielle Servlet-Controller
	 */
	public static LehrerControllerFactory withDeleteAccessStable(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.STABLE, BenutzerKompetenz.LEHRERDATEN_LOESCHEN);
	}

	/**
	 * Diese statische Methode dient dem lesenden Zugriff auf die Lehrerdaten (z.B. Lernplattformen) in der API-Schicht.
	 * Der Zugriff ist weiterhin nur im Entwicklungsmodus ({@link ServerMode#DEV}) möglich.
	 *
	 * @param request  der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return der spezielle Servlet-Controller
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	public static LehrerControllerFactory withReadAccessDev(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.DEV, BenutzerKompetenz.LEHRERDATEN_ANSEHEN);
	}

	/**
	 * Diese statische Methode dient dem schreibenden Zugriff auf die Lehrerdaten (z.B. Lernplattformen) in der API-Schicht.
	 * Der Zugriff ist weiterhin nur im Entwicklungsmodus ({@link ServerMode#DEV}) möglich.
	 *
	 * @param request  der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return der spezielle Servlet-Controller
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	public static LehrerControllerFactory withWriteAccessDev(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.DEV, BenutzerKompetenz.LEHRERDATEN_AENDERN);
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

	/**
	 * @return {@link LehrerLehramtController}
	 */
	public LehrerLehramtController getLehrerLehramtController() {
		return new LehrerLehramtControllerImpl(serviceFactory.getLehrerLehramtService());
	}

	/**
	 * @return {@link LehrerPersonaldatenController}
	 */
	public LehrerPersonaldatenController getLehrerPersonaldatenController() {
		return new LehrerPersonaldatenControllerImpl(serviceFactory.getLehrerPersonaldatenService());
	}

	/**
	 * Erstellt einen Controller für die Lernplattform-Zuordnungen von Lehrern
	 *
	 * @return der Controller
	 */
	public LehrerLernplattformController getLehrerLernplattformController() {
		return new LehrerLernplattformControllerImpl(serviceFactory.getLehrerLernplattformService());
	}

	/**
	 * Erstellt einen Controller für die Einwilligungen von Lehrern
	 *
	 * @return der Controller
	 */
	public LehrerEinwilligungController getLehrerEinwilligungController() {
		return new LehrerEinwilligungControllerImpl(serviceFactory.getLehrerEinwilligungService());
	}

	/**
	 * Erstellt einen Controller für die Stammdaten von Lehrern
	 *
	 * @return der Controller
	 */
	public LehrerStammdatenController getLehrerStammdatenController() {
		return new LehrerStammdatenControllerImpl(serviceFactory.getLehrerStammdatenService());
	}

	/**
	 * Erstellt einen Controller für die Liste der Lehrer und das Löschen mehrerer Lehrer
	 *
	 * @return der Controller
	 */
	public LehrerListeController getLehrerListeController() {
		return new LehrerListeControllerImpl(serviceFactory.getLehrerListeService());
	}

}
