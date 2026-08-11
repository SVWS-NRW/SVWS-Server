package de.svws_nrw.service.schule.merkmale;

import de.svws_nrw.mapper.schule.merkmale.MerkmalMapper;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;

/**
 * Factory für {@link MerkmalService}
 */
public final class MerkmalServiceFactory {

	private final KatalogRepositoryFactory repositoryFactory;
	private final MerkmalMapper mapper;

	private MerkmalServiceFactory(final KatalogRepositoryFactory repositoryFactory, final MerkmalMapper mapper) {
		this.repositoryFactory = repositoryFactory;
		this.mapper = mapper;
	}

	/**
	 * Erstellt eine neue Instanz der MerkmalServiceFactory.
	 *
	 * @param repositoryFactory das Repository für den Datenbankzugriff auf Merkmale
	 * @param mapper     der Mapper zur Konvertierung zwischen Domain- und API-Modellen
	 * @return eine neue Instanz der MerkmalServiceFactory
	 */
	public static MerkmalServiceFactory getNewInstance(final KatalogRepositoryFactory repositoryFactory, final MerkmalMapper mapper) {
		return new MerkmalServiceFactory(repositoryFactory, mapper);
	}

	/**
	 * Erstellt eine neue Instanz des MerkmalService.
	 *
	 * @return eine neue Instanz des MerkmalService
	 */
	public MerkmalService getMerkmalService() {
		return new MerkmalService(repositoryFactory.getMerkmalRepository(), mapper);
	}
}
