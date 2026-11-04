package de.svws_nrw.controller.schule.teilstandort;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für den {@link TeilstandortController}.
 */
public final class TeilstandortControllerFactory {

	private final EigeneSchuleServiceFactory serviceFactory;

	private TeilstandortControllerFactory(final EigeneSchuleServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	private static TeilstandortControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz kompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, kompetenz);
		final var serviceFactory = EigeneSchuleServiceFactory.getNewInstance(EigeneSchuleRepositoryFactory.getNewInstance());
		return new TeilstandortControllerFactory(serviceFactory);
	}

	/**
	 * Lesezugriff im Stable-Modus. Keine besonderen Benutzerkompetenzen erforderlich.
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine {@link TeilstandortControllerFactory}-Instanz
	 */
	public static TeilstandortControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Schreibzugriff im Stable-Modus. Erfordert {@link BenutzerKompetenz#KATALOG_EINTRAEGE_AENDERN}.
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine {@link TeilstandortControllerFactory}-Instanz
	 */
	public static TeilstandortControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Löschzugriff im Stable-Modus. Erfordert {@link BenutzerKompetenz#KATALOG_EINTRAEGE_LOESCHEN}.
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine {@link TeilstandortControllerFactory}-Instanz
	 */
	public static TeilstandortControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Erstellt einen neuen {@link TeilstandortController}.
	 *
	 * @return ein neuer {@link TeilstandortControllerImpl} mit dem konfigurierten Service
	 */
	public TeilstandortController getController() {
		return new TeilstandortControllerImpl(serviceFactory.getTeilstandortService());
	}

}
