package de.svws_nrw.service.kurse;

import de.svws_nrw.mapper.kurse.KursMapper;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;

/**
 * Eine Factory zum Erstellen der Services für Kurse.
 */
public final class KursServiceFactory {

	private final KurseRepositoryFactory kurseRepositoryFactory;

	private final SchuelerRepositoryFactory schuelerRepositoryFactory;

	private final LehrerRepositoryFactory lehrerRepositoryFactory;

	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;

	private final KatalogRepositoryFactory katalogRepositoryFactory;

	private KursServiceFactory(
			final KurseRepositoryFactory kurseRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory) {
		this.kurseRepositoryFactory = kurseRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.eigeneSchuleRepositoryFactory = eigeneSchuleRepositoryFactory;
		this.katalogRepositoryFactory = katalogRepositoryFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory.
	 *
	 * @param kurseRepositoryFactory            die Factory für die Kurs-Repositories
	 * @param schuelerRepositoryFactory         die Factory für die Schüler-Repositories
	 * @param lehrerRepositoryFactory           die Factory für die Lehrer-Repositories
	 * @param eigeneSchuleRepositoryFactory     die Factory für die Repositories der eigenen Schule
	 * @param katalogRepositoryFactory          die Factory für die Katalog-Repositories
	 *
	 * @return die Factory
	 */
	public static KursServiceFactory getNewInstance(
			final KurseRepositoryFactory kurseRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory) {
		return new KursServiceFactory(kurseRepositoryFactory, schuelerRepositoryFactory, lehrerRepositoryFactory, eigeneSchuleRepositoryFactory,
				katalogRepositoryFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory mit neuen Repository-Factories.
	 *
	 * @return die Factory
	 */
	public static KursServiceFactory getNewInstance() {
		return new KursServiceFactory(
				KurseRepositoryFactory.getNewInstance(),
				SchuelerRepositoryFactory.getNewInstance(),
				LehrerRepositoryFactory.getNewInstance(),
				EigeneSchuleRepositoryFactory.getNewInstance(),
				KatalogRepositoryFactory.getNewInstance());
	}

	/**
	 * Erstellt einen neuen Service für den Zugriff auf Kurse.
	 *
	 * @return der Service
	 */
	public KursService getKursService() {
		final var repositories = new KursRepositories(
				kurseRepositoryFactory.getKurseRepository(),
				kurseRepositoryFactory.getKursSchuelerRepository(),
				kurseRepositoryFactory.getKurslehrerRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				katalogRepositoryFactory.getJahrgangRepository(),
				katalogRepositoryFactory.getFachRepository(),
				lehrerRepositoryFactory.getLehrerRepository(),
				katalogRepositoryFactory.getSchuleRepository(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository()
		);
		return new KursService(repositories, KursMapper.INSTANCE);
	}

}
