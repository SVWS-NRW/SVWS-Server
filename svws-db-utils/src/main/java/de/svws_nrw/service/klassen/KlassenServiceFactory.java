package de.svws_nrw.service.klassen;

import de.svws_nrw.mapper.klassen.KlassenMapper;
import de.svws_nrw.mapper.schueler.SchuelerMapper;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;

/**
 * Eine Factory zum Erstellen der Services für Klassen
 */
public final class KlassenServiceFactory {

	private final KlassenRepositoryFactory klassenRepositoryFactory;
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;
	private final LehrerRepositoryFactory lehrerRepositoryFactory;
	private final KatalogRepositoryFactory katalogRepositoryFactory;
	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;
	private final EigeneSchuleServiceFactory eigeneSchuleServiceFactory;

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param klassenRepositoryFactory        die Factory für Klassen-Repositories
	 * @param schuelerRepositoryFactory       die Factory für Schüler-Repositories
	 * @param lehrerRepositoryFactory         die Factory für Lehrer-Repositories
	 * @param katalogRepositoryFactory        die Factory für Katalog-Repositories (Jahrgänge)
	 * @param eigeneSchuleRepositoryFactory   die Factory für Repositories der eigenen Schule (Teilstandorte)
	 * @param eigeneSchuleServiceFactory      die Factory für Services der eigenen Schule
	 */
	private KlassenServiceFactory(
			final KlassenRepositoryFactory klassenRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory) {
		this.klassenRepositoryFactory = klassenRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.katalogRepositoryFactory = katalogRepositoryFactory;
		this.eigeneSchuleRepositoryFactory = eigeneSchuleRepositoryFactory;
		this.eigeneSchuleServiceFactory = eigeneSchuleServiceFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param klassenRepositoryFactory        die Factory für Klassen-Repositories
	 * @param schuelerRepositoryFactory       die Factory für Schüler-Repositories
	 * @param lehrerRepositoryFactory         die Factory für Lehrer-Repositories
	 * @param katalogRepositoryFactory        die Factory für Katalog-Repositories (Jahrgänge)
	 * @param eigeneSchuleRepositoryFactory   die Factory für Repositories der eigenen Schule (Teilstandorte)
	 * @param eigeneSchuleServiceFactory      die Factory für Services der eigenen Schule
	 *
	 * @return die Factory
	 */
	public static KlassenServiceFactory getNewInstance(
			final KlassenRepositoryFactory klassenRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory) {
		return new KlassenServiceFactory(klassenRepositoryFactory, schuelerRepositoryFactory, lehrerRepositoryFactory, katalogRepositoryFactory,
				eigeneSchuleRepositoryFactory, eigeneSchuleServiceFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @return die Factory
	 */
	public static KlassenServiceFactory getNewInstance() {
		final var eigeneSchuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();

		return new KlassenServiceFactory(KlassenRepositoryFactory.getNewInstance(), SchuelerRepositoryFactory.getNewInstance(),
				LehrerRepositoryFactory.getNewInstance(), KatalogRepositoryFactory.getNewInstance(), eigeneSchuleRepositoryFactory,
				EigeneSchuleServiceFactory.getNewInstance(eigeneSchuleRepositoryFactory));
	}

	/**
	 * Erstellt einen neuen Service für den Zugriff auf Klassen
	 *
	 * @return der Service
	 */
	public KlassenService getKlassenService() {
		final var klassenRepository = klassenRepositoryFactory.getKlassenRepository();
		final var klassenleitungenRepository = klassenRepositoryFactory.getKlassenleitungenRepository();
		final var schuljahresabschnittService = eigeneSchuleServiceFactory.getSchuljahresabschnittService();
		final var eigeneSchuleService = eigeneSchuleServiceFactory.getSchuleService();
		final var lernabschnittRepository = schuelerRepositoryFactory.getSchuelerLernabschnittRepository();
		final var schuelerLoader = new KlassenSchuelerLoader(
				lernabschnittRepository,
				schuelerRepositoryFactory.getSchuelerRepository(),
				SchuelerMapper.INSTANCE);
		final var kontextLoader = new KlassenMappingContextLoader(
				klassenRepository,
				klassenleitungenRepository,
				schuelerLoader,
				eigeneSchuleService,
				schuljahresabschnittService);
		final var jahrgangRepository = katalogRepositoryFactory.getJahrgangRepository();
		final var repositories = new KlassenRepositories(klassenRepository, jahrgangRepository, eigeneSchuleRepositoryFactory.getTeilstandortRepository(),
				lernabschnittRepository);
		final var defaultSorter = new KlassenDefaultSorter(klassenRepository, jahrgangRepository);

		return new KlassenService(
				repositories,
				schuljahresabschnittService,
				eigeneSchuleService,
				kontextLoader,
				KlassenMapper.INSTANCE,
				getKlassenleitungService(),
				defaultSorter);
	}

	/**
	 * Erstellt einen neuen Service für die Klassenleitungen einer Klasse
	 *
	 * @return der Service
	 */
	public KlassenleitungService getKlassenleitungService() {
		return new KlassenleitungService(
				klassenRepositoryFactory.getKlassenRepository(),
				klassenRepositoryFactory.getKlassenleitungenRepository(),
				lehrerRepositoryFactory.getLehrerRepository());
	}

}
