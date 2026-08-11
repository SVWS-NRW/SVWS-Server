package de.svws_nrw.controller.schueler.schulbesuch;

import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.mapper.schueler.schulbesuch.BisherigeSchuleMapper;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.service.schueler.schulbesuch.BisherigeSchuleServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public class BisherigeSchuleControllerFactory {

	private final BisherigeSchuleServiceFactory serviceFactory;

	/**
	 * Erstellt eine neue BisherigeSchulenControllerFactory mit der angegebenen Service-Factory.
	 *
	 * @param serviceFactory die Factory zur Erstellung von BisherigeSchulenService-Instanzen
	 */
	public BisherigeSchuleControllerFactory(final BisherigeSchuleServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	private static BisherigeSchuleControllerFactory getNewInstance(final HttpServletRequest requst) {
		DBBenutzerUtils.getDBConnection(requst, ServerMode.STABLE,  BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN);
		final var repoFactory = SchuelerRepositoryFactory.getNewInstance();
		final var mapper = BisherigeSchuleMapper.INSTANCE;
		final var serviceFactory = BisherigeSchuleServiceFactory.getNewInstance(repoFactory, mapper);

		return new BisherigeSchuleControllerFactory(serviceFactory);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Schreibberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#SCHUELER_INDIVIDUALDATEN_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine BisherigeSchulenControllerFactory-Instanz mit Schreibberechtigung
	 */
	public static BisherigeSchuleControllerFactory withWriteAccess(final HttpServletRequest request) {
		return getNewInstance(request);
	}

	/**
	 * Erstellt eine Factory-Instanz mit Löschberechtigung.
	 * <p>
	 * Erfordert die Kompetenz {@link BenutzerKompetenz#SCHUELER_INDIVIDUALDATEN_AENDERN}.
	 * </p>
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine BisherigeSchulenControllerFactory-Instanz mit Löschberechtigung
	 */
	public static BisherigeSchuleControllerFactory withDeleteAccess(final HttpServletRequest request) {
		return getNewInstance(request);
	}

	/**
	 * Erstellt eine neue BisherigeSchulenController-Instanz.
	 *
	 * @return ein neuer BisherigeSchulenController mit dem konfigurierten BisherigeSchulenService
	 */
	public BisherigeSchuleController getBisherigeSchuleController() {
		return new BisherigeSchuleController(serviceFactory.getBisherigeSchuleService());
	}


}
