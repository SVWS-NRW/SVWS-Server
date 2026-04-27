package de.svws_nrw.service.bk;

import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;
/**
 * Eine Factory zum Erstellen der Services für das Berufliche Gymnasium
 */
public final class BKGymServiceFactory {

	private final SchuleRepositoryFactory schuleRepositoryFactory;
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;
	private final KatalogeRepositoryFactory katalogeRepositoryFactory;

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 * @param schuleRepositoryFactory     die Factory für Schul-Repositories
	 * @param katalogeRepositoryFactory   die Factory für Kataloge-Repositories
	 */
	private BKGymServiceFactory(final SchuelerRepositoryFactory schuelerRepositoryFactory, final SchuleRepositoryFactory schuleRepositoryFactory,
			final KatalogeRepositoryFactory katalogeRepositoryFactory) {
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.schuleRepositoryFactory = schuleRepositoryFactory;
		this.katalogeRepositoryFactory = katalogeRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 * @param schuleRepositoryFactory     die Factory für Schul-Repositories
	 * @param katalogeRepositoryFactory   die Factory für Kataloge-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static BKGymServiceFactory getNewInstance(final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final SchuleRepositoryFactory schuleRepositoryFactory, final KatalogeRepositoryFactory katalogeRepositoryFactory) {
		return new BKGymServiceFactory(schuelerRepositoryFactory, schuleRepositoryFactory, katalogeRepositoryFactory);
	}


	/**
	 * Erstellt einen neuen Service für das Bereitstellen der Abiturdaten im Beruflichen Gymnasium
	 *
	 * @return der Service
	 */
	public BKGymAbiturdatenService getAbiturdatenService() {
		return new BKGymAbiturdatenService(BKGymAbiturdatenServiceKontext.of(
				schuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				schuelerRepositoryFactory.getSchuelerRepository()));
	}


	/**
	 * Erstellt einen neuen Service für das Bereitstellen der Leistungsdaten im Beruflichen Gymnasium
	 *
	 * @return der Service
	 */
	public BKGymLeistungsdatenService getLeistungsdatenService() {
		return new BKGymLeistungsdatenService(BKGymLeistungsdatenServiceKontext.of(
				schuleRepositoryFactory.getSchuleRepository(),
				schuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				katalogeRepositoryFactory.getFachRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				new de.svws_nrw.service.schueler.SchuelerSprachenfolgeService(
						schuelerRepositoryFactory.getSchuelerSprachenfolgeRepository()),
				new de.svws_nrw.service.schueler.SchuelerSprachpruefungenService(
						schuelerRepositoryFactory.getSchuelerSprachpruefungenRepository()),
				katalogeRepositoryFactory.getJahrgaengeRepository()));
	}

}
