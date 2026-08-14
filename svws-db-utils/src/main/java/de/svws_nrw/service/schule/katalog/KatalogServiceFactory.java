package de.svws_nrw.service.schule.katalog;

import de.svws_nrw.mapper.schule.katalog.fachklasse.FachklasseMapper;
import de.svws_nrw.mapper.schule.katalog.merkmal.MerkmalMapper;
import de.svws_nrw.mapper.schule.katalog.ort.OrtMapper;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.fachklasse.FachklasseService;
import de.svws_nrw.service.schule.katalog.merkmal.MerkmalService;
import de.svws_nrw.service.schule.katalog.ort.OrtService;
import de.svws_nrw.service.schule.katalog.teilleistungsart.TeilleistungsartService;

public final class KatalogServiceFactory {

	private final KatalogRepositoryFactory katalogRepositoryFactory;
	private final EigeneSchuleServiceFactory eigeneSchuleServiceFactory;

	private KatalogServiceFactory(
			final KatalogRepositoryFactory katalogRepositoryFactory,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory) {
		this.katalogRepositoryFactory = katalogRepositoryFactory;
		this.eigeneSchuleServiceFactory = eigeneSchuleServiceFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param repositoryFactory   die Factory für Katalog-Repositories
	 * @param eigeneSchuleRepositoryFactory   die Factory für EigeneSchule-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static KatalogServiceFactory getNewInstance(
			final KatalogRepositoryFactory repositoryFactory,
			final EigeneSchuleServiceFactory eigeneSchuleRepositoryFactory) {
		return new KatalogServiceFactory(repositoryFactory, eigeneSchuleRepositoryFactory);
	}

	/**
	 * Erstellt einen neuen TeilleistungsartenService für den Zugriff auf die Katalogdaten.
	 *
	 * @return {@link TeilleistungsartService} - neu erzeugter Service
	 */
	public TeilleistungsartService getTeilLeistungsartenService() {
		return new TeilleistungsartService(katalogRepositoryFactory.getTeilleistungsartRepository());
	}

	/**
	 * Erstellt eine neue Instanz des MerkmalService.
	 *
	 * @return eine neue Instanz des MerkmalService
	 */
	public MerkmalService getMerkmalService() {
		return new MerkmalService(katalogRepositoryFactory.getMerkmalRepository(), MerkmalMapper.INSTANCE);
	}

	/**
	 * @return eine neue Instanz des FachklasseService
	 */
	public FachklasseService getFachklasseService() {
		return new FachklasseService(
				katalogRepositoryFactory.getFachklasseRepository(),
				FachklasseMapper.INSTANCE,
				eigeneSchuleServiceFactory.getSchuleService());
	}

	/**
	 * Erstellt eine neue Instanz des OrtService.
	 *
	 * @return eine neue Instanz des OrtService.
	 */
	public OrtService getOrtService() {
		return new OrtService(
				katalogRepositoryFactory.getOrtRepository(),
				OrtMapper.INSTANCE,
				eigeneSchuleServiceFactory.getSchuleService());
	}

}
