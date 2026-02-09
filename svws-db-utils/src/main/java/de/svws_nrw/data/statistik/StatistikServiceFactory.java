package de.svws_nrw.data.statistik;

import de.svws_nrw.data.lehrer.LehrerServiceFactory;
import de.svws_nrw.data.schule.SchuleServiceFactory;
import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;

/**
 * Eine Factory zum Erstellen der Statistik-spezifischen Services
 */
public final class StatistikServiceFactory {

	/** die Factory für die Katalog-Repositories */
	private final KatalogeRepositoryFactory katalogeRepositoryFactory;

	/** die Factory für die Klassen-Repositories */
	private final KlassenRepositoryFactory klassenRepositoryFactory;

	/** die Factory für die Lehrer-Repositories */
	private final LehrerRepositoryFactory lehrerRepositoryFactory;

	/** die Factory für die Schüler-Repositories */
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;

	/** die Factory für die Schule-Repositories */
	private final SchuleRepositoryFactory schuleRepositoryFactory;

	/** die Factory für Lehrer-Services */
	private final LehrerServiceFactory lehrerServiceFactory;

	/** die Factory für Schul-Services */
	private final SchuleServiceFactory schuleServiceFactory;


	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param katalogeRepositoryFactory    die Factory für Kataloge-Repositories
	 * @param klassenRepositoryFactory     die Factory für Klassen-Repositories
	 * @param lehrerRepositoryFactory      die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory    die Factory für Schüler-Repositories
	 * @param schuleRepositoryFactory      die Factory für Schule-Repositories
	 * @param lehrerServiceFactory         die Factory für Lehrer-Services
	 * @param schuleServiceFactory         die Factory für Schul-Services
	 */
	private StatistikServiceFactory(final KatalogeRepositoryFactory katalogeRepositoryFactory,
			final KlassenRepositoryFactory klassenRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final SchuleRepositoryFactory schuleRepositoryFactory,
			final LehrerServiceFactory lehrerServiceFactory,
			final SchuleServiceFactory schuleServiceFactory) {
		this.katalogeRepositoryFactory = katalogeRepositoryFactory;
		this.klassenRepositoryFactory = klassenRepositoryFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.schuleRepositoryFactory = schuleRepositoryFactory;
		this.lehrerServiceFactory = lehrerServiceFactory;
		this.schuleServiceFactory = schuleServiceFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param katalogeRepositoryFactory    die Factory für Kataloge-Repositories
	 * @param klassenRepositoryFactory     die Factory für Klassen-Repositories
	 * @param lehrerRepositoryFactory      die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory    die Factory für Schüler-Repositories
	 * @param schuleRepositoryFactory      die Factory für Schule-Repositories
	 * @param lehrerServiceFactory         die Factory für Lehrer-Services
	 * @param schuleServiceFactory         die Factory für Schul-Services
	 *
	 * @return die neue Factory-Instanz
	 */
	public static StatistikServiceFactory getNewInstance(final KatalogeRepositoryFactory katalogeRepositoryFactory,
			final KlassenRepositoryFactory klassenRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final SchuleRepositoryFactory schuleRepositoryFactory,
			final LehrerServiceFactory lehrerServiceFactory,
			final SchuleServiceFactory schuleServiceFactory) {
		return new StatistikServiceFactory(katalogeRepositoryFactory, klassenRepositoryFactory, lehrerRepositoryFactory, schuelerRepositoryFactory,
				schuleRepositoryFactory, lehrerServiceFactory, schuleServiceFactory);
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Förderschwerpunkten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Förderschwerpunkten
	 */
	public FoerderschwerpunkteStatistikService getFoerderschwerpunkteStatistikService() {
		return new FoerderschwerpunkteStatistikService(katalogeRepositoryFactory.getFoerderschwerpunkteRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Jahrgängen für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Jahrgängen
	 */
	public JahrgaengeStatistikService getJahrgaengeStatistikService() {
		return new JahrgaengeStatistikService(katalogeRepositoryFactory.getJahrgaengeRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Klassendaten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Klassendaten
	 */
	public KlassenStatistikService getKlassenStatistikService() {
		return new KlassenStatistikService(schuleRepositoryFactory.getSchuleRepository(),
				klassenRepositoryFactory.getKlassenRepository(),
				klassenRepositoryFactory.getKlassenleitungenRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Lehrerdaten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Lehrerdaten
	 */
	public LehrerStatistikService getLehrerStatistikService() {
		return new LehrerStatistikService(schuleRepositoryFactory.getSchuleRepository(),
				lehrerRepositoryFactory.getLehrerRepository(),
				lehrerRepositoryFactory.getLehrerAbschnittsdatenRepository(),
				lehrerServiceFactory.getLehrerLehramtService(),
				lehrerServiceFactory.getLehrerPersonalabschnittsdatenAnrechnungsstundenService());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Orten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Orten
	 */
	public OrteStatistikService getOrteStatistikService() {
		return new OrteStatistikService(katalogeRepositoryFactory.getOrteRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Religionen für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Religionen
	 */
	public ReligionStatistikService getReligionStatistikService() {
		return new ReligionStatistikService(katalogeRepositoryFactory.getReligionRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Schülerdaten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Schülerdaten
	 */
	public SchuelerStatistikService getSchuelerStatistikService() {
		return new SchuelerStatistikService(schuleRepositoryFactory.getSchuleRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerAbiturRepository(),
				schuelerRepositoryFactory.getSchuelerAbiturFachRepository(),
				katalogeRepositoryFactory.getFachRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Schuldaten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Schuldaten
	 */
	public SchuleStatistikService getSchuleStatistikService() {
		return new SchuleStatistikService(schuleRepositoryFactory.getSchuleRepository(), schuleServiceFactory.getSchuljahresabschnittService());
	}


	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf die Statistikdaten.
	 *
	 * @return der Statistik-Service
	 */
	public StatistikService getStatistikService() {
		return new StatistikService(
				this.getSchuleStatistikService(),
				this.getLehrerStatistikService(),
				this.getKlassenStatistikService(),
				this.getSchuelerStatistikService(),
				this.getJahrgaengeStatistikService(),
				this.getOrteStatistikService(),
				this.getFoerderschwerpunkteStatistikService(),
				this.getReligionStatistikService()
		);
	}


}
