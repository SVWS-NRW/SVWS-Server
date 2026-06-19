package de.svws_nrw.service.crypto;

import de.svws_nrw.repo.benutzer.BenutzerRepositoryFactory;
import de.svws_nrw.repo.schueler.SchuelerRepositoryFactory;

/**
 * Eine Factory zum Erstellen der Services für kryptographische Verfahren
 */
public final class CryptoServiceFactory {

	/** die Factory für die Benutzer-Repositories */
	private final BenutzerRepositoryFactory benutzerRepositoryFactory;

	/** die Factory für die Schueler-Repositories */
	private final SchuelerRepositoryFactory schuelerRepositoryFactory;


	/**
	 * Erstellt eine neue Service-Factory
	 *
	 * @param benutzerRepositoryFactory   die Factory für Benutzer-Repositories
	 * @param schuelerRepositoryFactory   die Factory für Schüler-Repositories
	 */
	private CryptoServiceFactory(final BenutzerRepositoryFactory benutzerRepositoryFactory,
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
	public static CryptoServiceFactory getNewInstance(final BenutzerRepositoryFactory benutzerRepositoryFactory,
			final SchuelerRepositoryFactory schuelerRepositoryFactory) {
		return new CryptoServiceFactory(benutzerRepositoryFactory, schuelerRepositoryFactory);
	}


	/**
	 * Erstellt einen neuen Service für die Credentials von Schülern
	 *
	 * @return der Service
	 */
	public SchuelerCredentialsService getSchuelerCredentialsService() {
		return new SchuelerCredentialsService(benutzerRepositoryFactory.getCredentialsRepository(),
				schuelerRepositoryFactory.getSchuelerRepository());
	}

}
