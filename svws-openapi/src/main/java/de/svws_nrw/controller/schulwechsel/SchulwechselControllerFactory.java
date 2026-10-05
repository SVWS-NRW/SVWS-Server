package de.svws_nrw.controller.schulwechsel;

import de.svws_nrw.controller.schulwechsel.abgang.SchulwechselAbgangControllerImpl;
import de.svws_nrw.controller.schulwechsel.dokument.SchulwechselDokumentControllerImpl;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.service.schulwechsel.SchulwechselServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für {@link SchulwechselAbgangControllerImpl}- und {@link SchulwechselDokumentControllerImpl}-Instanzen.
 */
public final class SchulwechselControllerFactory {

	private final SchulwechselServiceFactory serviceFactory;

	/**
	 * Erstellt eine neue Factory mit der angegebenen Service-Factory.
	 *
	 * @param serviceFactory die Service-Factory
	 */
	public SchulwechselControllerFactory(final SchulwechselServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	private static SchulwechselControllerFactory getNewInstance(final HttpServletRequest request) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.IMPORT_EXPORT_SCHULBEWERBUNG_DE);
		return new SchulwechselControllerFactory(SchulwechselServiceFactory.getNewInstance());
	}

	/**
	 * Erstellt eine Factory-Instanz mit Leseberechtigung.
	 *
	 * @param request die HTTP-Anfrage
	 * @return eine neue Factory-Instanz
	 */
	public static SchulwechselControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigung.
	 *
	 * @param request die HTTP-Anfrage
	 * @return eine neue Factory-Instanz
	 */
	public static SchulwechselControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung.
	 *
	 * @param request die HTTP-Anfrage
	 * @return eine neue Factory-Instanz
	 */
	public static SchulwechselControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request);
	}

	/**
	 * Erstellt einen neuen {@link SchulwechselAbgangControllerImpl}.
	 *
	 * @return ein neuer Controller
	 */
	public SchulwechselAbgangControllerImpl getSchulwechselAbgangController() {
		return new SchulwechselAbgangControllerImpl(this.serviceFactory.getSchulwechselAbgangService());
	}

	/**
	 * Erstellt einen neuen {@link SchulwechselDokumentControllerImpl}.
	 *
	 * @return ein neuer Controller
	 */
	public SchulwechselDokumentControllerImpl getSchulwechselDokumentController() {
		return new SchulwechselDokumentControllerImpl(this.serviceFactory.getSchulwechselDokumentService());
	}
}
