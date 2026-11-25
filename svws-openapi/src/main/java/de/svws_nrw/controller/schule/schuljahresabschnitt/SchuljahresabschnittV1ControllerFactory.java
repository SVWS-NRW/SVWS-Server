package de.svws_nrw.controller.schule.schuljahresabschnitt;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.mapper.schule.SchuljahresabschnittV1Mapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Factory für den {@link SchuljahresabschnittV1Controller} der externen API.
 */
public final class SchuljahresabschnittV1ControllerFactory {

	private final EigeneSchuleServiceFactory serviceFactory;

	private SchuljahresabschnittV1ControllerFactory(final EigeneSchuleServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	/**
	 * Erstellt eine Factory-Instanz mit Leseberechtigung für die externe API.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#IMPORT_EXPORT_LERNPLATTFORM} oder
	 * {@link BenutzerKompetenz#EXTERNAL_API_LERNPLATTFORM_EXPORTIEREN}.
	 * </p>
	 *
	 * @param request   die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 *
	 * @return eine {@link SchuljahresabschnittV1ControllerFactory}-Instanz mit Leseberechtigung
	 */
	public static SchuljahresabschnittV1ControllerFactory withReadAccess(final HttpServletRequest request) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE,
				BenutzerKompetenz.IMPORT_EXPORT_LERNPLATTFORM, BenutzerKompetenz.EXTERNAL_API_LERNPLATTFORM_EXPORTIEREN);
		final var serviceFactory = EigeneSchuleServiceFactory.getNewInstance(EigeneSchuleRepositoryFactory.getNewInstance());
		return new SchuljahresabschnittV1ControllerFactory(serviceFactory);
	}

	/**
	 * Erstellt einen neuen {@link SchuljahresabschnittV1Controller}.
	 *
	 * @return ein neuer {@link SchuljahresabschnittV1ControllerImpl} mit dem konfigurierten Service und Mapper
	 */
	public SchuljahresabschnittV1Controller getController() {
		return new SchuljahresabschnittV1ControllerImpl(serviceFactory.getSchuljahresabschnittService(), SchuljahresabschnittV1Mapper.INSTANCE);
	}

}
