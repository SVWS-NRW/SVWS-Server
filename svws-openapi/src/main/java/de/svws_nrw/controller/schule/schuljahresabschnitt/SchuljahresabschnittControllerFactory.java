package de.svws_nrw.controller.schule.schuljahresabschnitt;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für den {@link SchuljahresabschnittController}.
 */
public final class SchuljahresabschnittControllerFactory {

	private final EigeneSchuleServiceFactory serviceFactory;

	private SchuljahresabschnittControllerFactory(final EigeneSchuleServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#ADMIN}.
	 * </p>
	 *
	 * @param request   die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine {@link SchuljahresabschnittControllerFactory}-Instanz mit Schreibberechtigung
	 */
	public static SchuljahresabschnittControllerFactory withWriteAccess(final HttpServletRequest request) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, BenutzerKompetenz.ADMIN);
		final var serviceFactory = EigeneSchuleServiceFactory.getNewInstance(EigeneSchuleRepositoryFactory.getNewInstance());
		return new SchuljahresabschnittControllerFactory(serviceFactory);
	}

	/**
	 * Erstellt einen neuen {@link SchuljahresabschnittController}.
	 *
	 * @return ein neuer {@link SchuljahresabschnittControllerImpl} mit dem konfigurierten Service
	 */
	public SchuljahresabschnittController getController() {
		return new SchuljahresabschnittControllerImpl(serviceFactory.getSchuljahresabschnittService());
	}

}
