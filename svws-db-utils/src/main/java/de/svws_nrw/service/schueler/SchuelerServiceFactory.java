package de.svws_nrw.service.schueler;

import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.mapper.schueler.foto.SchuelerFotoMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerBisherigeSchuleMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerMerkmalMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchulbesuchMapper;
import de.svws_nrw.mapper.schueler.stammdaten.SchuelerStammdatenMapper;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.EigeneSchuleRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schueler.foto.SchuelerFotoService;
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

	private SchuelerServiceFactory(
			final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory,
			final KatalogServiceFactory katalogServiceFactory) {
		this.benutzerRepositoryFactory = benutzerRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.katalogRepositoryFactory = katalogRepositoryFactory;
		this.katalogServiceFactory = katalogServiceFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param benutzerRepositoryFactory   die Factory für Benutzer-Repositories
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 * @param katalogRepositoryFactory  die Factory für Katalog-Repositories
	 * @param katalogServiceFactory   die Factory für Katalog-Repositories
	 *
	 * @return die Factory
	 */
	public static SchuelerServiceFactory getNewInstance(
			final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory,
			final KatalogServiceFactory katalogServiceFactory) {
		return new SchuelerServiceFactory(benutzerRepositoryFactory, schuelerRepositoryFactory, katalogRepositoryFactory, katalogServiceFactory);
	}

	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @return die Factory
	 */
	public static SchuelerServiceFactory getNewInstance() {
		final var benutzerRepositoryFactory = BenutzerRepositoryFactory.getNewInstance();
		final var schuelerRepositoryFactory = SchuelerRepositoryFactory.getNewInstance();
		final var katalogRepositoryFactory = KatalogRepositoryFactory.getNewInstance();
		final var eigeneSchuleRepositoryFactory = EigeneSchuleRepositoryFactory.getNewInstance();
		final var eigeneSchuleServiceFactory = EigeneSchuleServiceFactory.getNewInstance(eigeneSchuleRepositoryFactory);
		final var katalogServiceFactory = KatalogServiceFactory.getNewInstance(katalogRepositoryFactory, eigeneSchuleServiceFactory);
		return new SchuelerServiceFactory(benutzerRepositoryFactory, schuelerRepositoryFactory, katalogRepositoryFactory, katalogServiceFactory);
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

}
