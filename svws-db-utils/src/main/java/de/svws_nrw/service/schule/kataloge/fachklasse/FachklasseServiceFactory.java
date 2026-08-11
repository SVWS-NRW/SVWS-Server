package de.svws_nrw.service.schule.kataloge.fachklasse;

import de.svws_nrw.mapper.schule.kataloge.fachklasse.FachklasseMapper;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schueler.schulbesuch.SchulbesuchServiceFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;

public final class FachklasseServiceFactory {

	private final KatalogRepositoryFactory repoFactory;
	private final FachklasseMapper mapper;
	private final SchuleServiceFactory schuleServiceFactory;

	/**
	 * @param repoFactory {@link KatalogRepositoryFactory}
	 * @param mapper {@link FachklasseMapper}
	 * @param schuleServiceFactory {@link SchulbesuchServiceFactory}
	 */
	public FachklasseServiceFactory(
			final KatalogRepositoryFactory repoFactory,
			final FachklasseMapper mapper,
			final SchuleServiceFactory schuleServiceFactory
	) {
		this.repoFactory = repoFactory;
		this.mapper = mapper;
		this.schuleServiceFactory = schuleServiceFactory;
	}

	/**
	 * @param repoFactory {@link KatalogRepositoryFactory}
	 * @param mapper {@link FachklasseMapper}
	 * @param schuleServiceFactory {@link SchulbesuchServiceFactory}
	 *
	 * @return eine neue Instanz der FachklasseServiceFactory
	 */
	public static FachklasseServiceFactory getNewInstance(
			final KatalogRepositoryFactory repoFactory,
			final FachklasseMapper mapper,
			final SchuleServiceFactory schuleServiceFactory) {
		return new FachklasseServiceFactory(repoFactory, mapper, schuleServiceFactory);
	}

	/**
	 * @return eine neue Instanz des FachklasseService
	 */
	public FachklasseService getService() {
		return new FachklasseService(repoFactory.getFachklasseRepository(), mapper, schuleServiceFactory.getSchuleService());
	}
}
