package de.svws_nrw.service.lehrer;

import de.svws_nrw.mapper.lehrer.LehrerMehrleistungMapper;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import de.svws_nrw.mapper.lehrer.LehrerMinderleistungMapper;
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
	 * Erzeugt eine neue Instanz der Service-Factory für die vorübergehende Verwendung in Data-Klasse
	 *
	 * @return die neue Factory-Instanz
	 */
	public static LehrerServiceFactory getNewInstance() {
		return new LehrerServiceFactory(
				LehrerRepositoryFactory.getNewInstance(),
				SchuleRepositoryFactory.getNewInstance());
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

	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Unterrichtsfächer von Lehrern.
	 *
	 * @return der Service für die Unterrichtsfächer von Lehrern
	 */
	public LehrerUnterrichtsfachService getLehrerUnterrichtsfachService() {
		return new LehrerUnterrichtsfachService(lehrerRepositoryFactory.getLehrerUnterrichtsfachRepository());
	}

	/**
	 * Erstellt einen neuen Service für den Zugriff auf Einträge zu den Anrechnungsstunden bei Lehrern.
	 *
	 * @return der Service für die Fachrichtungen von Lehrern.
	 */
	public LehrerAnrechnungsstundenService getLehrerAnrechnungsstundenService() {
		return new LehrerAnrechnungsstundenService(LehrerAnrechnungsstundenServiceKontext.of(
				schuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				lehrerRepositoryFactory.getLehrerPersonalabschnittsdatenRepository(),
				lehrerRepositoryFactory.getLehrerAnrechnungRepository()));
	}

	/**
	 * Erstellt einen neuen Service für den Zugriff auf Einträge zu den Mehrleistungen bei Lehrern.
	 *
	 * @return der Service für die Mehrleistungen von Lehrern.
	 */
	public LehrerMehrleistungService getLehrerMehrleistungService() {
		return new LehrerMehrleistungService(LehrerMehrleistungServiceKontext.of(
				schuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				lehrerRepositoryFactory.getLehrerPersonalabschnittsdatenRepository(),
				lehrerRepositoryFactory.getLehrerMehrleistungRepository()),
				LehrerMehrleistungMapper.INSTANCE);
	}

	/**
	 * Erstellt einen neuen Service für den Zugriff auf Einträge zu den Minderleistungen bei Lehrern.
	 *
	 * @return der Service für die Minderleistungen von Lehrern.
	 */
	public LehrerMinderleistungService getLehrerMinderleistungService() {
		return new LehrerMinderleistungService(
				lehrerRepositoryFactory.getLehrerMinderleistungRepository(),
				schuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				lehrerRepositoryFactory.getLehrerPersonalabschnittsdatenRepository(),
				LehrerMinderleistungMapper.INSTANCE,
				CoreTypeDataManager.getManager(LehrerMinderleistungsarten.class)
		);
	}

}
