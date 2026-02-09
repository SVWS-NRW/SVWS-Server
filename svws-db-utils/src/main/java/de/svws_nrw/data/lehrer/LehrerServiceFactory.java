package de.svws_nrw.data.lehrer;

import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;

/**
 * Eine Factory zum Erstellen der Lehrer-spezifischen Services
 */
public final class LehrerServiceFactory {

	/** die Factory für die Lehrer-Repositories */
	private final LehrerRepositoryFactory lehrerRepositoryFactory;

	/** die Factory für die Schule-Repositories */
	private final SchuleRepositoryFactory schuleRepositoryFactory;


	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param lehrerRepositoryFactory   die Factory für Lehrer-Repositories
	 * @param schuleRepositoryFactory   die Factory für Schule-Repositories
	 */
	private LehrerServiceFactory(final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuleRepositoryFactory schuleRepositoryFactory) {
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.schuleRepositoryFactory = schuleRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param lehrerRepositoryFactory   die Factory für Lehrer-Repositories
	 * @param schuleRepositoryFactory   die Factory für Schule-Repositories
	 *
	 * @return die neue Factory-Instanz
	 */
	public static LehrerServiceFactory getNewInstance(final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuleRepositoryFactory schuleRepositoryFactory) {
		return new LehrerServiceFactory(lehrerRepositoryFactory, schuleRepositoryFactory);
	}


	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf Lehrämter von Lehrern.
	 *
	 * @return der Service für die Lehrämter von Lehrern
	 */
	public LehrerLehramtService getLehrerLehramtService() {
		return new LehrerLehramtService(lehrerRepositoryFactory.getLehrerPersonaldatenLehramtRepository(),
				this.getLehrerFachrichtungService(),
				this.getLehrerLehrbefaehigungService());
	}

	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf die Lehrbefähigungen von Lehrern.
	 *
	 * @return der Service für die Lehrbefähigungen von Lehrern
	 */
	public LehrerLehrbefaehigungService getLehrerLehrbefaehigungService() {
		return new LehrerLehrbefaehigungService(lehrerRepositoryFactory.getLehrerPersonaldatenLehramtLehrbefaehigungenRepository());
	}

	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf die Fachrichtungen von Lehrern.
	 *
	 * @return der Service für die Fachrichtungen von Lehrern.
	 */
	public LehrerFachrichtungService getLehrerFachrichtungService() {
		return new LehrerFachrichtungService(lehrerRepositoryFactory.getLehrerPersonaldatenLehramtFachrichtungRepository());
	}

	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf die Anrechnungsstunden bei Lehrern.
	 *
	 * @return der Service für die Anrechnungsstunden bei Lehrern
	 */
	public LehrerPersonalabschnittsdatenAnrechnungsstundenService getLehrerPersonalabschnittsdatenAnrechnungsstundenService() {
		return new LehrerPersonalabschnittsdatenAnrechnungsstundenService(schuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				lehrerRepositoryFactory.getLehrerMehrleistungRepository(),
				lehrerRepositoryFactory.getLehrerMinderleistungRepository(),
				lehrerRepositoryFactory.getLehrerAnrechnungRepository());
	}

}
