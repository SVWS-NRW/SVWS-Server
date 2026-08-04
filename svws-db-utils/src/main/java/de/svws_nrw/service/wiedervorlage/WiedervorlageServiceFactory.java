package de.svws_nrw.service.wiedervorlage;

import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.svws_nrw.mapper.WiedervorlageMapper;
import de.svws_nrw.oauth.SchemaServiceFactory;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.erzieher.ErzieherRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.wiedervorlage.WiedervorlageRepositoryFactory;
import de.svws_nrw.service.wiedervorlage.cleanup.WiedervorlageCleanupService;

/**
 * Factory für {@link WiedervorlageService}.
 */
public final class WiedervorlageServiceFactory {

	private static final ConcurrentMap<String, LocalDate> RUN_DATE_BY_SCHEMA_INSTANCE = new ConcurrentHashMap<>();

	private final WiedervorlageRepositoryFactory wiedervorlageRepositoryFactory;
	private final BenutzerRepositoryFactory benutzerRepositoryFactory;
	private final LehrerRepositoryFactory lehrerRepositoryFactory;
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;
	private final ErzieherRepositoryFactory erzieherRepositoryFactory;
	private final SchemaServiceFactory schemaServiceFactory;


	private WiedervorlageServiceFactory(final WiedervorlageRepositoryFactory wiedervorlageRepositoryFactory,
			final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final ErzieherRepositoryFactory erzieherRepositoryFactory,
			final SchemaServiceFactory schemaServiceFactory) {
		this.wiedervorlageRepositoryFactory = wiedervorlageRepositoryFactory;
		this.benutzerRepositoryFactory = benutzerRepositoryFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.erzieherRepositoryFactory = erzieherRepositoryFactory;
		this.schemaServiceFactory = schemaServiceFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der {@link WiedervorlageServiceFactory}.
	 *
	 * @param wiedervorlageRepositoryFactory Repository-Factory für die Wiedervorlage-Domäne
	 * @param benutzerRepositoryFactory Repository-Factory für die Benutzer-Domäne
	 * @param lehrerRepositoryFactory Repository-Factory für die Lehrer-Domäne
	 * @param schuelerRepositoryFactory Repository-Factory für die Schueler-Domäne
	 * @param erzieherRepositoryFactory Repository-Factory für die Erzieher-Domäne
	 * @param schemaServiceFactory Service-Factory für Schemas
	 *
	 * @return neu erzeugte Instanz von {@link WiedervorlageServiceFactory}
	 */
	public static WiedervorlageServiceFactory getNewInstance(final WiedervorlageRepositoryFactory wiedervorlageRepositoryFactory,
			final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final ErzieherRepositoryFactory erzieherRepositoryFactory,
			final SchemaServiceFactory schemaServiceFactory) {
		return new WiedervorlageServiceFactory(wiedervorlageRepositoryFactory, benutzerRepositoryFactory, lehrerRepositoryFactory, schuelerRepositoryFactory,
				erzieherRepositoryFactory, schemaServiceFactory);
	}

	/**
	 * Erzeugt einen neuen {@link WiedervorlageService}.
	 *
	 * @return neu erzeugter {@link WiedervorlageService}
	 */
	public WiedervorlageService getWiedervorlageService() {
		return new WiedervorlageService(wiedervorlageRepositoryFactory.getWiedervorlageRepository(),
				benutzerRepositoryFactory.getBenutzergruppenMitgliedRepository(),
				benutzerRepositoryFactory.getBenutzergruppeRepository(),
				benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				benutzerRepositoryFactory.getViewBenutzerDetailsRepository(),
				lehrerRepositoryFactory.getLehrerRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				erzieherRepositoryFactory.getErzieherRepository(),
				WiedervorlageMapper.INSTANCE,
				getWiedervorlageCleanupService());
	}

	private WiedervorlageCleanupService getWiedervorlageCleanupService() {
		return new WiedervorlageCleanupService(RUN_DATE_BY_SCHEMA_INSTANCE,
				wiedervorlageRepositoryFactory.getWiedervorlageRepository(),
				schemaServiceFactory.getService());
	}

}
