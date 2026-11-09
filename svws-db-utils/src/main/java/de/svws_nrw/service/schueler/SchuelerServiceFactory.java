package de.svws_nrw.service.schueler;

import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.mapper.gost.GostJahrgangMapper;
import de.svws_nrw.mapper.schueler.SchuelerListeEintragMapper;
import de.svws_nrw.mapper.schueler.einwilligung.SchuelerEinwilligungMapper;
import de.svws_nrw.mapper.schueler.foto.SchuelerFotoMapper;
import de.svws_nrw.mapper.schueler.leistungsdaten.SchuelerLeistungsdatenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittBemerkungenMapper;
import de.svws_nrw.mapper.schueler.lernabschnittsdaten.SchuelerLernabschnittsdatenMapper;
import de.svws_nrw.mapper.schueler.lernplattform.SchuelerLernplattformMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerBisherigeSchuleMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerMerkmalMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchulbesuchMapper;
import de.svws_nrw.mapper.schueler.stammdaten.SchuelerStammdatenMapper;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.gost.GostRepositoryFactory;
import de.svws_nrw.repo.klassen.KlassenRepositoryFactory;
import de.svws_nrw.repo.kurse.KurseRepositoryFactory;
import de.svws_nrw.repo.lehrer.LehrerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.benutzer.BenutzerServiceFactory;
import de.svws_nrw.service.gost.GostJahrgangslisteService;
import de.svws_nrw.service.klassen.KlassenServiceFactory;
import de.svws_nrw.service.kurse.KursServiceFactory;
import de.svws_nrw.service.schueler.create.SchuelerCreateService;
import de.svws_nrw.service.schueler.einwilligung.SchuelerEinwilligungService;
import de.svws_nrw.service.schueler.foto.SchuelerFotoService;
import de.svws_nrw.service.schueler.liste.SchuelerListeEintragService;
import de.svws_nrw.service.schueler.liste.SchuelerListeService;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittService;
import de.svws_nrw.service.schueler.lernabschnitt.SchuelerLernabschnittsdatenRepos;
import de.svws_nrw.service.schueler.lernplattform.SchuelerLernplattformService;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchuleService;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalService;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchService;
import de.svws_nrw.service.schueler.sprachdaten.SchuelerSprachdatenService;
import de.svws_nrw.service.schueler.sprachenfolge.SchuelerSprachenfolgeService;
import de.svws_nrw.service.schueler.sprachpruefung.SchuelerSprachpruefungService;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenRepositories;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenService;
import de.svws_nrw.service.schule.EigeneSchuleServiceFactory;
import de.svws_nrw.service.schule.katalog.KatalogServiceFactory;

/**
 * Eine Factory zum Erstellen der Services für Schüler
 */
public final class SchuelerServiceFactory {

	/** die Factory für die Benutzer-Repositories */
	private final BenutzerRepositoryFactory benutzerRepositoryFactory;

	/** die Factory für die Schüler-Repositories */
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;

	/** die Factory für die Katalog-Repositories */
	private final KatalogRepositoryFactory katalogRepositoryFactory;

	/** die Factory für die Katalog-Services */
	private final KatalogServiceFactory katalogServiceFactory;

	/** die Factory für die Repositories der eigenen Schule */
	private final EigeneSchuleRepositoryFactory eigeneSchuleRepositoryFactory;

	/** die Factory für die Services der eigenen Schule */
	private final EigeneSchuleServiceFactory eigeneSchuleServiceFactory;

	private final KurseRepositoryFactory kurseRepositoryFactory;

	private final GostRepositoryFactory gostRepositoryFactory;

	private final KlassenServiceFactory klassenServiceFactory;

	private final KursServiceFactory kursServiceFactory;


	private SchuelerServiceFactory(
			final SchuelerFactories factories,
			final KlassenServiceFactory klassenServiceFactory,
			final KursServiceFactory kursServiceFactory) {
		this.benutzerRepositoryFactory = factories.benutzerRepositoryFactory();
		this.schuelerRepositoryFactory = factories.schuelerRepositoryFactory();
		this.katalogRepositoryFactory = factories.katalogRepositoryFactory();
		this.katalogServiceFactory = factories.katalogServiceFactory();
		this.eigeneSchuleRepositoryFactory = factories.eigeneSchuleRepositoryFactory();
		this.eigeneSchuleServiceFactory = factories.eigeneSchuleServiceFactory();
		this.kurseRepositoryFactory = factories.kurseRepositoryFactory();
		this.gostRepositoryFactory = factories.gostRepositoryFactory();
		this.klassenServiceFactory = klassenServiceFactory;
		this.kursServiceFactory = kursServiceFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param factories              die benötigten Factories
	 * @param klassenServiceFactory  die Factory für Klassen-Services
	 * @param kursServiceFactory     die Factory für Kurs-Services
	 *
	 * @return die Factory
	 */
	public static SchuelerServiceFactory getNewInstance(
			final SchuelerFactories factories,
			final KlassenServiceFactory klassenServiceFactory,
			final KursServiceFactory kursServiceFactory) {
		return new SchuelerServiceFactory(factories, klassenServiceFactory, kursServiceFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory. Die Factories für Klassen- und Kurs-Services werden dabei
	 * aus den übergebenen Factories erstellt.
	 *
	 * @param factories   die benötigten Factories
	 *
	 * @return die Factory
	 */
	public static SchuelerServiceFactory getNewInstance(final SchuelerFactories factories) {
		final var klassenServiceFactory = KlassenServiceFactory.getNewInstance(KlassenRepositoryFactory.getNewInstance(),
				factories.schuelerRepositoryFactory(), factories.eigeneSchuleServiceFactory());
		final var kursServiceFactory = KursServiceFactory.getNewInstance(factories.kurseRepositoryFactory(), factories.schuelerRepositoryFactory(),
				factories.katalogRepositoryFactory());
		return new SchuelerServiceFactory(factories, klassenServiceFactory, kursServiceFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @return die Factory
	 */
	public static SchuelerServiceFactory getNewInstance() {
		final var eigeneSchuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		final var eigeneSchuleServiceFactory = EigeneSchuleServiceFactory.getNewInstance(eigeneSchuleRepositoryFactory);
		final var katalogRepositoryFactory = KatalogRepositoryFactory.getNewInstance();
		final var factories = new SchuelerFactories(
				BenutzerRepositoryFactory.getNewInstance(),
				SchuelerRepositoryFactory.getNewInstance(),
				katalogRepositoryFactory,
				KatalogServiceFactory.getNewInstance(katalogRepositoryFactory, eigeneSchuleServiceFactory),
				eigeneSchuleRepositoryFactory,
				eigeneSchuleServiceFactory,
				KurseRepositoryFactory.getNewInstance(),
				GostRepositoryFactory.getNewInstance());
		return getNewInstance(factories);
	}


	/**
	 * Erstellt einen neuen Service für die SchuelerSprachenfolge
	 *
	 * @return der Service
	 */
	public SchuelerSprachenfolgeService getSchuelerSprachenfolgeService() {
		return new SchuelerSprachenfolgeService(schuelerRepositoryFactory.getSchuelerSprachenfolgeRepository());
	}


	/**
	 * Erstellt einen neuen Service für die SchuelerSprachpruefungen
	 *
	 * @return der Service
	 */
	public SchuelerSprachpruefungService getSchuelerSprachpruefungenService() {
		return new SchuelerSprachpruefungService(schuelerRepositoryFactory.getSchuelerSprachpruefungenRepository());
	}


	/**
	 * Erstellt einen neuen Service für die Schueler-Sprachdaten
	 *
	 * @return der Service
	 */
	public SchuelerSprachdatenService getSchuelerSprachdatenService() {
		return new SchuelerSprachdatenService(benutzerRepositoryFactory.getBenutzerAllgemeinRepository(),
				this.getSchuelerSprachenfolgeService(),
				this.getSchuelerSprachpruefungenService());
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerBisherigeSchuleService}.
	 * <p>
	 * Schulen- und Entlassgrundkataloge werden über die aktuelle Datenbankverbindung geladen.
	 * </p>
	 *
	 * @return ein neuer {@code BisherigeSchulenService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerBisherigeSchuleService getSchuelerBisherigeSchuleService() {
		final var dataSchulen = new DataSchulen(DbConnectionProvider.getConnection());
		final var dataEntlassgruende = new DataKatalogEntlassgruende(DbConnectionProvider.getConnection());
		return new SchuelerBisherigeSchuleService(
				schuelerRepositoryFactory.getSchuelerBisherigeSchuleRepository(),
				SchuelerBisherigeSchuleMapper.INSTANCE,
				dataSchulen,
				dataEntlassgruende);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerMerkmalService}.
	 * <p>
	 * Schulen- und Entlassgrundkataloge werden über die aktuelle Datenbankverbindung geladen.
	 * </p>
	 *
	 * @return ein neuer {@code SchuelerMerkmalService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerMerkmalService getSchuelerMerkmalService() {
		return new SchuelerMerkmalService(
				schuelerRepositoryFactory.getSchuelerMerkmaleRepository(),
				katalogServiceFactory.getMerkmalService(),
				SchuelerMerkmalMapper.INSTANCE);
	}

	/**
	 * Erzeugt eine neue Instanz
	 *
	 * @return SchulbesuchService
	 */
	public SchuelerSchulbesuchService getSchulbesuchService() {
		return new SchuelerSchulbesuchService(
				schuelerRepositoryFactory.getSchuelerRepository(),
				this.getSchuelerMerkmalService(),
				this.getSchuelerBisherigeSchuleService(),
				new DataKatalogEntlassgruende(DbConnectionProvider.getConnection()),
				new DataSchulen(DbConnectionProvider.getConnection()),
				SchulbesuchMapper.INSTANCE
		);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerFotoService}.
	 *
	 * @return ein neuer {@code SchuelerFotoService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerFotoService getSchuelerFotoService() {
		return new SchuelerFotoService(
				schuelerRepositoryFactory.getSchuelerFotoRepository(),
				SchuelerFotoMapper.INSTANCE
		);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerLernabschnittService}.
	 *
	 * @return ein neuer {@code SchuelerLernabschnittService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerLernabschnittService getSchuelerLernabschnittService() {
		final var repos = new SchuelerLernabschnittsdatenRepos(
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittBemerkungenRepository(),
				eigeneSchuleRepositoryFactory.getSchuljahresabschnitteRepository(),
				schuelerRepositoryFactory.getSchuelerLeistungsdatenRepository(),
				LehrerRepositoryFactory.getNewInstance().getLehrerRepository(),
				KlassenRepositoryFactory.getNewInstance().getKlassenRepository(),
				katalogRepositoryFactory.getFoerderschwerpunktRepository(),
				katalogRepositoryFactory.getFachklasseRepository(),
				katalogRepositoryFactory.getJahrgangRepository(),
				katalogRepositoryFactory.getSchuelerschwerpunktRepository()
		);
		return new SchuelerLernabschnittService(
				repos,
				SchuelerLernabschnittsdatenMapper.INSTANCE,
				SchuelerLeistungsdatenMapper.INSTANCE,
				SchuelerLernabschnittBemerkungenMapper.INSTANCE,
				eigeneSchuleServiceFactory.getSchuleService(),
				BenutzerServiceFactory.getNewInstance(benutzerRepositoryFactory).getBenutzerKompetenzService()
		);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerStammdatenService}.
	 *
	 * @return ein neuer {@code SchuelerStammdatenService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerStammdatenService getSchuelerStammdatenService() {
		final var repositories = new SchuelerStammdatenRepositories(
				schuelerRepositoryFactory.getSchuelerRepository(),
				katalogRepositoryFactory.getReligionRepository(),
				katalogRepositoryFactory.getOrtRepository(),
				katalogRepositoryFactory.getOrtsteilRepository(),
				katalogRepositoryFactory.getFahrschuelerartRepository(),
				katalogRepositoryFactory.getHaltestelleRepository()
		);
		return new SchuelerStammdatenService(
				repositories,
				SchuelerStammdatenMapper.INSTANCE,
				this.getSchuelerFotoService()
		);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerLernplattformService}.
	 *
	 * @return ein neuer {@code SchuelerLernplattformService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerLernplattformService getSchuelerLernplattformService() {
		return new SchuelerLernplattformService(
				schuelerRepositoryFactory.getSchuelerLernplattformRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				katalogRepositoryFactory.getLernplattformRepository(),
				katalogRepositoryFactory.getLernplattformCredentialRepository(),
				SchuelerLernplattformMapper.INSTANCE
		);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerEinwilligungService}.
	 *
	 * @return ein neuer {@code SchuelerEinwilligungService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerEinwilligungService getSchuelerEinwilligungService() {
		return new SchuelerEinwilligungService(
				schuelerRepositoryFactory.getSchuelerEinwilligungRepository(),
				schuelerRepositoryFactory.getSchuelerRepository(),
				katalogRepositoryFactory.getEinwilligungsartRepository(),
				SchuelerEinwilligungMapper.INSTANCE
		);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerCreateService}.
	 *
	 * @return ein neuer {@code SchuelerCreateService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerCreateService getSchuelerCreateService() {
		return new SchuelerCreateService(
				this.getSchuelerStammdatenService(),
				this.getSchuelerLernabschnittService(),
				this.getSchulbesuchService(),
				this.getSchuelerLernplattformService(),
				this.getSchuelerEinwilligungService()
		);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerListeEintragService}.
	 *
	 * @return ein neuer {@code SchuelerListeEintragService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerListeEintragService getSchuelerListeEintragService() {
		return new SchuelerListeEintragService(
				schuelerRepositoryFactory.getSchuelerRepository(),
				schuelerRepositoryFactory.getSchuelerLernabschnittRepository(),
				kurseRepositoryFactory.getKursSchuelerRepository(),
				katalogRepositoryFactory.getJahrgangRepository(),
				gostRepositoryFactory.getGostSchuelerWiederholungRepository(),
				eigeneSchuleServiceFactory.getSchuleService(),
				eigeneSchuleServiceFactory.getSchuljahresabschnittService(),
				SchuelerListeEintragMapper.INSTANCE
		);
	}

	/**
	 * Erstellt eine neue Instanz des {@link SchuelerListeService}.
	 *
	 * @return ein neuer {@code SchuelerListeService} mit allen erforderlichen Abhängigkeiten
	 */
	public SchuelerListeService getSchuelerListeService() {
		return new SchuelerListeService(
				this.getSchuelerListeEintragService(),
				klassenServiceFactory.getKlassenService(),
				kursServiceFactory.getKursService(),
				katalogServiceFactory.getJahrgangService(),
				this.getGostJahrgangslisteService(),
				eigeneSchuleServiceFactory.getSchuljahresabschnittService(),
				eigeneSchuleServiceFactory.getSchuleService()
		);
	}

	private GostJahrgangslisteService getGostJahrgangslisteService() {
		return new GostJahrgangslisteService(
				eigeneSchuleServiceFactory.getSchuleService(),
				eigeneSchuleServiceFactory.getSchuljahresabschnittService(),
				katalogRepositoryFactory.getJahrgangRepository(),
				gostRepositoryFactory.getGostJahrgangsdatenRepository(),
				gostRepositoryFactory.getGostJahrgangsinformationenRepository(),
				GostJahrgangMapper.INSTANCE
		);
	}

}
