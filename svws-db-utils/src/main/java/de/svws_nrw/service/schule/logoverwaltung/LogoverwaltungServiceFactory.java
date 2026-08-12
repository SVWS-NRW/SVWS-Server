package de.svws_nrw.service.schule.logoverwaltung;

import java.time.Clock;
import java.time.ZoneId;

import de.svws_nrw.mapper.schule.logoverwaltung.LogoverwaltungMapper;
import de.svws_nrw.repo.schule.logoverwaltung.LogoverwaltungRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;

public final class LogoverwaltungServiceFactory {

	private final LogoverwaltungRepositoryFactory repoFactory;
	private final LogoverwaltungMapper mapper;
	private final EigeneSchuleServiceFactory eigeneSchuleServiceFactory;

	/**
	 * @param repoFactory {@link LogoverwaltungRepositoryFactory}
	 * @param mapper {@link LogoverwaltungMapper}
	 * @param eigeneSchuleServiceFactory {@link EigeneSchuleServiceFactory}
	 */
	private LogoverwaltungServiceFactory(final LogoverwaltungRepositoryFactory repoFactory,
			final LogoverwaltungMapper mapper,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory) {
		this.repoFactory = repoFactory;
		this.mapper = mapper;
		this.eigeneSchuleServiceFactory = eigeneSchuleServiceFactory;
	}

	/**
	 * @param repoFactory {@link LogoverwaltungRepositoryFactory}
	 * @param mapper {@link LogoverwaltungMapper}
	 * @param eigeneSchuleServiceFactory {@link EigeneSchuleServiceFactory}
	 *
	 * @return eine neue Instanz der LogoverwaltungServiceFactory
	 */
	public static LogoverwaltungServiceFactory getNewInstance(final LogoverwaltungRepositoryFactory repoFactory,
			final LogoverwaltungMapper mapper,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory) {
		return new LogoverwaltungServiceFactory(repoFactory, mapper, eigeneSchuleServiceFactory);
	}

	/**
	 * @return eine neue Instanz des LogoverwaltungService
	 */
	public LogoverwaltungService getService() {
		return new LogoverwaltungService(repoFactory.getRepository(), mapper, eigeneSchuleServiceFactory.getSchuleService(),
				Clock.system(ZoneId.of("Europe/Berlin")));
	}
}
