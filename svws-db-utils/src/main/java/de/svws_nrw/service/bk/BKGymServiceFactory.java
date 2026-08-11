package de.svws_nrw.service.bk;

import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
/**
 * Eine Factory zum Erstellen der Services für das Berufliche Gymnasium
 */
public final class BKGymServiceFactory {

	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;
	private final KatalogRepositoryFactory katalogRepositoryFactory;

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 * @param eigeneSchuleRepositoryFactory     die Factory für Schul-Repositories
	 * @param katalogRepositoryFactory   die Factory für Kataloge-Repositories
	 */
	private BKGymServiceFactory(final SchuelerRepositoryFactory schuelerRepositoryFactory, final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory) {
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.eigeneSchuleRepositoryFactory = eigeneSchuleRepositoryFactory;
		this.katalogRepositoryFactory = katalogRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 * @param eigeneSchuleRepositoryFactory     die Factory für Schul-Repositories
	 * @param katalogRepositoryFactory   die Factory für Kataloge-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static BKGymServiceFactory getNewInstance(final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory, final KatalogRepositoryFactory katalogRepositoryFactory) {
		return new BKGymServiceFactory(schuelerRepositoryFactory, eigeneSchuleRepositoryFactory, katalogRepositoryFactory);
	}


	/**
	 * Erstellt einen neuen Service für das Bereitstellen der Abiturdaten im Beruflichen Gymnasium
	 *
	 * @return der Service
	 */
	public BKGymAbiturdatenService getAbiturdatenService() {
		return new BKGymAbiturdatenService(BKGymAbiturdatenServiceKontext.of(
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				schuelerRepositoryFactory.getSchuelerRepository()));
	}


	/**
	 * Erstellt einen neuen Service für das Bereitstellen der Leistungsdaten im Beruflichen Gymnasium
	 *
	 * @return der Service
	 */
	public BKGymLeistungsdatenService getLeistungsdatenService() {
		return new BKGymLeistungsdatenService(BKGymLeistungsdatenServiceKontext.of(
				eigeneSchuleRepositoryFactory.getSchuleRepository(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				katalogRepositoryFactory.getFachRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				new de.svws_nrw.service.schueler.SchuelerSprachenfolgeService(
						schuelerRepositoryFactory.getSchuelerSprachenfolgeRepository()),
				new de.svws_nrw.service.schueler.SchuelerSprachpruefungenService(
						schuelerRepositoryFactory.getSchuelerSprachpruefungenRepository()),
				katalogRepositoryFactory.getJahrgangRepository()));
	}

}
