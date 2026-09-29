package de.svws_nrw.controller.abschluesse;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.abschluesse.AbschlussService;
import de.svws_nrw.service.abschluesse.AbschlussServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Die Default-Implementierung einer Controller-Factory für den Bereich der Abschlüsse
 */
public final class AbschlussControllerFactory {

	/** Die Service-Factory für die Abschlüsse */
	private final AbschlussServiceFactory abschlussServiceFactory;

	private AbschlussControllerFactory() {
		final var schuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		final var schuelerRepositoryFactory = SchuelerRepositoryFactory.getNewInstance();
		this.abschlussServiceFactory = AbschlussServiceFactory.getNewInstance(schuleRepositoryFactory, schuelerRepositoryFactory);
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
	public static AbschlussControllerFactory withReadAccess(final HttpServletRequest request) {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ANSEHEN);
		return new AbschlussControllerFactory();
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
	public static AbschlussControllerFactory withWriteAccess(final HttpServletRequest request) {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN,
				BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN);
		return new AbschlussControllerFactory();
	}

	/**
	 * Erstellt einen Controller für die Unterrichtsfächer von Lehrern
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	public AbschlussController getAbschlussController() {
		final AbschlussService service = abschlussServiceFactory.getAbschlussService();
		return new AbschlussControllerImpl(service);
	}

}
