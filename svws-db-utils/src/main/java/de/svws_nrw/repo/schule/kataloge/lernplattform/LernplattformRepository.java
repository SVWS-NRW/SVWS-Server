package de.svws_nrw.repo.schule.kataloge.lernplattform;

import java.util.List;

import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf den Lernplattformen-Katalog der SVWS-Datenbank
 */
public interface LernplattformRepository extends Repository<DTOLernplattformen> {

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

}
