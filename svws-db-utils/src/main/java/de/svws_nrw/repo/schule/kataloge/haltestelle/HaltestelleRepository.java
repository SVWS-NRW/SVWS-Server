package de.svws_nrw.repo.schule.kataloge.haltestelle;

import java.util.Collection;
import java.util.Set;

import de.svws_nrw.db.dto.current.schild.katalog.DTOHaltestellen;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Haltestellen-Tabelle der SVWS-Datenbank.
 */
public interface HaltestelleRepository extends ReferencedBulkDeletionRepository<DTOHaltestellen> {

	/**
	 * Prüft, ob eine Haltestelle mit der angegebenen Bezeichnung bereits in der Datenbank existiert.
	 * Die Prüfung erfolgt case-insensitiv.
	 *
	 * @param bezeichnung die zu prüfende Bezeichnung
	 *
	 * @return {@code true}, wenn eine Haltestelle mit dieser Bezeichnung existiert, sonst {@code false}
	 */
	boolean bezeichnungExists(String bezeichnung);

	/**
	 * Prüft, ob eine Haltestelle mit der angegebenen Bezeichnung bereits in der Datenbank existiert,
	 * wobei die Haltestelle mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv.
	 * <p>
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass ein Objekt
	 * sich selbst als Duplikat erkennt.
	 *
	 * @param bezeichnung die zu prüfende Bezeichnung
	 * @param id die ID der Haltestelle, die von der Prüfung ausgeschlossen werden soll
	 *
	 * @return {@code true}, wenn eine andere Haltestelle mit dieser Bezeichnung existiert, sonst {@code false}
	 */
	boolean bezeichnungIstBereitsVergebenExceptId(String bezeichnung, long id);

	/**
	 * Prüft, ob eine Haltestelle mit der angegebenen ID in der Datenbank existiert.
	 *
	 * @param idHaltestelle die ID der Haltestelle
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idHaltestelle);

	/**
	 * Gibt die Menge der IDs zurück, für die eine Haltestelle in der Datenbank existiert.
	 *
	 * @param ids die zu prüfenden IDs
	 * @return Menge der gefundenen IDs
	 */
	Set<Long> existsByIds(Collection<Long> ids);

}
