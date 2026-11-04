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
	 * Erstellt ein neues Repository für die aggregierten Informationen zu den Jahrgängen der gymnasialen Oberstufe.
	 *
	 * @return das Repository-Objekt
	 */
	public GostJahrgangsinformationenRepository getGostJahrgangsinformationenRepository() {
		return getOrCreate(GostJahrgangsinformationenRepository.class, () -> new GostJahrgangsinformationenRepositoryImpl(conn));
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

	/**
	 * Gibt das Repository für die Wiederholungen von Schülern im folgenden Abiturjahrgang zurück.
	 *
	 * @return das Repository
	 */
	public GostSchuelerWiederholungRepository getGostSchuelerWiederholungRepository() {
		return getOrCreate(GostSchuelerWiederholungRepository.class, () -> new GostSchuelerWiederholungRepositoryImpl(conn));
	}

	/**
	 * Gibt das Repository für die Fachwahlen zurück, die Schüler für den Abiturjahrgang ihrer Wiederholung planen.
	 *
	 * @return das Repository
	 */
	public GostSchuelerWiederholungFachwahlenRepository getGostSchuelerWiederholungFachwahlenRepository() {
		return getOrCreate(GostSchuelerWiederholungFachwahlenRepository.class, () -> new GostSchuelerWiederholungFachwahlenRepositoryImpl(conn));
	}

	/**
	 * Gibt das Repository für die Fachwahlen der Laufbahnplanung zurück. Bei Schülern mit Wiederholung greift es auf die
	 * Fachwahlen der Wiederholung zu, sonst auf die Fachwahlen der aktuellen Planung.
	 *
	 * @return das Repository
	 */
	public GostSchuelerFachbelegungenRepository getGostSchuelerPlanungFachbelegungenRepository() {
		return new GostSchuelerPlanungFachbelegungenRepositoryImpl(getGostSchuelerFachbelegungenRepository(),
				getGostSchuelerWiederholungFachwahlenRepository(), getGostSchuelerWiederholungRepository());
	}

	/**
	 * Erstellt ein neues Repository für den Import aus Gost-Kursblockungen.
	 *
	 * @return das Repository-Objekt
	 */
	public GostKursblockungImportRepository getGostKursblockungImportRepository() {
		return getOrCreate(GostKursblockungImportRepository.class, () -> new GostKursblockungImportRepositoryImpl(conn));
	}

}
