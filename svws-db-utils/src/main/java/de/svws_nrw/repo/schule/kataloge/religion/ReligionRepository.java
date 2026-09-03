package de.svws_nrw.repo.schule.kataloge.religion;

import java.util.Collection;
import java.util.Set;

import de.svws_nrw.db.dto.current.schild.katalog.DTOReligion;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Religions-Tabelle der SVWS-Datenbank
 */
public interface ReligionRepository extends ReferencedBulkDeletionRepository<DTOReligion> {

	/**
	 * Prüft, ob eine Religion mit der angegeben Bezeichnung bereits in der Datenbank existiert.
	 * Die Prüfung erfolgt case-insensitiv.
	 *
	 * @param bezeichnung die zu prüfende Bezeichnung
	 *
	 * @return {@code true}, wenn eine Religion mit diesem Kürzel existiert, sonst {@code false}
	 */
	boolean bezeichnungIstBereitsVergeben(String bezeichnung);

	/**
	 * Prüft, ob eine Religion mit der angegeben Bezeichnung bereits in der Datenbank existiert,
	 * wobei die Religion mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv.
	 * <p>
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass ein Objekt
	 * sich selbst als Duplikat erkennt.
	 *
	 * @param bezeichnung die zu prüfende Bezeichnung
	 * @param id die ID der Fachklasse, das von der Prüfung ausgeschlossen werden soll
	 *
	 * @return {@code true}, wenn ein andere Religion mit diesem Kürzel existiert, sonst {@code false}
	 */
	boolean bezeichnungIstBereitsVergebenExceptId(String bezeichnung, long id);

	/**
	 * @param idReligion {@link Long}
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idReligion);

	/**
	 * Gibt die Menge der IDs zurück, für die eine Religion in der Datenbank existiert.
	 *
	 * @param ids die zu prüfenden IDs
	 * @return Menge der gefundenen IDs
	 */
	Set<Long> existsByIds(Collection<Long> ids);

}
