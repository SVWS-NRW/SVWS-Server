package de.svws_nrw.repo.klassen;

import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.repo.RepositoryFactory;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public final class KlassenRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static KlassenRepositoryFactory getNewInstance() {
		return new KlassenRepositoryFactory();
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOKlassen}.
	 *
	 * @return das Repository-Objekt
	 */
	public KlassenRepository getKlassenRepository() {
		return new KlassenRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOKlassenLeitung}.
	 *
	 * @return das Repository-Objekt
	 */
	public KlassenleitungenRepository getKlassenleitungenRepository() {
		return new KlassenleitungenRepositoryImpl(conn);
	}

}
