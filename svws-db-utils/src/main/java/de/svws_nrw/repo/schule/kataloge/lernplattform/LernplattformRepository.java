package de.svws_nrw.repo.schule.kataloge.lernplattform;

import java.util.List;

import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.repo.ReferencedBulkDeletionRepository;

/**
 * Das Interface für ein Repository zum Zugriff auf den Lernplattformen-Katalog der SVWS-Datenbank
 */
public interface LernplattformRepository extends ReferencedBulkDeletionRepository<DTOLernplattformen> {

	/**
	 * Bestimmt die IDs aller Lernplattformen des Katalogs.
	 *
	 * @return die aufsteigend sortierten IDs aller Lernplattformen, leer wenn keine vorhanden sind
	 */
	List<Long> getAllIds();

	/**
	 * Prüft, ob eine Lernplattform mit der übergebenen ID existiert.
	 *
	 * @param idLernplattform   die ID der Lernplattform
	 *
	 * @return {@code true}, wenn ein Eintrag gefunden wurde, sonst {@code false}
	 */
	boolean existsById(Long idLernplattform);

	/**
	 * Prüft, ob eine Lernplattform mit der angegebenen Bezeichnung bereits in der Datenbank existiert.
	 * Die Prüfung erfolgt case-insensitiv.
	 *
	 * @param bezeichnung   die zu prüfende Bezeichnung
	 *
	 * @return {@code true}, wenn eine Lernplattform mit dieser Bezeichnung existiert, sonst {@code false}
	 */
	boolean bezeichnungIsAlreadyUsedCreate(String bezeichnung);

	/**
	 * Prüft, ob eine Lernplattform mit der angegebenen Bezeichnung bereits in der Datenbank existiert,
	 * wobei die Lernplattform mit der angegebenen ID von der Prüfung ausgeschlossen wird.
	 * Die Prüfung erfolgt case-insensitiv.
	 * <p>
	 * Diese Methode ist für PATCH-Operationen gedacht, um zu verhindern, dass ein Objekt
	 * sich selbst als Duplikat erkennt.
	 *
	 * @param bezeichnung   die zu prüfende Bezeichnung
	 * @param id            die ID der Lernplattform, die von der Prüfung ausgeschlossen werden soll
	 *
	 * @return {@code true}, wenn eine andere Lernplattform mit dieser Bezeichnung existiert, sonst {@code false}
	 */
	boolean bezeichnungIsAlreadyUsedPatch(String bezeichnung, long id);

}
