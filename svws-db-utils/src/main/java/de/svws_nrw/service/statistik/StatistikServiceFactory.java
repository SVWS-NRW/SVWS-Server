package de.svws_nrw.service.statistik;

import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;

/**
 * Eine Factory zum Erstellen der Statistik-spezifischen Services
 */
public final class StatistikServiceFactory {

	/** die Factory für die Benutzer-Repositories */
	private final BenutzerRepositoryFactory benutzerRepositoryFactory;

	/** die Factory für die Katalog-Repositories */
	private final KatalogRepositoryFactory katalogRepositoryFactory;

	/** die Factory für die Klassen-Repositories */
	private final KlassenRepositoryFactory klassenRepositoryFactory;

	/** die Factory für die Kurs-Repositories */
	private final KurseRepositoryFactory kurseRepositoryFactory;

	/** die Factory für die Lehrer-Repositories */
	private final LehrerRepositoryFactory lehrerRepositoryFactory;

	/** die Factory für die Schüler-Repositories */
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;

	/** die Factory für die Schule-Repositories */
	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;

	/** die Factory für Lehrer-Services */
	private final LehrerServiceFactory lehrerServiceFactory;

	/** die Factory für Schul-Services */
	private final EigeneSchuleServiceFactory eigeneSchuleServiceFactory;


	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param benutzerRepositoryFactory    die Factory für die Benutzer-Repositories
	 * @param katalogRepositoryFactory    die Factory für Kataloge-Repositories
	 * @param klassenRepositoryFactory     die Factory für Klassen-Repositories
	 * @param kurseRepositoryFactory       die Factory für Kurse-Repositories
	 * @param lehrerRepositoryFactory      die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory    die Factory für Schüler-Repositories
	 * @param eigeneSchuleRepositoryFactory      die Factory für Schule-Repositories
	 * @param lehrerServiceFactory         die Factory für Lehrer-Services
	 * @param eigeneSchuleServiceFactory         die Factory für Schul-Services
	 */
	private StatistikServiceFactory(final BenutzerRepositoryFactory benutzerRepositoryFactory, final KatalogRepositoryFactory katalogRepositoryFactory,
			final KlassenRepositoryFactory klassenRepositoryFactory,
			final KurseRepositoryFactory kurseRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final LehrerServiceFactory lehrerServiceFactory,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory) {
		this.benutzerRepositoryFactory = benutzerRepositoryFactory;
		this.katalogRepositoryFactory = katalogRepositoryFactory;
		this.klassenRepositoryFactory = klassenRepositoryFactory;
		this.kurseRepositoryFactory = kurseRepositoryFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.eigeneSchuleRepositoryFactory = eigeneSchuleRepositoryFactory;
		this.lehrerServiceFactory = lehrerServiceFactory;
		this.eigeneSchuleServiceFactory = eigeneSchuleServiceFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param benutzerRepositoryFactory    die Factory für die Benutzer-Repositories
	 * @param katalogRepositoryFactory    die Factory für Kataloge-Repositories
	 * @param klassenRepositoryFactory     die Factory für Klassen-Repositories
	 * @param kurseRepositoryFactory       die Factory für Kurse-Repositories
	 * @param lehrerRepositoryFactory      die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory    die Factory für Schüler-Repositories
	 * @param eigeneSchuleRepositoryFactory      die Factory für Schule-Repositories
	 * @param lehrerServiceFactory         die Factory für Lehrer-Services
	 * @param eigeneSchuleServiceFactory         die Factory für Schul-Services
	 *
	 * @return die neue Factory-Instanz
	 */
	public static StatistikServiceFactory getNewInstance(final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory,
			final KlassenRepositoryFactory klassenRepositoryFactory,
			final KurseRepositoryFactory kurseRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory,
			final LehrerServiceFactory lehrerServiceFactory,
			final EigeneSchuleServiceFactory eigeneSchuleServiceFactory) {
		return new StatistikServiceFactory(benutzerRepositoryFactory, katalogRepositoryFactory, klassenRepositoryFactory, kurseRepositoryFactory,
				lehrerRepositoryFactory,
				schuelerRepositoryFactory, eigeneSchuleRepositoryFactory, lehrerServiceFactory, eigeneSchuleServiceFactory);
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Förderschwerpunkten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Förderschwerpunkten
	 */
	public FoerderschwerpunkteStatistikService getFoerderschwerpunkteStatistikService() {
		return new FoerderschwerpunkteStatistikService(eigeneSchuleRepositoryFactory.getSchuleRepository(),
				eigeneSchuleServiceFactory.getSchuljahresabschnittService(),
				katalogRepositoryFactory.getFoerderschwerpunktRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Jahrgängen für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Jahrgängen
	 */
	public JahrgaengeStatistikService getJahrgaengeStatistikService() {
		return new JahrgaengeStatistikService(eigeneSchuleRepositoryFactory.getSchuleRepository(), eigeneSchuleServiceFactory.getSchuljahresabschnittService(),
				katalogRepositoryFactory.getJahrgangRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Klassendaten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Klassendaten
	 */
	public KlassenStatistikService getKlassenStatistikService() {
		return new KlassenStatistikService(eigeneSchuleRepositoryFactory.getSchuleRepository(),
				klassenRepositoryFactory.getKlassenRepository(),
				klassenRepositoryFactory.getKlassenleitungenRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Kursdaten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Kursdaten
	 */
	public KurseStatistikService getKurseStatistikService() {
		return new KurseStatistikService(eigeneSchuleRepositoryFactory.getSchuleRepository(),
				kurseRepositoryFactory.getKurseRepository(),
				kurseRepositoryFactory.getKurslehrerRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Lehrerdaten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Lehrerdaten
	 */
	public LehrerStatistikService getLehrerStatistikService() {
		return new LehrerStatistikService(eigeneSchuleRepositoryFactory.getSchuleRepository(), eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				lehrerRepositoryFactory.getLehrerRepository(),
				lehrerRepositoryFactory.getLehrerPersonalabschnittsdatenRepository(),
				lehrerServiceFactory.getLehrerLehramtService(),
				lehrerServiceFactory.getLehrerPersonalabschnittsdatenAnrechnungsstundenService());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Orten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Orten
	 */
	public OrteStatistikService getOrteStatistikService() {
		return new OrteStatistikService(eigeneSchuleRepositoryFactory.getSchuleRepository(), eigeneSchuleServiceFactory.getSchuljahresabschnittService(),
				katalogRepositoryFactory.getOrtRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Religionen für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Religionen
	 */
	public ReligionStatistikService getReligionStatistikService() {
		return new ReligionStatistikService(eigeneSchuleRepositoryFactory.getSchuleRepository(), eigeneSchuleServiceFactory.getSchuljahresabschnittService(),
				katalogRepositoryFactory.getReligionRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Fächern für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Fächern
	 */
	public FachStatistikService getFachStatistikService() {
		return new FachStatistikService(eigeneSchuleRepositoryFactory.getSchuleRepository(), eigeneSchuleServiceFactory.getSchuljahresabschnittService(),
				katalogRepositoryFactory.getFachRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Schülerdaten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Schülerdaten
	 */
	public SchuelerStatistikService getSchuelerStatistikService() {
		return new SchuelerStatistikService(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				schuelerRepositoryFactory.getSchuelerAbiturRepository(),
				schuelerRepositoryFactory.getSchuelerAbiturFachRepository(),
				katalogRepositoryFactory.getFachRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Schuldaten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Schuldaten
	 */
	public SchuleStatistikService getSchuleStatistikService() {
		return new SchuleStatistikService(eigeneSchuleRepositoryFactory.getSchuleRepository(), eigeneSchuleServiceFactory.getSchuljahresabschnittService());
	}


	/**
	 * Erstellt einen neuen Statistik-Service für den Zugriff auf die Statistikdaten.
	 *
	 * @return der Statistik-Service
	 */
	public StatistikService getStatistikService() {
		return new StatistikService(
				getSchuleStatistikService(),
				getLehrerStatistikService(),
				getKlassenStatistikService(),
				getKurseStatistikService(),
				getSchuelerStatistikService(),
				getJahrgaengeStatistikService(),
				getOrteStatistikService(),
				getFoerderschwerpunkteStatistikService(),
				getReligionStatistikService(),
				getFachStatistikService()
		);
	}


}
