package de.svws_nrw.repo.schule.kataloge.ort;

import java.util.List;
import java.util.Set;

import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.repo.Repository;

/**
 * Das Interface für ein Repository zum Zugriff auf die Orte-Tabelle der SVWS-Datenbank
 */
public interface OrtRepository extends Repository<DTOOrt> {

	/**
	 *  Liefert Ids die in anderen Tabellen referenziert werden.
	 * @param idsToCheck zu überprüfende Ids
	 * @return Liste referenzierter ids
	 */
	Set<Long> getReferencedIds(List<Long> idsToCheck);

	/**
	 * Gibt zurück, ob der Ortsname für die gegebene Postleitzahl noch nicht vergeben ist.
	 * @param ortsname der Name des Orts.
	 * @param plz die Postleitzahl des Orts.
	 * @return true falls der Ortsname für die PLZ noch nicht existiert, ansonsten false
	 */
	boolean ortsnameIsUniqueForPlzCreate(String ortsname, String plz);

	/**
	 * Gibt zurück, ob der Ortsname für die gegebene Postleitzahl noch nicht vergeben ist,
	 * wobei der Ort mit der angegebenen ID ausgeschlossen wird.
	 * @param ortsname der Name des Orts.
	 * @param plz die Postleitzahl des Orts.
	 * @param idOrt die ID des zu aktualisierenden Orts.
	 * @return true falls kein anderer Ort den Ortsnamen für die PLZ verwendet, ansonsten false
	 */
	boolean ortsnameIsUniqueForPlzPatch(String ortsname, String plz, long idOrt);

}
