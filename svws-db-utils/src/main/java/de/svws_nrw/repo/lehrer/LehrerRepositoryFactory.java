package de.svws_nrw.repo.lehrer;

import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerUnterrichtsfach;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtBefaehigung;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramtFachrichtung;
import de.svws_nrw.repo.RepositoryFactory;

/**
 * Eine Factory zum Erstellen von Repositories für Datenbank-Entitäten und ggf. auch komplexere Abfragen.
 */
public final class LehrerRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static LehrerRepositoryFactory getNewInstance() {
		return new LehrerRepositoryFactory();
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrer}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerRepository getLehrerRepository() {
		return getOrCreate(LehrerRepository.class, () -> new LehrerRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrerAbschnittsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerAbschnittsdatenRepository getLehrerAbschnittsdatenRepository() {
		return getOrCreate(LehrerAbschnittsdatenRepository.class, () -> new LehrerAbschnittsdatenRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrerUnterrichtsfach}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerUnterrichtsfachRepository getLehrerUnterrichtsfachRepository() {
		return getOrCreate(LehrerUnterrichtsfachRepository.class, () -> new LehrerUnterrichtsfachRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrerAnrechnungsstunde}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerAnrechnungRepository getLehrerAnrechnungRepository() {
		return getOrCreate(LehrerAnrechnungRepository.class, () -> new LehrerAnrechnungRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrerMehrleistung}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerMehrleistungRepository getLehrerMehrleistungRepository() {
		return getOrCreate(LehrerMehrleistungRepository.class, () -> new LehrerMehrleistungRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrerEntlastungsstunde}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerMinderleistungRepository getLehrerMinderleistungRepository() {
		return getOrCreate(LehrerMinderleistungRepository.class, () -> new LehrerMinderleistungRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrerPersonaldatenLehramt}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerPersonaldatenLehramtRepository getLehrerPersonaldatenLehramtRepository() {
		return getOrCreate(LehrerPersonaldatenLehramtRepository.class, () -> new LehrerPersonaldatenLehramtRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrerPersonaldatenLehramtFachrichtung}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerPersonaldatenLehramtFachrichtungRepository getLehrerPersonaldatenLehramtFachrichtungRepository() {
		return getOrCreate(LehrerPersonaldatenLehramtFachrichtungRepository.class, () -> new LehrerPersonaldatenLehramtFachrichtungRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrerPersonaldatenLehramtBefaehigung}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerPersonaldatenLehramtLehrbefaehigungenRepository getLehrerPersonaldatenLehramtLehrbefaehigungenRepository() {
		return getOrCreate(LehrerPersonaldatenLehramtLehrbefaehigungenRepository.class, () -> new LehrerPersonaldatenLehramtLehrbefaehigungenRepositoryImpl(conn));
	}

}
