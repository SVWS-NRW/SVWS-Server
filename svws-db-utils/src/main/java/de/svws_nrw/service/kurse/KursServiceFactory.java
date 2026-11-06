package de.svws_nrw.service.kurse;

import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;

/**
 * Eine Factory zum Erstellen der Services für Kurse.
 */
public final class KursServiceFactory {

	private final KurseRepositoryFactory kurseRepositoryFactory;

	private final SchuelerRepositoryFactory schuelerRepositoryFactory;

	private final KatalogRepositoryFactory katalogRepositoryFactory;

	private KursServiceFactory(
			final KurseRepositoryFactory kurseRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory) {
		this.kurseRepositoryFactory = kurseRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.katalogRepositoryFactory = katalogRepositoryFactory;
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory.
	 *
	 * @param kurseRepositoryFactory       die Factory für die Kurs-Repositories
	 * @param schuelerRepositoryFactory    die Factory für die Schüler-Repositories
	 * @param katalogRepositoryFactory     die Factory für die Katalog-Repositories
	 *
	 * @return die Factory
	 */
	public static KursServiceFactory getNewInstance(
			final KurseRepositoryFactory kurseRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory) {
		return new KursServiceFactory(kurseRepositoryFactory, schuelerRepositoryFactory, katalogRepositoryFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory mit neuen Repository-Factories.
	 *
	 * @return die Factory
	 */
	public static KursServiceFactory getNewInstance() {
		return new KursServiceFactory(KurseRepositoryFactory.getNewInstance(), SchuelerRepositoryFactory.getNewInstance(),
				KatalogRepositoryFactory.getNewInstance());
	}

	/**
	 * Erstellt einen neuen Service für den lesenden Zugriff auf Kurse.
	 *
	 * @return der Service
	 */
	public KursService getKursService() {
		return new KursService(
				kurseRepositoryFactory.getKurseRepository(),
				kurseRepositoryFactory.getKursSchuelerRepository(),
				kurseRepositoryFactory.getKurslehrerRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				katalogRepositoryFactory.getJahrgangRepository());
	}

}
