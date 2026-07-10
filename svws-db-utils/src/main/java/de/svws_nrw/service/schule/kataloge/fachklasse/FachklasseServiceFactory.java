package de.svws_nrw.service.schule.kataloge.fachklasse;

import de.svws_nrw.mapper.schule.kataloge.fachklasse.FachklasseMapper;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepositoryFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;

public final class FachklasseServiceFactory {

	private final FachklasseRepositoryFactory repoFactory;
	private final FachklasseMapper mapper;
	private final SchuleServiceFactory schuleServiceFactory;

	/**
	 * @param repoFactory {@link FachklasseRepositoryFactory}
	 * @param mapper {@link FachklasseMapper}
	 * @param schuleServiceFactory {@link de.svws_nrw.service.schueler.schulbesuch.SchulbesuchServiceFactory}
	 */
	public FachklasseServiceFactory(
			final FachklasseRepositoryFactory repoFactory,
			final FachklasseMapper mapper,
			final SchuleServiceFactory schuleServiceFactory
	) {
		this.repoFactory = repoFactory;
		this.mapper = mapper;
		this.schuleServiceFactory = schuleServiceFactory;
	}

	/**
	 * @param repoFactory {@link FachklasseRepositoryFactory}
	 * @param mapper {@link FachklasseMapper}
	 * @param schuleServiceFactory {@link de.svws_nrw.service.schueler.schulbesuch.SchulbesuchServiceFactory}
	 *
	 * @return eine neue Instanz der FachklasseServiceFactory
	 */
	public static FachklasseServiceFactory getNewInstance(
			final FachklasseRepositoryFactory repoFactory,
			final FachklasseMapper mapper,
			final SchuleServiceFactory schuleServiceFactory) {
		return new FachklasseServiceFactory(repoFactory, mapper, schuleServiceFactory);
	}

	/**
	 * @return eine neue Instanz des FachklasseService
	 */
	public FachklasseService getService() {
		return new FachklasseService(repoFactory.getRepository(), mapper, schuleServiceFactory.getSchuleService());
	}
}
