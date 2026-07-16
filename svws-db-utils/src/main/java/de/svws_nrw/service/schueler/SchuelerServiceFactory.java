package de.svws_nrw.service.schueler;

import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;

/**
 * Eine Factory zum Erstellen der Services für Schüler
 */
public final class SchuelerServiceFactory {

	/** die Factory für die Benutzer-Repositories */
	private final BenutzerRepositoryFactory benutzerRepositoryFactory;

	/** die Factory für die Schüler-Repositories */
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;

	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param benutzerRepositoryFactory   die Factory für Benutzer-Repositories
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 */
	private SchuelerServiceFactory(final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory) {
		this.benutzerRepositoryFactory = benutzerRepositoryFactory;
		this.schuelerRepositoryFactory = schuelerRepositoryFactory;
	}


	/**
	 * Erzeugt eine neue Instanz der Service-Factory
	 *
	 * @param benutzerRepositoryFactory   die Factory für Benutzer-Repositories
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 *
	 * @return die Factory
	 */
	public static SchuelerServiceFactory getNewInstance(final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory) {
		return new SchuelerServiceFactory(benutzerRepositoryFactory, schuelerRepositoryFactory);
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
	public SchuelerSprachpruefungenService getSchuelerSprachpruefungenService() {
		return new SchuelerSprachpruefungenService(schuelerRepositoryFactory.getSchuelerSprachpruefungenRepository());
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

}
