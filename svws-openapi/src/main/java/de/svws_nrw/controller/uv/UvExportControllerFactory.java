package de.svws_nrw.controller.uv;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.uv.UvExportServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Die Default-Implementierung einer Controller-Factory für versionierte UV-Exporte.
 */
public final class UvExportControllerFactory {

	/** Die Service-Factory für versionierte UV-Exporte */
	private final UvExportServiceFactory serviceFactory;

	/**
	 * Erzeugt eine neue Factory für die übergebene Datenbank-Verbindung.
	 * Der Konstruktor ist private und wird nur von den statischen Zugriffsmethoden
	 * dieser Klasse aufgerufen.
	 */
	private UvExportControllerFactory() {
		this.serviceFactory = UvExportServiceFactory.getNewInstance();
	}

	/**
	 * Diese statische Methode dient dem Zugriff auf die Factory in der API-Schicht mit Lese-Berechtigung.
	 *
	 * @param request   der HTTP-Request mit welchem der spezielle Controller erzeugt wird
	 *
	 * @return die Controller-Factory
	 *
	 * @throws ApiOperationException   falls die Berechtigung nicht gegeben ist
	 */
	public static UvExportControllerFactory withReadAccess(final HttpServletRequest request) throws ApiOperationException {
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN);
		return new UvExportControllerFactory();
	}

	/**
	 * Erstellt einen Controller für versionierte UV-Exporte.
	 *
	 * @return der Controller
	 */
	public UvExportController getUvExportController() {
		return new UvExportControllerImpl(serviceFactory.getUvExportService());
	}

}
