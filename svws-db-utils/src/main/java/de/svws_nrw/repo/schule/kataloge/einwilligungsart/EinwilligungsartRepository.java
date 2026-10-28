package de.svws_nrw.repo.schule.kataloge.einwilligungsart;

import java.util.List;

import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.db.dto.current.schild.katalog.DTOEinwilligungsart;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;

/**
 * Das Interface für ein Repository zum Zugriff auf den Einwilligungsarten-Katalog der SVWS-Datenbank
 */
public interface EinwilligungsartRepository extends ReferencedBulkDeletionRepository<DTOEinwilligungsart> {

	/**
	 * Bestimmt die IDs aller Einwilligungsarten, die für den übergebenen Personentyp gelten.
	 *
	 * @param personTyp   der Personentyp
	 *
	 * @return die aufsteigend sortierten IDs der Einwilligungsarten, leer wenn keine vorhanden sind
	 */
	List<Long> getAllIdsByPersonTyp(PersonTyp personTyp);

	/**
	 * Prüft, ob eine Einwilligungsart mit der übergebenen ID für den übergebenen Personentyp existiert.
	 *
	 * @param idEinwilligungsart   die ID der Einwilligungsart
	 * @param personTyp            der Personentyp
	 *
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsByIdAndPersonTyp(long idEinwilligungsart, PersonTyp personTyp);

	/**
	 * Prüft, ob eine Einwilligungsart mit der angegebenen Bezeichnung für den Personentyp bereits in der Datenbank existiert.
	 * Die Prüfung erfolgt case-insensitiv.
	 *
	 * @param bezeichnung   die zu prüfende Bezeichnung
	 * @param personTyp     der Personentyp, für den die Eindeutigkeit gilt
	 *
	 * @return {@code true}, wenn eine Einwilligungsart mit dieser Bezeichnung für den Personentyp existiert, sonst {@code false}
	 */
	boolean bezeichnungIsAlreadyUsedCreate(String bezeichnung, PersonTyp personTyp);

	/**
	 * Prüft, ob eine Einwilligungsart mit der angegebenen Bezeichnung für den Personentyp bereits in der Datenbank existiert,
	 * wobei die Einwilligungsart mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv.
	 * <p>
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass ein Objekt
	 * sich selbst als Duplikat erkennt.
	 *
	 * @param bezeichnung   die zu prüfende Bezeichnung
	 * @param personTyp     der Personentyp, für den die Eindeutigkeit gilt
	 * @param id            die ID der Einwilligungsart, die von der Prüfung ausgeschlossen werden soll
	 *
	 * @return {@code true}, wenn eine andere Einwilligungsart mit dieser Bezeichnung für den Personentyp existiert, sonst {@code false}
	 */
	boolean bezeichnungIsAlreadyUsedPatch(String bezeichnung, PersonTyp personTyp, long id);

	/**
	 * Prüft, ob eine Einwilligungsart mit dem angegebenen Schlüssel für den Personentyp bereits in der Datenbank existiert.
	 * Die Prüfung erfolgt case-insensitiv.
	 *
	 * @param schluessel   der zu prüfende Schlüssel
	 * @param personTyp    der Personentyp, für den die Eindeutigkeit gilt
	 *
	 * @return {@code true}, wenn eine Einwilligungsart mit diesem Schlüssel für den Personentyp existiert, sonst {@code false}
	 */
	boolean schluesselIsAlreadyUsedCreate(String schluessel, PersonTyp personTyp);

	/**
	 * Prüft, ob eine Einwilligungsart mit dem angegebenen Schlüssel für den Personentyp bereits in der Datenbank existiert,
	 * wobei die Einwilligungsart mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv.
	 * <p>
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass ein Objekt
	 * sich selbst als Duplikat erkennt.
	 *
	 * @param schluessel   der zu prüfende Schlüssel
	 * @param personTyp    der Personentyp, für den die Eindeutigkeit gilt
	 * @param id           die ID der Einwilligungsart, die von der Prüfung ausgeschlossen werden soll
	 *
	 * @return {@code true}, wenn eine andere Einwilligungsart mit diesem Schlüssel für den Personentyp existiert, sonst {@code false}
	 */
	boolean schluesselIsAlreadyUsedPatch(String schluessel, PersonTyp personTyp, long id);

}
