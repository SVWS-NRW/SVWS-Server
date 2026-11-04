package de.svws_nrw.service.klassen;

import de.svws_nrw.mapper.klassen.KlassenMapper;
import de.svws_nrw.mapper.schueler.SchuelerMapper;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;

/**
 * Eine Factory zum Erstellen der Services für Klassen
 */
public final class KlassenServiceFactory {

	private final KlassenRepositoryFactory klassenRepositoryFactory;
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;
	private final EigeneSchuleServiceFactory eigeneSchuleServiceFactory;

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param klassenRepositoryFactory       die Factory für Klassen-Repositories
	 * @param schuelerRepositoryFactory      die Factory für Schüler-Repositories
	 * @param eigeneSchuleServiceFactory     die Factory für Services der eigenen Schule
	 */
	private KlassenServiceFactory(final KlassenRepositoryFactory klassenRepositoryFactory, final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory) {
		this.klassenRepositoryFactory = klassenRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.eigeneSchuleServiceFactory = eigeneSchuleServiceFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param klassenRepositoryFactory       die Factory für Klassen-Repositories
	 * @param schuelerRepositoryFactory      die Factory für Schüler-Repositories
	 * @param eigeneSchuleServiceFactory     die Factory für Services der eigenen Schule
	 *
	 * @return die Factory
	 */
	public static KlassenServiceFactory getNewInstance(final KlassenRepositoryFactory klassenRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory, final EigeneSchuleServiceFactory eigeneSchuleServiceFactory) {
		return new KlassenServiceFactory(klassenRepositoryFactory, schuelerRepositoryFactory, eigeneSchuleServiceFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @return die Factory
	 */
	public static KlassenServiceFactory getNewInstance() {
		return new KlassenServiceFactory(KlassenRepositoryFactory.getNewInstance(), SchuelerRepositoryFactory.getNewInstance(),
				EigeneSchuleServiceFactory.getNewInstance(EigeneSchuleRepositoryFactory.getNewInstance()));
	}

	/**
	 * Erstellt einen neuen Service für den lesenden Zugriff auf Klassen
	 *
	 * @return der Service
	 */
	public KlassenService getKlassenService() {
		final var klassenRepository = klassenRepositoryFactory.getKlassenRepository();
		final var schuljahresabschnittService = eigeneSchuleServiceFactory.getSchuljahresabschnittService();
		final var schuelerLoader = new KlassenSchuelerLoader(
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				SchuelerMapper.INSTANCE);
		final var kontextLoader = new KlassenMappingContextLoader(
				klassenRepository,
				klassenRepositoryFactory.getKlassenleitungenRepository(),
				schuelerLoader,
				eigeneSchuleServiceFactory.getSchuleService(),
				schuljahresabschnittService);
		return new KlassenService(klassenRepository, schuljahresabschnittService, kontextLoader, KlassenMapper.INSTANCE);
	}

}
