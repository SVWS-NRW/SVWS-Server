package de.svws_nrw.repo.enm;

import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulCredentials;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulKonfigurationClient;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulKonfigurationServer;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulVerbindungen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsNotenmodulCredentials;
import de.svws_nrw.repo.RepositoryFactory;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public final class NotenmodulRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static NotenmodulRepositoryFactory getNewInstance() {
		return new NotenmodulRepositoryFactory();
	}


	/**
	 * Erstellt ein neues Repository für {@link DTONotenmodulCredentials}.
	 *
	 * @return das Repository-Objekt
	 */
	public NotenmodulCredentialsRepository getNotenmodulCredentialsRepository() {
		return getOrCreate(NotenmodulCredentialsRepository.class, () -> new NotenmodulCredentialsRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsNotenmodulCredentials}.
	 *
	 * @return das Repository-Objekt
	 */
	public NotenmodulCredentialsTimestampsRepository getNotenmodulCredentialsTimestampsRepository() {
		return getOrCreate(NotenmodulCredentialsTimestampsRepository.class, () -> new NotenmodulCredentialsTimestampsRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTONotenmodulKonfigurationClient}.
	 *
	 * @return das Repository-Objekt
	 */
	public NotenmodulKonfigurationClientRepository getNotenmodulKonfigurationClientRepository() {
		return getOrCreate(NotenmodulKonfigurationClientRepository.class, () -> new NotenmodulKonfigurationClientRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTONotenmodulKonfigurationServer}.
	 *
	 * @return das Repository-Objekt
	 */
	public NotenmodulKonfigurationServerRepository getNotenmodulKonfigurationServerRepository() {
		return getOrCreate(NotenmodulKonfigurationServerRepository.class, () -> new NotenmodulKonfigurationServerRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTONotenmodulVerbindungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public NotenmodulVerbindungenRepository getNotenmodulVerbindungenRepository() {
		return getOrCreate(NotenmodulVerbindungenRepository.class, () -> new NotenmodulVerbindungenRepositoryImpl(conn));
	}

}
