package de.svws_nrw.service.wiedervorlage;

import de.svws_nrw.mapper.WiedervorlageMapper;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.erzieher.ErzieherRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.wiedervorlage.WiedervorlageRepositoryFactory;

/**
 * Factory für {@link WiedervorlageService}.
 */
public final class WiedervorlageServiceFactory {

	private final WiedervorlageRepositoryFactory wiedervorlageRepositoryFactory;
	private final BenutzerRepositoryFactory benutzerRepositoryFactory;
	private final LehrerRepositoryFactory lehrerRepositoryFactory;
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;
	private final ErzieherRepositoryFactory erzieherRepositoryFactory;

	private WiedervorlageServiceFactory(final WiedervorlageRepositoryFactory wiedervorlageRepositoryFactory,
			final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final ErzieherRepositoryFactory erzieherRepositoryFactory) {
		this.wiedervorlageRepositoryFactory = wiedervorlageRepositoryFactory;
		this.benutzerRepositoryFactory = benutzerRepositoryFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.erzieherRepositoryFactory = erzieherRepositoryFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der {@link WiedervorlageServiceFactory}.
	 *
	 * @param wiedervorlageRepositoryFactory Repository-Factory für die Wiedervorlage-Domäne
	 * @param benutzerRepositoryFactory Repository-Factory für die Benutzer-Domäne
	 * @param lehrerRepositoryFactory Repository-Factory für die Lehrer-Domäne
	 * @param schuelerRepositoryFactory Repository-Factory für die Schueler-Domäne
	 * @param erzieherRepositoryFactory Repository-Factory für die Erzieher-Domäne
	 *
	 * @return neu erzeugte Instanz von {@link WiedervorlageServiceFactory}
	 */
	public static WiedervorlageServiceFactory getNewInstance(final WiedervorlageRepositoryFactory wiedervorlageRepositoryFactory,
			final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final ErzieherRepositoryFactory erzieherRepositoryFactory) {
		return new WiedervorlageServiceFactory(wiedervorlageRepositoryFactory, benutzerRepositoryFactory, lehrerRepositoryFactory, schuelerRepositoryFactory,
				erzieherRepositoryFactory);
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
				benutzerRepositoryFactory.getBenutzerRepository(),
				lehrerRepositoryFactory.getLehrerRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				erzieherRepositoryFactory.getErzieherRepository(),
				WiedervorlageMapper.INSTANCE);
	}

}
