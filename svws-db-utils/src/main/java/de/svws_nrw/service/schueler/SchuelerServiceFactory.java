package de.svws_nrw.service.schueler;

import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerBisherigeSchuleMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchuelerMerkmalMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchulbesuchMapper;
import de.svws_nrw.repo.DbConnectionProvider;
import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;
import de.svws_nrw.repo.schule.kataloge.KatalogRepositoryFactory;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerBisherigeSchuleService;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerMerkmalService;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchService;
import de.svws_nrw.service.schueler.sprachdaten.SchuelerSprachdatenService;
import de.svws_nrw.service.schueler.sprachenfolge.SchuelerSprachenfolgeService;
import de.svws_nrw.service.schueler.sprachpruefung.SchuelerSprachpruefungService;

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

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param benutzerRepositoryFactory   die Factory für Benutzer-Repositories
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 * @param katalogRepositoryFactory   die Factory für Katalog-Repositories
	 */
	private SchuelerServiceFactory(
			final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory) {
		this.benutzerRepositoryFactory = benutzerRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
		this.katalogRepositoryFactory = katalogRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param benutzerRepositoryFactory   die Factory für Benutzer-Repositories
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 * @param katalogRepositoryFactory   die Factory für Katalog-Repositories
	 *
	 * @return die Factory
	 */
	public static SchuelerServiceFactory getNewInstance(
			final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory,
			final KatalogRepositoryFactory katalogRepositoryFactory) {
		return new SchuelerServiceFactory(benutzerRepositoryFactory, schuelerRepositoryFactory, katalogRepositoryFactory);
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
				katalogRepositoryFactory.getMerkmalRepository(),
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

}
