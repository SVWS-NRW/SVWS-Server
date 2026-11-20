package de.svws_nrw.repo.schule.kataloge.fahrschuelerart;

import java.util.Collection;
import java.util.Set;

import de.svws_nrw.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Fahrschuelerart-Tabelle der SVWS-Datenbank.
 */
public interface FahrschuelerartRepository extends ReferencedBulkDeletionRepository<DTOFahrschuelerart> {

	/**
	 * Prüft, ob eine Fahrschülerart mit der angegebenen Bezeichnung bereits in der Datenbank existiert.
	 * Die Prüfung erfolgt case-insensitiv.
	 *
	 * @param bezeichnung die zu prüfende Bezeichnung
	 *
	 * @return {@code true}, wenn eine Fahrschülerart mit dieser Bezeichnung existiert, sonst {@code false}
	 */
	boolean bezeichnungIsAlreadyUsedCreate(String bezeichnung);

	/**
	 * Prüft, ob eine Fahrschülerart mit der angegebenen Bezeichnung bereits in der Datenbank existiert,
	 * wobei die Fahrschülerart mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv.
	 * <p>
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass ein Objekt
	 * sich selbst als Duplikat erkennt.
	 *
	 * @param bezeichnung die zu prüfende Bezeichnung
	 * @param id die ID der Fahrschülerart, die von der Prüfung ausgeschlossen werden soll
	 *
	 * @return {@code true}, wenn eine andere Fahrschülerart mit dieser Bezeichnung existiert, sonst {@code false}
	 */
	boolean bezeichnungIsAlreadyUsedPatch(String bezeichnung, long id);

	/**
	 * Prüft, ob eine Fahrschülerart mit der angegebenen ID in der Datenbank existiert.
	 *
	 * @param idFahrschuelerart die ID der Fahrschülerart
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idFahrschuelerart);

	/**
	 * Gibt die Menge der IDs zurück, für die eine Fahrschülerart in der Datenbank existiert.
	 *
	 * @param ids die zu prüfenden IDs
	 * @return Menge der gefundenen IDs
	 */
	Set<Long> existsByIds(Collection<Long> ids);

}
