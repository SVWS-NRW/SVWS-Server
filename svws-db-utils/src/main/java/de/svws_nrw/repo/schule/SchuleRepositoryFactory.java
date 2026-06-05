package de.svws_nrw.repo.schule;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungsKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.RepositoryFactory;
import de.svws_nrw.repo.schule.schulleitung.SchulleitungRepository;
import de.svws_nrw.repo.schule.schulleitung.SchulleitungRepositoryImpl;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public final class SchuleRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static SchuleRepositoryFactory getNewInstance() {
		return new SchuleRepositoryFactory();
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOEigeneSchule}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuleRepository getSchuleRepository() {
		return getOrCreate(SchuleRepository.class, () -> new SchuleRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchulleitung}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchulleitungRepository getSchulleitungRepository() {
		return getOrCreate(SchulleitungRepository.class, () -> new SchulleitungRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuljahresabschnitte}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuljahresabschnitteRepository getSchuljahresabschnitteRepository() {
		return getOrCreate(SchuljahresabschnitteRepository.class, () -> new SchuljahresabschnitteRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOAbteilungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public AbteilungenRepository getAbteilungenRepository() {
		return getOrCreate(AbteilungenRepository.class, () -> new AbteilungenRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOAbteilungsKlassen}.
	 *
	 * @return das Repository-Objekt
	 */
	public AbteilungenKlassenRepository getAbteilungenKlassenRepository() {
		return getOrCreate(AbteilungenKlassenRepository.class, () -> new AbteilungenKlassenRepositoryImpl(conn));
	}

}
