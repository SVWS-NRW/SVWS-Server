package de.svws_nrw.repo.lehrer;

import de.svws_nrw.db.dto.current.schild.katalog.DTOLeitungsfunktion;
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
import de.svws_nrw.repo.lehrer.anrechnung.LehrerAnrechnungRepository;
import de.svws_nrw.repo.lehrer.anrechnung.LehrerAnrechnungRepositoryImpl;
import de.svws_nrw.repo.lehrer.funktion.LehrerFunktionRepository;
import de.svws_nrw.repo.lehrer.funktion.LehrerFunktionRepositoryImpl;
import de.svws_nrw.repo.lehrer.lehramt.LehrerLehramtRepository;
import de.svws_nrw.repo.lehrer.lehramt.LehrerLehramtRepositoryImpl;
import de.svws_nrw.repo.lehrer.fachrichtung.LehrerLehramtFachrichtungRepository;
import de.svws_nrw.repo.lehrer.fachrichtung.LehrerLehramtFachrichtungRepositoryImpl;
import de.svws_nrw.repo.lehrer.lehrbefaehigung.LehrerLehramtLehrbefaehigungenRepository;
import de.svws_nrw.repo.lehrer.lehrbefaehigung.LehrerLehramtLehrbefaehigungenRepositoryImpl;
import de.svws_nrw.repo.lehrer.leitungsfunktion.LehrerLeitungsfunktionRepository;
import de.svws_nrw.repo.lehrer.leitungsfunktion.LehrerLeitungsfunktionRepositoryImpl;
import de.svws_nrw.repo.lehrer.mehrleistung.LehrerMehrleistungRepository;
import de.svws_nrw.repo.lehrer.mehrleistung.LehrerMehrleistungRepositoryImpl;
import de.svws_nrw.repo.lehrer.minderleistung.LehrerMinderleistungRepository;
import de.svws_nrw.repo.lehrer.minderleistung.LehrerMinderleistungRepositoryImpl;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepositoryImpl;
import de.svws_nrw.repo.lehrer.unterrichtsfach.LehrerUnterrichtsfachRepository;
import de.svws_nrw.repo.lehrer.unterrichtsfach.LehrerUnterrichtsfachRepositoryImpl;

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
	public LehrerPersonalabschnittsdatenRepository getLehrerPersonalabschnittsdatenRepository() {
		return getOrCreate(LehrerPersonalabschnittsdatenRepository.class, () -> new LehrerPersonalabschnittsdatenRepositoryImpl(conn));
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
	public LehrerLehramtRepository getLehrerPersonaldatenLehramtRepository() {
		return getOrCreate(LehrerLehramtRepository.class, () -> new LehrerLehramtRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrerPersonaldatenLehramtFachrichtung}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerLehramtFachrichtungRepository getLehrerPersonaldatenLehramtFachrichtungRepository() {
		return getOrCreate(LehrerLehramtFachrichtungRepository.class, () -> new LehrerLehramtFachrichtungRepositoryImpl(conn));
	}


	/**
	 * Erstellt ein neues Repository für {@link DTOLehrerPersonaldatenLehramtBefaehigung}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerLehramtLehrbefaehigungenRepository getLehrerPersonaldatenLehramtLehrbefaehigungenRepository() {
		return getOrCreate(LehrerLehramtLehrbefaehigungenRepository.class, () -> new LehrerLehramtLehrbefaehigungenRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOLeitungsfunktion}.
	 *
	 * @return das Repository-Objekt
	 */
	public LehrerLeitungsfunktionRepository getLeitungsfunktionRepository() {
		return this.getOrCreate(LehrerLeitungsfunktionRepository.class, () -> new LehrerLeitungsfunktionRepositoryImpl(this.conn));
	}

	/**
	 * @return {@link LehrerFunktionRepository}
	 */
	public LehrerFunktionRepository getLehrerFunktionRepository() {
		return this.getOrCreate(LehrerFunktionRepository.class, () -> new LehrerFunktionRepositoryImpl(this.conn));
	}

}
