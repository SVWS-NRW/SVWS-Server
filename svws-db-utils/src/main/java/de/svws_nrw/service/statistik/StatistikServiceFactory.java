package de.svws_nrw.service.statistik;

import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.kataloge.KatalogeRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.SchuleRepositoryFactory;
import de.svws_nrw.service.lehrer.LehrerServiceFactory;
import de.svws_nrw.service.schule.SchuleServiceFactory;

/**
 * Eine Factory zum Erstellen der Statistik-spezifischen Services
 */
public final class StatistikServiceFactory {

	/** die Factory für die Benutzer-Repositories */
	private final BenutzerRepositoryFactory benutzerRepositoryFactory;

	/** die Factory für die Katalog-Repositories */
	private final KatalogeRepositoryFactory katalogeRepositoryFactory;

	/** die Factory für die Klassen-Repositories */
	private final KlassenRepositoryFactory klassenRepositoryFactory;

	/** die Factory für die Kurs-Repositories */
	private final KurseRepositoryFactory kurseRepositoryFactory;

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
	 * @param benutzerRepositoryFactory    die Factory für die Benutzer-Repositories
	 * @param katalogeRepositoryFactory    die Factory für Kataloge-Repositories
	 * @param klassenRepositoryFactory     die Factory für Klassen-Repositories
	 * @param kurseRepositoryFactory       die Factory für Kurse-Repositories
	 * @param lehrerRepositoryFactory      die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory    die Factory für Schüler-Repositories
	 * @param schuleRepositoryFactory      die Factory für Schule-Repositories
	 * @param lehrerServiceFactory         die Factory für Lehrer-Services
	 * @param schuleServiceFactory         die Factory für Schul-Services
	 */
	private StatistikServiceFactory(final BenutzerRepositoryFactory benutzerRepositoryFactory, final KatalogeRepositoryFactory katalogeRepositoryFactory,
			final KlassenRepositoryFactory klassenRepositoryFactory,
			final KurseRepositoryFactory kurseRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final SchuleRepositoryFactory schuleRepositoryFactory,
			final LehrerServiceFactory lehrerServiceFactory,
			final SchuleServiceFactory schuleServiceFactory) {
		this.benutzerRepositoryFactory = benutzerRepositoryFactory;
		this.katalogeRepositoryFactory = katalogeRepositoryFactory;
		this.klassenRepositoryFactory = klassenRepositoryFactory;
		this.kurseRepositoryFactory = kurseRepositoryFactory;
		this.lehrerRepositoryFactory = lehrerRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.schuleRepositoryFactory = schuleRepositoryFactory;
		this.lehrerServiceFactory = lehrerServiceFactory;
		this.schuleServiceFactory = schuleServiceFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param benutzerRepositoryFactory    die Factory für die Benutzer-Repositories
	 * @param katalogeRepositoryFactory    die Factory für Kataloge-Repositories
	 * @param klassenRepositoryFactory     die Factory für Klassen-Repositories
	 * @param kurseRepositoryFactory       die Factory für Kurse-Repositories
	 * @param lehrerRepositoryFactory      die Factory für Lehrer-Repositories
	 * @param schuelerRepositoryFactory    die Factory für Schüler-Repositories
	 * @param schuleRepositoryFactory      die Factory für Schule-Repositories
	 * @param lehrerServiceFactory         die Factory für Lehrer-Services
	 * @param schuleServiceFactory         die Factory für Schul-Services
	 *
	 * @return die neue Factory-Instanz
	 */
	public static StatistikServiceFactory getNewInstance(final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final KatalogeRepositoryFactory katalogeRepositoryFactory,
			final KlassenRepositoryFactory klassenRepositoryFactory,
			final KurseRepositoryFactory kurseRepositoryFactory,
			final LehrerRepositoryFactory lehrerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final SchuleRepositoryFactory schuleRepositoryFactory,
			final LehrerServiceFactory lehrerServiceFactory,
			final SchuleServiceFactory schuleServiceFactory) {
		return new StatistikServiceFactory(benutzerRepositoryFactory, katalogeRepositoryFactory, klassenRepositoryFactory, kurseRepositoryFactory,
				lehrerRepositoryFactory,
				schuelerRepositoryFactory, schuleRepositoryFactory, lehrerServiceFactory, schuleServiceFactory);
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Förderschwerpunkten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Förderschwerpunkten
	 */
	public FoerderschwerpunkteStatistikService getFoerderschwerpunkteStatistikService() {
		return new FoerderschwerpunkteStatistikService(schuleRepositoryFactory.getSchuleRepository(),
				schuleServiceFactory.getSchuljahresabschnittService(),
				katalogeRepositoryFactory.getFoerderschwerpunkteRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Jahrgängen für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Jahrgängen
	 */
	public JahrgaengeStatistikService getJahrgaengeStatistikService() {
		return new JahrgaengeStatistikService(schuleRepositoryFactory.getSchuleRepository(), schuleServiceFactory.getSchuljahresabschnittService(),
				katalogeRepositoryFactory.getJahrgaengeRepository());
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
	 * Erstellt einen neuen Service für den Zugriff auf die Kursdaten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Kursdaten
	 */
	public KurseStatistikService getKurseStatistikService() {
		return new KurseStatistikService(schuleRepositoryFactory.getSchuleRepository(),
				kurseRepositoryFactory.getKurseRepository(),
				kurseRepositoryFactory.getKurslehrerRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Lehrerdaten für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Lehrerdaten
	 */
	public LehrerStatistikService getLehrerStatistikService() {
		return new LehrerStatistikService(schuleRepositoryFactory.getSchuleRepository(), schuleRepositoryFactory.getSchuljahresabschnitteRepository(),
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
		return new OrteStatistikService(schuleRepositoryFactory.getSchuleRepository(), schuleServiceFactory.getSchuljahresabschnittService(),
				katalogeRepositoryFactory.getOrteRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Religionen für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Religionen
	 */
	public ReligionStatistikService getReligionStatistikService() {
		return new ReligionStatistikService(schuleRepositoryFactory.getSchuleRepository(), schuleServiceFactory.getSchuljahresabschnittService(),
				katalogeRepositoryFactory.getReligionRepository());
	}


	/**
	 * Erstellt einen neuen Service für den Zugriff auf die Daten zu den Fächern für die Statistik
	 *
	 * @return der Service für den Zugriff auf die Daten zu den Fächern
	 */
	public FachStatistikService getFachStatistikService() {
		return new FachStatistikService(schuleRepositoryFactory.getSchuleRepository(), schuleServiceFactory.getSchuljahresabschnittService(),
				katalogeRepositoryFactory.getFachRepository());
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
