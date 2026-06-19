package de.svws_nrw.repo.gost;

import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangBeratungslehrer;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.repo.RepositoryFactory;

/**
 * Eine Factory zum Erstellen von Repositories für den Bereich der Gymnasialen Oberstufe.
 */
public final class GostRepositoryFactory extends RepositoryFactory {

	/**
	 * Erstellt eine neue Factory-Instanz
	 *
	 * @return die neue Factory
	 */
	public static GostRepositoryFactory getNewInstance() {
		return new GostRepositoryFactory();
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOGostJahrgangBeratungslehrer}.
	 *
	 * @return das Repository-Objekt
	 */
	public GostJahrgangBeratungslehrerRepository getGostJahrgangBeratungslehrerRepository() {
		return getOrCreate(GostJahrgangBeratungslehrerRepository.class, () -> new GostJahrgangBeratungslehrerRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOGostJahrgangFachbelegungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public GostJahrgangFachbelegungenRepository getGostJahrgangFachbelegungenRepository() {
		return getOrCreate(GostJahrgangFachbelegungenRepository.class, () -> new GostJahrgangFachbelegungenRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOGostJahrgangFachkombinationen}.
	 *
	 * @return das Repository-Objekt
	 */
	public GostJahrgangFachkombinationenRepository getGostJahrgangFachkombinationenRepository() {
		return getOrCreate(GostJahrgangFachkombinationenRepository.class, () -> new GostJahrgangFachkombinationenRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOGostJahrgangFaecher}.
	 *
	 * @return das Repository-Objekt
	 */
	public GostJahrgangFaecherRepository getGostJahrgangFaecherRepository() {
		return getOrCreate(GostJahrgangFaecherRepository.class, () -> new GostJahrgangFaecherRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOGostJahrgangsdaten}.
	 *
	 * @return das Repository-Objekt
	 */
	public GostJahrgangsdatenRepository getGostJahrgangsdatenRepository() {
		return getOrCreate(GostJahrgangsdatenRepository.class, () -> new GostJahrgangsdatenRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOGostSchueler}.
	 *
	 * @return das Repository-Objekt
	 */
	public GostSchuelerRepository getGostSchuelerRepository() {
		return getOrCreate(GostSchuelerRepository.class, () -> new GostSchuelerRepositoryImpl(conn));
	}

	/**
	 * Erstellt ein neues Repository für {@link DTOGostSchuelerFachbelegungen}.
	 *
	 * @return das Repository-Objekt
	 */
	public GostSchuelerFachbelegungenRepository getGostSchuelerFachbelegungenRepository() {
		return getOrCreate(GostSchuelerFachbelegungenRepository.class, () -> new GostSchuelerFachbelegungenRepositoryImpl(conn));
	}

}
