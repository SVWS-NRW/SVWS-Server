package de.svws_nrw.repo.schueler;

import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerZP10;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbiturFach;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerAnkreuzkompetenzen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerTeilleistungen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerZP10;
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
		return getOrCreate(SchuelerRepository.class, () -> new SchuelerRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerAnkreuzfloskeln}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerAnkreuzkompetenzenRepository getSchuelerAnkreuzkompetenzenRepository() {
		return getOrCreate(SchuelerAnkreuzkompetenzenRepository.class, () -> new SchuelerAnkreuzkompetenzenRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerAnkreuzkompetenzen}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerAnkreuzkompetenzenTimestampsRepository getSchuelerAnkreuzkompetenzenTimestampsRepository() {
		return getOrCreate(SchuelerAnkreuzkompetenzenTimestampsRepository.class, () -> new SchuelerAnkreuzkompetenzenTimestampsRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerLeistungsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLeistungsdatenRepository getSchuelerLeistungsdatenRepository() {
		return getOrCreate(SchuelerLeistungsdatenRepository.class, () -> new SchuelerLeistungsdatenRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerLeistungsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLeistungsdatenTimestampsRepository getSchuelerLeistungsdatenTimestampsRepository() {
		return getOrCreate(SchuelerLeistungsdatenTimestampsRepository.class, () -> new SchuelerLeistungsdatenTimestampsRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerLernabschnittsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLernabschnittRepository getSchuelerLernabschnittRepository() {
		return getOrCreate(SchuelerLernabschnittRepository.class, () -> new SchuelerLernabschnittRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerLernabschnittsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLernabschnittTimestampsRepository getSchuelerLernabschnittTimestampsRepository() {
		return getOrCreate(SchuelerLernabschnittTimestampsRepository.class, () -> new SchuelerLernabschnittTimestampsRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerPSFachBemerkungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLernabschnittBemerkungenRepository getSchuelerLernabschnittBemerkungenRepository() {
		return getOrCreate(SchuelerLernabschnittBemerkungenRepository.class, () -> new SchuelerLernabschnittBemerkungenRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerTeilleistung}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerTeilleistungenRepository getSchuelerTeilleistungenRepository() {
		return getOrCreate(SchuelerTeilleistungenRepository.class, () -> new SchuelerTeilleistungenRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerTeilleistungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerTeilleistungenTimestampsRepository getSchuelerTeilleistungenTimestampsRepository() {
		return getOrCreate(SchuelerTeilleistungenTimestampsRepository.class, () -> new SchuelerTeilleistungenTimestampsRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerAbitur}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerAbiturRepository getSchuelerAbiturRepository() {
		return getOrCreate(SchuelerAbiturRepository.class, () -> new SchuelerAbiturRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerAbiturFach}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerAbiturFachRepository getSchuelerAbiturFachRepository() {
		return getOrCreate(SchuelerAbiturFachRepository.class, () -> new SchuelerAbiturFachRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerZP10}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerZP10Repository getSchuelerZP10Repository() {
		return getOrCreate(SchuelerZP10Repository.class, () -> new SchuelerZP10RepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerZP10}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerZP10TimestampsRepository getSchuelerZP10TimestampsRepository() {
		return getOrCreate(SchuelerZP10TimestampsRepository.class, () -> new SchuelerZP10TimestampsRepositoryImpl(conn));
	}

}
