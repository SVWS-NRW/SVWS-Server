package de.svws_nrw.controller.schule.logoverwaltung;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.mapper.schule.logoverwaltung.LogoverwaltungMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.logoverwaltung.LogoverwaltungRepositoryFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;
import de.svws_nrw.service.schule.kataloge.fachklasse.FachklasseServiceFactory;
import de.svws_nrw.service.schule.logoverwaltung.LogoverwaltungService;
import de.svws_nrw.service.schule.logoverwaltung.LogoverwaltungServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public final class LogoverwaltungControllerFactory {

	private final LogoverwaltungServiceFactory serviceFactory;

	/**
	 * @param serviceFactory {@link FachklasseServiceFactory}
	 */
	public LogoverwaltungControllerFactory(final LogoverwaltungServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	private static LogoverwaltungControllerFactory getNewInstance(final HttpServletRequest request, final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.DEV, benutzerKompetenz);
		final var serviceFactory = LogoverwaltungServiceFactory.getNewInstance(
				LogoverwaltungRepositoryFactory.getNewInstance(),
				LogoverwaltungMapper.INSTANCE,
				SchuleServiceFactory.getNewInstance(EigeneSchuleRepositoryFactory.getNewInstance())
		);
		return new LogoverwaltungControllerFactory(serviceFactory);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Leseberechtigung.
	 * <p>
	 * Es werden keine besonderen Benutzerkompetenzen benötigt.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine {@link LogoverwaltungControllerFactory}-Instanz mit Leseberechtigung
	 */
	public static LogoverwaltungControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.SCHULBEZOGENE_DATEN_ANSEHEN);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#SCHULBEZOGENE_DATEN_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine {@link LogoverwaltungControllerFactory}-Instanz mit Schreibberechtigung
	 */
	public static LogoverwaltungControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#SCHULBEZOGENE_DATEN_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine {@link LogoverwaltungControllerFactory}-Instanz mit Löschberechtigung
	 */
	public static LogoverwaltungControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN);
	}

	/**
	 * @return ein neuer {@link LogoverwaltungController} mit dem konfigurierten {@link LogoverwaltungService}
	 */
	public LogoverwaltungController getController() {
		return new LogoverwaltungController(serviceFactory.getService());
	}

}
