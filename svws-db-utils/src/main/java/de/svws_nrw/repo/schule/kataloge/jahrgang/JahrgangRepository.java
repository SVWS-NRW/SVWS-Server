package de.svws_nrw.repo.schule.kataloge.jahrgang;

import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Jahrgangs-Tabelle der SVWS-Datenbank
 */
public interface JahrgangRepository extends ReferencedBulkDeletionRepository<DTOJahrgang> {

	/**
	 * @param idJahrgang {@link Long}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idJahrgang);

	/**
	 * Prüft, ob ein Jahrgang mit dem angegebenen Kürzel bereits in der Datenbank existiert.
	 * Die Prüfung erfolgt case-insensitiv.
	 *
	 * @param kuerzel das zu prüfende Kürzel
	 *
	 * @return {@code true}, wenn ein Jahrgang mit diesem Kürzel existiert, sonst {@code false}
	 */
	boolean kuerzelIsAlreadyUsedCreate(String kuerzel);

	/**
	 * Prüft, ob ein Jahrgang mit der angegebenen Bezeichnung bereits in der Datenbank existiert.
	 * Die Prüfung erfolgt case-insensitiv.
	 *
	 * @param bezeichnung die zu prüfende Bezeichnung
	 *
	 * @return {@code true}, wenn ein Jahrgang mit dieser Bezeichnung existiert, sonst {@code false}
	 */
	boolean bezeichnungIsAlreadyUsedCreate(String bezeichnung);

	/**
	 * Prüft, ob ein Jahrgang mit dem angegebenen Kürzel bereits in der Datenbank existiert,
	 * wobei der Jahrgang mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv.
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass ein Objekt
	 * sich selbst als Duplikat erkennt.
	 *
	 * @param kuerzel das zu prüfende Kürzel (Kurztext)
	 * @param id die ID des Jahrgangs, das von der Prüfung ausgeschlossen werden soll
	 * @return {@code true}, wenn ein anderer Jahrgang mit diesem Kürzel existiert, {@code false} sonst
	 */
	boolean kuerzelIsAlreadyUsedPatch(String kuerzel, long id);

	/**
	 * Prüft, ob ein Jahrgang mit der angegebenen Bezeichnung bereits in der Datenbank existiert,
	 * wobei der Jahrgang mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv.
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass ein Objekt
	 * sich selbst als Duplikat erkennt.
	 *
	 * @param bezeichnung die zu prüfende Bezeichnung
	 * @param id die ID des Jahrgangs, das von der Prüfung ausgeschlossen werden soll
	 * @return {@code true}, wenn ein anderer Jahrgang mit dieser Bezeichnung existiert, {@code false} sonst
	 */
	boolean bezeichnungIsAlreadyUsedPatch(String bezeichnung, long id);
}
