package de.svws_nrw.repo.schueler;

import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbiturFach;
import de.svws_nrw.repo.RepositoryFactory;

/**
 * Eine Factory zum Erstellen von Repositories für den Schüler-Bereich.
 */
public final class SchuelerRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static SchuelerRepositoryFactory getNewInstance() {
		return new SchuelerRepositoryFactory();
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchueler}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerRepository getSchuelerRepository() {
		return new SchuelerRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerLernabschnittsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLernabschnittRepository getSchuelerLernabschnittRepository() {
		return new SchuelerLernabschnittRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerAbitur}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerAbiturRepository getSchuelerAbiturRepository() {
		return new SchuelerAbiturRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerAbiturFach}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerAbiturFachRepository getSchuelerAbiturFachRepository() {
		return new SchuelerAbiturFachRepositoryImpl(conn);
	}

}
