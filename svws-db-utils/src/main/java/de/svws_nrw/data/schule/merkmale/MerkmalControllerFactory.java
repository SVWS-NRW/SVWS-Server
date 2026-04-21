package de.svws_nrw.data.schule.merkmale;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.schule.merkmale.MerkmalRepositoryFactory;
import jakarta.servlet.http.HttpServletRequest;

public final class MerkmalControllerFactory {

	private final MerkmalServiceFactory merkmalServiceFactory;

	/**
	 * Erstellt eine neue MerkmalControllerFactory mit der angegebenen Service-Factory.
	 *
	 * @param merkmalServiceFactory die Factory zur Erstellung von MerkmalService-Instanzen
	 */
	public MerkmalControllerFactory(final MerkmalServiceFactory merkmalServiceFactory) {
		this.merkmalServiceFactory = merkmalServiceFactory;
	}

	/**
	 * Erstellt eine neue Factory-Instanz mit der angegebenen Benutzerkompetenz.
	 * <p>
	 * Initialisiert die Datenbankverbindung und konfiguriert alle erforderlichen
	 * Abhängigkeiten (Repository, Mapper, Service).
	 * </p>
	 *
	 * @param requst die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @param benutzerKompetenz die erforderliche Benutzerkompetenz für die Operation
	 * @return eine neue MerkmalControllerFactory-Instanz
	 */
	private static MerkmalControllerFactory getNewInstance(final HttpServletRequest requst, final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(requst, ServerMode.STABLE, benutzerKompetenz);
		final var merkmalRepositoryFactory = MerkmalRepositoryFactory.getNewInstance();
		final var mapper = MerkmalMapper.INSTANCE;
		final var merkmalServiceFactory = MerkmalServiceFactory.getNewInstance(merkmalRepositoryFactory, mapper);

		return new MerkmalControllerFactory(merkmalServiceFactory);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Leseberechtigung.
	 * <p>
	 * Es werden keine besonderen Benutzerkompetenzen benötigt.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine MerkmalControllerFactory-Instanz mit Leseberechtigung
	 */
	public static MerkmalControllerFactory withReadAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KEINE);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#KATALOG_EINTRAEGE_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine MerkmalControllerFactory-Instanz mit Schreibberechtigung
	 */
	public static MerkmalControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#KATALOG_EINTRAEGE_LOESCHEN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine MerkmalControllerFactory-Instanz mit Löschberechtigung
	 */
	public static MerkmalControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Erstellt eine neue MerkmalController-Instanz.
	 *
	 * @return ein neuer MerkmalController mit dem konfigurierten MerkmalService
	 */
	public MerkmalController getMerkmalController() {
		return new MerkmalController(merkmalServiceFactory.getMerkmalService());
	}

}
