package de.svws_nrw.service.wiedervorlage;

import de.svws_nrw.mapper.WiedervorlageMapper;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.wiedervorlage.WiedervorlageRepositoryFactory;
import org.mapstruct.factory.Mappers;

/**
 * Factory für {@link WiedervorlageService}.
 */
public final class WiedervorlageServiceFactory {

	private final WiedervorlageRepositoryFactory wiedervorlageRepositoryFactory;
	private final BenutzerRepositoryFactory benutzerRepositoryFactory;

	private WiedervorlageServiceFactory(
			final WiedervorlageRepositoryFactory wiedervorlageRepositoryFactory,
			final BenutzerRepositoryFactory benutzerRepositoryFactory) {
		this.wiedervorlageRepositoryFactory = wiedervorlageRepositoryFactory;
		this.benutzerRepositoryFactory = benutzerRepositoryFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der {@link WiedervorlageServiceFactory}.
	 *
	 * @param wiedervorlageRepositoryFactory Repository-Factory für die Wiedervorlage-Domäne
	 * @param benutzerRepositoryFactory      Repository-Factory für die Benutzer-Domäne
	 *
	 * @return neu erzeugte {@link WiedervorlageServiceFactory}
	 */
	public static WiedervorlageServiceFactory getNewInstance(
			final WiedervorlageRepositoryFactory wiedervorlageRepositoryFactory,
			final BenutzerRepositoryFactory benutzerRepositoryFactory) {
		return new WiedervorlageServiceFactory(wiedervorlageRepositoryFactory, benutzerRepositoryFactory);
	}

	/**
	 * Erzeugt einen neuen {@link WiedervorlageService}.
	 *
	 * @return neu erzeugter {@link WiedervorlageService}
	 */
	public WiedervorlageService getWiedervorlageService() {
		return new WiedervorlageService(
				wiedervorlageRepositoryFactory.getWiedervorlageRepository(),
				benutzerRepositoryFactory.getBenutzergruppenMitgliedRepository(),
				benutzerRepositoryFactory.getBenutzergruppeRepository(),
				benutzerRepositoryFactory.getBenutzerRepository(),
				Mappers.getMapper(WiedervorlageMapper.class));
	}

}
