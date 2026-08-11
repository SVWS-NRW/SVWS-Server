package de.svws_nrw.repo.schueler;

import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerZP10;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;
import de.svws_nrw.db.dto.current.schild.schueler.abitur.DTOSchuelerAbiturFach;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerAnkreuzkompetenzen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerTeilleistungen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerZP10;
import de.svws_nrw.repo.RepositoryFactory;
import de.svws_nrw.repo.schueler.abitur.SchuelerAbiturFachRepository;
import de.svws_nrw.repo.schueler.abitur.SchuelerAbiturFachRepositoryImpl;
import de.svws_nrw.repo.schueler.abitur.SchuelerAbiturRepository;
import de.svws_nrw.repo.schueler.abitur.SchuelerAbiturRepositoryImpl;
import de.svws_nrw.repo.schueler.ankreuzkompetenz.SchuelerAnkreuzkompetenzRepository;
import de.svws_nrw.repo.schueler.ankreuzkompetenz.SchuelerAnkreuzkompetenzRepositoryImpl;
import de.svws_nrw.repo.schueler.ankreuzkompetenz.SchuelerAnkreuzkompetenzTimestampRepository;
import de.svws_nrw.repo.schueler.ankreuzkompetenz.SchuelerAnkreuzkompetenzTimestampRepositoryImpl;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepositoryImpl;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenTimestampsRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenTimestampsRepositoryImpl;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepositoryImpl;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepositoryImpl;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittTimestampRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittTimestampRepositoryImpl;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerBisherigeSchuleRepository;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerBisherigeSchuleRepositoryImpl;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerMerkmalRepository;
import de.svws_nrw.repo.schueler.schulbesuch.SchuelerMerkmalRepositoryImpl;
import de.svws_nrw.repo.schueler.sprachenfolge.SchuelerSprachenfolgeRepository;
import de.svws_nrw.repo.schueler.sprachenfolge.SchuelerSprachenfolgeRepositoryImpl;
import de.svws_nrw.repo.schueler.sprachpruefung.SchuelerSprachpruefungRepository;
import de.svws_nrw.repo.schueler.sprachpruefung.SchuelerSprachpruefungRepositoryImpl;
import de.svws_nrw.repo.schueler.teilleistung.SchuelerTeilleistungRepository;
import de.svws_nrw.repo.schueler.teilleistung.SchuelerTeilleistungRepositoryImpl;
import de.svws_nrw.repo.schueler.teilleistung.SchuelerTeilleistungTimestampRepository;
import de.svws_nrw.repo.schueler.teilleistung.SchuelerTeilleistungTimestampRepositoryImpl;
import de.svws_nrw.repo.schueler.zp10.SchuelerZP10Repository;
import de.svws_nrw.repo.schueler.zp10.SchuelerZP10RepositoryImpl;
import de.svws_nrw.repo.schueler.zp10.SchuelerZP10TimestampsRepository;
import de.svws_nrw.repo.schueler.zp10.SchuelerZP10TimestampsRepositoryImpl;

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
	public SchuelerAnkreuzkompetenzRepository getSchuelerAnkreuzkompetenzenRepository() {
		return getOrCreate(SchuelerAnkreuzkompetenzRepository.class, () -> new SchuelerAnkreuzkompetenzRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerAnkreuzkompetenzen}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerAnkreuzkompetenzTimestampRepository getSchuelerAnkreuzkompetenzenTimestampsRepository() {
		return getOrCreate(SchuelerAnkreuzkompetenzTimestampRepository.class, () -> new SchuelerAnkreuzkompetenzTimestampRepositoryImpl(conn));
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
	 * Erstellt ein neues Repository für {@link DTOSchuelerSprachenfolge}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerSprachenfolgeRepository getSchuelerSprachenfolgeRepository() {
		return getOrCreate(SchuelerSprachenfolgeRepository.class, () -> new SchuelerSprachenfolgeRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerSprachpruefungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerSprachpruefungRepository getSchuelerSprachpruefungenRepository() {
		return getOrCreate(SchuelerSprachpruefungRepository.class, () -> new SchuelerSprachpruefungRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerLernabschnittsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLernabschnittTimestampRepository getSchuelerLernabschnittTimestampsRepository() {
		return getOrCreate(SchuelerLernabschnittTimestampRepository.class, () -> new SchuelerLernabschnittTimestampRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerPSFachBemerkungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerLernabschnittBemerkungRepository getSchuelerLernabschnittBemerkungenRepository() {
		return getOrCreate(SchuelerLernabschnittBemerkungRepository.class, () -> new SchuelerLernabschnittBemerkungRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerTeilleistung}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerTeilleistungRepository getSchuelerTeilleistungenRepository() {
		return getOrCreate(SchuelerTeilleistungRepository.class, () -> new SchuelerTeilleistungRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOTimestampsSchuelerTeilleistungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerTeilleistungTimestampRepository getSchuelerTeilleistungenTimestampsRepository() {
		return getOrCreate(SchuelerTeilleistungTimestampRepository.class, () -> new SchuelerTeilleistungTimestampRepositoryImpl(conn));
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

	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerAbgaenge}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerBisherigeSchuleRepository getSchuelerBisherigeSchuleRepository() {
		return this.getOrCreate(SchuelerBisherigeSchuleRepository.class, () -> new SchuelerBisherigeSchuleRepositoryImpl(this.conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOSchuelerMerkmale}.
	 *
	 * @return das Repository-Objekt
	 */
	public SchuelerMerkmalRepository getSchuelerMerkmaleRepository() {
		return this.getOrCreate(SchuelerMerkmalRepository.class, () -> new SchuelerMerkmalRepositoryImpl(this.conn));
	}

}
