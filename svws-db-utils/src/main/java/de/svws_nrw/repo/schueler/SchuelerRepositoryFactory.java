package de.svws_nrw.repo.schueler;

import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbiturFach;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerAnkreuzkompetenzen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerTeilleistungen;
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
	 * Erstellt ein neues Repository für {@link DTOSchuelerAnkreuzfloskeln}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerAnkreuzkompetenzenRepository getSchuelerAnkreuzkompetenzenRepository() {
		return new SchuelerAnkreuzkompetenzenRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerAnkreuzkompetenzen}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerAnkreuzkompetenzenTimestampsRepository getSchuelerAnkreuzkompetenzenTimestampsRepository() {
		return new SchuelerAnkreuzkompetenzenTimestampsRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerLeistungsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLeistungsdatenRepository getSchuelerLeistungsdatenRepository() {
		return new SchuelerLeistungsdatenRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerLeistungsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLeistungsdatenTimestampsRepository getSchuelerLeistungsdatenTimestampsRepository() {
		return new SchuelerLeistungsdatenTimestampsRepositoryImpl(conn);
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
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerLernabschnittsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLernabschnittTimestampsRepository getSchuelerLernabschnittTimestampsRepository() {
		return new SchuelerLernabschnittTimestampsRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerPSFachBemerkungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLernabschnittBemerkungenRepository getSchuelerLernabschnittBemerkungenRepository() {
		return new SchuelerLernabschnittBemerkungenRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerTeilleistung}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerTeilleistungenRepository getSchuelerTeilleistungenRepository() {
		return new SchuelerTeilleistungenRepositoryImpl(conn);
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerTeilleistungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerTeilleistungenTimestampsRepository getSchuelerTeilleistungenTimestampsRepository() {
		return new SchuelerTeilleistungenTimestampsRepositoryImpl(conn);
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
