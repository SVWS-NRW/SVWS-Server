package de.svws_nrw.controller.schule.schulleitung;

import de.svws_nrw.mapper.schule.schulleitung.SchulleitungMapper;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.schule.schulleitung.SchulleitungService;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.service.schule.schulleitung.SchulleitungServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public final class SchulleitungControllerFactory {

	private final SchulleitungServiceFactory serviceFactory;

	/**
	 * Erstellt eine neue {@code SchulleitungControllerFactory}.
	 *
	 * @param serviceFactory die Factory zur Erstellung von {@link SchulleitungService}-Instanzen
	 */
	public SchulleitungControllerFactory(final SchulleitungServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	private static SchulleitungControllerFactory getNewInstance(
			final HttpServletRequest request,
			final BenutzerKompetenz kompetenz) {
		DBBenutzerUtils.getDBConnection(request, ServerMode.STABLE, kompetenz);
		final var repoFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		final var leitungsfunktionRepoFactory = LehrerRepositoryFactory.getNewInstance();
		final var mapper = SchulleitungMapper.INSTANCE;
		final var serviceFactory = SchulleitungServiceFactory.getNewInstance(repoFactory, leitungsfunktionRepoFactory, mapper);

		return new SchulleitungControllerFactory(serviceFactory);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Leseberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#LEHRER_PERSONALDATEN_ANSEHEN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine SchulleitungControllerFactory-Instanz mit Leseberechtigung
	 */
	public static SchulleitungControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.LEHRERDATEN_ANSEHEN);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#LEHRER_PERSONALDATEN_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine SchulleitungControllerFactory-Instanz mit Schreibberechtigung
	 */
	public static SchulleitungControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.LEHRERDATEN_AENDERN);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#LEHRERDATEN_LOESCHEN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine SchulleitungControllerFactory-Instanz mit Löschberechtigung
	 */
	public static SchulleitungControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.LEHRERDATEN_LOESCHEN);
	}

	/**
	 * Erstellt eine neue {@link SchulleitungController}-Instanz.
	 *
	 * @return ein neuer {@code SchulleitungController} mit dem konfigurierten Service
	 */
	public SchulleitungController getSchulleitungController() {
		return new SchulleitungController(serviceFactory.getSchulleitungService());
	}

}
