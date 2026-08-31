package de.svws_nrw.controller.schule.katalog;

import de.svws_nrw.controller.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangController;
import de.svws_nrw.controller.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangControllerImpl;
import de.svws_nrw.controller.schule.katalog.fachklasse.FachklasseController;
import de.svws_nrw.controller.schule.katalog.fachklasse.FachklasseControllerImpl;
import de.svws_nrw.controller.schule.katalog.merkmal.MerkmalController;
import de.svws_nrw.controller.schule.katalog.merkmal.MerkmalControllerImpl;
import de.svws_nrw.controller.schule.katalog.ort.OrtController;
import de.svws_nrw.controller.schule.katalog.ort.OrtControllerImpl;
import de.svws_nrw.controller.schule.katalog.ortsteil.OrtsteilController;
import de.svws_nrw.controller.schule.katalog.ortsteil.OrtsteilControllerImpl;
import de.svws_nrw.controller.schule.katalog.religion.ReligionController;
import de.svws_nrw.controller.schule.katalog.religion.ReligionControllerImpl;
import de.svws_nrw.controller.schule.katalog.teilleistungsart.TeilleistungsartController;
import de.svws_nrw.controller.schule.katalog.teilleistungsart.TeilleistungsartControllerImpl;
import de.svws_nrw.core.types.ServerMode;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.benutzer.DBBenutzerUtils;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.KatalogServiceFactory;
import jakarta.servlet.http.HttpServletRequest;

public final class KatalogControllerFactory {

	private final KatalogServiceFactory serviceFactory;

	private KatalogControllerFactory(final KatalogServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	private static KatalogControllerFactory getNewInstance(
			final HttpServletRequest request,
			final ServerMode serverMode,
			final BenutzerKompetenz benutzerKompetenz) {
		DBBenutzerUtils.getDBConnection(request, serverMode, benutzerKompetenz);
		final var serviceFactory = KatalogServiceFactory.getNewInstance(
				KatalogRepositoryFactory.getNewInstance(),
				EigeneSchuleServiceFactory.getNewInstance(EigeneSchuleRepositoryFactory.getNewInstance()));
		return new KatalogControllerFactory(serviceFactory);
	}

	/**
	 * Lesezugriff im Stable-Modus. Keine besonderen Benutzerkompetenzen erforderlich.
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine KatalogControllerFactory-Instanz
	 */
	public static KatalogControllerFactory withReadAccessStable(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.STABLE, BenutzerKompetenz.KEINE);
	}

	/**
	 * Schreibzugriff im Stable-Modus. Erfordert {@link BenutzerKompetenz#KATALOG_EINTRAEGE_AENDERN}.
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine KatalogControllerFactory-Instanz
	 */
	public static KatalogControllerFactory withWriteAccessStable(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Löschzugriff im Stable-Modus. Erfordert {@link BenutzerKompetenz#KATALOG_EINTRAEGE_LOESCHEN}.
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine KatalogControllerFactory-Instanz
	 */
	public static KatalogControllerFactory withDeleteAccessStable(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.STABLE, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Lesezugriff im Dev-Modus. Keine besonderen Benutzerkompetenzen erforderlich.
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine KatalogControllerFactory-Instanz
	 */
	public static KatalogControllerFactory withReadAccessDev(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.DEV, BenutzerKompetenz.KEINE);
	}

	/**
	 * Schreibzugriff im Dev-Modus. Erfordert {@link BenutzerKompetenz#KATALOG_EINTRAEGE_AENDERN}.
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine KatalogControllerFactory-Instanz
	 */
	public static KatalogControllerFactory withWriteAccessDev(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.DEV, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN);
	}

	/**
	 * Löschzugriff im Dev-Modus. Erfordert {@link BenutzerKompetenz#KATALOG_EINTRAEGE_LOESCHEN}.
	 *
	 * @param request die HTTP-Anfrage zur Initialisierung der Datenbankverbindung
	 * @return eine KatalogControllerFactory-Instanz
	 */
	public static KatalogControllerFactory withDeleteAccessDev(final HttpServletRequest request) {
		return getNewInstance(request, ServerMode.DEV, BenutzerKompetenz.KATALOG_EINTRAEGE_LOESCHEN);
	}

	/**
	 * Erstellt eine neue FachklasseController-Instanz.
	 *
	 * @return ein neuer FachklasseController mit dem konfigurierten FachklasseSerice
	 */
	public FachklasseController getFachklasseController() {
		return new FachklasseControllerImpl(serviceFactory.getFachklasseService());
	}

	/**
	 * Erstellt eine neue MerkmalController-Instanz.
	 *
	 * @return ein neuer MerkmalController mit dem konfigurierten MerkmalService
	 */
	public MerkmalController getMerkmalController() {
		return new MerkmalControllerImpl(serviceFactory.getMerkmalService());
	}

	/**
	 * Erstellt einen neuen TeilLeistungsartenController.
	 *
	 * @return {@link TeilleistungsartController} - neu erzeugter Controller
	 */
	public TeilleistungsartController getTeilLeistungsartController() {
		return new TeilleistungsartControllerImpl(serviceFactory.getTeilLeistungsartenService());
	}

	/**
	 * Erstellt einen neuen OrtController.
	 *
	 * @return {@link OrtController} - neu erzeugter Controller
	 */
	public OrtController getOrtController() {
		return new OrtControllerImpl(serviceFactory.getOrtService());
	}

	/**
	 * Erstellt einen neuen OrtsteilController.
	 *
	 * @return {@link OrtsteilController} - neu erzeugter Controller
	 */
	public OrtsteilController getOrtsteilController() {
		return new OrtsteilControllerImpl(serviceFactory.getOrtsteilService());
	}

	/**
	 * Erstellt einen neuen ReligionController.
	 *
	 * @return {@link ReligionController} - neu erzeugter Controller
	 */
	public ReligionController getReligionController() {
		return new ReligionControllerImpl(serviceFactory.getReligionService());
	}

	/**
	 * Erstellt einen neuen AnkreuzkompetenzJahrgangController.
	 *
	 * @return {@link AnkreuzkompetenzJahrgangController} - neu erzeugter Controller
	 */
	public AnkreuzkompetenzJahrgangController getAnkreuzkompetenzJahrgangController() {
		return new AnkreuzkompetenzJahrgangControllerImpl(serviceFactory.getAnkreuzkompetenzJahrgangService());
	}

}
