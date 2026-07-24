package de.svws_nrw.service.schule.logoverwaltung;

import java.time.Clock;
import java.time.ZoneId;

import de.svws_nrw.mapper.schule.logoverwaltung.LogoverwaltungMapper;
import de.svws_nrw.repo.schule.logoverwaltung.LogoverwaltungRepositoryFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;

public final class LogoverwaltungServiceFactory {

	private final LogoverwaltungRepositoryFactory repoFactory;
	private final LogoverwaltungMapper mapper;
	private final SchuleServiceFactory schuleServiceFactory;

	/**
	 * @param repoFactory {@link LogoverwaltungRepositoryFactory}
	 * @param mapper {@link LogoverwaltungMapper}
	 * @param schuleServiceFactory {@link SchuleServiceFactory}
	 */
	private LogoverwaltungServiceFactory(final LogoverwaltungRepositoryFactory repoFactory,
			final LogoverwaltungMapper mapper,
			final SchuleServiceFactory schuleServiceFactory) {
		this.repoFactory = repoFactory;
		this.mapper = mapper;
		this.schuleServiceFactory = schuleServiceFactory;
	}

	/**
	 * @param repoFactory {@link LogoverwaltungRepositoryFactory}
	 * @param mapper {@link LogoverwaltungMapper}
	 * @param schuleServiceFactory {@link SchuleServiceFactory}
	 *
	 * @return eine neue Instanz der LogoverwaltungServiceFactory
	 */
	public static LogoverwaltungServiceFactory getNewInstance(final LogoverwaltungRepositoryFactory repoFactory,
			final LogoverwaltungMapper mapper,
			final SchuleServiceFactory schuleServiceFactory) {
		return new LogoverwaltungServiceFactory(repoFactory, mapper, schuleServiceFactory);
	}

	/**
	 * @return eine neue Instanz des LogoverwaltungService
	 */
	public LogoverwaltungService getService() {
		return new LogoverwaltungService(repoFactory.getRepository(), mapper, schuleServiceFactory.getSchuleService(),
				Clock.system(ZoneId.of("Europe/Berlin")));
	}
}
