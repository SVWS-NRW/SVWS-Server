package de.svws_nrw.data.statistik;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Eine Factory für den Zugriff auf Controller für den Bereich der Statistik
 */
public interface StatistikControllerFactory {


	/**
	 * Diese statistche Methode dient dem Zugriff auf die in der API-Schicht.
	 *
	 * @param request  der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return der spezielle Servlet-Controller
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	static StatistikControllerFactoryImpl getAdmin(final HttpServletRequest request) throws ApiOperationException {
		// Die Datenbank-Verbindung muss aufgebaut werden, bevor auf Respositories zugegriffen wird
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, BenutzerKompetenz.ADMIN);
		return new StatistikControllerFactoryImpl();
	}


	/**
	 * Erstellt einen Controller für die Gesamt-Statistikdaten
	 *
	 * @return der Controller
	 *
	 * @throws ApiOperationException wenn ein Fehler bei der Überprüfung der Berechtigung auftritt
	 */
	StatistikController getControllerStatistikGesamt() throws ApiOperationException;

}
